package com.unina.natour.controllers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentActivity;
import androidx.preference.PreferenceManager;

import com.unina.natour.controllers.utils.TimeUtils;
import com.unina.natour.dto.response.ItineraryResponseDTO;
import com.unina.natour.models.DettagliItinerarioModel;
import com.unina.natour.models.dao.implementation.ItineraryDAOImpl;
import com.unina.natour.models.dao.interfaces.ItineraryDAO;
import com.unina.natour.views.dialogs.MessageDialog;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.CustomZoomButtonsController;
import org.osmdroid.views.MapView;

import java.util.ArrayList;
import java.util.List;

import io.jenetics.jpx.GPX;
import io.jenetics.jpx.Track;
import io.jenetics.jpx.TrackSegment;
import io.jenetics.jpx.WayPoint;

@RequiresApi(api = Build.VERSION_CODES.N)
@SuppressLint("LongLogTag")
public class DettagliItinerarioController {

    private final static String TAG ="DettagliItinerarioController";

    public final static String EXTRA_ITINERARY_ID = "itineraryId";

    FragmentActivity activity;
    MessageDialog messageDialog;

    private DettagliItinerarioModel dettagliItinerarioModel;

    private ItineraryDAO itineraryDAO;


    public DettagliItinerarioController(FragmentActivity activity, MessageDialog messageDialog){
        this.activity = activity;
        this.messageDialog = messageDialog;

        this.itineraryDAO = new ItineraryDAOImpl(activity);


        Intent intent = activity.getIntent();
        long itineraryId = intent.getLongExtra(EXTRA_ITINERARY_ID,-1);
/*
        if(itineraryId < 0){
            //todo error
            return;
        }
*/
        ItineraryResponseDTO itineraryResponseDTO = itineraryDAO.findById(itineraryId);
        this.dettagliItinerarioModel = toModel(itineraryResponseDTO);
    }



    public MessageDialog getMessageDialog() {
        return messageDialog;
    }



    public void initMap(MapView mapView) {
        mapView.setTileSource(TileSourceFactory.MAPNIK);

        mapView.setMultiTouchControls(true);

        IMapController mapController = mapView.getController();
        mapController.setZoom(9.5);
        mapView.setMaxZoomLevel(17.0);
        mapView.setMinZoomLevel(5.0);


        CustomZoomButtonsController zoomController = mapView.getZoomController();
        zoomController.setVisibility(CustomZoomButtonsController.Visibility.NEVER);
    }

    public void initOsmdroidConfiguration(){
        Context appContext = activity.getApplicationContext();
        Configuration.getInstance().load(appContext, PreferenceManager.getDefaultSharedPreferences(appContext));
    }


    public DettagliItinerarioModel getModel() {
         return this.dettagliItinerarioModel;
    }



    private DettagliItinerarioModel toModel(ItineraryResponseDTO dto) {

        DettagliItinerarioModel model = new DettagliItinerarioModel();


        model.setItineraryId(dto.getId());
        model.setDescription(dto.getDescription());
        int difficulty = dto.getDifficulty();
        if(difficulty == 0) model.setDifficulty("Facile");
        else if(difficulty == 1) model.setDifficulty("Intermedio");
        else if(difficulty == 2) model.setDifficulty("Difficile");
        else{
            Log.i(TAG, "errore conversione dto to model");
            return null;
        }

        float duration = dto.getDuration();
        String stringDuration = TimeUtils.toDurationString(duration);
        model.setDuration(stringDuration);

        float lenght = dto.getLenght();
        String stringLenght = TimeUtils.toDistanceString(lenght);
        model.setLenght(stringLenght);

        model.setName(dto.getName());

       GPX gpx = dto.getGpx();

       List<GeoPoint> wayPoints = new ArrayList<GeoPoint>();
       for(WayPoint wayPoint : gpx.getWayPoints()){
           GeoPoint geoPoint = new GeoPoint(wayPoint.getLatitude().doubleValue(), wayPoint.getLongitude().doubleValue());
           wayPoints.add(geoPoint);
       }

       model.setWayPoints(wayPoints);


       List<GeoPoint> routePoints = new ArrayList<GeoPoint>();
       Track track = gpx.getTracks().get(0);
       List<TrackSegment> segments = track.getSegments();
       for(TrackSegment segment : segments){
           List<WayPoint> routeWayPoints = segment.getPoints();
           for(WayPoint routeWayPoint: routeWayPoints){
               GeoPoint geoPoint = new GeoPoint(routeWayPoint.getLatitude().doubleValue(), routeWayPoint.getLongitude().doubleValue());
               routePoints.add(geoPoint);
           }
       }

       model.setRoutePoints(routePoints);


       model.setHasBeenReported(dto.getHasBeenReported());
       return model;
    }
}
