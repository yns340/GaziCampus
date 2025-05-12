package com.example.myapplication3;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class DersEkleActivity extends AppCompatActivity {

    private EditText editTextDersAdi, editTextSaat, editTextAciklama;
    private Button buttonKaydet;

    private String seciliGun;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ders_ekle);

        // View'ları tanımla
        editTextDersAdi = findViewById(R.id.editTextDersAdi);
        editTextSaat = findViewById(R.id.editTextSaat);
        editTextAciklama = findViewById(R.id.editTextAciklama);
        buttonKaydet = findViewById(R.id.buttonKaydet);

        // LessonsFragment'tan gelen gün bilgisini al
        seciliGun = getIntent().getStringExtra("gun");

        buttonKaydet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dersAdi = editTextDersAdi.getText().toString().trim();
                String saat = editTextSaat.getText().toString().trim();
                String aciklama = editTextAciklama.getText().toString().trim();

                if (dersAdi.isEmpty() || saat.isEmpty()) {
                    Toast.makeText(DersEkleActivity.this, "Lütfen tüm alanları doldurun", Toast.LENGTH_SHORT).show();
                    return;
                }

                Ders yeniDers = new Ders(0, dersAdi, saat, aciklama, seciliGun, false);
                MyDatabaseHelper db = new MyDatabaseHelper(DersEkleActivity.this);
                db.addDers(yeniDers);

                Toast.makeText(DersEkleActivity.this, "Ders eklendi", Toast.LENGTH_SHORT).show();
                finish(); // Geri dön
            }
        });
    }
}