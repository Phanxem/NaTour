package com.unina.natour.controllers.exceptionHandler.exceptions.subAppException;

import com.unina.natour.controllers.exceptionHandler.exceptions.AppException;

public class NotExistDirectoryException extends AppException {

    public NotExistDirectoryException(){
        super();
    }

    public NotExistDirectoryException(Exception e){
        super(e);
    }

}
