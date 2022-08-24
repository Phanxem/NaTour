package com.unina.natour.controllers.exceptionHandler.exceptions.subAppException;

import com.unina.natour.controllers.exceptionHandler.exceptions.AppException;

public class NotCompletedFindRouteException extends AppException {

    public NotCompletedFindRouteException(){
        super();
    }

    public NotCompletedFindRouteException(Exception e){
        super(e);
    }
}
