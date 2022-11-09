package com.unina.natour.controllers;

import android.app.Activity;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.unina.natour.R;
import com.unina.natour.dto.response.ResultMessageDTO;
import com.unina.natour.models.dao.implementation.ItineraryDAOImpl;
import com.unina.natour.models.dao.interfaces.ItineraryDAO;
import com.unina.natour.views.activities.NaTourActivity;

public class EliminaItinerarioController extends NaTourController{

    private long itineraryId;

    private ItineraryDAO itineraryDAO;

    public EliminaItinerarioController(NaTourActivity activity,
                                       ResultMessageController resultMessageController,
                                       ItineraryDAO itineraryDAO,
                                       long itineraryId){
        super(activity, resultMessageController);
        this.itineraryId = itineraryId;
        this.itineraryDAO = itineraryDAO;
    }

    public EliminaItinerarioController(NaTourActivity activity, long itineraryId){
        super(activity);
        this.itineraryId = itineraryId;
        this.itineraryDAO = new ItineraryDAOImpl(getActivity());
    }

    public boolean deleteItinerary(){
        Activity activity = getActivity();
        String messageToShow = null;

        ResultMessageDTO resultMessageDTO = itineraryDAO.deleteItineraryById(itineraryId);
        if(!ResultMessageController.isSuccess(resultMessageDTO)){
            messageToShow = activity.getString(R.string.Message_UnknownError);
            showErrorMessageAndBack(messageToShow);
            return false;
        }

        return true;
    }


}
