package com.example.myapplication3;

public class Ders {
    private int id;
    private String dersAdi;
    private String saat;
    private String aciklama;
    private String gun;
    private boolean tamamlandi;

    public Ders(int id, String dersAdi, String saat, String aciklama, String gun, boolean tamamlandi) {
        this.id = id;
        this.dersAdi = dersAdi;
        this.saat = saat;
        this.aciklama = aciklama;
        this.gun = gun;
        this.tamamlandi = tamamlandi;
    }

    // Getter'lar
    public int getId() {
        return id;
    }

    public String getDersAdi() {
        return dersAdi;
    }

    public String getSaat() {
        return saat;
    }

    public String getAciklama() {
        return aciklama;
    }

    public String getGun() {
        return gun;
    }

    public boolean isTamamlandi() {
        return tamamlandi;
    }

    // Setter'lar
    public void setId(int id) {
        this.id = id;
    }

    public void setDersAdi(String dersAdi) {
        this.dersAdi = dersAdi;
    }

    public void setSaat(String saat) {
        this.saat = saat;
    }

    public void setAciklama(String aciklama) {
        this.aciklama = aciklama;
    }

    public void setGun(String gun) {
        this.gun = gun;
    }

    public void setTamamlandi(boolean tamamlandi) {
        this.tamamlandi = tamamlandi;
    }
}