package com.unina.natour.controllers;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentActivity;

import com.unina.natour.controllers.exceptionHandler.ExceptionHandler;
import com.unina.natour.controllers.exceptionHandler.exceptions.ServerException;
import com.unina.natour.controllers.exceptionHandler.exceptions.subAppException.FailureUpdateOptionalInfoException;
import com.unina.natour.controllers.exceptionHandler.exceptions.subAppException.InvalidBirthDateException;
import com.unina.natour.controllers.exceptionHandler.exceptions.subAppException.NotCompletedFindAddressException;
import com.unina.natour.controllers.utils.TimeUtils;
import com.unina.natour.dto.MessageDTO;
import com.unina.natour.dto.OptionalInfoDTO;
import com.unina.natour.models.ImpostaInfoOpzionaliProfiloModel;
import com.unina.natour.models.dao.implementation.UserDAOImpl;
import com.unina.natour.models.dao.interfaces.UserDAO;
import com.unina.natour.views.activities.NaTourActivity;
import com.unina.natour.views.activities.PersonalizzaAccountInfoOpzionaliActivity;
import com.unina.natour.views.dialogs.MessageDialog;

import java.io.IOException;
import java.util.Calendar;
import java.util.concurrent.ExecutionException;

@RequiresApi(api = Build.VERSION_CODES.N)
@SuppressLint("LongLogTag")
public class InfoOpzionaliProfiloController extends NaTourController {


    public final static int REQUEST_CODE = 01;

    private final static long SUCCESS_CODE = 0;



    ImpostaInfoOpzionaliProfiloModel impostaInfoOpzionaliProfiloModel;

    UserDAO userDAO;


    public InfoOpzionaliProfiloController(NaTourActivity activity){
        super(activity);

        this.impostaInfoOpzionaliProfiloModel = new ImpostaInfoOpzionaliProfiloModel();

        this.userDAO = new UserDAOImpl(activity);
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




    public void openPersonalizzaAccountInfoOpzionaliActivity(){
        Intent intent = new Intent(getActivity(), PersonalizzaAccountInfoOpzionaliActivity.class);
        getActivity().startActivity(intent);
        getActivity().finish();
    }

    public void openPersonalizzaAccountInfoOpzionaliActivity(boolean isFirstUpdate){
        Intent intent = new Intent(getActivity(), PersonalizzaAccountInfoOpzionaliActivity.class);
        if(isFirstUpdate) intent.putExtra(PersonalizzaAccountInfoOpzionaliActivity.PREV_ACTIVITY,true);
        getActivity().startActivity(intent);
        getActivity().finish();
    }




    public boolean isValidDate(Calendar date){
        if(date == null) return true;

        Calendar currentCalendar = Calendar.getInstance();
        if(!date.before(currentCalendar)) return false;

        return true;
    }


}
