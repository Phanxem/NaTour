package com.unina.natour.models.dao.interfaces;

import android.graphics.Bitmap;

import com.unina.natour.dto.request.SaveUserRequestDTO;
import com.unina.natour.dto.response.GetBitmapResponseDTO;
import com.unina.natour.dto.response.GetListUserResponseDTO;
import com.unina.natour.dto.response.GetUserResponseDTO;
import com.unina.natour.dto.response.ResultMessageDTO;
import com.unina.natour.dto.request.SaveUserOptionalInfoRequestDTO;
import com.unina.natour.dto.response.composted.ListUserChatResponseDTO;
import com.unina.natour.dto.response.UserIdResponseDTO;
import com.unina.natour.dto.response.composted.GetUserWithImageResponseDTO;

public interface UserDAO{

    //GETs
    public GetUserResponseDTO getUserById(long idUser);

    public GetBitmapResponseDTO getUserImageById(long idUser);
    public GetUserResponseDTO getUserByIdP(String identityProvider, String userProviderId);

    public GetUserResponseDTO getUserByIdConnection(String idConnection);


    public GetListUserResponseDTO getListUserByUsername(String username, int page);

    public GetListUserResponseDTO getListUserWithConversation(long idUser, int page);


    //POSTs
    public ResultMessageDTO addUser(SaveUserRequestDTO saveUserRequest);


    //PUTs
    public ResultMessageDTO updateProfileImage(long idUser, Bitmap profileImage);
    public ResultMessageDTO updateOptionalInfo(long idUser, SaveUserOptionalInfoRequestDTO saveOptionalInfoRequest);


    //DELETEs
    public ResultMessageDTO cancelRegistrationUser(String idCognitoUser);


    //UserResponseDTO getUser(String username);


    //Bitmap getUserProfileImage(String username) throws ExecutionException, InterruptedException, ServerException, IOException;
    //File getUserProfileImage(long id);





/*
    //COMPOSITED
    GetUserWithImageResponseDTO getUser(long id);



    ResultMessageDTO removeProfileImage();

    ListUserChatResponseDTO findUserChatByConversation();

    ListUserChatResponseDTO findUserChatByUsername(String researchString);

*/

}
