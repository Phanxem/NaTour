package com.unina.natour.models.dao.interfaces;

import com.unina.natour.dto.response.GetAddressResponseDTO;
import com.unina.natour.dto.response.GetListAddressResponseDTO;

import org.osmdroid.util.GeoPoint;

public interface AddressDAO {

    GetAddressResponseDTO getAddressByGeoPoint(GeoPoint geoPoint);

    GetListAddressResponseDTO getAddressesByQuery(String query);

}
