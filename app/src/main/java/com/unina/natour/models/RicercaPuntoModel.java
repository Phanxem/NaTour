package com.unina.natour.models;

import java.util.ArrayList;
import java.util.List;

public class RicercaPuntoModel extends  NaTourModel {
    private List<AddressModel> resultPoints;

    public RicercaPuntoModel(){
        super();
        this.resultPoints = new ArrayList<AddressModel>();
    }

    public List<AddressModel> getResultPoints() {
        return resultPoints;
    }

    public void setResultPoints(List<AddressModel> resultPoints) {
        this.resultPoints.clear();
        this.resultPoints.addAll(resultPoints);

        notifyObservers();
    }

    public void clearAndNotify(){
        this.resultPoints.clear();
        notifyObservers();
    }

    public Boolean hasResultPoints(){
        return !resultPoints.isEmpty();
    }



    public List<String> getResultsNames(){
        List<String> strings = new ArrayList<String>();
        for(AddressModel addressModel : resultPoints){
            strings.add(addressModel.getAddressName());
        }
        return strings;
    }

    public void clear(){
        this.resultPoints.clear();
    }
}
