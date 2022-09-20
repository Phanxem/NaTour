package com.unina.natour.models.dao.interfaces;

import com.unina.natour.controllers.exceptionHandler.exceptions.ServerException;
import com.unina.natour.dto.response.ItineraryListResponseDTO;
import com.unina.natour.dto.response.MessageResponseDTO;
import com.unina.natour.dto.request.ItineraryRequestDTO;
import com.unina.natour.dto.response.ItineraryElementResponseDTO;
import com.unina.natour.dto.response.ItineraryResponseDTO;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

public interface ItineraryDAO extends ServerDAO{

    MessageResponseDTO addItinerary(ItineraryRequestDTO itineraryDTO);

    ItineraryListResponseDTO getRandomItineraryList();

    //ItineraryListResponseDTO getUserItinearyList(String username);
    ItineraryListResponseDTO findByUserId(long userId);

    ItineraryResponseDTO findById(long itineraryId);

    MessageResponseDTO deleteById(long itinerayId);

    ItineraryListResponseDTO findByName(String researchString);
}
