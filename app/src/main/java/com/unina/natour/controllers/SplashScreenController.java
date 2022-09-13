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
import com.unina.natour.views.activities.NaTourActivity;
import com.unina.natour.views.dialogs.MessageDialog;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RequiresApi(api = Build.VERSION_CODES.N)
public class SplashScreenController extends NaTourController{

    MainController mainController;
    AttivaAccountController attivaAccountController;
    AutenticazioneController autenticazioneController;


    public SplashScreenController(NaTourActivity activity){
        super(activity);

        this.mainController = new MainController(activity);
        this.attivaAccountController = new AttivaAccountController(activity);
        this.autenticazioneController = new AutenticazioneController(activity);
    }

    public void redirectToRightActivity(){

        CompletableFuture<AWSCognitoAuthSession> completableFuture = new CompletableFuture<AWSCognitoAuthSession>();

        Amplify.Auth.fetchAuthSession(
                result -> {
                    AWSCognitoAuthSession cognitoAuthSession = (AWSCognitoAuthSession) result;
                    completableFuture.complete(cognitoAuthSession);
                },
                error -> {
                    ExceptionHandler.handleMessageError(getMessageDialog(),error);
                    completableFuture.complete(null);
                }
        );

        AWSCognitoAuthSession result = null;

        try {
            result = completableFuture.get();
        }
        catch (ExecutionException | InterruptedException e) {
            NotCompletedFetchAuthSessionException exception = new NotCompletedFetchAuthSessionException(e);
            ExceptionHandler.handleMessageError(getMessageDialog(),exception);

        }

        if(result != null && result.isSignedIn()){
            MainController.openMainActivity(getActivity());
        }
        else {
            String packageName = getActivity().getApplicationContext().getPackageName();
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences(packageName, Context.MODE_PRIVATE);

            boolean mustActivateAccount = sharedPreferences.getBoolean(AttivaAccountController.SHARED_PREFERENCES_ACCOUNT_ACTIVATION, false);

            if(mustActivateAccount){
                String username = sharedPreferences.getString(AttivaAccountController.SHARED_PREFERENCES_USERNAME, null);
                String password = sharedPreferences.getString(AttivaAccountController.SHARED_PREFERENCES_PASSWORD, null);

                attivaAccountController.openAttivaAccountActivity(getActivity(),username,password);
            }
            else autenticazioneController.openAutenticazioneActivity(getActivity());
        }

        getActivity().finish();
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