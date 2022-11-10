package com.unina.natour.controllers;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.unina.natour.R;
import com.unina.natour.controllers.utils.EmailUtils;
import com.unina.natour.controllers.utils.StringsUtils;
import com.unina.natour.dto.response.ResultMessageDTO;
import com.unina.natour.models.dao.implementation.AmplifyDAO;
import com.unina.natour.models.dao.interfaces.AccountDAO;
import com.unina.natour.views.activities.NaTourActivity;
import com.unina.natour.views.activities.RegistrazioneActivity;

public class RegistrazioneController extends NaTourController{

    public final static int MIN_LENGHT_PASSWORD = 8;

    private AutenticazioneController autenticazioneController;

    private AccountDAO accountDAO;

    public RegistrazioneController(NaTourActivity activity,
                                   ResultMessageController resultMessageController,
                                   AutenticazioneController autenticazioneController,
                                   AccountDAO accountDAO)
    {
        super(activity, resultMessageController);

        this.autenticazioneController = autenticazioneController;

        this.accountDAO = accountDAO;
    }

    public RegistrazioneController(NaTourActivity activity){
        super(activity);

        this.autenticazioneController = new AutenticazioneController(activity);

        this.accountDAO = new AmplifyDAO();
    }



    public Boolean signUp(String username, String email, String password){
        Activity activity = getActivity();
        String messageToShow = null;

        if(!StringsUtils.areAllFieldsFull(username,email,password)){
            messageToShow = activity.getString(R.string.Message_EmptyFieldError);
            showErrorMessageAndBack(messageToShow);
            return false;
        }

        if(password.length() < MIN_LENGHT_PASSWORD){
            messageToShow = activity.getString(R.string.AmplifyException_PasswordNotConformPolicy);
            showErrorMessageAndBack(messageToShow);
            return false;
        }

        if(!EmailUtils.isEmail(email)){
            messageToShow = activity.getString(R.string.AmplifyException_InvalidEmailFormat);
            showErrorMessageAndBack(messageToShow);
            return false;
        }

        ResultMessageDTO resultMessageDTO = accountDAO.signUp(username, email, password);
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

    public static void openRegistrazioneActivity(NaTourActivity fromActivity){
        Intent intent = new Intent(fromActivity, RegistrazioneActivity.class);
        fromActivity.startActivity(intent);
    }

    public void back() {
        getActivity().onBackPressed();
    }
}
