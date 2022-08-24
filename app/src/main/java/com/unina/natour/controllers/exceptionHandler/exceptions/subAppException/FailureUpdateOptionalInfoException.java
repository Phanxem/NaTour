package com.unina.natour.controllers.exceptionHandler.exceptions.subAppException;

import com.unina.natour.controllers.exceptionHandler.exceptions.AppException;

public class FailureUpdateOptionalInfoException extends AppException {

    public FailureUpdateOptionalInfoException(){
        super();
    }

    public FailureUpdateOptionalInfoException(Exception e){
        super(e);
    }

}
