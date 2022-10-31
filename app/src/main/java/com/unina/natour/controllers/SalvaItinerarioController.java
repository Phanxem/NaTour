package com.unina.natour.controllers;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.activity.result.ActivityResultLauncher;
import androidx.core.app.ActivityCompat;

import com.unina.natour.R;
import com.unina.natour.config.CurrentUserInfo;
import com.unina.natour.dto.request.SaveItineraryRequestDTO;
import com.unina.natour.dto.response.ResultMessageDTO;
import com.unina.natour.models.AddressModel;
import com.unina.natour.models.SalvaItinerarioModel;
import com.unina.natour.models.dao.implementation.ItineraryDAOImpl;
import com.unina.natour.models.dao.interfaces.ItineraryDAO;
import com.unina.natour.views.activities.ImportaFileGPXActivity;
import com.unina.natour.views.activities.NaTourActivity;
import com.unina.natour.views.activities.SalvaItinerarioActivity;

import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;
import java.util.List;

import io.jenetics.jpx.GPX;
import io.jenetics.jpx.TrackSegment;
import io.jenetics.jpx.WayPoint;

public class SalvaItinerarioController extends NaTourController{

    public final static int CODE_DIFFICULTY_EASY = 0;
    public final static int CODE_DIFFICULTY_MEDIUM = 1;
    public final static int CODE_DIFFICULTY_HARD = 2;

    public final static String EXTRA_DURATION = "duration";
    public final static String EXTRA_DISTANCE = "distance";
    public final static String EXTRA_WAYPOINTS = "waypoints";
    public static final String EXTRA_IS_SAVED = "isSaved";

    SalvaItinerarioModel salvaItinerarioModel;

    ItineraryDAO itineraryDAO;


    public SalvaItinerarioController(NaTourActivity activity){
        super(activity);

        Intent intent = activity.getIntent();
        float duration = intent.getFloatExtra(EXTRA_DURATION, -1);
        float distance = intent.getFloatExtra(EXTRA_DISTANCE, -1);
        List<AddressModel> wayPoints = intent.getParcelableArrayListExtra(EXTRA_WAYPOINTS);

        this.salvaItinerarioModel = new SalvaItinerarioModel();

        if(duration >= 0 && distance >= 0 && wayPoints != null && !wayPoints.isEmpty()){
            salvaItinerarioModel.setDefaultDuration(duration);
            salvaItinerarioModel.setDistance(distance);
            salvaItinerarioModel.setWayPoints(wayPoints);
        }


        this.itineraryDAO = new ItineraryDAOImpl(activity);

    }

    public SalvaItinerarioModel getModel(){ return salvaItinerarioModel; }

    public void setDifficulty(Integer difficulty){
        Activity activity = getActivity();
        String messageToShow = null;

        if(difficulty != null && (difficulty < CODE_DIFFICULTY_EASY || difficulty > CODE_DIFFICULTY_HARD)) {
            messageToShow = activity.getString(R.string.Message_InvalidDifficultyError);
            showErrorMessage(messageToShow);
            return ;
        }

        salvaItinerarioModel.setDifficulty(difficulty);
    }

    public void setDuration(float duration){
        Log.i(TAG, "getDuration");
        salvaItinerarioModel.setDuration(duration);
    }

    public void resetDuration(){
        salvaItinerarioModel.setDuration(-1);
    }


    public boolean saveItinerary(String titolo, String descrizione) {
        Activity activity = getActivity();
        String messageToShow = null;

        if(salvaItinerarioModel.getDefaultDuration() < 0 ||
           salvaItinerarioModel.getDistance() < 0 ||
           salvaItinerarioModel.getWayPoints() == null ||
           salvaItinerarioModel.getWayPoints().isEmpty())
        {
            messageToShow = activity.getString(R.string.Message_InvalidItineraryError);
            showErrorMessage(messageToShow);
            return false;
        }


        if(titolo == null || titolo.isEmpty()){
            messageToShow = activity.getString(R.string.Message_InvalidItineraryTitleError);
            showErrorMessage(messageToShow);
            return false;
        }

        if(salvaItinerarioModel.getDifficulty() == null){
            messageToShow = activity.getString(R.string.Message_InvalidItineraryDifficultyError);
            showErrorMessage(messageToShow);
            return false;
        }


        List<WayPoint> wayPoints = new ArrayList<WayPoint>();
        for(AddressModel address : salvaItinerarioModel.getWayPoints()){
            GeoPoint point = address.getPoint();
            WayPoint wayPoint = WayPoint.of(point.getLatitude(),point.getLongitude());
            wayPoints.add(wayPoint);
        }

        TrackSegment trackSegment = TrackSegment.of(wayPoints);

        GPX gpx = GPX.builder()
                .addTrack( track -> track.addSegment(trackSegment))
                .build();

        SaveItineraryRequestDTO itineraryDTO = new SaveItineraryRequestDTO();

        itineraryDTO.setName(titolo);
        itineraryDTO.setDescription(descrizione);
        if(salvaItinerarioModel.getDuration() < 0) itineraryDTO.setDuration(salvaItinerarioModel.getDefaultDuration());
        else itineraryDTO.setDuration(salvaItinerarioModel.getDuration());
        itineraryDTO.setLenght(salvaItinerarioModel.getDistance());
        itineraryDTO.setDifficulty(salvaItinerarioModel.getDifficulty());
        itineraryDTO.setGpx(gpx);

        String stringIdUser = String.valueOf(CurrentUserInfo.getId());
        itineraryDTO.setIdUser(stringIdUser);



        ResultMessageDTO resultMessageDTO = itineraryDAO.addItinerary(itineraryDTO);
        if(!ResultMessageController.isSuccess(resultMessageDTO)){
            messageToShow = activity.getString(R.string.Message_UnknownError);
            showErrorMessage(messageToShow);
            return false;
        }

        Intent intent = new Intent();
        intent.putExtra(EXTRA_IS_SAVED, true);
        getActivity().setResult(0, intent);
        return true;
    }


    public static void openSalvaItinerarioActivity(NaTourActivity fromActivity, ActivityResultLauncher<Intent> activityResultLauncherSalvaItinerario, float duration, float distance, ArrayList<AddressModel> wayPoints){
        Intent intent = new Intent(fromActivity, SalvaItinerarioActivity.class);
        intent.putExtra(EXTRA_DURATION,duration);
        intent.putExtra(EXTRA_DISTANCE,distance);
        intent.putParcelableArrayListExtra(EXTRA_WAYPOINTS,wayPoints);
        activityResultLauncherSalvaItinerario.launch(intent);

        //fromActivity.startActivity(intent);
    }

    public void back(){
        getActivity().onBackPressed();
    }



}
