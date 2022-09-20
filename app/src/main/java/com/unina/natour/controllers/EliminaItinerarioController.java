package com.unina.natour.controllers;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.unina.natour.dto.response.MessageResponseDTO;
import com.unina.natour.models.dao.implementation.ItineraryDAOImpl;
import com.unina.natour.models.dao.interfaces.ItineraryDAO;
import com.unina.natour.views.activities.NaTourActivity;

@RequiresApi(api = Build.VERSION_CODES.N)
public class EliminaItinerarioController extends NaTourController{

    private long itineraryId;

    private ItineraryDAO itineraryDAO;


    public EliminaItinerarioController(NaTourActivity activity, long itineraryId){
        super(activity);
        this.itineraryId = itineraryId;
        this.itineraryDAO = new ItineraryDAOImpl(getActivity());
    }

    public boolean deleteItinerary(){
        MessageResponseDTO messageResponseDTO = itineraryDAO.deleteById(itineraryId);
        if(messageResponseDTO.getCode() != MessageController.SUCCESS_CODE){
            showErrorMessage(messageResponseDTO);
            return false;
        }
        return true;
    }


}
