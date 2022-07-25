package com.unina.natour.controllers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.amplifyframework.core.Amplify;
import com.unina.natour.R;

public class SplashScreenActivity extends AppCompatActivity {

    private final static String TAG ="SplashScreenActivity";

    public final static String SHARED_PREFERENCES_ACCOUNT_ACTIVATION = "accountActivation";
    public final static String SHARED_PREFERENCES_USERNAME = "username";
    public final static String SHARED_PREFERENCES_PASSWORD = "password";

    public final static String EXTRA_USERNAME = "username";
    public final static String EXTRA_PASSWORD = "password";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        redirectToRightActivity();

    }

    private void redirectToRightActivity(){
        Amplify.Auth.fetchAuthSession(
                result -> {
                    Log.i(TAG, result.toString());
                    if(result.isSignedIn()){
                        openHomeActivity();
                    }
                    else{
                        String packageName = getApplicationContext().getPackageName();
                        SharedPreferences sharedPreferences = getSharedPreferences(packageName, Context.MODE_PRIVATE);
                        boolean isAccountActive = sharedPreferences.getBoolean(SHARED_PREFERENCES_ACCOUNT_ACTIVATION, false);

                        if(!isAccountActive){
                            String username = sharedPreferences.getString(SHARED_PREFERENCES_USERNAME, null);
                            String password = sharedPreferences.getString(SHARED_PREFERENCES_PASSWORD, null);

                            openAttivaAccountActivity(username,password);
                            return;
                        }

                        openAutenticazioneActivity();
                    }
                },
                error -> {
                    Log.e(TAG, error.toString());
                }
        );
    }




    private void openHomeActivity(){
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    private void openAttivaAccountActivity(String username, String password){
        Intent intent = new Intent(this, AttivaAccountActivity.class);
        intent.putExtra(EXTRA_USERNAME,username);
        intent.putExtra(EXTRA_PASSWORD,password);
        startActivity(intent);
        finish();
    }

    private void openAutenticazioneActivity(){
        Intent intent = new Intent(this, AutenticazioneActivity.class);
        startActivity(intent);
        finish();
    }

}