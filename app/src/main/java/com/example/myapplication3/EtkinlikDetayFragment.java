package com.example.myapplication3;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class EtkinlikDetayFragment extends Fragment {

    public EtkinlikDetayFragment() {
        // Boş constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_etkinlik_detay, container, false);

        TextView etkinlikAdi = view.findViewById(R.id.detayEtkinlikAdi);
        ImageView etkinlikAfis = view.findViewById(R.id.detayEtkinlikImage);

        Bundle args = getArguments();
        if (args != null) {
            String ad = args.getString("etkinlikAdi", "Etkinlik");
            Bitmap afisBitmap = args.getParcelable("etkinlikAfisBitmap");

            etkinlikAdi.setText(ad);
            if (afisBitmap != null) {
                etkinlikAfis.setImageBitmap(afisBitmap);
            } else {
                etkinlikAfis.setImageResource(R.drawable.afis);
            }
        }

        return view;
    }

}
