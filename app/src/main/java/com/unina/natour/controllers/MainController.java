package com.unina.natour.controllers;

import android.app.Activity;
import android.content.Intent;

import androidx.fragment.app.Fragment;

import com.unina.natour.R;
import com.unina.natour.config.ApplicationController;
import com.unina.natour.config.CurrentUserInfo;
import com.unina.natour.controllers.socketHandler.ChatWebSocketHandler;
import com.unina.natour.controllers.socketHandler.ChatWebSocketHandlerInterface;
import com.unina.natour.dto.response.HasMessageToReadResponseDTO;
import com.unina.natour.models.dao.implementation.ChatDAOImpl;
import com.unina.natour.models.dao.interfaces.ChatDAO;
import com.unina.natour.views.activities.MainActivity;
import com.unina.natour.views.activities.NaTourActivity;

import javax.xml.transform.Result;

public class MainController extends NaTourController{


    public static final String KEY_CONTROLLER = "CONTROLLER";

    private Fragment currentFragment;
    private boolean hasChatNotification;

    private ChatDAO chatDAO;

    public MainController(NaTourActivity activity,
                          ResultMessageController resultMessageController,
                          Fragment currentFragment,
                          boolean hasChatNotification,
                          ChatDAO chatDAO)
    {
        super(activity, resultMessageController);

        this.currentFragment = currentFragment;
        this.hasChatNotification = hasChatNotification;
        this.chatDAO = chatDAO;
    }

    public MainController(NaTourActivity activity){
        super(activity);
        String messageToShow = null;

        if(!CurrentUserInfo.isSignedIn()){
            messageToShow = activity.getString(R.string.Message_UnknownError);
            showErrorMessageAndBack(messageToShow);
            return ;
        }

        this.currentFragment = null;

        this.chatDAO = new ChatDAOImpl(activity);
/*
        HasMessageToReadResponseDTO hasMessageToReadResponseDTO = chatDAO.checkIfHasMessageToRead(CurrentUserInfo.getId());
        if(!ResultMessageController.isSuccess((hasMessageToReadResponseDTO.getResultMessage()))){
            messageToShow = activity.getString(R.string.Message_UnknownError);
            showErrorMessageAndBack(messageToShow);
            return;
        }
        hasChatNotification = hasMessageToReadResponseDTO.hasMessageToRead();
*/
        ApplicationController applicationController = (ApplicationController) activity.getApplicationContext();
        ChatWebSocketHandlerInterface chatWebSocketHandler = applicationController.getChatWebSocketHandler();

        chatWebSocketHandler.openWebSocket();
        //chatWebSocketHandler.initConnectionToWebSocket();
    }

    public Fragment getCurrentFragment() {
        return currentFragment;
    }

    public void setCurrentFragment(Fragment currentFragment) {
        this.currentFragment = currentFragment;
    }

    public void checkIfHasChatNotification(){
        Activity activity = getActivity();
        String messageToShow = null;

        if(!CurrentUserInfo.isSignedIn()){
            messageToShow = activity.getString(R.string.Message_UnknownError);
            showErrorMessageAndBack(messageToShow);
            return;
        }

        this.currentFragment = null;

        HasMessageToReadResponseDTO hasMessageToReadResponseDTO = chatDAO.checkIfHasMessageToRead(CurrentUserInfo.getId());
        if(!ResultMessageController.isSuccess((hasMessageToReadResponseDTO.getResultMessage()))){
            messageToShow = activity.getString(R.string.Message_UnknownError);
            showErrorMessageAndBack(messageToShow);
            return;
        }
        hasChatNotification = hasMessageToReadResponseDTO.hasMessageToRead();
    }

    public void back(){
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getActivity().startActivity(intent);
    }


    public boolean hasChatNotification() {
        return hasChatNotification;
    }

    public void setHasChatNotification(boolean hasChatNotification) {
        this.hasChatNotification = hasChatNotification;
    }

    public static void openMainActivity(NaTourActivity fromActivity){
        Intent intent = new Intent(fromActivity, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        fromActivity.startActivity(intent);
    }

}
