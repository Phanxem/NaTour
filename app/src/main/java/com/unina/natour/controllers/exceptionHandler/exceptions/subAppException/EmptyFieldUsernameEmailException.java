package com.unina.natour.controllers.exceptionHandler.exceptions.subAppException;

import com.unina.natour.controllers.exceptionHandler.exceptions.AppException;

public class EmptyFieldUsernameEmailException extends AppException {

    public EmptyFieldUsernameEmailException(){
        super();
    }

    public EmptyFieldUsernameEmailException(Exception e){
        super(e);
    }

}
