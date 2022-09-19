package com.unina.natour.dto.response;

public class EmailResponseDTO {
    private MessageResponseDTO resultMessage;
    private String email;

    public EmailResponseDTO(MessageResponseDTO resultMessage, String email){
        this.resultMessage = resultMessage;
        this.email = email;
    }



    public EmailResponseDTO(){
        this.resultMessage = new MessageResponseDTO();
    }

    public MessageResponseDTO getResultMessage() {
        return resultMessage;
    }

    public void setResultMessage(MessageResponseDTO resultMessage) {
        this.resultMessage = resultMessage;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
