package com.unina.natour.views;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.amplifyframework.core.Amplify;
import com.unina.natour.R;

public class SplashScreenActivity extends AppCompatActivity {

    private final static String TAG ="SplashScreenActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        //TESTING
        Amplify.Auth.fetchAuthSession(
                result -> Log.i(TAG, result.toString()),
                error -> Log.e(TAG, error.toString())
        );

    }
}