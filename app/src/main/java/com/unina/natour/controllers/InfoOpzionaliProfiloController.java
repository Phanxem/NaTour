package com.unina.natour.controllers;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentActivity;

import com.unina.natour.controllers.exceptionHandler.ExceptionHandler;
import com.unina.natour.controllers.exceptionHandler.exceptions.ServerException;
import com.unina.natour.controllers.exceptionHandler.exceptions.subAppException.FailureGetUserException;
import com.unina.natour.controllers.exceptionHandler.exceptions.subAppException.FailureGetUserProfileImageException;
import com.unina.natour.controllers.exceptionHandler.exceptions.subAppException.FailureUpdateOptionalInfoException;
import com.unina.natour.controllers.exceptionHandler.exceptions.subAppException.InvalidBirthDateException;
import com.unina.natour.controllers.exceptionHandler.exceptions.subAppException.NotCompletedFindAddressException;
import com.unina.natour.controllers.exceptionHandler.exceptions.subAppException.NotCompletedGetUserException;
import com.unina.natour.controllers.exceptionHandler.exceptions.subAppException.NotCompletedGetUserProfileImageException;
import com.unina.natour.controllers.utils.TimeUtils;
import com.unina.natour.dto.MessageDTO;
import com.unina.natour.dto.OptionalInfoDTO;
import com.unina.natour.dto.UserDTO;
import com.unina.natour.models.ImpostaInfoOpzionaliProfiloModel;
import com.unina.natour.models.dao.implementation.UserDAOImpl;
import com.unina.natour.models.dao.interfaces.UserDAO;
import com.unina.natour.views.activities.NaTourActivity;
import com.unina.natour.views.activities.PersonalizzaAccountInfoOpzionaliActivity;
import com.unina.natour.views.dialogs.MessageDialog;

import java.io.IOException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.concurrent.ExecutionException;

@RequiresApi(api = Build.VERSION_CODES.N)
@SuppressLint("LongLogTag")
public class InfoOpzionaliProfiloController extends NaTourController {


    public final static int REQUEST_CODE = 01;

    private final static long SUCCESS_CODE = 0;
    private static final String EXTRA_FIRST_UPDATE = "FIRST_UPDATE";


    ImpostaInfoOpzionaliProfiloModel impostaInfoOpzionaliProfiloModel;

    private boolean isFirstUpdate;

    UserDAO userDAO;


    public InfoOpzionaliProfiloController(NaTourActivity activity){
        super(activity);

        this.impostaInfoOpzionaliProfiloModel = new ImpostaInfoOpzionaliProfiloModel();

        this.isFirstUpdate = getActivity().getIntent().getBooleanExtra(EXTRA_FIRST_UPDATE,false);

        this.userDAO = new UserDAOImpl(activity);

        initModel();
    }

    public void initModel(){
        //TODO this.username = Amplify.Auth.getCurrentUser().getUsername();
        String username = "user";

        UserDTO userDTO;
        try {
            userDTO = userDAO.getUser(username);
        } catch (ExecutionException | InterruptedException e) {
            NotCompletedGetUserException exception = new NotCompletedGetUserException(e);
            ExceptionHandler.handleMessageError(getMessageDialog(), exception);
            return;
        } catch (ServerException e) {
            ExceptionHandler.handleMessageError(getMessageDialog(), e);
            return;
        } catch (IOException e) {
            FailureGetUserException exception = new FailureGetUserException(e);
            ExceptionHandler.handleMessageError(getMessageDialog(), exception);
            return;
        }

        String stringDateOfBirth = userDTO.getDateOfBirth();
        Calendar dateOfBirth = null;
        try {
            dateOfBirth = TimeUtils.toCalendar(stringDateOfBirth);
        }
        catch (ParseException e) {
            //TODO EXCEPTION
        }

        impostaInfoOpzionaliProfiloModel.setDateOfBirth(dateOfBirth);

        String fullPlaceOfResidence = userDTO.getPlaceOfResidence();
        String[] placeOfResidence = fullPlaceOfResidence.split(", ");

        String country = "";
        String city = "";
        String address = "";

        if(placeOfResidence.length>0){
            country = placeOfResidence[0];
        }
        if(placeOfResidence.length>1){
            city = placeOfResidence[1];
        }
        for(int i = 2; i < placeOfResidence.length; i++){
            address = address + placeOfResidence[i];
        }

        impostaInfoOpzionaliProfiloModel.setCountry(country);
        impostaInfoOpzionaliProfiloModel.setCity(city);
        impostaInfoOpzionaliProfiloModel.setAddress(address);
    }

    public ImpostaInfoOpzionaliProfiloModel getImpostaInfoOpzionaliProfiloModel() {
        return impostaInfoOpzionaliProfiloModel;
    }

    public void setDateOfBirth(Calendar calendar){
        impostaInfoOpzionaliProfiloModel.setDateOfBirth(calendar);
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







    public Boolean modificaInfoOpzionali(String address){
        OptionalInfoDTO optionalInfoDTO = new OptionalInfoDTO();

        Calendar dateOfBirth = impostaInfoOpzionaliProfiloModel.getDateOfBirth();

        if(!isValidDate(dateOfBirth)){
            InvalidBirthDateException exception = new InvalidBirthDateException();
            ExceptionHandler.handleMessageError(getMessageDialog(), exception);
            return false;
        }

        String stringDate = null;
        if(dateOfBirth != null) stringDate = TimeUtils.toSimpleString(dateOfBirth);

        optionalInfoDTO.setDateOfBirth(stringDate);


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
        catch (ExecutionException | InterruptedException e) {
            NotCompletedFindAddressException exception = new NotCompletedFindAddressException(e);
            ExceptionHandler.handleMessageError(getMessageDialog(),exception);
            return false;
        }
        catch (IOException e) {
            FailureUpdateOptionalInfoException exception = new FailureUpdateOptionalInfoException(e);
            ExceptionHandler.handleMessageError(getMessageDialog(),exception);
            return false;
        }
        catch (ServerException e) {
            ExceptionHandler.handleMessageError(getMessageDialog(),e);
            return false;
        }
        if(result == null || result.getCode() != SUCCESS_CODE){
            FailureUpdateOptionalInfoException exception = new FailureUpdateOptionalInfoException();
            ExceptionHandler.handleMessageError(getMessageDialog(),exception);
            return false;
        }

        return true;
    }




    public static void openPersonalizzaAccountInfoOpzionaliActivity(NaTourActivity fromActivity){
        Intent intent = new Intent(fromActivity, PersonalizzaAccountInfoOpzionaliActivity.class);
        fromActivity.startActivity(intent);
        fromActivity.finish();
    }

    public static void openPersonalizzaAccountInfoOpzionaliActivity(NaTourActivity fromActivity, boolean isFirstUpdate){
        if(!isFirstUpdate){
            openPersonalizzaAccountInfoOpzionaliActivity(fromActivity);
            return;
        }
        Intent intent = new Intent(fromActivity, PersonalizzaAccountInfoOpzionaliActivity.class);
        intent.putExtra(EXTRA_FIRST_UPDATE,true);
        fromActivity.startActivity(intent);
        fromActivity.finish();
    }


    public boolean isFirstUpdate() {
        return isFirstUpdate;
    }

    public boolean isValidDate(Calendar date){
        if(date == null) return true;

        Calendar currentCalendar = Calendar.getInstance();
        if(!date.before(currentCalendar)) return false;

        return true;
    }



}
