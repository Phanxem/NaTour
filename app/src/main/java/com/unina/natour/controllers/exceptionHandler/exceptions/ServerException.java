package com.unina.natour.controllers.exceptionHandler.exceptions;


import com.unina.natour.dto.MessageDTO;

public class ServerException extends Exception {
    private long code;
    private String message;

    public ServerException(){
        this(MessageDTO.unknownErrorMessage());
    }

    public ServerException(MessageDTO messageDTO){
        setCode(messageDTO.getCode());
        setMessage(messageDTO.getMessage());
    }

    public MessageDTO getMessageDTO() {
        return new MessageDTO(getCode(), getMessage());
    }

    public void setMessageDTO(MessageDTO messageDTO) {
        setCode(messageDTO.getCode());
        setMessage(messageDTO.getMessage());
    }

    public long getCode() {
        return code;
    }
    public void setCode(long code) {
        this.code = code;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
}
