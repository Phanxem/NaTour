package com.unina.natour.controllers.exceptionHandler.exceptions.subAppException;

import com.unina.natour.controllers.exceptionHandler.exceptions.AppException;

public class NotCompletedFetchUserAttiributes extends AppException {

    public NotCompletedFetchUserAttiributes(){
        super();
    }

    public NotCompletedFetchUserAttiributes(Exception e){
        super(e);
    }

}
