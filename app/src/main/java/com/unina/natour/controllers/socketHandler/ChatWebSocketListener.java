package com.unina.natour.controllers.socketHandler;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import androidx.fragment.app.Fragment;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.unina.natour.config.ApplicationController;
import com.unina.natour.controllers.ChatController;
import com.unina.natour.controllers.CommunityController;
import com.unina.natour.controllers.ListaUtentiController;
import com.unina.natour.controllers.MainController;
import com.unina.natour.controllers.utils.TimeUtils;
import com.unina.natour.views.activities.ChatActivity;
import com.unina.natour.views.activities.MainActivity;
import com.unina.natour.views.fragments.CommunityFragment;

import java.text.ParseException;
import java.util.Calendar;

import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class ChatWebSocketListener extends WebSocketListener {

    private static final int NORMAL_CLOSURE_STATUS = 1000;
    private static final String TAG = "ChatWebSocketListener";

    private Context context;

    public ChatWebSocketListener(Context context){
        this.context = context;
    }


    @Override
    public void onOpen(WebSocket webSocket, Response response) {
        Log.i(TAG, "ON OPEN");
        //webSocket.send("{\"action\": \"sendmessage\", \"message\": \"hello, I'm connect\"}");


        //webSocket.close(NORMAL_CLOSURE_STATUS, "Goodbye !");

    }

    @Override
    public void onMessage(WebSocket webSocket, String text) {
        Log.i(TAG, "ON MESSAGE (String) : " + text);

        ApplicationController applicationController = (ApplicationController) context.getApplicationContext();
        Activity currentActivity = applicationController.getCurrentActivity();


        JsonElement jsonElement = null;
        try {
            jsonElement = JsonParser.parseString(text);
        }
        catch(Exception e){
            System.out.println("Error: JsonParser");
            return;
        }

        JsonObject jsonObject = jsonElement.getAsJsonObject();

        String stringIdSource = jsonObject.get("idUserSource").getAsString();
        String message = jsonObject.get("message").getAsString();
        String stringInputTime = jsonObject.get("inputTime").getAsString();

        long idSource = Long.valueOf(stringIdSource);
        Calendar inputTime = null;
        try {
            inputTime = TimeUtils.toCalendar(stringInputTime);
        }
        catch (ParseException e) {
            System.out.println("Error: CalendarParser");
            return;
        }


        if(currentActivity instanceof ChatActivity){
            Log.i(TAG, "Activity : ChatActivity");

            ChatActivity chatActivity = (ChatActivity) currentActivity;
            ChatController chatController = chatActivity.getChatController();
            long idOtherUser = chatController.getIdOtherUser();

            //sono nella chat dell'utente che mi ha inviato il message
            if(idSource == idOtherUser){

                Calendar finalInputTime = inputTime;
                chatActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        chatActivity.receiveMessage(message, finalInputTime);
                    }
                });

            }
            //sono in un'altra chat
            else{

            }
        }

        if(currentActivity instanceof MainActivity){
            Log.i(TAG, "Activity : MainActivity");

            MainActivity mainActivity = (MainActivity) currentActivity;
            MainController mainController = mainActivity.getMainController();

            Fragment currentFragment = mainController.getCurrentFragment();

            if(currentFragment instanceof CommunityFragment){

                CommunityFragment communityFragment = (CommunityFragment) currentFragment;
                CommunityController communityController =  communityFragment.getCommunityController();
                ListaUtentiController listaUtentiController = communityFragment.getListaUtentiController();
                //TODO rimuovi chat e riaggiungila in cima alla lista, con toRead=true
            }

            //TODO Se non è presente, aggiungi notifica all'icona Community
            return;
        }

    }

    @Override
    public void onMessage(WebSocket webSocket, ByteString bytes) {
        Log.i(TAG, "ON MESSAGE (bytes)");
    }
    @Override
    public void onClosing(WebSocket webSocket, int code, String reason) {
        Log.i(TAG, "ON CLOSING");

        webSocket.send("I'm disconnecting!");
        webSocket.close(NORMAL_CLOSURE_STATUS, null);

    }
    @Override
    public void onFailure(WebSocket webSocket, Throwable t, Response response) {
        Log.i(TAG, "ON FAILURE");
        //t.printStackTrace();
    }

}
