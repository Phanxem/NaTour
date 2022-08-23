package com.unina.natour.controllers;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.unina.natour.controllers.exceptionHandler.ExceptionHandler;
import com.unina.natour.controllers.utils.TimeUtils;
import com.unina.natour.dto.MessageDTO;
import com.unina.natour.dto.OptionalInfoDTO;
import com.unina.natour.models.ImpostaInfoOpzionaliProfiloModel;
import com.unina.natour.models.dao.implementation.UserDAOImpl;
import com.unina.natour.models.dao.interfaces.UserDAO;
import com.unina.natour.views.activities.PersonalizzaAccountInfoOpzionaliActivity;
import com.unina.natour.views.dialogs.MessageDialog;

import java.util.Calendar;

public class ImpostaInfoOpzionaliProfiloController {

    private final static String TAG ="ImpostaInfoOpzionaliProfiloController";

    public final static int REQUEST_CODE = 01;

    private final static long SUCCESS_CODE = 0;

    Activity activity;
    MessageDialog messageDialog;

    //private HomeController homeController;

    //Model---
    ImpostaInfoOpzionaliProfiloModel impostaInfoOpzionaliProfiloModel;

    //---


    UserDAO userDAO;

    public ImpostaInfoOpzionaliProfiloController(Activity activity){
        this.activity = activity;
        this.messageDialog = new MessageDialog(activity);



        this.impostaInfoOpzionaliProfiloModel = new ImpostaInfoOpzionaliProfiloModel();

        this.userDAO = new UserDAOImpl(activity);
    }

    public ImpostaInfoOpzionaliProfiloModel getImpostaInfoOpzionaliProfiloModel() {
        return impostaInfoOpzionaliProfiloModel;
    }

    public void setDateOfBirth(Calendar calendar){

        impostaInfoOpzionaliProfiloModel.setDateOfBirth(calendar);

        /*
        Date date = new Date(calendar.getTimeInMillis());
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String stringDate = dateFormat.format(date);

        this.dateOfBirth = stringDate;
        */
    }

    public void setCountry(String country){
        impostaInfoOpzionaliProfiloModel.setCountry(country);
    }

    public String getCountry(){
        String string = impostaInfoOpzionaliProfiloModel.getCountry();
        return string;
    }

    public void setCity(String city) {
        impostaInfoOpzionaliProfiloModel.setCity(city);
    }

    public String getCity(){
        return impostaInfoOpzionaliProfiloModel.getCity();
    }





    @SuppressLint("LongLogTag")
    public Boolean modificaInfoOpzionali(String address){
        OptionalInfoDTO optionalInfoDTO = new OptionalInfoDTO();

        Calendar dateOfBirth = impostaInfoOpzionaliProfiloModel.getDateOfBirth();
        if(dateOfBirth != null) {
            if(!isValidDate(dateOfBirth)){
                Log.e(TAG, "error");
                ExceptionHandler.handleMessageError(messageDialog,new ServerException());
                return false;
            }

            String stringDate = TimeUtils.toSimpleString(dateOfBirth);

            optionalInfoDTO.setDateOfBirth(stringDate);
        }

        String placeOfResidence = null;

        String country = impostaInfoOpzionaliProfiloModel.getCountry();
        if(country != null && !country.isEmpty()){
            placeOfResidence = country;

            String city = impostaInfoOpzionaliProfiloModel.getCity();
            if(city != null && !city.isEmpty()) {
                placeOfResidence = placeOfResidence + ", " + city;

                if(address != null && !address.isEmpty()) placeOfResidence = placeOfResidence + ", " + address;
            }

            optionalInfoDTO.setPlaceOfResidence(placeOfResidence);
        }


        MessageDTO result = null;
        try {
            result = userDAO.updateOptionalInfo(optionalInfoDTO);
        }
        catch (UnknownException e) {
            e.printStackTrace();
            Log.e(TAG, "ERRORE");
            ExceptionHandler.handleMessageError(messageDialog, e);
        }

        if(result == null) return false;


        if(result.getCode() != SUCCESS_CODE){
            //TODO
            ExceptionHandler.handleMessageError(messageDialog,new ServerException());
            Log.e(TAG, "ERRORE");
            return false;
        }

        Log.i(TAG, "immagine impostata");
        return true;

    }




    public void openPersonalizzaAccountInfoOpzionaliActivity(){
        Intent intent = new Intent(activity, PersonalizzaAccountInfoOpzionaliActivity.class);
        activity.startActivity(intent);
        activity.finish();
    }

    public void openPersonalizzaAccountInfoOpzionaliActivity(boolean isFirstUpdate){
        Intent intent = new Intent(activity, PersonalizzaAccountInfoOpzionaliActivity.class);
        if(isFirstUpdate) intent.putExtra(PersonalizzaAccountInfoOpzionaliActivity.PREV_ACTIVITY,true);
        activity.startActivity(intent);
        activity.finish();
    }














    @SuppressLint("LongLogTag")
    public boolean isValidDate(Calendar date){

        if(date == null){
            return true;
        }

        Calendar currentCalendar = Calendar.getInstance();

        if(!date.before(currentCalendar)){
            //TODO EXCEPTION
            Log.e(TAG, "expetion");
            return false;

        }

        return true;
    }


}
