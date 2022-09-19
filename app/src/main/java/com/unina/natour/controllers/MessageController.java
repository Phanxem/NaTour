package com.unina.natour.controllers;

import com.unina.natour.R;
import com.unina.natour.dto.response.MessageResponseDTO;
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



    public static final MessageResponseDTO MESSAGE_UNKNOWN_ERROR = new MessageResponseDTO(ERROR_CODE_UNKNOWN, ERROR_MESSAGE_UNKNOWN);
    public static final MessageResponseDTO MESSAGE_SUCCESS = new MessageResponseDTO(SUCCESS_CODE, SUCCESS_MESSAGE);

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


    public void showErrorMessage(MessageResponseDTO messageResponseDTO) {
        long errorCode = messageResponseDTO.getCode();
        String errorMessage = messageResponseDTO.getMessage();
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


    public static MessageResponseDTO getSuccessMessage(){
        MessageResponseDTO messageResponseDTO = new MessageResponseDTO(SUCCESS_CODE, SUCCESS_MESSAGE);

        return messageResponseDTO;
    }


    public static MessageResponseDTO getUnknownErrorMessage(){
        MessageResponseDTO messageResponseDTO = new MessageResponseDTO(ERROR_CODE_UNKNOWN, ERROR_MESSAGE_UNKNOWN);

        return messageResponseDTO;
    }


    public static MessageResponseDTO getFailureMessage(){
        MessageResponseDTO messageResponseDTO = new MessageResponseDTO(ERROR_CODE_FAILURE, ERROR_MESSAGE_FAILURE);

        return messageResponseDTO;
    }

    public static MessageResponseDTO getNotFoundMessage(){
        return null;
    }
}
