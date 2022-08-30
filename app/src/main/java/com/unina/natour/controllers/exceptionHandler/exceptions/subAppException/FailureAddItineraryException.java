package com.unina.natour.controllers.exceptionHandler.exceptions.subAppException;

import com.unina.natour.controllers.exceptionHandler.exceptions.AppException;

public class FailureAddItineraryException extends AppException {

    public FailureAddItineraryException(){
        super();
    }

    public FailureAddItineraryException(Exception e){
        super(e);
    }

}
