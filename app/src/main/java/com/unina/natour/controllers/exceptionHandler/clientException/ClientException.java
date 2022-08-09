package com.unina.natour.controllers.exceptionHandler.clientException;

public class ClientException extends Exception{

    private long code;
    private String message;

    public ClientException(){
        this.code = -100;
        this.message = "TODO Exception";
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
