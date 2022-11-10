package com.unina.natour.dto.response;

public class GetEmailResponseDTO {
    private ResultMessageDTO resultMessage;
    private String email;

    public GetEmailResponseDTO(ResultMessageDTO resultMessage, String email){
        this.resultMessage = resultMessage;
        this.email = email;
    }



    public GetEmailResponseDTO(){
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
