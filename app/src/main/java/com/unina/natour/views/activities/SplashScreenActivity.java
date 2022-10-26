package com.unina.natour.views.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.amplifyframework.core.Amplify;
import com.unina.natour.R;
import com.unina.natour.controllers.DisconnessioneController;
import com.unina.natour.controllers.SplashScreenController;
import com.unina.natour.views.dialogs.MessageDialog;

public class SplashScreenActivity extends NaTourActivity{

    private SplashScreenController splashScreenController;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        //DisconnessioneController disconnessioneController = new DisconnessioneController(this);
        //disconnessioneController.signOut();

        splashScreenController = new SplashScreenController(this);
        splashScreenController.redirectToRightActivity();
    }
}