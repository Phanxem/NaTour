package com.unina.natour.controllers;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentActivity;

import com.amplifyframework.auth.AuthUserAttributeKey;
import com.amplifyframework.auth.options.AuthSignUpOptions;
import com.amplifyframework.core.Amplify;
import com.unina.natour.controllers.exceptionHandler.ExceptionHandler;
import com.unina.natour.controllers.exceptionHandler.exceptions.subAppException.EmptyFieldSignUpException;
import com.unina.natour.controllers.exceptionHandler.exceptions.subAppException.NotCompletedSignUpException;
import com.unina.natour.views.activities.RegistrazioneActivity;
import com.unina.natour.views.dialogs.MessageDialog;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RequiresApi(api = Build.VERSION_CODES.N)
public class RegistrazioneController {

    private final static String TAG ="RegistrazioneController";



    FragmentActivity activity;
    MessageDialog messageDialog;

    AutenticazioneController autenticazioneController;

    
    public RegistrazioneController(FragmentActivity activity, MessageDialog messageDialog){
        this.activity = activity;
        this.messageDialog = messageDialog;

        this.autenticazioneController = new AutenticazioneController(activity, messageDialog);

    }

    public MessageDialog getMessageDialog() {
        return messageDialog;
    }

    


    public Boolean signUp(String username, String email, String password){

        if(!ExceptionHandler.areAllFieldsFull(username,email,password)){
            EmptyFieldSignUpException exception = new EmptyFieldSignUpException();
            ExceptionHandler.handleMessageError(messageDialog,exception);
            return false;
        }

        AuthSignUpOptions options = AuthSignUpOptions
                .builder()
                .userAttribute(AuthUserAttributeKey.email(), email)
                .build();

        CompletableFuture<Boolean> completableFuture = new CompletableFuture<Boolean>();

        Amplify.Auth.signUp(
                username,
                password,
                options,
                result -> {
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
            NotCompletedSignUpException exception = new NotCompletedSignUpException(e);
            ExceptionHandler.handleMessageError(messageDialog,exception);
            return false;
        }

        return result;

    }

    public void openRegistrazioneActivity(){
        Intent intent = new Intent(activity, RegistrazioneActivity.class);
        activity.startActivity(intent);
    }

    public void back() {
        activity.onBackPressed();
    }
}
