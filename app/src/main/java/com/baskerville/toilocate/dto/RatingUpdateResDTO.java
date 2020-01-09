package com.baskerville.toilocate.dto;

import java.io.Serializable;

public class RatingUpdateResDTO implements Serializable {

    private String status;
    private String message;
    private float rating;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }
}
