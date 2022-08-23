package com.unina.natour.controllers;

import android.app.Activity;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class SelezionaCittàController {

    private static final String TAG = "SEDANO";
    Activity activity;

    private String country;
    private String[] cities;

    public SelezionaCittàController(Activity activity){
        this.activity = activity;
    }



    public void setCountry(String country){


        //RETRIVE JSON FROM ASSETS
        String jsonStringResult = null;
        try {
            InputStream inputStream = activity.getAssets().open("countries_cities.json");
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            jsonStringResult = new String(buffer, "UTF-8");
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        //JSON MAPPER
        Log.i("JSON: ", jsonStringResult);

        JsonElement jsonElementResult = JsonParser.parseString(jsonStringResult);
        JsonObject jsonObjectResult = jsonElementResult.getAsJsonObject();

        JsonElement jsonElementCities = jsonObjectResult.get(country);
        if(jsonElementCities == null){
            Log.e(TAG, "la nazione non è valida");
            return;
        }

        JsonArray jsonArrayCities = jsonElementCities.getAsJsonArray();
        String[] cities = new String[jsonArrayCities.size()];


        for(int i = 0; i < jsonArrayCities.size(); i++){
            cities[i] = jsonArrayCities.get(i).getAsString();
        }

        Arrays.sort(cities);

        this.country = country;
        this.cities = cities;
    }

    public String getCountry() {
        return country;
    }

    public String[] getCities() {
        return cities;
    }

}
