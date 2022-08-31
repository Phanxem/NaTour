package com.unina.natour.models.socketHandler;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.unina.natour.amplify.ApplicationConfig;
import com.unina.natour.controllers.ChatController;
import com.unina.natour.models.ChatMessageModel;
import com.unina.natour.views.activities.ChatActivity;

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
        webSocket.send("{\"action\": \"sendmessage\", \"message\": \"hello, I'm connect\"}");


        //webSocket.close(NORMAL_CLOSURE_STATUS, "Goodbye !");

    }

    @Override
    public void onMessage(WebSocket webSocket, String text) {
        Log.i(TAG, "ON MESSAGE (String) : " + text);

        ApplicationConfig applicationConfig = (ApplicationConfig) context.getApplicationContext();
        Activity currentActivity = applicationConfig.getCurrentActivity();

        if(currentActivity instanceof ChatActivity){
            Log.i(TAG, "Activity : ChatActivity");

            ChatActivity chatActivity = (ChatActivity) currentActivity;
            ChatController chatController = chatActivity.getChatController();

            ChatMessageModel messageModel = new ChatMessageModel(text, false);

            chatActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //chatController.receiveMessage(text);
                    chatController.addMessage(messageModel);
                }
            });
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
