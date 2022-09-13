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
import com.unina.natour.controllers.exceptionHandler.exceptions.subAppException.EmptyFieldPasswordRecoveryException;
import com.unina.natour.controllers.exceptionHandler.exceptions.subAppException.EmptyFieldUsernameEmailException;
import com.unina.natour.controllers.exceptionHandler.exceptions.subAppException.NotCompletedConfirmResetPasswordException;
import com.unina.natour.controllers.exceptionHandler.exceptions.subAppException.NotCompletedStartPasswordRecoveryException;
import com.unina.natour.controllers.exceptionHandler.exceptions.subAppException.UnmatchedPasswordException;
import com.unina.natour.views.activities.CompletaRecuperoPasswordActivity;
import com.unina.natour.views.activities.IniziaRecuperoPasswordActivity;
import com.unina.natour.views.activities.NaTourActivity;
import com.unina.natour.views.dialogs.MessageDialog;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
@RequiresApi(api = Build.VERSION_CODES.N)
@SuppressLint("LongLogTag")
public class RecuperoPasswordController extends NaTourController{



    public RecuperoPasswordController(NaTourActivity activity){
        super(activity);
    }

    public Boolean startPasswordRecovery(String username) {

        if(!ExceptionHandler.areAllFieldsFull(username)){
            EmptyFieldUsernameEmailException exception = new EmptyFieldUsernameEmailException();
            ExceptionHandler.handleMessageError(getMessageDialog(),exception);
            return false;
        }

        CompletableFuture<Boolean> completableFuture = new CompletableFuture<Boolean>();

        Amplify.Auth.resetPassword(
                username,
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
            NotCompletedStartPasswordRecoveryException exception = new NotCompletedStartPasswordRecoveryException(e);
            ExceptionHandler.handleMessageError(getMessageDialog(),exception);
            return false;
        }

        return result;
    }


    public Boolean completePasswordRecovery(String code, String password1, String password2){

        if(!ExceptionHandler.areAllFieldsFull(code,password1,password2)){
            EmptyFieldPasswordRecoveryException exception = new EmptyFieldPasswordRecoveryException();
            ExceptionHandler.handleMessageError(getMessageDialog(),exception);
            return false;
        }

        if(!ExceptionHandler.doPasswordMatch(password1,password2)){
            UnmatchedPasswordException exception = new UnmatchedPasswordException();
            ExceptionHandler.handleMessageError(getMessageDialog(),exception);
            return false;
        }

        CompletableFuture<Boolean> completableFuture = new CompletableFuture<Boolean>();

        Amplify.Auth.confirmResetPassword(
                password1,
                code,
                () -> {
                    completableFuture.complete(true);
                },
                error ->{
                    ExceptionHandler.handleMessageError(getMessageDialog(), error);
                    completableFuture.complete(false);
                }
        );

        Boolean result = false;
        try {
            result = completableFuture.get();
        }
        catch (ExecutionException | InterruptedException e) {
            NotCompletedConfirmResetPasswordException exception = new NotCompletedConfirmResetPasswordException(e);
            ExceptionHandler.handleMessageError(getMessageDialog(),exception);
            return false;
        }
        return result;
    }

    public void back() {
        getActivity().onBackPressed();
    }

    public void openIniziaRecuperoPasswordActivity(){
        Intent intent = new Intent(getActivity(), IniziaRecuperoPasswordActivity.class);
        getActivity().startActivity(intent);
    }

    public void openCompletaRecuperoPasswordActivity(){
        Intent intent = new Intent(getActivity(), CompletaRecuperoPasswordActivity.class);
        getActivity().startActivity(intent);
        getActivity().finish();
    }


}
