package com.baskerville.toilocate.dto;

import java.io.Serializable;

public class ToiletSaveResDTO implements Serializable {

    private String status;
    private String message;

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

    @Override
    public String toString() {
        return "ToiletSaveResDTO{" +
                "status='" + status + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
