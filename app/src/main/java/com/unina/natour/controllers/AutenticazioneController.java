package com.unina.natour.controllers;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.amplifyframework.core.Amplify;
import com.unina.natour.controllers.exceptionHandler.ExceptionHandler;
import com.unina.natour.views.activities.AutenticazioneActivity;
import com.unina.natour.views.dialogs.MessageDialog;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class AutenticazioneController {

    private final static String TAG ="AutenticazioneController";

    Activity activity;
    MessageDialog messageDialog;

    public AutenticazioneController(Activity activity){
        this.activity = activity;
        this.messageDialog = new MessageDialog(activity);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public Boolean signIn(String usernameEmail, String password) {

        if(!ExceptionHandler.areAllFieldsFull(messageDialog,usernameEmail,password)) return false;

        return effectiveSignIn(usernameEmail,password);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("LongLogTag")
    public Boolean effectiveSignIn(String usernameEmail, String password){

        CompletableFuture<Boolean> completableFuture = new CompletableFuture<Boolean>();

        Amplify.Auth.signIn(
                usernameEmail,
                password,
                result -> {
                    Log.i(TAG, "Confirm signIn succeeded");
                    completableFuture.complete(true);
                },
                error -> {
                    ExceptionHandler.handleMessageError(messageDialog, error);
                    completableFuture.complete(false);
                }
        );

        Boolean result = false;
        try {
            result = completableFuture.get();
        }
        catch (ExecutionException e) {
            e.printStackTrace();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }

        return result;
    }

    public void openAutenticazioneActivity(){
        Intent intent = new Intent(activity, AutenticazioneActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        activity.startActivity(intent);
    }
}
