package com.unina.natour.controllers;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.unina.natour.dto.MessageDTO;
import com.unina.natour.dto.OptionalInfoDTO;
import com.unina.natour.models.dao.classes.UserDAOImpl;
import com.unina.natour.models.dao.interfaces.UserDAO;
import com.unina.natour.views.activities.PersonalizzaAccountInfoOpzionaliActivity;
import com.unina.natour.views.dialogs.MessageDialog;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class ImpostaInfoOpzionaliProfiloController {

    private final static String TAG ="ImpostaInfoOpzionaliProfiloController";

    public final static int REQUEST_CODE = 01;

    private final static long SUCCESS_CODE = 0;

    Activity activity;
    MessageDialog messageDialog;

    private HomeController homeController;

    String date;

    UserDAO userDAO;

    public ImpostaInfoOpzionaliProfiloController(Activity activity){
        this.activity = activity;
        this.messageDialog = new MessageDialog(activity);
        this.date = null;

        homeController = new HomeController(activity);

        this.userDAO = new UserDAOImpl(activity);
    }

    public void setDate(Calendar calendar){

        Date date = new Date(calendar.getTimeInMillis());
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        String stringDate = dateFormat.format(date);

        this.date = stringDate;

    }


    public void modificaInfoOpzionali(String luogoDiResidenza){
        OptionalInfoDTO optionalInfoDTO = new OptionalInfoDTO();

        if(luogoDiResidenza != null) optionalInfoDTO.setPlaceOfResidence(luogoDiResidenza);
        if(date != null) {
            if(!isValidDate(date)) return;
            optionalInfoDTO.setDateOfBirth(date);
        }

        MessageDTO result = userDAO.updateOptionalInfo(optionalInfoDTO);
        if(result == null) {
            Log.e(TAG, "ERRORE");
            return;
        }
        if(result.getCode() == SUCCESS_CODE){
            homeController.openHomeActivity();
            Log.i(TAG, "immagine impostata");
        }
        else Log.e(TAG, "ERRORE");

    }


    public void openPersonalizzaAccountInfoOpzionaliActivity(){
        Intent intent = new Intent(activity, PersonalizzaAccountInfoOpzionaliActivity.class);
        activity.startActivity(intent);
        activity.finish();
    }


    public boolean isValidDate(String stringDate){

        if(stringDate != null){
            DateFormat dateFormat = new SimpleDateFormat();
            Date date = null;
            try { date = dateFormat.parse(stringDate); }
            catch (ParseException e) {
                e.printStackTrace();
                //TODO EXCEPTION
                return false;
            }

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);

            Calendar currentCalendar = Calendar.getInstance();

            if(!calendar.before(currentCalendar)){
                //TODO EXCEPTION
                return false;
            }
        }

        return true;
    }
}
