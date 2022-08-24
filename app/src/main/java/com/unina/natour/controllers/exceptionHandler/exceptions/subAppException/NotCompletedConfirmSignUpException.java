package com.unina.natour.controllers.exceptionHandler.exceptions.subAppException;

import com.unina.natour.controllers.exceptionHandler.exceptions.AppException;

public class NotCompletedConfirmSignUpException extends AppException {

    public NotCompletedConfirmSignUpException(){
        super();
    }

    public NotCompletedConfirmSignUpException(Exception e){
        super(e);
    }
}
