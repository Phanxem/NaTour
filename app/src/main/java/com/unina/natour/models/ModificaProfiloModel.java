package com.unina.natour.models;

import com.unina.natour.controllers.utils.TimeUtils;
import com.unina.natour.dto.response.UserResponseDTO;

import java.text.ParseException;
import java.util.Calendar;

public class ModificaProfiloModel extends NaTourModel {

    private ImpostaImmagineProfiloModel immagineProfiloModel;
    private ImpostaInfoOpzionaliProfiloModel optionalInfoModel;

    private String email;
    private boolean isFacebookAccountLinked;
    private boolean isGoogleAccountLinked;




    public ModificaProfiloModel(ImpostaInfoOpzionaliProfiloModel optionalInfoModel, ImpostaImmagineProfiloModel imageModel){
        super();

        this.immagineProfiloModel = imageModel;
        this.optionalInfoModel = optionalInfoModel;
    }


    public ImpostaImmagineProfiloModel getImmagineProfiloModel() {
        return immagineProfiloModel;
    }

    public void setImmagineProfiloModel(ImpostaImmagineProfiloModel immagineProfiloModel) {
        this.immagineProfiloModel = immagineProfiloModel;
    }

    public ImpostaInfoOpzionaliProfiloModel getOptionalInfoModel() {
        return optionalInfoModel;
    }

    public void setOptionalInfoModel(ImpostaInfoOpzionaliProfiloModel optionalInfoModel) {
        this.optionalInfoModel = optionalInfoModel;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isFacebookAccountLinked() {
        return isFacebookAccountLinked;
    }

    public void setFacebookAccountLinked(boolean facebookAccountLinked) {
        isFacebookAccountLinked = facebookAccountLinked;
    }

    public boolean isGoogleAccountLinked() {
        return isGoogleAccountLinked;
    }

    public void setGoogleAccountLinked(boolean googleAccountLinked) {
        isGoogleAccountLinked = googleAccountLinked;
    }



    public void setByDTO(UserResponseDTO userResponseDTO) throws ParseException {

        String stringDateOfBirth = userResponseDTO.getDateOfBirth();
        if(stringDateOfBirth != null && !stringDateOfBirth.isEmpty()) {
            Calendar dateOfBirth = TimeUtils.toCalendar(stringDateOfBirth);
            this.optionalInfoModel.setDateOfBirth(dateOfBirth);
        }

        String completeAddress = userResponseDTO.getPlaceOfResidence();
        if(completeAddress != null && !completeAddress.isEmpty()){
            String[] strings = completeAddress.split(",");
            this.optionalInfoModel.setCountry(strings[0]);
            if(strings.length > 1){
                this.optionalInfoModel.setCity(strings[1]);
            }
            if(strings.length > 2){
                String address = new String();
                for(int i = 2; i < strings.length; i++)address = address + strings[i];
                this.optionalInfoModel.setAddress(address);
            }
        }

        //TODO userDTO facebook and google

    }



    public void clear(){
        this.immagineProfiloModel.clear();
        this.optionalInfoModel.clear();

        this.email = null;
        this.isFacebookAccountLinked = false;
        this.isGoogleAccountLinked = false;
    }




}
