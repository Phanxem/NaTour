package com.unina.natour.controllers.exceptionHandler.exceptions.subAppException;

import com.unina.natour.controllers.exceptionHandler.exceptions.AppException;

public class GPSNotEnabledException extends AppException {

    public GPSNotEnabledException(){
        super();
    }

    public GPSNotEnabledException(Exception e){
        super(e);
    }
}
