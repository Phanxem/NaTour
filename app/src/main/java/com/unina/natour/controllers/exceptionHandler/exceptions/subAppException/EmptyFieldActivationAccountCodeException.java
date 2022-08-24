package com.unina.natour.controllers.exceptionHandler.exceptions.subAppException;

import com.unina.natour.controllers.exceptionHandler.exceptions.AppException;

public class EmptyFieldActivationAccountCodeException extends AppException {

    public EmptyFieldActivationAccountCodeException(){
        super();
    }

    public EmptyFieldActivationAccountCodeException(Exception e){
        super(e);
    }

}
