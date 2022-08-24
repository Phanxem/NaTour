package com.unina.natour.controllers.exceptionHandler.exceptions.subAppException;

import com.unina.natour.controllers.exceptionHandler.exceptions.AppException;

public class FailureGetUserException extends AppException {

    public FailureGetUserException(){
        super();
    }

    public FailureGetUserException(Exception e){
        super(e);
    }

}
