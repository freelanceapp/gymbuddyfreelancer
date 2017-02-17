package com.backbencherslab.gymbuddy.maps.rest;

import com.backbencherslab.gymbuddy.maps.model.CoffeeShops;

import java.util.Map;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.QueryMap;

public interface GooglePlacesService {
    @GET("/maps/api/place/nearbysearch/json")
    void getCafes(@QueryMap Map<String, String> options, Callback<CoffeeShops> callback);
}
