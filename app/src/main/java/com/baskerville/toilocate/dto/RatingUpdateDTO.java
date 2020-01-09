package com.baskerville.toilocate.dto;

import java.io.Serializable;

public class RatingUpdateDTO implements Serializable {

    private String id;
    private float rating;

    public RatingUpdateDTO(){}

    public RatingUpdateDTO(String id, float rating) {
        this.id = id;
        this.rating = rating;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    @Override
    public String toString() {
        return "RatingUpdateDTO{" +
                "id='" + id + '\'' +
                ", rating=" + rating +
                '}';
    }
}
