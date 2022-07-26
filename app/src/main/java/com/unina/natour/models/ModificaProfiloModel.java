package com.unina.natour.models;

import com.unina.natour.controllers.utils.TimeUtils;
import com.unina.natour.dto.response.composted.GetUserWithImageResponseDTO;

import java.text.ParseException;
import java.util.Calendar;

public class ModificaProfiloModel extends NaTourModel {

    private ImpostaImmagineProfiloModel immagineProfiloModel;
    private ImpostaInfoOpzionaliProfiloModel optionalInfoModel;

    private String email;





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


    public void setByDTO(GetUserWithImageResponseDTO getUserWithImageResponseDTO) throws ParseException {

        String stringDateOfBirth = getUserWithImageResponseDTO.getDateOfBirth();
        if(stringDateOfBirth != null && !stringDateOfBirth.isEmpty()) {
            Calendar dateOfBirth = TimeUtils.toCalendar(stringDateOfBirth);
            this.optionalInfoModel.setDateOfBirth(dateOfBirth);
        }

        String completeAddress = getUserWithImageResponseDTO.getPlaceOfResidence();
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
    }




}
