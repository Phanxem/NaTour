package com.unina.natour.controllers;

import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.amplifyframework.core.Amplify;
import com.unina.natour.controllers.exceptionHandler.ExceptionHandler;
import com.unina.natour.controllers.exceptionHandler.exceptions.subAppException.EmptyFieldUsernameEmailException;
import com.unina.natour.controllers.exceptionHandler.exceptions.subAppException.NotCompletedSignInException;
import com.unina.natour.views.activities.ModificaPasswordActivity;
import com.unina.natour.views.activities.NaTourActivity;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RequiresApi(api = Build.VERSION_CODES.N)
public class ModificaPasswordController extends NaTourController{

    public ModificaPasswordController(NaTourActivity activity){
        super(activity);
    }

    public static void openModificaPasswordActivity(NaTourActivity fromActivity){
        Intent intent = new Intent(fromActivity, ModificaPasswordActivity.class);
        fromActivity.startActivity(intent);
    }


    public boolean updatePassword(String oldPassword, String newPassword, String newPassword2){
        if(!ExceptionHandler.areAllFieldsFull(oldPassword, newPassword, newPassword2)){
            //TODO exception
            return false;
        }
        if(!newPassword.equals(newPassword2)){
            //TODO exception
            return false;
        }

        CompletableFuture<Boolean> completableFuture = new CompletableFuture<Boolean>();

        Amplify.Auth.updatePassword(
                oldPassword,
                newPassword,
                () -> {
                    Log.i(TAG, "Updated password successfully");
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
            //TODO exception
            result = false;
        }


        return result;
    }

}
