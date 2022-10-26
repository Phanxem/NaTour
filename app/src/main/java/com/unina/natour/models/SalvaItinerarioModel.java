package com.unina.natour.models;

import java.util.ArrayList;
import java.util.List;

public class SalvaItinerarioModel extends NaTourModel {

    private List<AddressModel> wayPoints;
    private float defaultDuration;
    private float duration;
    private float distance;

    private Integer difficulty;


    public SalvaItinerarioModel(){
        super();

        this.wayPoints = new ArrayList<AddressModel>();
        this.defaultDuration = -1;
        this.duration = -1;
        this.distance = -1;
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

    public void clear(){
        this.wayPoints.clear();
        this.defaultDuration = -1;
        this.duration = -1;
        this.distance = -1;

        this.difficulty = -1;
    }
}
