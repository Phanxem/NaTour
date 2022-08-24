package com.unina.natour.controllers.exceptionHandler.exceptions.subAppException;

import com.unina.natour.controllers.exceptionHandler.exceptions.AppException;

public class NotCompletedSignInException extends AppException {

    public NotCompletedSignInException(){
        super();
    }

    public NotCompletedSignInException(Exception e){
        super(e);
    }

}
