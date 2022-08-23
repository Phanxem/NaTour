package com.unina.natour.controllers;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;
import android.widget.ListView;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.unina.natour.controllers.exceptionHandler.exceptions.ServerException;
import com.unina.natour.controllers.utils.GPSUtils;
import com.unina.natour.models.AddressModel;
import com.unina.natour.models.RicercaPuntoModel;
import com.unina.natour.models.dao.implementation.AddressDAOImpl;
import com.unina.natour.models.dao.interfaces.AddressDAO;
import com.unina.natour.views.dialogs.MessageDialog;
import com.unina.natour.views.listAdapters.RisultatiRicercaPuntoListAdapter;

import org.osmdroid.util.GeoPoint;

import java.util.List;

public class RicercaPuntoController {
    public static final int REQUEST_CODE = 100;

    public static final int RESULT_CODE_RETURN_POINT = 100;
    public static final int RESULT_CODE_GET_FROM_MAP = 101;

    public final static String EXTRA_ADDRESS = "ADDRESS";


    FragmentActivity activity;
    MessageDialog messageDialog;
    RisultatiRicercaPuntoListAdapter risultatiRicercaPuntoListAdapter;

    ActivityResultLauncher<String> activityResultLauncherPermissions;

    RicercaPuntoModel ricercaPuntoModel;

    AddressDAO addressDAO;



    public RicercaPuntoController(FragmentActivity activity){
        this.activity = activity;
        this.messageDialog = new MessageDialog(activity);

        this.ricercaPuntoModel = new RicercaPuntoModel();

        this.risultatiRicercaPuntoListAdapter = new RisultatiRicercaPuntoListAdapter(
                activity,
                ricercaPuntoModel.getResultPoints(),
                this
        );

        this.activityResultLauncherPermissions = activity.registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                new ActivityResultCallback<Boolean>() {
                    @Override
                    public void onActivityResult(Boolean result) {
                        if (result) {
                            selectCurrentPosition();
                            Log.i("ACCEPT", "acceptated tutt cos");
                            return;
                        }
                        Log.i("NOT ACCEPT", "acceptaNOT ted NOT tutt cos");
                    }
                }
        );



        this.addressDAO = new AddressDAOImpl();
    }

    public void initListViewResultPoints(ListView listView_risultatiPunti) {
        listView_risultatiPunti.setAdapter(risultatiRicercaPuntoListAdapter);
    }

    public RicercaPuntoModel getModel() {
        return ricercaPuntoModel;
    }

    public void selectCurrentPosition() {
        if(!GPSUtils.hasGPSFeature(activity)){
            //TODO ERRORE
            Log.e("RIC_PUNT: ", "GPS NOT PRESENT");
            return;
        }

        if(!GPSUtils.isGPSEnabled(activity)){
            //TODO ERRORE
            Log.e("RIC_PUNT: ", "GPS DISABLED");
            return;
        }

        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.e("RIC_PUNT: ", "GPS PERMISSION REQUIRED");
            activityResultLauncherPermissions.launch(Manifest.permission.ACCESS_FINE_LOCATION);
            return;
        }

        LocationManager locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        if(location == null) location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        if(location == null){
            //TODO ERRORE
            Log.e("RIC_PUNT: ", "location null");
            return;
        }

        GeoPoint geoPoint = new GeoPoint(location.getLatitude(),location.getLongitude());
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

        Intent intent = new Intent();
        intent.putExtra(EXTRA_ADDRESS, address);
        activity.setResult(RESULT_CODE_RETURN_POINT, intent);
        activity.finish();
        return;
    }

    public void selectFromMap() {
        Intent intent = new Intent();
        activity.setResult(RESULT_CODE_GET_FROM_MAP, intent);
        activity.finish();
    }

    public void searchInterestPoint(String searchString) {
        if(searchString.isEmpty()){
            ricercaPuntoModel.clear();
            risultatiRicercaPuntoListAdapter.notifyDataSetChanged();
            return;
        }
        List<AddressModel> resultAddresses = null;
        try {
            resultAddresses = addressDAO.findAddressesByQuery(searchString);
        }
        catch (ServerException e) {
            e.printStackTrace();
        }
        catch (UnknownException e) {
            e.printStackTrace();
        }
        ricercaPuntoModel.setResultPoints(resultAddresses);
        risultatiRicercaPuntoListAdapter.notifyDataSetChanged();
    }

    public void selectResultPoint(AddressModel resultAddress){
        Intent intent = new Intent();
        intent.putExtra(EXTRA_ADDRESS, resultAddress);
        activity.setResult(RESULT_CODE_RETURN_POINT, intent);
        activity.finish();

    }
}
