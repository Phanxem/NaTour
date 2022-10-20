package com.unina.natour.dto.response;

import java.util.ArrayList;
import java.util.List;

public class GetListChatMessageResponseDTO {
    private ResultMessageDTO resultMessage;
    private List<GetChatMessageResponseDTO> listMessage;

    public GetListChatMessageResponseDTO(){
        this.listMessage = new ArrayList<GetChatMessageResponseDTO>();
    }

    public ResultMessageDTO getResultMessage() {
        return resultMessage;
    }

    public void setResultMessage(ResultMessageDTO resultMessage) {
        this.resultMessage = resultMessage;
    }

    public List<GetChatMessageResponseDTO> getListMessage() {
        return listMessage;
    }

    public void setListMessage(List<GetChatMessageResponseDTO> listMessage) {
        this.listMessage = listMessage;
    }
}
