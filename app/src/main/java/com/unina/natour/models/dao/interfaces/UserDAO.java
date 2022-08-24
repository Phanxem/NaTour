package com.unina.natour.models.dao.interfaces;

import android.graphics.Bitmap;

import com.unina.natour.controllers.exceptionHandler.exceptions.ServerException;
import com.unina.natour.dto.MessageDTO;
import com.unina.natour.dto.OptionalInfoDTO;
import com.unina.natour.dto.UserDTO;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public interface UserDAO {

    UserDTO getUser(String username) throws ExecutionException, InterruptedException, ServerException, IOException;
    //UserDTO getUser(long id);

    Bitmap getUserProfileImage(String username) throws ExecutionException, InterruptedException, ServerException, IOException;
    //File getUserProfileImage(long id);


    MessageDTO updateProfileImage(Bitmap profileImage) throws  IOException, ExecutionException, InterruptedException, ServerException;
    MessageDTO updateOptionalInfo(OptionalInfoDTO optionalInfo) throws  ExecutionException, InterruptedException, IOException, ServerException;

    MessageDTO removeProfileImage();

}
