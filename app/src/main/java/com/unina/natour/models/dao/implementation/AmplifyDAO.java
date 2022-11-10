package com.unina.natour.models.dao.implementation;

import android.util.Log;

import com.amplifyframework.auth.AuthUserAttribute;
import com.amplifyframework.auth.AuthUserAttributeKey;
import com.amplifyframework.auth.cognito.AWSCognitoAuthSession;
import com.amplifyframework.auth.options.AuthSignUpOptions;
import com.amplifyframework.core.Amplify;
import com.unina.natour.controllers.ResultMessageController;
import com.unina.natour.dto.response.GetAuthSessionResponseDTO;
import com.unina.natour.dto.response.GetEmailResponseDTO;
import com.unina.natour.dto.response.ResultMessageDTO;
import com.unina.natour.models.dao.interfaces.AccountDAO;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class AmplifyDAO extends ServerDAO implements AccountDAO {

    public ResultMessageDTO signUp(String username, String email, String password){

        CompletableFuture<ResultMessageDTO> completableFuture = new CompletableFuture<ResultMessageDTO>();

        AuthSignUpOptions options = AuthSignUpOptions
                .builder()
                .userAttribute(AuthUserAttributeKey.email(), email)
                .build();

        Amplify.Auth.signUp(
                username,
                password,
                options,
                result -> {
                    completableFuture.complete(ResultMessageController.SUCCESS_MESSAGE);
                },
                error -> {
                    String message = error.getCause().getMessage();
                    ResultMessageDTO resultMessageDTO = new ResultMessageDTO(ResultMessageController.ERROR_CODE_AMPLIFY, message);
                    completableFuture.complete(resultMessageDTO);
                }
        );

        ResultMessageDTO result = null;
        try {
            result = completableFuture.get();
        }
        catch (ExecutionException | InterruptedException e) {
            result = ResultMessageController.ERROR_MESSAGE_FAILURE_CLIENT;
        }

        return result;
    }

    public ResultMessageDTO activateAccount(String username, String confirmationCode){

        CompletableFuture<ResultMessageDTO> completableFuture = new CompletableFuture<ResultMessageDTO>();

        Amplify.Auth.confirmSignUp(
                username,
                confirmationCode,
                confirmSignUpResult -> {
                    completableFuture.complete(ResultMessageController.SUCCESS_MESSAGE);
                },
                error -> {
                    String message = error.getCause().getMessage();
                    ResultMessageDTO resultMessageDTO = new ResultMessageDTO(ResultMessageController.ERROR_CODE_AMPLIFY, message);
                    completableFuture.complete(resultMessageDTO);
                }
        );

        ResultMessageDTO result = null;
        try {
            result = completableFuture.get();
        }
        catch (ExecutionException | InterruptedException e) {
            result = ResultMessageController.ERROR_MESSAGE_FAILURE_CLIENT;
        }

        return result;
    }

    public ResultMessageDTO resendActivationCode(String username){
        CompletableFuture<ResultMessageDTO> completableFuture = new CompletableFuture<ResultMessageDTO>();

        Amplify.Auth.resendSignUpCode(
                username,
                result -> {
                    completableFuture.complete(ResultMessageController.SUCCESS_MESSAGE);
                },
                error -> {
                    String message = error.getCause().getMessage();
                    ResultMessageDTO resultMessageDTO = new ResultMessageDTO(ResultMessageController.ERROR_CODE_AMPLIFY, message);
                    completableFuture.complete(resultMessageDTO);
                }
        );

        ResultMessageDTO result = null;
        try {
            result = completableFuture.get();
        }
        catch (ExecutionException | InterruptedException e) {
            result = ResultMessageController.ERROR_MESSAGE_FAILURE_CLIENT;
        }

        return result;
    }

    public ResultMessageDTO signIn(String username, String password){
        CompletableFuture<ResultMessageDTO> completableFuture = new CompletableFuture<ResultMessageDTO>();

        Amplify.Auth.signIn(
                username,
                password,
                result -> {
                    completableFuture.complete(ResultMessageController.SUCCESS_MESSAGE);
                },
                error -> {
                    String message = error.getCause().getMessage();
                    ResultMessageDTO resultMessageDTO = new ResultMessageDTO(ResultMessageController.ERROR_CODE_AMPLIFY, message);
                    completableFuture.complete(resultMessageDTO);
                }
        );

        ResultMessageDTO result = null;
        try {
            result = completableFuture.get();
        }
        catch (ExecutionException | InterruptedException e) {
            result = ResultMessageController.ERROR_MESSAGE_FAILURE_CLIENT;
        }

        return result;
    }

    public ResultMessageDTO signOut(){
        CompletableFuture<ResultMessageDTO> completableFuture = new CompletableFuture<ResultMessageDTO>();

        Amplify.Auth.signOut(
                () -> {
                    completableFuture.complete(ResultMessageController.SUCCESS_MESSAGE);
                },
                error -> {
                    String message = error.getCause().getMessage();
                    ResultMessageDTO resultMessageDTO = new ResultMessageDTO(ResultMessageController.ERROR_CODE_AMPLIFY, message);
                    completableFuture.complete(resultMessageDTO);
                }
        );

        ResultMessageDTO result = null;
        try {
            result = completableFuture.get();
        }
        catch (ExecutionException | InterruptedException e) {
            result = ResultMessageController.ERROR_MESSAGE_FAILURE_CLIENT;
        }

        return result;
    }

    public ResultMessageDTO updatePassword(String oldPassword, String newPassword){
        CompletableFuture<ResultMessageDTO> completableFuture = new CompletableFuture<ResultMessageDTO>();

        Amplify.Auth.updatePassword(
                oldPassword,
                newPassword,
                () -> {
                    completableFuture.complete(ResultMessageController.SUCCESS_MESSAGE);
                },
                error -> {
                    String message = error.getCause().getMessage();
                    ResultMessageDTO resultMessageDTO = new ResultMessageDTO(ResultMessageController.ERROR_CODE_AMPLIFY, message);
                    completableFuture.complete(resultMessageDTO);
                }
        );

        ResultMessageDTO result = null;
        try {
            result = completableFuture.get();
        }
        catch (ExecutionException | InterruptedException e) {
            result = ResultMessageController.ERROR_MESSAGE_FAILURE_CLIENT;
        }

        return result;
    }

    public GetEmailResponseDTO getEmail(){
        GetEmailResponseDTO result = new GetEmailResponseDTO();

        CompletableFuture<GetEmailResponseDTO> completableFuture = new CompletableFuture<GetEmailResponseDTO>();

        GetAuthSessionResponseDTO getAuthSessionResponseDTO = fetchAuthSessione();
        if(!ResultMessageController.isSuccess(getAuthSessionResponseDTO.getResultMessage())){
            result.setResultMessage(getAuthSessionResponseDTO.getResultMessage());
            return result;
        }


        //Signed in with cognito
        AWSCognitoAuthSession authSession = getAuthSessionResponseDTO.getAuthSessione();
        if(!authSession.isSignedIn()){
            result.setResultMessage(ResultMessageController.ERROR_MESSAGE_FAILURE_CLIENT);
            return result;
        }


        Amplify.Auth.fetchUserAttributes(
                attributes -> {
                    for (AuthUserAttribute attribute : attributes) {
                        if (attribute.getKey().getKeyString().equals("email")) {
                            GetEmailResponseDTO getEmailResponseDTO = new GetEmailResponseDTO(ResultMessageController.SUCCESS_MESSAGE,attribute.getValue());
                            completableFuture.complete(getEmailResponseDTO);
                        }
                    }
                },
                error -> {
                    String message = error.getCause().getMessage();
                    Log.e("TAG","error",error);
                    ResultMessageDTO resultMessageDTO = new ResultMessageDTO(ResultMessageController.ERROR_CODE_AMPLIFY, message);
                    GetEmailResponseDTO getEmailResponseDTO = new GetEmailResponseDTO(resultMessageDTO, null);
                    completableFuture.complete(getEmailResponseDTO);
                }
        );


        try {
            result = completableFuture.get();
        }
        catch (ExecutionException | InterruptedException e) {
            ResultMessageDTO resultMessageDTO = ResultMessageController.ERROR_MESSAGE_FAILURE_CLIENT;
            result = new GetEmailResponseDTO(resultMessageDTO,null);
        }

        return result;
    }

    public ResultMessageDTO startPasswordRecovery(String username){
        CompletableFuture<ResultMessageDTO> completableFuture = new CompletableFuture<ResultMessageDTO>();
        Amplify.Auth.resetPassword(
                username,
                result -> {
                    completableFuture.complete(ResultMessageController.SUCCESS_MESSAGE);
                },
                error -> {
                    String message = error.getCause().getMessage();
                    ResultMessageDTO resultMessageDTO = new ResultMessageDTO(ResultMessageController.ERROR_CODE_AMPLIFY, message);
                    completableFuture.complete(resultMessageDTO);
                }
        );

        ResultMessageDTO result = null;
        try {
            result = completableFuture.get();
        }
        catch (ExecutionException | InterruptedException e) {
            result = ResultMessageController.ERROR_MESSAGE_FAILURE_CLIENT;
        }

        return result;
    }

    public ResultMessageDTO completePasswordRecovery(String confirmationCode, String password){
        CompletableFuture<ResultMessageDTO> completableFuture = new CompletableFuture<ResultMessageDTO>();

        Amplify.Auth.confirmResetPassword(
                password,
                confirmationCode,
                () -> {
                    completableFuture.complete(ResultMessageController.SUCCESS_MESSAGE);
                },
                error ->{
                    String message = error.getCause().getMessage();
                    ResultMessageDTO resultMessageDTO = new ResultMessageDTO(ResultMessageController.ERROR_CODE_AMPLIFY, message);
                    completableFuture.complete(resultMessageDTO);
                }
        );

        ResultMessageDTO result = null;
        try {
            result = completableFuture.get();
        }
        catch (ExecutionException | InterruptedException e) {
            result = ResultMessageController.ERROR_MESSAGE_FAILURE_CLIENT;
        }
        return result;
    }

    public GetAuthSessionResponseDTO fetchAuthSessione(){

        CompletableFuture<GetAuthSessionResponseDTO> completableFuture = new CompletableFuture<GetAuthSessionResponseDTO>();

        Amplify.Auth.fetchAuthSession(
                result -> {
                    AWSCognitoAuthSession cognitoAuthSession = (AWSCognitoAuthSession) result;

                    GetAuthSessionResponseDTO getAuthSessionResponseDTO = new GetAuthSessionResponseDTO();
                    getAuthSessionResponseDTO.setAuthSessione(cognitoAuthSession);
                    getAuthSessionResponseDTO.setResultMessage(ResultMessageController.SUCCESS_MESSAGE);
                    completableFuture.complete(getAuthSessionResponseDTO);
                },
                error -> {
                    String message = error.getCause().getMessage();
                    GetAuthSessionResponseDTO getAuthSessionResponseDTO = new GetAuthSessionResponseDTO();
                    ResultMessageDTO resultMessageDTO = new ResultMessageDTO(ResultMessageController.ERROR_CODE_AMPLIFY, message);
                    getAuthSessionResponseDTO.setResultMessage(resultMessageDTO);
                    completableFuture.complete(getAuthSessionResponseDTO);
                }
        );

        GetAuthSessionResponseDTO result = null;

        try {
            result = completableFuture.get();
        }
        catch (ExecutionException | InterruptedException e) {
            result = new GetAuthSessionResponseDTO();
            ResultMessageDTO resultMessageDTO = ResultMessageController.ERROR_MESSAGE_FAILURE_CLIENT;
            result.setResultMessage(resultMessageDTO);
        }

        return result;
    }



}
