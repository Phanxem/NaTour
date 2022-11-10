package com.unina.natour.controllers;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.unina.natour.R;
import com.unina.natour.controllers.utils.StringsUtils;
import com.unina.natour.dto.response.ResultMessageDTO;
import com.unina.natour.models.dao.implementation.AmplifyDAO;
import com.unina.natour.models.dao.interfaces.AccountDAO;
import com.unina.natour.views.activities.ModificaPasswordActivity;
import com.unina.natour.views.activities.NaTourActivity;

public class ModificaPasswordController extends NaTourController{

    private AccountDAO accountDAO;

    public ModificaPasswordController(NaTourActivity activity,
                                      ResultMessageController resultMessageController,
                                      AccountDAO accountDAO){
        super(activity, resultMessageController);
        this.accountDAO = accountDAO;
    }

    public ModificaPasswordController(NaTourActivity activity){
        super(activity);
        this.accountDAO = new AmplifyDAO();
    }


    public boolean updatePassword(String oldPassword, String newPassword1, String newPassword2){
        Activity activity = getActivity();
        String messageToShow = null;

        if(!StringsUtils.areAllFieldsFull(oldPassword, newPassword1, newPassword2)){
            messageToShow = activity.getString(R.string.Message_EmptyFieldError);
            showErrorMessageAndBack(messageToShow);
            return false;
        }
        if(!newPassword1.equals(newPassword2)){
            messageToShow = activity.getString(R.string.Message_UnmatchPasswordsError);
            showErrorMessageAndBack(messageToShow);
            return false;
        }

        ResultMessageDTO resultMessageDTO = accountDAO.updatePassword(oldPassword, newPassword1);
        if(!ResultMessageController.isSuccess(resultMessageDTO)){
            if(resultMessageDTO.getCode() == ResultMessageController.ERROR_CODE_AMPLIFY){
                messageToShow = ResultMessageController.findMessageFromAmplifyMessage(activity, resultMessageDTO.getMessage());
                showErrorMessage(messageToShow);
                return false;
            }

            messageToShow = activity.getString(R.string.Message_UnknownError);
            showErrorMessage(messageToShow);
            return false;
        }

        return true;
    }


    public static void openModificaPasswordActivity(NaTourActivity fromActivity){
        Intent intent = new Intent(fromActivity, ModificaPasswordActivity.class);
        fromActivity.startActivity(intent);
    }
}
