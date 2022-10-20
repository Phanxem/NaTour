package com.unina.natour.dto.response;

import com.unina.natour.dto.response.composted.GetUserWithImageResponseDTO;

import java.util.List;

public class GetListUserResponseDTO {

    private ResultMessageDTO resultMessage;
    private List<GetUserWithImageResponseDTO> listUser;


    public ResultMessageDTO getResultMessage() {
        return resultMessage;
    }
    public void setResultMessage(ResultMessageDTO resultMessage) {
        this.resultMessage = resultMessage;
    }
    public List<GetUserWithImageResponseDTO> getListUser() {
        return listUser;
    }
    public void setListUser(List<GetUserWithImageResponseDTO> listUser) {
        this.listUser = listUser;
    }

}
