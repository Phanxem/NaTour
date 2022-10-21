package com.unina.natour.dto.response;

import com.unina.natour.dto.response.composted.ItineraryElementResponseDTO;

import java.util.ArrayList;
import java.util.List;

public class GetListItineraryResponseDTO {

    private ResultMessageDTO resultMessage;
    private List<GetItineraryResponseDTO> listItinerary;

    public GetListItineraryResponseDTO(){
        this.listItinerary = new ArrayList<GetItineraryResponseDTO>();
    }


    public ResultMessageDTO getResultMessage() {
        return resultMessage;
    }

    public void setResultMessage(ResultMessageDTO resultMessage) {
        this.resultMessage = resultMessage;
    }

    public List<GetItineraryResponseDTO> getListItinerary() {
        return listItinerary;
    }

    public void setListItinerary(List<GetItineraryResponseDTO> listItinerary) {
        this.listItinerary = listItinerary;
    }
}
