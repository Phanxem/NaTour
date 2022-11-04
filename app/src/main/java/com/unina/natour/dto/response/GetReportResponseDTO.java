package com.unina.natour.dto.response;

public class GetReportResponseDTO {

    private ResultMessageDTO resultMessage;

    private long id;
    private String name;
    private String dateOfInput;
    private String description;

    private long idUser;

    private long idItinerary;
    private String nameItinerary;


    public ResultMessageDTO getResultMessage() {
        return resultMessage;
    }

    public void setResultMessage(ResultMessageDTO resultMessage) {
        this.resultMessage = resultMessage;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

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

    public String getNameItinerary() {
        return nameItinerary;
    }

    public void setNameItinerary(String nameItinerary) {
        this.nameItinerary = nameItinerary;
    }
}
