package com.baskerville.toilocate.dto;

import java.io.Serializable;

public class ImagePayloadDTO implements Serializable {
    private String imageURL;

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}
