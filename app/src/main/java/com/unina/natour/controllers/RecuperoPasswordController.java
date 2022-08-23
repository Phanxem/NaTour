package com.unina.natour.controllers;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.amplifyframework.core.Amplify;
import com.unina.natour.controllers.exceptionHandler.ExceptionHandler;
import com.unina.natour.views.activities.CompletaRecuperoPasswordActivity;
import com.unina.natour.views.activities.IniziaRecuperoPasswordActivity;
import com.unina.natour.views.dialogs.MessageDialog;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
@RequiresApi(api = Build.VERSION_CODES.N)
public class RecuperoPasswordController {

    private final static String TAG ="RecuperoPasswordController";

    Activity activity;
    MessageDialog messageDialog;

    public RecuperoPasswordController(Activity activity){
        this.activity = activity;
        this.messageDialog = new MessageDialog(activity);
    }


    @SuppressLint("LongLogTag")
    public Boolean startPasswordRecovery(String username) {

        if(!ExceptionHandler.areAllFieldsFull(messageDialog,username)) return false;

        CompletableFuture<Boolean> completableFuture = new CompletableFuture<Boolean>();

        Amplify.Auth.resetPassword(
                username,
                result -> {
                    Log.i(TAG, result.toString());

                    completableFuture.complete(true);
                },
                error -> {
                    Log.e(TAG, error.toString());
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

    @SuppressLint("LongLogTag")
    public Boolean completePasswordRecovery(String code, String password1, String password2){

        if(!ExceptionHandler.areAllFieldsFull(messageDialog,code,password1,password2)) return false;

        if(!ExceptionHandler.doPasswordMatch(messageDialog,password1,password2)) return false;

        CompletableFuture<Boolean> completableFuture = new CompletableFuture<Boolean>();

        Amplify.Auth.confirmResetPassword(
                password1,
                code,
                () -> {
                    Log.i(TAG, "New password confirmed");
                    completableFuture.complete(true);

                },
                error ->{
                    Log.e(TAG, error.toString());
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

    public void back() {
        activity.onBackPressed();
    }

    public void openIniziaRecuperoPasswordActivity(){
        Intent intent = new Intent(activity, IniziaRecuperoPasswordActivity.class);
        activity.startActivity(intent);
    }

    public void openCompletaRecuperoPasswordActivity(){
        Intent intent = new Intent(activity, CompletaRecuperoPasswordActivity.class);
        activity.startActivity(intent);
        activity.finish();
    }


}
