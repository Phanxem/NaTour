package com.unina.natour.controllers.exceptionHandler.exceptions.subAppException;

import com.unina.natour.controllers.exceptionHandler.exceptions.AppException;

public class NotCompletedSignOutException extends AppException {

    public NotCompletedSignOutException(){
        super();
    }

    public NotCompletedSignOutException(Exception e){
        super(e);
    }

}
