package com.unina.natour.controllers;

import com.unina.natour.models.dao.implementation.ReportDAOImpl;
import com.unina.natour.models.dao.interfaces.ReportDAO;
import com.unina.natour.views.activities.NaTourActivity;

public class RimuoviSegnalazioneController extends NaTourController{

    private long reportId;

    private ReportDAO reportDAO;


    public RimuoviSegnalazioneController(NaTourActivity activity, long reportId){
        super(activity);
        this.reportId = reportId;
        this.reportDAO = new ReportDAOImpl();
    }

    public void deleteReport(){
        reportDAO.deleteReportById(reportId);
    }
}
