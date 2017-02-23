package com.backbencherslab.gymbuddy.maps.model;

import com.google.gson.annotations.SerializedName;

public class Location {
    @SerializedName("lng")
    private String longitude;

    @SerializedName("lat")
    private String latitude;

    public String getLongitude() {
        return longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    @Override
    public String toString() {
        return "Location [longitude = " + longitude + ", latitude = " + latitude + "]";
    }
}