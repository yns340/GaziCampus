package com.example.myapplication3;

import android.graphics.Bitmap;
import java.util.List;

public abstract class Kulup {
    private int id;
    private String ad;
    private String aciklama;
    private Bitmap logo;
    private List<Etkinlik> etkinlikler;

    public Kulup(int id, String ad, String aciklama, Bitmap logo) {
        this.id = id;
        this.ad = ad;
        this.aciklama = aciklama;
        this.logo = logo;
    }

    public int getId() { return id; }
    public String getAd() { return ad; }
    public String getAciklama() { return aciklama; }
    public Bitmap getLogo() { return logo; }

    public List<Etkinlik> getEtkinlikler() { return etkinlikler; }
    public void setEtkinlikler(List<Etkinlik> etkinlikler) { this.etkinlikler = etkinlikler; }
}
