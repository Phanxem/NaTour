package com.unina.natour.models;

import android.location.Address;

import com.unina.natour.views.observers.Observable;
import com.unina.natour.views.observers.Observer;

import java.util.ArrayList;
import java.util.List;

public class RicercaPuntoModel implements Observable {
    private List<AddressModel> resultPoints;

    private List<Observer> observers;


    public RicercaPuntoModel(){
        this.resultPoints = new ArrayList<AddressModel>();

        this.observers = new ArrayList<Observer>();
    }



    public List<AddressModel> getResultPoints() {
        return resultPoints;
    }

    public void setResultPoints(List<AddressModel> resultPoints) {
        this.resultPoints.clear();
        this.resultPoints.addAll(resultPoints);

        notifyObservers();
    }

    public void clear(){
        this.resultPoints.clear();
        notifyObservers();
    }

    public Boolean hasResultPoints(){
        return !resultPoints.isEmpty();
    }



    public List<String> getResultsNames(){
        List<String> strings = new ArrayList<String>();
        for(AddressModel addressModel : resultPoints){
            strings.add(addressModel.getAddressName());
        }
        return strings;
    }


    @Override
    public void registerObserver(Observer observer) {
        if(!observers.contains(observer)) observers.add(observer);
    }

    @Override
    public void undergisterObserver(Observer observer) {
        if(observers.contains(observer)) observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for(Observer observer : observers){
            observer.update();
        }
    }
}
