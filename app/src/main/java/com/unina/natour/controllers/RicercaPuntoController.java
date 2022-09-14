package com.unina.natour.controllers;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Parcelable;
import android.util.Log;
import android.widget.ListView;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.unina.natour.controllers.exceptionHandler.ExceptionHandler;
import com.unina.natour.controllers.exceptionHandler.exceptions.ServerException;
import com.unina.natour.controllers.exceptionHandler.exceptions.subAppException.CurrentLocationNotFoundException;
import com.unina.natour.controllers.exceptionHandler.exceptions.subAppException.FailureFindAddressException;
import com.unina.natour.controllers.exceptionHandler.exceptions.subAppException.GPSFeatureNotPresentException;
import com.unina.natour.controllers.exceptionHandler.exceptions.subAppException.GPSNotEnabledException;
import com.unina.natour.controllers.exceptionHandler.exceptions.subAppException.GPSPermissionNotGrantedException;
import com.unina.natour.controllers.exceptionHandler.exceptions.subAppException.NotCompletedFindAddressException;
import com.unina.natour.controllers.utils.GPSUtils;
import com.unina.natour.models.AddressModel;
import com.unina.natour.models.PianificaItinerarioModel;
import com.unina.natour.models.RicercaPuntoModel;
import com.unina.natour.models.dao.implementation.AddressDAOImpl;
import com.unina.natour.models.dao.interfaces.AddressDAO;
import com.unina.natour.views.activities.NaTourActivity;
import com.unina.natour.views.activities.RicercaPuntoActivity;
import com.unina.natour.views.dialogs.MessageDialog;
import com.unina.natour.views.listAdapters.RisultatiRicercaPuntoListAdapter;

import org.osmdroid.util.GeoPoint;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

@RequiresApi(api = Build.VERSION_CODES.N)
public class RicercaPuntoController extends NaTourController{
    public static final int REQUEST_CODE = 100;

    public static final int RESULT_CODE_RETURN_POINT = 100;
    public static final int RESULT_CODE_GET_FROM_MAP = 101;

    public final static String EXTRA_ADDRESS = "ADDRESS";

    RisultatiRicercaPuntoListAdapter risultatiRicercaPuntoListAdapter;

    ActivityResultLauncher<String> activityResultLauncherPermissions;

    RicercaPuntoModel ricercaPuntoModel;

    AddressDAO addressDAO;




    public RicercaPuntoController(NaTourActivity activity){
        super(activity);

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
                        if (result) selectCurrentPosition();
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
        if(!GPSUtils.hasGPSFeature(getActivity())){
            GPSFeatureNotPresentException exception = new GPSFeatureNotPresentException();
            ExceptionHandler.handleMessageError(getMessageDialog(),exception);
            return;
        }

        if(!GPSUtils.isGPSEnabled(getActivity())){
            GPSNotEnabledException exception = new GPSNotEnabledException();
            ExceptionHandler.handleMessageError(getMessageDialog(),exception);
            return;
        }

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            activityResultLauncherPermissions.launch(Manifest.permission.ACCESS_FINE_LOCATION);
            return;
        }

        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        if(location == null) location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        if(location == null){
            CurrentLocationNotFoundException exception = new CurrentLocationNotFoundException();
            ExceptionHandler.handleMessageError(getMessageDialog(),exception);
            return;
        }

        GeoPoint geoPoint = new GeoPoint(location.getLatitude(),location.getLongitude());
        AddressModel address = null;

        try {
            address = addressDAO.findAddressByGeoPoint(geoPoint);
        }
        catch (ServerException e) {
            ExceptionHandler.handleMessageError(getMessageDialog(),e);
            return;
        }
        catch (IOException e) {
            FailureFindAddressException exception = new FailureFindAddressException(e);
            ExceptionHandler.handleMessageError(getMessageDialog(),exception);
            return;
        }
        catch (ExecutionException | InterruptedException e) {
            NotCompletedFindAddressException exception = new NotCompletedFindAddressException(e);
            ExceptionHandler.handleMessageError(getMessageDialog(),exception);
            return;
        }
        if(address == null){
            FailureFindAddressException exception = new FailureFindAddressException();
            ExceptionHandler.handleMessageError(getMessageDialog(),exception);
            return;
        }

        Intent intent = new Intent();
        intent.putExtra(EXTRA_ADDRESS, address);
        getActivity().setResult(RESULT_CODE_RETURN_POINT, intent);
        getActivity().finish();
    }

    public void selectFromMap() {
        Intent intent = new Intent();
        getActivity().setResult(RESULT_CODE_GET_FROM_MAP, intent);
        getActivity().finish();
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
            ExceptionHandler.handleMessageError(getMessageDialog(),e);
            return;
        }
        catch (IOException e) {
            FailureFindAddressException exception = new FailureFindAddressException(e);
            ExceptionHandler.handleMessageError(getMessageDialog(),exception);
            return;
        }
        catch (ExecutionException | InterruptedException e) {
            NotCompletedFindAddressException exception = new NotCompletedFindAddressException(e);
            ExceptionHandler.handleMessageError(getMessageDialog(),exception);
            return;
        }
        if(resultAddresses == null){
            FailureFindAddressException exception = new FailureFindAddressException();
            ExceptionHandler.handleMessageError(getMessageDialog(),exception);
            return;
        }
        ricercaPuntoModel.setResultPoints(resultAddresses);
        risultatiRicercaPuntoListAdapter.notifyDataSetChanged();
    }

    public void selectResultPoint(AddressModel resultAddress){
        Intent intent = new Intent();
        intent.putExtra(EXTRA_ADDRESS, resultAddress);
        getActivity().setResult(RESULT_CODE_RETURN_POINT, intent);
        getActivity().finish();

    }


    public static void openRicercaPuntoActivity(NaTourActivity fromActivity, ActivityResultLauncher<Intent> activityResultLauncher){
        Intent intent = new Intent(fromActivity, RicercaPuntoActivity.class);
        activityResultLauncher.launch(intent);
        //fromActivity.startActivity(intent);
    }
}
