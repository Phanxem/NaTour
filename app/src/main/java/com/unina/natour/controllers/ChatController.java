package com.unina.natour.controllers;

import android.content.Intent;
import android.util.Log;
import android.widget.ListView;

import com.unina.natour.dto.response.MessageResponseDTO;
import com.unina.natour.dto.response.UserResponseDTO;
import com.unina.natour.models.ElementMessageModel;
import com.unina.natour.models.dao.implementation.UserDAOImpl;
import com.unina.natour.models.dao.interfaces.UserDAO;
import com.unina.natour.models.socketHandler.ChatWebSocketHandler;
import com.unina.natour.views.activities.AutenticazioneActivity;
import com.unina.natour.views.activities.ChatActivity;
import com.unina.natour.views.activities.NaTourActivity;
import com.unina.natour.views.listAdapters.ChatListAdapter;

import java.util.LinkedList;
import java.util.List;

public class ChatController extends NaTourController{


    private static final String EXTRA_SOURCE_USER_ID = "SOURCE_USER_ID";
    private static final String EXTRA_DESTATION_USER_ID = "DESTINATION_USER_ID";


    private ListaMessaggiController listaMessaggiController;

    private UserDAO userDAO;

    public ChatController(NaTourActivity activity){
        super(activity);

        this.userDAO = new UserDAOImpl(getActivity());

        //Amplify
        String user = "user";
        UserResponseDTO userResponseDTO = userDAO.getUser(user);
        MessageResponseDTO messageResponseDTO = userResponseDTO.getResultMessage();
        if(messageResponseDTO.getCode() != MessageController.SUCCESS_CODE){
            showErrorMessage(messageResponseDTO);
            return;
        }

        //TODO testing
        //long myId = userResponseDTO.getId();
        long myId = 11;

        Intent intent = getActivity().getIntent();
        long userId = intent.getLongExtra(EXTRA_DESTATION_USER_ID, -1);
        if(userId < 0){
            Log.i(TAG,"id non inserito");
            showErrorMessage(messageResponseDTO);
            return;
        }

        this.listaMessaggiController = new ListaMessaggiController(activity, myId, userId);


        /*
        this.messages = new LinkedList<ElementMessageModel>();

        ElementMessageModel message1 = new ElementMessageModel("hellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohello",true);
        ElementMessageModel message2 = new ElementMessageModel("hi",false);

        messages.add(message1);
        messages.add(message2);

        this.chatListAdapter = new ChatListAdapter(activity, messages);

        this.chatWebSocketHandler = new ChatWebSocketHandler(activity);
*/
    }


    public void sendMessage(){
        //TODO socket

    }

    public ListaMessaggiController getListaMessaggiController() {
        return listaMessaggiController;
    }

    public void setListaMessaggiController(ListaMessaggiController listaMessaggiController) {
        this.listaMessaggiController = listaMessaggiController;
    }


//TODO socketTests
    /*
    public void addMessage(ElementMessageModel message3){
        //Log.i(TAG, message.getMessage());

        chatListAdapter.add(message3);

        for(ElementMessageModel messageModel: messages){
            Log.i(TAG, messageModel.getMessage());
        }

        //chatListAdapter.notifyDataSetChanged();
    }

    public void receiveMessage(String message){
        ElementMessageModel receivedMessage = new ElementMessageModel(message,false);
        chatListAdapter.add(receivedMessage);

        for(ElementMessageModel messageModel: messages){
            Log.i(TAG, messageModel.getMessage());
        }
    }

    public List<ElementMessageModel> getMessages() {
        return messages;
    }

    public void setMessages(List<ElementMessageModel> messages) {
        this.messages = messages;
    }


//TESTING

    public void testOpen(){
        chatWebSocketHandler.openWebSocket();
    }

    public void testClose(){
        chatWebSocketHandler.closeWebSocket();
    }

    public void testSend(){
        String text = "testing";
        boolean result = chatWebSocketHandler.sendToWebSocket(text);
        if(result) {
            ElementMessageModel message = new ElementMessageModel(text,true);
            chatListAdapter.add(message);
        }

    }
    */

    public static void openChatActivity(NaTourActivity fromActivity, long idUser){
        Intent intent = new Intent(fromActivity, ChatActivity.class);
        intent.putExtra(EXTRA_DESTATION_USER_ID, idUser);
        fromActivity.startActivity(intent);
    }

}
