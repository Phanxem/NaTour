package com.unina.natour.views.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import com.unina.natour.R;
import com.unina.natour.controllers.ChatController;
import com.unina.natour.views.dialogs.MessageDialog;

//FATE PRELIMINARE DI TESTING
//facciamo in modo che tutti i messaggi che scriviamo vengono visualizzati come inviati da noi
//tutti i messaggi che arrivano vengono visualizzati come mandati da altri


public class ChatActivity extends AppCompatActivity {


    //TODO
    //ProfiloController profiloController;

    ChatController chatController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        MessageDialog messageDialog = new MessageDialog();
        messageDialog.setFragmentActivity(this);

        this.chatController = new ChatController(this, messageDialog);

        ListView listView_messages = findViewById(R.id.Chat_listView_messages);
        chatController.initListViewChat(listView_messages);


    }

    public void pressBackIcon(){

    }

    public void pressMenuIcon(){

    }

    public void pressUserAccount(){
        //profiloController.openProfiloActivity(long idUser);
    }

    public void sendMessage(){

    }

}