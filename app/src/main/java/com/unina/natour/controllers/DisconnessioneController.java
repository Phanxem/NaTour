package com.unina.natour.controllers;

import android.annotation.SuppressLint;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.unina.natour.amplify.ApplicationController;
import com.unina.natour.dto.response.MessageResponseDTO;
import com.unina.natour.models.dao.implementation.AmplifyDAO;
import com.unina.natour.models.socketHandler.ChatWebSocketHandler;
import com.unina.natour.views.activities.NaTourActivity;

@RequiresApi(api = Build.VERSION_CODES.N)
@SuppressLint("LongLogTag")
public class DisconnessioneController extends NaTourController{
    private AutenticazioneController autenticazioneController;

    private AmplifyDAO amplifyDAO;

    public DisconnessioneController(NaTourActivity activity){
        super(activity);

        this.autenticazioneController = new AutenticazioneController(activity);

        this.amplifyDAO = new AmplifyDAO();
    }


    public Boolean signOut(){
        MessageResponseDTO messageResponseDTO = amplifyDAO.signOut();

        if(messageResponseDTO.getCode() != MessageController.SUCCESS_CODE){
            showErrorMessage(messageResponseDTO);
            return false;
        }

        ApplicationController applicationController = (ApplicationController) getActivity().getApplicationContext();
        ChatWebSocketHandler chatWebSocketHandler = applicationController.getChatWebSocketHandler();

        chatWebSocketHandler.closeWebSocket();

        return true;
    }
}
