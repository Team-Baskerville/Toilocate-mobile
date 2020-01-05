package com.baskerville.toilocate.classes;

import com.google.android.gms.maps.model.LatLng;

public class Toilet {

    private String name;
    private LatLng location;
    private String gender;
    private float rating;
    private String description;

    public Toilet() {
    }

    public Toilet(String name, double lat, double lng, String gender, float rating, String description) {
        this.name = name;
        this.gender = gender;
        this.description = description;
        this.rating = rating;
        this.location = new LatLng(lat, lng);
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LatLng getLocation() {
        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
