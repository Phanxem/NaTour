package com.unina.natour.controllers;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.amplifyframework.auth.AuthUserAttributeKey;
import com.amplifyframework.auth.options.AuthSignUpOptions;
import com.amplifyframework.core.Amplify;
import com.unina.natour.controllers.exceptionHandler.ExceptionHandler;
import com.unina.natour.views.activities.RegistrazioneActivity;
import com.unina.natour.views.dialogs.MessageDialog;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RequiresApi(api = Build.VERSION_CODES.N)
public class RegistrazioneController {

    private final static String TAG ="RegistrazioneController";



    Activity activity;
    MessageDialog messageDialog;

    AutenticazioneController autenticazioneController;

    
    public RegistrazioneController(Activity activity){
        this.activity = activity;
        this.messageDialog = new MessageDialog(activity);

        this.autenticazioneController = new AutenticazioneController(activity);

    }

    


    public Boolean signUp(String username, String email, String password){

        if(!ExceptionHandler.areAllFieldsFull(messageDialog,username,email,password)) return false;

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
                    Log.i(TAG, "Result: " + result.toString());
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

    public void openRegistrazioneActivity(){
        Intent intent = new Intent(activity, RegistrazioneActivity.class);
        activity.startActivity(intent);
    }

    public void back() {
        activity.onBackPressed();
    }
}
