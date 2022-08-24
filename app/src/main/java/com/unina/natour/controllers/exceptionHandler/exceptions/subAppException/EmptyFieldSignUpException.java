package com.unina.natour.controllers.exceptionHandler.exceptions.subAppException;

import com.unina.natour.controllers.exceptionHandler.exceptions.AppException;

public class EmptyFieldSignUpException extends AppException {

    public EmptyFieldSignUpException(){
        super();
    }

    public EmptyFieldSignUpException(Exception e){
        super(e);
    }

}
