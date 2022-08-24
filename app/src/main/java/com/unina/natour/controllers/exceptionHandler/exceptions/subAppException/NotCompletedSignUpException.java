package com.unina.natour.controllers.exceptionHandler.exceptions.subAppException;

import com.unina.natour.controllers.exceptionHandler.exceptions.AppException;

public class NotCompletedSignUpException extends AppException {

    public NotCompletedSignUpException(){
        super();
    }

    public NotCompletedSignUpException(Exception e){
        super(e);
    }

}
