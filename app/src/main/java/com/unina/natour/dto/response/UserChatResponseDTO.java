package com.unina.natour.dto.response;

import android.graphics.Bitmap;

public class UserChatResponseDTO {

    private Long id;
    private String username;
    private Bitmap profileImage;
    private boolean messagesToRead;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Bitmap getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(Bitmap profileImage) {
        this.profileImage = profileImage;
    }

    public boolean hasMessagesToRead() {
        return messagesToRead;
    }

    public void setMessagesToRead(boolean messagesToRead) {
        this.messagesToRead = messagesToRead;
    }
}
