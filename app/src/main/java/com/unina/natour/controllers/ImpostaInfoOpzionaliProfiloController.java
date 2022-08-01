package com.unina.natour.controllers;

import android.app.Activity;
import android.content.Intent;

import com.unina.natour.dto.OptionalInfoDTO;
import com.unina.natour.views.activities.PersonalizzaAccountInfoOpzionaliActivity;
import com.unina.natour.views.dialogs.MessageDialog;

import java.util.Calendar;
import java.util.Date;

public class ImpostaInfoOpzionaliProfiloController {

    private final static String TAG ="ImpostaInfoOpzionaliProfiloController";

    public final static int REQUEST_CODE = 01;

    Activity activity;
    MessageDialog messageDialog;

    Date date;

    public ImpostaInfoOpzionaliProfiloController(Activity activity){
        this.activity = activity;
        this.messageDialog = new MessageDialog(activity);
        this.date = null;
    }

    public void setDate(Calendar date){
        this.date = date.getTime();
    }


    public void modificaInfoOpzionali(String luogoDiResidenza){
        OptionalInfoDTO optionalInfoDTO = new OptionalInfoDTO();

        if(luogoDiResidenza != null) optionalInfoDTO.setPlaceOfResidence(luogoDiResidenza);
        if(date != null) optionalInfoDTO.setDateOfBirth(date);


        if(optionalInfoDTO.getPlaceOfResidence() != null || optionalInfoDTO.getDateOfBirth() != null){
            //USER DAO
        }

        //open home

    }


    public void openPersonalizzaAccountInfoOpzionaliActivity(){
        Intent intent = new Intent(activity, PersonalizzaAccountInfoOpzionaliActivity.class);
        activity.startActivity(intent);
        activity.finish();
    }
}
