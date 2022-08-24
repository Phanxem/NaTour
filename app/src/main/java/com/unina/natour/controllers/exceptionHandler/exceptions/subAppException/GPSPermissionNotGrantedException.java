package com.unina.natour.controllers.exceptionHandler.exceptions.subAppException;

import com.unina.natour.controllers.exceptionHandler.exceptions.AppException;

public class GPSPermissionNotGrantedException extends AppException {

    public GPSPermissionNotGrantedException(){
        super();
    }

    public GPSPermissionNotGrantedException(Exception e){
        super(e);
    }
}
