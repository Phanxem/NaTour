package com.unina.natour.dto.response;

import java.util.ArrayList;
import java.util.List;

public class UserListResponseDTO {

    private MessageResponseDTO resultMessage;
    private List<UserResponseDTO> users;

    public UserListResponseDTO(){
        this.users = new ArrayList<UserResponseDTO>();
    }


    public MessageResponseDTO getResultMessage() {
        return resultMessage;
    }

    public void setResultMessage(MessageResponseDTO resultMessage) {
        this.resultMessage = resultMessage;
    }

    public List<UserResponseDTO> getUsers() {
        return users;
    }

    public void setUsers(List<UserResponseDTO> users) {
        this.users = users;
    }
}
