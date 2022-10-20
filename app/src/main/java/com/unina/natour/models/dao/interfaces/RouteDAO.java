package com.unina.natour.models.dao.interfaces;

import com.unina.natour.dto.response.GetRouteResponseDTO;

import org.osmdroid.util.GeoPoint;

import java.util.List;

public interface RouteDAO {

    //GETs
    public GetRouteResponseDTO getRouteByCoordinates(String coordinates);

    public GetRouteResponseDTO getRouteByGeoPoints(List<GeoPoint> geoPoints);
}
