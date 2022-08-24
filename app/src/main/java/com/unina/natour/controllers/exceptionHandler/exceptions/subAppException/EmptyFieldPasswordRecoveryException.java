package com.unina.natour.controllers.exceptionHandler.exceptions.subAppException;

import com.unina.natour.controllers.exceptionHandler.exceptions.AppException;

public class EmptyFieldPasswordRecoveryException extends AppException {

    public EmptyFieldPasswordRecoveryException(){
        super();
    }

    public EmptyFieldPasswordRecoveryException(Exception e){
        super(e);
    }

}
