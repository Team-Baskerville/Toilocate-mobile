package com.baskerville.toilocate.dto;

public class ResponseDTO {

    String status;

    PayloadDTO payload;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public PayloadDTO getPayload() {
        return payload;
    }

    public void setPayload(PayloadDTO payload) {
        this.payload = payload;
    }
}
