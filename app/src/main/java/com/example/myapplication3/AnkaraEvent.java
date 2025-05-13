package com.example.myapplication3;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class AnkaraEvent implements Serializable {
    private String name;
    private String content;
    private Venue venue;
    @SerializedName("poster_url") // API'deki poster_url ile eşleşmesini sağlıyoruz

    private String posterUrl; // Poster URL'si

    private String start;

    // Constructor
    public AnkaraEvent(String name, String content, Venue venue) {
        this.name = name;
        this.content = content;
        this.venue = venue;
    }

    // Getter ve Setter metotları
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Venue getVenue() {
        return venue;
    }

    public void setVenue(Venue venue) {
        this.venue = venue;
    }

    public static class Venue implements Serializable {
        private String address;

        public Venue(String address) {
            this.address = address;
        }

        public String getAddress() {
            return address;
        }
    }

    public String getPosterUrl(){
        return posterUrl;
    }

    public String getStart(){
        return start;
    }
}
