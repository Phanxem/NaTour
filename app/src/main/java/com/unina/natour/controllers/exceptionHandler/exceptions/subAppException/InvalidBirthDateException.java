package com.unina.natour.controllers.exceptionHandler.exceptions.subAppException;

import com.unina.natour.controllers.exceptionHandler.exceptions.AppException;

public class InvalidBirthDateException extends AppException {

    public InvalidBirthDateException(){
        super();
    }

    public InvalidBirthDateException(Exception e){
        super(e);
    }

}
