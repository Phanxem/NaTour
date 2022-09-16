package com.unina.natour.dto;

import android.graphics.Bitmap;

import java.util.Date;

public class UserDTO {

    private Long id;
    private String username;
    private Bitmap profileImage;
    private String placeOfResidence;
    private String dateOfBirth;

    private boolean isFacebookLinked;
    private boolean isGoogleLinked;

    public UserDTO() {
    }

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

    public String getPlaceOfResidence() {
        return placeOfResidence;
    }

    public void setPlaceOfResidence(String placeOfResidence) {
        this.placeOfResidence = placeOfResidence;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public boolean isFacebookLinked() {
        return isFacebookLinked;
    }

    public void setFacebookLinked(boolean facebookLinked) {
        isFacebookLinked = facebookLinked;
    }

    public boolean isGoogleLinked() {
        return isGoogleLinked;
    }

    public void setGoogleLinked(boolean googleLinked) {
        isGoogleLinked = googleLinked;
    }
}
