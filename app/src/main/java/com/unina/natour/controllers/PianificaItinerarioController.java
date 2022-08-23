package com.unina.natour.controllers;

import android.Manifest;
import android.annotation.SuppressLint;
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
import androidx.fragment.app.FragmentActivity;
import androidx.preference.PreferenceManager;

import com.unina.natour.controllers.exceptionHandler.ExceptionHandler;
import com.unina.natour.controllers.exceptionHandler.exceptions.ServerException;
import com.unina.natour.controllers.utils.GPSUtils;
import com.unina.natour.dto.RouteDTO;
import com.unina.natour.models.AddressModel;
import com.unina.natour.models.PianificaItinerarioModel;
import com.unina.natour.models.RouteLegModel;
import com.unina.natour.models.dao.implementation.AddressDAOImpl;
import com.unina.natour.models.dao.implementation.RouteDAOImpl;
import com.unina.natour.models.dao.interfaces.AddressDAO;
import com.unina.natour.models.dao.interfaces.RouteDAO;
import com.unina.natour.views.activities.ImportaFileGPXActivity;
import com.unina.natour.views.activities.RicercaPuntoActivity;
import com.unina.natour.views.dialogs.MessageDialog;
import com.unina.natour.views.listAdapters.PuntiIntermediListAdapter;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.CustomZoomButtonsController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.Projection;
import org.osmdroid.views.overlay.Overlay;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@SuppressLint("LongLogTag")
@RequiresApi(api = Build.VERSION_CODES.N)
public class PianificaItinerarioController implements Parcelable {

    public final static Integer STARTING_POINT_CODE = -2;
    public final static Integer DESTINATION_POINT_CODE = -1;

    public final static GeoPoint DEFAULT_CENTER_POINT = new GeoPoint((double) 0, (double) 0);

    public static final int REQUEST_CODE = 0;
    private static final String TAG = "PianificaItinerarioController";

    FragmentActivity activity;
    MessageDialog messageDialog;


    RicercaPuntoController ricercaPuntoController;

    ActivityResultLauncher<Intent> activityResultLauncherRicercaPunto;
    ActivityResultLauncher<Intent> activityResultLauncherImportaFileGPX;
    ActivityResultLauncher<String> activityResultLauncherPermissions;


    PuntiIntermediListAdapter puntiIntermediListAdapter;
    PianificaItinerarioModel pianificaItinerarioModel;


    RouteDAO routeDAO;
    AddressDAO addressDAO;



