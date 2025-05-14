package com.example.myapplication3;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class FavorilerFragment extends Fragment {

    LinearLayout favorilerContainer;
    Database dbHelper;
    int userId; // SharedPreferences'ten alınacak

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favoriler, container, false);
        favorilerContainer = view.findViewById(R.id.favorilerContainer);
        dbHelper = new Database(requireContext());

        //  1. SharedPreferences'ten userId'yi al
        SharedPreferences prefs = requireActivity().getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        userId = prefs.getInt("user_id", -1);

        if (userId == -1) {
            Toast.makeText(getContext(), "Kullanıcı oturumu bulunamadı!", Toast.LENGTH_SHORT).show();
        } else {
            gosterFavoriler();  //  2. Favorileri göster
        }

        return view;
    }

    private void gosterFavoriler() {
        List<Duyuru> favoriler = dbHelper.getFavoriler(userId);
        favorilerContainer.removeAllViews();
        LayoutInflater inflater = getLayoutInflater();

        if (favoriler.isEmpty()) {
            TextView bos = new TextView(requireContext());
            bos.setText("Henüz kaydedilen duyurunuz yok.");
            favorilerContainer.addView(bos);
            return;
        }

        for (Duyuru d : favoriler) {
            View duyuruView = inflater.inflate(R.layout.duyuru_item, favorilerContainer, false);
            TextView title = duyuruView.findViewById(R.id.duyuruTitle);
            TextView date = duyuruView.findViewById(R.id.duyuruDate);
            ImageView bookmarkIcon = duyuruView.findViewById(R.id.bookmarkIcon);

            title.setText(d.getTitle());
            date.setText(d.getDate());
            bookmarkIcon.setImageResource(R.drawable.bookmark_filled);

            //  Favoriden çıkarma
            bookmarkIcon.setOnClickListener(v -> {
                dbHelper.favoriSil(userId, d.getTitle());
                gosterFavoriler(); // Listeyi güncelle
            });

            favorilerContainer.addView(duyuruView);
        }
    }
}