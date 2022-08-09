package com.unina.natour.models.dao.interfaces;

import android.graphics.Bitmap;

import com.unina.natour.dto.MessageDTO;
import com.unina.natour.dto.OptionalInfoDTO;
import com.unina.natour.dto.UserDTO;

import java.io.File;

public interface UserDAO {

    UserDTO getUser(String username);
    //UserDTO getUser(long id);

    Bitmap getUserProfileImage(String username);
    //File getUserProfileImage(long id);


    MessageDTO updateProfileImage(Bitmap profileImage);
    MessageDTO updateOptionalInfo(OptionalInfoDTO optionalInfo);


}
