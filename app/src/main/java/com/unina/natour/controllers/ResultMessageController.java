package com.unina.natour.controllers;

import android.app.Activity;

import com.unina.natour.R;
import com.unina.natour.dto.response.ResultMessageDTO;
import com.unina.natour.views.activities.NaTourActivity;
import com.unina.natour.views.dialogs.MessageDialog;

public class ResultMessageController {

    public static final ResultMessageDTO SUCCESS_MESSAGE = new ResultMessageDTO(200, "");
    public static final ResultMessageDTO ERROR_MESSAGE_FAILURE_CLIENT = new ResultMessageDTO(400, "");
    public static final ResultMessageDTO ERROR_MESSAGE_UNKNOWN2 = new ResultMessageDTO(100, "");

    public static final ResultMessageDTO ERROR_MESSAGE_INVALID_REQUEST = new ResultMessageDTO(400,"");
    public static final ResultMessageDTO ERROR_MESSAGE_FAILURE = new ResultMessageDTO(500,"");
    public static final ResultMessageDTO ERROR_MESSAGE_NOT_FOUND = new ResultMessageDTO(404,"");
    public static final ResultMessageDTO ERROR_MESSAGE_UNIQUE_VIOLATION = new ResultMessageDTO(402,"");



    public static final long ERROR_CODE_AMPLIFY = 300;
    public static final long ERROR_CODE_NOT_FOUND = 404;
    public static final long ERROR_CODE_SERVER = 500;


    public final String TAG = this.getClass().getSimpleName();

    private NaTourActivity activity;
    private MessageDialog messageDialog;

    public ResultMessageController(NaTourActivity activity, MessageDialog messageDialog){
        this.activity = activity;
        this.messageDialog = messageDialog;
    }

    public ResultMessageController(NaTourActivity activity){
        this.activity = activity;
        this.messageDialog = new MessageDialog();
        this.messageDialog.setNaTourActivity(activity);
    }

    public NaTourActivity getActivity() {
        return activity;
    }

    public void setActivity(NaTourActivity activity) {
        this.activity = activity;
    }

    public MessageDialog getMessageDialog() {
        return messageDialog;
    }

    public void setMessageDialog(MessageDialog messageDialog) {
        this.messageDialog = messageDialog;

    }


    public void setGoBackOnClose(boolean value){
        this.messageDialog.setGoBackOnClose(value);
    }

/*
    public void showErrorMessage(ResultMessageDTO resultMessageDTO) {
        long errorCode = resultMessageDTO.getCode();
        String errorMessage = resultMessageDTO.getMessage();
        String messageToShow = ERROR_MESSAGE_UNKNOWN;

        if(errorCode == ERROR_CODE_AMPLIFY){
            messageToShow = findMessageFromAmplifyMessage(errorMessage);
        }

        messageDialog.setMessage(messageToShow);
        messageDialog.showOverUi();
    }

*/

    public void showErrorMessage(String messageToShow) {
        messageDialog.setMessage(messageToShow);
        messageDialog.showOverUi();
    }


    public static String findMessageFromAmplifyMessage(Activity activity, String message){
        String amplifyMessage = null;

        amplifyMessage = activity.getString(R.string.AmplifyException_UserAlreadyExists);
        if(message.contains(amplifyMessage)){
            return activity.getString(R.string.Message_UserAlreadyExistsError);
        }

        amplifyMessage = activity.getString(R.string.AmplifyException_EmailAlreadyExists);
        if(message.contains(amplifyMessage)){
            return activity.getString(R.string.Message_EmailAlreadyExistsError);
        }

        amplifyMessage = activity.getString(R.string.AmplifyException_PasswordNotConformPolicy);
        if(message.contains(amplifyMessage)){
            return activity.getString(R.string.Message_PasswordNotConformPolicyError);
        }

        amplifyMessage = activity.getString(R.string.AmplifyException_InvalidVerificationCode);
        if(message.contains(amplifyMessage)){
            return activity.getString(R.string.Message_InvalidVerificationCodeError);
        }

        amplifyMessage = activity.getString(R.string.AmplifyException_InvalidEmailFormat);
        if(message.contains(amplifyMessage)){
            return activity.getString(R.string.Message_InvalidEmailFormatError);
        }

        amplifyMessage = activity.getString(R.string.AmplifyException_UserNotExist);
        if(message.contains(amplifyMessage)){
            return activity.getString(R.string.Message_UserNotExistError);
        }

        amplifyMessage = activity.getString(R.string.AmplifyException_IncorrectUserPassword);
        if(message.contains(amplifyMessage)){
            return activity.getString(R.string.Message_IncorrectUserPasswordError);
        }

        amplifyMessage = activity.getString(R.string.AmplifyException_UserNotConfirmed);
        if(message.contains(amplifyMessage)){
            return activity.getString(R.string.Message_UserNotConfirmedError);
        }


        amplifyMessage = activity.getString(R.string.AmplifyException_AccountNotFound);
        if(message.contains(amplifyMessage)){
            return activity.getString(R.string.Message_AccountNotFoundError);
        }

        amplifyMessage = activity.getString(R.string.AmplifyException_ErrorPasswordRecovery_UserNotConfirmed);
        if(message.contains(amplifyMessage)){
            return activity.getString(R.string.Message_ErrorPasswordRecoveryUserNotConfirmedError);
        }

        return activity.getString(R.string.Message_UnknownError);
    }







    public static boolean isSuccess(ResultMessageDTO resultMessageDTO){
        if(resultMessageDTO == null) return false;

        long code = resultMessageDTO.getCode();

        return code == SUCCESS_MESSAGE.getCode();
    }

}
