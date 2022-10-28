package com.unina.natour.dto.response;

import com.unina.natour.dto.response.composted.GetUserWithImageResponseDTO;

import java.util.ArrayList;
import java.util.List;

public class GetListUserResponseDTO {


    private ResultMessageDTO resultMessage;
    private List<GetUserResponseDTO> listUser;

    public GetListUserResponseDTO(){
        this.listUser = new ArrayList<GetUserResponseDTO>();
    }

    public ResultMessageDTO getResultMessage() {
        return resultMessage;
    }
    public void setResultMessage(ResultMessageDTO resultMessage) {
        this.resultMessage = resultMessage;
    }
    public List<GetUserResponseDTO> getListUser() {
        return listUser;
    }
    public void setListUser(List<GetUserResponseDTO> listUser) {
        this.listUser = listUser;
    }

}
