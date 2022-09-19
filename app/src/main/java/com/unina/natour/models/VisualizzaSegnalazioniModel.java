package com.unina.natour.models;

import com.unina.natour.views.activities.VisualizzaSegnalazioniActivity;

import java.util.ArrayList;
import java.util.List;

public class VisualizzaSegnalazioniModel extends NaTourModel{

    private long itineraryId;
    private String itineraryName;
    private List<ElementReportModel> reports;

    public VisualizzaSegnalazioniModel(){
        super();
        this.reports = new ArrayList<ElementReportModel> ();
    }

    public long getItineraryId() {
        return itineraryId;
    }

    public void setItineraryId(long itineraryId) {
        this.itineraryId = itineraryId;
    }

    public String getItineraryName() {
        return itineraryName;
    }

    public void setItineraryName(String itineraryName) {
        this.itineraryName = itineraryName;
    }

    public List<ElementReportModel> getReports() {
        return reports;
    }

    public void setReports(List<ElementReportModel> reports) {
        this.reports = reports;
    }


    public void clear(){
        this.itineraryId = -1;
        this.itineraryName = null;
        this.reports.clear();
    }
}
