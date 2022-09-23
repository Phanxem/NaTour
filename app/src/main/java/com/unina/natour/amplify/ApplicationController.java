package com.unina.natour.amplify;

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
import com.unina.natour.models.socketHandler.ChatWebSocketHandler;
import com.unina.natour.views.activities.NaTourActivity;

public class ApplicationController extends Application {

    private final static String TAG = "ApplicationConfig";

    private NaTourActivity currentActivity;
    private ChatWebSocketHandler chatWebSocketHandler;

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
        //Thread.setDefaultUncaughtExceptionHandler(generateUncaughtExcetionHandler());



    }

    public Activity getCurrentActivity() {
        return currentActivity;
    }

    public void setCurrentActivity(NaTourActivity currentActivity) {
        this.currentActivity = currentActivity;
    }

    public ChatWebSocketHandler getChatWebSocketHandler() {
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



    /*
    private Thread.UncaughtExceptionHandler generateUncaughtExcetionHandler(){
        Thread.UncaughtExceptionHandler uncaughtExceptionHandler = new Thread.UncaughtExceptionHandler(){

            ExceptionHandler exceptionHandler = new ExceptionHandler();

            @Override
            public void uncaughtException(@NonNull Thread t, @NonNull Throwable e) {
                //Log.e(TAG,"TestGlobalExceptionHandler", e);

                Log.i(TAG,"pij o cazz mm'occ strunzz");

                MessageDialog messageDialog = new MessageDialog();
                messageDialog.setNaTourActivity(currentActivity);

                if(e instanceof AmplifyException){
                    AmplifyException ex = (AmplifyException) e;
                    exceptionHandler.handleMessageError(messageDialog,ex);
                    return;
                }
                if(e instanceof ServerException){
                    ServerException ex = (ServerException) e;
                    exceptionHandler.handleMessageError(messageDialog,ex);
                    return;
                }
                if(e instanceof AppException){
                    AppException ex = (AppException) e;
                    exceptionHandler.handleMessageError(messageDialog,ex);
                    return;
                }
                else{
                    exceptionHandler.handleMessageError(messageDialog);
                }



                //exceptionHandler.handleMessageError(messageDialog);
            }
        };

        return uncaughtExceptionHandler;
    }
*/


}
