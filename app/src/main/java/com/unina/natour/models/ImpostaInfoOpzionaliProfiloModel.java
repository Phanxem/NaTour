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

public class ImpostaInfoOpzionaliProfiloModel implements Observable {

    private Calendar dateOfBirth;

    private String country;
    private String city;
    private String address;

    private List<Observer> observers;


    public ImpostaInfoOpzionaliProfiloModel(){
        this.observers = new ArrayList<Observer>();
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



    @Override
    public void registerObserver(Observer observer) {
        if(!observers.contains(observer)) observers.add(observer);
    }

    @Override
    public void undergisterObserver(Observer observer) {
        if(observers.contains(observer)) observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for(Observer observer : observers){
            observer.update();
        }
    }
}
