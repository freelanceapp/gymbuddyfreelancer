package com.backbencherslab.gymbuddy.maps.model;

import com.google.gson.annotations.SerializedName;

public class Photos {
    @SerializedName("photo_reference")
    private String photoReference;

    @SerializedName("height")
    private String height;

    @SerializedName("html_attributions")
    private String[] htmlAttributions;

    @SerializedName("width")
    private String width;

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    @Override
    public String toString() {
        return "Photos [photoReference = " + photoReference + ", height = " + height + ", htmlAttributions = " + htmlAttributions + ", width = " + width + "]";
    }
}