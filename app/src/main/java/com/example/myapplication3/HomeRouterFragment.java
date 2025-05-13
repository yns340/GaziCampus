package com.example.myapplication3;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class HomeRouterFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_router, container, false);

        // Veritabanı işlemleri burada
        Database db = new Database(requireContext());

        if (db.tumKulupleriGetir().isEmpty()) {
            int muzikKulubuId = db.kulupEkle("Müzik Kulübü", "muzik_logo", "Her hafta müzik etkinlikleri düzenliyoruz.");
            int acmKulubuId = db.kulupEkle("Acm Gazi", "acm_logo", "ACMIX etkinlikleri düzenliyoruz.");
            int dansKulubuId = db.kulupEkle("Dans Kulübü", "dans_logo", "Her hafta dans eğitimleri veriyoruz.");
            int tiyatroKulubuId = db.kulupEkle("Tiyatro Kulübü", "tiyatro_logo", "her ay tiyatro sahnesinde yer alabilirsiniz.");

            if (muzikKulubuId != -1) {
                db.etkinlikEkle("Konser Gecesi", "konser_afis", muzikKulubuId);
                db.etkinlikEkle("Jazz Atölyesi", "jazz_afis", muzikKulubuId);
            }

            if (acmKulubuId != -1) {
                db.etkinlikEkle("Hackathon", "hackathon_afis", acmKulubuId);
                db.etkinlikEkle("TechTalks", "techtalks_afis", acmKulubuId);
            }

            if (dansKulubuId != -1) {
                db.etkinlikEkle("Salsa Gecesi", "salsa", dansKulubuId);
                db.etkinlikEkle("Zumba Workshop", "zumba", dansKulubuId);
            }

            if (tiyatroKulubuId != -1) {
                db.etkinlikEkle("Romeo ve Juliet", "romeo_afis", tiyatroKulubuId);

            }
        }

        // Ana Fragment'e yönlendir (örneğin ClubsFragment)
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container_view, new ClubsFragment())
                .commit();

        return view;
    }
}
