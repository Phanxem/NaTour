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
import com.unina.natour.controllers.SplashScreenController;
import com.unina.natour.views.dialogs.MessageDialog;
@RequiresApi(api = Build.VERSION_CODES.N)
public class SplashScreenActivity extends AppCompatActivity {

    private final static String TAG ="SplashScreenActivity";
    private SplashScreenController splashScreenController;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        MessageDialog messageDialog = new MessageDialog();
        messageDialog.setFragmentActivity(this);

        splashScreenController = new SplashScreenController(this, messageDialog);
        splashScreenController.redirectToRightActivity();
    }
}