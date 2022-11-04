package com.unina.natour.controllers;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.util.Log;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.preference.PreferenceManager;

import com.unina.natour.R;
import com.unina.natour.controllers.utils.AddressUtils;
import com.unina.natour.config.CurrentUserInfo;
import com.unina.natour.controllers.utils.GPSUtils;
import com.unina.natour.controllers.utils.ImageUtils;
import com.unina.natour.controllers.utils.TimeUtils;
import com.unina.natour.dto.response.GetRouteLegResponseDTO;
import com.unina.natour.dto.response.GetRouteResponseDTO;
import com.unina.natour.dto.response.ResultMessageDTO;
import com.unina.natour.dto.response.composted.GetItineraryWithGpxAndUserAndReportResponseDTO;
import com.unina.natour.models.DettagliItinerarioModel;
import com.unina.natour.models.dao.implementation.ItineraryDAOImpl;
import com.unina.natour.models.dao.implementation.RouteDAOImpl;
import com.unina.natour.models.dao.implementation.UserDAOImpl;
import com.unina.natour.models.dao.interfaces.ItineraryDAO;
import com.unina.natour.models.dao.interfaces.RouteDAO;
import com.unina.natour.models.dao.interfaces.UserDAO;
import com.unina.natour.views.activities.DettagliItinerarioActivity;
import com.unina.natour.views.activities.NaTourActivity;
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
import io.jenetics.jpx.WayPoint;


public class DettagliItinerarioController extends NaTourController{

    private final static int USER_IMAGE_SIZE_DP = 24;

    public final static String EXTRA_ITINERARY_ID = "itineraryId";


    ActivityResultLauncher<String> activityResultLauncherPermissions;

    private LocationListener locationListener;

    private DettagliItinerarioModel dettagliItinerarioModel;

    private UserDAO userDAO;
    private ItineraryDAO itineraryDAO;
    private RouteDAO routeDAO;


