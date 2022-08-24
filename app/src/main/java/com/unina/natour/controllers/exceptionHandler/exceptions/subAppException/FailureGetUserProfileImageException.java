package com.unina.natour.controllers.exceptionHandler.exceptions.subAppException;

import com.unina.natour.controllers.exceptionHandler.exceptions.AppException;

public class FailureGetUserProfileImageException extends AppException {

    public FailureGetUserProfileImageException(){
        super();
    }

    public FailureGetUserProfileImageException(Exception e){
        super(e);
    }

}
