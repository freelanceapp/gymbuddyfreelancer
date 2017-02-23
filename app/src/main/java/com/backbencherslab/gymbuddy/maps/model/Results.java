package com.backbencherslab.gymbuddy.maps.model;

import com.google.gson.annotations.SerializedName;

public class Results {
    @SerializedName("icon")
    private String icon;

    @SerializedName("place_id")
    private String placeId;

    @SerializedName("scope")
    private String scope;

    @SerializedName("reference")
    private String reference;

    @SerializedName("geometry")
    private Geometry geometry;

    @SerializedName("opening_hours")
    private OpeningHours openingHours;

    @SerializedName("id")
    private String id;

    @SerializedName("photos")
    private Photos[] photos;

    @SerializedName("price_levels")
    private String priceLevel;

    @SerializedName("vicinity")
    private String vicinity;

    @SerializedName("name")
    private String name;

    @SerializedName("rating")
    private String rating;

    private String[] types;

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Geometry getGeometry() {
        return geometry;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVicinity() {
        return vicinity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRating() {
        return rating;
    }

    @Override
    public String toString() {
        return "Results [icon = " + icon + ", placeId = " + placeId + ", scope = " + scope + ", reference = " + reference + ", geometry = " + geometry.toString() + ", openingHours = " + openingHours.toString() + ", id = " + id + ", photos = " + photos.toString() + ", priceLevel = " + priceLevel + ", vicinity = " + vicinity + ", name = " + name + ", rating = " + rating + ", types = " + types + "]";
    }
}