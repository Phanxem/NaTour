package com.unina.natour.controllers;

import com.unina.natour.R;
import com.unina.natour.dto.response.ResultMessageDTO;
import com.unina.natour.views.activities.NaTourActivity;
import com.unina.natour.views.dialogs.MessageDialog;

public class MessageController {

    public static final long ERROR_CODE_UNKNOWN = 100;
    public static final String ERROR_MESSAGE_UNKNOWN = "Errore Sconosciuto";

    public static final long SUCCESS_CODE = 200;
    public static final String SUCCESS_MESSAGE = "Operazione effettuata con successo";

    public static final long ERROR_CODE_AMPLIFY = 300;
    public static final long ERROR_CODE_FAILURE = 400;
    public static final String ERROR_MESSAGE_FAILURE = "Operazione non completata";

    public static final long ERROR_CODE_EMPTY_FIELD = 500;
    public static final long ERROR_CODE_EMPTY_FIELD_ACTIVATION = 501;



    public static final ResultMessageDTO MESSAGE_UNKNOWN_ERROR = new ResultMessageDTO(ERROR_CODE_UNKNOWN, ERROR_MESSAGE_UNKNOWN);
    public static final ResultMessageDTO MESSAGE_SUCCESS = new ResultMessageDTO(SUCCESS_CODE, SUCCESS_MESSAGE);

    public final String TAG = this.getClass().getSimpleName();

    private NaTourActivity activity;
    private MessageDialog messageDialog;

    public MessageController(NaTourActivity activity){
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

    public void showErrorMessage(long errorCode) {
        String messageToShow = ERROR_MESSAGE_UNKNOWN;

        if(errorCode == ERROR_CODE_EMPTY_FIELD_ACTIVATION){
            messageToShow = ""; //todo
        }

        messageDialog.setMessage(messageToShow);
        messageDialog.showOverUi();
    }

    private String findMessageFromAmplifyMessage(String message){
        String amplifyMessage = null;

        amplifyMessage = activity.getString(R.string.AmplifyException_UserAlreadyExists);
        if(message.contains(amplifyMessage)){
            return activity.getString(R.string.UserAlreadyExists);
        }

        amplifyMessage = activity.getString(R.string.AmplifyException_EmailAlreadyExists);
        if(message.contains(amplifyMessage)){
            return activity.getString(R.string.EmailAlreadyExists);
        }

        amplifyMessage = activity.getString(R.string.AmplifyException_PasswordNotConformPolicy);
        if(message.contains(amplifyMessage)){
            return activity.getString(R.string.PasswordNotConformPolicy);
        }

        amplifyMessage = activity.getString(R.string.AmplifyException_InvalidVerificationCode);
        if(message.contains(amplifyMessage)){
            return activity.getString(R.string.InvalidVerificationCode);
        }

        amplifyMessage = activity.getString(R.string.AmplifyException_InvalidEmailFormat);
        if(message.contains(amplifyMessage)){
            return activity.getString(R.string.InvalidEmailFormat);
        }

        amplifyMessage = activity.getString(R.string.AmplifyException_UserNotExist);
        if(message.contains(amplifyMessage)){
            return activity.getString(R.string.UserNotExist);
        }

        amplifyMessage = activity.getString(R.string.AmplifyException_IncorrectUserPassword);
        if(message.contains(amplifyMessage)){
            return activity.getString(R.string.IncorrectUserPassword);
        }

        amplifyMessage = activity.getString(R.string.AmplifyException_UserNotConfirmed);
        if(message.contains(amplifyMessage)){
            return activity.getString(R.string.UserNotConfirmed);
        }


        amplifyMessage = activity.getString(R.string.AmplifyException_AccountNotFound);
        if(message.contains(amplifyMessage)){
            return activity.getString(R.string.AccountNotFound);
        }

        amplifyMessage = activity.getString(R.string.AmplifyException_ErrorPasswordRecovery_UserNotConfirmed);
        if(message.contains(amplifyMessage)){
            return activity.getString(R.string.ErrorPasswordRecovery_UserNotConfirmed);
        }

        return activity.getString(R.string.UnknownException);
    }


    public static ResultMessageDTO getSuccessMessage(){
        ResultMessageDTO resultMessageDTO = new ResultMessageDTO(SUCCESS_CODE, SUCCESS_MESSAGE);

        return resultMessageDTO;
    }


    public static ResultMessageDTO getUnknownErrorMessage(){
        ResultMessageDTO resultMessageDTO = new ResultMessageDTO(ERROR_CODE_UNKNOWN, ERROR_MESSAGE_UNKNOWN);

        return resultMessageDTO;
    }


    public static ResultMessageDTO getFailureMessage(){
        ResultMessageDTO resultMessageDTO = new ResultMessageDTO(ERROR_CODE_FAILURE, ERROR_MESSAGE_FAILURE);

        return resultMessageDTO;
    }

    public static ResultMessageDTO getNotFoundMessage(){
        return null;
    }
}
