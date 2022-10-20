package com.unina.natour.controllers;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.unina.natour.controllers.utils.StringsUtils;
import com.unina.natour.dto.response.ResultMessageDTO;
import com.unina.natour.models.dao.implementation.AmplifyDAO;
import com.unina.natour.views.activities.CompletaRecuperoPasswordActivity;
import com.unina.natour.views.activities.IniziaRecuperoPasswordActivity;
import com.unina.natour.views.activities.NaTourActivity;

@RequiresApi(api = Build.VERSION_CODES.N)
@SuppressLint("LongLogTag")
public class RecuperoPasswordController extends NaTourController{

    private AmplifyDAO amplifyDAO;

    public RecuperoPasswordController(NaTourActivity activity){
        super(activity);
        this.amplifyDAO = new AmplifyDAO();
    }

    public Boolean startPasswordRecovery(String username) {
        if(!StringsUtils.areAllFieldsFull(username)){
            //TODO
            showErrorMessage(0);
            return false;
        }

        ResultMessageDTO resultMessageDTO = amplifyDAO.startPasswordRecovery(username);

        if(resultMessageDTO.getCode() != MessageController.SUCCESS_CODE){
            showErrorMessage(resultMessageDTO);
            return false;
        }

        return true;
    }


    public Boolean completePasswordRecovery(String code, String password, String password2){

        if(!StringsUtils.areAllFieldsFull(code,password,password2)){
            //TODO
            showErrorMessage(0);
            return false;
        }

        if(password.equals(password2)){
            //TODO
            showErrorMessage(0);
            return false;
        }

        ResultMessageDTO resultMessageDTO = amplifyDAO.completePasswordRecovery(code,password);

        if(resultMessageDTO.getCode() != MessageController.SUCCESS_CODE){
            showErrorMessage(resultMessageDTO);
            return false;
        }

        return true;
    }

    public void back() {
        getActivity().onBackPressed();
    }

    public static void openIniziaRecuperoPasswordActivity(NaTourActivity fromActivity){
        Intent intent = new Intent(fromActivity, IniziaRecuperoPasswordActivity.class);
        fromActivity.startActivity(intent);
    }

    public static void openCompletaRecuperoPasswordActivity(NaTourActivity fromActivity){
        Intent intent = new Intent(fromActivity, CompletaRecuperoPasswordActivity.class);
        fromActivity.startActivity(intent);
        fromActivity.finish();
    }


}
