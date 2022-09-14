package com.unina.natour.controllers;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentActivity;

import com.amplifyframework.core.Amplify;
import com.unina.natour.controllers.exceptionHandler.ExceptionHandler;
import com.unina.natour.controllers.exceptionHandler.exceptions.subAppException.EmptyFieldUsernameEmailException;
import com.unina.natour.controllers.exceptionHandler.exceptions.subAppException.NotCompletedSignInException;
import com.unina.natour.views.activities.AutenticazioneActivity;
import com.unina.natour.views.activities.NaTourActivity;
import com.unina.natour.views.dialogs.MessageDialog;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
@RequiresApi(api = Build.VERSION_CODES.N)
@SuppressLint("LongLogTag")
public class AutenticazioneController extends NaTourController{


    public AutenticazioneController(NaTourActivity activity){
        super(activity);
    }


    public Boolean signIn(String usernameEmail, String password) {
        if(!ExceptionHandler.areAllFieldsFull(usernameEmail,password)){
            EmptyFieldUsernameEmailException exception = new EmptyFieldUsernameEmailException();
            ExceptionHandler.handleMessageError(getMessageDialog(), exception);
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
                    ExceptionHandler.handleMessageError(getMessageDialog(), error);
                    completableFuture.complete(false);
                }
        );

        Boolean result = false;
        try {
            result = completableFuture.get();
        }
        catch (ExecutionException | InterruptedException e) {
            NotCompletedSignInException exception = new NotCompletedSignInException(e);
            ExceptionHandler.handleMessageError(getMessageDialog(),exception);
            result = false;
        }


        return result;
    }




    public static void openAutenticazioneActivity(NaTourActivity fromActivity){
        Intent intent = new Intent(fromActivity, AutenticazioneActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        fromActivity.startActivity(intent);
    }
}
