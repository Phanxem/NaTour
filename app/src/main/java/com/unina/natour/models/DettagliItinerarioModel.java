package com.unina.natour.models;

import android.graphics.Bitmap;

import com.unina.natour.views.observers.Observable;
import com.unina.natour.views.observers.Observer;

import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;
import java.util.List;

public class DettagliItinerarioModel extends NaTourModel {

    private long itineraryId;
    private String name;
    private String description;
    private String duration;
    private String lenght;
    private String difficulty;

    private long idUser;

    private boolean hasBeenReported;

    private List<GeoPoint> wayPoints;
    private List<GeoPoint> routePoints;

    private boolean isNavigationActive;
    private GeoPoint currentLocation;



    public DettagliItinerarioModel(){
        super();

        this.wayPoints = new ArrayList<GeoPoint>();
        this.routePoints = new ArrayList<GeoPoint>();
    }

    public long getItineraryId() {
        return itineraryId;
    }

    public void setItineraryId(long itineraryId) {
        this.itineraryId = itineraryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getLenght() {
        return lenght;
    }

    public void setLenght(String lenght) {
        this.lenght = lenght;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public List<GeoPoint> getWayPoints() {
        return wayPoints;
    }

    public void setWayPoints(List<GeoPoint> wayPoints) {
        this.wayPoints = wayPoints;
    }

    public List<GeoPoint> getRoutePoints() {
        return routePoints;
    }

    public void setRoutePoints(List<GeoPoint> routePoints) {
        this.routePoints = routePoints;
    }

    public boolean hasBeenReported() {
        return hasBeenReported;
    }

    public void setHasBeenReported(boolean hasBeenReported) {
        this.hasBeenReported = hasBeenReported;
    }

    public boolean isNavigationActive() {
        return isNavigationActive;
    }

    public void setNavigationActive(boolean navigationActive) {
        isNavigationActive = navigationActive;
        notifyObservers();
    }

    public GeoPoint getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(GeoPoint currentLocation) {
        this.currentLocation = currentLocation;
        notifyObservers();
    }

    public long getIdUser() {
        return idUser;
    }

    public void setIdUser(long idUser) {
        this.idUser = idUser;
    }


}
