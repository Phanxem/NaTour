package com.unina.natour.dto.response;

public class ChatMessageResponseDTO {

    private MessageResponseDTO resultMessage;

    private long userSourceId;
    private long userDestinationId;
    private String dateOfInput;
    private String message;



    public MessageResponseDTO getResultMessage() {
        return resultMessage;
    }

    public void setResultMessage(MessageResponseDTO resultMessage) {
        this.resultMessage = resultMessage;
    }

    public long getUserSourceId() {
        return userSourceId;
    }

    public void setUserSourceId(long userSourceId) {
        this.userSourceId = userSourceId;
    }

    public long getUserDestinationId() {
        return userDestinationId;
    }

    public void setUserDestinationId(long userDestinationId) {
        this.userDestinationId = userDestinationId;
    }

    public String getDateOfInput() {
        return dateOfInput;
    }

    public void setDateOfInput(String dateOfInput) {
        this.dateOfInput = dateOfInput;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
