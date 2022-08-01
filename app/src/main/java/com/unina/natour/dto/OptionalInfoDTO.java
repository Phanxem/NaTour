package com.unina.natour.dto;

import java.util.Date;

public class OptionalInfoDTO {

    private String placeOfResidence;
    private Date dateOfBirth;

    public OptionalInfoDTO(){};

    public OptionalInfoDTO(String placeOfResidence, Date dateOfBirth) {
        this.placeOfResidence = placeOfResidence;
        this.dateOfBirth = dateOfBirth;

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


}
