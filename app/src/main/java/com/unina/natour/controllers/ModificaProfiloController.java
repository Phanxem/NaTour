package com.unina.natour.controllers;

import android.content.Intent;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.unina.natour.views.activities.ModificaProfiloActivity;
import com.unina.natour.views.activities.NaTourActivity;


public class ModificaProfiloController extends NaTourController{

    public ModificaProfiloController(NaTourActivity activity) {
        super(activity);
    }

    public static void openModificaProfiloActivity(NaTourActivity fromActivity){
        Intent intent = new Intent(fromActivity, ModificaProfiloActivity.class);
        fromActivity.startActivity(intent);
    }
}
