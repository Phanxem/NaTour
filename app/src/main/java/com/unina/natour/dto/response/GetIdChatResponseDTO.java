package com.unina.natour.dto.response;

public class GetIdChatResponseDTO {

    private ResultMessageDTO resultMessage;

    private long id;

    public ResultMessageDTO getResultMessage() {
        return resultMessage;
    }

    public void setResultMessage(ResultMessageDTO resultMessage) {
        this.resultMessage = resultMessage;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
