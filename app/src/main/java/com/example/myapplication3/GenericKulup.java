package com.example.myapplication3;

import android.graphics.Bitmap;
import java.util.List;

public class GenericKulup extends Kulup {
    public GenericKulup(int id, String ad, String aciklama, Bitmap logo, List<Etkinlik> etkinlikler) {
        super(id, ad, aciklama, logo);
        setEtkinlikler(etkinlikler);
    }
}
