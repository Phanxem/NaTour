package com.unina.natour.controllers.socketHandler;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import androidx.fragment.app.Fragment;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.unina.natour.R;
import com.unina.natour.config.ApplicationController;
import com.unina.natour.config.CurrentUserInfo;
import com.unina.natour.controllers.ChatController;
import com.unina.natour.controllers.CommunityController;
import com.unina.natour.controllers.ListaUtentiController;
import com.unina.natour.controllers.MainController;
import com.unina.natour.controllers.utils.TimeUtils;
import com.unina.natour.dto.response.ResultMessageDTO;
import com.unina.natour.models.ElementUserModel;
import com.unina.natour.views.activities.ChatActivity;
import com.unina.natour.views.activities.MainActivity;
import com.unina.natour.views.fragments.CommunityFragment;

import java.text.ParseException;
import java.util.Calendar;
import java.util.List;

import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class ChatWebSocketListener extends WebSocketListener {

    private final static String KEY_ACTION = "action";
    private final static String KEY_ID_USER = "idUser";

    private final static String ACTION_INIT_CONNECTION = "initConnection";

    private static final int NORMAL_CLOSURE_STATUS = 1000;
    private static final String TAG = "ChatWebSocketListener";


    private Context context;

    public ChatWebSocketListener(Context context){
        this.context = context;
    }


    @Override
    public void onOpen(WebSocket webSocket, Response response) {
        Log.i(TAG, "ON OPEN");

        if(!CurrentUserInfo.isSignedIn()){
            return;
        }

        String messageString = "{\"" + KEY_ACTION + "\" : \"" + ACTION_INIT_CONNECTION + "\", " +
                                "\"" + KEY_ID_USER + "\" : \"" + CurrentUserInfo.getId() + "\"}";

        boolean result = webSocket.send(messageString);
        if(!result){
            return;
        }

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

        //Arriva un Messaggio di risposta ad una richiesta
        if(jsonObject.has("code") && jsonObject.has("message")){
            long code = jsonObject.get("code").getAsLong();
            String message = jsonObject.get("message").getAsString();

            ResultMessageDTO resultMessageDTO = new ResultMessageDTO(code,message);
            return;
        }

        //Arriva un messaggio sconosciuto
        if(!jsonObject.has("idUserSource") ||
           !jsonObject.has("message") ||
           !jsonObject.has("inputTime") )
        {
            return;
        }


        //Arriva un messaggio
        String stringIdSource = jsonObject.get("idUserSource").getAsString();
        long idSource = Long.valueOf(stringIdSource);

        String message = jsonObject.get("message").getAsString();

        String stringInputTime = jsonObject.get("inputTime").getAsString();
        Calendar inputTime = null;
        try {
            inputTime = TimeUtils.toCalendar(stringInputTime);
        }
        catch (ParseException e) {
            System.out.println("Error: CalendarParser");
            return;
        }


        //Schermata Chat
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
            return;
        }

        //Schermata Main
        if(currentActivity instanceof MainActivity){
            Log.i(TAG, "Activity : MainActivity");

            MainActivity mainActivity = (MainActivity) currentActivity;
            MainController mainController = mainActivity.getMainController();

            mainController.setHasChatNotification(true);
            mainActivity.updateChatNotification();


            Fragment currentFragment = mainController.getCurrentFragment();

            //Fragment Community
            if(currentFragment instanceof CommunityFragment){

                CommunityFragment communityFragment = (CommunityFragment) currentFragment;
                CommunityController communityController =  communityFragment.getCommunityController();
                ListaUtentiController listaUtentiController = communityFragment.getListaUtentiController();

                //Gli elementi visualizzati sono le conversazioni effettuate
                long researchCode = listaUtentiController.getResearchCode();
                if(researchCode == ListaUtentiController.CODE_USER_WITH_CONVERSATION){
                    mainActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            listaUtentiController.receiveMessage(idSource);
                        }
                    });
                }
            }
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
        t.printStackTrace();
    }

}
