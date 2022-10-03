package com.unina.natour.controllers;

import android.annotation.SuppressLint;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.amplifyframework.auth.cognito.AWSCognitoAuthSession;
import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.unina.natour.amplify.ApplicationController;
import com.unina.natour.dto.response.CognitoAuthSessionDTO;
import com.unina.natour.dto.response.MessageResponseDTO;
import com.unina.natour.models.dao.implementation.AmplifyDAO;
import com.unina.natour.models.socketHandler.ChatWebSocketHandler;
import com.unina.natour.views.activities.NaTourActivity;

@RequiresApi(api = Build.VERSION_CODES.N)
@SuppressLint("LongLogTag")
public class DisconnessioneController extends NaTourController{
    private AutenticazioneController autenticazioneController;

    private AmplifyDAO amplifyDAO;

    public DisconnessioneController(NaTourActivity activity){
        super(activity);

        this.autenticazioneController = new AutenticazioneController(activity);

        this.amplifyDAO = new AmplifyDAO();
    }


    public Boolean signOut(){

        //loggato con cognito
        CognitoAuthSessionDTO cognitoAuthSessionDTO = amplifyDAO.fetchAuthSessione();
        MessageResponseDTO messageResponseDTO = cognitoAuthSessionDTO.getResultMessage();
        if(messageResponseDTO.getCode() != MessageController.SUCCESS_CODE){
            showErrorMessage(messageResponseDTO);
            //todo handle error
            return false;
        }


        AWSCognitoAuthSession authSession = cognitoAuthSessionDTO.getAuthSessione();
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getActivity());

        //Signed in with cognito
        if(authSession.isSignedIn()){
            messageResponseDTO = amplifyDAO.signOut();

            if(messageResponseDTO.getCode() != MessageController.SUCCESS_CODE){
                showErrorMessage(messageResponseDTO);
                return false;
            }
        }
        //Signed in with facebook
        else if(accessToken != null && !accessToken.isExpired()){
            LoginManager.getInstance().logOut();
        }
        //Signed in with google
        else if(account != null && !account.isExpired()){
            GoogleSignInOptions gso = new GoogleSignInOptions.
                    Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).
                    build();

            GoogleSignInClient googleSignInClient=GoogleSignIn.getClient(getActivity(),gso);
            googleSignInClient.signOut();
        }
        else{
            //TODO exception
            return false;
        }



        ApplicationController applicationController = (ApplicationController) getActivity().getApplicationContext();
        ChatWebSocketHandler chatWebSocketHandler = applicationController.getChatWebSocketHandler();

        chatWebSocketHandler.closeWebSocket();
        return true;
    }
}
