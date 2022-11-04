package com.unina.natour.controllers;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.unina.natour.R;
import com.unina.natour.config.ApplicationController;
import com.unina.natour.config.CurrentUserInfo;
import com.unina.natour.controllers.utils.TimeUtils;
import com.unina.natour.dto.response.ResultMessageDTO;
import com.unina.natour.models.dao.implementation.ChatDAOImpl;
import com.unina.natour.models.dao.implementation.UserDAOImpl;
import com.unina.natour.models.dao.interfaces.ChatDAO;
import com.unina.natour.models.dao.interfaces.UserDAO;
import com.unina.natour.controllers.socketHandler.ChatWebSocketHandler;
import com.unina.natour.views.activities.ChatActivity;
import com.unina.natour.views.activities.NaTourActivity;

import java.util.Calendar;

public class ChatController extends NaTourController{
    private static final String EXTRA_DESTATION_USER_ID = "DESTINATION_USER_ID";

    private ListaMessaggiController listaMessaggiController;

    long idOtherUser;

    private UserDAO userDAO;
    private ChatDAO chatDAO;


    public ChatController(NaTourActivity activity){
        super(activity);

        Intent intent = activity.getIntent();
        this.idOtherUser = intent.getLongExtra(EXTRA_DESTATION_USER_ID, -1);

        if(this.idOtherUser < 0){
            String messageToShow = activity.getString(R.string.Message_UnknownError);
            showErrorMessageAndBack(messageToShow);
            return;
        }

        this.userDAO = new UserDAOImpl(getActivity());
        this.chatDAO = new ChatDAOImpl(getActivity());


        this.listaMessaggiController = new ListaMessaggiController(activity, idOtherUser);
    }

    public ListaMessaggiController getListaMessaggiController() {
        return listaMessaggiController;
    }

    public void setListaMessaggiController(ListaMessaggiController listaMessaggiController) {
        this.listaMessaggiController = listaMessaggiController;
    }

    public long getIdOtherUser() {
        return idOtherUser;
    }

    public boolean sendMessage(String message){
        Activity activity = getActivity();
        String messageToShow = null;

        Calendar calendar = Calendar.getInstance();
        String stringInputTime = TimeUtils.toFullString(calendar);

        String stringId = String.valueOf(idOtherUser);

        ApplicationController applicationController = (ApplicationController) getActivity().getApplicationContext();
        ChatWebSocketHandler chatWebSocketHandler = applicationController.getChatWebSocketHandler();


        boolean result = chatWebSocketHandler.sendToWebSocket(stringId, message, stringInputTime);
        if(!result) {
            messageToShow = activity.getString(R.string.Message_UnknownError);
            showErrorMessage(messageToShow);
            return false;
        }

        listaMessaggiController.addMessageSent(message, Calendar.getInstance());
        return true;
    }

    public void receiveMessage(String message, Calendar calendar){
        listaMessaggiController.addMessageReceived(message, calendar);
    }

    public boolean readAllMessage(){
        if(!CurrentUserInfo.isSignedIn()){
            return false;
        }

        long myId = CurrentUserInfo.getId();

        ResultMessageDTO resultMessage = chatDAO.readAllMessageByIdsUser(myId, idOtherUser);
        if(!ResultMessageController.isSuccess(resultMessage)){
            return false;
        }

        return true;
    }


    public static void openChatActivity(NaTourActivity fromActivity, long idUser){
        Intent intent = new Intent(fromActivity, ChatActivity.class);
        intent.putExtra(EXTRA_DESTATION_USER_ID, idUser);
        fromActivity.startActivity(intent);
    }

}
