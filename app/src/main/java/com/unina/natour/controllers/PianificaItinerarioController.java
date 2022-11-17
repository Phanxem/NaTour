package com.unina.natour.controllers;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import android.util.Log;
import android.view.MotionEvent;
import android.widget.ListView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.preference.PreferenceManager;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobile.auth.core.IdentityManager;
import com.amazonaws.mobile.config.AWSConfiguration;
import com.unina.natour.R;
import com.unina.natour.controllers.utils.AddressMapper;
import com.unina.natour.controllers.utils.GPSUtils;
import com.unina.natour.dto.response.GetAddressResponseDTO;
import com.unina.natour.dto.response.GetRouteLegResponseDTO;
import com.unina.natour.dto.response.ResultMessageDTO;
import com.unina.natour.dto.response.GetRouteResponseDTO;
import com.unina.natour.models.AddressModel;
import com.unina.natour.models.PianificaItinerarioModel;
import com.unina.natour.models.RouteLegModel;
import com.unina.natour.models.dao.implementation.AddressDAOImpl;
import com.unina.natour.models.dao.implementation.RouteDAOImpl;
import com.unina.natour.models.dao.interfaces.AddressDAO;
import com.unina.natour.models.dao.interfaces.RouteDAO;
import com.unina.natour.views.activities.NaTourActivity;
import com.unina.natour.views.listAdapters.PuntiIntermediListAdapter;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.CustomZoomButtonsController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.Projection;
import org.osmdroid.views.overlay.Overlay;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressLint("LongLogTag")
public class PianificaItinerarioController extends NaTourController implements Parcelable{

    public final static Integer STARTING_POINT_CODE = -2;
    public final static Integer DESTINATION_POINT_CODE = -1;

    public final static GeoPoint DEFAULT_CENTER_POINT = new GeoPoint((double) 0, (double) 0);

    private static final String EXTRA_ID_ITINERARY = "NEW_ITINERARY";

    private ActivityResultLauncher<Intent> activityResultLauncherRicercaPunto;
    private ActivityResultLauncher<Intent> activityResultLauncherImportaFileGPX;
    private ActivityResultLauncher<Intent> activityResultLauncherSalvaItinerario;
    private ActivityResultLauncher<String> activityResultLauncherPermissions;

    private PuntiIntermediListAdapter puntiIntermediListAdapter;

    private PianificaItinerarioModel pianificaItinerarioModel;

    private RouteDAO routeDAO;
    private AddressDAO addressDAO;

    public PianificaItinerarioController(NaTourActivity activity,
                                         ResultMessageController resultMessageController,
                                         ActivityResultLauncher<Intent> activityResultLauncherRicercaPunto,
                                         ActivityResultLauncher<Intent> activityResultLauncherImportaFileGPX,
                                         ActivityResultLauncher<Intent> activityResultLauncherSalvaItinerario,
                                         ActivityResultLauncher<String> activityResultLauncherPermissions,
                                         PuntiIntermediListAdapter puntiIntermediListAdapter,
                                         PianificaItinerarioModel pianificaItinerarioModel,
                                         RouteDAO routeDAO,
                                         AddressDAO addressDAO)
    {
        super(activity, resultMessageController);

        this.activityResultLauncherRicercaPunto = activityResultLauncherRicercaPunto;

        this.activityResultLauncherImportaFileGPX = activityResultLauncherImportaFileGPX;

        this.activityResultLauncherSalvaItinerario = activityResultLauncherSalvaItinerario;

        this.activityResultLauncherPermissions = activityResultLauncherPermissions;

        this.pianificaItinerarioModel = pianificaItinerarioModel;

        this.puntiIntermediListAdapter = puntiIntermediListAdapter;

        this.routeDAO = routeDAO;
        this.addressDAO = addressDAO;
    }


