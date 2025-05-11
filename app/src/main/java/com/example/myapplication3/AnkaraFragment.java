package com.example.myapplication3;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;


public class AnkaraFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public AnkaraFragment() {
        // Required empty public constructor
    }

    public static AnkaraFragment newInstance(String param1, String param2) {
        AnkaraFragment fragment = new AnkaraFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_ankara, container, false);

        // Butonlara eriş
        Button buttonTravel = view.findViewById(R.id.buttonTravel);
        Button buttonEntertainment = view.findViewById(R.id.buttonEntertainment);
        Button buttonFood = view.findViewById(R.id.buttonFood);

        // Tıklama olayları
        buttonTravel.setOnClickListener(v -> {
            Database database = new Database(requireContext());
            ArrayList<Place> placesList = database.getPlacesData();

            Bundle bundle = new Bundle();
            bundle.putSerializable("placesList", placesList);

            Fragment newFragment = new AnkaraScrollFragment(); // scroll sayfanın fragmentı
            newFragment.setArguments(bundle);

            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, newFragment) // fragmentContainer senin ana sayfandaki FrameLayout veya benzeri bir ID
                    .addToBackStack(null) // geri tuşuna basınca geri gelmek istersen
                    .commit();
        });

        buttonEntertainment.setOnClickListener(v -> {
            Fragment newFragment = new AnkaraScrollFragment(); // scroll sayfanın fragmentı
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, newFragment) // fragmentContainer senin ana sayfandaki FrameLayout veya benzeri bir ID
                    .addToBackStack(null) // geri tuşuna basınca geri gelmek istersen
                    .commit();
        });

        buttonFood.setOnClickListener(v -> {
            Database database = new Database(requireContext());
            ArrayList<Place> foodsList = database.getFoodsData();

            Bundle bundle = new Bundle();
            bundle.putSerializable("foodsList", foodsList);

            Fragment newFragment = new AnkaraScrollFragment(); // scroll sayfanın fragmentı
            newFragment.setArguments(bundle);

            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, newFragment) // fragmentContainer senin ana sayfandaki FrameLayout veya benzeri bir ID
                    .addToBackStack(null) // geri tuşuna basınca geri gelmek istersen
                    .commit();
        });

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


        return view;
    }

}