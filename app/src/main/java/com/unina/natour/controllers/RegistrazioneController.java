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
import com.unina.natour.views.activities.NaTourActivity;
import com.unina.natour.views.activities.RegistrazioneActivity;
import com.unina.natour.views.dialogs.MessageDialog;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RequiresApi(api = Build.VERSION_CODES.N)
public class RegistrazioneController extends NaTourController{

    AutenticazioneController autenticazioneController;

    
    public RegistrazioneController(NaTourActivity activity){
        super(activity);

        this.autenticazioneController = new AutenticazioneController(activity);

    }


    public Boolean signUp(String username, String email, String password){

        if(!ExceptionHandler.areAllFieldsFull(username,email,password)){
            EmptyFieldSignUpException exception = new EmptyFieldSignUpException();
            ExceptionHandler.handleMessageError(getMessageDialog(),exception);
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
                    ExceptionHandler.handleMessageError(getMessageDialog(), error);
                    completableFuture.complete(false);
                }
        );

        Boolean result = false;
        try {
            result = completableFuture.get();
        }
        catch (ExecutionException | InterruptedException e) {
            NotCompletedSignUpException exception = new NotCompletedSignUpException(e);
            ExceptionHandler.handleMessageError(getMessageDialog(),exception);
            return false;
        }

        return result;

    }

    public static void openRegistrazioneActivity(NaTourActivity fromActivity){
        Intent intent = new Intent(fromActivity, RegistrazioneActivity.class);
        fromActivity.startActivity(intent);
    }

    public void back() {
        getActivity().onBackPressed();
    }
}
