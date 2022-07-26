package com.unina.natour.models;

import com.unina.natour.controllers.DettagliSegnalazioneController;

public class DettagliSegnalazioneModel extends NaTourModel{

    private long reportId;
    private String ReportName;
    private String dateOfInput;
    private String descrizione;

    private long userId;

    private long itineraryId;
    private String itineraryName;



    public DettagliSegnalazioneModel(){
        super();
    }

    public long getReportId() {
        return reportId;
    }

    public void setReportId(long reportId) {
        this.reportId = reportId;
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

    public String getReportName() {
        return ReportName;
    }

    public void setReportName(String reportName) {
        ReportName = reportName;
    }

    public String getDateOfInput() {
        return dateOfInput;
    }

    public void setDateOfInput(String dateOfInput) {
        this.dateOfInput = dateOfInput;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public void clear(){
        this.reportId = -1;
        this.ReportName = null;
        this.dateOfInput = null;
        this.descrizione = null;

        this.userId = -1;

        this.itineraryId = -1;
        this.itineraryName = null;
    }
}
