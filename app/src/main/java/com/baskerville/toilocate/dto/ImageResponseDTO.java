package com.baskerville.toilocate.dto;

import java.io.Serializable;

public class ImageResponseDTO implements Serializable{

    private String status;

    private String message;

    private String payload;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public String getPayload() {
        return payload;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    //    public ImagePayloadDTO getPayload() {
//        return payload;
//    }
//
//    public void setPayload(ImagePayloadDTO payload) {
//        this.payload = payload;
//    }


    @Override
    public String toString() {
        return "ImageResponseDTO{" +
                "status='" + status + '\'' +
                ", message='" + message + '\'' +
                ", payload='" + payload + '\'' +
                '}';
    }
}
