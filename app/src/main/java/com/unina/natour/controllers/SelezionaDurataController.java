package com.unina.natour.controllers;

import androidx.fragment.app.FragmentActivity;

import java.util.Arrays;
import java.util.Locale;

public class SelezionaDurataController {

    FragmentActivity activity;


    public SelezionaDurataController(FragmentActivity activity){

        this.activity = activity;


    }

    public float toSeconds(float hours, float minutes){
        float seconds = minutes * 60 + hours * 3600;
        return seconds;
    }


}
