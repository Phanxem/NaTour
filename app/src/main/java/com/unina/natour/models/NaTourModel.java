package com.unina.natour.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.unina.natour.views.observers.Observable;
import com.unina.natour.views.observers.Observer;

import java.util.ArrayList;
import java.util.List;

public class NaTourModel implements Observable {

    public final String TAG = this.getClass().getSimpleName();


    private List<Observer> observers;

    public NaTourModel(){
        this.observers = new ArrayList<Observer>();
    }


    public List<Observer> getObservers() {
        return observers;
    }

    public void setObservers(List<Observer> observers) {
        this.observers = observers;
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

