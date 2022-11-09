package com.unina.natour.controllers;

import com.unina.natour.config.CurrentUserInfo;
import com.unina.natour.models.DettagliSegnalazioneModel;
import com.unina.natour.models.dao.implementation.ReportDAOImpl;
import com.unina.natour.models.dao.interfaces.ReportDAO;
import com.unina.natour.views.activities.NaTourActivity;

public class RimuoviSegnalazioneController extends NaTourController{

    private DettagliSegnalazioneModel dettagliSegnalazioneModel;

    private ReportDAO reportDAO;

    public RimuoviSegnalazioneController(NaTourActivity activity,
                                         ResultMessageController resultMessageController,
                                         DettagliSegnalazioneModel reportModel,
                                         ReportDAO reportDAO){
        super(activity, resultMessageController);
        this.dettagliSegnalazioneModel = reportModel;
        this.reportDAO = reportDAO;
    }

    public RimuoviSegnalazioneController(NaTourActivity activity, DettagliSegnalazioneModel reportModel){
        super(activity);
        this.dettagliSegnalazioneModel = reportModel;
        this.reportDAO = new ReportDAOImpl();
    }

    public void deleteReport(){

        if(!CurrentUserInfo.isSignedIn()) return;

        long idUserReport = dettagliSegnalazioneModel.getUserId();

        if(CurrentUserInfo.getId() == idUserReport){
            reportDAO.deleteReportById(dettagliSegnalazioneModel.getReportId());
        }

    }
}
