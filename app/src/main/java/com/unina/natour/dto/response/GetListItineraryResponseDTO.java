package com.unina.natour.dto.response;

import com.unina.natour.dto.response.composted.ItineraryElementResponseDTO;

import java.util.ArrayList;
import java.util.List;

public class GetListItineraryResponseDTO {

    private ResultMessageDTO resultMessage;
    private List<ItineraryElementResponseDTO> listItinerary;

    public GetListItineraryResponseDTO(){
        this.listItinerary = new ArrayList<ItineraryElementResponseDTO>();
    }


    public ResultMessageDTO getResultMessage() {
        return resultMessage;
    }

    public void setResultMessage(ResultMessageDTO resultMessage) {
        this.resultMessage = resultMessage;
    }

    public List<ItineraryElementResponseDTO> getListItinerary() {
        return listItinerary;
    }

    public void setListItinerary(List<ItineraryElementResponseDTO> listItinerary) {
        this.listItinerary = listItinerary;
    }
}
