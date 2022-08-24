package com.unina.natour.controllers.exceptionHandler.exceptions.subAppException;

import com.unina.natour.controllers.exceptionHandler.exceptions.AppException;

public class NotExistParentDirectoryException extends AppException{

    public NotExistParentDirectoryException(){
        super();
    }

    public NotExistParentDirectoryException(Exception e){
        super(e);
    }

}
