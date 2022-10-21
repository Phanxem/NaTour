package com.unina.natour.dto.response;

public class GetChatMessageResponseDTO {

    private ResultMessageDTO resultMessage;

    private long id;
    private String body;
    private String dateOfInput;

    private long idUser;
    private long idChat;


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

    public long getIdUser() {
        return idUser;
    }

    public void setIdUser(long idUser) {
        this.idUser = idUser;
    }

    public long getIdChat() {
        return idChat;
    }

    public void setIdChat(long idChat) {
        this.idChat = idChat;
    }

    public String getDateOfInput() {
        return dateOfInput;
    }

    public void setDateOfInput(String dateOfInput) {
        this.dateOfInput = dateOfInput;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

}
