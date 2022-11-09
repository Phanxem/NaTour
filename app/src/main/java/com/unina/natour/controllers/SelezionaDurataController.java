package com.unina.natour.controllers;

import androidx.fragment.app.FragmentActivity;

import com.unina.natour.views.activities.NaTourActivity;

import java.util.Arrays;
import java.util.Locale;

public class SelezionaDurataController extends NaTourController{

    public SelezionaDurataController(NaTourActivity activity,
                                     ResultMessageController resultMessageController)
    {
        super(activity, resultMessageController);
    }

    public SelezionaDurataController(NaTourActivity activity){
        super(activity);
    }

    public float toSeconds(float hours, float minutes){
        float seconds = minutes * 60 + hours * 3600;
        return seconds;
    }


}
