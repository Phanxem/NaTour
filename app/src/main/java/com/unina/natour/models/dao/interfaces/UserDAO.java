package com.unina.natour.models.dao.interfaces;

import android.graphics.Bitmap;

import com.unina.natour.controllers.exceptionHandler.exceptions.ServerException;
import com.unina.natour.dto.response.MessageResponseDTO;
import com.unina.natour.dto.request.OptionalInfoRequestDTO;
import com.unina.natour.dto.response.UserListResponseDTO;
import com.unina.natour.dto.response.UserResponseDTO;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

public interface UserDAO extends ServerDAO{

    UserResponseDTO getUser(String username);
    UserResponseDTO getUser(long id);

    //Bitmap getUserProfileImage(String username) throws ExecutionException, InterruptedException, ServerException, IOException;
    //File getUserProfileImage(long id);


    MessageResponseDTO updateProfileImage(Bitmap profileImage);
    MessageResponseDTO updateOptionalInfo(OptionalInfoRequestDTO optionalInfo);

    MessageResponseDTO removeProfileImage();

    UserListResponseDTO getUserWithConversation();

    UserListResponseDTO getUserByUsername(String researchString);
}
