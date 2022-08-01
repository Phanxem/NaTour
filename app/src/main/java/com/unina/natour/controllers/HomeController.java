package com.unina.natour.controllers;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.unina.natour.R;
import com.unina.natour.views.activities.HomeActivity;
import com.unina.natour.views.dialogs.MessageDialog;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

public class HomeController {

    private final static String TAG ="HomeController";

    Activity activity;
    MessageDialog messageDialog;

    public HomeController(Activity activity){
        this.activity = activity;
        this.messageDialog = new MessageDialog(activity);
    }

    public void openHomeActivity(){
        Intent intent = new Intent(activity, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        activity.startActivity(intent);
    }

    public void g(){
        Resources resources = activity.getResources();

        InputStream inputStream = resources.openRawResource(R.raw.awsconfiguration);
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        JsonElement jsonElement = JsonParser.parseReader(inputStreamReader);
        JsonObject jsonObject = jsonElement.getAsJsonObject();
    }

}
