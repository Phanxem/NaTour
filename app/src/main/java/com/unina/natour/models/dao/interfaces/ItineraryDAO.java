package com.unina.natour.models.dao.interfaces;

import com.unina.natour.dto.response.GetGpxResponseDTO;
import com.unina.natour.dto.response.GetListItineraryResponseDTO;
import com.unina.natour.dto.response.ResultMessageDTO;
import com.unina.natour.dto.request.SaveItineraryRequestDTO;
import com.unina.natour.dto.response.GetItineraryResponseDTO;
import com.unina.natour.dto.response.composted.GetItineraryWithReportResponseDTO;
import com.unina.natour.dto.response.composted.GetItineraryWithUserResponseDTO;
import com.unina.natour.dto.response.composted.GetListItineraryWithUserResponseDTO;

import io.jenetics.jpx.GPX;

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

    public GetItineraryWithReportResponseDTO getItineraryWithReportById(long idItinerary);






    //ItineraryListResponseDTO getUserItinearyList(String username);
}
