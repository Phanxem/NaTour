package com.unina.natour.dto;

import java.util.Date;

public class OptionalInfoDTO {

    private String placeOfResidence;
    private String dateOfBirth;

    public OptionalInfoDTO(){};

    public OptionalInfoDTO(String placeOfResidence, String dateOfBirth) {
        this.placeOfResidence = placeOfResidence;
        this.dateOfBirth = dateOfBirth;

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
}
