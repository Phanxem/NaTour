package com.unina.natour.dto.response.composted;

import com.unina.natour.dto.response.ResultMessageDTO;
import com.unina.natour.dto.response.composted.UserChatResponseDTO;

import java.util.ArrayList;
import java.util.List;

public class ListUserChatResponseDTO {

    private ResultMessageDTO resultMessage;
    private List<UserChatResponseDTO> users;

    public ListUserChatResponseDTO(){
        this.users = new ArrayList<UserChatResponseDTO>();
    }


    public ResultMessageDTO getResultMessage() {
        return resultMessage;
    }

    public void setResultMessage(ResultMessageDTO resultMessage) {
        this.resultMessage = resultMessage;
    }

    public List<UserChatResponseDTO> getUsers() {
        return users;
    }

    public void setUsers(List<UserChatResponseDTO> users) {
        this.users = users;
    }
}
