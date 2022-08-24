package com.unina.natour.controllers.exceptionHandler.exceptions.subAppException;

import com.unina.natour.controllers.exceptionHandler.exceptions.AppException;

public class NoIndexSelectedException extends AppException {

    public NoIndexSelectedException(){
        super();
    }

    public NoIndexSelectedException(Exception e){
        super(e);
    }
}
