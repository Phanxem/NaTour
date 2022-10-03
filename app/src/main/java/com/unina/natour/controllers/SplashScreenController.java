package com.unina.natour.controllers;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

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
import com.unina.natour.amplify.ApplicationController;
import com.unina.natour.controllers.utils.StringsUtils;
import com.unina.natour.controllers.exceptionHandler.exceptions.subAppException.NotCompletedFetchAuthSessionException;
import com.unina.natour.dto.response.CognitoAuthSessionDTO;
import com.unina.natour.dto.response.MessageResponseDTO;
import com.unina.natour.models.dao.implementation.AmplifyDAO;
import com.unina.natour.models.dao.implementation.ServerDAO;
import com.unina.natour.views.activities.ConnessioneServerFallitaActivity;
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

        ApplicationController applicationController = (ApplicationController) getActivity().getApplicationContext();
        Activity activity = applicationController.getCurrentActivity();

        if(!hasInternetConnection() || !isServerReachable()){
            if(activity instanceof ConnessioneServerFallitaActivity){
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(),"Connessione Fallita", Toast.LENGTH_SHORT);
                    }
                });
            }
            else ConnessioneServerFallitaActivity.openConnessioneServerFallitaActivity(getActivity());
            return;
        }


        if(isSignedIn()){
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
            String email = sharedPreferences.getString(AttivaAccountController.SHARED_PREFERENCES_EMAIL, null);

            AttivaAccountController.openAttivaAccountActivity(getActivity(),username,password, email);
            getActivity().finish();
            return;
        }

        AutenticazioneController.openAutenticazioneActivity(getActivity());
        getActivity().finish();

    }


    private boolean isSignedIn(){
        CognitoAuthSessionDTO cognitoAuthSessionDTO = amplifyDAO.fetchAuthSessione();
        MessageResponseDTO messageResponseDTO = cognitoAuthSessionDTO.getResultMessage();
        if(messageResponseDTO.getCode() != MessageController.SUCCESS_CODE){
            showErrorMessage(messageResponseDTO);
            //todo handle error
            return false;
        }

        //Signed in with cognito
        AWSCognitoAuthSession authSession = cognitoAuthSessionDTO.getAuthSessione();
        if(authSession.isSignedIn()){
            Log.i(TAG, "logged with Cognito");
            return true;
        }

        //Signed in with facebook
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        if(accessToken != null && !accessToken.isExpired()){
            Log.i(TAG, "logged with Facebook");
            return true;
        }

        //Signed in with google
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
            return true;
        }

        return false;
    }

    private boolean hasInternetConnection(){
        ConnectivityManager connectivityManager = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        if(connectivityManager != null){
            NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
            if (capabilities != null) {
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN))
                {
                    return true;
                }
                return false;
            }
        }
        return false;
    }

    private boolean isServerReachable(){
        MessageResponseDTO messageResponseDTO = ServerDAO.testServer();
        if(messageResponseDTO.getCode() != MessageController.SUCCESS_CODE){
            //todo handle error
            return false;
        }
        return true;
    }
}