package com.unina.natour.controllers;

import android.app.Activity;
import android.util.Log;
import android.view.View;

import com.amplifyframework.core.Amplify;
import com.unina.natour.controllers.exceptionHandler.ExceptionHandler;
import com.unina.natour.views.dialogs.MessageDialog;

public class DisconnessioneController {

    private final static String TAG ="DisconnessioneController";

    Activity activity;
    MessageDialog messageDialog;

    AutenticazioneController autenticazioneController;

    public DisconnessioneController(Activity activity){
        this.activity = activity;
        this.messageDialog = new MessageDialog(activity);

        this.autenticazioneController = new AutenticazioneController(activity);

    }

    public void signOut(){
        Amplify.Auth.signOut(
                () -> {
                    Log.i(TAG, "Signed out successfully");
                    autenticazioneController.openAutenticazioneActivity();
                    activity.finish();
                },
                error -> {
                    ExceptionHandler.handleMessageError(messageDialog, error);
                }
        );
    }
}
