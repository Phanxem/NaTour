package com.unina.natour.controllers;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.util.Log;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.preference.PreferenceManager;

import com.amplifyframework.core.Amplify;
import com.unina.natour.controllers.exceptionHandler.ExceptionHandler;
import com.unina.natour.controllers.exceptionHandler.exceptions.subAppException.GPSFeatureNotPresentException;
import com.unina.natour.controllers.exceptionHandler.exceptions.subAppException.GPSNotEnabledException;
import com.unina.natour.controllers.utils.GPSUtils;
import com.unina.natour.controllers.utils.TimeUtils;
import com.unina.natour.dto.UserDTO;
import com.unina.natour.dto.response.ItineraryResponseDTO;
import com.unina.natour.models.DettagliItinerarioModel;
import com.unina.natour.models.dao.implementation.ItineraryDAOImpl;
import com.unina.natour.models.dao.interfaces.ItineraryDAO;
import com.unina.natour.models.dao.interfaces.UserDAO;
import com.unina.natour.views.activities.DettagliItinerarioActivity;
import com.unina.natour.views.activities.NaTourActivity;
import com.unina.natour.views.activities.PersonalizzaAccountImmagineActivity;
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
public class DettagliItinerarioController extends NaTourController{

    public final static String EXTRA_ITINERARY_ID = "itineraryId";

    ActivityResultLauncher<String> activityResultLauncherPermissions;

    private LocationListener locationListener;

    private DettagliItinerarioModel dettagliItinerarioModel;

    private ItineraryDAO itineraryDAO;


    public DettagliItinerarioController(NaTourActivity activity){
        super(activity);

        this.itineraryDAO = new ItineraryDAOImpl(activity);

        this.locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                GeoPoint geoPoint = new GeoPoint(location.getLatitude(),location.getLongitude());
                dettagliItinerarioModel.setCurrentLocation(geoPoint);
                Log.i(TAG,"lat: " + geoPoint.getLatitude() + " | lon: " + geoPoint.getLongitude());
            }

            public void onProviderDisabled(String provider) {

            }


            public void onProviderEnabled(String provider) {

            }
        };



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


        this.activityResultLauncherPermissions = activity.registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                new ActivityResultCallback<Boolean>() {
                    @Override
                    public void onActivityResult(Boolean result) {
                        if (result) activeNavigation();
                    }
                }
        );

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
        Context appContext = getActivity().getApplicationContext();
        Configuration.getInstance().load(appContext, PreferenceManager.getDefaultSharedPreferences(appContext));
    }


    public DettagliItinerarioModel getModel() {
         return this.dettagliItinerarioModel;
    }


    public boolean isMyItinerary() {
        //TODO
        /*
        String username = Amplify.Auth.getCurrentUser().getUsername();
        UserDTO userDTO = userDAO.getUser(username);
        long idUserItinerary = dettagliItinerarioModel.getIdUser();
        long myIdUser = userDTO.getIdUser();

        if(idUserItinerary == myIdUser) return true
        return false;
        */
        return true;
    }

    //---

    public void activeNavigation(){
        if(!GPSUtils.hasGPSFeature(getActivity())){
            Log.i(TAG, "gps1");
            GPSFeatureNotPresentException exception = new GPSFeatureNotPresentException();
            ExceptionHandler.handleMessageError(getMessageDialog(),exception);
            return;
        }

        if(!GPSUtils.isGPSEnabled(getActivity())){
            Log.i(TAG, "gps2");
            GPSNotEnabledException exception = new GPSNotEnabledException();
            ExceptionHandler.handleMessageError(getMessageDialog(),exception);
            return;
        }

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "gps3");
            activityResultLauncherPermissions.launch(Manifest.permission.ACCESS_FINE_LOCATION);
            return;
        }


        Log.i(TAG, "gps4");
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 4000, 0, (LocationListener) locationListener);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 4000, 0, locationListener);

        if(dettagliItinerarioModel.getCurrentLocation() == null){
            Log.i(TAG, "gps5");
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if(location != null){
                dettagliItinerarioModel.setCurrentLocation(new GeoPoint(location.getLatitude(), location.getLongitude()));
            }

            Log.i(TAG, "gps6");
            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if(location != null){
                dettagliItinerarioModel.setCurrentLocation(new GeoPoint(location.getLatitude(), location.getLongitude()));
            }
            else{Log.i(TAG, "gps7");
                dettagliItinerarioModel.setCurrentLocation(new GeoPoint(0d,0d));
            }
        }

        Log.i(TAG, "gps8");
        dettagliItinerarioModel.setNavigationActive(true);
    }


    public void deactivateNavigation(){
        dettagliItinerarioModel.setNavigationActive(false);
        dettagliItinerarioModel.setCurrentLocation(null);
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        locationManager.removeUpdates(locationListener);
    }



    public void openDettagliItinerarioActivity(long itineraryId){
        Intent intent = new Intent(getActivity(), DettagliItinerarioActivity.class);
        intent.putExtra(EXTRA_ITINERARY_ID,itineraryId);
        getActivity().startActivity(intent);
    }

    public static void openDettagliItinerarioActivity2(NaTourActivity fromActivity, long itineraryId){
        Intent intent = new Intent(fromActivity, DettagliItinerarioActivity.class);
        intent.putExtra(EXTRA_ITINERARY_ID, itineraryId);
        fromActivity.startActivity(intent);
    }


    //---

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
