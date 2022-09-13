package com.unina.natour.controllers;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentActivity;

import com.amplifyframework.core.Amplify;
import com.unina.natour.controllers.exceptionHandler.ExceptionHandler;
import com.unina.natour.controllers.exceptionHandler.exceptions.subAppException.NotCompletedSignInException;
import com.unina.natour.controllers.exceptionHandler.exceptions.subAppException.NotCompletedSignOutException;
import com.unina.natour.views.activities.NaTourActivity;
import com.unina.natour.views.dialogs.MessageDialog;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
@RequiresApi(api = Build.VERSION_CODES.N)
@SuppressLint("LongLogTag")
public class DisconnessioneController extends NaTourController{



    AutenticazioneController autenticazioneController;

    public DisconnessioneController(NaTourActivity activity){
        super(activity);

        this.autenticazioneController = new AutenticazioneController(activity);
    }


    public Boolean signOut(){

        CompletableFuture<Boolean> completableFuture = new CompletableFuture<Boolean>();

        Amplify.Auth.signOut(
                () -> {
                    Log.i(TAG, "Signed out successfully");
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
            NotCompletedSignOutException exception = new NotCompletedSignOutException(e);
            ExceptionHandler.handleMessageError(getMessageDialog(),exception);
            result = false;
        }


        return result;
    }
}
