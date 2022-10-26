package com.unina.natour.models;

import java.util.Calendar;

public class ImpostaInfoOpzionaliProfiloModel extends NaTourModel {

    private Calendar dateOfBirth;

    private String country;
    private String city;
    private String address;

    public ImpostaInfoOpzionaliProfiloModel(){
        super();
    }

    public Calendar getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Calendar dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
        notifyObservers();
    }

    public String getCountry() {

        return this.country;
    }

    public void setCountry(String country) {
        if(this.country != null && !this.country.isEmpty()){
            if (!country.equals(this.country)) this.city = null;
        }

        this.country = country;
        notifyObservers();
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
        notifyObservers();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    public void clear(){
        this.dateOfBirth = null;
        this.country = null;
        this.city = null;
        this.address = null;
    }
}
