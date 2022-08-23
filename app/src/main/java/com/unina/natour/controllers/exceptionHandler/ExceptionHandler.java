package com.unina.natour.controllers.exceptionHandler;

import android.util.Log;

import com.amplifyframework.auth.AuthException;
import com.unina.natour.controllers.exceptionHandler.exceptions.ServerException;
import com.unina.natour.views.dialogs.MessageDialog;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class ExceptionHandler {

    private static final String ERROR_MESSAGE_UNKNOWN = "E' stato riscontrato un problema";
    private static final String ERROR_MESSAGE_EMPTY_FIELD = "Uno o più campi non sono stati compilati";
    private static final String ERROR_MESSAGE_UNMATCH_PASSWORD = "Le password inserite non corrispondono";

    private static final String AMPLIFY_ERROR_MESSAGE_USER_NOT_CONFIRMED = "User is not confirmed";

    private static final Map<String, String> exceptionMessages = new HashMap<>();
    static {
        //SIGN-UP EXCEPTION
        exceptionMessages.put("User already exists", "L'username inserito è già utilizzato");
        exceptionMessages.put("PreSignUp failed with error EmailExistsException", "L'email inserita è già utilizzata");
        exceptionMessages.put("Password did not conform with policy", "La password inserita non è troppo corta.\n Una password deve essere lunga almeno 8 caratteri");
        exceptionMessages.put("Invalid verification code provided", "Il codice inserito non è valido");
        exceptionMessages.put("Invalid email address format", "L'e-mail inserita non è valida");


        //SIGN-IN EXCEPTION
        exceptionMessages.put("User does not exist", "Username o Email errata");
        exceptionMessages.put("Incorrect username or password", "Password errata");
        exceptionMessages.put("User is not confirmed", "Account non attivo");


        //RECOVERY PASSWORD EXCEPTION
        exceptionMessages.put("Username/client id combination not found", "l'username o l'email inserita non corrisponde a nessun account");
        exceptionMessages.put("Cannot reset password for the user as there is no registered/verified email or phone_number", "l'account dell'utente non risulta attivato, non è quindi possibile effettuare il recupero della password");

    }

    //Eccezioni di Cognito
    //l'errore viene identificato secondo il messaggio
    public static void handleMessageError(MessageDialog messageDialog, AuthException error){
        String errorMessage = error.getCause().getMessage();
        messageDialog.setMessage(ERROR_MESSAGE_UNKNOWN);

        //TODO funzione di ricerca dell'errore (basata sui messaggi restituiti dall'eccezione)

        for(Map.Entry<String, String> entry : exceptionMessages.entrySet()){
            if(errorMessage.contains(entry.getKey())){
                messageDialog.setMessage(entry.getValue());
                break;
            }
        }

        messageDialog.showOverUi();
    }

    //Eccezioni del server
    public static void handleMessageError(MessageDialog messageDialog, ServerException error){
        String errorMessage = error.getMessage();
        messageDialog.setMessage(errorMessage);

        //TODO funzione di ricerca dell'errore (basata sui codici restituiti dall'eccezione)

        messageDialog.showOverUi();
    }


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













/*
    //Visualizza messaggio
    public static void handleMessageError(MessageDialog messageDialog, MessageDTO messageDTO){
        String errorMessage = messageDTO.getMessage();
        messageDialog.setMessage(errorMessage);

        Log.e("ERROR_MESSAGE:", errorMessage );

        messageDialog.showOverUi();
    }
*/








    public static boolean areAllFieldsFull(MessageDialog messageDialog, String... strings){

        for(String string : strings){
            if(string == null || string.isEmpty()){
                messageDialog.setMessage(ERROR_MESSAGE_EMPTY_FIELD);
                messageDialog.showOverUi();
                return false;
            }
        }
        return true;
    }

    public static boolean doPasswordMatch(MessageDialog messageDialog, String password1, String passowrd2){

        if(!password1.equals(passowrd2)){
            messageDialog.setMessage(ERROR_MESSAGE_UNMATCH_PASSWORD);
            messageDialog.showOverUi();
            return false;
        }
        return true;
    }

    public static boolean isAccountNotActiveError(AuthException error){
        if(error.getCause().getMessage().contains(AMPLIFY_ERROR_MESSAGE_USER_NOT_CONFIRMED)) return true;
        return false;
    }



}
