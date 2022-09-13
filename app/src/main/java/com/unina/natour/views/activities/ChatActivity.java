package com.unina.natour.views.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.unina.natour.R;
import com.unina.natour.controllers.ChatController;
import com.unina.natour.views.dialogs.MessageDialog;

//FATE PRELIMINARE DI TESTING
//facciamo in modo che tutti i messaggi che scriviamo vengono visualizzati come inviati da noi
//tutti i messaggi che arrivano vengono visualizzati come mandati da altri


public class ChatActivity extends NaTourActivity {


    //TODO
    //ProfiloController profiloController;

    ChatController chatController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        this.chatController = new ChatController(this);

        ListView listView_messages = findViewById(R.id.Chat_listView_messages);
        chatController.initListViewChat(listView_messages);


        pressBackIcon();
        pressMenuIcon();
        pressUserAccount();



    }

    public ChatController getChatController() {
        return chatController;
    }

    public void pressBackIcon(){
        ImageView imageView_back = findViewById(R.id.Chat_imageView_indietro);
        imageView_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO
                chatController.testClose();
            }
        });
    }

    public void pressMenuIcon(){
        ImageView imageView_menu = findViewById(R.id.Chat_imageView_menu);
        imageView_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO
                chatController.testOpen();
            }
        });
    }

    public void pressUserAccount(){
        RelativeLayout relativeLayout_user = findViewById(R.id.ListElementItinerary_relativeLayout_user);
        relativeLayout_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO
                chatController.testSend();
            }
        });

        //profiloController.openProfiloActivity(long idUser);
    }

    public void pressSendKey(){
        EditText editText_messageField = findViewById(R.id.Chat_textField_messageField);
        editText_messageField.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER){
                    //TODO
                    return true;
                }
                return false;
            }
        });
    }

}