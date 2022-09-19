package com.unina.natour.controllers;

import android.content.Intent;

import com.amplifyframework.core.Amplify;
import com.unina.natour.controllers.utils.StringsUtils;
import com.unina.natour.controllers.exceptionHandler.exceptions.ServerException;
import com.unina.natour.controllers.exceptionHandler.exceptions.subAppException.FailureGetUserException;
import com.unina.natour.controllers.exceptionHandler.exceptions.subAppException.NotCompletedGetUserException;
import com.unina.natour.controllers.utils.TimeUtils;
import com.unina.natour.dto.request.ReportRequestDTO;
import com.unina.natour.dto.response.MessageResponseDTO;
import com.unina.natour.dto.response.ReportResponseDTO;
import com.unina.natour.dto.response.UserResponseDTO;
import com.unina.natour.models.dao.implementation.ReportDAOImpl;
import com.unina.natour.models.dao.implementation.UserDAOImpl;
import com.unina.natour.models.dao.interfaces.ReportDAO;
import com.unina.natour.models.dao.interfaces.UserDAO;
import com.unina.natour.views.activities.NaTourActivity;
import com.unina.natour.views.activities.SegnalaItinerarioActivity;

import java.io.IOException;
import java.util.Calendar;
import java.util.concurrent.ExecutionException;

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

        ReportRequestDTO reportRequestDTO = new ReportRequestDTO();

        reportRequestDTO.setName(titolo);
        reportRequestDTO.setDescription(descrizione);
        reportRequestDTO.setId_itinerary(itineraryId);

        String username = Amplify.Auth.getCurrentUser().getUsername();
        UserResponseDTO userResponseDTO = userDAO.getUser(username);
        MessageResponseDTO messageResponseDTO = userResponseDTO.getResultMessage();
        if(messageResponseDTO.getCode() != MessageController.SUCCESS_CODE){
            showErrorMessage(messageResponseDTO);
            return false;
        }

        reportRequestDTO.setId_user(userResponseDTO.getId());

        Calendar calendar = Calendar.getInstance();
        String stringDateOfInput = TimeUtils.toFullString(calendar);

        reportRequestDTO.setDateOfInput(stringDateOfInput);


        messageResponseDTO = reportDAO.addReport(reportRequestDTO);
        if(messageResponseDTO.getCode() != MessageController.SUCCESS_CODE){
            showErrorMessage(messageResponseDTO);
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
