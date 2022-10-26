package com.unina.natour.controllers;

import android.content.Intent;

import androidx.fragment.app.Fragment;

import com.unina.natour.config.ApplicationController;
import com.unina.natour.controllers.socketHandler.ChatWebSocketHandler;
import com.unina.natour.views.activities.MainActivity;
import com.unina.natour.views.activities.NaTourActivity;

public class MainController extends NaTourController{


    public static final String KEY_CONTROLLER = "CONTROLLER";

    public Fragment currentFragment;

    public MainController(NaTourActivity activity){
        super(activity);

        this.currentFragment = null;

        ApplicationController applicationController = (ApplicationController) activity.getApplicationContext();
        ChatWebSocketHandler chatWebSocketHandler = applicationController.getChatWebSocketHandler();

        chatWebSocketHandler.openWebSocket();
    }

    public Fragment getCurrentFragment() {
        return currentFragment;
    }

    public void setCurrentFragment(Fragment currentFragment) {
        this.currentFragment = currentFragment;
    }

    public static void openMainActivity(NaTourActivity fromActivity){
        Intent intent = new Intent(fromActivity, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        fromActivity.startActivity(intent);
    }

}
