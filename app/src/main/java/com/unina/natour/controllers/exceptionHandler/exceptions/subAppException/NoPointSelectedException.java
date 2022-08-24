package com.unina.natour.controllers.exceptionHandler.exceptions.subAppException;

import com.unina.natour.controllers.exceptionHandler.exceptions.AppException;

public class NoPointSelectedException extends AppException {

    public NoPointSelectedException(){
        super();
    }

    public NoPointSelectedException(Exception e){
        super(e);
    }
}
