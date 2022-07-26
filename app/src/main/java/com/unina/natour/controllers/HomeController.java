package com.unina.natour.controllers;

import android.app.Activity;
import android.content.Intent;

import com.unina.natour.views.activities.HomeActivity;
import com.unina.natour.views.dialogs.MessageDialog;

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

}
