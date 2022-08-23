package com.unina.natour.dto;

import com.unina.natour.models.RouteLegModel;

import org.osmdroid.util.GeoPoint;

import java.util.List;

public class RouteDTO {

    private List<GeoPoint> wayPoints;
    private List<RouteLegModel> routeLegs;

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
