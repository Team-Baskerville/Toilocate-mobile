package com.baskerville.toilocate.dto;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.List;

public class PayloadDTO implements Serializable {

    List<ToiletDTO> toilets;

    public List<ToiletDTO> getToilets() {
        return toilets;
    }

    public void setToilets(List<ToiletDTO> toilets) {
        this.toilets = toilets;
    }

    @NonNull
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("toilets: { ");
        for (ToiletDTO toiletDto : toilets) {
            sb.append("[")
                    .append(toiletDto.toString())
                    .append("]\n");
        }
        sb.append("}");
        return sb.toString();
    }
}
