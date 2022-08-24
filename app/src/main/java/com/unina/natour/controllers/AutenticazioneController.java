package com.unina.natour.controllers;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.amplifyframework.core.Amplify;
import com.unina.natour.controllers.exceptionHandler.ExceptionHandler;
import com.unina.natour.controllers.exceptionHandler.exceptions.subAppException.EmptyFieldUsernameEmailException;
import com.unina.natour.controllers.exceptionHandler.exceptions.subAppException.NotCompletedSignInException;
import com.unina.natour.views.activities.AutenticazioneActivity;
import com.unina.natour.views.dialogs.MessageDialog;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
@RequiresApi(api = Build.VERSION_CODES.N)
@SuppressLint("LongLogTag")
public class AutenticazioneController {

    private final static String TAG ="AutenticazioneController";

    Activity activity;
    MessageDialog messageDialog;


    public AutenticazioneController(Activity activity, MessageDialog messageDialog){
        this.activity = activity;
        this.messageDialog = messageDialog;
    }

    public MessageDialog getMessageDialog() {
        return messageDialog;
    }


    public Boolean signIn(String usernameEmail, String password) {

        if(!ExceptionHandler.areAllFieldsFull(usernameEmail,password)){
            EmptyFieldUsernameEmailException exception = new EmptyFieldUsernameEmailException();
            ExceptionHandler.handleMessageError(messageDialog, exception);
            return false;
        }

        return effectiveSignIn(usernameEmail,password);
    }


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
        catch (ExecutionException | InterruptedException e) {
            NotCompletedSignInException exception = new NotCompletedSignInException(e);
            ExceptionHandler.handleMessageError(messageDialog,exception);
            result = false;
        }


        return result;
    }

    public void openAutenticazioneActivity(){
        Intent intent = new Intent(activity, AutenticazioneActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        activity.startActivity(intent);
    }
}
