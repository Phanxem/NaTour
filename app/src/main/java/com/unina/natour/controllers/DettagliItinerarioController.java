package com.unina.natour.controllers;

import androidx.fragment.app.FragmentActivity;

import com.unina.natour.views.dialogs.MessageDialog;

public class DettagliItinerarioController {

    private final static String TAG ="HomeController";

    FragmentActivity activity;
    MessageDialog messageDialog;

    public DettagliItinerarioController(FragmentActivity activity, MessageDialog messageDialog){
        this.activity = activity;
        this.messageDialog = messageDialog;
    }

    public MessageDialog getMessageDialog() {
        return messageDialog;
    }

}
