package com.unina.natour.controllers;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.ListView;

import com.unina.natour.R;
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

    public VisualizzaSegnalazioniController(NaTourActivity activity,
                                            ResultMessageController resultMessageController,
                                            ReportListAdapter reportListAdapter,
                                             VisualizzaSegnalazioniModel visualizzaSegnalazioniModel,
                                            ItineraryDAO itineraryDAO,
                                            ReportDAO reportDAO){
        super(activity, resultMessageController);

        this.visualizzaSegnalazioniModel = visualizzaSegnalazioniModel;
        this.itineraryDAO = itineraryDAO;
        this.reportDAO = reportDAO;


        this.reportListAdapter = reportListAdapter;
    }

    public VisualizzaSegnalazioniController(NaTourActivity activity){
        super(activity);
        String messageToShow = null;

        long itineraryId = getActivity().getIntent().getLongExtra(EXTRA_ITINERARY_ID, -1);
        if(itineraryId < 0){
            messageToShow = activity.getString(R.string.Message_UnknownError);
            showErrorMessageAndBack(messageToShow);
            return;
        }

        this.visualizzaSegnalazioniModel = new VisualizzaSegnalazioniModel();

        this.itineraryDAO = new ItineraryDAOImpl(getActivity());
        this.reportDAO = new ReportDAOImpl();


        boolean result = initModel(itineraryId);
        if(!result){
            return;
        }

        this.reportListAdapter = new ReportListAdapter(activity, visualizzaSegnalazioniModel.getReports(),getActivity());
    }

    public void initListViewReports(ListView listView) {
        listView.setAdapter(reportListAdapter);
    }

    public void notifyListAdapter() {
        reportListAdapter.notifyDataSetChanged();
    }

    public boolean initModel(long itineraryId){
        Activity activity = getActivity();
        String messageToShow = null;

        GetItineraryResponseDTO itineraryDTO = itineraryDAO.getItineraryById(itineraryId);
        if(!ResultMessageController.isSuccess(itineraryDTO.getResultMessage())){
            messageToShow = activity.getString(R.string.Message_UnknownError);
            showErrorMessageAndBack(messageToShow);
            return false;
        }

        GetListReportResponseDTO getListReportResponseDTO = reportDAO.getReportByIdItinerary(itineraryId);
        if(!ResultMessageController.isSuccess(getListReportResponseDTO.getResultMessage())){
            messageToShow = activity.getString(R.string.Message_UnknownError);
            showErrorMessageAndBack(messageToShow);
            return false;
        }

        boolean result = dtoToModel(itineraryDTO, getListReportResponseDTO, visualizzaSegnalazioniModel);
        if(!result){
            messageToShow = activity.getString(R.string.Message_UnknownError);
            showErrorMessageAndBack(messageToShow);
            return false;
        }

        Log.e(TAG, "itineraryId: " + visualizzaSegnalazioniModel.getItineraryId());
        Log.e(TAG, "itineraryName: " + visualizzaSegnalazioniModel.getItineraryName());

        for(ElementReportModel report : visualizzaSegnalazioniModel.getReports()){
            Log.e(TAG, "reportId: " + report.getId() + " | reportName: " + report.getTitolo() + " | date: " + report.getDateOfInput());
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
                Log.e("TAG", "dtoToModel Failure");
                return false;
            }
            listReportModel.add(elementModel);
        }

        model.setItineraryName(itineraryDto.getName());
        model.setItineraryId(itineraryDto.getId());

        List<ElementReportModel> listReport = model.getReports();
        listReport.clear();
        listReport.addAll(listReportModel);

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
