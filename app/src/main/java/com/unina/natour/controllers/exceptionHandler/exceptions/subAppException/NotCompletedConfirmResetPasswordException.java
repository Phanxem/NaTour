package com.unina.natour.controllers.exceptionHandler.exceptions.subAppException;

import com.unina.natour.controllers.exceptionHandler.exceptions.AppException;

public class NotCompletedConfirmResetPasswordException extends AppException {

    public NotCompletedConfirmResetPasswordException(){
        super();
    }

    public NotCompletedConfirmResetPasswordException(Exception e){
        super(e);
    }

}
