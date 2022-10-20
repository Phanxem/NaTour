package com.unina.natour.models.dao.implementation;

import com.google.gson.JsonObject;
import com.unina.natour.controllers.MessageController;
import com.unina.natour.dto.response.ResultMessageDTO;

public class ResultMessageDAO extends ServerDAO {

    public static ResultMessageDTO toMessageDTO(JsonObject jsonObject){
        if(!jsonObject.has("code")  || !jsonObject.has("message")){
            return MessageController.getUnknownErrorMessage();
        }

        long code = jsonObject.get("code").getAsLong();
        String message = jsonObject.get("message").getAsString();

        ResultMessageDTO resultMessageDTO = new ResultMessageDTO(code,message);

        return resultMessageDTO;
    }
}
