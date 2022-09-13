package com.unina.natour.controllers;

import android.app.Activity;
import android.util.Log;

import androidx.fragment.app.FragmentActivity;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.unina.natour.views.activities.NaTourActivity;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class SelezionaCittàController extends NaTourController{

    private String country;
    private String[] cities;

    public SelezionaCittàController(NaTourActivity activity){
        super(activity);
    }



    public void setCountry(String country){


        //RETRIVE JSON FROM ASSETS
        String jsonStringResult = null;
        try {
            InputStream inputStream = getActivity().getAssets().open("countries_cities.json");
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
