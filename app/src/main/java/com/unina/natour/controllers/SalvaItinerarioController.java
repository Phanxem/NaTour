package com.unina.natour.controllers;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentActivity;

import com.unina.natour.controllers.exceptionHandler.ExceptionHandler;
import com.unina.natour.controllers.exceptionHandler.exceptions.ServerException;
import com.unina.natour.controllers.exceptionHandler.exceptions.subAppException.FailureAddItineraryException;
import com.unina.natour.controllers.exceptionHandler.exceptions.subAppException.FailureInitSaveItineraryException;
import com.unina.natour.controllers.exceptionHandler.exceptions.subAppException.InvalidDifficultyException;
import com.unina.natour.controllers.exceptionHandler.exceptions.subAppException.NotCompletedAddItineraryException;
import com.unina.natour.dto.request.ItineraryRequestDTO;
import com.unina.natour.models.AddressModel;
import com.unina.natour.models.SalvaItinerarioModel;
import com.unina.natour.models.dao.implementation.ItineraryDAOImpl;
import com.unina.natour.models.dao.interfaces.ItineraryDAO;
import com.unina.natour.views.activities.NaTourActivity;
import com.unina.natour.views.activities.SalvaItinerarioActivity;
import com.unina.natour.views.dialogs.MessageDialog;

import org.osmdroid.util.GeoPoint;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import io.jenetics.jpx.GPX;
import io.jenetics.jpx.TrackSegment;
import io.jenetics.jpx.WayPoint;

@RequiresApi(api = Build.VERSION_CODES.N)
@SuppressLint("LongLogTag")
public class SalvaItinerarioController extends NaTourController{

    public final static int CODE_DIFFICULTY_EASY = 0;
    public final static int CODE_DIFFICULTY_MEDIUM = 1;
    public final static int CODE_DIFFICULTY_HARD = 2;

    public final static String EXTRA_DURATION = "duration";
    public final static String EXTRA_DISTANCE = "distance";
    public final static String EXTRA_WAYPOINTS = "waypoints";
    ;

    SalvaItinerarioModel salvaItinerarioModel;

    ItineraryDAO itineraryDAO;


    public SalvaItinerarioController(NaTourActivity activity){
        super(activity);

        Intent intent = activity.getIntent();
        float duration = intent.getFloatExtra(EXTRA_DURATION, -1);
        float distance = intent.getFloatExtra(EXTRA_DISTANCE, -1);
        List<AddressModel> wayPoints = intent.getParcelableArrayListExtra(EXTRA_WAYPOINTS);

        Log.i(TAG, "-------------------------" + duration + " | " + distance + " | ");


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
        if(difficulty != null && (difficulty < CODE_DIFFICULTY_EASY || difficulty > CODE_DIFFICULTY_HARD)) {
            InvalidDifficultyException exception = new InvalidDifficultyException();
            ExceptionHandler.handleMessageError(getMessageDialog(),exception);
            return;
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

        if(salvaItinerarioModel.getDefaultDuration() < 0) Log.i(TAG, "1---------------");
        if(salvaItinerarioModel.getDistance() < 0)Log.i(TAG, "2---------------");
            if(salvaItinerarioModel.getWayPoints() == null) Log.i(TAG, "3---------------");
        if(salvaItinerarioModel.getWayPoints().isEmpty()) Log.i(TAG, "4---------------");

        if(salvaItinerarioModel.getDefaultDuration() < 0 ||
           salvaItinerarioModel.getDistance() < 0 ||
           salvaItinerarioModel.getWayPoints() == null ||
           salvaItinerarioModel.getWayPoints().isEmpty())
        {
            this.getMessageDialog().setGoBackOnClose(true);
            FailureInitSaveItineraryException exception = new FailureInitSaveItineraryException();
            ExceptionHandler.handleMessageError(getMessageDialog(),exception);
            return false;
        }


        if(titolo == null || titolo.isEmpty()){
            Log.e(TAG, "errore titolo");
            //TODO exteption
            return false;
        }

        if(salvaItinerarioModel.getDifficulty() == null){
            Log.e(TAG, "errore difficoltà");
            //TODO exteption
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

        ItineraryRequestDTO itineraryDTO = new ItineraryRequestDTO();

        itineraryDTO.setName(titolo);
        itineraryDTO.setDescription(descrizione);
        if(salvaItinerarioModel.getDuration() < 0) itineraryDTO.setDuration(salvaItinerarioModel.getDefaultDuration());
        else itineraryDTO.setDuration(salvaItinerarioModel.getDuration());
        itineraryDTO.setLenght(salvaItinerarioModel.getDistance());
        itineraryDTO.setDifficulty(salvaItinerarioModel.getDifficulty());
        itineraryDTO.setGpx(gpx);


        try {
            itineraryDAO.addItinerary(itineraryDTO);
        }
        catch (IOException e) {
            FailureAddItineraryException exception = new FailureAddItineraryException(e);
            ExceptionHandler.handleMessageError(getMessageDialog(),exception);
            return false;
        }
        catch (ExecutionException | InterruptedException e) {
            NotCompletedAddItineraryException exception = new NotCompletedAddItineraryException(e);
            ExceptionHandler.handleMessageError(getMessageDialog(),exception);
            return false;
        }
        catch (ServerException e) {
            ExceptionHandler.handleMessageError(getMessageDialog(),e);
            return false;
        }

        return true;
    }


    public static void openSalvaItinerarioActivity(NaTourActivity fromActivity, float duration, float distance, ArrayList<AddressModel> wayPoints){
        Intent intent = new Intent(fromActivity, SalvaItinerarioActivity.class);
        intent.putExtra(EXTRA_DURATION,duration);
        intent.putExtra(EXTRA_DISTANCE,distance);
        intent.putParcelableArrayListExtra(EXTRA_WAYPOINTS,wayPoints);
        fromActivity.startActivity(intent);
    }

    public void back(){
        getActivity().onBackPressed();
    }



}
