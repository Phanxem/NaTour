package com.unina.natour.controllers.exceptionHandler.exceptions.subAppException;

import com.unina.natour.controllers.exceptionHandler.exceptions.AppException;

public class NotCompletedGetUserProfileImageException extends AppException {

    public NotCompletedGetUserProfileImageException(){
        super();
    }

    public NotCompletedGetUserProfileImageException(Exception e){
        super(e);
    }

}
