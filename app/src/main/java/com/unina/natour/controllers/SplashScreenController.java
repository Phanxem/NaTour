package com.unina.natour.controllers;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.amplifyframework.core.Amplify;
import com.unina.natour.controllers.exceptionHandler.ExceptionHandler;
import com.unina.natour.views.activities.AttivaAccountActivity;
import com.unina.natour.views.activities.AutenticazioneActivity;
import com.unina.natour.views.activities.HomeActivity;
import com.unina.natour.views.activities.RegistrazioneActivity;
import com.unina.natour.views.dialogs.MessageDialog;

public class SplashScreenController {

    private final static String TAG ="SplashScreenController";

    Activity activity;
    MessageDialog messageDialog;

    HomeController homeController;
    AttivaAccountController attivaAccountController;
    AutenticazioneController autenticazioneController;

    public SplashScreenController(Activity activity){
        this.activity = activity;
        this.messageDialog = new MessageDialog(activity);

        this.homeController = new HomeController(activity);
        this.attivaAccountController = new AttivaAccountController(activity);
        this.autenticazioneController = new AutenticazioneController(activity);
    }

    public void redirectToRightActivity(){
        Amplify.Auth.fetchAuthSession(
                result -> {
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
    }

    /*
    public void openHomeActivity(){
        Intent intent = new Intent(activity, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        activity.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(activity).toBundle());
    }

    public void openAttivaAccountActivity(String username, String password){

        Intent intentAutenticazioneActivity = new Intent(activity, AutenticazioneActivity.class);
        intentAutenticazioneActivity.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        activity.startActivity(intentAutenticazioneActivity);

        Intent intentRegistrazioneActivity = new Intent(activity, RegistrazioneActivity.class);
        intentRegistrazioneActivity.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        activity.startActivity(intentRegistrazioneActivity);

        Intent intent = new Intent(activity, AttivaAccountActivity.class);
        intent.putExtra(AttivaAccountController.EXTRA_USERNAME,username);
        intent.putExtra(AttivaAccountController.EXTRA_PASSWORD,password);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        activity.startActivity(intent);
    }

    public void openAutenticazioneActivity(){
        Intent intent = new Intent(activity, AutenticazioneActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        activity.startActivity(intent);
    }
*/
}
