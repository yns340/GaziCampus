package com.example.myapplication3;

import java.io.Serializable;

public class Yemek implements Serializable {
    private String tarih;
    private String ogleYemegi;

    public Yemek(String tarih, String ogleYemegi) {
        this.tarih = tarih;
        this.ogleYemegi = ogleYemegi;
    }

    public String getTarih() {
        return tarih;
    }

    public String getOgleYemegi() {
        return ogleYemegi;
    }
}