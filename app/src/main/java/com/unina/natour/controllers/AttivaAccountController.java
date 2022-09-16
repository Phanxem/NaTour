package com.unina.natour.controllers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentActivity;

import com.amplifyframework.core.Amplify;

import com.unina.natour.controllers.exceptionHandler.ExceptionHandler;
import com.unina.natour.controllers.exceptionHandler.exceptions.subAppException.NotCompletedConfirmSignUpException;
import com.unina.natour.controllers.exceptionHandler.exceptions.subAppException.EmptyFieldActivationAccountCodeException;
import com.unina.natour.views.activities.AttivaAccountActivity;
import com.unina.natour.views.activities.AutenticazioneActivity;
import com.unina.natour.views.activities.NaTourActivity;
import com.unina.natour.views.activities.RegistrazioneActivity;
import com.unina.natour.views.dialogs.MessageDialog;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RequiresApi(api = Build.VERSION_CODES.N)
public class AttivaAccountController extends NaTourController{

    private final static String TAG ="AttivaAcountController";

    public static final String SHARED_PREFERENCES_ACCOUNT_ACTIVATION = "accountActivation";
    public static final String SHARED_PREFERENCES_USERNAME = "USERNAME";
    public static final String SHARED_PREFERENCES_PASSWORD = "PASSWORD";

    public static final String EXTRA_USERNAME = "USERNAME";
    public static final String EXTRA_PASSWORD = "PASSWORD";

    AutenticazioneController autenticazioneController;

    String username;
    String password;


    public AttivaAccountController(NaTourActivity activity){
        super(activity);

        this.autenticazioneController = new AutenticazioneController(activity);

        Intent intent = activity.getIntent();
        this.username = intent.getStringExtra(EXTRA_USERNAME);
        this.password = intent.getStringExtra(EXTRA_PASSWORD);
    }

    public void initAccountActivation(){
        String packageName = getActivity().getApplicationContext().getPackageName();
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(packageName,Context.MODE_PRIVATE);
        SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();

        sharedPreferencesEditor.putBoolean(SHARED_PREFERENCES_ACCOUNT_ACTIVATION,true);
        sharedPreferencesEditor.putString(SHARED_PREFERENCES_USERNAME,username);
        sharedPreferencesEditor.putString(SHARED_PREFERENCES_PASSWORD,password);
        sharedPreferencesEditor.commit();
    }

    public Boolean activeAccount(String code){

        if(!ExceptionHandler.areAllFieldsFull(code)) {
            EmptyFieldActivationAccountCodeException exception = new EmptyFieldActivationAccountCodeException();
            ExceptionHandler.handleMessageError(getMessageDialog(),exception);
            return false;
        }

        CompletableFuture<Boolean> completableFuture = new CompletableFuture<Boolean>();

        Amplify.Auth.confirmSignUp(
                username,
                code,
                confirmSignUpResult -> {
                    Log.i(TAG, "Confirm signUp succeeded");

                    String packageName = getActivity().getApplicationContext().getPackageName();
                    SharedPreferences sharedPreferences = getActivity().getSharedPreferences(packageName,Context.MODE_PRIVATE);
                    SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();

                    sharedPreferencesEditor.remove(SHARED_PREFERENCES_ACCOUNT_ACTIVATION);
                    sharedPreferencesEditor.remove(SHARED_PREFERENCES_USERNAME);
                    sharedPreferencesEditor.remove(SHARED_PREFERENCES_PASSWORD);
                    sharedPreferencesEditor.commit();

                    Boolean result = autenticazioneController.effectiveSignIn(username,password);
                    completableFuture.complete(result);
                },
                error -> {
                    ExceptionHandler.handleMessageError(getMessageDialog(), error);
                    completableFuture.complete(false);
                }
        );

        Boolean result = true;
        try {
            result = completableFuture.get();
        }
        catch (ExecutionException | InterruptedException e) {
            NotCompletedConfirmSignUpException exception = new NotCompletedConfirmSignUpException(e);
            ExceptionHandler.handleMessageError(getMessageDialog(),exception);
            result = false;
        }

        return result;
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

        String packageName = getActivity().getApplicationContext().getPackageName();
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(packageName,Context.MODE_PRIVATE);
        SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();

        sharedPreferencesEditor.remove(SHARED_PREFERENCES_ACCOUNT_ACTIVATION);
        sharedPreferencesEditor.remove(SHARED_PREFERENCES_USERNAME);
        sharedPreferencesEditor.remove(SHARED_PREFERENCES_PASSWORD);
        sharedPreferencesEditor.commit();

        getActivity().finish();

    }

    public void back(){
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getActivity().startActivity(intent);
    }

    public void resendCode(){
        Amplify.Auth.resendSignUpCode(
                username,
                result -> {
                    Log.i(TAG, "resend Code Success");
                    Toast.makeText(getActivity(), "Codice Inviato", Toast.LENGTH_SHORT).show();
                },
                error -> {
                    Log.e(TAG, "resend Code Failure");
                    ExceptionHandler.handleMessageError(getMessageDialog(), error);
                }
        );
    }





    public static void openAttivaAccountActivity(NaTourActivity fromActivity, String username, String password){
        if( !(fromActivity instanceof RegistrazioneActivity) ){
            Intent intentAutenticazioneActivity = new Intent(fromActivity, AutenticazioneActivity.class);
            intentAutenticazioneActivity.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            fromActivity.startActivity(intentAutenticazioneActivity);

            Intent intentRegistrazioneActivity = new Intent(fromActivity, RegistrazioneActivity.class);
            intentRegistrazioneActivity.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            fromActivity.startActivity(intentRegistrazioneActivity);
        }

        Intent intent = new Intent(fromActivity, AttivaAccountActivity.class);
        intent.putExtra(EXTRA_USERNAME,username);
        intent.putExtra(EXTRA_PASSWORD,password);
        fromActivity.startActivity(intent);


    }

}
