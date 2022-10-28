package com.unina.natour.dto.response.composted;

import com.unina.natour.dto.response.ResultMessageDTO;

import java.util.ArrayList;
import java.util.List;

public class GetListUserWithImageResponseDTO {

    private ResultMessageDTO resultMessage;
    private List<GetUserWithImageResponseDTO> listUser;

    public GetListUserWithImageResponseDTO(){
        this.listUser = new ArrayList<GetUserWithImageResponseDTO>();
    }

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
