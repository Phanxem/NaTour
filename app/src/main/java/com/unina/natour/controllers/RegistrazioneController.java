package com.unina.natour.controllers;

import android.content.Intent;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.unina.natour.controllers.utils.StringsUtils;
import com.unina.natour.dto.response.MessageResponseDTO;
import com.unina.natour.models.dao.implementation.AmplifyDAO;
import com.unina.natour.views.activities.NaTourActivity;
import com.unina.natour.views.activities.RegistrazioneActivity;

@RequiresApi(api = Build.VERSION_CODES.N)
public class RegistrazioneController extends NaTourController{

    private AutenticazioneController autenticazioneController;

    private AmplifyDAO  amplifyDAO;
    
    public RegistrazioneController(NaTourActivity activity){
        super(activity);

        this.autenticazioneController = new AutenticazioneController(activity);

        this.amplifyDAO = new AmplifyDAO();
    }


    public Boolean signUp(String username, String email, String password){

        if(!StringsUtils.areAllFieldsFull(username,email,password)){
            //TODO
            showErrorMessage(0);
            return false;
        }

        MessageResponseDTO messageResponseDTO = amplifyDAO.signUp(username, email, password);

        if(messageResponseDTO.getCode() != MessageController.SUCCESS_CODE){
            showErrorMessage(messageResponseDTO);
            return false;
        }
        return true;
    }

    public static void openRegistrazioneActivity(NaTourActivity fromActivity){
        Intent intent = new Intent(fromActivity, RegistrazioneActivity.class);
        fromActivity.startActivity(intent);
    }

    public void back() {
        getActivity().onBackPressed();
    }
}
