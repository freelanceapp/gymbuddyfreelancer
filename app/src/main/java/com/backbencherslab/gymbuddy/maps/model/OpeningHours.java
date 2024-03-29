package com.backbencherslab.gymbuddy.maps.model;

import com.google.gson.annotations.SerializedName;

public class OpeningHours {
    @SerializedName("open_now")
    private String openNow;

    @SerializedName("weekday_text")
    private String[] weekdayText;

    @Override
    public String toString() {
        return "ClassPojo [openNow = " + openNow + ", weekdayText = " + weekdayText + "]";
    }
}
