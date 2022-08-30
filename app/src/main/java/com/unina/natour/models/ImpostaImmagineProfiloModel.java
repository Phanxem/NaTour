package com.unina.natour.models;

import android.graphics.Bitmap;

import com.unina.natour.controllers.ImpostaImmagineProfiloController;
import com.unina.natour.views.observers.Observable;
import com.unina.natour.views.observers.Observer;

import java.util.List;

import kotlin.collections.ArrayDeque;

public class ImpostaImmagineProfiloModel implements Observable {
    private Bitmap profileImage;

    private List<Observer> observers;

    public ImpostaImmagineProfiloModel(){
        this.observers = new ArrayDeque<Observer>();
    }


    public Bitmap getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(Bitmap profileImage) {
        this.profileImage = profileImage;
        notifyObservers();
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