    public PianificaItinerarioController(FragmentActivity activity){
        this.activity = activity;
        this.messageDialog = new MessageDialog(activity);

        this.ricercaPuntoController = new RicercaPuntoController(activity);

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

                                RouteDTO routeDTO = null;

                                try {
                                    routeDTO = routeDAO.findRouteByGeoPoints(geoPoints);
                                }
                                catch (ServerException e) {
                                    ExceptionHandler.handleMessageError(messageDialog, e);
                                }
                                //TODO per le successive eccezioni definire delle eccezioni eprsonalizzate da passare all'handler
                                catch (InterruptedException e) {
                                    ExceptionHandler.handleMessageError(messageDialog, e);
                                }
                                catch (ExecutionException e){
                                    ExceptionHandler.handleMessageError(messageDialog, e);
                                }
                                catch (IOException e) {
                                    ExceptionHandler.handleMessageError(messageDialog, e);
                                }


                                pianificaItinerarioModel.updateRoutes(routeDTO.getRouteLegs());
                                puntiIntermediListAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                }
        );

        this.activityResultLauncherPermissions = activity.registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                new ActivityResultCallback<Boolean>() {
                    @Override
                    public void onActivityResult(Boolean result) {
                        if (result) {
                            openImportaFileGPXActivity();
                            Log.i("ACCEPT", "acceptated tutt cos");
                            return;
                        }
                        Log.i("NOT ACCEPT", "acceptaNOT ted NOT tutt cos");
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
    }

    public void initMap(MapView mapView){
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

            @SuppressLint("LongLogTag")
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

        if(GPSUtils.hasGPSFeature(activity) &&
           GPSUtils.isGPSEnabled(activity) &&
           ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            LocationManager locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            if(location == null) location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            if(location != null) centerPoint = new GeoPoint(location.getLatitude(),location.getLongitude());
        }

        mapController.setCenter(centerPoint);

    }

    public void initItermediatePointsList(ListView listView_puntiIntermedi) {
        listView_puntiIntermedi.setAdapter(puntiIntermediListAdapter);
    }

    public void initOsmdroidConfiguration(){
        Context appContext = activity.getApplicationContext();
        Configuration.getInstance().load(appContext, PreferenceManager.getDefaultSharedPreferences(appContext));
    }





    public void selectStartingPoint() {
        pianificaItinerarioModel.setIndexPointSelected(STARTING_POINT_CODE);
        openRicercaPuntoActivity();
    }

    public void selectDestinationPoint() {
        pianificaItinerarioModel.setIndexPointSelected(DESTINATION_POINT_CODE);
        openRicercaPuntoActivity();
    }

    public void addIntermediatePoint() {
        int indexIntermediatePoint = pianificaItinerarioModel.getIntermediatePointsSize();

        pianificaItinerarioModel.setIndexPointSelected(indexIntermediatePoint);
        openRicercaPuntoActivity();
    }

    public void selectIntermediatePoint(int index) {
        if(!pianificaItinerarioModel.isValidIndexPoint(index)) return;

        pianificaItinerarioModel.setIndexPointSelected(index);
        openRicercaPuntoActivity();
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
        AddressModel address = pianificaItinerarioModel.getPointSelectedOnMap();

        if(address == null){
            //TODO ERRORE
            return;
        }

        pianificaItinerarioModel.setStartingPoint(address);
        updateRouteWithSetPoint(STARTING_POINT_CODE);
        pianificaItinerarioModel.removeIndexPointSelected();
        pianificaItinerarioModel.removePointSelectedOnMap();
    }

    public void setSelectedPointOnMapAsDestinationPoint() {

        AddressModel address = pianificaItinerarioModel.getPointSelectedOnMap();

        if(address == null){
            //TODO ERRORE
            return;
        }

        pianificaItinerarioModel.setDestinationPoint(address);
        updateRouteWithSetPoint(DESTINATION_POINT_CODE);
        pianificaItinerarioModel.removeIndexPointSelected();
        pianificaItinerarioModel.removePointSelectedOnMap();

    }

    public void setSelectedPointOnMapAsIntermediatePoint() {
        AddressModel address = pianificaItinerarioModel.getPointSelectedOnMap();
        Integer index = pianificaItinerarioModel.getIndexPointSelected();

        if(address == null){
            //TODO ERRORE
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
        Integer indexPointSelected = pianificaItinerarioModel.getIndexPointSelected();
        if(indexPointSelected == null){
            //TODO ERROR
            return;
        }

        if(indexPointSelected.equals(STARTING_POINT_CODE)){
            Log.e(TAG, "Start");
            pianificaItinerarioModel.setStartingPoint(address);
            updateRouteWithSetPoint(STARTING_POINT_CODE);
            return;
        }
        if(indexPointSelected.equals(DESTINATION_POINT_CODE)){
            Log.e(TAG, "Destination");
            pianificaItinerarioModel.setDestinationPoint(address);
            updateRouteWithSetPoint(DESTINATION_POINT_CODE);
            return;
        }
        if(indexPointSelected >= 0 && indexPointSelected < pianificaItinerarioModel.getIntermediatePointsSize()){
            Log.e(TAG, "Intermediate " + indexPointSelected);
            pianificaItinerarioModel.setIntermediatePoint(indexPointSelected,address);
            updateRouteWithSetPoint(indexPointSelected);
            return;
        }
        if(indexPointSelected == pianificaItinerarioModel.getIntermediatePointsSize()){
            Log.e(TAG, "New Intermediate");
            pianificaItinerarioModel.addIntermediatePoint(address);
            updateRouteWithAddPoint();
            return;
        }


        Log.e(TAG, "indice lista errato");
    }


    private void updateRouteWithRemovePoint(int index) {
        if(!getModel().isValidIndexPoint(index)){
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
        RouteDTO routeDTO = null;
        try {
            routeDTO = routeDAO.findRouteByGeoPoints(geoPoints);
        }
        catch (UnknownException e) {
            e.printStackTrace();
        }
        catch (ServerException e) {
            e.printStackTrace();
        }
        RouteLegModel routeLeg = routeDTO.getRouteLegs().get(0);


        List<RouteLegModel> route = pianificaItinerarioModel.getRouteLegs();

        route.remove(index+1);
        route.set(index,routeLeg);
    }

    private void updateRouteWithSetPoint(int index) {

        if(!getModel().isValidIndexPoint(index)){
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

        RouteDTO routeDTO = null;
        try {
            routeDTO = routeDAO.findRouteByGeoPoints(geoPoints);
        }
        catch (UnknownException e) {
            e.printStackTrace();
        }
        catch (ServerException e) {
            e.printStackTrace();
        }
        RouteLegModel routeLeg0 = routeDTO.getRouteLegs().get(0);
        RouteLegModel routeLeg1 = null;
        if(intermediateAddressRoute != null) routeLeg1 = routeDTO.getRouteLegs().get(1);

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
        RouteDTO routeDTO = null;
        try {
            routeDTO = routeDAO.findRouteByGeoPoints(geoPoints);
        }
        catch (UnknownException e) {
            e.printStackTrace();
        }
        catch (ServerException e) {
            e.printStackTrace();
        }
        RouteLegModel routeLeg0 = routeDTO.getRouteLegs().get(0);
        RouteLegModel routeLeg1 = routeDTO.getRouteLegs().get(1);

        List<RouteLegModel> route = pianificaItinerarioModel.getRouteLegs();

        route.remove(pianificaItinerarioModel.getRouteLegsSize()-1);
        route.add(routeLeg0);
        route.add(routeLeg1);
    }


//---

    public PianificaItinerarioModel getModel() {
        return pianificaItinerarioModel;
    }

    private void setPointSelectedOnMap(GeoPoint geoPoint) {
        if(geoPoint == null) return;

        AddressModel address = null;
        try {
            address = addressDAO.findAddressByGeoPoint(geoPoint);
        }
        catch (ServerException e) {
            e.printStackTrace();
        }
        catch (UnknownException e) {
            e.printStackTrace();
        }
        if(address == null) return;

        pianificaItinerarioModel.setPointSelectedOnMap(address);

    }






    //---


    public void openRicercaPuntoActivity(){
        Intent intent = new Intent(activity, RicercaPuntoActivity.class);
        activityResultLauncherRicercaPunto.launch(intent);
    }

    public void openImportaFileGPXActivity(){

        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            activityResultLauncherPermissions.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
            return;
        }

        Intent intent = new Intent(activity, ImportaFileGPXActivity.class);
        activityResultLauncherImportaFileGPX.launch(intent);
    }





























    //---

    protected PianificaItinerarioController(Parcel in) {
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