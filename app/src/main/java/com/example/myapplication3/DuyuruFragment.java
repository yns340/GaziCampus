package com.example.myapplication3;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.android.material.tabs.TabLayout;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DuyuruFragment extends Fragment {

    LinearLayout duyuruContainer;
    TabLayout tabLayout;
    Map<String, List<Duyuru>> ayDuyurulari = new HashMap<>();
    Database database;

    int userId;
    String[] urls = {
            "https://gazi.edu.tr/view/announcement-list?id=1&type=1&SearchString=&dates=&date=",
            "https://gazi.edu.tr/view/announcement-list?id=2&type=1&SearchString=&dates=&date=",
            "https://gazi.edu.tr/view/announcement-list?id=3&type=1&SearchString=&dates=&date=",
            "https://gazi.edu.tr/view/announcement-list?id=4&type=1&SearchString=&dates=&date=",
            "https://gazi.edu.tr/view/announcement-list?id=5&type=1&SearchString=&dates=&date=",
            "https://gazi.edu.tr/view/announcement-list?id=6&type=1&SearchString=&dates=&date="
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_duyuru, container, false);

        duyuruContainer = view.findViewById(R.id.duyuruContainer);
        tabLayout = view.findViewById(R.id.tabLayout);
        database = new Database(requireContext());

        SharedPreferences prefs = requireContext().getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        userId = prefs.getInt("user_id", -1);

        if (userId == -1) {
            Toast.makeText(getContext(), "Kullanıcı oturumu alınamadı!", Toast.LENGTH_SHORT).show();
            return view;
        }

        ImageButton favorilerButton = view.findViewById(R.id.favori);
        favorilerButton.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, new FavorilerFragment())
                    .addToBackStack(null)
                    .commit();
        });

        tabLayout.addTab(tabLayout.newTab().setText("Mayıs"));
        tabLayout.addTab(tabLayout.newTab().setText("Nisan"));
        tabLayout.addTab(tabLayout.newTab().setText("Mart"));



        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                guncelleDuyurular(tab.getText().toString());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        loadAllAnnouncements();

        return view;
    }

    private void loadAllAnnouncements() {
        new Thread(() -> {
            Map<String, List<Duyuru>> geciciMap = new HashMap<>();

            for (String url : urls) {
                try {
                    Document doc = Jsoup.connect(url).get();
                    Elements duyuruBloklari = doc.select("div[id^=listAnnouncement]");

                    for (Element blok : duyuruBloklari) {
                        Element titleElement = blok.selectFirst("div.subpage-ann-link > a");
                        Element dayElement = blok.selectFirst(".ann-day");
                        Element monthElement = blok.selectFirst(".ann-month");
                        Element yearElement = blok.selectFirst(".ann-year");

                        if (titleElement == null || dayElement == null || monthElement == null)
                            continue;

                        String title = titleElement.text().trim();
                        String gun = dayElement.text().trim();
                        String ay = monthElement.text().trim();
                        String yil = (yearElement != null) ? yearElement.text().trim() : "";

                        String tarih = gun + " " + ay + " " + yil;
                        Duyuru d = new Duyuru(title, tarih, ay);

                        if (!geciciMap.containsKey(ay)) {
                            geciciMap.put(ay, new ArrayList<>());
                        }
                        geciciMap.get(ay).add(d);
                    }

                } catch (Exception e) {
                    Log.e("DUYURU_HATA", "Veri çekilirken hata oluştu: " + e.getMessage());
                }
            }

            ayDuyurulari = geciciMap;

            requireActivity().runOnUiThread(() -> {
                if (isAdded() && getActivity() != null) { //HATA KAYNAĞI (?)
                    if (!isDetached()) { // Make sure the fragment is not detached
                        guncelleDuyurular("Mayıs");
                    }                }
            });
        }).start();
    }

    private void guncelleDuyurular(String secilenAy) {
        duyuruContainer.removeAllViews();
        LayoutInflater inflater = getLayoutInflater();

        List<Duyuru> duyurular = ayDuyurulari.get(secilenAy);
        if (duyurular == null || duyurular.isEmpty()) {
            TextView bosText = new TextView(requireContext());
            bosText.setText(secilenAy + " ayında duyuru bulunamadı.");
            duyuruContainer.addView(bosText);
            return;
        }

        for (Duyuru d : duyurular) {
            View duyuruView = inflater.inflate(R.layout.duyuru_item, duyuruContainer, false);
            TextView titleTextView = duyuruView.findViewById(R.id.duyuruTitle);
            TextView dateTextView = duyuruView.findViewById(R.id.duyuruDate);
            ImageView bookmarkIcon = duyuruView.findViewById(R.id.bookmarkIcon);

            titleTextView.setText(d.getTitle());
            dateTextView.setText(d.getDate());

            boolean favoriMi = database.isFavori(userId, d.getTitle());

            if (favoriMi) {
                bookmarkIcon.setImageResource(R.drawable.bookmark_filled);
            } else {
                bookmarkIcon.setImageResource(R.drawable.bookmark);
            }

            bookmarkIcon.setOnClickListener(v -> {
                if (database.isFavori(userId, d.getTitle())) {
                    database.favoriSil(userId, d.getTitle());
                    bookmarkIcon.setImageResource(R.drawable.bookmark);
                } else {
                    database.favoriEkle(userId, d.getTitle(), d.getDate(), d.getMonth());
                    bookmarkIcon.setImageResource(R.drawable.bookmark_filled);
                }
            });

            duyuruContainer.addView(duyuruView);
        }
    }
}