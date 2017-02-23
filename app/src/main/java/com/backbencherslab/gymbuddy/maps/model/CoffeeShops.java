package com.backbencherslab.gymbuddy.maps.model;

import com.google.gson.annotations.SerializedName;

public class CoffeeShops {
    @SerializedName("next_page_token")
    private String nextPageToken;

    @SerializedName("results")
    private Results[] results;

    @SerializedName("html_attributions")
    private String[] htmlAttributions;

    @SerializedName("status")
    private String status;

    public Results[] getResults() {
        return results;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "CoffeeShops [nextPageToken = " + nextPageToken + ", results = " + results.toString() + ", htmlAttributions = " + htmlAttributions + ", status = " + status + "]";
    }
}