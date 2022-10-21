package com.unina.natour.controllers;

import android.content.Intent;
import android.widget.ListView;

import com.unina.natour.dto.response.ResultMessageDTO;
import com.unina.natour.dto.response.GetListReportResponseDTO;
import com.unina.natour.dto.response.GetReportResponseDTO;
import com.unina.natour.dto.response.GetItineraryResponseDTO;
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

        GetItineraryResponseDTO itineraryDTO = itineraryDAO.getItineraryById(itineraryId);
        ResultMessageDTO resultMessageDTO = itineraryDTO.getResultMessage();
        if(resultMessageDTO.getCode() != ResultMessageController.SUCCESS_CODE){
            showErrorMessage(resultMessageDTO);
            return false;
        }

        GetListReportResponseDTO getListReportResponseDTO = reportDAO.getReportByIdItinerary(itineraryId);
        resultMessageDTO = getListReportResponseDTO.getResultMessage();
        if(resultMessageDTO.getCode() != ResultMessageController.SUCCESS_CODE){
            showErrorMessage(resultMessageDTO);
            return false;
        }

        boolean result = dtoToModel(itineraryDTO, getListReportResponseDTO, visualizzaSegnalazioniModel);
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


    public static boolean dtoToModel(GetItineraryResponseDTO itineraryDto, GetListReportResponseDTO reportListDto, VisualizzaSegnalazioniModel model){
        model.clear();

        List<GetReportResponseDTO> reportDtos = reportListDto.getListReport();

        List<ElementReportModel> listReportModel = new ArrayList<ElementReportModel>();
        for(GetReportResponseDTO elementDto: reportDtos){
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

    public static boolean dtoToModel(GetReportResponseDTO dto, ElementReportModel model){
        model.clear();

        model.setId(dto.getId());
        model.setTitolo(dto.getName());
        model.setDateOfInput(dto.getDateOfInput());

        return true;
    }


}
