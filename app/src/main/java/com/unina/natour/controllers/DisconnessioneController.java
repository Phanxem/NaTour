package com.unina.natour.controllers;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.amplifyframework.auth.cognito.AWSCognitoAuthSession;
import com.facebook.AccessToken;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.unina.natour.R;
import com.unina.natour.config.ApplicationController;
import com.unina.natour.config.CurrentUserInfo;
import com.unina.natour.controllers.socketHandler.ChatWebSocketHandlerInterface;
import com.unina.natour.dto.response.GetAuthSessionResponseDTO;
import com.unina.natour.dto.response.ResultMessageDTO;
import com.unina.natour.models.dao.implementation.AmplifyDAO;
import com.unina.natour.controllers.socketHandler.ChatWebSocketHandler;
import com.unina.natour.models.dao.interfaces.AccountDAO;
import com.unina.natour.views.activities.NaTourActivity;

@SuppressLint("LongLogTag")
public class DisconnessioneController extends NaTourController{


    private AccountDAO accountDAO;

    public DisconnessioneController(NaTourActivity activity,
                                    ResultMessageController resultMessageController,
                                    AccountDAO accountDAO){
        super(activity, resultMessageController);


        this.accountDAO = accountDAO;
    }

    public DisconnessioneController(NaTourActivity activity){
        super(activity);

        this.accountDAO = new AmplifyDAO();
    }


    public Boolean signOut(){
        Activity activity = getActivity();
        String messageToShow = null;

        String identityProvider = CurrentUserInfo.getIdentityProvider();
            ResultMessageDTO resultMessageDTO = accountDAO.signOut();

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

            LoginManager.getInstance().logOut();

            GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getActivity());

            GoogleSignInOptions gso = new GoogleSignInOptions.
                    Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).
                    build();

            GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(getActivity(),gso);
            googleSignInClient.signOut();


        CurrentUserInfo.clear();

        ApplicationController applicationController = (ApplicationController) getActivity().getApplicationContext();
        ChatWebSocketHandlerInterface chatWebSocketHandler = applicationController.getChatWebSocketHandler();
        chatWebSocketHandler.closeWebSocket();

        return true;
    }
}
