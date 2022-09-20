package com.unina.natour.models.socketHandler;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;
import okio.ByteString;

public class ChatWebSocketHandler {
    private final static String URL_WEBSOCKET = "wss://w7m7m8mork.execute-api.eu-west-1.amazonaws.com/production";
    private final static String URL_CONNECTION = "https://w7m7m8mork.execute-api.eu-west-1.amazonaws.com/production/@connections";

    private final static String KEY_ACTION = "action";
    private final static String KEY_MESSAGE = "message";
    private final static String KEY_USERNAME = "username";

    private final static String ACTION_SEND_MESSAGE = "sendmessage";

    private Context context;
    private WebSocket webSocket;


    public ChatWebSocketHandler(Context context){
        this.context = context;
    }

    public void openWebSocket(){
        Request request = new Request.Builder()
                .url(URL_WEBSOCKET)
                .build();

        ChatWebSocketListener listener = new ChatWebSocketListener(context);
        OkHttpClient client = new OkHttpClient();

        webSocket = client.newWebSocket(request, listener);

        client.dispatcher().executorService().shutdown();
    }

    public void closeWebSocket(){
        if(webSocket != null) webSocket.close(1001, "Goodbye !");
    }

    public boolean sendToWebSocket(String message){

        //TODO definire la web socket in modo che venga specificato anche il destinatario e l'orario di invio

        boolean result = false;
        //webSocket.send("{\"action\": \"sendmessage\", \"message\": \"hello, I'm connect\"}");
        result = webSocket.send("{\"" + KEY_ACTION + "\" : \"" + ACTION_SEND_MESSAGE + "\", \"" + KEY_MESSAGE + "\": \"" + message + "\"}");

        return result;
    }

}
