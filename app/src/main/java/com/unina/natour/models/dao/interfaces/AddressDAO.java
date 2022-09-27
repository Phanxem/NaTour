package com.unina.natour.models.dao.interfaces;

import com.unina.natour.controllers.exceptionHandler.exceptions.ServerException;
import com.unina.natour.dto.response.AddressResponseDTO;
import com.unina.natour.dto.response.AddressListResponseDTO;

import org.osmdroid.util.GeoPoint;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public interface AddressDAO {

    AddressResponseDTO findAddressByGeoPoint(GeoPoint geoPoint);

    AddressListResponseDTO findAddressesByQuery(String query);

}
