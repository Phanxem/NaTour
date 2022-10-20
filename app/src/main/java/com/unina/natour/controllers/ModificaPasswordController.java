package com.unina.natour.controllers;

import android.content.Intent;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.unina.natour.controllers.utils.StringsUtils;
import com.unina.natour.dto.response.ResultMessageDTO;
import com.unina.natour.models.dao.implementation.AmplifyDAO;
import com.unina.natour.views.activities.ModificaPasswordActivity;
import com.unina.natour.views.activities.NaTourActivity;

@RequiresApi(api = Build.VERSION_CODES.N)
public class ModificaPasswordController extends NaTourController{

    private AmplifyDAO amplifyDAO;

    public ModificaPasswordController(NaTourActivity activity){
        super(activity);
        this.amplifyDAO = new AmplifyDAO();
    }


    public boolean updatePassword(String oldPassword, String newPassword, String newPassword2){
        if(!StringsUtils.areAllFieldsFull(oldPassword, newPassword, newPassword2)){
            //TODO exception
            return false;
        }
        if(!newPassword.equals(newPassword2)){
            //TODO exception
            return false;
        }

        ResultMessageDTO resultMessageDTO = amplifyDAO.updatePassword(oldPassword, newPassword);

        if(resultMessageDTO.getCode() != MessageController.SUCCESS_CODE){
            showErrorMessage(resultMessageDTO);
            return false;
        }

        return true;
    }


    public static void openModificaPasswordActivity(NaTourActivity fromActivity){
        Intent intent = new Intent(fromActivity, ModificaPasswordActivity.class);
        fromActivity.startActivity(intent);
    }
}
