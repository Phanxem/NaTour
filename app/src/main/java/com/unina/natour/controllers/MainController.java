package com.unina.natour.controllers;

import android.app.Activity;
import android.content.Intent;

import com.unina.natour.views.activities.MainActivity;
import com.unina.natour.views.activities.PersonalizzaAccountInfoOpzionaliActivity;
import com.unina.natour.views.dialogs.MessageDialog;

public class MainController {


    public static final String KEY_CONTROLLER = "Controller";

    Activity activity;
    MessageDialog messageDialog;

    public MainController(Activity activity){
        this.activity = activity;
        this.messageDialog = new MessageDialog(activity);
    }

    public void openMainActivity(){
        Intent intent = new Intent(activity, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        activity.startActivity(intent);
    }

}
