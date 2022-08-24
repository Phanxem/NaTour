package com.unina.natour.amplify;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.amplifyframework.AmplifyException;
import com.amplifyframework.api.aws.AWSApiPlugin;
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin;
import com.amplifyframework.core.Amplify;

public class ApplicationConfig extends Application {

    private final static String TAG = "AmplifyConfig";

    private static Context appContext;

    public void onCreate() {
        super.onCreate();
        this.appContext = getApplicationContext();

        try {
            Amplify.addPlugin(new AWSApiPlugin());
            Amplify.addPlugin(new AWSCognitoAuthPlugin());
            Amplify.configure(getApplicationContext());
            Log.i(TAG, "Initialized Amplify");
        }
        catch (AmplifyException error) {
            Log.e(TAG, "Could not initialize Amplify", error);
        }
    }

    public static Context getAppContext(){
        return appContext;
    }
}
