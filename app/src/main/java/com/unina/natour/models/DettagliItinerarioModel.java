package com.unina.natour.models;

import android.graphics.Bitmap;

import org.osmdroid.util.GeoPoint;

import java.util.List;

public class DettagliItinerarioModel {

    private long itineraryId;
    private String name;
    private String description;
    private String duration;
    private String lenght;
    private String difficulty;

    private boolean hasBeenReported;

    private List<GeoPoint> wayPoints;
    private List<GeoPoint> routePoints;

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
}
