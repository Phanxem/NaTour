package com.unina.natour.views.activities;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.unina.natour.R;
import com.unina.natour.controllers.ChatController;
import com.unina.natour.controllers.ListaMessaggiController;

import java.util.Calendar;

//FATE PRELIMINARE DI TESTING
//facciamo in modo che tutti i messaggi che scriviamo vengono visualizzati come inviati da noi
//tutti i messaggi che arrivano vengono visualizzati come mandati da altri


public class ChatActivity extends NaTourActivity {

    private ChatController chatController;
    private ListaMessaggiController listaMessaggiController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        this.chatController = new ChatController(this);
        this.listaMessaggiController = chatController.getListaMessaggiController();

        RecyclerView recyclerView_messages= findViewById(R.id.Chat_recyclerView_messages);
        NestedScrollView nestedScrollView_messages = findViewById(R.id.Chat_nestedScrollView_messages);
        ProgressBar progressBar_messages = findViewById(R.id.Chat_progressBar_messages);

        listaMessaggiController.initList(nestedScrollView_messages,recyclerView_messages, progressBar_messages);

        pressBackIcon();
        pressMenuIcon();
        pressSendKey();
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
                //chatController.testClose();

            }
        });
    }

    public void pressMenuIcon(){
        ImageView imageView_menu = findViewById(R.id.Chat_imageView_menu);
        imageView_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO
                //chatController.testOpen();
            }
        });
    }

    public void pressUserAccount(){
        RelativeLayout relativeLayout_user = findViewById(R.id.ListElementItinerary_relativeLayout_user);
        relativeLayout_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO
                //chatController.testSend();
                NestedScrollView nestedScrollView_messages = findViewById(R.id.Chat_nestedScrollView_messages);

                chatController.receiveMessage("test hello", Calendar.getInstance());

                new Handler().postDelayed(new Runnable() {
                                              @Override
                                              public void run() {
                                                  nestedScrollView_messages.fullScroll(View.FOCUS_DOWN);
                                              }
                                          },
                        350);

            }
        });

        //profiloController.openProfiloActivity(long idUser);
    }

    public void pressSendKey(){
        EditText editText_messageField = findViewById(R.id.Chat_textField_messageField);
        NestedScrollView nestedScrollView_messages = findViewById(R.id.Chat_nestedScrollView_messages);


        editText_messageField.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER){

                    String message = String.valueOf(editText_messageField.getText());


                    chatController.sendMessage(message);

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            nestedScrollView_messages.fullScroll(View.FOCUS_DOWN);
                        }
                    },
                    350);

                    editText_messageField.setText(null);

                    return true;
                }
                return false;
            }
        });
    }


    public void receiveMessage(String message, Calendar time){
        NestedScrollView nestedScrollView_messages = findViewById(R.id.Chat_nestedScrollView_messages);

        chatController.receiveMessage(message, time);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                nestedScrollView_messages.fullScroll(View.FOCUS_DOWN);
            }
        },
        350);
    }
}