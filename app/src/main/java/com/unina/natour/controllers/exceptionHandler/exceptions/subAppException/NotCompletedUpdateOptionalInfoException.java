package com.unina.natour.controllers.exceptionHandler.exceptions.subAppException;

import com.unina.natour.controllers.exceptionHandler.exceptions.AppException;

public class NotCompletedUpdateOptionalInfoException extends AppException {

    public NotCompletedUpdateOptionalInfoException(){
        super();
    }

    public NotCompletedUpdateOptionalInfoException(Exception e){
        super(e);
    }
}
