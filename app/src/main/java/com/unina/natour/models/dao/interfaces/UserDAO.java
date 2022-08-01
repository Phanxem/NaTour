package com.unina.natour.models.dao.interfaces;

import android.graphics.Bitmap;

import com.unina.natour.dto.*;

public interface UserDAO {

    MessageDTO updateProfileImage(Bitmap profileImage);
    MessageDTO updateOptionalInfo(OptionalInfoDTO optionalInfo);


}
