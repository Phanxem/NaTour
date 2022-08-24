package com.unina.natour.controllers.exceptionHandler.exceptions.subAppException;

import com.unina.natour.controllers.exceptionHandler.exceptions.AppException;

public class GPSFeatureNotPresentException extends AppException {

    public GPSFeatureNotPresentException(){
        super();
    }

    public GPSFeatureNotPresentException(Exception e){
        super(e);
    }

}
