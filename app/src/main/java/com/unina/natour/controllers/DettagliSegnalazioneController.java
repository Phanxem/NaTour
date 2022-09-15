package com.unina.natour.controllers;

import android.content.Intent;

import com.amplifyframework.core.Amplify;
import com.unina.natour.dto.ReportDTO;
import com.unina.natour.dto.UserDTO;
import com.unina.natour.dto.response.ItineraryResponseDTO;
import com.unina.natour.models.DettagliSegnalazioneModel;
import com.unina.natour.models.dao.implementation.ItineraryDAOImpl;
import com.unina.natour.models.dao.implementation.ReportDAOImpl;
import com.unina.natour.models.dao.implementation.UserDAOImpl;
import com.unina.natour.models.dao.interfaces.ItineraryDAO;
import com.unina.natour.models.dao.interfaces.ReportDAO;
import com.unina.natour.models.dao.interfaces.UserDAO;
import com.unina.natour.views.activities.DettagliItinerarioActivity;
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

        initModel();
    }

    public void initModel(){
        long reportId = getActivity().getIntent().getLongExtra(EXTRA_REPORT_ID, -1);

        if(reportId < 0){

        }

        ReportDTO reportDTO = reportDAO.findById(reportId);
        long itineraryId = reportDTO.getId_itinerary();

        ItineraryResponseDTO itineraryDTO = itineraryDAO.findById(itineraryId);

        dettagliSegnalazioneModel.setItineraryId(itineraryId);
        dettagliSegnalazioneModel.setItineraryName(itineraryDTO.getName());
        dettagliSegnalazioneModel.setReportName(reportDTO.getName());
        dettagliSegnalazioneModel.setDescrizione(reportDTO.getDescription());
        dettagliSegnalazioneModel.setDateOfInput(reportDTO.getDateOfInput());
        dettagliSegnalazioneModel.setReportId(reportDTO.getId());
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

    public static void openDettagliSegnalazioneActivity(NaTourActivity fromActivity, long reportId){
        Intent intent = new Intent(fromActivity, DettagliSegnalazioneActivity.class);
        intent.putExtra(EXTRA_REPORT_ID, reportId);
        fromActivity.startActivity(intent);
    }

    public DettagliSegnalazioneModel getModel() {
        return dettagliSegnalazioneModel;
    }
}
