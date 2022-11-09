package com.unina.natour.controllers;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.amplifyframework.auth.AuthUser;
import com.amplifyframework.auth.cognito.AWSCognitoAuthSession;
import com.amplifyframework.core.Amplify;
import com.facebook.AccessToken;
import com.facebook.Profile;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.Continuation;
import com.unina.natour.R;
import com.unina.natour.config.ApplicationController;
import com.unina.natour.config.CurrentUserInfo;
import com.unina.natour.dto.response.GetCognitoAuthSessionResponseDTO;
import com.unina.natour.dto.response.GetUserResponseDTO;
import com.unina.natour.dto.response.ResultMessageDTO;
import com.unina.natour.models.dao.implementation.AmplifyDAO;
import com.unina.natour.models.dao.implementation.ServerDAO;
import com.unina.natour.models.dao.implementation.UserDAOImpl;
import com.unina.natour.models.dao.interfaces.UserDAO;
import com.unina.natour.views.activities.ConnessioneServerFallitaActivity;
import com.unina.natour.views.activities.NaTourActivity;

@RequiresApi(api = Build.VERSION_CODES.N)
public class SplashScreenController extends NaTourController{


    private AutenticazioneController autenticazioneController;
    private AmplifyDAO amplifyDAO;
    private ServerDAO serverDAO;
    private UserDAO userDAO;

    public SplashScreenController(NaTourActivity activity,
                                  ResultMessageController resultMessageController,
                                  AutenticazioneController autenticazioneController,
                                  AmplifyDAO amplifyDAO,
                                  ServerDAO serverDAO,
                                  UserDAO userDAO)
    {
        super(activity, resultMessageController);

        this.autenticazioneController = autenticazioneController;
        this.amplifyDAO = amplifyDAO;
        this.serverDAO = serverDAO;
        this.userDAO = userDAO;
    }

