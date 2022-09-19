package com.unina.natour.models;

import android.location.Address;
import android.os.Parcel;
import android.os.Parcelable;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.Polyline;

import java.util.ArrayList;
import java.util.List;

public class RouteLegModel extends NaTourModel {

    private GeoPoint startingPoint;
    private GeoPoint destinationPoint;

    private List<GeoPoint> track;

    private float distance;
    private float duration;

    public RouteLegModel() {
        super();

        this.track = new ArrayList<GeoPoint>();
    }

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

    public void clear(){
        this.startingPoint = null;
        this.destinationPoint = null;

        this.track.clear();

        this.distance = -1;
        this.duration = -1;

    }

}
