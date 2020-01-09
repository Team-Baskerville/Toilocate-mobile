package com.baskerville.toilocate.dto;

import java.io.Serializable;

public class ToiletSearchDTO implements Serializable {

    private double longitude;
    private double latitude;
    private double maxDist;

    public ToiletSearchDTO(){}

    public ToiletSearchDTO(double longitude, double latitude, double maxDist) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.maxDist = maxDist;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getMaxDist() {
        return maxDist;
    }

    public void setMaxDist(double maxDist) {
        this.maxDist = maxDist;
    }
}
