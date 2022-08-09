package com.unina.natour.controllers;

import android.app.Activity;

import com.unina.natour.views.observers.Observer;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SelezionaNazioneController {

    Activity activity;

    private String[] countries;

    public SelezionaNazioneController(Activity activity){

        this.activity = activity;


        String[] locales = Locale.getISOCountries();

        this.countries = new String[locales.length];

        for (int i = 0; i < locales.length; i++) {
            Locale locale = new Locale("en", locales[i]);
            countries[i] = locale.getDisplayCountry(locale);
        }
    }

    public String[] getCountries() {
        return countries;
    }

    public void setCountries(String[] countries) {
        this.countries = countries;
    }

}
