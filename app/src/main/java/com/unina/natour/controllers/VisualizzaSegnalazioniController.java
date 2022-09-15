package com.unina.natour.controllers;

import android.content.Intent;
import android.widget.ListView;

import com.unina.natour.dto.ReportDTO;
import com.unina.natour.dto.response.ItineraryResponseDTO;
import com.unina.natour.models.ElementReportModel;
import com.unina.natour.models.ImportaFileGPXModel;
import com.unina.natour.models.VisualizzaSegnalazioniModel;
import com.unina.natour.models.dao.implementation.ItineraryDAOImpl;
import com.unina.natour.models.dao.implementation.ReportDAOImpl;
import com.unina.natour.models.dao.interfaces.AddressDAO;
import com.unina.natour.models.dao.interfaces.ItineraryDAO;
import com.unina.natour.models.dao.interfaces.ReportDAO;
import com.unina.natour.views.activities.NaTourActivity;
import com.unina.natour.views.activities.VisualizzaSegnalazioniActivity;
import com.unina.natour.views.listAdapters.FileGpxListAdapter;
import com.unina.natour.views.listAdapters.ReportListAdapter;

import java.util.ArrayList;
import java.util.List;

public class VisualizzaSegnalazioniController extends NaTourController{

    public final static String EXTRA_ITINERARY_ID = "itineraryId";

    private ReportListAdapter reportListAdapter;

    private VisualizzaSegnalazioniModel visualizzaSegnalazioniModel;

    private ItineraryDAO itineraryDAO;
    private ReportDAO reportDAO;


    public VisualizzaSegnalazioniController(NaTourActivity activity){
        super(activity);

        this.visualizzaSegnalazioniModel = new VisualizzaSegnalazioniModel();

        long itineraryId = getActivity().getIntent().getLongExtra(EXTRA_ITINERARY_ID, -1);
        /*TODO
        if(itineraryId < 0){
            exteption
        }
         */

        this.itineraryDAO = new ItineraryDAOImpl(getActivity());
        this.reportDAO = new ReportDAOImpl();

        initModel(itineraryId);
        this.reportListAdapter = new ReportListAdapter(activity, visualizzaSegnalazioniModel.getReports(),getActivity());



    }

    public void initListViewReports(ListView listView) {
        listView.setAdapter(reportListAdapter);
    }

    public void notifyListAdapter() {
        reportListAdapter.notifyDataSetChanged();
    }

    public void initModel(long itineraryId){

        ItineraryResponseDTO itineraryDTO = itineraryDAO.findById(itineraryId);
        visualizzaSegnalazioniModel.setItineraryName(itineraryDTO.getName());

        List<ReportDTO> reportDTOs = reportDAO.findByItineraryId(itineraryId);

        visualizzaSegnalazioniModel.setItineraryId(itineraryId);

        List<ElementReportModel> reports = visualizzaSegnalazioniModel.getReports();
        reports.clear();
        for(ReportDTO reportDTO : reportDTOs){
            ElementReportModel report = new ElementReportModel();
            report.setId(reportDTO.getId());
            report.setTitolo(reportDTO.getName());
            report.setDateOfInput(reportDTO.getDateOfInput());

            reports.add(report);
        }
    }


    public static void openVisualizzaSegnalazioniActivity(NaTourActivity fromActivity, long itineraryId){
        Intent intent = new Intent(fromActivity, VisualizzaSegnalazioniActivity.class);
        intent.putExtra(EXTRA_ITINERARY_ID, itineraryId);
        fromActivity.startActivity(intent);
    }

    public VisualizzaSegnalazioniModel getModel() {
        return visualizzaSegnalazioniModel;
    }

}
