package com.unina.natour.controllers;

import android.content.Intent;

import com.amplifyframework.core.Amplify;
import com.unina.natour.controllers.utils.StringsUtils;
import com.unina.natour.controllers.utils.TimeUtils;
import com.unina.natour.dto.request.SaveReportRequestDTO;
import com.unina.natour.dto.response.ResultMessageDTO;
import com.unina.natour.dto.response.composted.GetUserWithImageResponseDTO;
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


    public SegnalaItinerarioController(NaTourActivity activity){
        super(activity);

        Intent intent = activity.getIntent();
        this.itineraryId = intent.getLongExtra(EXTRA_ITINERARY_ID,-1);
/*TODO
        if(itineraryId < 0){
            activity.finish()
            return;
        }
*/
        this.reportDAO = new ReportDAOImpl();
        this.userDAO = new UserDAOImpl(getActivity());

    }

    public boolean inviaSegnalazione(String titolo, String descrizione) {

        if(StringsUtils.areAllFieldsFull(titolo)){
            /*TODO def. exception
            EmptyFieldActivationAccountCodeException exception = new EmptyFieldActivationAccountCodeException();
            ExceptionHandler.handleMessageError(getMessageDialog(),exception);
            */
            return false;
        }

        SaveReportRequestDTO saveReportRequestDTO = new SaveReportRequestDTO();

        saveReportRequestDTO.setName(titolo);
        saveReportRequestDTO.setDescription(descrizione);
        saveReportRequestDTO.setId_itinerary(itineraryId);

        String username = Amplify.Auth.getCurrentUser().getUsername();
        GetUserWithImageResponseDTO getUserWithImageResponseDTO = userDAO.getUser(username);
        ResultMessageDTO resultMessageDTO = getUserWithImageResponseDTO.getResultMessage();
        if(resultMessageDTO.getCode() != MessageController.SUCCESS_CODE){
            showErrorMessage(resultMessageDTO);
            return false;
        }

        saveReportRequestDTO.setId_user(getUserWithImageResponseDTO.getId());

        Calendar calendar = Calendar.getInstance();
        String stringDateOfInput = TimeUtils.toFullString(calendar);

        saveReportRequestDTO.setDateOfInput(stringDateOfInput);


        resultMessageDTO = reportDAO.addReport(saveReportRequestDTO);
        if(resultMessageDTO.getCode() != MessageController.SUCCESS_CODE){
            showErrorMessage(resultMessageDTO);
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
