package com.unina.natour.controllers;

import android.app.Activity;
import android.content.Intent;

import com.unina.natour.R;
import com.unina.natour.config.CurrentUserInfo;
import com.unina.natour.controllers.utils.StringsUtils;
import com.unina.natour.controllers.utils.TimeUtils;
import com.unina.natour.dto.request.SaveReportRequestDTO;
import com.unina.natour.dto.response.ResultMessageDTO;
import com.unina.natour.models.dao.implementation.ReportDAOImpl;
import com.unina.natour.models.dao.implementation.UserDAOImpl;
import com.unina.natour.models.dao.interfaces.ReportDAO;
import com.unina.natour.models.dao.interfaces.UserDAO;
import com.unina.natour.views.activities.NaTourActivity;
import com.unina.natour.views.activities.SegnalaItinerarioActivity;

import java.util.Calendar;

public class SegnalaItinerarioController extends NaTourController{

    public final static String EXTRA_ITINERARY_ID = "itineraryId";

    private long itineraryId;

    private ReportDAO reportDAO;
    private UserDAO userDAO;

    public SegnalaItinerarioController(NaTourActivity activity,
                                       ResultMessageController resultMessageController,
                                       ReportDAO reportDAO,
                                       UserDAO userDAO,
                                       long itineraryId){
        super(activity, resultMessageController);

        this.itineraryId = itineraryId;

        this.reportDAO = reportDAO;
        this.userDAO = userDAO;
    }

    public SegnalaItinerarioController(NaTourActivity activity){
        super(activity);
        String messageToShow = null;

        Intent intent = activity.getIntent();
        this.itineraryId = intent.getLongExtra(EXTRA_ITINERARY_ID,-1);

        if(itineraryId < 0){
            messageToShow = activity.getString(R.string.Message_UnknownError);
            showErrorMessageAndBack(messageToShow);
            return;
        }

        this.reportDAO = new ReportDAOImpl();
        this.userDAO = new UserDAOImpl(getActivity());

    }

    public boolean inviaSegnalazione(String titolo, String descrizione) {
        Activity activity = getActivity();
        String messageToShow = null;

        if(!CurrentUserInfo.isSignedIn()) return false;

        if(!StringsUtils.areAllFieldsFull(titolo)){
            messageToShow = activity.getString(R.string.Message_EmptyFieldError);
            showErrorMessage(messageToShow);
            return false;
        }

        SaveReportRequestDTO saveReportRequestDTO = new SaveReportRequestDTO();

        saveReportRequestDTO.setName(titolo);
        saveReportRequestDTO.setDescription(descrizione);
        saveReportRequestDTO.setIdItinerary(itineraryId);

        saveReportRequestDTO.setIdUser(CurrentUserInfo.getId());

        Calendar calendar = Calendar.getInstance();
        String stringDateOfInput = TimeUtils.toFullString(calendar);

        saveReportRequestDTO.setDateOfInput(stringDateOfInput);


        ResultMessageDTO resultMessageDTO = reportDAO.addReport(saveReportRequestDTO);
        if(!ResultMessageController.isSuccess(resultMessageDTO)){
            messageToShow = activity.getString(R.string.Message_EmptyFieldError);
            showErrorMessageAndBack(messageToShow);
            return false;
        }
        return true;
    }

    public static void openSegnalaItinerarioActivity(NaTourActivity fromActivity, long itineraryId){
        Intent intent = new Intent(fromActivity, SegnalaItinerarioActivity.class);
        intent.putExtra(EXTRA_ITINERARY_ID, itineraryId);
        fromActivity.startActivity(intent);
    }
}
