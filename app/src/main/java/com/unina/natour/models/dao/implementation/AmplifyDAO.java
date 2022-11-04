package com.unina.natour.models.dao.implementation;

import android.util.Log;

import com.amplifyframework.auth.AuthUserAttribute;
import com.amplifyframework.auth.AuthUserAttributeKey;
import com.amplifyframework.auth.cognito.AWSCognitoAuthSession;
import com.amplifyframework.auth.options.AuthSignUpOptions;
import com.amplifyframework.core.Amplify;
import com.unina.natour.R;
import com.unina.natour.controllers.ResultMessageController;
import com.unina.natour.dto.response.GetCognitoAuthSessionResponseDTO;
import com.unina.natour.dto.response.GetCognitoEmailResponseDTO;
import com.unina.natour.dto.response.ResultMessageDTO;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import javax.xml.transform.Result;

public class AmplifyDAO extends ServerDAO {

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

    public GetCognitoEmailResponseDTO getEmail(){
        GetCognitoEmailResponseDTO result = new GetCognitoEmailResponseDTO();

        CompletableFuture<GetCognitoEmailResponseDTO> completableFuture = new CompletableFuture<GetCognitoEmailResponseDTO>();

        GetCognitoAuthSessionResponseDTO getCognitoAuthSessionResponseDTO = fetchAuthSessione();
        if(!ResultMessageController.isSuccess(getCognitoAuthSessionResponseDTO.getResultMessage())){
            result.setResultMessage(getCognitoAuthSessionResponseDTO.getResultMessage());
            return result;
        }


        //Signed in with cognito
        AWSCognitoAuthSession authSession = getCognitoAuthSessionResponseDTO.getAuthSessione();
        if(!authSession.isSignedIn()){
            result.setResultMessage(ResultMessageController.ERROR_MESSAGE_FAILURE_CLIENT);
            return result;
        }


        Amplify.Auth.fetchUserAttributes(
                attributes -> {
                    for (AuthUserAttribute attribute : attributes) {
                        if (attribute.getKey().getKeyString().equals("email")) {
                            GetCognitoEmailResponseDTO getCognitoEmailResponseDTO = new GetCognitoEmailResponseDTO(ResultMessageController.SUCCESS_MESSAGE,attribute.getValue());
                            completableFuture.complete(getCognitoEmailResponseDTO);
                        }
                    }
                },
                error -> {
                    String message = error.getCause().getMessage();
                    Log.e("TAG","error",error);
                    ResultMessageDTO resultMessageDTO = new ResultMessageDTO(ResultMessageController.ERROR_CODE_AMPLIFY, message);
                    GetCognitoEmailResponseDTO getCognitoEmailResponseDTO = new GetCognitoEmailResponseDTO(resultMessageDTO, null);
                    completableFuture.complete(getCognitoEmailResponseDTO);
                }
        );


        try {
            result = completableFuture.get();
        }
        catch (ExecutionException | InterruptedException e) {
            ResultMessageDTO resultMessageDTO = ResultMessageController.ERROR_MESSAGE_FAILURE_CLIENT;
            result = new GetCognitoEmailResponseDTO(resultMessageDTO,null);
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

    public GetCognitoAuthSessionResponseDTO fetchAuthSessione(){

        CompletableFuture<GetCognitoAuthSessionResponseDTO> completableFuture = new CompletableFuture<GetCognitoAuthSessionResponseDTO>();

        Amplify.Auth.fetchAuthSession(
                result -> {
                    AWSCognitoAuthSession cognitoAuthSession = (AWSCognitoAuthSession) result;

                    GetCognitoAuthSessionResponseDTO getCognitoAuthSessionResponseDTO = new GetCognitoAuthSessionResponseDTO();
                    getCognitoAuthSessionResponseDTO.setAuthSessione(cognitoAuthSession);
                    getCognitoAuthSessionResponseDTO.setResultMessage(ResultMessageController.SUCCESS_MESSAGE);
                    completableFuture.complete(getCognitoAuthSessionResponseDTO);
                },
                error -> {
                    String message = error.getCause().getMessage();
                    GetCognitoAuthSessionResponseDTO getCognitoAuthSessionResponseDTO = new GetCognitoAuthSessionResponseDTO();
                    ResultMessageDTO resultMessageDTO = new ResultMessageDTO(ResultMessageController.ERROR_CODE_AMPLIFY, message);
                    getCognitoAuthSessionResponseDTO.setResultMessage(resultMessageDTO);
                    completableFuture.complete(getCognitoAuthSessionResponseDTO);
                }
        );

        GetCognitoAuthSessionResponseDTO result = null;

        try {
            result = completableFuture.get();
        }
        catch (ExecutionException | InterruptedException e) {
            result = new GetCognitoAuthSessionResponseDTO();
            ResultMessageDTO resultMessageDTO = ResultMessageController.ERROR_MESSAGE_FAILURE_CLIENT;
            result.setResultMessage(resultMessageDTO);
        }

        return result;
    }



}
