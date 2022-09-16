package com.unina.natour.models.dao.implementation;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.amplifyframework.auth.AuthUserAttribute;
import com.amplifyframework.auth.AuthUserAttributeKey;
import com.amplifyframework.auth.options.AuthSignUpOptions;
import com.amplifyframework.core.Amplify;
import com.unina.natour.controllers.exceptionHandler.ExceptionHandler;
import com.unina.natour.controllers.exceptionHandler.exceptions.subAppException.NotCompletedConfirmResetPasswordException;
import com.unina.natour.controllers.exceptionHandler.exceptions.subAppException.NotCompletedConfirmSignUpException;
import com.unina.natour.controllers.exceptionHandler.exceptions.subAppException.NotCompletedFindAddressException;
import com.unina.natour.controllers.exceptionHandler.exceptions.subAppException.NotCompletedSignInException;
import com.unina.natour.controllers.exceptionHandler.exceptions.subAppException.NotCompletedSignOutException;
import com.unina.natour.controllers.exceptionHandler.exceptions.subAppException.NotCompletedSignUpException;
import com.unina.natour.controllers.exceptionHandler.exceptions.subAppException.NotCompletedStartPasswordRecoveryException;
import com.unina.natour.dto.EmailDTO;
import com.unina.natour.dto.MessageDTO;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class AmplifyAuthDAO {

    public MessageDTO signUp(String username, String email, String password){

        CompletableFuture<MessageDTO> completableFuture = new CompletableFuture<MessageDTO>();

        AuthSignUpOptions options = AuthSignUpOptions
                .builder()
                .userAttribute(AuthUserAttributeKey.email(), email)
                .build();

        Amplify.Auth.signUp(
                username,
                password,
                options,
                result -> {
                    completableFuture.complete(MessageDTO.successMessage());
                },
                error -> {
                    String message = error.getCause().getMessage();
                    MessageDTO messageDTO = new MessageDTO(ExceptionHandler.ERROR_CODE_AMPLIFY, message);
                    completableFuture.complete(messageDTO);
                }
        );

        MessageDTO result = null;
        try {
            result = completableFuture.get();
        }
        catch (ExecutionException | InterruptedException e) {
            result = new MessageDTO(ExceptionHandler.ERROR_CODE_NOT_COMPLETED,ExceptionHandler.ERROR_MESSAGE_NOT_COMPLETED);
        }

        return result;
    }

    public MessageDTO activateAccount(String username, String confirmationCode){

        CompletableFuture<MessageDTO> completableFuture = new CompletableFuture<MessageDTO>();

        Amplify.Auth.confirmSignUp(
                username,
                confirmationCode,
                confirmSignUpResult -> {
                    completableFuture.complete(MessageDTO.successMessage());
                },
                error -> {
                    String message = error.getCause().getMessage();
                    MessageDTO messageDTO = new MessageDTO(ExceptionHandler.ERROR_CODE_AMPLIFY, message);
                    completableFuture.complete(messageDTO);
                }
        );

        MessageDTO result = null;
        try {
            result = completableFuture.get();
        }
        catch (ExecutionException | InterruptedException e) {
            result = new MessageDTO(ExceptionHandler.ERROR_CODE_NOT_COMPLETED,ExceptionHandler.ERROR_MESSAGE_NOT_COMPLETED);
        }

        return result;
    }

    public MessageDTO signIn(String username, String password){
        CompletableFuture<MessageDTO> completableFuture = new CompletableFuture<MessageDTO>();

        Amplify.Auth.signIn(
                username,
                password,
                result -> {
                    completableFuture.complete(MessageDTO.successMessage());
                },
                error -> {
                    String message = error.getCause().getMessage();
                    MessageDTO messageDTO = new MessageDTO(ExceptionHandler.ERROR_CODE_AMPLIFY, message);
                    completableFuture.complete(messageDTO);
                }
        );

        MessageDTO result = null;
        try {
            result = completableFuture.get();
        }
        catch (ExecutionException | InterruptedException e) {
            result = new MessageDTO(ExceptionHandler.ERROR_CODE_NOT_COMPLETED,ExceptionHandler.ERROR_MESSAGE_NOT_COMPLETED);
        }

        return result;
    }

    public MessageDTO signOut(){
        CompletableFuture<MessageDTO> completableFuture = new CompletableFuture<MessageDTO>();

        Amplify.Auth.signOut(
                () -> {
                    completableFuture.complete(MessageDTO.successMessage());
                },
                error -> {
                    String message = error.getCause().getMessage();
                    MessageDTO messageDTO = new MessageDTO(ExceptionHandler.ERROR_CODE_AMPLIFY, message);
                    completableFuture.complete(messageDTO);
                }
        );

        MessageDTO result = null;
        try {
            result = completableFuture.get();
        }
        catch (ExecutionException | InterruptedException e) {
            result = new MessageDTO(ExceptionHandler.ERROR_CODE_NOT_COMPLETED,ExceptionHandler.ERROR_MESSAGE_NOT_COMPLETED);
        }

        return result;
    }

    public MessageDTO updatePassword(String oldPassword, String newPassword){
        CompletableFuture<MessageDTO> completableFuture = new CompletableFuture<MessageDTO>();

        Amplify.Auth.updatePassword(
                oldPassword,
                newPassword,
                () -> {
                    completableFuture.complete(MessageDTO.successMessage());
                },
                error -> {
                    String message = error.getCause().getMessage();
                    MessageDTO messageDTO = new MessageDTO(ExceptionHandler.ERROR_CODE_AMPLIFY, message);
                    completableFuture.complete(messageDTO);
                }
        );

        MessageDTO result = null;
        try {
            result = completableFuture.get();
        }
        catch (ExecutionException | InterruptedException e) {
            result = new MessageDTO(ExceptionHandler.ERROR_CODE_NOT_COMPLETED,ExceptionHandler.ERROR_MESSAGE_NOT_COMPLETED);
        }

        return result;
    }

    public EmailDTO getEmail(){
        CompletableFuture<EmailDTO> completableFuture = new CompletableFuture<EmailDTO>();

        Amplify.Auth.fetchUserAttributes(
                attributes -> {
                    for (AuthUserAttribute attribute : attributes) {
                        if (attribute.getKey().getKeyString().equals("email")) {
                            EmailDTO emailDTO = new EmailDTO(MessageDTO.successMessage(),attribute.getValue());
                            completableFuture.complete(emailDTO);
                        }
                    }
                },
                error -> {
                    String message = error.getCause().getMessage();
                    MessageDTO messageDTO = new MessageDTO(ExceptionHandler.ERROR_CODE_AMPLIFY, message);
                    EmailDTO emailDTO = new EmailDTO(messageDTO, null);
                    completableFuture.complete(emailDTO);
                }
        );

        EmailDTO result = null;
        try {
            result = completableFuture.get();
        }
        catch (ExecutionException | InterruptedException e) {
            MessageDTO messageDTO = new MessageDTO(ExceptionHandler.ERROR_CODE_NOT_COMPLETED,ExceptionHandler.ERROR_MESSAGE_NOT_COMPLETED);
            result = new EmailDTO(messageDTO,null);
        }

        return result;
    }

    public MessageDTO startPasswordRecovery(String username){
        CompletableFuture<MessageDTO> completableFuture = new CompletableFuture<MessageDTO>();

        Amplify.Auth.resetPassword(
                username,
                result -> {
                    completableFuture.complete(MessageDTO.successMessage());
                },
                error -> {
                    String message = error.getCause().getMessage();
                    MessageDTO messageDTO = new MessageDTO(ExceptionHandler.ERROR_CODE_AMPLIFY, message);
                    completableFuture.complete(messageDTO);
                }
        );


        MessageDTO result = null;
        try {
            result = completableFuture.get();
        }
        catch (ExecutionException | InterruptedException e) {
            result = new MessageDTO(ExceptionHandler.ERROR_CODE_NOT_COMPLETED,ExceptionHandler.ERROR_MESSAGE_NOT_COMPLETED);
        }

        return result;
    }

    public MessageDTO completePasswordRecovery(String confirmationCode, String password){
        CompletableFuture<MessageDTO> completableFuture = new CompletableFuture<MessageDTO>();

        Amplify.Auth.confirmResetPassword(
                password,
                confirmationCode,
                () -> {
                    completableFuture.complete(MessageDTO.successMessage());
                },
                error ->{
                    String message = error.getCause().getMessage();
                    MessageDTO messageDTO = new MessageDTO(ExceptionHandler.ERROR_CODE_AMPLIFY, message);
                    completableFuture.complete(messageDTO);
                }
        );

        MessageDTO result = null;
        try {
            result = completableFuture.get();
        }
        catch (ExecutionException | InterruptedException e) {
            result = new MessageDTO(ExceptionHandler.ERROR_CODE_NOT_COMPLETED,ExceptionHandler.ERROR_MESSAGE_NOT_COMPLETED);
        }
        return result;
    }
}
