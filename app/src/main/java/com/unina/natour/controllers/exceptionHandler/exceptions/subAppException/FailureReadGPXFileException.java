package com.unina.natour.controllers.exceptionHandler.exceptions.subAppException;

import com.unina.natour.controllers.exceptionHandler.exceptions.AppException;

public class FailureReadGPXFileException extends AppException {

    public FailureReadGPXFileException(){
        super();
    }

    public FailureReadGPXFileException(Exception e){
        super(e);
    }

}