    public DettagliItinerarioController(NaTourActivity activity, long idItinerary){
        super(activity);
        String messageToShow = null;

        if(idItinerary < 0){
            messageToShow = activity.getString(R.string.Message_UnknownError);
            showErrorMessageAndBack(messageToShow);
            return;
        }

        this.userDAO = new UserDAOImpl(activity);
        this.itineraryDAO = new ItineraryDAOImpl(activity);
        this.routeDAO = new RouteDAOImpl();

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

        this.dettagliItinerarioModel = new DettagliItinerarioModel();
        boolean result = initModel(idItinerary);
        if(!result){
            return;
        }

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

    public boolean initModel(long idItinerary){
        Activity activity = getActivity();
        String messageToShow = null;

        GetItineraryWithGpxAndUserAndReportResponseDTO itineraryDTO = itineraryDAO.getItineraryWithGpxAndUserAndReportById(idItinerary);
        ResultMessageDTO resultMessageDTO = itineraryDTO.getResultMessage();
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


        boolean result = dtoToModel(itineraryDTO,dettagliItinerarioModel);
        if(!result){
            messageToShow = activity.getString(R.string.Message_UnknownError);
            showErrorMessageAndBack(messageToShow);
            return false;
        }

        return true;
    }

    public boolean initMap(MapView mapView) {
        if(mapView == null) return false;

        mapView.setTileSource(TileSourceFactory.MAPNIK);

        mapView.setMultiTouchControls(true);

        IMapController mapController = mapView.getController();
        mapController.setZoom(9.5);
        mapView.setMaxZoomLevel(17.0);
        mapView.setMinZoomLevel(5.0);

        CustomZoomButtonsController zoomController = mapView.getZoomController();
        zoomController.setVisibility(CustomZoomButtonsController.Visibility.NEVER);

        return true;
    }

    public boolean initOsmdroidConfiguration(){
        Context appContext = getActivity().getApplicationContext();
        Configuration.getInstance().load(appContext, PreferenceManager.getDefaultSharedPreferences(appContext));
        return true;
    }


    public DettagliItinerarioModel getModel() {
         return this.dettagliItinerarioModel;
    }


    public boolean isMyItinerary() {
        if(!CurrentUserInfo.isSignedIn()){
            return false;
        }

        long id = CurrentUserInfo.getId();
        long idUserItinerary = dettagliItinerarioModel.getIdUser();

        Log.e(TAG, "my id: " + id + ", other id: " + idUserItinerary);

        if(id == idUserItinerary) return true;

        return false;
    }


    public void activeNavigation(){
        Activity activity = getActivity();
        String messageToShow = null;

        if(!GPSUtils.hasGPSFeature(getActivity())){
            messageToShow = activity.getString(R.string.Message_GPSFeatureNotFoundError);
            showErrorMessage(messageToShow);
            return ;
        }

        if(!GPSUtils.isGPSEnabled(getActivity())){
            messageToShow = activity.getString(R.string.Message_GPSDisableError);
            showErrorMessage(messageToShow);
            return ;
        }

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            activityResultLauncherPermissions.launch(Manifest.permission.ACCESS_FINE_LOCATION);
            return;
        }

        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 4000, 0, (LocationListener) locationListener);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 4000, 0, locationListener);

        if(dettagliItinerarioModel.getCurrentLocation() == null){
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if(location != null){
                dettagliItinerarioModel.setCurrentLocation(new GeoPoint(location.getLatitude(), location.getLongitude()));
            }

            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if(location != null){
                dettagliItinerarioModel.setCurrentLocation(new GeoPoint(location.getLatitude(), location.getLongitude()));
            }
            else{
                dettagliItinerarioModel.setCurrentLocation(new GeoPoint(0d,0d));
            }
        }

        List<GeoPoint> routePoints = dettagliItinerarioModel.getRoutePoints();
        GeoPoint currentLocationPoint = dettagliItinerarioModel.getCurrentLocation();

        if(!AddressUtils.isPointCloseToRoute(currentLocationPoint, routePoints, 500)){
            MessageDialog messageDialog = new MessageDialog();
            messageDialog.setNaTourActivity(getActivity());
            messageDialog.setMessage(getActivity().getResources().getString(R.string.DettagliItinerarioDialog_textView_lontanoDalPercorso));
            messageDialog.showOverUi();
        }


        dettagliItinerarioModel.setNavigationActive(true);
    }


    public void deactivateNavigation(){
        dettagliItinerarioModel.setNavigationActive(false);
        dettagliItinerarioModel.setCurrentLocation(null);
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        locationManager.removeUpdates(locationListener);
    }


    //---

    public static void openDettagliItinerarioActivity(NaTourActivity fromActivity, long itineraryId){
        Intent intent = new Intent(fromActivity, DettagliItinerarioActivity.class);
        intent.putExtra(EXTRA_ITINERARY_ID, itineraryId);
        fromActivity.startActivity(intent);
    }

    //---

    public boolean dtoToModel(GetItineraryWithGpxAndUserAndReportResponseDTO dto, DettagliItinerarioModel model){
        model.clear();

        model.setItineraryId(dto.getId());
        model.setDescription(dto.getDescription());

        int difficulty = dto.getDifficulty();
        if(difficulty == 0) model.setDifficulty("Facile");
        else if(difficulty == 1) model.setDifficulty("Intermedio");
        else if(difficulty == 2) model.setDifficulty("Difficile");

        float duration = dto.getDuration();
        String stringDuration = TimeUtils.toDurationString(duration);
        model.setDuration(stringDuration);

        float lenght = dto.getLenght();
        String stringLenght = TimeUtils.toDistanceString(lenght);
        model.setLenght(stringLenght);

        model.setName(dto.getName());

        model.setIdUser(dto.getIdUser());
        model.setUsername(dto.getUsername());

        if(dto.getProfileImage() != null){
            Bitmap resizeUserImage = ImageUtils.resizeBitmap(dto.getProfileImage(),USER_IMAGE_SIZE_DP);
            model.setProfileImage(resizeUserImage);
        }
        /*
        else {
            Bitmap genericProfileImage = BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.ic_generic_account);
            model.setProfileImage(genericProfileImage);
        }
        */


        GPX gpx = dto.getGpx();

        List<GeoPoint> wayPoints = new ArrayList<GeoPoint>();
        for(WayPoint wayPoint : gpx.getWayPoints()){
            GeoPoint geoPoint = new GeoPoint(wayPoint.getLatitude().doubleValue(), wayPoint.getLongitude().doubleValue());
            wayPoints.add(geoPoint);
        }
        model.setWayPoints(wayPoints);


        List<GeoPoint> routePoints = new ArrayList<GeoPoint>();

        GetRouteResponseDTO getRouteResponseDTO = routeDAO.getRouteByGeoPoints(wayPoints);
        if(!ResultMessageController.isSuccess(getRouteResponseDTO.getResultMessage())){
            return false;
        }

        List<GetRouteLegResponseDTO> listRouteLeg = getRouteResponseDTO.getTracks();
        for(GetRouteLegResponseDTO routeLeg: listRouteLeg){
            List<GeoPoint> listGeoPoint = routeLeg.getTrack();
            routePoints.addAll(listGeoPoint);
        }
        /*
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
        */
        model.setRoutePoints(routePoints);

        model.setHasBeenReported(dto.isReported());

        return true;
    }
}
