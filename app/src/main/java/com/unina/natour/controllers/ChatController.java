package com.unina.natour.controllers;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;

import com.unina.natour.R;
import com.unina.natour.config.ApplicationController;
import com.unina.natour.config.CurrentUserInfo;
import com.unina.natour.controllers.socketHandler.ChatWebSocketHandlerInterface;
import com.unina.natour.controllers.utils.TimeUtils;
import com.unina.natour.dto.response.ResultMessageDTO;
import com.unina.natour.dto.response.composted.GetUserWithImageResponseDTO;
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

    private long idOtherUser;
    private String username;
    private Bitmap profileImage;

    private Long idChat = null;

    private UserDAO userDAO;
    private ChatDAO chatDAO;

    public ChatController(NaTourActivity activity,
                          ResultMessageController resultMessageController,
                          ListaMessaggiController listaMessaggiController,
                          long idOtherUser,
                          String username,
                          Bitmap profileImage,
                          Long idChat,
                          UserDAO userDAO,
                          ChatDAO chatDAO){

        super(activity, resultMessageController);

        this.listaMessaggiController = listaMessaggiController;
        this.idOtherUser = idOtherUser;
        this.username = username;
        this.profileImage = profileImage;
        this.idChat = idChat;
        this.userDAO = userDAO;
        this.chatDAO = chatDAO;
    }

    public ChatController(NaTourActivity activity){
        super(activity);
        String messageToShow = null;

        if(!CurrentUserInfo.isSignedIn()){
            messageToShow = activity.getString(R.string.Message_UnknownError);
            showErrorMessageAndBack(messageToShow);
            return;
        }

        Intent intent = activity.getIntent();
        this.idOtherUser = intent.getLongExtra(EXTRA_DESTATION_USER_ID, -1);

        if(this.idOtherUser < 0){
            messageToShow = activity.getString(R.string.Message_UnknownError);
            showErrorMessageAndBack(messageToShow);
            return;
        }

        this.userDAO = new UserDAOImpl(getActivity());
        this.chatDAO = new ChatDAOImpl(getActivity());

        GetUserWithImageResponseDTO getUserWithImageResponseDTO = userDAO.getUserWithImageById(idOtherUser);
        if(!ResultMessageController.isSuccess(getUserWithImageResponseDTO.getResultMessage())){
            messageToShow = activity.getString(R.string.Message_UnknownError);
            showErrorMessageAndBack(messageToShow);
            return;
        }

        username = getUserWithImageResponseDTO.getUsername();
        profileImage = getUserWithImageResponseDTO.getProfileImage();

        this.listaMessaggiController = new ListaMessaggiController(activity, idOtherUser);
    }

    public boolean readAllMessage(){
        Activity activity = getActivity();
        String messageToShow = null;

        if(!CurrentUserInfo.isSignedIn()){
            messageToShow = activity.getString(R.string.Message_UnknownError);
            showErrorMessage(messageToShow);
            return false;
        }

        ResultMessageDTO resultMessageDTO = chatDAO.readAllMessageByIdsUser(CurrentUserInfo.getId(), idOtherUser);
        if(!ResultMessageController.isSuccess(resultMessageDTO)){
            messageToShow = activity.getString(R.string.Message_UnknownError);
            showErrorMessage(messageToShow);
            return false;
        }

        return true;
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
        ChatWebSocketHandlerInterface chatWebSocketHandler = applicationController.getChatWebSocketHandler();

        boolean result = chatWebSocketHandler.sendMessageToWebSocket(stringId, message, stringInputTime);
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




    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Bitmap getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(Bitmap profileImage) {
        this.profileImage = profileImage;
    }

    public static void openChatActivity(NaTourActivity fromActivity, long idUser){
        Intent intent = new Intent(fromActivity, ChatActivity.class);
        intent.putExtra(EXTRA_DESTATION_USER_ID, idUser);
        fromActivity.startActivity(intent);
    }

}
