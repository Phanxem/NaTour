package com.unina.natour.models.dao.interfaces;

import com.unina.natour.controllers.exceptionHandler.exceptions.ServerException;
import com.unina.natour.dto.response.RouteResponseDTO;

import org.osmdroid.util.GeoPoint;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

public interface RouteDAO {
    RouteResponseDTO findRouteByCoordinates(String coordinates);

    RouteResponseDTO findRouteByGeoPoints(List<GeoPoint> geoPoints);
}
