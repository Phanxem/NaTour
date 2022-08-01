package com.unina.natour.controllers;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.unina.natour.views.activities.RegistrazioneActivity;
import com.unina.natour.views.dialogs.MessageDialog;

public class ProfiloPersonaleController {

    private final static String TAG ="ProfiloPersonaleController";



    Activity activity;
    MessageDialog messageDialog;

    public ProfiloPersonaleController(Activity activity){
        this.activity = activity;
        this.messageDialog = new MessageDialog(activity);
    }


/*
    public void openPersonalizzaAccountImmagineProfiloActivity(){
        Intent intent = new Intent(activity, RegistrazioneActivity.class);
        activity.startActivity(intent);
    }

    public void openPersonalizzaAccountImmagineProfiloActivity(){
        Intent intent = new Intent(activity, RegistrazioneActivity.class);
        activity.startActivity(intent);
    }
*/

}
