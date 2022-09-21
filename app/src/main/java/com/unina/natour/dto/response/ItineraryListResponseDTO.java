package com.unina.natour.dto.response;

import java.util.ArrayList;
import java.util.List;

public class ItineraryListResponseDTO {

    private MessageResponseDTO resultMessage;
    private List<ItineraryElementResponseDTO> itineraries;

    public ItineraryListResponseDTO(){
        this.itineraries = new ArrayList<ItineraryElementResponseDTO>();
    }


    public MessageResponseDTO getResultMessage() {
        return resultMessage;
    }

    public void setResultMessage(MessageResponseDTO resultMessage) {
        this.resultMessage = resultMessage;
    }

    public List<ItineraryElementResponseDTO> getItineraries() {
        return itineraries;
    }

    public void setItineraries(List<ItineraryElementResponseDTO> itineraries) {
        this.itineraries = itineraries;
    }
}
