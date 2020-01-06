package com.baskerville.toilocate.dto;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class LocationDTO implements Serializable {

    private String type;
    private Double[] coordinates;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double[] getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Double[] coordinates) {
        this.coordinates = coordinates;
    }

    @NonNull
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("type: " + type)
                .append(", coordinates: [")
                .append(coordinates[0])
                .append(",")
                .append(coordinates[1])
                .append("]");
        return sb.toString();
    }
}
