package com.unina.natour.dto.request;

import com.unina.natour.dto.response.MessageResponseDTO;

public class ReportRequestDTO {

    private String name;
    private String dateOfInput;
    private String description;

    private long id_itinerary;
    private long id_user;


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

    public long getId_itinerary() {
        return id_itinerary;
    }

    public void setId_itinerary(long id_itinerary) {
        this.id_itinerary = id_itinerary;
    }

    public long getId_user() {
        return id_user;
    }

    public void setId_user(long id_user) {
        this.id_user = id_user;
    }

}
