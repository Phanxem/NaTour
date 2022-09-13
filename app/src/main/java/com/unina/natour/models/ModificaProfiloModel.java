package com.unina.natour.models;

import com.unina.natour.controllers.utils.TimeUtils;
import com.unina.natour.dto.UserDTO;
import com.unina.natour.views.observers.Observable;
import com.unina.natour.views.observers.Observer;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ModificaProfiloModel implements Observable {

    private ImpostaImmagineProfiloModel immagineProfiloModel;
    private ImpostaInfoOpzionaliProfiloModel optionalInfoModel;

    private String email;
    private boolean isFacebookAccountLinked;
    private boolean isGoogleAccountLinked;

    private List<Observer> observers;


    public ModificaProfiloModel(ImpostaInfoOpzionaliProfiloModel optionalInfoModel, ImpostaImmagineProfiloModel imageModel){
        this.immagineProfiloModel = imageModel;
        this.optionalInfoModel = optionalInfoModel;

        this.observers = new ArrayList<Observer>();
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



    public void setByDTO(UserDTO userDTO) throws ParseException {

        String stringDateOfBirth = userDTO.getDateOfBirth();
        if(stringDateOfBirth != null && !stringDateOfBirth.isEmpty()) {
            Calendar dateOfBirth = TimeUtils.toCalendar(stringDateOfBirth);
            this.optionalInfoModel.setDateOfBirth(dateOfBirth);
        }

        String completeAddress = userDTO.getPlaceOfResidence();
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

        this.email




    }






    @Override
    public void registerObserver(Observer observer) {
        if(!observers.contains(observer)) observers.add(observer);
        optionalInfoModel.registerObserver(observer);
        immagineProfiloModel.registerObserver(observer);
    }

    @Override
    public void undergisterObserver(Observer observer) {
        if(observers.contains(observer)) observers.remove(observer);
        optionalInfoModel.undergisterObserver(observer);
        immagineProfiloModel.undergisterObserver(observer);
    }

    @Override
    public void notifyObservers() {
        for(Observer observer : observers){
            observer.update();
        }
    }


}