    public SplashScreenController(NaTourActivity activity){
        super(activity);

        this.autenticazioneController = new AutenticazioneController(getActivity());
        this.amplifyDAO = new AmplifyDAO();
        this.serverDAO = new ServerDAO();
        this.userDAO = new UserDAOImpl(activity);
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

        //TODO dopo la fase di testing rimuovere----------------------------------------------------
/*
        DisconnessioneController disconnessioneController = new DisconnessioneController(getActivity());
        disconnessioneController.signOut();

        String packageName = getActivity().getApplicationContext().getPackageName();
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(packageName,Context.MODE_PRIVATE);
        SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();

        sharedPreferencesEditor.remove(AttivaAccountController.SHARED_PREFERENCES_ACCOUNT_ACTIVATION);
        sharedPreferencesEditor.remove(AttivaAccountController.SHARED_PREFERENCES_USERNAME);
        sharedPreferencesEditor.remove(AttivaAccountController.SHARED_PREFERENCES_PASSWORD);
        sharedPreferencesEditor.remove(AttivaAccountController.SHARED_PREFERENCES_EMAIL);
        sharedPreferencesEditor.commit();
*/
        //------------------------------------------------------------------------------------------

        if(isSignedIn()){
            updateCurrentUser();

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
        if(isSignedInWithCognito()) return true;

        if(isSignedInWithFacebook()) return true;

        if(isSignedInWithGoogle()) return true;

        return false;
    }


    private boolean isSignedInWithCognito(){
        Activity activity = getActivity();
        String messageToShow = null;

        GetCognitoAuthSessionResponseDTO getCognitoAuthSessionResponseDTO = amplifyDAO.fetchAuthSessione();
        ResultMessageDTO resultMessageDTO = getCognitoAuthSessionResponseDTO.getResultMessage();
        if(!ResultMessageController.isSuccess(resultMessageDTO)){
            if(resultMessageDTO.getCode() == ResultMessageController.ERROR_CODE_AMPLIFY){
                messageToShow = ResultMessageController.findMessageFromAmplifyMessage(activity, resultMessageDTO.getMessage());
                showErrorMessage(messageToShow);
                return false;
            }

            messageToShow = activity.getString(R.string.Message_UnknownError);
            showErrorMessage(messageToShow);
            return false;
        }



        //Signed in with cognito
        AWSCognitoAuthSession authSession = getCognitoAuthSessionResponseDTO.getAuthSessione();
        if(authSession.isSignedIn()){
            Log.i(TAG, "logged with Cognito");
            return true;
        }

        return false;
    }

    private boolean isSignedInWithFacebook(){
        //Signed in with facebook
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        if(accessToken != null && !accessToken.isExpired()){
            Log.i(TAG, "logged with Facebook");
            return true;
        }
        return false;
    }

    private boolean isSignedInWithGoogle(){
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

    private void updateCurrentUser(){
        Activity activity = getActivity();
        String messageToShow = null;

        CurrentUserInfo.clear();
        if(!isSignedIn()){
            return;
        }

        String identityProvider = null;
        String idIdentityProvided = null;
        long id = -1;

        if(isSignedInWithCognito()){
            identityProvider = activity.getString(R.string.IdentityProvider_Cognito);
            AuthUser currentUser = Amplify.Auth.getCurrentUser();
            idIdentityProvided = currentUser.getUsername();

            GetUserResponseDTO getUserResponseDTO = userDAO.getUserByIdP(identityProvider,idIdentityProvided);
            if(!ResultMessageController.isSuccess(getUserResponseDTO.getResultMessage())){
                messageToShow = activity.getString(R.string.Message_UnknownError);
                showErrorMessage(messageToShow);
                return;
            }

            id = getUserResponseDTO.getId();

            CurrentUserInfo.set(id,identityProvider,idIdentityProvided);
            return;
        }

        if(isSignedInWithFacebook()){
            identityProvider = activity.getString(R.string.IdentityProvider_Facebook);
            idIdentityProvided = Profile.getCurrentProfile().getId();

            GetUserResponseDTO getUserResponseDTO = userDAO.getUserByIdP(identityProvider,idIdentityProvided);
            if(!ResultMessageController.isSuccess(getUserResponseDTO.getResultMessage())){
                messageToShow = activity.getString(R.string.Message_UnknownError);
                showErrorMessage(messageToShow);
                return;
            }

            id = getUserResponseDTO.getId();

            CurrentUserInfo.set(id,identityProvider,idIdentityProvided);
            return;
        }

        if(isSignedInWithGoogle()){
            identityProvider = activity.getString(R.string.IdentityProvider_Facebook);

            GoogleSignInAccount googleSignInAccount = GoogleSignIn.getLastSignedInAccount(getActivity());
            idIdentityProvided = googleSignInAccount.getId();

            GetUserResponseDTO getUserResponseDTO = userDAO.getUserByIdP(identityProvider,idIdentityProvided);
            if(!ResultMessageController.isSuccess(getUserResponseDTO.getResultMessage())){
                messageToShow = activity.getString(R.string.Message_UnknownError);
                showErrorMessage(messageToShow);
                return;
            }

            id = getUserResponseDTO.getId();

            CurrentUserInfo.set(id,identityProvider,idIdentityProvided);
            return;
        }

        return;
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
        Activity activity = getActivity();
        String messageToShow = null;

        ResultMessageDTO resultMessageDTO = serverDAO.testServer();
        if(!ResultMessageController.isSuccess(resultMessageDTO)){

            if(resultMessageDTO == null){
                Log.e(TAG, "result null");
                messageToShow = activity.getString(R.string.Message_ServerError);
                showErrorMessage(messageToShow);
                return false;
            }

            Log.e(TAG, "code: " + resultMessageDTO.getCode());

            if(resultMessageDTO.getCode() == ResultMessageController.ERROR_CODE_SERVER){
                messageToShow = activity.getString(R.string.Message_ServerError);
                showErrorMessage(messageToShow);
                return false;
            }

            messageToShow = activity.getString(R.string.Message_UnknownError);
            showErrorMessage(messageToShow);
            return false;
        }

        Log.e(TAG, "true");
        return true;
    }
}