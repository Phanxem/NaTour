package com.unina.natour.dto.response;

import org.osmdroid.util.GeoPoint;

import java.util.List;

public class GetRouteResponseDTO {

    private ResultMessageDTO resultMessage;

    private List<GeoPoint> wayPoints;
    private List<GetRouteLegResponseDTO> tracks;


    public ResultMessageDTO getResultMessage() {
        return resultMessage;
    }

    public void setResultMessage(ResultMessageDTO resultMessage) {
        this.resultMessage = resultMessage;
    }

    public List<GeoPoint> getWayPoints() {
        return wayPoints;
    }

    public void setWayPoints(List<GeoPoint> wayPoints) {
        this.wayPoints = wayPoints;
    }

    public List<GetRouteLegResponseDTO> getTracks() {
        return tracks;
    }

    public void setTracks(List<GetRouteLegResponseDTO> tracks) {
        this.tracks = tracks;
    }
}
