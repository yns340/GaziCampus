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
                fragment = new ClubsFragment();
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


}
