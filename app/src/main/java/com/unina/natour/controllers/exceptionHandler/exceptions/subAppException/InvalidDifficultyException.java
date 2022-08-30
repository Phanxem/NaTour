package com.unina.natour.controllers.exceptionHandler.exceptions.subAppException;

import com.unina.natour.controllers.exceptionHandler.exceptions.AppException;

public class InvalidDifficultyException extends AppException {

    public InvalidDifficultyException(){
        super();
    }

    public InvalidDifficultyException(Exception e){
        super(e);
    }

}
