package com.baskerville.toilocate.dto;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class ToiletDTO implements Serializable {

    private String _id;
    private String userId;
    private String name;
    private String rating;
    private String gender;
    private String imagePath;
    private LocationDTO location;
    private ToiletDescriptionDTO description;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public LocationDTO getLocation() {
        return location;
    }

    public void setLocation(LocationDTO location) {
        this.location = location;
    }

    public ToiletDescriptionDTO getDescription() {
        return description;
    }

    public void setDescription(ToiletDescriptionDTO description) {
        this.description = description;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    @NonNull
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("name: ").append(name)
                .append(", id: ").append(_id)
                .append(", userId: ").append(userId)
                .append(", rating: ").append(rating)
                .append(", gender: ").append(gender)
                .append(", imagePath: ").append(imagePath)
                .append(", location: ").append(location.toString())
                .append(", description: ").append(description.toString());
        return sb.toString();
    }
}
