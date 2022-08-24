package com.unina.natour.controllers.exceptionHandler.exceptions.subAppException;

import com.unina.natour.controllers.exceptionHandler.exceptions.AppException;

public class FailureReadProfileImageException extends AppException {

    public FailureReadProfileImageException(){
        super();
    }

    public FailureReadProfileImageException(Exception e){
        super(e);
    }

}