    public PianificaItinerarioController(NaTourActivity activity){
        super(activity);

        this.activityResultLauncherRicercaPunto = activity.registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {

                        if(result == null || result.getData() == null) return;

                        if(result.getResultCode() == RicercaPuntoController.RESULT_CODE_RETURN_POINT){
                            if(result.getData().getParcelableExtra(RicercaPuntoController.EXTRA_ADDRESS) != null) {

                                AddressModel address = result.getData().getParcelableExtra(RicercaPuntoController.EXTRA_ADDRESS);

                                updateInterestPointByIndexSelected(address);

                                pianificaItinerarioModel.removePointSelectedOnMap();
                                pianificaItinerarioModel.removeIndexPointSelected();
                                puntiIntermediListAdapter.notifyDataSetChanged();
                            }
                            return;
                        }

                        if(result.getResultCode() == RicercaPuntoController.RESULT_CODE_GET_FROM_MAP){
                            pianificaItinerarioModel.notifyObservers();
                            return;
                        }
                        pianificaItinerarioModel.removeIndexPointSelected();
                    }
                }
        );

        this.activityResultLauncherImportaFileGPX = activity.registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {

                        if(result == null || result.getData() == null) return;


                        if(result.getResultCode() == ImportaFileGPXController.RESULT_CODE_RETURN_ALL_ADDRESSES){
                            if(result.getData().getParcelableArrayListExtra(ImportaFileGPXController.EXTRA_ADDRESSES) != null) {

                                List<AddressModel> addresses = result.getData().getParcelableArrayListExtra(ImportaFileGPXController.EXTRA_ADDRESSES);
                                pianificaItinerarioModel.updateInterestPoints(addresses);

                                List<GeoPoint> geoPoints = new ArrayList<GeoPoint>();
                                for(AddressModel address: addresses) geoPoints.add(address.getPoint());

                                GetRouteResponseDTO getRouteResponseDTO = null;


                                getRouteResponseDTO = routeDAO.getRouteByGeoPoints(geoPoints);
                                if(!ResultMessageController.isSuccess(getRouteResponseDTO.getResultMessage())){
                                    String messageToShow = activity.getString(R.string.Message_UnknownError);
                                    showErrorMessageAndBack(messageToShow);
                                    return;
                                }

                                List<RouteLegModel> tracks = dtoToModel(getRouteResponseDTO.getTracks());

                                pianificaItinerarioModel.updateRoutes(tracks);
                                puntiIntermediListAdapter.notifyDataSetChanged();

                            }
                        }
                    }
                }
        );

        this.activityResultLauncherSalvaItinerario = activity.registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {

                        if(result == null || result.getData() == null) return;
                        Boolean isSaved = result.getData().getBooleanExtra(SalvaItinerarioController.EXTRA_IS_SAVED, false);

                        if(isSaved){
                            Log.e(TAG, "isSaved");
                            pianificaItinerarioModel.clearAndNotify();
                        }
                    }
                }
        );

        this.activityResultLauncherPermissions = activity.registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                new ActivityResultCallback<Boolean>() {
                    @Override
                    public void onActivityResult(Boolean result) {
                        goToImportGPX();
                    }
                }
        );

        this.pianificaItinerarioModel = new PianificaItinerarioModel();

        this.puntiIntermediListAdapter = new PuntiIntermediListAdapter(
                activity,
                pianificaItinerarioModel.getIntermediatePoints(),
                this
        );

        this.routeDAO = new RouteDAOImpl();
        this.addressDAO = new AddressDAOImpl();


