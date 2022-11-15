package com.unina.natour.config;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.amplifyframework.AmplifyException;
import com.amplifyframework.api.aws.AWSApiPlugin;
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin;
import com.amplifyframework.core.Amplify;
import com.unina.natour.controllers.socketHandler.ChatWebSocketHandler;
import com.unina.natour.controllers.socketHandler.ChatWebSocketHandlerInterface;
import com.unina.natour.views.activities.NaTourActivity;

public class ApplicationController extends Application {

    private final static String TAG = "ApplicationConfig";



    private NaTourActivity currentActivity;
    private ChatWebSocketHandlerInterface chatWebSocketHandler;

    public void onCreate() {
        super.onCreate();

        try {
            Amplify.addPlugin(new AWSApiPlugin());
            Amplify.addPlugin(new AWSCognitoAuthPlugin());
            Amplify.configure(getApplicationContext());
            Log.i(TAG, "Initialized Amplify");
        }
        catch (AmplifyException error) {
            Log.e(TAG, "Could not initialize Amplify", error);
        }

        Application.ActivityLifecycleCallbacks activityLifecycleCallbacks = generateActivityLifecycleCallbacks();
        registerActivityLifecycleCallbacks(activityLifecycleCallbacks);

        this.chatWebSocketHandler = new ChatWebSocketHandler(this);
    }

    public Activity getCurrentActivity() {
        return currentActivity;
    }

    public void setCurrentActivity(NaTourActivity currentActivity) {
        this.currentActivity = currentActivity;
    }

    public ChatWebSocketHandlerInterface getChatWebSocketHandler() {
        return chatWebSocketHandler;
    }

    public void setChatWebSocketHandler(ChatWebSocketHandler chatWebSocketHandler) {
        this.chatWebSocketHandler = chatWebSocketHandler;
    }


    private Application.ActivityLifecycleCallbacks generateActivityLifecycleCallbacks(){

        ActivityLifecycleCallbacks result = new Application.ActivityLifecycleCallbacks(){
            @Override
            public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
                Log.i(TAG, "onActivityCreated");

            }

            @Override
            public void onActivityStarted(@NonNull Activity activity) {
                Log.i(TAG, "onActivityStarted");


            }

            @Override
            public void onActivityResumed(@NonNull Activity activity) {
                Log.i(TAG, "onActivityResumed");
                if(activity instanceof NaTourActivity){
                    currentActivity = (NaTourActivity) activity;

                }


                //currentActivity = activity;
            }

            @Override
            public void onActivityPaused(@NonNull Activity activity) {
                Log.i(TAG, "onActivityPaused");
                currentActivity = null;
            }

            @Override
            public void onActivityStopped(@NonNull Activity activity) {
                Log.i(TAG, "onActivityStopped");
            }

            @Override
            public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {
                Log.i(TAG, "onActivitySaveInstanceState");
            }

            @Override
            public void onActivityDestroyed(@NonNull Activity activity) {
                Log.i(TAG, "onActivityDestroyed");
            }
        };

        return result;
    }
}
