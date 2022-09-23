package com.unina.natour.models;

import android.graphics.Bitmap;

public class ElementUserModel extends NaTourModel{

    private long userId;
    private Bitmap profileImage;
    private String username;
    private boolean hasMessagesToRead;


    public ElementUserModel(){
        super();
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public Bitmap getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(Bitmap profileImage) {
        this.profileImage = profileImage;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean hasMessagesToRead() {
        return hasMessagesToRead;
    }

    public void setMessagesToRead(boolean hasMessagesToRead) {
        this.hasMessagesToRead = hasMessagesToRead;
    }

    public void clear(){
        this.userId = -1;
        this.profileImage = null;
        this.username = null;
        this.hasMessagesToRead = false;
    }
}