/*
        IdentityManager identityManager = new IdentityManager(
                getActivity().getApplicationContext(),
                new AWSConfiguration(getActivity().getApplicationContext()));

        IdentityManager.setDefaultIdentityManager(identityManager);

        CognitoCachingCredentialsProvider cognitoCachingCredentialsProvider = IdentityManager.getDefaultIdentityManager().getUnderlyingProvider();

        Log.e(TAG, "FederatedLogin Facebook");
        Log.e(TAG, "Token: " + cognitoCachingCredentialsProvider.getToken());
        Log.e(TAG, "AccessKey: " + cognitoCachingCredentialsProvider.getCredentials().getAWSAccessKeyId());
        Log.e(TAG, "SecretKey: " + cognitoCachingCredentialsProvider.getCredentials().getAWSSecretKey());
        Log.e(TAG, "SessionToken: " + cognitoCachingCredentialsProvider.getCredentials().getSessionToken());
*/
    }



    public PianificaItinerarioModel getModel() {
        return pianificaItinerarioModel;
    }

    public boolean initMap(MapView mapView){
        mapView.setTileSource(TileSourceFactory.MAPNIK);

        mapView.setMultiTouchControls(true);

        IMapController mapController = mapView.getController();
        mapController.setZoom(9.5);
        mapView.setMaxZoomLevel(17.0);
        mapView.setMinZoomLevel(5.0);


        CustomZoomButtonsController zoomController = mapView.getZoomController();
        zoomController.setVisibility(CustomZoomButtonsController.Visibility.NEVER);

        Overlay mapOverlay = new Overlay(){
            @Override
            public void draw(Canvas canvas, MapView mapView, boolean shadow) {
                super.draw(canvas,mapView,shadow);
            }

            @Override
            public boolean onSingleTapConfirmed(MotionEvent motionEvent, MapView mapView) {
                Projection projection = mapView.getProjection();
                GeoPoint geoPoint = (GeoPoint) projection.fromPixels((int)motionEvent.getX(), (int)motionEvent.getY());

                Log.i(TAG, "Lat: " + geoPoint.getLatitude() + " | Lon: " + geoPoint.getLongitude());

                setPointSelectedOnMap(geoPoint);

                return true;
            }
        };
        mapView.getOverlays().add(mapOverlay);


        GeoPoint centerPoint = DEFAULT_CENTER_POINT;

        if(GPSUtils.hasGPSFeature(getActivity()) &&
           GPSUtils.isGPSEnabled(getActivity()) &&
           ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            if(location == null) location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            if(location != null) centerPoint = new GeoPoint(location.getLatitude(),location.getLongitude());
        }

        mapController.setCenter(centerPoint);

        return true;
    }

    public void initItermediatePointsList(ListView listView_puntiIntermedi) {
        listView_puntiIntermedi.setAdapter(puntiIntermediListAdapter);
    }

    public void initOsmdroidConfiguration(){
        Context appContext = getActivity().getApplicationContext();
        Configuration.getInstance().load(appContext, PreferenceManager.getDefaultSharedPreferences(appContext));
    }




    public void goToImportGPX(){
        ImportaFileGPXController.openImportaFileGPXActivity(getActivity(),activityResultLauncherImportaFileGPX,activityResultLauncherPermissions);
    }

    public void goToSaveItinerary(float duration, float distance, List<AddressModel> wayPoints){
        SalvaItinerarioController.openSalvaItinerarioActivity(getActivity(),activityResultLauncherSalvaItinerario, duration, distance, wayPoints);
    }


    public void selectStartingPoint() {
        pianificaItinerarioModel.setIndexPointSelected(STARTING_POINT_CODE);
        RicercaPuntoController.openRicercaPuntoActivity(getActivity(),activityResultLauncherRicercaPunto);
    }

    public void selectDestinationPoint() {
        pianificaItinerarioModel.setIndexPointSelected(DESTINATION_POINT_CODE);
        RicercaPuntoController.openRicercaPuntoActivity(getActivity(),activityResultLauncherRicercaPunto);
    }

    public void addIntermediatePoint() {
        int indexIntermediatePoint = pianificaItinerarioModel.getIntermediatePointsSize();

        pianificaItinerarioModel.setIndexPointSelected(indexIntermediatePoint);
        RicercaPuntoController.openRicercaPuntoActivity(getActivity(),activityResultLauncherRicercaPunto);
    }

    public void selectIntermediatePoint(int index) {
        if(!pianificaItinerarioModel.isValidIndexPoint(index)) return;

        pianificaItinerarioModel.setIndexPointSelected(index);
        RicercaPuntoController.openRicercaPuntoActivity(getActivity(),activityResultLauncherRicercaPunto);
    }

    public void cancelStartingPoint() {
        updateRouteWithRemovePoint(STARTING_POINT_CODE);
        if (!pianificaItinerarioModel.hasIntermediatePoints()) {
            pianificaItinerarioModel.removeStartingPoint();

            return;
        }

        AddressModel intermediatePoint = pianificaItinerarioModel.getIntermediatePoint(0);
        pianificaItinerarioModel.setStartingPoint(intermediatePoint);
        pianificaItinerarioModel.removeIntermediatePoint(0);


        puntiIntermediListAdapter.notifyDataSetChanged();
    }


    public void cancelDestinationPoint() {
        updateRouteWithRemovePoint(DESTINATION_POINT_CODE);
        if (!pianificaItinerarioModel.hasIntermediatePoints()) {
            pianificaItinerarioModel.removeDestinationPoint();
            return;
        }

        int indexLastIntermediatePoint = pianificaItinerarioModel.getIntermediatePointsSize() - 1;
        AddressModel intermediatePoint = pianificaItinerarioModel.getIntermediatePoint(indexLastIntermediatePoint);
        pianificaItinerarioModel.setDestinationPoint(intermediatePoint);
        pianificaItinerarioModel.removeIntermediatePoint(indexLastIntermediatePoint);

        puntiIntermediListAdapter.notifyDataSetChanged();
    }

    public void cancelPointSelectedOnMap() {
        pianificaItinerarioModel.removeIndexPointSelected();
        pianificaItinerarioModel.removePointSelectedOnMap();
    }

    public void cancelIntermediatePoint(int index) {
        updateRouteWithRemovePoint(index);
        if(!pianificaItinerarioModel.isValidIndexPoint(index)) return;

        pianificaItinerarioModel.removeIntermediatePoint(index);

        puntiIntermediListAdapter.notifyDataSetChanged();
    }

    public void setSelectedPointOnMapAsStartingPoint() {
        Activity activity = getActivity();
        String messageToShow = null;

        AddressModel address = pianificaItinerarioModel.getPointSelectedOnMap();

        if(address == null){
            messageToShow = activity.getString(R.string.Message_UnknownError);
            showErrorMessage(messageToShow);
            return;
        }

        pianificaItinerarioModel.setStartingPoint(address);
        updateRouteWithSetPoint(STARTING_POINT_CODE);
        pianificaItinerarioModel.removeIndexPointSelected();
        pianificaItinerarioModel.removePointSelectedOnMap();
    }

    public void setSelectedPointOnMapAsDestinationPoint() {
        Activity activity = getActivity();
        String messageToShow = null;

        AddressModel address = pianificaItinerarioModel.getPointSelectedOnMap();

        if(address == null){
            messageToShow = activity.getString(R.string.Message_UnknownError);
            showErrorMessage(messageToShow);
            return;
        }

        pianificaItinerarioModel.setDestinationPoint(address);

        updateRouteWithSetPoint(DESTINATION_POINT_CODE);
        pianificaItinerarioModel.removeIndexPointSelected();
        pianificaItinerarioModel.removePointSelectedOnMap();

    }

    public void setSelectedPointOnMapAsIntermediatePoint() {
        Activity activity = getActivity();
        String messageToShow = null;

        AddressModel address = pianificaItinerarioModel.getPointSelectedOnMap();
        Integer index = pianificaItinerarioModel.getIndexPointSelected();

        if(address == null){
            messageToShow = activity.getString(R.string.Message_UnknownError);
            showErrorMessage(messageToShow);
            return;
        }

        //NUOVO PUNTO INTERMEDIO
        if(index == null || index == pianificaItinerarioModel.getIntermediatePointsSize()) {
            pianificaItinerarioModel.addIntermediatePoint(address);
            updateRouteWithAddPoint();

        }
        //PUNTO INTERMEDIO ESISTENTE
        else{
            pianificaItinerarioModel.setIntermediatePoint(index,address);
            updateRouteWithSetPoint(index);
        }

        puntiIntermediListAdapter.notifyDataSetChanged();
        pianificaItinerarioModel.removeIndexPointSelected();
        pianificaItinerarioModel.removePointSelectedOnMap();
    }



    public void updateInterestPointByIndexSelected(AddressModel address){
        Activity activity = getActivity();
        String messageToShow = null;

        Integer indexPointSelected = pianificaItinerarioModel.getIndexPointSelected();
        if(indexPointSelected == null){
            messageToShow = activity.getString(R.string.Message_UnknownError);
            showErrorMessage(messageToShow);
            return;
        }

        if(indexPointSelected.equals(STARTING_POINT_CODE)){
            pianificaItinerarioModel.setStartingPoint(address);
            updateRouteWithSetPoint(STARTING_POINT_CODE);
            return;
        }
        if(indexPointSelected.equals(DESTINATION_POINT_CODE)){
            pianificaItinerarioModel.setDestinationPoint(address);
            updateRouteWithSetPoint(DESTINATION_POINT_CODE);
            return;
        }
        if(indexPointSelected >= 0 && indexPointSelected < pianificaItinerarioModel.getIntermediatePointsSize()){
            pianificaItinerarioModel.setIntermediatePoint(indexPointSelected,address);
            updateRouteWithSetPoint(indexPointSelected);
            return;
        }
        if(indexPointSelected == pianificaItinerarioModel.getIntermediatePointsSize()){
            pianificaItinerarioModel.addIntermediatePoint(address);
            updateRouteWithAddPoint();
            return;
        }
    }


    private void updateRouteWithRemovePoint(int index) {
        Activity activity = getActivity();
        String messageToShow = null;

        if(!getModel().isValidIndexPoint(index)){
            messageToShow = activity.getString(R.string.Message_UnknownError);
            showErrorMessage(messageToShow);
            return;
        }

        if(index == STARTING_POINT_CODE){
            if(pianificaItinerarioModel.getRouteLegsSize() == 0) return;
            pianificaItinerarioModel.removeRouteLeg(0);
            return;
        }
        if(index == DESTINATION_POINT_CODE){
            if(pianificaItinerarioModel.getRouteLegsSize() == 0) return;
            pianificaItinerarioModel.removeRouteLeg(pianificaItinerarioModel.getRouteLegsSize()-1);
            return;
        }

        AddressModel startingAddressRoute;
        if(index - 1 < 0) startingAddressRoute = pianificaItinerarioModel.getStartingPoint();
        else startingAddressRoute = pianificaItinerarioModel.getIntermediatePoint(index - 1);


        AddressModel destinationAddressRoute;
        if(index + 1 > pianificaItinerarioModel.getIntermediatePointsSize() - 1) destinationAddressRoute = pianificaItinerarioModel.getDestinationPoint();
        else destinationAddressRoute = pianificaItinerarioModel.getIntermediatePoint(index + 1);

        List<GeoPoint> geoPoints = new ArrayList<GeoPoint>();
        geoPoints.add(startingAddressRoute.getPoint());
        geoPoints.add(destinationAddressRoute.getPoint());



        GetRouteResponseDTO getRouteResponseDTO = routeDAO.getRouteByGeoPoints(geoPoints);
        if(!ResultMessageController.isSuccess(getRouteResponseDTO.getResultMessage())){
            messageToShow = activity.getString(R.string.Message_UnknownError);
            showErrorMessage(messageToShow);
            return;
        }


        List<GetRouteLegResponseDTO> listRouteLeg = getRouteResponseDTO.getTracks();
        RouteLegModel routeLeg = dtoToModel(listRouteLeg.get(0));
        List<RouteLegModel> route = pianificaItinerarioModel.getRouteLegs();

        route.remove(index+1);
        route.set(index,routeLeg);
    }

    private void updateRouteWithSetPoint(int index) {
        Activity activity = getActivity();
        String messageToShow = null;

        if(!getModel().isValidIndexPoint(index)){
            Log.e(TAG, "ERROR1");
            messageToShow = activity.getString(R.string.Message_UnknownError);
            showErrorMessage(messageToShow);
            return;
        }

        AddressModel startingAddressRoute = null;
        AddressModel intermediateAddressRoute = null;
        AddressModel destinationAddressRoute = null;

        if(index == STARTING_POINT_CODE){
            startingAddressRoute = pianificaItinerarioModel.getStartingPoint();
            if(pianificaItinerarioModel.hasIntermediatePoints()){
                destinationAddressRoute = pianificaItinerarioModel.getIntermediatePoint(0);
            }
            else if(pianificaItinerarioModel.hasDestinationPoint()){
                destinationAddressRoute = pianificaItinerarioModel.getDestinationPoint();
            }
            else return;

        }

        else if(index == DESTINATION_POINT_CODE){
            destinationAddressRoute = pianificaItinerarioModel.getDestinationPoint();
            if(pianificaItinerarioModel.hasIntermediatePoints()){
                startingAddressRoute = pianificaItinerarioModel.getIntermediatePoint(pianificaItinerarioModel.getIntermediatePointsSize()-1);
            }
            else if(pianificaItinerarioModel.hasStartingPoint()){
                startingAddressRoute = pianificaItinerarioModel.getStartingPoint();
            }
            else return;

        }
        else{
            if(index - 1 < 0) startingAddressRoute = pianificaItinerarioModel.getStartingPoint();
            else startingAddressRoute = pianificaItinerarioModel.getIntermediatePoint(index - 1);

            intermediateAddressRoute = pianificaItinerarioModel.getIntermediatePoint(index);

            if(index + 1 > pianificaItinerarioModel.getIntermediatePointsSize() - 1) destinationAddressRoute = pianificaItinerarioModel.getDestinationPoint();
            else destinationAddressRoute = pianificaItinerarioModel.getIntermediatePoint(index + 1);
        }



        List<GeoPoint> geoPoints = new ArrayList<GeoPoint>();
        geoPoints.add(startingAddressRoute.getPoint());
        if(intermediateAddressRoute != null) geoPoints.add(intermediateAddressRoute.getPoint());
        geoPoints.add(destinationAddressRoute.getPoint());

        GetRouteResponseDTO getRouteResponseDTO = routeDAO.getRouteByGeoPoints(geoPoints);
        if(!ResultMessageController.isSuccess(getRouteResponseDTO.getResultMessage())){
            Log.e(TAG, "ERROR2");
            messageToShow = activity.getString(R.string.Message_UnknownError);
            showErrorMessage(messageToShow);
            return;
        }

        List<GetRouteLegResponseDTO> list1RouteLegDTO = getRouteResponseDTO.getTracks();
        List<GetRouteLegResponseDTO> list2RouteLegDTO = getRouteResponseDTO.getTracks();

        RouteLegModel routeLeg0 = dtoToModel(list1RouteLegDTO.get(0));
        RouteLegModel routeLeg1 = null;
        if(intermediateAddressRoute != null) routeLeg1 = dtoToModel(list1RouteLegDTO.get(1));

        List<RouteLegModel> route = pianificaItinerarioModel.getRouteLegs();

        if(index == STARTING_POINT_CODE) {
            if(route.isEmpty()) route.add(routeLeg0);
            else route.set(0,routeLeg0);
            return;
        }
        if(index == DESTINATION_POINT_CODE){
            if(route.isEmpty()) route.add(routeLeg0);
            route.set(pianificaItinerarioModel.getRouteLegsSize()-1,routeLeg0);
            return;
        }

        route.set(index, routeLeg0);
        route.set(index+1, routeLeg1);
    }

    private void updateRouteWithAddPoint() {

        AddressModel startingAddressRoute;
        if(pianificaItinerarioModel.getIntermediatePointsSize() - 1 > 0) startingAddressRoute = pianificaItinerarioModel.getIntermediatePoint(pianificaItinerarioModel.getIntermediatePointsSize()-2);
        else startingAddressRoute = pianificaItinerarioModel.getStartingPoint();
        AddressModel intermediateAddressRoute = pianificaItinerarioModel.getIntermediatePoint(pianificaItinerarioModel.getIntermediatePointsSize()-1);
        AddressModel destinationAddressRoute = pianificaItinerarioModel.getDestinationPoint();

        List<GeoPoint> geoPoints = new ArrayList<GeoPoint>();
        geoPoints.add(startingAddressRoute.getPoint());
        geoPoints.add(intermediateAddressRoute.getPoint());
        geoPoints.add(destinationAddressRoute.getPoint());
        GetRouteResponseDTO getRouteResponseDTO = routeDAO.getRouteByGeoPoints(geoPoints);

        List<GetRouteLegResponseDTO> listRouteLeg = getRouteResponseDTO.getTracks();


        RouteLegModel routeLeg0 = dtoToModel(listRouteLeg.get(0));
        RouteLegModel routeLeg1 = dtoToModel(listRouteLeg.get(1));

        List<RouteLegModel> route = pianificaItinerarioModel.getRouteLegs();

        route.remove(pianificaItinerarioModel.getRouteLegsSize()-1);
        route.add(routeLeg0);
        route.add(routeLeg1);
    }


    private void setPointSelectedOnMap(GeoPoint geoPoint) {
        Activity activity = getActivity();
        String messageToShow = null;

        if(geoPoint == null) return;

        GetAddressResponseDTO addressDTO = addressDAO.getAddressByGeoPoint(geoPoint);
        if(!ResultMessageController.isSuccess(addressDTO.getResultMessage())){
            messageToShow = activity.getString(R.string.Message_UnknownError);
            showErrorMessage(messageToShow);
            return;
        }

        AddressModel addressModel = new AddressModel();

        boolean result = AddressMapper.dtoToModel(addressDTO, addressModel);
        if(!result){
            //TODO error
            return;
        }

        pianificaItinerarioModel.setPointSelectedOnMap(addressModel);

    }


    private RouteLegModel dtoToModel(GetRouteLegResponseDTO dto){
        RouteLegModel routeLegModel = new RouteLegModel();

        routeLegModel.setStartingPoint(dto.getStartingPoint());
        routeLegModel.setDestinationPoint(dto.getDestinationPoint());
        routeLegModel.setDistance(dto.getDistance());
        routeLegModel.setDuration(dto.getDuration());
        routeLegModel.setTrack(dto.getTrack());

        return routeLegModel;
    }

    private List<RouteLegModel> dtoToModel(List<GetRouteLegResponseDTO> tracks) {

        List<RouteLegModel> listRouteLeg = new ArrayList<RouteLegModel>();
        for(GetRouteLegResponseDTO routeLegDTO : tracks){
            RouteLegModel routeLegModel = dtoToModel(routeLegDTO);

            listRouteLeg.add(routeLegModel);
        }

        return listRouteLeg;
    }



    protected PianificaItinerarioController(Parcel in) {
        super();
    }

    public static final Creator<PianificaItinerarioController> CREATOR = new Creator<PianificaItinerarioController>() {
        @Override
        public PianificaItinerarioController createFromParcel(Parcel in) {
            return new PianificaItinerarioController(in);
        }

        @Override
        public PianificaItinerarioController[] newArray(int size) {
            return new PianificaItinerarioController[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }

}
