package com.baskerville.toilocate.classes;

import androidx.annotation.NonNull;

import com.baskerville.toilocate.dto.ToiletDTO;
import com.baskerville.toilocate.dto.ToiletDescriptionDTO;
import com.google.android.gms.maps.model.LatLng;

public class Toilet {

    private String name;
    private LatLng location;
    private String gender;
    private float rating;
    private String imagePath;
    private ToiletDescriptionDTO description;

    public Toilet() {
    }

    public Toilet(String name, double lat, double lng, String gender, float rating, String description) {
        this.name = name;
        this.gender = gender;
        //this.description = description;
        this.rating = rating;
        this.location = new LatLng(lat, lng);
    }

    public Toilet(ToiletDTO toiletDTO) {
        this.name = toiletDTO.getName();
        this.rating = Float.parseFloat(toiletDTO.getRating());
        this.gender = toiletDTO.getGender() == null ? "undefined" : toiletDTO.getGender();
        this.imagePath = toiletDTO.getImagePath();
        this.location = new LatLng(toiletDTO.getLocation().getCoordinates()[1],
                toiletDTO.getLocation().getCoordinates()[0]);
        this.description = toiletDTO.getDescription();
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

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public ToiletDescriptionDTO getDescription() {
        return description;
    }

    public void setDescription(ToiletDescriptionDTO description) {
        this.description = description;
    }

    @NonNull
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("name: " + name).append(", location : [").append(location.latitude)
                .append(",").append(location.longitude);
        return sb.toString();
    }
}
