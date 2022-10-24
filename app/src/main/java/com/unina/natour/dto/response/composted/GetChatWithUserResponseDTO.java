package com.unina.natour.dto.response.composted;

import android.graphics.Bitmap;

import com.unina.natour.dto.response.ResultMessageDTO;

public class GetChatWithUserResponseDTO {

    private ResultMessageDTO resultMessage;

    private Long idUser;
    private String nameChat;
    private Bitmap profileImage;

    private boolean hasMessagesToRead;


    public ResultMessageDTO getResultMessage() {
        return resultMessage;
    }

    public void setResultMessage(ResultMessageDTO resultMessage) {
        this.resultMessage = resultMessage;
    }

    public Long getIdUser() {
        return idUser;
    }

    public void setIdUser(Long idUser) {
        this.idUser = idUser;
    }

    public String getNameChat() {
        return nameChat;
    }

    public void setNameChat(String nameChat) {
        this.nameChat = nameChat;
    }

    public Bitmap getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(Bitmap profileImage) {
        this.profileImage = profileImage;
    }

    public boolean hasMessagesToRead() {
        return hasMessagesToRead;
    }

    public void setHasMessagesToRead(boolean hasMessagesToRead) {
        this.hasMessagesToRead = hasMessagesToRead;
    }

    public boolean isHasMessagesToRead() {
        return hasMessagesToRead;
    }
}
