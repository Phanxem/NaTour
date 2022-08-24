package com.unina.natour.controllers.exceptionHandler.exceptions.subAppException;

import com.unina.natour.controllers.exceptionHandler.exceptions.AppException;

public class InvalidURLFormatException extends AppException {

    public InvalidURLFormatException(){
        super();
    }

    public InvalidURLFormatException(Exception e){
        super(e);
    }

}
