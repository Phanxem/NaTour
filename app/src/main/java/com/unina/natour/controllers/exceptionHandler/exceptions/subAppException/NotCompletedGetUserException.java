package com.unina.natour.controllers.exceptionHandler.exceptions.subAppException;

import com.unina.natour.controllers.exceptionHandler.exceptions.AppException;

public class NotCompletedGetUserException extends AppException {

    public NotCompletedGetUserException(){
        super();
    }

    public NotCompletedGetUserException(Exception e){
        super(e);
    }

}
