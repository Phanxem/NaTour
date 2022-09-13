package com.unina.natour.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.unina.natour.views.observers.Observable;
import com.unina.natour.views.observers.Observer;

import java.util.ArrayList;
import java.util.List;

public class NaTourModel implements Observable, Parcelable {

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

    //---

    protected NaTourModel(Parcel in) {
    }

    public static final Creator<NaTourModel> CREATOR = new Creator<NaTourModel>() {
        @Override
        public NaTourModel createFromParcel(Parcel in) {
            return new NaTourModel(in);
        }

        @Override
        public NaTourModel[] newArray(int size) {
            return new NaTourModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }

}

