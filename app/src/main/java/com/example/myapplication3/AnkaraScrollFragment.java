package com.example.myapplication3;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

public class AnkaraScrollFragment extends Fragment {

    private LinearLayout tabContainer;
    private List<TabItem> tabItems = new ArrayList<>();

    public AnkaraScrollFragment() {
        // Boş constructor (zorunlu)
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Fragment için layout'u bağla
        View view = inflater.inflate(R.layout.fragment_ankara_scroll, container, false);

        tabContainer = view.findViewById(R.id.tabContainer); // root view üzerinden eriş

        prepareTabData();
        createTabs(inflater);

        return view;
    }

    private void prepareTabData() {
        tabItems.add(new TabItem("Atakule", 4.5f, R.drawable.atakule));
        tabItems.add(new TabItem("Anıtkabir", 5.0f, R.drawable.anitkabir));
        tabItems.add(new TabItem("Maltepe Camii", 4.5f, R.drawable.maltepecamii));
        tabItems.add(new TabItem("Maltepe Camii", 4.5f, R.drawable.maltepecamii));
        tabItems.add(new TabItem("Maltepe Camii", 4.5f, R.drawable.maltepecamii));
        tabItems.add(new TabItem("Maltepe Camii", 4.5f, R.drawable.maltepecamii));
        tabItems.add(new TabItem("Maltepe Camii", 4.5f, R.drawable.maltepecamii));
        tabItems.add(new TabItem("Maltepe Camii", 4.5f, R.drawable.maltepecamii));
        tabItems.add(new TabItem("Maltepe Camii", 4.5f, R.drawable.maltepecamii));
        tabItems.add(new TabItem("Maltepe Camii", 4.5f, R.drawable.maltepecamii));
        tabItems.add(new TabItem("Maltepe Camii", 4.5f, R.drawable.maltepecamii));
        tabItems.add(new TabItem("Maltepe Camii", 4.5f, R.drawable.maltepecamii));

    }

    private void createTabs(LayoutInflater inflater) {
        for (TabItem item : tabItems) {
            View tabView = inflater.inflate(R.layout.fragment_ankara_tabs, tabContainer, false);

            TextView titleTextView = tabView.findViewById(R.id.textView);
            RatingBar ratingBar = tabView.findViewById(R.id.ratingBar);
            ImageView imageView = tabView.findViewById(R.id.imageView);

            titleTextView.setText(item.getTitle());
            ratingBar.setRating(item.getRating());
            imageView.setImageResource(item.getImageResourceId());

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            layoutParams.setMargins(25, 25, 25, 25);
            tabView.setLayoutParams(layoutParams);

            tabView.setOnClickListener(v -> {
                Fragment ankaraPageFragment = new AnkaraItemPageFragment();  // AnkaraPage artık fragment
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

class TabItem {
    private String title;
    private float rating;
    private int imageResourceId;

    public TabItem(String title, float rating, int imageResourceId) {
        this.title = title;
        this.rating = rating;
        this.imageResourceId = imageResourceId;
    }

    public String getTitle() {
        return title;
    }

    public float getRating() {
        return rating;
    }

    public int getImageResourceId() {
        return imageResourceId;
    }
}