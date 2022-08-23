package com.unina.natour.models.dao.converters;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.unina.natour.controllers.exceptionHandler.exceptions.ServerException;
import com.unina.natour.models.AddressModel;
import com.unina.natour.dto.MessageDTO;
import com.unina.natour.dto.RouteDTO;
import com.unina.natour.models.RouteLegModel;
import com.unina.natour.dto.UserDTO;

import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;
import java.util.List;

public class JsonConverter {

    private static final String TAG = "ASF";

    public static MessageDTO toMessageDTO(JsonObject jsonObject) throws ServerException {

        if(!jsonObject.has("code")  || !jsonObject.has("message")){
            throw new ServerException();
        }

        long code = jsonObject.get("code").getAsLong();
        String message = jsonObject.get("message").getAsString();

        MessageDTO messageDTO = new MessageDTO(code,message);

        return messageDTO;
    }

    public static UserDTO toUserDTO(JsonObject jsonObject) throws ServerException {
        if(!jsonObject.has("id")  ||
           !jsonObject.has("username") )
        {
            MessageDTO messageDTO  = toMessageDTO(jsonObject);
            throw new ServerException(messageDTO);
        }

        long id = jsonObject.get("id").getAsLong();
        String username = jsonObject.get("username").getAsString();

        UserDTO userDTO = new UserDTO();
        userDTO.setId(id);
        userDTO.setUsername(username);


        if(jsonObject.has("placeOfResidence")){
            String placeOfResidence = jsonObject.get("placeOfResidence").getAsString();
            userDTO.setPlaceOfResidence(placeOfResidence);
        }

        if(jsonObject.has("dateOfBirth")){
            String dateOfBirth = jsonObject.get("dateOfBirth").getAsString();
            userDTO.setDateOfBirth(dateOfBirth);
        }

        return userDTO;
    }

    public static AddressModel toAddressModel(JsonObject jsonObject) throws ServerException {
        if(!jsonObject.has("point")  ||
           !jsonObject.has("addressLine") )
        {
            MessageDTO messageDTO  = toMessageDTO(jsonObject);
            throw new ServerException(messageDTO);
        }

        JsonObject jsonObjectPoint = jsonObject.get("point").getAsJsonObject();

        double lon = jsonObjectPoint.get("lon").getAsDouble();
        double lat = jsonObjectPoint.get("lat").getAsDouble();
        GeoPoint geoPoint = new GeoPoint(lat,lon);

        String addressName = jsonObject.get("addressLine").getAsString();

        AddressModel addressModel = new AddressModel();
        addressModel.setPoint(geoPoint);
        addressModel.setAddressName(addressName);

        return addressModel;
    }

    public static List<AddressModel> toListAddressModel(JsonArray jsonArray) throws ServerException {

        List<AddressModel> listAddressModel = new ArrayList<AddressModel>();
        for(JsonElement jsonElement : jsonArray){
            JsonObject jsonObject = jsonElement.getAsJsonObject();

            AddressModel addressModel = toAddressModel(jsonObject);
            if(addressModel == null) return null;
            listAddressModel.add(addressModel);
        }

        return listAddressModel;
    }

    public static RouteDTO toRouteDTO(JsonObject jsonObject) throws ServerException {

        if(!jsonObject.has("wayPoints")  ||
           !jsonObject.has("tracks") )
        {
           MessageDTO messageDTO  = toMessageDTO(jsonObject);
           throw new ServerException(messageDTO);
        }

        JsonArray jsonArrayWayPoints = jsonObject.get("wayPoints").getAsJsonArray();
        JsonArray jsonArrayTracks = jsonObject.get("tracks").getAsJsonArray();

        List<GeoPoint> geoWayPoints = new ArrayList<GeoPoint>();
        for(JsonElement jsonElement : jsonArrayWayPoints){
            JsonObject jsonObjectWayPoint = jsonElement.getAsJsonObject();
            double lon = jsonObjectWayPoint.get("lon").getAsDouble();
            double lat = jsonObjectWayPoint.get("lat").getAsDouble();
            GeoPoint geoPoint = new GeoPoint(lat, lon);

            geoWayPoints.add(geoPoint);
        }

        List<RouteLegModel> routeLegs = new ArrayList<RouteLegModel>();
        for(JsonElement jsonElement : jsonArrayTracks){
            JsonObject jsonObjectTrack = jsonElement.getAsJsonObject();

            JsonObject jsonObjectStartingPoint = jsonObjectTrack.get("startingPoint").getAsJsonObject();
            double startingLon = jsonObjectStartingPoint.get("lon").getAsDouble();
            double startingLat = jsonObjectStartingPoint.get("lat").getAsDouble();
            GeoPoint startingGeoPoint = new GeoPoint(startingLat, startingLon);

            JsonObject jsonObjectDestinationPoint = jsonObjectTrack.get("destinationPoint").getAsJsonObject();
            double destinationLon = jsonObjectDestinationPoint.get("lon").getAsDouble();
            double destinationLat = jsonObjectDestinationPoint.get("lat").getAsDouble();
            GeoPoint destinationGeoPoint = new GeoPoint(destinationLat, destinationLon);

            List<GeoPoint> geoPointsTrack = new ArrayList<GeoPoint>();
            JsonArray jsonArrayTrack = jsonObjectTrack.get("track").getAsJsonArray();
            for(JsonElement jsonElementTrackPoint : jsonArrayTrack){
                JsonObject jsonObjectTrackPoint = jsonElementTrackPoint.getAsJsonObject();

                double lon = jsonObjectTrackPoint.get("lon").getAsDouble();
                double lat = jsonObjectTrackPoint.get("lat").getAsDouble();
                GeoPoint geoPoint = new GeoPoint(lat,lon);
                geoPointsTrack.add(geoPoint);
            }

            float duration = jsonObjectTrack.get("duration").getAsFloat();
            float distance = jsonObjectTrack.get("distance").getAsFloat();

            RouteLegModel routeLegModel = new RouteLegModel();
            routeLegModel.setStartingPoint(startingGeoPoint);
            routeLegModel.setDestinationPoint(destinationGeoPoint);
            routeLegModel.setTrack(geoPointsTrack);
            routeLegModel.setDuration(duration);
            routeLegModel.setDistance(distance);

            routeLegs.add(routeLegModel);
        }

        RouteDTO routeModel = new RouteDTO();
        routeModel.setWayPoints(geoWayPoints);
        routeModel.setRouteLegs(routeLegs);

        return routeModel;
    }

}
