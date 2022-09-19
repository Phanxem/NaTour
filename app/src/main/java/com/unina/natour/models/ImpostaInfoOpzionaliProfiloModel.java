package com.unina.natour.models;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.unina.natour.views.observers.Observable;
import com.unina.natour.views.observers.Observer;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ImpostaInfoOpzionaliProfiloModel extends NaTourModel {

    private Calendar dateOfBirth;

    private String country;
    private String city;
    private String address;

    public ImpostaInfoOpzionaliProfiloModel(){
        super();
    }

    public Calendar getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Calendar dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
        notifyObservers();
    }

    public String getCountry() {

        return this.country;
    }

    public void setCountry(String country) {
        if(this.country != null && !this.country.isEmpty()){
            if (!country.equals(this.country)) this.city = null;
        }

        this.country = country;
        notifyObservers();
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
        notifyObservers();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    public void clear(){
        this.dateOfBirth = null;
        this.country = null;
        this.city = null;
        this.address = null;
    }
}
