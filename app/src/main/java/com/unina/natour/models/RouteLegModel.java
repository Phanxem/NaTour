package com.unina.natour.models;

import android.location.Address;
import android.os.Parcel;
import android.os.Parcelable;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.Polyline;

import java.util.ArrayList;
import java.util.List;

public class RouteLegModel implements Parcelable {

    private GeoPoint startingPoint;
    private GeoPoint destinationPoint;

    private List<GeoPoint> track;

    private float distance;
    private float duration;

    public RouteLegModel(){
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







    protected RouteLegModel(Parcel in) {
        startingPoint = in.readParcelable(GeoPoint.class.getClassLoader());
        destinationPoint = in.readParcelable(GeoPoint.class.getClassLoader());
        track = in.createTypedArrayList(GeoPoint.CREATOR);
        distance = in.readFloat();
        duration = in.readFloat();
    }

    public static final Creator<RouteLegModel> CREATOR = new Creator<RouteLegModel>() {
        @Override
        public RouteLegModel createFromParcel(Parcel in) {
            return new RouteLegModel(in);
        }

        @Override
        public RouteLegModel[] newArray(int size) {
            return new RouteLegModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(startingPoint, flags);
        dest.writeParcelable(destinationPoint, flags);
        dest.writeTypedList(track);
        dest.writeFloat(distance);
        dest.writeFloat(duration);
    }
}
