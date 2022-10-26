package com.unina.natour.models;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.unina.natour.controllers.PianificaItinerarioController;

import java.util.ArrayList;
import java.util.List;

public class PianificaItinerarioModel extends NaTourModel {

    private AddressModel startingPoint;
    private AddressModel destinationPoint;
    private List<AddressModel> intermediatePoints;

    private AddressModel pointSelectedOnMap;
    private Integer indexPointSelected;

    private List<RouteLegModel> routeLegs;


    public PianificaItinerarioModel(){
        super();

        this.intermediatePoints = new ArrayList<AddressModel>();
        this.routeLegs = new ArrayList<RouteLegModel>();
    }

    public AddressModel getStartingPoint() {
        return startingPoint;
    }

    public void setStartingPoint(AddressModel startingPoint) {
        this.startingPoint = startingPoint;
        notifyObservers();
    }

    public void removeStartingPoint() {
        this.startingPoint = null;
        notifyObservers();
    }

    public boolean hasStartingPoint() {
        if(this.startingPoint != null) return true;
        return false;
    }


    public AddressModel getDestinationPoint() {
        return destinationPoint;
    }

    public void setDestinationPoint(AddressModel destinationPoint) {
        this.destinationPoint = destinationPoint;
        notifyObservers();
    }

    public void removeDestinationPoint() {
        this.destinationPoint = null;
        notifyObservers();
    }

    public boolean hasDestinationPoint() {
        if(this.destinationPoint != null) return true;
        return false;
    }


    public List<AddressModel> getIntermediatePoints() {
        return intermediatePoints;
    }

    public void setIntermediatePoints(List<AddressModel> intermediatePoints) {
        this.intermediatePoints = intermediatePoints;
        notifyObservers();
    }

    public void addIntermediatePoint(AddressModel address) {
        this.intermediatePoints.add(address);
        notifyObservers();
    }

    public AddressModel getIntermediatePoint(int i) {
        if(isValidIndexIntermedaitePoint(i)) return intermediatePoints.get(i);
        return null;
    }

    public void setIntermediatePoint(int i, AddressModel address) {
        if(isValidIndexIntermedaitePoint(i)) {
            this.intermediatePoints.set(i,address);
            notifyObservers();
        }
    }

    public void removeIntermediatePoint(int i) {
        if(isValidIndexIntermedaitePoint(i)){
            this.intermediatePoints.remove(i);
            notifyObservers();
        }
    }

    public int getIntermediatePointsSize() {
        return intermediatePoints.size();
    }

    public boolean hasIntermediatePoints() {
        if(intermediatePoints == null || intermediatePoints.isEmpty()) return false;

        return true;
    }

    public AddressModel getInterestPoint(int i){
        if (i == PianificaItinerarioController.STARTING_POINT_CODE) return this.startingPoint;
        if (i == PianificaItinerarioController.DESTINATION_POINT_CODE) return this.destinationPoint;
        return getIntermediatePoint(i);
    }

    public List<AddressModel> getInterestPoints(){
        List<AddressModel> results = new ArrayList<AddressModel>();
        if(hasStartingPoint()) results.add(startingPoint);
        if(hasIntermediatePoints()){
            for(AddressModel address: intermediatePoints){
                results.add(address);
            }
        }
        if(hasDestinationPoint()) results.add(destinationPoint);

        return results;
    }

    public AddressModel getPointSelectedOnMap() {
        return pointSelectedOnMap;
    }

    public void setPointSelectedOnMap(AddressModel pointSelectedOnMap) {
        this.pointSelectedOnMap = pointSelectedOnMap;
        notifyObservers();
    }

    public void removePointSelectedOnMap() {
        this.pointSelectedOnMap = null;
        notifyObservers();
    }

    public Integer getIndexPointSelected() {
        return indexPointSelected;
    }

    public void setIndexPointSelected(Integer indexPointSelected) {
        this.indexPointSelected = indexPointSelected;
    }

    public void removeIndexPointSelected() {
        this.indexPointSelected = null;
        notifyObservers();
    }

    public List<RouteLegModel> getRouteLegs() {
        return routeLegs;
    }

    public void setRouteLegs(List<RouteLegModel> routeLegs) {
        this.routeLegs = routeLegs;
        notifyObservers();
    }

    public int getRouteLegsSize(){
        return this.routeLegs.size();
    }

    public void clearRouteLegs() {
        this.routeLegs.clear();
        notifyObservers();
    }

    public void removeRouteLeg(int i){
        this.routeLegs.remove(i);
        notifyObservers();
    }



    public float getDistance(){
        if(routeLegs == null || routeLegs.isEmpty()) return 0;

        float distance = 0;
        for(RouteLegModel routeLeg: routeLegs){
            distance = distance + routeLeg.getDistance();
        }

        return distance;
    }

    public float getDuration(){
        if(routeLegs == null || routeLegs.isEmpty()) return 0;

        float duration = 0;
        for(RouteLegModel routeLeg: routeLegs){
            duration = duration + routeLeg.getDuration();
        }

        return duration;
    }

    public void updateInterestPoints(List<AddressModel> addresses){

        if(addresses == null || addresses.isEmpty()) return;

        startingPoint = null;
        destinationPoint = null;
        intermediatePoints.clear();

        this.startingPoint = addresses.get(0);
        if(addresses.size() == 1) return;

        for(int i = 1; i < addresses.size()-1; i++){
            intermediatePoints.add(addresses.get(i));
        }

        this.destinationPoint = addresses.get(addresses.size()-1);

        notifyObservers();
    }

    public void updateRoutes(List<RouteLegModel> routeLegs){
        this.routeLegs.clear();

        for(RouteLegModel routeLeg : routeLegs){
            this.routeLegs.add(routeLeg);
        }

        notifyObservers();
    }


    public boolean isValidIndexPoint(int i) {
        if (i == PianificaItinerarioController.STARTING_POINT_CODE || i == PianificaItinerarioController.DESTINATION_POINT_CODE) return true;
        return isValidIndexIntermedaitePoint(i);
    }

    public boolean isValidIndexIntermedaitePoint(int i){
        if (i >= 0 && i < intermediatePoints.size()) return true;
        return false;
    }

    public boolean isValidIndexRoute(int i){
        if(i >= 0 && i < routeLegs.size()) return true;
        return false;
    }

    public void clear(){
        this.startingPoint.clear();
        this.destinationPoint.clear();
        this.intermediatePoints.clear();

        this.pointSelectedOnMap.clear();
        this.indexPointSelected = -1;

        this.routeLegs.clear();
    }

}
