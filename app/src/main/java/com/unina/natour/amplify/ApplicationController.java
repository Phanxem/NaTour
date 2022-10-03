package com.unina.natour.amplify;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.amplifyframework.AmplifyException;
import com.amplifyframework.api.aws.AWSApiPlugin;
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin;
import com.amplifyframework.core.Amplify;
import com.unina.natour.controllers.MessageController;
import com.unina.natour.dto.response.MessageResponseDTO;
import com.unina.natour.dto.response.UserIdResponseDTO;
import com.unina.natour.models.dao.implementation.UserDAOImpl;
import com.unina.natour.models.dao.interfaces.UserDAO;
import com.unina.natour.models.socketHandler.ChatWebSocketHandler;
import com.unina.natour.views.activities.NaTourActivity;

public class ApplicationController extends Application {

    private final static String TAG = "ApplicationConfig";

    private final static String IDP_COGNITO = "Cognito";
    private final static String IDP_FACEBOOK = "Facebook";
    private final static String IDP_GOOGLE = "Google";

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

        //FacebookSdk.fullyInitialize();
        //AppEventsLogger.activateApp(this);

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

    public static class UserInfo{
        private static String identityProvider;
        private static String userProviderId;
        private static Long userId;

        public static String getIdentityProvider() {
            return identityProvider;
        }

        public static String getUserProviderId() {
            return userProviderId;
        }

        public static Long getUserId() {
            return userId;
        }


        public static boolean signInWithCognito(Context context, String cognitoUsername){
            UserDAO userDAO = new UserDAOImpl(context);

            UserIdResponseDTO userIdResponseDTO = userDAO.getUserId(IDP_COGNITO, cognitoUsername);
            MessageResponseDTO messageResponseDTO = userIdResponseDTO.getResultMessage();
            if(messageResponseDTO.getCode() != MessageController.SUCCESS_CODE){
                //todo handle error
                return false;
            }

            identityProvider = IDP_COGNITO;
            userProviderId = cognitoUsername;
            userId = userIdResponseDTO.getUserId();
            return true;
        }

        public static boolean signInWithFacebook(Context context, String facebookId){
            UserDAO userDAO = new UserDAOImpl(context);

            UserIdResponseDTO userIdResponseDTO = userDAO.getUserId(IDP_FACEBOOK, facebookId);
            MessageResponseDTO messageResponseDTO = userIdResponseDTO.getResultMessage();
            if(messageResponseDTO.getCode() != MessageController.SUCCESS_CODE){
                //todo handle error
                return false;
            }

            identityProvider = IDP_FACEBOOK;
            userProviderId = facebookId;
            userId = userIdResponseDTO.getUserId();
            return true;
        }

        public static boolean signInWithGoogle(Context context, String googleId){
            UserDAO userDAO = new UserDAOImpl(context);

            UserIdResponseDTO userIdResponseDTO = userDAO.getUserId(IDP_GOOGLE, googleId);
            MessageResponseDTO messageResponseDTO = userIdResponseDTO.getResultMessage();
            if(messageResponseDTO.getCode() != MessageController.SUCCESS_CODE){
                //todo handle error
                return false;
            }

            identityProvider = IDP_GOOGLE;
            userProviderId = googleId;
            userId = userIdResponseDTO.getUserId();
            return true;
        }

        public static boolean signOut(){
            identityProvider = null;
            userProviderId = null;
            userId = null;
            return true;
        }

    }
}
