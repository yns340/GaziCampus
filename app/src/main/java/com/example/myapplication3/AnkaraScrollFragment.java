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

import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class AnkaraScrollFragment extends Fragment {

    private LinearLayout tabContainer;
    private ArrayList<Place> placesList;
    private ArrayList<Place> foodsList;
    private ArrayList<Place> itemList;


    public AnkaraScrollFragment() {
        // Boş constructor (zorunlu)
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Fragment için layout'u bağla
        View view = inflater.inflate(R.layout.fragment_ankara_scroll, container, false);

        tabContainer = view.findViewById(R.id.tabContainer); // root view üzerinden eriş

        ImageButton homeButton = view.findViewById(R.id.HomeButton); // HomeButton

        homeButton.setOnClickListener(v -> {
            Fragment newFragment = new DuyuruFragment(); // DuyuruFragment'ını yükle
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, newFragment) // fragmentContainer ID'si
                    .addToBackStack(null) // Geri gelmek için stack'e ekle
                    .commit();

            BottomNavigationView bottomNavigationView = requireActivity().findViewById(R.id.bottom_navigation);
            bottomNavigationView.setSelectedItemId(R.id.nav_anasayfa);
        });

        itemList = new ArrayList<>();

        // Bundle'dan verileri al
        if (getArguments() != null) {
            // Check for placesList first
            if (getArguments().containsKey("placesList")) {
                itemList = (ArrayList<Place>) getArguments().getSerializable("placesList");
            }
            // Then check for foodsList
            else if (getArguments().containsKey("foodsList")) {
                itemList = (ArrayList<Place>) getArguments().getSerializable("foodsList");
            } else {
                // If neither is found, initialize an empty list to avoid null pointer
                itemList = new ArrayList<>();
            }
        } else {
            // If no arguments, initialize an empty list
            itemList = new ArrayList<>();
        }


        createTabs(inflater);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        tabContainer.removeAllViews();
        createTabs(LayoutInflater.from(requireContext()));
    }


    private void createTabs(LayoutInflater inflater) {
        if (itemList != null && !itemList.isEmpty()) {
            for (Place place : itemList) {
                View tabView = inflater.inflate(R.layout.fragment_ankara_tabs, tabContainer, false);

                TextView titleTextView = tabView.findViewById(R.id.textView);
                RatingBar ratingBar = tabView.findViewById(R.id.ratingBar);
                ImageView imageView = tabView.findViewById(R.id.imageView);

                titleTextView.setText(place.getTitle());
                ratingBar.setRating((float) place.getRating());
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
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("place", place);

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

