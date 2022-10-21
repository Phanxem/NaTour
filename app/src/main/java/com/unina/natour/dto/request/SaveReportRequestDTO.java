package com.unina.natour.dto.request;

public class SaveReportRequestDTO {

    private String name;
    private String dateOfInput;
    private String description;

    private long idItinerary;
    private long idUser;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDateOfInput() {
        return dateOfInput;
    }

    public void setDateOfInput(String dateOfInput) {
        this.dateOfInput = dateOfInput;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getIdItinerary() {
        return idItinerary;
    }

    public void setIdItinerary(long idItinerary) {
        this.idItinerary = idItinerary;
    }

    public long getIdUser() {
        return idUser;
    }

    public void setIdUser(long idUser) {
        this.idUser = idUser;
    }

}
