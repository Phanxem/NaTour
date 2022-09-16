package com.unina.natour.dto;

public class EmailDTO {
    private MessageDTO message;
    private String email;

    public EmailDTO(MessageDTO message, String email){
        this.message = message;
        this.email = email;
    }

    public EmailDTO(){
        this.message = new MessageDTO();
    }

    public MessageDTO getMessage() {
        return message;
    }

    public void setMessage(MessageDTO message) {
        this.message = message;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
