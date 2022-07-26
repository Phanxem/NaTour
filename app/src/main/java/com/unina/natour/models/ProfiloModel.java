package com.unina.natour.models;

import android.graphics.Bitmap;

import java.util.Date;

public class ProfiloModel extends NaTourModel{

    private Long id;
    private String username;
    private String email;
    private Bitmap profileImage;
    private String placeOfResidence;
    private String dateOfBirth;

    public ProfiloModel(){
        super();
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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



    public void clear(){
        this.id = -1l;
        this.username = null;
        this.email = null;
        this.profileImage = null;
        this.placeOfResidence = null;
        this.dateOfBirth = null;

    }
}
