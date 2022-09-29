package com.unina.natour.models.dao.implementation;

import com.google.gson.JsonObject;
import com.unina.natour.controllers.MessageController;
import com.unina.natour.dto.response.MessageResponseDTO;
import com.unina.natour.models.dao.interfaces.MessageDAO;

public class MessageDAOImpl extends ServerDAO implements MessageDAO {

    public static MessageResponseDTO toMessageDTO(JsonObject jsonObject){
        if(!jsonObject.has("code")  || !jsonObject.has("message")){
            return MessageController.getUnknownErrorMessage();
        }

        long code = jsonObject.get("code").getAsLong();
        String message = jsonObject.get("message").getAsString();

        MessageResponseDTO messageResponseDTO = new MessageResponseDTO(code,message);

        return messageResponseDTO;
    }
}
