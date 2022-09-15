package com.unina.natour.controllers;

import android.content.Intent;

import com.amplifyframework.core.Amplify;
import com.unina.natour.controllers.exceptionHandler.ExceptionHandler;
import com.unina.natour.controllers.exceptionHandler.exceptions.ServerException;
import com.unina.natour.controllers.exceptionHandler.exceptions.subAppException.EmptyFieldActivationAccountCodeException;
import com.unina.natour.controllers.exceptionHandler.exceptions.subAppException.FailureGetUserException;
import com.unina.natour.controllers.exceptionHandler.exceptions.subAppException.NotCompletedGetUserException;
import com.unina.natour.controllers.utils.TimeUtils;
import com.unina.natour.dto.ReportDTO;
import com.unina.natour.dto.UserDTO;
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

        if(ExceptionHandler.areAllFieldsFull(titolo)){
            /*TODO def. exception
            EmptyFieldActivationAccountCodeException exception = new EmptyFieldActivationAccountCodeException();
            ExceptionHandler.handleMessageError(getMessageDialog(),exception);
            */
            return false;
        }

        ReportDTO reportDTO = new ReportDTO();

        reportDTO.setName(titolo);
        reportDTO.setDescription(descrizione);
        reportDTO.setId_itinerary(itineraryId);

        String username = Amplify.Auth.getCurrentUser().getUsername();
        UserDTO userDTO;
        try {
            userDTO = userDAO.getUser(username);
        } catch (ExecutionException | InterruptedException e) {
            NotCompletedGetUserException exception = new NotCompletedGetUserException(e);
            ExceptionHandler.handleMessageError(getMessageDialog(), exception);
            return false;
        } catch (ServerException e) {
            ExceptionHandler.handleMessageError(getMessageDialog(), e);
            return false;
        } catch (IOException e) {
            FailureGetUserException exception = new FailureGetUserException(e);
            ExceptionHandler.handleMessageError(getMessageDialog(), exception);
            return false;
        }

        reportDTO.setId_user(userDTO.getId());

        Calendar calendar = Calendar.getInstance();
        String stringDateOfInput = TimeUtils.toFullString(calendar);

        reportDTO.setDateOfInput(stringDateOfInput);

        reportDAO.addReport(reportDTO);
        return true;
    }

    public static void openSegnalaItinerarioActivity(NaTourActivity fromActivity, long itineraryId){
        Intent intent = new Intent(fromActivity, SegnalaItinerarioActivity.class);
        intent.putExtra(EXTRA_ITINERARY_ID, itineraryId);
        fromActivity.startActivity(intent);
    }
}
