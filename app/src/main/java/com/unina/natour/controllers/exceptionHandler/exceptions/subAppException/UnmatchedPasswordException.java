package com.unina.natour.controllers.exceptionHandler.exceptions.subAppException;

import com.unina.natour.controllers.exceptionHandler.exceptions.AppException;

public class UnmatchedPasswordException extends AppException {

    public UnmatchedPasswordException(){
        super();
    }

    public UnmatchedPasswordException(Exception e){
        super(e);
    }
}
