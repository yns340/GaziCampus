package com.example.myapplication3;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ClubsFragment extends Fragment implements ClubAdapter.OnKulupClickListener {

    private RecyclerView recyclerView;
    private EditText searchEditText;
    private ClubAdapter adapter;

    private List<GenericKulup> tumKulupler;
    private List<GenericKulup> filtrelenmisKulupler;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_clubs, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.kuluplerRecyclerView);
        searchEditText = view.findViewById(R.id.searchEditText);

        Database db = new Database(requireContext());
        tumKulupler = new ArrayList<>();

        // tüm kulüpler getiriliyor
        for (Kulup k : db.tumKulupleriGetir()) {
            if (k instanceof GenericKulup) {
                tumKulupler.add((GenericKulup) k);
            }
        }

        filtrelenmisKulupler = new ArrayList<>(tumKulupler);
        adapter = new ClubAdapter(filtrelenmisKulupler, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapter);

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filtreleKulupListesi(s.toString());
            }
        });
    }

    private void filtreleKulupListesi(String query) {
        filtrelenmisKulupler.clear();
        for (GenericKulup kulup : tumKulupler) {
            if (kulup.getAd().toLowerCase().contains(query.toLowerCase())) {
                filtrelenmisKulupler.add(kulup);
            }
        }
        adapter.notifyDataSetChanged();
    }


    @Override
    public void onKulupClick(GenericKulup kulup) {
        Bundle bundle = new Bundle();
        bundle.putInt("kulup_id", kulup.getId()); // ✅ bu satır çalışmalı!

        KulupSayfasiFragment fragment = new KulupSayfasiFragment();
        fragment.setArguments(bundle);

        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .addToBackStack(null)
                .commit();
    }

}