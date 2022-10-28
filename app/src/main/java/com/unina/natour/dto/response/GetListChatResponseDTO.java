package com.unina.natour.dto.response;

import java.util.ArrayList;
import java.util.List;

public class GetListChatResponseDTO {

    private ResultMessageDTO resultMessage;
    private List<GetChatResponseDTO> listChat;

    public GetListChatResponseDTO(){
        this.listChat = new ArrayList<GetChatResponseDTO>();
    }

    public ResultMessageDTO getResultMessage() {
        return resultMessage;
    }
    public void setResultMessage(ResultMessageDTO resultMessage) {
        this.resultMessage = resultMessage;
    }
    public List<GetChatResponseDTO> getListChat() {
        return listChat;
    }
    public void setListChat(List<GetChatResponseDTO> listChat) {
        this.listChat = listChat;
    }

}
