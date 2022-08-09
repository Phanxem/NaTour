package com.unina.natour.models.dao.converters;

import android.content.Context;
import android.graphics.Bitmap;

import com.google.gson.JsonObject;
import com.unina.natour.dto.MessageDTO;
import com.unina.natour.dto.UserDTO;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class JsonConverter {

    private static final String TAG = "ASF";





    public static MessageDTO toMessageDTO(JsonObject jsonObject) {

        if(!jsonObject.has("code")  || !jsonObject.has("message")){
            return null;
        }

        long code = jsonObject.get("code").getAsLong();
        String message = jsonObject.get("message").getAsString();

        MessageDTO messageDTO = new MessageDTO(code,message);

        return messageDTO;
    }

    public static UserDTO toUserDTO(JsonObject jsonObject){
        if(!jsonObject.has("id")  ||
           !jsonObject.has("username") )
        {
            return null;
        }

        long id = jsonObject.get("id").getAsLong();
        String username = jsonObject.get("username").getAsString();

        UserDTO userDTO = new UserDTO();
        userDTO.setId(id);
        userDTO.setUsername(username);


        if(jsonObject.has("placeOfResidence")){
            String placeOfResidence = jsonObject.get("placeOfResidence").getAsString();
            userDTO.setPlaceOfResidence(placeOfResidence);
        }

        if(jsonObject.has("dateOfBirth")){
            String dateOfBirth = jsonObject.get("dateOfBirth").getAsString();
            userDTO.setDateOfBirth(dateOfBirth);
        }

        return userDTO;
    }
/*
    public static UserDTO toUserDTO(JsonObject jsonObject){
        if(!jsonObject.has("id") ||
           !jsonObject.has("username") ||
           !jsonObject.has("profileImage") ||
           !jsonObject.has("placeOfResidence") ||
           !jsonObject.has("dateOfBirth") ||
           !jsonObject.has("gender") )
        {
            return null;
        }

        UserDTO userDTO = new UserDTO();

        userDTO.setId(jsonObject.get("id").getAsLong());
        userDTO.setUsername(jsonObject.get("username").getAsString());

        JsonArray jsonArray = jsonObject.get("profileImage").getAsJsonArray();


        //userDTO.setProfileImage(jsonObject.get("profileImage").getAsB);
    }
*/


}
