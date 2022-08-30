package com.unina.natour.controllers;

import android.widget.ListView;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.FragmentActivity;

import com.unina.natour.models.ChatMessageModel;
import com.unina.natour.models.RicercaPuntoModel;
import com.unina.natour.models.dao.implementation.AddressDAOImpl;
import com.unina.natour.models.dao.interfaces.AddressDAO;
import com.unina.natour.views.dialogs.MessageDialog;
import com.unina.natour.views.listAdapters.ChatListAdapter;
import com.unina.natour.views.listAdapters.RisultatiRicercaPuntoListAdapter;

import java.util.ArrayList;
import java.util.List;

public class ChatController {

    FragmentActivity activity;
    MessageDialog messageDialog;

    ChatListAdapter chatListAdapter;

    //TODO
    List<ChatMessageModel> messages;
    //MessageDAO messageDAO;

    public ChatController(FragmentActivity activity, MessageDialog messageDialog){
        this.activity = activity;
        this.messageDialog = messageDialog;

        this.messages = new ArrayList<ChatMessageModel>();

        ChatMessageModel message1 = new ChatMessageModel("hellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohello",true);
        ChatMessageModel message2 = new ChatMessageModel("hi",false);

        messages.add(message1);
        messages.add(message2);

        this.chatListAdapter = new ChatListAdapter(activity, messages);

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

}
