package com.example.myapplication3;
import java.io.Serializable;

public class Place implements Serializable{
    private int id;
    private String title;
    private double rating;
    private String imagePath;

    private String location;

    public Place (int id, String title, double rating, String imagePath, String location) {
        this.id = id;
        this.title = title;
        this.rating = rating;
        this.imagePath = imagePath;
        this.location = location;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public double getRating() {
        return rating;
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getLocation() {
        return location;
    }
}
