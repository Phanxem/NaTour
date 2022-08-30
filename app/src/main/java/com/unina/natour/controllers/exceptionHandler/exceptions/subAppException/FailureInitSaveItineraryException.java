package com.unina.natour.controllers.exceptionHandler.exceptions.subAppException;

import com.unina.natour.controllers.exceptionHandler.exceptions.AppException;

public class FailureInitSaveItineraryException extends AppException {

    public FailureInitSaveItineraryException(){
        super();
    }

    public FailureInitSaveItineraryException(Exception e){
        super(e);
    }

}
