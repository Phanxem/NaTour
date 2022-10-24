package com.unina.natour.models.dao.interfaces;

import com.unina.natour.dto.response.GetAddressResponseDTO;
import com.unina.natour.dto.response.GetListAddressResponseDTO;

import org.osmdroid.util.GeoPoint;

public interface AddressDAO {

    public GetAddressResponseDTO getAddressByGeoPoint(GeoPoint geoPoint);

    public GetListAddressResponseDTO getAddressesByQuery(String query);

}
