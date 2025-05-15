package com.example.myapplication3;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.ImageButton;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.squareup.picasso.Picasso;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class AnkaraScrollFragment extends Fragment {

    private static final String ARG_EVENT_LIST = "eventList";
    private static final String ARG_PLACES_LIST = "placesList";
    private static final String ARG_FOODS_LIST = "foodsList";
    private LinearLayout tabContainer;
    private ArrayList<Place> placeList;  // placeList ve foodsList, Place nesnesi türünde olacak
    private ArrayList<AnkaraEvent> eventList;

    public AnkaraScrollFragment() {
        // Boş constructor (zorunlu)
    }

    public static AnkaraScrollFragment newInstance(ArrayList<AnkaraEvent> eventList) {
        AnkaraScrollFragment fragment = new AnkaraScrollFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_EVENT_LIST, eventList);  // Event list'ini argüman olarak gönderiyoruz
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Fragment için layout'u bağla
        View view = inflater.inflate(R.layout.fragment_ankara_scroll, container, false);

        tabContainer = view.findViewById(R.id.tabContainer); // root view üzerinden eriş

        placeList = new ArrayList<>();
        eventList = new ArrayList<>();

        // Bundle'dan verileri al
        if (getArguments() != null) {
            // API'den gelen veriler (AnkaraEvent listesi)
            if (getArguments().containsKey(ARG_EVENT_LIST)) {
                eventList = (ArrayList<AnkaraEvent>) getArguments().getSerializable(ARG_EVENT_LIST);
            }
            // placesList (Place nesnelerinden gelen veriler)
            else if (getArguments().containsKey(ARG_PLACES_LIST)) {
                placeList = (ArrayList<Place>) getArguments().getSerializable(ARG_PLACES_LIST);
            }
            // foodsList (Place nesnelerinden gelen veriler)
            else if (getArguments().containsKey(ARG_FOODS_LIST)) {
                placeList = (ArrayList<Place>) getArguments().getSerializable(ARG_FOODS_LIST);
            }
        }

        createTabs(inflater);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        tabContainer.removeAllViews();
        createTabs(LayoutInflater.from(requireContext()));

        Database db = new Database(requireContext());

        if (placeList != null && !placeList.isEmpty()) {
            for (int i = 0; i < tabContainer.getChildCount(); i++) {
                View tabView = tabContainer.getChildAt(i);

                if (tabView != null && i < placeList.size()) {
                    Place place = placeList.get(i);

                    RatingBar ratingBar = tabView.findViewById(R.id.ratingBar);

                    double updatedRating = db.getRating(place.getTitle(), place.getTableName());
                    ratingBar.setRating((float) updatedRating);
                }
            }
        }
    }


    private void createTabs(LayoutInflater inflater) {
        if (eventList != null && !eventList.isEmpty()) {
            for (AnkaraEvent event : eventList) {
                View tabView = inflater.inflate(R.layout.fragment_ankara_tabs, tabContainer, false);

                TextView titleTextView = tabView.findViewById(R.id.textView);
                titleTextView.setText(event.getName()); // 'isim' yerine 'name' kullanıyoruz

                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

                layoutParams.setMargins(25, 25, 25, 25);
                tabView.setLayoutParams(layoutParams);

                // RatingBar ve TextView Content kontrolü
                RatingBar ratingBar = tabView.findViewById(R.id.ratingBar);

                // API'den geldiği için rating yerine contentTextView'i göster
                if (event != null) {
                    ratingBar.setVisibility(View.GONE);
                    TextView date = tabView.findViewById(R.id.dateTextView);
                    String originalDate = event.getStart();

                    try {
                        Date dateObj = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.getDefault()).parse(originalDate);
                        String formattedDate = new SimpleDateFormat("dd/MM/yyyy\nHH:mm", Locale.getDefault()).format(dateObj);
                        date.setText(formattedDate);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    date.setVisibility(View.VISIBLE);
                }

                ImageView poster = tabView.findViewById(R.id.imageView);
                String posterUrl = event.getPosterUrl();
                Picasso.get().load(posterUrl).into(poster);

                tabView.setOnClickListener(v -> {
                    // Yeni bir fragment açarak detayları göster
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("event", event);  // event nesnesini bundle'a ekliyoruz

                    Fragment ankaraPageFragment = new AnkaraItemPageFragment();
                    ankaraPageFragment.setArguments(bundle);  // Bundle'ı fragmente ekle

                    requireActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragmentContainer, ankaraPageFragment)
                            .addToBackStack(null)
                            .commit();
                });

                tabContainer.addView(tabView);
            }
        }
        // Eğer placeList veya foodsList varsa, Place nesnesine göre işlem yap
        else if (placeList != null && !placeList.isEmpty()) {
            for (Place place : placeList) {
                View tabView = inflater.inflate(R.layout.fragment_ankara_tabs, tabContainer, false);

                TextView titleTextView = tabView.findViewById(R.id.textView);
                titleTextView.setText(place.getTitle());

                RatingBar rating = tabView.findViewById(R.id.ratingBar);
                rating.setRating((float)place.getRating());

                ImageView imageView = tabView.findViewById(R.id.imageView);
                if (place.getImagePath() != null) {
                    try {
                        // assets/image/ dosyasındaki resim dosyasını yükle
                        InputStream inputStream = requireContext().getAssets().open(place.getImagePath());
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        imageView.setImageBitmap(bitmap);
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                        imageView.setImageResource(R.drawable.place_holder_image);
                    }
                }

                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

                layoutParams.setMargins(25, 25, 25, 25);
                tabView.setLayoutParams(layoutParams);

                tabView.setOnClickListener(v -> {
                    // Yeni bir fragment açarak detayları göster
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("place", place);  // place nesnesini bundle'a ekliyoruz

                    Fragment ankaraPageFragment = new AnkaraItemPageFragment();
                    ankaraPageFragment.setArguments(bundle);  // Bundle'ı fragmente ekle

                    requireActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragmentContainer, ankaraPageFragment)
                            .addToBackStack(null)
                            .commit();
                });

                tabContainer.addView(tabView);
            }
        }
    }
}

