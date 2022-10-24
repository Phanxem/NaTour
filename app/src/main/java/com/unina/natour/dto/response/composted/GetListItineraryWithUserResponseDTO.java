package com.unina.natour.dto.response.composted;

import com.unina.natour.dto.response.ResultMessageDTO;

import java.util.List;

public class GetListItineraryWithUserResponseDTO {

    private ResultMessageDTO resultMessage;
    private List<GetItineraryWithUserResponseDTO> listItinerary;

    public ResultMessageDTO getResultMessage() {
        return resultMessage;
    }

    public void setResultMessage(ResultMessageDTO resultMessage) {
        this.resultMessage = resultMessage;
    }

    public List<GetItineraryWithUserResponseDTO> getListItinerary() {
        return listItinerary;
    }

    public void setListItinerary(List<GetItineraryWithUserResponseDTO> listItinerary) {
        this.listItinerary = listItinerary;
    }
}
