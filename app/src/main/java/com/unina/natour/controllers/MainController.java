package com.unina.natour.controllers;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.unina.natour.amplify.ApplicationController;
import com.unina.natour.models.socketHandler.ChatWebSocketHandler;
import com.unina.natour.views.activities.MainActivity;
import com.unina.natour.views.activities.NaTourActivity;
import com.unina.natour.views.dialogs.MessageDialog;

public class MainController extends NaTourController{


    public static final String KEY_CONTROLLER = "CONTROLLER";

    public MainController(NaTourActivity activity){
        super(activity);

        ApplicationController applicationController = (ApplicationController) activity.getApplicationContext();
        ChatWebSocketHandler chatWebSocketHandler = applicationController.getChatWebSocketHandler();

        chatWebSocketHandler.openWebSocket();

    }

    public static void openMainActivity(NaTourActivity fromActivity){
        Intent intent = new Intent(fromActivity, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        fromActivity.startActivity(intent);
    }

}
