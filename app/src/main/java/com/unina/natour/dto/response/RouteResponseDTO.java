package com.unina.natour.dto.response;

import com.unina.natour.dto.response.MessageResponseDTO;
import com.unina.natour.models.RouteLegModel;

import org.osmdroid.util.GeoPoint;

import java.util.List;

public class RouteResponseDTO {

    private MessageResponseDTO resultMessage;

    private List<GeoPoint> wayPoints;
    private List<RouteLegModel> routeLegs;


    public MessageResponseDTO getResultMessage() {
        return resultMessage;
    }

    public void setResultMessage(MessageResponseDTO resultMessage) {
        this.resultMessage = resultMessage;
    }

    public List<GeoPoint> getWayPoints() {
        return wayPoints;
    }

    public void setWayPoints(List<GeoPoint> wayPoints) {
        this.wayPoints = wayPoints;
    }

    public List<RouteLegModel> getRouteLegs() {
        return routeLegs;
    }

    public void setRouteLegs(List<RouteLegModel> routeLegs) {
        this.routeLegs = routeLegs;
    }
}
