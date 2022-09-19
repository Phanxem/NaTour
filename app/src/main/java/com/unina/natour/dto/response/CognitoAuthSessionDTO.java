package com.unina.natour.dto.response;

import com.amplifyframework.auth.cognito.AWSCognitoAuthSession;

public class CognitoAuthSessionDTO {
    private MessageResponseDTO resultMessage;
    private AWSCognitoAuthSession authSessione;



    public MessageResponseDTO getResultMessage() {
        return resultMessage;
    }

    public void setResultMessage(MessageResponseDTO resultMessage) {
        this.resultMessage = resultMessage;
    }

    public AWSCognitoAuthSession getAuthSessione() {
        return authSessione;
    }

    public void setAuthSessione(AWSCognitoAuthSession authSessione) {
        this.authSessione = authSessione;
    }
}
