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
import com.unina.natour.dto.response.GetCognitoAuthSessionResponseDTO;
import com.unina.natour.dto.response.ResultMessageDTO;
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
        GetCognitoAuthSessionResponseDTO getCognitoAuthSessionResponseDTO = amplifyDAO.fetchAuthSessione();
        ResultMessageDTO resultMessageDTO = getCognitoAuthSessionResponseDTO.getResultMessage();
        if(resultMessageDTO.getCode() != MessageController.SUCCESS_CODE){
            showErrorMessage(resultMessageDTO);
            //todo handle error
            return false;
        }


        AWSCognitoAuthSession authSession = getCognitoAuthSessionResponseDTO.getAuthSessione();
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getActivity());

        //Signed in with cognito
        if(authSession.isSignedIn()){
            resultMessageDTO = amplifyDAO.signOut();

            if(resultMessageDTO.getCode() != MessageController.SUCCESS_CODE){
                showErrorMessage(resultMessageDTO);
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
