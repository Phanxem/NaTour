package com.unina.natour.controllers;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobile.auth.core.IdentityManager;
import com.amazonaws.mobile.config.AWSConfiguration;
import com.amazonaws.regions.Regions;
import com.amplifyframework.auth.AuthSession;
import com.amplifyframework.auth.cognito.AWSCognitoAuthSession;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.Task;
import com.unina.natour.R;
import com.unina.natour.config.CurrentUserInfo;
import com.unina.natour.controllers.utils.StringsUtils;
import com.unina.natour.dto.request.SaveUserRequestDTO;
import com.unina.natour.dto.response.GetAuthSessionResponseDTO;
import com.unina.natour.dto.response.GetUserResponseDTO;
import com.unina.natour.dto.response.ResultMessageDTO;
import com.unina.natour.models.dao.implementation.AmplifyDAO;
import com.unina.natour.models.dao.implementation.UserDAOImpl;
import com.unina.natour.models.dao.interfaces.AccountDAO;
import com.unina.natour.models.dao.interfaces.UserDAO;
import com.unina.natour.views.activities.AutenticazioneActivity;
import com.unina.natour.views.activities.NaTourActivity;

import org.osmdroid.api.IGeoPoint;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class AutenticazioneController extends NaTourController{

    public static final int FACEBOOK_LOGIN_CODE = 0;
    public static final int GOOGLE_LOGIN_CODE = 1;

    private static final String EMAIL = "email";

    private CallbackManager callbackManager = CallbackManager.Factory.create();
    private AccountDAO accountDAO;
    private UserDAO userDAO;

    public AutenticazioneController(NaTourActivity activity,
                                    ResultMessageController resultMessageController,
                                    AccountDAO accountDAO,
                                    UserDAO userDAO){

        super(activity, resultMessageController);
        this.accountDAO = accountDAO;
        this.userDAO = userDAO;
    }

    public AutenticazioneController(NaTourActivity activity){
        super(activity);
        this.accountDAO = new AmplifyDAO();
        this.userDAO = new UserDAOImpl(activity);

        ProfileTracker profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(@Nullable Profile profile, @Nullable Profile profile1) {
                this.stopTracking();
                Profile.setCurrentProfile(profile1);
            }
        };
        profileTracker.startTracking();

    }

    public Boolean signIn(String username, String password) {
        Activity activity = getActivity();
        String messageToShow = null;

        if(!StringsUtils.areAllFieldsFull(username,password)){
            messageToShow = activity.getString(R.string.Message_EmptyFieldError);
            showErrorMessage(messageToShow);
            return false;
        }

        ResultMessageDTO resultMessageDTO = accountDAO.signIn(username,password);
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

        String identityProvider = activity.getString(R.string.IdentityProvider_Cognito);
        GetUserResponseDTO getUserResponseDTO = userDAO.getUserByIdP(identityProvider,username);
        if(!ResultMessageController.isSuccess(getUserResponseDTO.getResultMessage())){
            messageToShow = activity.getString(R.string.Message_UnknownError);
            showErrorMessage(messageToShow);
            return false;
        }

        CurrentUserInfo.set(getUserResponseDTO.getId(),identityProvider,username);


        String idIdentityPool = "eu-west-1:f7e961f5-0038-4fdd-9384-95671ef663d0";


        IdentityManager identityManager = new IdentityManager(
                getActivity().getApplicationContext(),
                new AWSConfiguration(getActivity().getApplicationContext()));

        IdentityManager.setDefaultIdentityManager(identityManager);

        CognitoCachingCredentialsProvider cognitoCachingCredentialsProvider = IdentityManager.getDefaultIdentityManager().getUnderlyingProvider();





        CognitoCachingCredentialsProvider cognitoCachingCredentialsProvider2 = new CognitoCachingCredentialsProvider(activity,idIdentityPool, Regions.EU_WEST_1);

        cognitoCachingCredentialsProvider = cognitoCachingCredentialsProvider2;

        //cognitoCachingCredentialsProvider.refresh();



        return true;
    }


    public void initButtonFacebook(LoginButton loginButton){
        Activity activity = getActivity();
        final String[] messageToShow = {null};

        loginButton.setPermissions(Arrays.asList(EMAIL));


        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                if(loginResult.getAccessToken() == null || loginResult.getAccessToken().isExpired()){
                    messageToShow[0] = activity.getString(R.string.Message_UnknownError);
                    showErrorMessage(messageToShow[0]);
                    return;
                }

                boolean result = federateWithFacebook(loginResult.getAccessToken());
                if(!result){
                    messageToShow[0] = activity.getString(R.string.Message_UnknownError);
                    showErrorMessage(messageToShow[0]);
                    return;
                }

                Log.i(TAG, "----------------FB SUCCESS");

/*
                String identityProvider = activity.getString(R.string.IdentityProvider_Facebook);


                Log.e(TAG, "fb in | id: " + Profile.getCurrentProfile().getId() + ", name: " + Profile.getCurrentProfile().getName());

                String idProvided = Profile.getCurrentProfile().getId();
                String username = Profile.getCurrentProfile().getName();

                GetUserResponseDTO getUserResponseDTO = userDAO.getUserByIdP(identityProvider, idProvided);
                if(ResultMessageController.isSuccess(getUserResponseDTO.getResultMessage())){
                    CurrentUserInfo.set(getUserResponseDTO.getId(),identityProvider,idProvided);
                    MainController.openMainActivity(getActivity());
                    return;
                }

                ResultMessageDTO resultMessageDTO = getUserResponseDTO.getResultMessage();
                if(resultMessageDTO.getCode() == ResultMessageController.ERROR_CODE_NOT_FOUND){
                    SaveUserRequestDTO saveUserRequestDTO = new SaveUserRequestDTO();
                    saveUserRequestDTO.setUsername(username);
                    saveUserRequestDTO.setIdentityProvider(identityProvider);
                    saveUserRequestDTO.setIdIdentityProvided(idProvided);

                    resultMessageDTO = userDAO.addUser(saveUserRequestDTO);
                    if(!ResultMessageController.isSuccess(resultMessageDTO)){
                        messageToShow[0] = activity.getString(R.string.Message_UnknownError);
                        showErrorMessage(messageToShow[0]);
                        return;
                    }

                    CurrentUserInfo.set(getUserResponseDTO.getId(),identityProvider,idProvided);
                    MainController.openMainActivity(getActivity());
                    return;
                }
                else{
                    messageToShow[0] = activity.getString(R.string.Message_UnknownError);
                    showErrorMessage(messageToShow[0]);
                    return;
                }
*/
            }

            @Override
            public void onCancel() {
                Log.i(TAG, "FB CANCEL");
            }

            @Override
            public void onError(@NonNull FacebookException e) {
                Log.i(TAG, "FB ERROR");
                messageToShow[0] = activity.getString(R.string.Message_UnknownError);
                showErrorMessage(messageToShow[0]);
                return;
            }
        });
    }

    private boolean federateWithFacebook(AccessToken accessToken){

        ExecutorService executorService = Executors.newSingleThreadExecutor();

        executorService.execute(new Runnable() {
            @Override
            public void run() {

                IdentityManager identityManager = new IdentityManager(
                        getActivity().getApplicationContext(),
                        new AWSConfiguration(getActivity().getApplicationContext()));

                IdentityManager.setDefaultIdentityManager(identityManager);

                CognitoCachingCredentialsProvider cognitoCachingCredentialsProvider = IdentityManager.getDefaultIdentityManager().getUnderlyingProvider();

                Map<String, String> logins = new HashMap<String, String>();
                logins.put("graph.facebook.com", accessToken.getToken());

                cognitoCachingCredentialsProvider.clear();
                cognitoCachingCredentialsProvider.setLogins(logins);
                cognitoCachingCredentialsProvider.refresh();

                Log.e(TAG, "FederatedLogin Facebook");
                Log.e(TAG, "Token: " + cognitoCachingCredentialsProvider.getToken());
                Log.e(TAG, "AccessKey: " + cognitoCachingCredentialsProvider.getCredentials().getAWSAccessKeyId());
                Log.e(TAG, "SecretKey: " + cognitoCachingCredentialsProvider.getCredentials().getAWSSecretKey());
                Log.e(TAG, "SessionToken: " + cognitoCachingCredentialsProvider.getCredentials().getSessionToken());
            }
        });

        executorService.shutdown();

        boolean finished = false;
        try {
            finished = executorService.awaitTermination(1, TimeUnit.MINUTES);
        }
        catch (InterruptedException e) {
            Log.i(TAG, "InterrupedException");
            return false;
        }

        return true;
    }

    public void callbackFacebook(int requestCode, int resultCode, Intent data){
        Activity activity = getActivity();
        String messageToShow = null;

        callbackManager.onActivityResult(requestCode, resultCode, data);



        //while(Profile.getCurrentProfile() == null) {}
        //Log.e(TAG,"Profile.getCurrentProfile() != null");

/*
        Log.i(TAG, "Facebook UserId: " + Profile.getCurrentProfile().getId());
        Log.i(TAG, "Facebook User Name: " + Profile.getCurrentProfile().getName());

        Log.e(TAG, "fb in | id: " + Profile.getCurrentProfile().getId() + ", name: " + Profile.getCurrentProfile().getName());

        String idProvided = Profile.getCurrentProfile().getId();
        String username = Profile.getCurrentProfile().getName();

        String identityProvider = activity.getString(R.string.IdentityProvider_Facebook);
        GetUserResponseDTO getUserResponseDTO = userDAO.getUserByIdP(identityProvider, idProvided);
        if(ResultMessageController.isSuccess(getUserResponseDTO.getResultMessage())){
            CurrentUserInfo.set(getUserResponseDTO.getId(),identityProvider,idProvided);
            MainController.openMainActivity(getActivity());
            return;
        }


        ResultMessageDTO resultMessageDTO = getUserResponseDTO.getResultMessage();
        if(resultMessageDTO.getCode() == ResultMessageController.ERROR_CODE_NOT_FOUND){
            SaveUserRequestDTO saveUserRequestDTO = new SaveUserRequestDTO();
            saveUserRequestDTO.setUsername(username);
            saveUserRequestDTO.setIdentityProvider(identityProvider);
            saveUserRequestDTO.setIdIdentityProvided(idProvided);

            resultMessageDTO = userDAO.addUser(saveUserRequestDTO);
            if(!ResultMessageController.isSuccess(resultMessageDTO)){
                messageToShow = activity.getString(R.string.Message_UnknownError);
                showErrorMessage(messageToShow);
                return;
            }

            CurrentUserInfo.set(getUserResponseDTO.getId(),identityProvider,idProvided);
            MainController.openMainActivity(getActivity());
            return;
        }
        else{
            messageToShow = activity.getString(R.string.Message_UnknownError);
            showErrorMessage(messageToShow);
            return;
        }

*/
    }

    public void addFacebookUser(){
        Activity activity  = getActivity();
        String messageToShow = null;

        if(Profile.getCurrentProfile() == null){
            Log.e(TAG,"Profile.getCurrentProfile() == null");
            return;
        }

        Log.i(TAG, "Facebook UserId: " + Profile.getCurrentProfile().getId());
        Log.i(TAG, "Facebook User Name: " + Profile.getCurrentProfile().getName());

        Log.e(TAG, "fb in | id: " + Profile.getCurrentProfile().getId() + ", name: " + Profile.getCurrentProfile().getName());

        String idProvided = Profile.getCurrentProfile().getId();
        String username = Profile.getCurrentProfile().getName();

        String identityProvider = activity.getString(R.string.IdentityProvider_Facebook);
        GetUserResponseDTO getUserResponseDTO = userDAO.getUserByIdP(identityProvider, idProvided);
        if(ResultMessageController.isSuccess(getUserResponseDTO.getResultMessage())){
            CurrentUserInfo.set(getUserResponseDTO.getId(),identityProvider,idProvided);
            MainController.openMainActivity(getActivity());
            return;
        }


        ResultMessageDTO resultMessageDTO = getUserResponseDTO.getResultMessage();
        if(resultMessageDTO.getCode() == ResultMessageController.ERROR_CODE_NOT_FOUND){
            SaveUserRequestDTO saveUserRequestDTO = new SaveUserRequestDTO();
            saveUserRequestDTO.setUsername(username);
            saveUserRequestDTO.setIdentityProvider(identityProvider);
            saveUserRequestDTO.setIdIdentityProvided(idProvided);

            resultMessageDTO = userDAO.addUser(saveUserRequestDTO);
            if(!ResultMessageController.isSuccess(resultMessageDTO)){
                messageToShow = activity.getString(R.string.Message_UnknownError);
                showErrorMessage(messageToShow);
                return;
            }

            CurrentUserInfo.set(getUserResponseDTO.getId(),identityProvider,idProvided);
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    MainController.openMainActivity(getActivity());
                }
            });


            return;
        }
        else{
            messageToShow = activity.getString(R.string.Message_UnknownError);
            showErrorMessage(messageToShow);
            return;
        }
    }


    public void initButtonGoogle(SignInButton googleLoginButton, ActivityResultLauncher<Intent> activityResultLauncher) {

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getActivity().getResources().getString(R.string.google_client))
                .requestEmail()
                .build();

        GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(getActivity(), googleSignInOptions);

        googleLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityResultLauncher.launch(googleSignInClient.getSignInIntent());
            }
        });

    }

    boolean federateWithGoogle(GoogleSignInAccount account){
        if(account.getIdToken() == null){
             return false;
        }


        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executorService.execute(new Runnable() {
            @Override
            public void run() {

                IdentityManager identityManager = new IdentityManager(
                        getActivity().getApplicationContext(),
                        new AWSConfiguration(getActivity().getApplicationContext()));

                IdentityManager.setDefaultIdentityManager(identityManager);

                CognitoCachingCredentialsProvider cognitoCachingCredentialsProvider = IdentityManager.getDefaultIdentityManager().getUnderlyingProvider();



                Map<String, String> logins = new HashMap<String, String>();
                logins.put("accounts.google.com", account.getIdToken());

                cognitoCachingCredentialsProvider.clear();
                cognitoCachingCredentialsProvider.setLogins(logins);
                cognitoCachingCredentialsProvider.refresh();

                Log.i(TAG, "FederatedLogin Google");

                Log.i(TAG, "Token: " + cognitoCachingCredentialsProvider.getToken());
                Log.i(TAG, "AccessKey: " + cognitoCachingCredentialsProvider.getCredentials().getAWSAccessKeyId());
                Log.i(TAG, "SecretKey: " + cognitoCachingCredentialsProvider.getCredentials().getAWSSecretKey());
                Log.i(TAG, "SessionToken: " + cognitoCachingCredentialsProvider.getCredentials().getSessionToken());
            }
        });

        executorService.shutdown();

        boolean finished = false;
        try {
            finished = executorService.awaitTermination(1, TimeUnit.MINUTES);
        }
        catch (InterruptedException e) {
            return false;
        }

        return true;
    }

    public void callbackGoogle(Intent data){
        Activity activity = getActivity();
        String messageToShow = null;

        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
        GoogleSignInAccount googleSignInAccount = task.getResult();
        boolean result = federateWithGoogle(googleSignInAccount);
        if(!result){
            messageToShow = activity.getString(R.string.Message_UnknownError);
            showErrorMessage(messageToShow);
            return;
        }


        String identityProvider = activity.getString(R.string.IdentityProvider_Google);
        //TODO da testare
        //String idProvided = loginResult.getAccessToken().getUserId();
        String idProvided = googleSignInAccount.getId();
        String username = googleSignInAccount.getDisplayName();

        GetUserResponseDTO getUserResponseDTO = userDAO.getUserByIdP(identityProvider, idProvided);
        if(ResultMessageController.isSuccess(getUserResponseDTO.getResultMessage())){
            CurrentUserInfo.set(getUserResponseDTO.getId(),identityProvider,idProvided);
            MainController.openMainActivity(getActivity());
            return;
        }

        ResultMessageDTO resultMessageDTO = getUserResponseDTO.getResultMessage();
        if(resultMessageDTO.getCode() == ResultMessageController.ERROR_CODE_NOT_FOUND){
            SaveUserRequestDTO saveUserRequestDTO = new SaveUserRequestDTO();
            saveUserRequestDTO.setUsername(username);
            saveUserRequestDTO.setIdentityProvider(identityProvider);
            saveUserRequestDTO.setIdIdentityProvided(idProvided);

            resultMessageDTO = userDAO.addUser(saveUserRequestDTO);
            if(!ResultMessageController.isSuccess(resultMessageDTO)){
                messageToShow = activity.getString(R.string.Message_UnknownError);
                showErrorMessage(messageToShow);
                return;
            }

            CurrentUserInfo.set(getUserResponseDTO.getId(),identityProvider,idProvided);
            MainController.openMainActivity(getActivity());
            return;
        }
        else{
            messageToShow = activity.getString(R.string.Message_UnknownError);
            showErrorMessage(messageToShow);
            return;
        }
    }



    public static void openAutenticazioneActivity(NaTourActivity fromActivity){
        Intent intent = new Intent(fromActivity, AutenticazioneActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        fromActivity.startActivity(intent);
    }


}
