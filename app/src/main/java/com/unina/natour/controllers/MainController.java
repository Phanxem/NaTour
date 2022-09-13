package com.unina.natour.controllers;

import android.app.Activity;
import android.content.Intent;

import androidx.fragment.app.FragmentActivity;

import com.unina.natour.views.activities.MainActivity;
import com.unina.natour.views.activities.NaTourActivity;
import com.unina.natour.views.dialogs.MessageDialog;

public class MainController extends NaTourController{


    public static final String KEY_CONTROLLER = "CONTROLLER";

    public MainController(NaTourActivity activity){
        super(activity);
    }

    public static void openMainActivity(NaTourActivity fromActivity){
        Intent intent = new Intent(fromActivity, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        fromActivity.startActivity(intent);
    }

}
