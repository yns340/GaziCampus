package com.example.myapplication3;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.myapplication3.ui.yemek.YemeklerActivity;

public class MainActivity extends AppCompatActivity {

    private Button yemeklerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // MainActivity'nin layout dosyasını ayarlayın

        yemeklerButton = findViewById(R.id.yemeklerButton); // Butonun ID'sini bulun
        yemeklerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // YemeklerActivity'i başlatmak için Intent oluşturun
                Intent intent = new Intent(MainActivity.this, YemeklerActivity.class);
                startActivity(intent); // YemeklerActivity'i başlatın
            }
        });
    }
}