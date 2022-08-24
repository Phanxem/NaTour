package com.unina.natour.controllers.exceptionHandler.exceptions.subAppException;

import com.unina.natour.controllers.exceptionHandler.exceptions.AppException;

public class InvalidProfileImageException extends AppException {

    public InvalidProfileImageException(){
        super();
    }

    public InvalidProfileImageException(Exception e){
        super(e);
    }

}
