package com.unina.natour.controllers;

import android.annotation.SuppressLint;
import android.app.Activity;

import com.amplifyframework.auth.cognito.AWSCognitoAuthSession;
import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.unina.natour.R;
import com.unina.natour.config.ApplicationController;
import com.unina.natour.dto.response.GetAuthSessionResponseDTO;
import com.unina.natour.dto.response.ResultMessageDTO;
import com.unina.natour.models.dao.implementation.AmplifyDAO;
import com.unina.natour.controllers.socketHandler.ChatWebSocketHandler;
import com.unina.natour.models.dao.interfaces.AccountDAO;
import com.unina.natour.views.activities.NaTourActivity;

@SuppressLint("LongLogTag")
public class DisconnessioneController extends NaTourController{
    private AutenticazioneController autenticazioneController;

    private AccountDAO accountDAO;

    public DisconnessioneController(NaTourActivity activity,
                                    ResultMessageController resultMessageController,
                                    AutenticazioneController autenticazioneController,
                                    AccountDAO accountDAO){
        super(activity, resultMessageController);

        this.autenticazioneController = autenticazioneController;

        this.accountDAO = accountDAO;
    }

    public DisconnessioneController(NaTourActivity activity){
        super(activity);

        this.autenticazioneController = new AutenticazioneController(activity);

        this.accountDAO = new AmplifyDAO();
    }


    public Boolean signOut(){
        Activity activity = getActivity();
        String messageToShow = null;

        //loggato con cognito
        GetAuthSessionResponseDTO getAuthSessionResponseDTO = accountDAO.fetchAuthSessione();
        ResultMessageDTO resultMessageDTO = getAuthSessionResponseDTO.getResultMessage();
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

        AWSCognitoAuthSession authSession = getAuthSessionResponseDTO.getAuthSessione();
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getActivity());

        //Signed in with cognito
        if(authSession.isSignedIn()){
            resultMessageDTO = accountDAO.signOut();

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
            messageToShow = activity.getString(R.string.Message_UnknownError);
            showErrorMessage(messageToShow);
            return false;
        }

        ApplicationController applicationController = (ApplicationController) getActivity().getApplicationContext();
        ChatWebSocketHandler chatWebSocketHandler = applicationController.getChatWebSocketHandler();
        chatWebSocketHandler.closeWebSocket();

        return true;
    }
}
