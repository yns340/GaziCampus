package com.example.myapplication3;

public class Duyuru {
    private String title;
    private String date; // örn: "7 Mayıs"
    private String month; // örn: "Mayıs"
    private boolean favori;
    public Duyuru(String title, String date, String month) {
        this.title = title;
        this.date = date;
        this.month = month;
    }

    public String getTitle() { return title; }
    public String getDate() { return date; }
    public String getMonth() { return month; }
    public void setFavori(boolean favori) { this.favori = favori; }
}
