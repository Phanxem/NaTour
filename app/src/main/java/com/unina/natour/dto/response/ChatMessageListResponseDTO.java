package com.unina.natour.dto.response;

import java.util.ArrayList;
import java.util.List;

public class ChatMessageListResponseDTO {
    private MessageResponseDTO resultMessage;
    private List<ChatMessageResponseDTO> messages;

    public ChatMessageListResponseDTO(){
        this.messages = new ArrayList<ChatMessageResponseDTO>();
    }

    public MessageResponseDTO getResultMessage() {
        return resultMessage;
    }

    public void setResultMessage(MessageResponseDTO resultMessage) {
        this.resultMessage = resultMessage;
    }

    public List<ChatMessageResponseDTO> getMessages() {
        return messages;
    }

    public void setMessages(List<ChatMessageResponseDTO> messages) {
        this.messages = messages;
    }
}
