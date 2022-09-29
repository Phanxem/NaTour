package com.unina.natour.models.dao.implementation;

import android.util.Log;

import com.amplifyframework.auth.AuthUserAttribute;
import com.amplifyframework.auth.AuthUserAttributeKey;
import com.amplifyframework.auth.cognito.AWSCognitoAuthSession;
import com.amplifyframework.auth.options.AuthSignUpOptions;
import com.amplifyframework.core.Amplify;
import com.unina.natour.controllers.MessageController;
import com.unina.natour.controllers.exceptionHandler.exceptions.subAppException.NotCompletedFetchAuthSessionException;
import com.unina.natour.controllers.utils.StringsUtils;
import com.unina.natour.dto.response.CognitoAuthSessionDTO;
import com.unina.natour.dto.response.EmailResponseDTO;
import com.unina.natour.dto.response.MessageResponseDTO;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class AmplifyDAO extends ServerDAO {

    public MessageResponseDTO signUp(String username, String email, String password){

        CompletableFuture<MessageResponseDTO> completableFuture = new CompletableFuture<MessageResponseDTO>();

        AuthSignUpOptions options = AuthSignUpOptions
                .builder()
                .userAttribute(AuthUserAttributeKey.email(), email)
                .build();

        Amplify.Auth.signUp(
                username,
                password,
                options,
                result -> {
                    completableFuture.complete(MessageController.getSuccessMessage());
                },
                error -> {
                    String message = error.getCause().getMessage();
                    MessageResponseDTO messageResponseDTO = new MessageResponseDTO(MessageController.ERROR_CODE_AMPLIFY, message);
                    completableFuture.complete(messageResponseDTO);
                }
        );

        MessageResponseDTO result = null;
        try {
            result = completableFuture.get();
        }
        catch (ExecutionException | InterruptedException e) {
            result = MessageController.getFailureMessage();
        }

        return result;
    }

    public MessageResponseDTO activateAccount(String username, String confirmationCode){

        CompletableFuture<MessageResponseDTO> completableFuture = new CompletableFuture<MessageResponseDTO>();

        Amplify.Auth.confirmSignUp(
                username,
                confirmationCode,
                confirmSignUpResult -> {
                    completableFuture.complete(MessageController.getSuccessMessage());
                },
                error -> {
                    String message = error.getCause().getMessage();
                    MessageResponseDTO messageResponseDTO = new MessageResponseDTO(MessageController.ERROR_CODE_AMPLIFY, message);
                    completableFuture.complete(messageResponseDTO);
                }
        );

        MessageResponseDTO result = null;
        try {
            result = completableFuture.get();
        }
        catch (ExecutionException | InterruptedException e) {
            result = MessageController.getFailureMessage();
        }

        return result;
    }

    public MessageResponseDTO resendActivationCode(String username){
        CompletableFuture<MessageResponseDTO> completableFuture = new CompletableFuture<MessageResponseDTO>();

        Amplify.Auth.resendSignUpCode(
                username,
                result -> {
                    completableFuture.complete(MessageController.getSuccessMessage());
                },
                error -> {
                    String message = error.getCause().getMessage();
                    MessageResponseDTO messageResponseDTO = new MessageResponseDTO(MessageController.ERROR_CODE_AMPLIFY, message);
                    completableFuture.complete(messageResponseDTO);
                }
        );

        MessageResponseDTO result = null;
        try {
            result = completableFuture.get();
        }
        catch (ExecutionException | InterruptedException e) {
            result = MessageController.getFailureMessage();
        }

        return result;
    }

    public MessageResponseDTO signIn(String username, String password){
        CompletableFuture<MessageResponseDTO> completableFuture = new CompletableFuture<MessageResponseDTO>();

        Amplify.Auth.signIn(
                username,
                password,
                result -> {
                    completableFuture.complete(MessageController.getSuccessMessage());
                },
                error -> {
                    String message = error.getCause().getMessage();
                    MessageResponseDTO messageResponseDTO = new MessageResponseDTO(MessageController.ERROR_CODE_AMPLIFY, message);
                    completableFuture.complete(messageResponseDTO);
                }
        );

        MessageResponseDTO result = null;
        try {
            result = completableFuture.get();
        }
        catch (ExecutionException | InterruptedException e) {
            result = MessageController.getFailureMessage();
        }

        return result;
    }

    public MessageResponseDTO signOut(){
        CompletableFuture<MessageResponseDTO> completableFuture = new CompletableFuture<MessageResponseDTO>();

        Amplify.Auth.signOut(
                () -> {
                    completableFuture.complete(MessageController.getSuccessMessage());
                },
                error -> {
                    String message = error.getCause().getMessage();
                    MessageResponseDTO messageResponseDTO = new MessageResponseDTO(MessageController.ERROR_CODE_AMPLIFY, message);
                    completableFuture.complete(messageResponseDTO);
                }
        );

        MessageResponseDTO result = null;
        try {
            result = completableFuture.get();
        }
        catch (ExecutionException | InterruptedException e) {
            result = MessageController.getFailureMessage();
        }

        return result;
    }

    public MessageResponseDTO updatePassword(String oldPassword, String newPassword){
        CompletableFuture<MessageResponseDTO> completableFuture = new CompletableFuture<MessageResponseDTO>();

        Amplify.Auth.updatePassword(
                oldPassword,
                newPassword,
                () -> {
                    completableFuture.complete(MessageController.getSuccessMessage());
                },
                error -> {
                    String message = error.getCause().getMessage();
                    MessageResponseDTO messageResponseDTO = new MessageResponseDTO(MessageController.ERROR_CODE_AMPLIFY, message);
                    completableFuture.complete(messageResponseDTO);
                }
        );

        MessageResponseDTO result = null;
        try {
            result = completableFuture.get();
        }
        catch (ExecutionException | InterruptedException e) {
            result = MessageController.getFailureMessage();
        }

        return result;
    }

    public EmailResponseDTO getEmail(){
        CompletableFuture<EmailResponseDTO> completableFuture = new CompletableFuture<EmailResponseDTO>();

        Amplify.Auth.fetchUserAttributes(
                attributes -> {
                    for (AuthUserAttribute attribute : attributes) {
                        if (attribute.getKey().getKeyString().equals("email")) {
                            EmailResponseDTO emailResponseDTO = new EmailResponseDTO(MessageController.getSuccessMessage(),attribute.getValue());
                            completableFuture.complete(emailResponseDTO);
                        }
                    }
                },
                error -> {
                    String message = error.getCause().getMessage();
                    Log.e("TAG","error",error);
                    MessageResponseDTO messageResponseDTO = new MessageResponseDTO(MessageController.ERROR_CODE_AMPLIFY, message);
                    EmailResponseDTO emailResponseDTO = new EmailResponseDTO(messageResponseDTO, null);
                    completableFuture.complete(emailResponseDTO);
                }
        );

        EmailResponseDTO result = null;
        try {
            result = completableFuture.get();
        }
        catch (ExecutionException | InterruptedException e) {
            MessageResponseDTO messageResponseDTO = MessageController.getFailureMessage();
            result = new EmailResponseDTO(messageResponseDTO,null);
        }

        return result;
    }

    public MessageResponseDTO startPasswordRecovery(String username){
        CompletableFuture<MessageResponseDTO> completableFuture = new CompletableFuture<MessageResponseDTO>();

        Amplify.Auth.resetPassword(
                username,
                result -> {
                    completableFuture.complete(MessageController.getSuccessMessage());
                },
                error -> {
                    String message = error.getCause().getMessage();
                    MessageResponseDTO messageResponseDTO = new MessageResponseDTO(MessageController.ERROR_CODE_AMPLIFY, message);
                    completableFuture.complete(messageResponseDTO);
                }
        );

        MessageResponseDTO result = null;
        try {
            result = completableFuture.get();
        }
        catch (ExecutionException | InterruptedException e) {
            result = MessageController.getFailureMessage();
        }

        return result;
    }

    public MessageResponseDTO completePasswordRecovery(String confirmationCode, String password){
        CompletableFuture<MessageResponseDTO> completableFuture = new CompletableFuture<MessageResponseDTO>();

        Amplify.Auth.confirmResetPassword(
                password,
                confirmationCode,
                () -> {
                    completableFuture.complete(MessageController.getSuccessMessage());
                },
                error ->{
                    String message = error.getCause().getMessage();
                    MessageResponseDTO messageResponseDTO = new MessageResponseDTO(MessageController.ERROR_CODE_AMPLIFY, message);
                    completableFuture.complete(messageResponseDTO);
                }
        );

        MessageResponseDTO result = null;
        try {
            result = completableFuture.get();
        }
        catch (ExecutionException | InterruptedException e) {
            result = MessageController.getFailureMessage();
        }
        return result;
    }

    public CognitoAuthSessionDTO fetchAuthSessione(){

        CompletableFuture<CognitoAuthSessionDTO> completableFuture = new CompletableFuture<CognitoAuthSessionDTO>();

        Amplify.Auth.fetchAuthSession(
                result -> {
                    AWSCognitoAuthSession cognitoAuthSession = (AWSCognitoAuthSession) result;

                    CognitoAuthSessionDTO cognitoAuthSessionDTO = new CognitoAuthSessionDTO();
                    cognitoAuthSessionDTO.setAuthSessione(cognitoAuthSession);
                    cognitoAuthSessionDTO.setResultMessage(MessageController.getSuccessMessage());
                    completableFuture.complete(cognitoAuthSessionDTO);
                },
                error -> {
                    String message = error.getCause().getMessage();
                    CognitoAuthSessionDTO cognitoAuthSessionDTO = new CognitoAuthSessionDTO();
                    MessageResponseDTO messageResponseDTO = new MessageResponseDTO(MessageController.ERROR_CODE_AMPLIFY, message);
                    cognitoAuthSessionDTO.setResultMessage(messageResponseDTO);
                    completableFuture.complete(cognitoAuthSessionDTO);
                }
        );

        CognitoAuthSessionDTO result = null;

        try {
            result = completableFuture.get();
        }
        catch (ExecutionException | InterruptedException e) {
            result = new CognitoAuthSessionDTO();
            MessageResponseDTO messageResponseDTO = MessageController.getFailureMessage();
            result.setResultMessage(messageResponseDTO);
        }

        return result;
    }



}
