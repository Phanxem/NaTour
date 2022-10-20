package com.unina.natour.controllers;

import android.content.Intent;

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

    public DettagliSegnalazioneController(NaTourActivity activity){
        super(activity);

        this.dettagliSegnalazioneModel = new DettagliSegnalazioneModel();

        this.userDAO = new UserDAOImpl(activity);
        this.itineraryDAO = new ItineraryDAOImpl(activity);
        this.reportDAO = new ReportDAOImpl();

        boolean result = initModel();
        if(!result){
            //TODO
            showErrorMessage(0);
            getActivity().finish();
            return;
        }
    }

    public boolean initModel(){
        long reportId = getActivity().getIntent().getLongExtra(EXTRA_REPORT_ID, -1);

        if(reportId < 0){
            //TODO
            showErrorMessage(0);
            return false;
        }

        //TODO test
        GetReportResponseDTO reportDTO = reportDAO.getReportById(reportId);
        ResultMessageDTO resultMessageDTO = reportDTO.getResultMessage();
        if(resultMessageDTO.getCode() != MessageController.SUCCESS_CODE){
            //TODO
            showErrorMessage(resultMessageDTO.getCode());
            return false;
        }

        long itineraryId = reportDTO.getIdItinerary();

        GetItineraryResponseDTO itineraryDTO = itineraryDAO.getItineraryById(itineraryId);
        resultMessageDTO = itineraryDTO.getResultMessage();
        if(resultMessageDTO.getCode() != MessageController.SUCCESS_CODE){
            //TODO
            showErrorMessage(resultMessageDTO.getCode());
            return false;
        }

        boolean result = dtoToModel(reportDTO, itineraryDTO, dettagliSegnalazioneModel);
        if(!result){
            //TODO
            showErrorMessage(0);
            return false;
        }

        return true;
    }

    public DettagliSegnalazioneModel getModel() {
        return dettagliSegnalazioneModel;
    }

    public boolean isMyItinerary() {

        /*TODO
        effettuare un controllo per verificare se l'utente ha effettuato l'accesso guest,
        in questo caso il risultato della funzione sarà sempre false;

        String username = Amplify.Auth.getCurrentUser().getUsername();
        UserDTO userDTO = userDAO.getUser(username);
        ItineraryResponseDTO itineraryDTO = itineraryDAO.findById(dettagliSegnalazioneModel.getItineraryId());

        long idUserItinerary = itineraryDTO.getId_user()
        long myIdUser = userDTO.getId();

        if(idUserItinerary == myIdUser) return true
        return false;
        */
        return true;
    }

    //---

    public static void openDettagliSegnalazioneActivity(NaTourActivity fromActivity, long reportId){
        Intent intent = new Intent(fromActivity, DettagliSegnalazioneActivity.class);
        intent.putExtra(EXTRA_REPORT_ID, reportId);
        fromActivity.startActivity(intent);
    }

    //---

    public static boolean dtoToModel(GetReportResponseDTO dtoReport, GetItineraryResponseDTO dtoItinerary, DettagliSegnalazioneModel model){
        model.clear();

        model.setItineraryId(dtoItinerary.getId());
        model.setItineraryName(dtoItinerary.getName());
        model.setReportName(dtoReport.getName());
        model.setDescrizione(dtoReport.getDescription());
        model.setDateOfInput(dtoReport.getDateOfInput());
        model.setReportId(dtoReport.getId());

        return true;
    }

}
