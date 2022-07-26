package com.unina.natour.controllers;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.amplifyframework.core.Amplify;
import com.unina.natour.controllers.exceptionHandler.ExceptionHandler;
import com.unina.natour.views.activities.CompletaRecuperoPasswordActivity;
import com.unina.natour.views.activities.IniziaRecuperoPasswordActivity;
import com.unina.natour.views.dialogs.MessageDialog;

public class RecuperoPasswordController {

    private final static String TAG ="RecuperoPasswordController";

    Activity activity;
    MessageDialog messageDialog;

    public RecuperoPasswordController(Activity activity){
        this.activity = activity;
        this.messageDialog = new MessageDialog(activity);
    }

    @SuppressLint("LongLogTag")
    public void startPasswordRecovery(String username) {
        if(!ExceptionHandler.isThereAnEmptyField(messageDialog,username)) return;

        Amplify.Auth.resetPassword(
                username,
                result -> {
                    Log.i(TAG, result.toString());
                    openCompletaRecuperoPasswordActivity();

                },
                error -> {
                    Log.e(TAG, error.toString());
                    ExceptionHandler.handleMessageError(messageDialog, error);
                }
        );
    }

    @SuppressLint("LongLogTag")
    public void completePasswordRecovery(String code, String password1, String password2){

        if(!ExceptionHandler.isThereAnEmptyField(messageDialog,code,password1,password2)) return;

        if(!ExceptionHandler.doPasswordMatch(messageDialog,password1,password2)) return;

        Amplify.Auth.confirmResetPassword(
                password1,
                code,
                () -> {
                    Log.i(TAG, "New password confirmed");
                },
                error ->{
                    Log.e(TAG, error.toString());
                    ExceptionHandler.handleMessageError(messageDialog, error);
                }
        );
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
