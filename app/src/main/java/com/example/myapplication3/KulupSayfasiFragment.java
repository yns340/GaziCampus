package com.example.myapplication3;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.widget.TextView;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class KulupSayfasiFragment extends Fragment implements EtkinlikClickListener {

    private RecyclerView recyclerView;
    private EtkinlikAdapter adapter;

    public KulupSayfasiFragment() {
        // Boş constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_kulupsayfasi, container, false);

        TextView kulupAdiTextView = view.findViewById(R.id.kulupId);
        TextView kulupAciklamaTextView = view.findViewById(R.id.kulupAciklama);
        ImageView kulupLogo = view.findViewById(R.id.kulupLogo);
        recyclerView = view.findViewById(R.id.etkinlikRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        int kulupId = getArguments() != null ? getArguments().getInt("kulup_id", -1) : -1;
        Log.d("DEBUG", "Gelen kulup_id: " + kulupId);

        if (kulupId == -1) {
            kulupAdiTextView.setText("Hata");
            kulupAciklamaTextView.setText("Kulüp ID'si alınamadı.");
            kulupLogo.setImageResource(R.drawable.default_logo);
            return view;
        }

        Database dbHelper = new Database(getContext());
        GenericKulup kulup = dbHelper.getKulupById(kulupId);

        if (kulup != null) {
            kulupAdiTextView.setText(kulup.getAd());
            kulupAciklamaTextView.setText(kulup.getAciklama());

            // ❗ Değiştirilen satır
            if (kulup.getLogo() != null) {
                kulupLogo.setImageBitmap(kulup.getLogo());
            } else {
                kulupLogo.setImageResource(R.drawable.default_logo);
            }

            List<GenericEtkinlik> etkinlikler = new ArrayList<>();
            for (Etkinlik e : kulup.getEtkinlikler()) {
                if (e instanceof GenericEtkinlik) {
                    etkinlikler.add((GenericEtkinlik) e);
                }
            }

            adapter = new EtkinlikAdapter(etkinlikler, this);
            recyclerView.setAdapter(adapter);

        } else {
            kulupAdiTextView.setText("Kulüp Bulunamadı");
            kulupAciklamaTextView.setText("Veritabanında kayıtlı kulüp bilgisi yok.");
            kulupLogo.setImageResource(R.drawable.default_logo);
        }

        return view;
    }

    @Override
    public void onEtkinlikClick(int etkinlikId) {
        Database dbHelper = new Database(getContext());
        GenericEtkinlik etkinlik = dbHelper.getEtkinlikler(etkinlikId);

        if (etkinlik != null) {
            etkinlik.yukleAfisBitmap(getContext()); // zaten bitmap yüklüyor

            Bundle args = new Bundle();
            args.putString("etkinlikAdi", etkinlik.getAd());
            args.putParcelable("etkinlikAfisBitmap", etkinlik.getAfisBitmap()); // değiştirildi

            EtkinlikDetayFragment detayFragment = new EtkinlikDetayFragment();
            detayFragment.setArguments(args);

            getParentFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, detayFragment)
                    .addToBackStack(null)
                    .commit();
        } else {
            Toast.makeText(getContext(), "Etkinlik bulunamadı.", Toast.LENGTH_SHORT).show();
        }
    }
}
