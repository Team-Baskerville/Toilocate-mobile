package com.baskerville.toilocate.dto;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class ToiletDescriptionDTO implements Serializable {

    private boolean urineTanks;
    private boolean waterSink;
    private boolean mirror;
    private boolean shower;
    private boolean squat;
    private boolean commode;

    public ToiletDescriptionDTO(){}

    public ToiletDescriptionDTO(boolean urineTanks, boolean waterSink, boolean mirror,
                                boolean shower, boolean squat, boolean commode) {
        this.urineTanks = urineTanks;
        this.waterSink = waterSink;
        this.mirror = mirror;
        this.shower = shower;
        this.squat = squat;
        this.commode = commode;
    }

    public boolean isUrineTanks() {
        return urineTanks;
    }

    public void setUrineTanks(boolean urineTanks) {
        this.urineTanks = urineTanks;
    }

    public boolean isWaterSink() {
        return waterSink;
    }

    public void setWaterSink(boolean waterSink) {
        this.waterSink = waterSink;
    }

    public boolean isMirror() {
        return mirror;
    }

    public void setMirror(boolean mirror) {
        this.mirror = mirror;
    }

    public boolean isShower() {
        return shower;
    }

    public void setShower(boolean shower) {
        this.shower = shower;
    }

    public boolean isSquat() {
        return squat;
    }

    public void setSquat(boolean squat) {
        this.squat = squat;
    }

    public boolean isCommode() {
        return commode;
    }

    public void setCommode(boolean commode) {
        this.commode = commode;
    }

    @NonNull
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("urine tanks: ").append(urineTanks);
        sb.append(", water sink: ").append(waterSink);
        sb.append(", mirror: ").append(mirror);
        sb.append(", shower: ").append(shower);
        sb.append(", squat: ").append(squat);
        sb.append(", commode: ").append(commode);
        return sb.toString();
    }
}
