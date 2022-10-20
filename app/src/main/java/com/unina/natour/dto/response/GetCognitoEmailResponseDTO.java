package com.unina.natour.dto.response;

public class GetCognitoEmailResponseDTO {
    private ResultMessageDTO resultMessage;
    private String email;

    public GetCognitoEmailResponseDTO(ResultMessageDTO resultMessage, String email){
        this.resultMessage = resultMessage;
        this.email = email;
    }



    public GetCognitoEmailResponseDTO(){
        this.resultMessage = new ResultMessageDTO();
    }

    public ResultMessageDTO getResultMessage() {
        return resultMessage;
    }

    public void setResultMessage(ResultMessageDTO resultMessage) {
        this.resultMessage = resultMessage;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
