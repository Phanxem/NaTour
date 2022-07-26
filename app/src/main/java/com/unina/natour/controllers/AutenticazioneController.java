package com.unina.natour.controllers;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.amplifyframework.core.Amplify;
import com.unina.natour.controllers.exceptionHandler.ExceptionHandler;
import com.unina.natour.views.activities.AutenticazioneActivity;
import com.unina.natour.views.activities.HomeActivity;
import com.unina.natour.views.dialogs.MessageDialog;

public class AutenticazioneController {

    private final static String TAG ="AutenticazioneController";

    Activity activity;
    MessageDialog messageDialog;

    HomeController homeController;

    public AutenticazioneController(Activity activity){
        this.activity = activity;
        this.messageDialog = new MessageDialog(activity);
        this.homeController = new HomeController(activity);
    }

    public void signIn(String usernameEmail, String password) {

        if(!ExceptionHandler.isThereAnEmptyField(messageDialog,usernameEmail,password)) return;

        effectiveSignIn(usernameEmail,password);

    }

    @SuppressLint("LongLogTag")
    public void effectiveSignIn(String usernameEmail, String password){
        Amplify.Auth.signIn(
                usernameEmail,
                password,
                result -> {
                    Log.i(TAG, "Confirm signIn succeeded");
                    homeController.openHomeActivity();
                },
                error -> {
                    ExceptionHandler.handleMessageError(messageDialog, error);
                }
        );
    }

    public void openAutenticazioneActivity(){
        Intent intent = new Intent(activity, AutenticazioneActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        activity.startActivity(intent);
    }
}
