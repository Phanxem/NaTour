package com.unina.natour.controllers;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.widget.ListView;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

import com.unina.natour.controllers.utils.AddressMapper;
import com.unina.natour.controllers.utils.GPSUtils;
import com.unina.natour.dto.response.GetListAddressResponseDTO;
import com.unina.natour.dto.response.GetAddressResponseDTO;
import com.unina.natour.dto.response.ResultMessageDTO;
import com.unina.natour.models.AddressModel;
import com.unina.natour.models.RicercaPuntoModel;
import com.unina.natour.models.dao.implementation.AddressDAOImpl;
import com.unina.natour.models.dao.interfaces.AddressDAO;
import com.unina.natour.views.activities.NaTourActivity;
import com.unina.natour.views.activities.RicercaPuntoActivity;
import com.unina.natour.views.listAdapters.RisultatiRicercaPuntoListAdapter;

import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;
import java.util.List;

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
            //TODO
            showErrorMessage(0);
            return;
        }

        if(!GPSUtils.isGPSEnabled(getActivity())){
            //TODO
            showErrorMessage(0);
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
            //CurrentLocationNotFoundException exception = new CurrentLocationNotFoundException();
            //TODO
            showErrorMessage(0);
            return;
        }

        GeoPoint geoPoint = new GeoPoint(location.getLatitude(),location.getLongitude());

        GetAddressResponseDTO addressDTO = addressDAO.getAddressByGeoPoint(geoPoint);
        ResultMessageDTO resultMessageDTO = addressDTO.getResultMessage();
        if(resultMessageDTO.getCode() != ResultMessageController.SUCCESS_CODE){
            showErrorMessage(resultMessageDTO);
            return;
        }
        AddressModel addressModel = new AddressModel();

        boolean result = AddressMapper.dtoToModel(addressDTO, addressModel);
        if(!result){
            //TODO error
            showErrorMessage(resultMessageDTO);
            return;
        }

        Intent intent = new Intent();
        intent.putExtra(EXTRA_ADDRESS, addressModel);
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
            ricercaPuntoModel.clearAndNotify();
            risultatiRicercaPuntoListAdapter.notifyDataSetChanged();
            return;
        }
        GetListAddressResponseDTO resultAddressesDTO = addressDAO.getAddressesByQuery(searchString);
        ResultMessageDTO resultMessageDTO = resultAddressesDTO.getResultMessage();
        if(resultMessageDTO.getCode() != ResultMessageController.SUCCESS_CODE){
            showErrorMessage(resultMessageDTO);
            return;
        }

        List<AddressModel> addressModels = new ArrayList<AddressModel>();
        boolean result = AddressMapper.dtoToModel(resultAddressesDTO, addressModels);
        if(!result){
            //todo
            showErrorMessage(0);
            return;
        }

        ricercaPuntoModel.setResultPoints(addressModels);
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
