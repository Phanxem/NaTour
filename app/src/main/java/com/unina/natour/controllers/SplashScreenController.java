package com.unina.natour.controllers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.amplifyframework.core.Amplify;
import com.unina.natour.controllers.exceptionHandler.ExceptionHandler;
import com.unina.natour.views.activities.AttivaAccountActivity;
import com.unina.natour.views.activities.AutenticazioneActivity;
import com.unina.natour.views.activities.HomeActivity;
import com.unina.natour.views.dialogs.MessageDialog;

public class SplashScreenController {

    private final static String TAG ="SplashScreenController";

    Activity activity;
    MessageDialog messageDialog;

    HomeController homeController;
    AttivaAccountController attivaAccountController;
    AutenticazioneController autenticazioneController;

    public SplashScreenController(Activity activity){
        this.activity = activity;
        this.messageDialog = new MessageDialog(activity);

        this.homeController = new HomeController(activity);
        this.attivaAccountController = new AttivaAccountController(activity);
        this.autenticazioneController = new AutenticazioneController(activity);
    }

    public void redirectToRightActivity(){
        Amplify.Auth.fetchAuthSession(
                result -> {
                    Log.i(TAG, result.toString());
                    if(result.isSignedIn()){
                        homeController.openHomeActivity();
                    }
                    else{
                        String packageName = activity.getApplicationContext().getPackageName();
                        SharedPreferences sharedPreferences = activity.getSharedPreferences(packageName, Context.MODE_PRIVATE);
                        boolean isAccountActive = sharedPreferences.getBoolean(AttivaAccountController.SHARED_PREFERENCES_ACCOUNT_ACTIVATION, false);

                        if(!isAccountActive){
                            String username = sharedPreferences.getString(AttivaAccountController.SHARED_PREFERENCES_USERNAME, null);
                            String password = sharedPreferences.getString(AttivaAccountController.SHARED_PREFERENCES_PASSWORD, null);

                            attivaAccountController.openAttivaAccountActivity(username,password);
                            return;
                        }

                        autenticazioneController.openAutenticazioneActivity();
                    }

                    activity.finish();
                },
                error -> {
                    Log.e(TAG, error.toString());
                    ExceptionHandler.handleMessageError(messageDialog,error);
                }
        );
    }
}
