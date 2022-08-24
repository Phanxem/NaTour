package com.unina.natour.controllers.exceptionHandler.exceptions.subAppException;

import com.unina.natour.controllers.exceptionHandler.exceptions.AppException;

public class FailureUpdateProfileImageException extends AppException {

    public FailureUpdateProfileImageException(){
        super();
    }

    public FailureUpdateProfileImageException(Exception e){
        super(e);
    }

}
