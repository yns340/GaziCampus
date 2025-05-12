package com.example.myapplication3;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.io.InputStream;

public class GenericEtkinlik extends Etkinlik {
    private String afisAdresi;
    private int kulupId;
    private Bitmap afisBitmap;

    public GenericEtkinlik(int id, String ad, String afisAdresi, int kulupId) {
        super(id, ad, afisAdresi, kulupId);
        this.afisAdresi = afisAdresi;
        this.kulupId = kulupId;
    }

    public void yukleAfisBitmap(Context context) {
        try {
            InputStream is = context.getAssets().open("images/" + afisAdresi + ".png"); // uzantı sabit değilse ayarla
            afisBitmap = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
            afisBitmap = null;
        }
    }

    public Bitmap getAfisBitmap() {
        return afisBitmap;
    }

    public String getAfisAdresi() {
        return afisAdresi;
    }

    public int getKulupId() {
        return kulupId;
    }
}
