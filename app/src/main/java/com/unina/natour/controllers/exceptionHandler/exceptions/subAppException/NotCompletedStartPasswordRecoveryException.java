package com.unina.natour.controllers.exceptionHandler.exceptions.subAppException;

import com.unina.natour.controllers.exceptionHandler.exceptions.AppException;

public class NotCompletedStartPasswordRecoveryException extends AppException {

    public NotCompletedStartPasswordRecoveryException(){
        super();
    }

    public NotCompletedStartPasswordRecoveryException(Exception e){
        super(e);
    }
}
