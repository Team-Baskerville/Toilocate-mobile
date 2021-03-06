package com.baskerville.toilocate.dto;

import com.baskerville.toilocate.classes.Toilet;

import java.io.Serializable;

public class ToiletLiteDTO implements Serializable {

    private String id;
    private String name;
    private String gender;
    private String imagePath;
    private float rating;
    private ToiletDescriptionDTO description;

    public ToiletLiteDTO() {
    }

    public ToiletLiteDTO(Toilet toilet) {
        this.id = toilet.getId();
        this.name = toilet.getName();
        this.gender = toilet.getGender();
        this.imagePath =  toilet.getImagePath();
        this.rating = toilet.getRating();
        this.description = toilet.getDescription();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public ToiletDescriptionDTO getDescription() {
        return description;
    }

    public void setDescription(ToiletDescriptionDTO description) {
        this.description = description;
    }
}
