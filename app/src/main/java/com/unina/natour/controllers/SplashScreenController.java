package com.unina.natour.controllers;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.amplifyframework.auth.cognito.AWSCognitoAuthSession;
import com.amplifyframework.core.Amplify;
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



    AmplifyDAO amplifyDAO;


    public SplashScreenController(NaTourActivity activity){
        super(activity);



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

        AWSCognitoAuthSession authSession = cognitoAuthSessionDTO.getAuthSessione();

        if(authSession.isSignedIn()){
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