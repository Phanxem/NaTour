package com.unina.natour.controllers.exceptionHandler.exceptions;


import com.unina.natour.controllers.ResultMessageController;
import com.unina.natour.dto.response.ResultMessageDTO;

public class ServerException extends Exception {
    private long code;
    private String message;

    public ServerException(){
        this(ResultMessageController.getUnknownErrorMessage());
    }

    public ServerException(ResultMessageDTO resultMessageDTO){
        setCode(resultMessageDTO.getCode());
        setMessage(resultMessageDTO.getMessage());
    }

    public ResultMessageDTO getMessageDTO() {
        return new ResultMessageDTO(getCode(), getMessage());
    }

    public void setMessageDTO(ResultMessageDTO resultMessageDTO) {
        setCode(resultMessageDTO.getCode());
        setMessage(resultMessageDTO.getMessage());
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
