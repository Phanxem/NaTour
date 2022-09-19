package com.unina.natour.controllers.exceptionHandler.exceptions;


import com.unina.natour.controllers.MessageController;
import com.unina.natour.dto.response.MessageResponseDTO;

public class ServerException extends Exception {
    private long code;
    private String message;

    public ServerException(){
        this(MessageController.unknownErrorMessage());
    }

    public ServerException(MessageResponseDTO messageResponseDTO){
        setCode(messageResponseDTO.getCode());
        setMessage(messageResponseDTO.getMessage());
    }

    public MessageResponseDTO getMessageDTO() {
        return new MessageResponseDTO(getCode(), getMessage());
    }

    public void setMessageDTO(MessageResponseDTO messageResponseDTO) {
        setCode(messageResponseDTO.getCode());
        setMessage(messageResponseDTO.getMessage());
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
