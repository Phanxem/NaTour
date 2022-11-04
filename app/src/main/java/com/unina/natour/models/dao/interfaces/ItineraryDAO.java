package com.unina.natour.models.dao.interfaces;

import com.unina.natour.dto.response.GetGpxResponseDTO;
import com.unina.natour.dto.response.GetListItineraryResponseDTO;
import com.unina.natour.dto.response.ResultMessageDTO;
import com.unina.natour.dto.request.SaveItineraryRequestDTO;
import com.unina.natour.dto.response.GetItineraryResponseDTO;
import com.unina.natour.dto.response.composted.GetItineraryWithGpxAndUserAndReportResponseDTO;
import com.unina.natour.dto.response.composted.GetListItineraryWithUserResponseDTO;

public interface ItineraryDAO{

    //GETs
    public GetItineraryResponseDTO getItineraryById(long idItinerary);

    public GetGpxResponseDTO getItineraryGpxById(long idItinerary);

    public GetListItineraryResponseDTO getListItineraryByIdUser(long userId, int page);

    public GetListItineraryResponseDTO getListItineraryRandom();

    public GetListItineraryResponseDTO getListItineraryByName(String name, int page);


    //POSTs
    public ResultMessageDTO addItinerary(SaveItineraryRequestDTO saveItineraryRequest);


    //DELETEs
    public ResultMessageDTO deleteItineraryById(long idItinerary);


    //COMPOSITEDs
    public GetListItineraryWithUserResponseDTO getListItineraryWithUserRandom();

    public GetListItineraryWithUserResponseDTO getListItineraryWithUserByName(String name, int page);

    public GetItineraryWithGpxAndUserAndReportResponseDTO getItineraryWithGpxAndUserAndReportById(long idItinerary);







    //ItineraryListResponseDTO getUserItinearyList(String username);
}
