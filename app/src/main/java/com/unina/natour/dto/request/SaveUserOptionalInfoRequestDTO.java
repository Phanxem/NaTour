package com.unina.natour.dto.request;

public class SaveUserOptionalInfoRequestDTO {
    private String placeOfResidence;
    private String dateOfBirth;

    public SaveUserOptionalInfoRequestDTO(){};

    public SaveUserOptionalInfoRequestDTO(String placeOfResidence, String dateOfBirth) {
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
