package com.unina.natour.controllers.exceptionHandler.exceptions.subAppException;

import com.unina.natour.controllers.exceptionHandler.exceptions.AppException;

public class NotCompletedFindAddressException extends AppException {

    public NotCompletedFindAddressException(){
        super();
    }

    public NotCompletedFindAddressException(Exception e){
        super(e);
    }

}
