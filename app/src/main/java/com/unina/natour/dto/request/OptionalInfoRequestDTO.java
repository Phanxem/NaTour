package com.unina.natour.dto.request;

import com.unina.natour.dto.response.MessageResponseDTO;

public class OptionalInfoRequestDTO {



    private String placeOfResidence;
    private String dateOfBirth;

    public OptionalInfoRequestDTO(){};

    public OptionalInfoRequestDTO(String placeOfResidence, String dateOfBirth) {
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
