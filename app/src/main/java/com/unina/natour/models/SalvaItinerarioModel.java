package com.unina.natour.models;

import com.unina.natour.views.observers.Observable;
import com.unina.natour.views.observers.Observer;

import java.util.ArrayList;
import java.util.List;

public class SalvaItinerarioModel implements Observable {

    private List<AddressModel> wayPoints;
    private float defaultDuration;
    private float duration;
    private float distance;

    private Integer difficulty;

    private List<Observer> observers;


    public SalvaItinerarioModel(){
        this.wayPoints = new ArrayList<AddressModel>();
        this.defaultDuration = -1;
        this.duration = -1;
        this.distance = -1;
        this.observers = new ArrayList<Observer>();
    }

    public List<AddressModel> getWayPoints() {
        return wayPoints;
    }

    public void setWayPoints(List<AddressModel> wayPoints) {
        this.wayPoints = wayPoints;
    }

    public float getDefaultDuration() {
        return defaultDuration;
    }

    public void setDefaultDuration(float defaultDuration) {
        this.defaultDuration = defaultDuration;
    }

    public float getDuration() {
        return duration;
    }

    public void setDuration(float duration) {
        this.duration = duration;
        notifyObservers();
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public Integer getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Integer difficulty) {
        this.difficulty = difficulty;
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
