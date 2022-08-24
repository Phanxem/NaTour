package com.unina.natour.controllers.exceptionHandler.exceptions.subAppException;

import com.unina.natour.controllers.exceptionHandler.exceptions.AppException;

public class NotCompletedUpdateProfileImageException extends AppException {

    public NotCompletedUpdateProfileImageException(){
        super();
    }

    public NotCompletedUpdateProfileImageException(Exception e){
        super(e);
    }

}
