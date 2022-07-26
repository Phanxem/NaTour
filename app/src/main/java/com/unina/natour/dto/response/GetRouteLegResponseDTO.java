package com.unina.natour.dto.response;

import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;
import java.util.List;

public class GetRouteLegResponseDTO {

    private GeoPoint startingPoint;
    private GeoPoint destinationPoint;

    private List<GeoPoint> track;

    private float distance;
    private float duration;


    public GeoPoint getStartingPoint() {
        return startingPoint;
    }

    public void setStartingPoint(GeoPoint startingPoint) {
        this.startingPoint = startingPoint;
    }

    public GeoPoint getDestinationPoint() {
        return destinationPoint;
    }

    public void setDestinationPoint(GeoPoint destinationPoint) {
        this.destinationPoint = destinationPoint;
    }

    public List<GeoPoint> getTrack() {
        return track;
    }

    public void setTrack(List<GeoPoint> track) {
        this.track = track;
    }


    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public float getDuration() {
        return duration;
    }

    public void setDuration(float duration) {
        this.duration = duration;
    }

}
