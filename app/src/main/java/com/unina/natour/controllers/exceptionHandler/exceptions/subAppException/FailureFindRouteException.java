package com.unina.natour.controllers.exceptionHandler.exceptions.subAppException;

import com.unina.natour.controllers.exceptionHandler.exceptions.AppException;

public class FailureFindRouteException extends AppException {

    public FailureFindRouteException(){
        super();
    }

    public FailureFindRouteException(Exception e){
        super(e);
    }

}
