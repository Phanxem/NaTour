package com.unina.natour.controllers;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;

import androidx.fragment.app.FragmentActivity;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.unina.natour.R;
import com.unina.natour.views.activities.NaTourActivity;
import com.unina.natour.views.dialogs.MessageDialog;

import java.io.InputStream;
import java.io.InputStreamReader;

public class HomeController extends NaTourController{



    String searchString;

    public HomeController(NaTourActivity activity) {
        super(activity);

        this.searchString = null;
    }

    public void openAwsConfigurationFile(){
        Resources resources = getActivity().getResources();

        InputStream inputStream = resources.openRawResource(R.raw.awsconfiguration);
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        JsonElement jsonElement = JsonParser.parseReader(inputStreamReader);
        JsonObject jsonObject = jsonElement.getAsJsonObject();
    }

}
