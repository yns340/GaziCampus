package com.example.myapplication3;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Database db = new Database(this);

        // Eğer hiç kulüp yoksa örnek verileri ekle
        if (db.tumKulupleriGetir().isEmpty()) {
            int muzikKulubuId = db.kulupEkle("Müzik Kulübü", "muzik_logo", "Her hafta müzik etkinlikleri düzenliyoruz.");
            int tiyatroKulubuId = db.kulupEkle("Tiyatro Kulübü", "tiyatro_logo", "Tiyatro oyunları sahneliyoruz.");
            int yazilimKulubuId = db.kulupEkle("Kodlama Kulübü", "kodlama_logo", "Yazılım ve teknoloji ile ilgileniyoruz.");

            if (muzikKulubuId != -1) db.etkinlikEkle("Konser Gecesi", "konser_afis", muzikKulubuId);
            if (tiyatroKulubuId != -1) db.etkinlikEkle("Tiyatro Gösterisi", "tiyatro_afis", tiyatroKulubuId);
            if (yazilimKulubuId != -1) db.etkinlikEkle("Hackathon", "hackathon_afis", yazilimKulubuId);
        }

        // ❗❗ BURASI EKLENMELİ: ClubsFragment ilk yüklensin
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container_view, new ClubsFragment())
                    .commit();
        }
    }
}
