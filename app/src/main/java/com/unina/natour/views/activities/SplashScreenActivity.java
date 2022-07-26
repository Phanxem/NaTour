package com.unina.natour.views.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.amplifyframework.core.Amplify;
import com.unina.natour.R;
import com.unina.natour.controllers.SplashScreenController;

public class SplashScreenActivity extends AppCompatActivity {

    private final static String TAG ="SplashScreenActivity";
    private SplashScreenController splashScreenController;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        splashScreenController = new SplashScreenController(this);
        splashScreenController.redirectToRightActivity();
    }
}