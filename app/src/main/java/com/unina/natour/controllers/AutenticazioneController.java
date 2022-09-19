package com.unina.natour.controllers;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.unina.natour.controllers.utils.StringsUtils;
import com.unina.natour.dto.response.MessageResponseDTO;
import com.unina.natour.models.dao.implementation.AmplifyDAO;
import com.unina.natour.views.activities.AutenticazioneActivity;
import com.unina.natour.views.activities.NaTourActivity;

@RequiresApi(api = Build.VERSION_CODES.N)
@SuppressLint("LongLogTag")
public class AutenticazioneController extends NaTourController{

    private AmplifyDAO amplifyDAO;

    public AutenticazioneController(NaTourActivity activity){
        super(activity);
        this.amplifyDAO = new AmplifyDAO();
    }


    public Boolean signIn(String usernameEmail, String password) {
        if(!StringsUtils.areAllFieldsFull(usernameEmail,password)){
            //TODO
            showErrorMessage(0);
            return false;
        }

        MessageResponseDTO messageResponseDTO = amplifyDAO.signIn(usernameEmail,password);
        if(messageResponseDTO.getCode() != MessageController.SUCCESS_CODE){
            showErrorMessage(messageResponseDTO);
            return false;
        }

        return true;
    }





    public static void openAutenticazioneActivity(NaTourActivity fromActivity){
        Intent intent = new Intent(fromActivity, AutenticazioneActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        fromActivity.startActivity(intent);
    }
}
