package com.unina.natour.controllers;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobile.auth.core.IdentityManager;
import com.amazonaws.mobile.config.AWSConfiguration;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.Task;
import com.unina.natour.R;
import com.unina.natour.controllers.utils.StringsUtils;
import com.unina.natour.dto.response.ResultMessageDTO;
import com.unina.natour.models.dao.implementation.AmplifyDAO;
import com.unina.natour.views.activities.AutenticazioneActivity;
import com.unina.natour.views.activities.NaTourActivity;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AutenticazioneController extends NaTourController{

    public static final int FACEBOOK_LOGIN_CODE = 0;
    public static final int GOOGLE_LOGIN_CODE = 1;

    private static final String EMAIL = "email";

    private CallbackManager callbackManager = CallbackManager.Factory.create();
    private AmplifyDAO amplifyDAO;


    public AutenticazioneController(NaTourActivity activity){
        super(activity);
        this.amplifyDAO = new AmplifyDAO();
    }

    public Boolean signIn(String usernameEmail, String password) {
        if(!StringsUtils.areAllFieldsFull(usernameEmail,password)){
            //TODO
            showErrorMessage(0);
            return false;
        }

        ResultMessageDTO resultMessageDTO = amplifyDAO.signIn(usernameEmail,password);
        if(resultMessageDTO.getCode() != ResultMessageController.SUCCESS_CODE){
            showErrorMessage(resultMessageDTO);
            return false;
        }

        return true;
    }


    public void initButtonFacebook(LoginButton loginButton){
        loginButton.setPermissions(Arrays.asList(EMAIL));

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                if(loginResult.getAccessToken() == null || loginResult.getAccessToken().isExpired()){
                    //TODO
                    //showError
                }

                boolean result = federateWithFacebook(loginResult.getAccessToken());
                if(!result){
                    //TODO
                    //showError
                }
                Log.i(TAG, "----------------FB SUCCESS");


                //TODO
                /*
                cerca nel db se esiste un utente Facebook con l'id dell'utente appena loggato
                - se non esiste, crea un nuovo utente e inseriscilo
                 */

                MainController.openMainActivity(getActivity());

            }

            @Override
            public void onCancel() {
                Log.i(TAG, "FB CANCEL");
                //TODO do nothing
            }

            @Override
            public void onError(@NonNull FacebookException e) {
                //TODO show error
                Log.i(TAG, "FB ERROR");
            }
        });
    }

    private boolean federateWithFacebook(AccessToken accessToken){

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executorService.execute(new Runnable() {
            @Override
            public void run() {

                IdentityManager identityManager = new IdentityManager(
                        getActivity().getApplicationContext(),
                        new AWSConfiguration(getActivity().getApplicationContext()));

                IdentityManager.setDefaultIdentityManager(identityManager);

                CognitoCachingCredentialsProvider cccp = IdentityManager.getDefaultIdentityManager().getUnderlyingProvider();



                Map<String, String> logins = new HashMap<String, String>();
                logins.put("graph.facebook.com", accessToken.getToken());

                cccp.clear();
                cccp.setLogins(logins);
                cccp.refresh();

                Log.i(TAG, "FederatedLogin Facebook");

                //String username = Amplify.Auth.getCurrentUser();
                //Log.i(TAG, "Username: " + username);

                //String username = AccessToken.getCurrentAccessToken().getUserId();
                //Log.i(TAG, "Username: " + username);
                Log.i(TAG, "Token: " + cccp.getToken());

                Log.i(TAG, "AccessKey: " + cccp.getCredentials().getAWSAccessKeyId());
                Log.i(TAG, "SecretKey: " + cccp.getCredentials().getAWSSecretKey());
                Log.i(TAG, "SessionToken: " + cccp.getCredentials().getSessionToken());

                Log.i(TAG, "Facebook UserId: " + Profile.getCurrentProfile().getId());




        //----------------------------------------------------------------------------------------

/*
                String url = "https://vwyxm7bcre.execute-api.eu-west-1.amazonaws.com/testing/test";

                RequestBody requestBody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("test", "genos")
                        .addFormDataPart("side", "cutter")
                        .build();

                Request request = new Request.Builder()
                        .url(url)
                        //.post(requestBody)
                        .build();

                try {
                    request = ServerDAO.signRequest(request, cccp.getCredentials());
                } catch (Exception exception) {
                    exception.printStackTrace();
                }

                OkHttpClient client = new OkHttpClient();
                Call call = client.newCall(request);



                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        Log.e("TESTING----------: ", "error ",  e);
                    }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        Log.i("VAAAAAAAAAAAAAAAAAAAAAAAAA", "si, va");

                        if(!response.isSuccessful()){
                            Log.i("failure----------------", "failure");
                        }



                        Log.e("TESTING----------: ", response.body().string());
                    }
                });
*/




        //----------------------------------------------------------------------------------------

            }
        });



        return true;
    }

    public void callbackFacebook(int requestCode, int resultCode, Intent data){
        callbackManager.onActivityResult(requestCode, resultCode, data);
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

                CognitoCachingCredentialsProvider cccp = IdentityManager.getDefaultIdentityManager().getUnderlyingProvider();



                Map<String, String> logins = new HashMap<String, String>();
                logins.put("accounts.google.com", account.getIdToken());

                cccp.clear();
                cccp.setLogins(logins);
                cccp.refresh();

                Log.i(TAG, "FederatedLogin Google");

                //String username = Amplify.Auth.getCurrentUser();
                //Log.i(TAG, "Username: " + username);

                //String username = AccessToken.getCurrentAccessToken().getUserId();
                //Log.i(TAG, "Username: " + username);
                Log.i(TAG, "Token: " + cccp.getToken());

                Log.i(TAG, "AccessKey: " + cccp.getCredentials().getAWSAccessKeyId());
                Log.i(TAG, "SecretKey: " + cccp.getCredentials().getAWSSecretKey());
                Log.i(TAG, "SessionToken: " + cccp.getCredentials().getSessionToken());

                Log.i(TAG, "Google UserId: " + account.getId());



                //----------------------------------------------------------------------------------------
/*

                String url = "https://vwyxm7bcre.execute-api.eu-west-1.amazonaws.com/testing/test";

                RequestBody requestBody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("test", "genos")
                        .addFormDataPart("side", "cutter")
                        .build();

                Request request = new Request.Builder()
                        .url(url)
                        //.post(requestBody)
                        .build();

                try {
                    request = ServerDAO.signRequest(request, cccp.getCredentials());
                }
                catch (Exception exception) {
                    exception.printStackTrace();
                }


                OkHttpClient client = new OkHttpClient();
                Call call = client.newCall(request);


                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        Log.e("TESTING----------: ", "error ",  e);
                    }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        Log.i("VAAAAAAAAAAAAAAAAAAAAAAAAA", "si, va");

                        if(!response.isSuccessful()){
                            Log.i("failure----------------", "failure");
                        }



                        Log.e("TESTING----------: ", response.body().string());
                    }
                });




*/
                //----------------------------------------------------------------------------------------

            }
        });

        return true;
    }

    public void callbackGoogle(Intent data){

        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
        GoogleSignInAccount googleSignInAccount = task.getResult();
        boolean result = federateWithGoogle(googleSignInAccount);
        if(!result){
            //TODO
            //showError
        }

        /*
            cerca nel db se esiste un utente Facebook con l'id dell'utente appena loggato
            - se non esiste, crea un nuovo utente e inseriscilo
        */

        MainController.openMainActivity(getActivity());

    }



    public static void openAutenticazioneActivity(NaTourActivity fromActivity){
        Intent intent = new Intent(fromActivity, AutenticazioneActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        fromActivity.startActivity(intent);
    }


}
