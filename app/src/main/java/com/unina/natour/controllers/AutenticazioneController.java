package com.unina.natour.controllers;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

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
import com.unina.natour.amplify.SignOkHttp;
import com.unina.natour.controllers.utils.StringsUtils;
import com.unina.natour.controllers.utils.TimeUtils;
import com.unina.natour.dto.response.MessageResponseDTO;
import com.unina.natour.models.dao.implementation.AmplifyDAO;
import com.unina.natour.views.activities.AutenticazioneActivity;
import com.unina.natour.views.activities.NaTourActivity;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AutenticazioneController extends NaTourController{

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

        MessageResponseDTO messageResponseDTO = amplifyDAO.signIn(usernameEmail,password);
        if(messageResponseDTO.getCode() != MessageController.SUCCESS_CODE){
            showErrorMessage(messageResponseDTO);
            return false;
        }

        return true;
    }


    public boolean initButtonFB(LoginButton loginButton){
        loginButton.setPermissions(Arrays.asList(EMAIL));

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                if(loginResult.getAccessToken() != null && !loginResult.getAccessToken().isExpired())
                federateWithFB(loginResult.getAccessToken());
                Log.i(TAG, "----------------FB SUCCESS");


            }

            @Override
            public void onCancel() {
                Log.i(TAG, "FB CANCEL");
            }

            @Override
            public void onError(@NonNull FacebookException e) {
                Log.i(TAG, "FB ERROR");
            }
        });

        return true;
    }

    private void federateWithFB(AccessToken accessToken){

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


                String url = "https://vwyxm7bcre.execute-api.eu-west-1.amazonaws.com/testing/test";

                RequestBody requestBody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("test", "genos")
                        .addFormDataPart("side", "cutter")
                        .build();

                Request request = new Request.Builder()
                        .url(url)
                        .post(requestBody)
                        .build();


                ZonedDateTime time = ZonedDateTime.now(ZoneId.of("Europe/Dublin")).minusHours(1);
                String region = "eu-west-1";
                String service = "execute-api";

                String authorization = null;
                try {
                    String canonicalRequest = SignOkHttp.getCanonicalRequest("POST",
                                                    "/testing/test",
                                                    "",
                                                    "vwyxm7bcre.execute-api.eu-west-1.amazonaws.com",
                                                    time,
                                                    cccp.getCredentials().getSessionToken(),
                                                    request
                    );


                    String stringToSign = SignOkHttp.getStringToSign(time,region,service,canonicalRequest);

                    byte[] derivedSigningKey = SignOkHttp.getDerivedSigningKey(cccp.getCredentials().getAWSSecretKey(),
                                                                               time,
                                                                               region,
                                                                               service
                    );

                    String signature = SignOkHttp.getSignature(stringToSign,derivedSigningKey);

                    authorization = SignOkHttp.getAuthorization(cccp.getCredentials().getAWSAccessKeyId(),time,region,service,signature);

                }
                catch (IOException e) {
                    e.printStackTrace();
                }
                catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
                catch (Exception exception) {
                    exception.printStackTrace();
                }

                request = request.newBuilder()
                        .addHeader("X-Amz-Date", TimeUtils.toISO8601String(time))
                        .addHeader("X-Amz-Security-Token", cccp.getCredentials().getSessionToken())
                        .addHeader("Authorization", authorization)
                        .build();

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





        //----------------------------------------------------------------------------------------

            }
        });






        //cccp.withLogins(logins);
        //cccp.refresh();
    }



    public void callbackFB(int requestCode, int resultCode, Intent data){
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public static void openAutenticazioneActivity(NaTourActivity fromActivity){
        Intent intent = new Intent(fromActivity, AutenticazioneActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        fromActivity.startActivity(intent);
    }
}
