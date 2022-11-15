package com.unina.natour.controllers.socketHandler;

import android.content.Context;

import com.unina.natour.config.CurrentUserInfo;

import java.util.concurrent.ExecutorService;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;

public interface ChatWebSocketHandlerInterface {
    
    public boolean openWebSocket();

    public void closeWebSocket();

    public boolean sendMessageToWebSocket(String idUser, String message, String inputTime);

    //public boolean initConnectionToWebSocket();
}
