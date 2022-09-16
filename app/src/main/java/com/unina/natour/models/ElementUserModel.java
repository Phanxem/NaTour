package com.unina.natour.models;

import android.graphics.Bitmap;

public class ElementUserModel extends NaTourModel{

    private Bitmap profileImage;
    private String username;


    public ElementUserModel(){
        super();
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
}
