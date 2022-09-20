package com.unina.natour.controllers;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.unina.natour.controllers.utils.StringsUtils;
import com.unina.natour.controllers.exceptionHandler.exceptions.ServerException;
import com.unina.natour.controllers.exceptionHandler.exceptions.subAppException.FailureGetUserException;
import com.unina.natour.controllers.exceptionHandler.exceptions.subAppException.FailureUpdateOptionalInfoException;
import com.unina.natour.controllers.exceptionHandler.exceptions.subAppException.InvalidBirthDateException;
import com.unina.natour.controllers.exceptionHandler.exceptions.subAppException.NotCompletedFindAddressException;
import com.unina.natour.controllers.exceptionHandler.exceptions.subAppException.NotCompletedGetUserException;
import com.unina.natour.controllers.utils.TimeUtils;
import com.unina.natour.dto.response.MessageResponseDTO;
import com.unina.natour.dto.request.OptionalInfoRequestDTO;
import com.unina.natour.dto.response.UserResponseDTO;
import com.unina.natour.models.ImpostaInfoOpzionaliProfiloModel;
import com.unina.natour.models.dao.implementation.UserDAOImpl;
import com.unina.natour.models.dao.interfaces.UserDAO;
import com.unina.natour.views.activities.NaTourActivity;
import com.unina.natour.views.activities.PersonalizzaAccountInfoOpzionaliActivity;

import java.io.IOException;
import java.text.ParseException;
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

        boolean result = initModel();
        if(!result){
            //TODO
            showErrorMessage(0);
            getActivity().finish();
            return;
        }
    }

    public boolean initModel(){
        //TODO this.username = Amplify.Auth.getCurrentUser().getUsername();
        String username = "user";

        UserResponseDTO userResponseDTO = userDAO.getUser(username);
        MessageResponseDTO messageResponseDTO = userResponseDTO.getResultMessage();
        if(messageResponseDTO.getCode() != MessageController.SUCCESS_CODE){
            showErrorMessage(messageResponseDTO);
            return false;
        }


        boolean result = dtoToModel(userResponseDTO,impostaInfoOpzionaliProfiloModel);

        if(!result){
            //todo
            showErrorMessage(0);
            return false;
        }

        return true;
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
        OptionalInfoRequestDTO optionalInfoRequestDTO = new OptionalInfoRequestDTO();

        boolean result = modelToDto(impostaInfoOpzionaliProfiloModel, optionalInfoRequestDTO);
        if(!result){
            //TODO
            showErrorMessage(0);
            return false;
        }

        MessageResponseDTO messageResponseDTO = userDAO.updateOptionalInfo(optionalInfoRequestDTO);
        if(messageResponseDTO.getCode() != MessageController.SUCCESS_CODE){
            showErrorMessage(messageResponseDTO);
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


    public static boolean dtoToModel(UserResponseDTO dto, ImpostaInfoOpzionaliProfiloModel model){
        model.clear();

        String stringDateOfBirth = dto.getDateOfBirth();
        Calendar dateOfBirth = null;
        try {
            dateOfBirth = TimeUtils.toCalendar(stringDateOfBirth);
        }
        catch (ParseException e) {
            return false;
        }

        model.setDateOfBirth(dateOfBirth);

        String fullPlaceOfResidence = dto.getPlaceOfResidence();
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

        model.setCountry(country);
        model.setCity(city);
        model.setAddress(address);

        return true;
    }

    public static boolean modelToDto(ImpostaInfoOpzionaliProfiloModel model, OptionalInfoRequestDTO dto){
        dto.setDateOfBirth(null);
        dto.setPlaceOfResidence(null);

        Calendar dateOfBirth = model.getDateOfBirth();

        String stringDate = null;
        if(dateOfBirth != null) stringDate = TimeUtils.toSimpleString(dateOfBirth);

        dto.setDateOfBirth(stringDate);


        String placeOfResidence = null;

        String country = model.getCountry();
        if(country != null && !country.isEmpty()){
            placeOfResidence = country;

            String city = model.getCity();
            if(city != null && !city.isEmpty()) {
                placeOfResidence = placeOfResidence + ", " + city;

                String address = model.getAddress();
                if(address != null && !address.isEmpty()) placeOfResidence = placeOfResidence + ", " + address;
            }

            dto.setPlaceOfResidence(placeOfResidence);
        }

        return true;
    }

}
