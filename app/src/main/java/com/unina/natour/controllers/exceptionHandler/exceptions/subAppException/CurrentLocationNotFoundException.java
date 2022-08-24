package com.unina.natour.controllers.exceptionHandler.exceptions.subAppException;

import com.unina.natour.controllers.exceptionHandler.exceptions.AppException;

public class CurrentLocationNotFoundException extends AppException {

    public CurrentLocationNotFoundException(){
        super();
    }

    public CurrentLocationNotFoundException(Exception e){
        super(e);
    }

}
