package com.unina.natour.controllers;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.amplifyframework.auth.cognito.AWSCognitoAuthSession;
import com.amplifyframework.core.Amplify;
import com.facebook.AccessToken;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.Task;
import com.unina.natour.R;
import com.unina.natour.controllers.utils.StringsUtils;
import com.unina.natour.controllers.exceptionHandler.exceptions.subAppException.NotCompletedFetchAuthSessionException;
import com.unina.natour.dto.response.CognitoAuthSessionDTO;
import com.unina.natour.dto.response.MessageResponseDTO;
import com.unina.natour.models.dao.implementation.AmplifyDAO;
import com.unina.natour.views.activities.NaTourActivity;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RequiresApi(api = Build.VERSION_CODES.N)
public class SplashScreenController extends NaTourController{


    private AutenticazioneController autenticazioneController;
    private AmplifyDAO amplifyDAO;


    public SplashScreenController(NaTourActivity activity){
        super(activity);


        this.autenticazioneController = new AutenticazioneController(getActivity());
        this.amplifyDAO = new AmplifyDAO();
    }

    public void redirectToRightActivity(){

        CognitoAuthSessionDTO cognitoAuthSessionDTO = amplifyDAO.fetchAuthSessione();
        MessageResponseDTO messageResponseDTO = cognitoAuthSessionDTO.getResultMessage();
        if(messageResponseDTO.getCode() != MessageController.SUCCESS_CODE){
            showErrorMessage(messageResponseDTO);
            //todo handle error
            return;
        }

        //Signed in with cognito
        AWSCognitoAuthSession authSession = cognitoAuthSessionDTO.getAuthSessione();
        if(authSession.isSignedIn()){
            Log.i(TAG, "logged with Cognito");

            MainController.openMainActivity(getActivity());
            getActivity().finish();
            return;
        }


        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        if(accessToken != null && !accessToken.isExpired()){
            Log.i(TAG, "logged with Facebook");

            MainController.openMainActivity(getActivity());
            getActivity().finish();
            return;
        }

        GoogleSignInAccount googleSignInAccount = GoogleSignIn.getLastSignedInAccount(getActivity());
        if(googleSignInAccount != null && !googleSignInAccount.isExpired()){
            GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getActivity().getResources().getString(R.string.google_client))
                    .requestEmail()
                    .build();

            GoogleSignIn.getClient(getActivity(), googleSignInOptions)
                    .silentSignIn()
                    .continueWith((Continuation<GoogleSignInAccount, Void>) task -> {
                        GoogleSignInAccount account = task.getResult();
                        autenticazioneController.federateWithGoogle(account);
                        return null;
                    });

            Log.i(TAG, "logged with Google");

            MainController.openMainActivity(getActivity());
            getActivity().finish();
            return;
        }



        String packageName = getActivity().getApplicationContext().getPackageName();
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(packageName, Context.MODE_PRIVATE);

        boolean mustActivateAccount = sharedPreferences.getBoolean(AttivaAccountController.SHARED_PREFERENCES_ACCOUNT_ACTIVATION, false);

        if(mustActivateAccount){
            String username = sharedPreferences.getString(AttivaAccountController.SHARED_PREFERENCES_USERNAME, null);
            String password = sharedPreferences.getString(AttivaAccountController.SHARED_PREFERENCES_PASSWORD, null);

            AttivaAccountController.openAttivaAccountActivity(getActivity(),username,password);
            getActivity().finish();
            return;
        }

        AutenticazioneController.openAutenticazioneActivity(getActivity());
        getActivity().finish();

    }

}