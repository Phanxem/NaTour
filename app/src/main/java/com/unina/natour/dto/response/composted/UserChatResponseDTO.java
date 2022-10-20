package com.unina.natour.dto.response.composted;

import android.graphics.Bitmap;

public class UserChatResponseDTO {

    private Long id;

    private String nameChat;
    private Bitmap profileImage;

    private String lastMessage;
    private String inputTime;
    private boolean hasMessagesToRead;



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getInputTime() {
        return inputTime;
    }

    public void setInputTime(String inputTime) {
        this.inputTime = inputTime;
    }

    public boolean isHasMessagesToRead() {
        return hasMessagesToRead;
    }
}
