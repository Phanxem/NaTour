package com.unina.natour.controllers;

import android.content.Intent;
import android.widget.ListView;

import com.unina.natour.dto.request.ReportRequestDTO;
import com.unina.natour.dto.response.MessageResponseDTO;
import com.unina.natour.dto.response.ReportListResponseDTO;
import com.unina.natour.dto.response.ReportResponseDTO;
import com.unina.natour.dto.response.ItineraryResponseDTO;
import com.unina.natour.models.ElementReportModel;
import com.unina.natour.models.VisualizzaSegnalazioniModel;
import com.unina.natour.models.dao.implementation.ItineraryDAOImpl;
import com.unina.natour.models.dao.implementation.ReportDAOImpl;
import com.unina.natour.models.dao.interfaces.ItineraryDAO;
import com.unina.natour.models.dao.interfaces.ReportDAO;
import com.unina.natour.views.activities.NaTourActivity;
import com.unina.natour.views.activities.VisualizzaSegnalazioniActivity;
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

    public boolean initModel(long itineraryId){

        ItineraryResponseDTO itineraryDTO = itineraryDAO.findById(itineraryId);
        MessageResponseDTO messageResponseDTO = itineraryDTO.getResultMessage();
        if(messageResponseDTO.getCode() != MessageController.SUCCESS_CODE){
            showErrorMessage(messageResponseDTO);
            return false;
        }

        ReportListResponseDTO reportListResponseDTO = reportDAO.findByItineraryId(itineraryId);
        messageResponseDTO = reportListResponseDTO.getResultMessage();
        if(messageResponseDTO.getCode() != MessageController.SUCCESS_CODE){
            showErrorMessage(messageResponseDTO);
            return false;
        }

        boolean result = dtoToModel(itineraryDTO, reportListResponseDTO, visualizzaSegnalazioniModel);
        if(!result){
            //TODO
            showErrorMessage(0);
            return false;
        }
        return true;
    }


    public static void openVisualizzaSegnalazioniActivity(NaTourActivity fromActivity, long itineraryId){
        Intent intent = new Intent(fromActivity, VisualizzaSegnalazioniActivity.class);
        intent.putExtra(EXTRA_ITINERARY_ID, itineraryId);
        fromActivity.startActivity(intent);
    }

    public VisualizzaSegnalazioniModel getModel() {
        return visualizzaSegnalazioniModel;
    }


    public static boolean dtoToModel(ItineraryResponseDTO itineraryDto, ReportListResponseDTO reportListDto, VisualizzaSegnalazioniModel model){
        model.clear();

        List<ReportResponseDTO> reportDtos = reportListDto.getReports();

        List<ElementReportModel> listReportModel = new ArrayList<ElementReportModel>();
        for(ReportResponseDTO elementDto: reportDtos){
            ElementReportModel elementModel = new ElementReportModel();
            boolean result = dtoToModel(elementDto, elementModel);
            if(!result){
                //todo error
                return false;
            }
            listReportModel.add(elementModel);
        }

        model.setItineraryName(itineraryDto.getName());
        model.setItineraryId(itineraryDto.getId());
        model.setReports(listReportModel);

        return true;
    }

    public static boolean dtoToModel(ReportResponseDTO dto, ElementReportModel model){
        model.clear();

        model.setId(dto.getId());
        model.setTitolo(dto.getName());
        model.setDateOfInput(dto.getDateOfInput());

        return true;
    }


}
