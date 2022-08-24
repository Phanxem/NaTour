package com.unina.natour.controllers.exceptionHandler.exceptions.subAppException;

import com.unina.natour.controllers.exceptionHandler.exceptions.AppException;

public class NotCompletedFetchAuthSessionException extends AppException {

    public NotCompletedFetchAuthSessionException(){
        super();
    }

    public NotCompletedFetchAuthSessionException(Exception e){
        super(e);
    }
}
