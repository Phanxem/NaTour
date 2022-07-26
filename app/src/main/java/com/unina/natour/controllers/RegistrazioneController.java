package com.unina.natour.controllers;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.amplifyframework.auth.AuthUserAttributeKey;
import com.amplifyframework.auth.options.AuthSignUpOptions;
import com.amplifyframework.core.Amplify;
import com.unina.natour.controllers.exceptionHandler.ExceptionHandler;
import com.unina.natour.views.activities.AttivaAccountActivity;
import com.unina.natour.views.activities.AutenticazioneActivity;
import com.unina.natour.views.activities.RegistrazioneActivity;
import com.unina.natour.views.dialogs.MessageDialog;


public class RegistrazioneController {

    private final static String TAG ="RegistrazioneController";



    Activity activity;
    MessageDialog messageDialog;

    AutenticazioneController autenticazioneController;
    AttivaAccountController attivaAccountController;
    
    public RegistrazioneController(Activity activity){
        this.activity = activity;
        this.messageDialog = new MessageDialog(activity);

        this.autenticazioneController = new AutenticazioneController(activity);
        this.attivaAccountController = new AttivaAccountController(activity);
    }

    

    public void signUp(String username, String email, String password){

        if(!ExceptionHandler.isThereAnEmptyField(messageDialog,username,email,password)) return;

        AuthSignUpOptions options = AuthSignUpOptions
                .builder()
                .userAttribute(AuthUserAttributeKey.email(), email)
                .build();

        Amplify.Auth.signUp(
                username,
                password,
                options,
                result -> {
                    Log.i(TAG, "Result: " + result.toString());
                    attivaAccountController.openAttivaAccountActivity(username, password);
                },
                error -> {
                    ExceptionHandler.handleMessageError(messageDialog, error);
                }
        );
    }

    public void openRegistrazioneActivity(){
        Intent intent = new Intent(activity, RegistrazioneActivity.class);
        activity.startActivity(intent);
    }

    public void back() {
        activity.onBackPressed();
    }
}
