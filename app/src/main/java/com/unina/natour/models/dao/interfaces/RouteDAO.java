package com.unina.natour.models.dao.interfaces;

import com.unina.natour.controllers.exceptionHandler.exceptions.ServerException;
import com.unina.natour.dto.RouteDTO;

import org.osmdroid.util.GeoPoint;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

public interface RouteDAO extends ServerDAO{
    RouteDTO findRouteByCoordinates(String coordinates) throws ServerException, IOException, ExecutionException, InterruptedException;

    RouteDTO findRouteByGeoPoints(List<GeoPoint> geoPoints) throws ServerException, InterruptedException, ExecutionException, IOException;
}
