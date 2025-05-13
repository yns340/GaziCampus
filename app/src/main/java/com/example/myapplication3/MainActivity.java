package com.example.myapplication3;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import android.content.res.ColorStateList;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Bottom navigation view'u bulalım
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Renk seçicisini hem ikon hem de metin için uygulayın
        ColorStateList colorStateList = getResources().getColorStateList(R.color.bottom_nav_item_color, getTheme());
        bottomNavigationView.setItemIconTintList(colorStateList);
        bottomNavigationView.setItemTextColor(colorStateList);

        // İlk fragment'ı yükleyelim
        loadFragment(new DuyuruFragment());
        bottomNavigationView.setSelectedItemId(R.id.nav_anasayfa);

        // Bottom Navigation için listener oluşturalım
        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment fragment = null;

            // Hangi menü öğesine tıklandığını belirleyelim
            int itemId = item.getItemId();

            if (itemId == R.id.nav_kulupler) {
                loadClubFragment(savedInstanceState);
            } else if (itemId == R.id.nav_ders) {
                fragment = new LessonsFragment();
            } else if (itemId == R.id.nav_anasayfa) {
                fragment = new DuyuruFragment();
            } else if (itemId == R.id.nav_yemek) {
                fragment = new FoodsFragment();
            } else if (itemId == R.id.nav_ankara) {
                fragment = new AnkaraFragment();
            }

            return loadFragment(fragment);
        });

    }

    // Fragment yükleme fonksiyonu
    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    private void loadClubFragment(Bundle savedInstanceState){
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
                    .replace(R.id.fragmentContainer, new ClubsFragment())
                    .commit();
        }
    }
}
