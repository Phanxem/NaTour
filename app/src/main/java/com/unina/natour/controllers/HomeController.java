package com.unina.natour.controllers;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;

import androidx.fragment.app.FragmentActivity;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.unina.natour.R;
import com.unina.natour.views.dialogs.MessageDialog;

import java.io.InputStream;
import java.io.InputStreamReader;

public class HomeController {

    private final static String TAG ="HomeController";

    FragmentActivity activity;
    MessageDialog messageDialog;

    String searchString;

    public HomeController(FragmentActivity activity, MessageDialog messageDialog){
        this.activity = activity;
        this.messageDialog = messageDialog;

        this.searchString = null;
    }

    public MessageDialog getMessageDialog() {
        return messageDialog;
    }



    public void openAwsConfigurationFile(){
        Resources resources = activity.getResources();

        InputStream inputStream = resources.openRawResource(R.raw.awsconfiguration);
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        JsonElement jsonElement = JsonParser.parseReader(inputStreamReader);
        JsonObject jsonObject = jsonElement.getAsJsonObject();
    }

}
