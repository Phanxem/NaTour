package com.unina.natour.controllers.exceptionHandler.exceptions.subAppException;

import com.unina.natour.controllers.exceptionHandler.exceptions.AppException;

public class NotCompletedAddItineraryException extends AppException {

    public NotCompletedAddItineraryException(){
        super();
    }

    public NotCompletedAddItineraryException(Exception e){
        super(e);
    }
}
