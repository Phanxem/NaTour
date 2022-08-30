package com.unina.natour.controllers;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentActivity;

import com.amplifyframework.auth.cognito.AWSCognitoAuthSession;
import com.amplifyframework.auth.cognito.AWSCognitoUserPoolTokens;
import com.amplifyframework.auth.result.AuthSessionResult;
import com.amplifyframework.core.Amplify;
import com.unina.natour.controllers.exceptionHandler.ExceptionHandler;
import com.unina.natour.controllers.exceptionHandler.exceptions.subAppException.NotCompletedFetchAuthSessionException;
import com.unina.natour.views.dialogs.MessageDialog;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RequiresApi(api = Build.VERSION_CODES.N)
public class SplashScreenController {

    private final static String TAG ="SplashScreenController";

    FragmentActivity activity;
    MessageDialog messageDialog;

    MainController mainController;
    AttivaAccountController attivaAccountController;
    AutenticazioneController autenticazioneController;


    public SplashScreenController(FragmentActivity activity, MessageDialog messageDialog){
        this.activity = activity;
        this.messageDialog = messageDialog;

        this.mainController = new MainController(activity, messageDialog);
        this.attivaAccountController = new AttivaAccountController(activity, messageDialog);
        this.autenticazioneController = new AutenticazioneController(activity, messageDialog);
    }

    public MessageDialog getMessageDialog() {
        return messageDialog;
    }

    public void redirectToRightActivity(){

        CompletableFuture<AWSCognitoAuthSession> completableFuture = new CompletableFuture<AWSCognitoAuthSession>();

        Amplify.Auth.fetchAuthSession(
                result -> {
                    AWSCognitoAuthSession cognitoAuthSession = (AWSCognitoAuthSession) result;
                    completableFuture.complete(cognitoAuthSession);
                },
                error -> {
                    ExceptionHandler.handleMessageError(messageDialog,error);
                    completableFuture.complete(null);
                }
        );

        AWSCognitoAuthSession result = null;

        try {
            result = completableFuture.get();
        }
        catch (ExecutionException | InterruptedException e) {
            NotCompletedFetchAuthSessionException exception = new NotCompletedFetchAuthSessionException(e);
            ExceptionHandler.handleMessageError(messageDialog,exception);

        }

        if(result != null && result.isSignedIn()){
            mainController.openMainActivity();
        }
        else {
            String packageName = activity.getApplicationContext().getPackageName();
            SharedPreferences sharedPreferences = activity.getSharedPreferences(packageName, Context.MODE_PRIVATE);

            boolean mustActivateAccount = sharedPreferences.getBoolean(AttivaAccountController.SHARED_PREFERENCES_ACCOUNT_ACTIVATION, false);

            if(mustActivateAccount){
                String username = sharedPreferences.getString(AttivaAccountController.SHARED_PREFERENCES_USERNAME, null);
                String password = sharedPreferences.getString(AttivaAccountController.SHARED_PREFERENCES_PASSWORD, null);

                attivaAccountController.openAttivaAccountActivity(username,password);
            }
            else autenticazioneController.openAutenticazioneActivity();
        }

        activity.finish();
    }

}


/*
* Amplify.Auth.fetchAuthSession(
                result -> {
                    AWSCognitoAuthSession cognitoAuthSession = (AWSCognitoAuthSession) result;

                    AuthSessionResult<AWSCognitoUserPoolTokens> authSessionResult = cognitoAuthSession.getUserPoolTokens();
                    AWSCognitoUserPoolTokens awsCognitoUserPoolTokens = authSessionResult.getValue();

                    if(awsCognitoUserPoolTokens == null) Log.i(TAG, "NULL");
                    else Log.i(TAG, "accessToken: " + awsCognitoUserPoolTokens.getAccessToken());


                    Log.i(TAG, result.toString());
                    if(result.isSignedIn()){
                        homeController.openHomeActivity();
                        //openHomeActivity();
                    }
                    else{
                        String packageName = activity.getApplicationContext().getPackageName();
                        SharedPreferences sharedPreferences = activity.getSharedPreferences(packageName, Context.MODE_PRIVATE);


                        boolean mustActivateAccount = sharedPreferences.getBoolean(AttivaAccountController.SHARED_PREFERENCES_ACCOUNT_ACTIVATION, false);

                        if(mustActivateAccount){
                            String username = sharedPreferences.getString(AttivaAccountController.SHARED_PREFERENCES_USERNAME, null);
                            String password = sharedPreferences.getString(AttivaAccountController.SHARED_PREFERENCES_PASSWORD, null);

                            attivaAccountController.openAttivaAccountActivity(username,password);
                            //openAttivaAccountActivity(username,password);
                            return;
                        }

                        autenticazioneController.openAutenticazioneActivity();
                        //openAutenticazioneActivity();
                    }

                    activity.finish();
                },
                error -> {
                    Log.e(TAG, error.toString());
                    ExceptionHandler.handleMessageError(messageDialog,error);
                }
        );
* */