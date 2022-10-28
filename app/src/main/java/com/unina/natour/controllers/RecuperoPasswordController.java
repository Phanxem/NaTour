package com.unina.natour.controllers;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.unina.natour.R;
import com.unina.natour.controllers.utils.StringsUtils;
import com.unina.natour.dto.response.ResultMessageDTO;
import com.unina.natour.models.dao.implementation.AmplifyDAO;
import com.unina.natour.views.activities.CompletaRecuperoPasswordActivity;
import com.unina.natour.views.activities.IniziaRecuperoPasswordActivity;
import com.unina.natour.views.activities.NaTourActivity;

@SuppressLint("LongLogTag")
public class RecuperoPasswordController extends NaTourController{

    private AmplifyDAO amplifyDAO;

    public RecuperoPasswordController(NaTourActivity activity){
        super(activity);
        this.amplifyDAO = new AmplifyDAO();
    }

    public Boolean startPasswordRecovery(String username) {
        Activity activity = getActivity();
        String messageToShow = null;

        if(!StringsUtils.areAllFieldsFull(username)){
            messageToShow = activity.getString(R.string.Message_EmptyFieldError);
            showErrorMessage(messageToShow);
            return false;
        }

        Log.e(TAG, "1");

        ResultMessageDTO resultMessageDTO = amplifyDAO.startPasswordRecovery(username);

        Log.e(TAG, "2");

        if(!ResultMessageController.isSuccess(resultMessageDTO)){

            if(resultMessageDTO.getCode() == ResultMessageController.ERROR_CODE_AMPLIFY){
                Log.e(TAG, "ERROR2");
                messageToShow = ResultMessageController.findMessageFromAmplifyMessage(activity, resultMessageDTO.getMessage());
                showErrorMessage(messageToShow);
                return false;
            }

            Log.e(TAG, "ERROR3");
            messageToShow = activity.getString(R.string.Message_UnknownError);
            showErrorMessage(messageToShow);
            return false;
        }
        return true;
    }


    public Boolean completePasswordRecovery(String code, String password, String password2){
        Activity activity = getActivity();
        String messageToShow = null;

        if(!StringsUtils.areAllFieldsFull(code,password,password2)){
            messageToShow = activity.getString(R.string.Message_EmptyFieldError);
            showErrorMessageAndBack(messageToShow);
            return false;
        }

        if(password.equals(password2)){
            messageToShow = activity.getString(R.string.Message_UnmatchPasswordsError);
            showErrorMessageAndBack(messageToShow);
            return false;
        }

        ResultMessageDTO resultMessageDTO = amplifyDAO.completePasswordRecovery(code,password);
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
