package com.unina.natour.controllers;

import android.util.Log;
import android.widget.ListView;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.FragmentActivity;

import com.unina.natour.models.ChatMessageModel;
import com.unina.natour.models.RicercaPuntoModel;
import com.unina.natour.models.dao.implementation.AddressDAOImpl;
import com.unina.natour.models.dao.interfaces.AddressDAO;
import com.unina.natour.models.socketHandler.ChatWebSocketHandler;
import com.unina.natour.views.activities.NaTourActivity;
import com.unina.natour.views.dialogs.MessageDialog;
import com.unina.natour.views.listAdapters.ChatListAdapter;
import com.unina.natour.views.listAdapters.RisultatiRicercaPuntoListAdapter;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ChatController extends NaTourController{


    ChatListAdapter chatListAdapter;

    ChatWebSocketHandler chatWebSocketHandler;


    List<ChatMessageModel> messages;
    //TODO
    //MessageDAO messageDAO;

    public ChatController(NaTourActivity activity){
        super(activity);

        this.messages = new LinkedList<ChatMessageModel>();

        ChatMessageModel message1 = new ChatMessageModel("hellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohello",true);
        ChatMessageModel message2 = new ChatMessageModel("hi",false);

        messages.add(message1);
        messages.add(message2);

        this.chatListAdapter = new ChatListAdapter(activity, messages);

        this.chatWebSocketHandler = new ChatWebSocketHandler(activity);

    }

    public void initListViewChat(ListView listView_messages) {
        listView_messages.setAdapter(chatListAdapter);
    }

    public void back(){

    }

    public void menu() {

    }

    public void sendMessage(){
        //TODO socket

    }

    public void addMessage(ChatMessageModel message3){
        //Log.i(TAG, message.getMessage());

        chatListAdapter.add(message3);

        for(ChatMessageModel messageModel: messages){
            Log.i(TAG, messageModel.getMessage());
        }

        //chatListAdapter.notifyDataSetChanged();
    }

    public void receiveMessage(String message){
        ChatMessageModel receivedMessage = new ChatMessageModel(message,false);
        chatListAdapter.add(receivedMessage);

        for(ChatMessageModel messageModel: messages){
            Log.i(TAG, messageModel.getMessage());
        }
    }

    public List<ChatMessageModel> getMessages() {
        return messages;
    }

    public void setMessages(List<ChatMessageModel> messages) {
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
            ChatMessageModel message = new ChatMessageModel(text,true);
            chatListAdapter.add(message);
        }

    }
}
