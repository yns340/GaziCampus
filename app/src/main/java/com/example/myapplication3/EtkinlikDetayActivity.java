package com.example.myapplication3;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class EtkinlikDetayActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_etkinlik_detay);

        ImageView imageView = findViewById(R.id.detayImage);
        TextView titleView = findViewById(R.id.detayTitle);

        String title = getIntent().getStringExtra("title");
        int imageResId = getIntent().getIntExtra("imageResId", R.drawable.default_image);

        titleView.setText(title);
        imageView.setImageResource(imageResId);
    }
}
