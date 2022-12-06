package com.unina.natour.controllers.socketHandler;

import android.content.Context;

import com.unina.natour.config.CurrentUserInfo;
import com.unina.natour.controllers.ResultMessageController;
import com.unina.natour.dto.response.GetBitmapResponseDTO;
import com.unina.natour.dto.response.GetUserResponseDTO;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;

public class ChatWebSocketHandler implements  ChatWebSocketHandlerInterface{

    //testing socket
    private final static String URL_WEBSOCKET = "wss://q80y613dnc.execute-api.eu-west-1.amazonaws.com/production";
    private final static String URL_CONNECTION = "https://q80y613dnc.execute-api.eu-west-1.amazonaws.com/production/@connections";

    private final static String KEY_ACTION = "action";
    private final static String KEY_MESSAGE = "message";
    private final static String KEY_ID_USER = "idUser";
    private final static String KEY_INPUT_TIME = "inputTime";

    private final static String ACTION_SEND_MESSAGE = "sendMessage";
    private final static String ACTION_INIT_CONNECTION = "initConnection";

    private Context context;
    private WebSocket webSocket;


    public ChatWebSocketHandler(Context context){
        this.context = context;
    }

    public boolean openWebSocket(){
        if(!CurrentUserInfo.isSignedIn()) return false;

        Request request = new Request.Builder()
                .url(URL_WEBSOCKET)
                .build();

        ChatWebSocketListener listener = new ChatWebSocketListener(context);
        OkHttpClient client = new OkHttpClient();

        webSocket = client.newWebSocket(request, listener);

        ExecutorService executorService = client.dispatcher().executorService();
        executorService.shutdown();

        return true;
    }

    public void closeWebSocket(){
        if(webSocket != null) webSocket.close(1001, "Goodbye !");
    }

    public boolean sendMessageToWebSocket(String idUser, String message, String inputTime){

        boolean result = false;

        String messageString = "{\"" + KEY_ACTION + "\" : \"" + ACTION_SEND_MESSAGE + "\", " +
                                "\"" + KEY_MESSAGE + "\": \"" + message + "\", " +
                                "\"" + KEY_ID_USER + "\": \"" + idUser + "\", " +
                                "\"" + KEY_INPUT_TIME + "\": \"" + inputTime + "\"}";

        result = webSocket.send(messageString);

        return result;
    }


}
