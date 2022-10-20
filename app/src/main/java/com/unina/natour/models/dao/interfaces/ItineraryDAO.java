package com.unina.natour.models.dao.interfaces;

import com.unina.natour.dto.response.GetListItineraryResponseDTO;
import com.unina.natour.dto.response.ResultMessageDTO;
import com.unina.natour.dto.request.SaveItineraryRequestDTO;
import com.unina.natour.dto.response.GetItineraryResponseDTO;

import io.jenetics.jpx.GPX;

public interface ItineraryDAO{

    //GETs
    public GetItineraryResponseDTO getItineraryById(long idItinerary);

    public GPX getItineraryGpxById(long idItinerary);

    public GetListItineraryResponseDTO getListItineraryByIdUser(long userId, int page);

    public GetListItineraryResponseDTO getListItineraryRandom();

    public GetListItineraryResponseDTO getListItineraryByName(String name);


    //POSTs
    public ResultMessageDTO addItinerary(SaveItineraryRequestDTO saveItineraryRequest);


    //DELETEs
    public ResultMessageDTO deleteItineraryById(long idItinerary);





    //ItineraryListResponseDTO getUserItinearyList(String username);
}
