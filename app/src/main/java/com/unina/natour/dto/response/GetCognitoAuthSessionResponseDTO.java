package com.unina.natour.dto.response;

import com.amplifyframework.auth.cognito.AWSCognitoAuthSession;

public class GetCognitoAuthSessionResponseDTO {
    private ResultMessageDTO resultMessage;
    private AWSCognitoAuthSession authSessione;



    public ResultMessageDTO getResultMessage() {
        return resultMessage;
    }

    public void setResultMessage(ResultMessageDTO resultMessage) {
        this.resultMessage = resultMessage;
    }

    public AWSCognitoAuthSession getAuthSessione() {
        return authSessione;
    }

    public void setAuthSessione(AWSCognitoAuthSession authSessione) {
        this.authSessione = authSessione;
    }
}
