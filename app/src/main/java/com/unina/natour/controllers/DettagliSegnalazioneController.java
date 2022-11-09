package com.unina.natour.controllers;

import android.app.Activity;
import android.content.Intent;

import com.unina.natour.R;
import com.unina.natour.config.CurrentUserInfo;
import com.unina.natour.dto.response.ResultMessageDTO;
import com.unina.natour.dto.response.GetReportResponseDTO;
import com.unina.natour.dto.response.GetItineraryResponseDTO;
import com.unina.natour.models.DettagliSegnalazioneModel;
import com.unina.natour.models.dao.implementation.ItineraryDAOImpl;
import com.unina.natour.models.dao.implementation.ReportDAOImpl;
import com.unina.natour.models.dao.implementation.UserDAOImpl;
import com.unina.natour.models.dao.interfaces.ItineraryDAO;
import com.unina.natour.models.dao.interfaces.ReportDAO;
import com.unina.natour.models.dao.interfaces.UserDAO;
import com.unina.natour.views.activities.DettagliSegnalazioneActivity;
import com.unina.natour.views.activities.NaTourActivity;

public class DettagliSegnalazioneController extends NaTourController{

    public final static String EXTRA_REPORT_ID = "reportId";

    private DettagliSegnalazioneModel dettagliSegnalazioneModel;

    private UserDAO userDAO;
    private ItineraryDAO itineraryDAO;
    private ReportDAO reportDAO;

    public DettagliSegnalazioneController(NaTourActivity activity,
                                          ResultMessageController resultMessageController,
                                          DettagliSegnalazioneModel dettagliSegnalazioneModel,
                                          UserDAO userDAO,
                                          ItineraryDAO itineraryDAO,
                                          ReportDAO reportDAO){
        super(activity, resultMessageController);

        this.dettagliSegnalazioneModel = dettagliSegnalazioneModel;

        this.userDAO = userDAO;
        this.itineraryDAO = itineraryDAO;
        this.reportDAO = reportDAO;
    }

    public DettagliSegnalazioneController(NaTourActivity activity){
        super(activity);

        this.dettagliSegnalazioneModel = new DettagliSegnalazioneModel();

        this.userDAO = new UserDAOImpl(activity);
        this.itineraryDAO = new ItineraryDAOImpl(activity);
        this.reportDAO = new ReportDAOImpl();

        boolean result = initModel();
        if(!result){ return;}
    }

    public boolean initModel(){
        Activity activity = getActivity();
        String messageToShow = null;

        Intent intent = getActivity().getIntent();
        long reportId = intent.getLongExtra(EXTRA_REPORT_ID, -1);

        if(reportId < 0){
            messageToShow = activity.getString(R.string.Message_UnknownError);
            showErrorMessageAndBack(messageToShow);
            return false;
        }

        GetReportResponseDTO reportDTO = reportDAO.getReportById(reportId);
        ResultMessageDTO resultMessageDTO = reportDTO.getResultMessage();
        if(!ResultMessageController.isSuccess(resultMessageDTO)){
            if(resultMessageDTO.getCode() == ResultMessageController.ERROR_CODE_NOT_FOUND){
                messageToShow = activity.getString(R.string.Message_ReportNotFoundError);
                showErrorMessageAndBack(messageToShow);
                return false;
            }

            messageToShow = activity.getString(R.string.Message_UnknownError);
            showErrorMessageAndBack(messageToShow);
            return false;
        }

        /*
        long itineraryId = reportDTO.getIdItinerary();

        GetItineraryResponseDTO itineraryDTO = itineraryDAO.getItineraryById(itineraryId);
        resultMessageDTO = itineraryDTO.getResultMessage();
        if(!ResultMessageController.isSuccess(resultMessageDTO)){
            if(resultMessageDTO.getCode() == ResultMessageController.ERROR_CODE_NOT_FOUND){
                messageToShow = activity.getString(R.string.Message_ItineraryNotFoundError);
                showErrorMessageAndBack(messageToShow);
                return false;
            }

            messageToShow = activity.getString(R.string.Message_UnknownError);
            showErrorMessageAndBack(messageToShow);
            return false;
        }
*/

        //boolean result = dtoToModel(reportDTO, itineraryDTO, dettagliSegnalazioneModel);
        boolean result = dtoToModel(reportDTO, dettagliSegnalazioneModel);
        if(!result){
            messageToShow = activity.getString(R.string.Message_UnknownError);
            showErrorMessageAndBack(messageToShow);
            return false;
        }

        return true;
    }

    public DettagliSegnalazioneModel getModel() {
        return dettagliSegnalazioneModel;
    }

    public boolean isMyItinerary() {
        Activity activity = getActivity();
        String messageToShow = null;

        if(!CurrentUserInfo.isSignedIn()){
            return false;
        }

        GetItineraryResponseDTO getItineraryResponseDTO = itineraryDAO.getItineraryById(dettagliSegnalazioneModel.getItineraryId());
        if(!ResultMessageController.isSuccess(getItineraryResponseDTO.getResultMessage())){
            messageToShow = activity.getString(R.string.Message_UnknownError);
            showErrorMessage(messageToShow);
            return false;
        }

        long id = CurrentUserInfo.getId();
        long idUserItinerary = getItineraryResponseDTO.getIdUser();

        if(id == idUserItinerary) return true;

        return false;
    }

    public boolean isMyReport(){
        Activity activity = getActivity();
        String messageToShow = null;

        if(!CurrentUserInfo.isSignedIn()) return false;

        long id = CurrentUserInfo.getId();
        long idUserReport = dettagliSegnalazioneModel.getUserId();

        if(id == idUserReport) return true;

        return false;
    }
    //---

    public static void openDettagliSegnalazioneActivity(NaTourActivity fromActivity, long reportId){
        Intent intent = new Intent(fromActivity, DettagliSegnalazioneActivity.class);
        intent.putExtra(EXTRA_REPORT_ID, reportId);
        fromActivity.startActivity(intent);
    }

    //---

    public static boolean dtoToModel(GetReportResponseDTO dtoReport, DettagliSegnalazioneModel model){
        model.clear();

        model.setReportId(dtoReport.getId());
        model.setReportName(dtoReport.getName());
        model.setDescrizione(dtoReport.getDescription());
        model.setDateOfInput(dtoReport.getDateOfInput());

        model.setUserId(dtoReport.getIdUser());

        model.setItineraryId(dtoReport.getIdItinerary());
        model.setItineraryName(dtoReport.getNameItinerary());

        return true;
    }

}
