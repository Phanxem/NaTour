package com.unina.natour.controllers.exceptionHandler;

import android.content.Context;
import android.util.Log;

import androidx.fragment.app.FragmentActivity;

import com.amplifyframework.AmplifyException;
import com.amplifyframework.auth.AuthException;
import com.unina.natour.R;
import com.unina.natour.amplify.ApplicationConfig;
import com.unina.natour.controllers.exceptionHandler.exceptions.AppException;
import com.unina.natour.controllers.exceptionHandler.exceptions.ServerException;
import com.unina.natour.views.activities.NaTourActivity;
import com.unina.natour.views.dialogs.MessageDialog;

public class ExceptionHandler {

    //TODO TESTING new context

    //Eccezioni di Amplify
    //l'errore viene identificato secondo il messaggio
    public static void handleMessageError(MessageDialog messageDialog, AmplifyException exception){
        NaTourActivity activity = messageDialog.getNaTourActivity();

        String amplifyMessage = exception.getCause().getMessage();
        String message = findMessageFromAmplifyMessage(activity, amplifyMessage);
        messageDialog.setMessage(message);
        //messageDialog.show(messageDialog.requireActivity().getSupportFragmentManager(), "");
        messageDialog.showOverUi();
    }

    //Eccezioni del server
    public static void handleMessageError(MessageDialog messageDialog, ServerException exception){
        NaTourActivity activity = messageDialog.getNaTourActivity();
        String errorMessage = exception.getMessage();

        long serverCode = exception.getCode();

        String message = findMessageFromServerCode(activity, serverCode);
        messageDialog.setMessage(message);
        //messageDialog.show(messageDialog.requireActivity().getSupportFragmentManager(), "");
        messageDialog.showOverUi();
    }


    //Eccezioni del app
    public static void handleMessageError(MessageDialog messageDialog, AppException exception){
        NaTourActivity activity = messageDialog.getNaTourActivity();

        String message = findMessageFromExceptionType(activity, exception);
        messageDialog.setMessage(message);
        //messageDialog.show(messageDialog.requireActivity().getSupportFragmentManager(), "");
        messageDialog.showOverUi();

    }




    private static String findMessageFromAmplifyMessage(Context context, String message){
        //Context context = ApplicationConfig.getAppContext();
        String amplifyMessage = null;

        amplifyMessage = context.getString(R.string.AmplifyException_UserAlreadyExists);
        if(message.contains(amplifyMessage)){
            return context.getString(R.string.UserAlreadyExists);
        }

        amplifyMessage = context.getString(R.string.AmplifyException_EmailAlreadyExists);
        if(message.contains(amplifyMessage)){
            return context.getString(R.string.EmailAlreadyExists);
        }

        amplifyMessage = context.getString(R.string.AmplifyException_PasswordNotConformPolicy);
        if(message.contains(amplifyMessage)){
            return context.getString(R.string.PasswordNotConformPolicy);
        }

        amplifyMessage = context.getString(R.string.AmplifyException_InvalidVerificationCode);
        if(message.contains(amplifyMessage)){
            return context.getString(R.string.InvalidVerificationCode);
        }

        amplifyMessage = context.getString(R.string.AmplifyException_InvalidEmailFormat);
        if(message.contains(amplifyMessage)){
            return context.getString(R.string.InvalidEmailFormat);
        }


        amplifyMessage = context.getString(R.string.AmplifyException_UserNotExist);
        if(message.contains(amplifyMessage)){
            return context.getString(R.string.UserNotExist);
        }

        amplifyMessage = context.getString(R.string.AmplifyException_IncorrectUserPassword);
        if(message.contains(amplifyMessage)){
            return context.getString(R.string.IncorrectUserPassword);
        }

        amplifyMessage = context.getString(R.string.AmplifyException_UserNotConfirmed);
        if(message.contains(amplifyMessage)){
            return context.getString(R.string.UserNotConfirmed);
        }


        amplifyMessage = context.getString(R.string.AmplifyException_AccountNotFound);
        if(message.contains(amplifyMessage)){
            return context.getString(R.string.AccountNotFound);
        }

        amplifyMessage = context.getString(R.string.AmplifyException_ErrorPasswordRecovery_UserNotConfirmed);
        if(message.contains(amplifyMessage)){
            return context.getString(R.string.ErrorPasswordRecovery_UserNotConfirmed);
        }

        return context.getString(R.string.UnknownException);
    }

    private static String findMessageFromServerCode(Context context, long code){
        //Context context = ApplicationConfig.getAppContext();

        return context.getString(R.string.UnknownException);
    }

    private static String findMessageFromExceptionType(Context context, AppException exception){
        //Context context = ApplicationConfig.getAppContext();

        return context.getString(R.string.UnknownException);
    }




/*
    //Eccezioni interne dell'app
    public static void handleMessageError(MessageDialog messageDialog, IOException error){
        String errorMessage = error.getCause().getMessage();
        messageDialog.setMessage(ERROR_MESSAGE_UNKNOWN);

        Log.e("ERROR_MESSAGE:", errorMessage );

        messageDialog.showOverUi();
    }



    //Eccezioni chiamate API async (eccezioni derivanti dalla classe CompletableFuture)
    public static void handleMessageError(MessageDialog messageDialog, InterruptedException error){
        String errorMessage = "E' stato riscontrato un errore, non è possibile completare l'operazione";
        messageDialog.setMessage(errorMessage);

        messageDialog.showOverUi();
    }

    public static void handleMessageError(MessageDialog messageDialog, ExecutionException error){
        String errorMessage = "E' stato riscontrato un errore, non è possibile completare l'operazione";
        messageDialog.setMessage(errorMessage);

        messageDialog.showOverUi();
    }
*/












/*
    //Visualizza messaggio
    public static void handleMessageError(MessageDialog messageDialog, MessageDTO messageDTO){
        String errorMessage = messageDTO.getMessage();
        messageDialog.setMessage(errorMessage);

        Log.e("ERROR_MESSAGE:", errorMessage );

        messageDialog.showOverUi();
    }
*/








    public static boolean areAllFieldsFull(String firstString, String... strings){

        if(firstString == null || firstString.isEmpty()) return false;

        for(String string : strings){
            if(string == null || string.isEmpty()) return false;
        }
        return true;
    }


    public static boolean doPasswordMatch(String password1, String passowrd2){

        if(!password1.equals(passowrd2)) return false;

        return true;
    }

    /*
    public static boolean isAccountNotActiveError(AuthException error){
        if(error.getCause().getMessage().contains(AMPLIFY_ERROR_MESSAGE_USER_NOT_CONFIRMED)) return true;
        return false;
    }
*/


}
