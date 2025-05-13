package com.example.myapplication3;

public abstract class Etkinlik {
    private int id;
    private String ad;
    private String afisAdresi;
    private int kulupId;

    public Etkinlik(int id, String ad, String afisAdresi, int kulupId) {
        this.id = id;
        this.ad = ad;
        this.afisAdresi = afisAdresi;
        this.kulupId = kulupId;
    }

    public int getId() { return id; }
    public String getAd() { return ad; }
    public String getAfisAdresi() { return afisAdresi; }
    public int getKulupId() { return kulupId; }

    // Soyut metotlar gerekirse buraya eklenebilir
}
