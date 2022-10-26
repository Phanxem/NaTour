package com.unina.natour.controllers;

import com.unina.natour.views.activities.NaTourActivity;

import java.util.Arrays;
import java.util.Locale;

public class SelezionaNazioneController extends NaTourController{



    private String[] countries;

    public SelezionaNazioneController(NaTourActivity activity){
        super(activity);


        String[] locales = Locale.getISOCountries();

        this.countries = new String[locales.length];

        for (int i = 0; i < locales.length; i++) {
            Locale locale = new Locale("en", locales[i]);
            countries[i] = locale.getDisplayCountry(locale);
        }

        Arrays.sort(countries);
    }

    public String[] getCountries() {
        return countries;
    }

    public void setCountries(String[] countries) {
        this.countries = countries;
    }

}
