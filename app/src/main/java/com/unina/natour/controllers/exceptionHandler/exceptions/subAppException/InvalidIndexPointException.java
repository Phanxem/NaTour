package com.unina.natour.controllers.exceptionHandler.exceptions.subAppException;

import com.unina.natour.controllers.exceptionHandler.exceptions.AppException;

public class InvalidIndexPointException extends AppException {

    public InvalidIndexPointException(){
        super();
    }

    public InvalidIndexPointException(Exception e){
        super(e);
    }
}
