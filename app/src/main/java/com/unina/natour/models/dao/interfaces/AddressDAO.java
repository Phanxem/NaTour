package com.unina.natour.models.dao.interfaces;

import com.unina.natour.controllers.exceptionHandler.exceptions.ServerException;
import com.unina.natour.models.AddressModel;

import org.osmdroid.util.GeoPoint;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

public interface AddressDAO {

    AddressModel findAddressByGeoPoint(GeoPoint geoPoint) throws ServerException, IOException, ExecutionException, InterruptedException;

    List<AddressModel> findAddressesByQuery(String query) throws ServerException, IOException, ExecutionException, InterruptedException;

}
