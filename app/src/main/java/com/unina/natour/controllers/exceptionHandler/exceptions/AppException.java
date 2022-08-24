package com.unina.natour.controllers.exceptionHandler.exceptions;

public class AppException extends Exception{
    private long code;
    private String message;

    public AppException(){ }

    public AppException(Exception exception){
        super(exception);
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
