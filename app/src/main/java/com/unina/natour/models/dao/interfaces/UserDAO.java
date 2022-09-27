package com.unina.natour.models.dao.interfaces;

import android.graphics.Bitmap;

import com.unina.natour.dto.response.MessageResponseDTO;
import com.unina.natour.dto.request.OptionalInfoRequestDTO;
import com.unina.natour.dto.response.UserChatListResponseDTO;
import com.unina.natour.dto.response.UserChatResponseDTO;
import com.unina.natour.dto.response.UserResponseDTO;

public interface UserDAO{

    UserResponseDTO getUser(String username);
    UserResponseDTO getUser(long id);

    //Bitmap getUserProfileImage(String username) throws ExecutionException, InterruptedException, ServerException, IOException;
    //File getUserProfileImage(long id);


    MessageResponseDTO updateProfileImage(Bitmap profileImage);
    MessageResponseDTO updateOptionalInfo(OptionalInfoRequestDTO optionalInfo);

    MessageResponseDTO removeProfileImage();

    UserChatListResponseDTO findUserChatByConversation();

    UserChatListResponseDTO findUserChatByUsername(String researchString);
}
