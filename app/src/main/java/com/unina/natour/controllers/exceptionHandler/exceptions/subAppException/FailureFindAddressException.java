package com.unina.natour.controllers.exceptionHandler.exceptions.subAppException;

import com.unina.natour.controllers.exceptionHandler.exceptions.AppException;

public class FailureFindAddressException extends AppException {

    public FailureFindAddressException(){
        super();
    }

    public FailureFindAddressException(Exception e){
        super(e);
    }

}
