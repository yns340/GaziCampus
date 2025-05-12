package com.example.myapplication3;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.google.android.material.button.MaterialButton;

import java.util.List;

public class LessonsFragment extends Fragment {

    private RecyclerView recyclerView;
    private DersAdapter dersAdapter;
    private MyDatabaseHelper dbHelper;
    private String seciliGun = "Pzt"; // Varsayılan gün

    public LessonsFragment() {
        // Boş kurucu
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_lessons, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dbHelper = new MyDatabaseHelper(getContext());

        recyclerView = view.findViewById(R.id.recyclerViewLessons);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Gün butonları
        MaterialButton btnPzt = view.findViewById(R.id.btnPzt);
        MaterialButton btnSl = view.findViewById(R.id.btnSl);
        MaterialButton btnCars = view.findViewById(R.id.btnCars);
        MaterialButton btnPers = view.findViewById(R.id.btnPers);
        MaterialButton btnCuma = view.findViewById(R.id.btnCuma);
        MaterialButton btnCmt = view.findViewById(R.id.btnCmt);
        MaterialButton btnPz = view.findViewById(R.id.btnPz);
        // İlk yüklemede Pazartesi'yi göster
        loadLessonsForDay(seciliGun);

        // Gün butonlarına tıklanınca liste güncellensin
        btnPzt.setOnClickListener(v -> {
            seciliGun = "Pazartesi";
            loadLessonsForDay(seciliGun);
        });

        btnSl.setOnClickListener(v -> {
            seciliGun = "Salı";
            loadLessonsForDay(seciliGun);
        });

        btnCars.setOnClickListener(v -> {
            seciliGun = "Çarşamba";
            loadLessonsForDay(seciliGun);
        });

        btnPers.setOnClickListener(v -> {
            seciliGun = "Perşembe";
            loadLessonsForDay(seciliGun);
        });

        btnCuma.setOnClickListener(v -> {
            seciliGun = "Cuma";
            loadLessonsForDay(seciliGun);
        });

        btnCmt.setOnClickListener(v -> {
            seciliGun = "Cumartesi";
            loadLessonsForDay(seciliGun);
        });

        btnPz.setOnClickListener(v -> {
            seciliGun = "Pazar";
            loadLessonsForDay(seciliGun);
        });

        // + Butonuna tıklanınca DersEkleActivity'yi başlat, günü ileterek
        ImageButton addButton = view.findViewById(R.id.addButton);
        addButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), DersEkleActivity.class);
            intent.putExtra("gun", seciliGun); // seçili günü gönder
            startActivity(intent);
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        // Sayfa geri dönünce yeniden yükle
        loadLessonsForDay(seciliGun);
    }

    // Seçilen güne ait dersleri yükler
    private void loadLessonsForDay(String gun) {
        List<Ders> dersList = dbHelper.getDerslerByGun(gun);
        dersAdapter = new DersAdapter(dersList);
        recyclerView.setAdapter(dersAdapter);
    }
}