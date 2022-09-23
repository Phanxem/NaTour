package com.unina.natour.dto.response;

import java.util.ArrayList;
import java.util.List;

public class UserChatListResponseDTO {

    private MessageResponseDTO resultMessage;
    private List<UserChatResponseDTO> users;

    public UserChatListResponseDTO(){
        this.users = new ArrayList<UserChatResponseDTO>();
    }


    public MessageResponseDTO getResultMessage() {
        return resultMessage;
    }

    public void setResultMessage(MessageResponseDTO resultMessage) {
        this.resultMessage = resultMessage;
    }

    public List<UserChatResponseDTO> getUsers() {
        return users;
    }

    public void setUsers(List<UserChatResponseDTO> users) {
        this.users = users;
    }
}
