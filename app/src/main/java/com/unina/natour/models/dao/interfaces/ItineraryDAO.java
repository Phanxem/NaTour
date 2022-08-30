package com.unina.natour.models.dao.interfaces;

import com.unina.natour.controllers.exceptionHandler.exceptions.ServerException;
import com.unina.natour.dto.MessageDTO;
import com.unina.natour.dto.request.ItineraryRequestDTO;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public interface ItineraryDAO extends ServerDAO{

    MessageDTO addItinerary(ItineraryRequestDTO itineraryDTO) throws IOException, ExecutionException, InterruptedException, ServerException;

}
