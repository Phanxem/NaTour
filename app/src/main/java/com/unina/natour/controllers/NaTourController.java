package com.unina.natour.controllers;

import com.unina.natour.dto.response.MessageResponseDTO;
import com.unina.natour.views.activities.NaTourActivity;

public class NaTourController {

    public final String TAG = this.getClass().getSimpleName();

    private NaTourActivity activity;
    private MessageController messageController;


    public NaTourController(NaTourActivity activity){
        this.activity = activity;
        this.messageController = new MessageController(activity);

    }

    public NaTourController(){}

    public NaTourActivity getActivity() {
        return activity;
    }

    public void setActivity(NaTourActivity activity) {
        this.activity = activity;
    }

    public MessageController getMessageController() {
        return messageController;
    }

    public void setErrorMessageController(MessageController messageController) {
        this.messageController = messageController;
    }

    public void showErrorMessage(MessageResponseDTO messageResponseDTO){
        this.messageController.showErrorMessage(messageResponseDTO);
    }

    public void showErrorMessage(long code){
        this.messageController.showErrorMessage(code);
    }

//---

}
