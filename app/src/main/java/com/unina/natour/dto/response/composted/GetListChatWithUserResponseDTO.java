package com.unina.natour.dto.response.composted;

import com.unina.natour.dto.response.ResultMessageDTO;

import java.util.ArrayList;
import java.util.List;

public class GetListChatWithUserResponseDTO {

    private ResultMessageDTO resultMessage;
    private List<GetChatWithUserResponseDTO> listChat;

    public GetListChatWithUserResponseDTO(){
        this.listChat = new ArrayList<GetChatWithUserResponseDTO>();
    }


    public ResultMessageDTO getResultMessage() {
        return resultMessage;
    }

    public void setResultMessage(ResultMessageDTO resultMessage) {
        this.resultMessage = resultMessage;
    }

    public List<GetChatWithUserResponseDTO> getListChat() {
        return listChat;
    }

    public void setListChat(List<GetChatWithUserResponseDTO> listChat) {
        this.listChat = listChat;
    }
}
