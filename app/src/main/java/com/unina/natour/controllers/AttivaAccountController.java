package com.unina.natour.controllers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AnonymousAWSCredentials;
import com.amazonaws.services.cognitoidentity.model.CognitoIdentityProvider;
import com.amazonaws.services.cognitoidentityprovider.AmazonCognitoIdentityProvider;

import com.amazonaws.services.cognitoidentityprovider.AmazonCognitoIdentityProviderClient;
import com.amazonaws.services.cognitoidentityprovider.model.AdminDeleteUserRequest;
import com.amplifyframework.core.Amplify;

import com.unina.natour.amplify.AmplifyUtils;
import com.unina.natour.controllers.exceptionHandler.ExceptionHandler;
import com.unina.natour.views.activities.AttivaAccountActivity;
import com.unina.natour.views.activities.AutenticazioneActivity;
import com.unina.natour.views.activities.RegistrazioneActivity;
import com.unina.natour.views.dialogs.MessageDialog;



public class AttivaAccountController {

    private final static String TAG ="AttivaAcountController";

    public static final String SHARED_PREFERENCES_ACCOUNT_ACTIVATION = "accountActivation";
    public static final String SHARED_PREFERENCES_USERNAME = "USERNAME";
    public static final String SHARED_PREFERENCES_PASSWORD = "PASSWORD";

    public static final String EXTRA_USERNAME = "USERNAME";
    public static final String EXTRA_PASSWORD = "PASSWORD";

    Activity activity;
    MessageDialog messageDialog;

    AutenticazioneController autenticazioneController;

    String username;
    String password;

    public AttivaAccountController(Activity activity){
        this.activity = activity;
        this.messageDialog = new MessageDialog(activity);

        this.autenticazioneController = new AutenticazioneController(activity);

        Intent intent = activity.getIntent();
        this.username = intent.getStringExtra(EXTRA_USERNAME);
        this.password = intent.getStringExtra(EXTRA_PASSWORD);

    }

    public void initAccountActivation(){
        String packageName = activity.getApplicationContext().getPackageName();
        SharedPreferences sharedPreferences = activity.getSharedPreferences(packageName,Context.MODE_PRIVATE);
        SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();

        sharedPreferencesEditor.putBoolean(SHARED_PREFERENCES_ACCOUNT_ACTIVATION,true);
        sharedPreferencesEditor.putString(SHARED_PREFERENCES_USERNAME,username);
        sharedPreferencesEditor.putString(SHARED_PREFERENCES_PASSWORD,password);
        sharedPreferencesEditor.commit();
    }

    public void activeAccount(String code){

        ExceptionHandler.isThereAnEmptyField(messageDialog,code);

        Amplify.Auth.confirmSignUp(
                username,
                code,
                confirmSignUpResult -> {
                    Log.i(TAG, "Confirm signUp succeeded");

                    String packageName = activity.getApplicationContext().getPackageName();
                    SharedPreferences sharedPreferences = activity.getSharedPreferences(packageName,Context.MODE_PRIVATE);
                    SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();

                    sharedPreferencesEditor.remove(SHARED_PREFERENCES_ACCOUNT_ACTIVATION);
                    sharedPreferencesEditor.remove(SHARED_PREFERENCES_USERNAME);
                    sharedPreferencesEditor.remove(SHARED_PREFERENCES_PASSWORD);
                    sharedPreferencesEditor.commit();

                    autenticazioneController.effectiveSignIn(username,password,true);


                },
                error -> {
                    ExceptionHandler.handleMessageError(messageDialog, error);
                }
        );
    }

    public void cancelAccountActivation(){


        /*
        AmplifyUtils amplifyUtils = new AmplifyUtils(activity);
        AmazonCognitoIdentityProviderClient cognitoIdentityProviderClient = new AmazonCognitoIdentityProviderClient();

        cognitoIdentityProviderClient.setRegion(amplifyUtils.getRegion());

        AdminDeleteUserRequest adminDeleteUserRequest = new AdminDeleteUserRequest();
        adminDeleteUserRequest.setUsername(username);
        adminDeleteUserRequest.setUserPoolId(amplifyUtils.getPoolId());

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                cognitoIdentityProviderClient.adminDeleteUser(adminDeleteUserRequest);
                Log.i(TAG, "Account deleted");
            }
        });

        thread.start();

*/



        //---

        String packageName = activity.getApplicationContext().getPackageName();
        SharedPreferences sharedPreferences = activity.getSharedPreferences(packageName,Context.MODE_PRIVATE);
        SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();

        sharedPreferencesEditor.remove(SHARED_PREFERENCES_ACCOUNT_ACTIVATION);
        sharedPreferencesEditor.remove(SHARED_PREFERENCES_USERNAME);
        sharedPreferencesEditor.remove(SHARED_PREFERENCES_PASSWORD);
        sharedPreferencesEditor.commit();

        activity.finish();

    }

    public void back(){
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }

    public void resendCode(){
        Amplify.Auth.resendSignUpCode(
                username,
                result -> {
                    Log.i(TAG, "resend Code Success");
                    Toast.makeText(activity, "Codice Inviato", Toast.LENGTH_SHORT).show();
                },
                error -> {
                    Log.e(TAG, "resend Code Failure");
                    ExceptionHandler.handleMessageError(messageDialog, error);
                }
        );
    }





    public void openAttivaAccountActivity(String username, String password){
        if( !(activity instanceof RegistrazioneActivity) ){
            Log.i(TAG, "open not from RegistrazioneActivity");
            Intent intentAutenticazioneActivity = new Intent(activity, AutenticazioneActivity.class);
            intentAutenticazioneActivity.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            activity.startActivity(intentAutenticazioneActivity);

            Intent intentRegistrazioneActivity = new Intent(activity, RegistrazioneActivity.class);
            intentRegistrazioneActivity.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            activity.startActivity(intentRegistrazioneActivity);
        }

        Intent intent = new Intent(activity, AttivaAccountActivity.class);
        intent.putExtra(EXTRA_USERNAME,username);
        intent.putExtra(EXTRA_PASSWORD,password);
        activity.startActivity(intent);


    }

}
