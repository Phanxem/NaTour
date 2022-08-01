package com.unina.natour.dto;

import java.util.Date;

public class UserDTO {

    private Long id;
    private String username;
    private byte[] profileImage;
    private String placeOfResidence;
    private Date dateOfBirth;
    private String gender;


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

    public byte[] getProfileImage() {
        return profileImage;
    }
    public void setProfileImage(byte[] profileImage) {
        this.profileImage = profileImage;
    }
    public String getPlaceOfResidence() {
        return placeOfResidence;
    }
    public void setPlaceOfResidence(String placeOfResidence) {
        this.placeOfResidence = placeOfResidence;
    }
    public Date getDateOfBirth() {
        return dateOfBirth;
    }
    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
    public String getGender() {
        return gender;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }
}
