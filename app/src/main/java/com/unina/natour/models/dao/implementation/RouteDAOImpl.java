package com.unina.natour.models.dao.implementation;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.unina.natour.controllers.MessageController;
import com.unina.natour.dto.response.ResultMessageDTO;
import com.unina.natour.dto.response.GetRouteResponseDTO;
import com.unina.natour.models.RouteLegModel;
import com.unina.natour.models.dao.interfaces.RouteDAO;

import org.osmdroid.util.GeoPoint;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
@RequiresApi(api = Build.VERSION_CODES.N)
public class RouteDAOImpl extends ServerDAO implements RouteDAO {
    private static final String URL = SERVER_URL + "/route";

    private static final String GET_ROUTE = "/get";


    @Override
    public GetRouteResponseDTO getRouteByCoordinates(String coordinates) {
        GetRouteResponseDTO getRouteResponseDTO = new GetRouteResponseDTO();

        //can throw UnsupportedEncodingException
        String url = null;
        try {
            url = URL + GET_ROUTE + "/" + URLEncoder.encode(coordinates,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            getRouteResponseDTO.setResultMessage(MessageController.getFailureMessage());
            return getRouteResponseDTO;
        }

        Request request = new Request.Builder()
                .url(url)
                .build();

        OkHttpClient client = new OkHttpClient();

        Call call = client.newCall(request);

        final IOException[] exception = {null};
        CompletableFuture<JsonObject> completableFuture = new CompletableFuture<JsonObject>();

        call.enqueue((Callback) new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                exception[0] = e;
                completableFuture.complete(null);
                return;
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String jsonStringResult = response.body().string();
                JsonElement jsonElementResult = JsonParser.parseString(jsonStringResult);
                JsonObject jsonObjectResult = jsonElementResult.getAsJsonObject();

                completableFuture.complete(jsonObjectResult);


            }
        });

        //can throw ExecutionException, InterruptedException
        JsonObject jsonObjectResult = null;
        try {
            jsonObjectResult = completableFuture.get();
        }
        catch (ExecutionException | InterruptedException e) {
            ResultMessageDTO resultMessageDTO = MessageController.getFailureMessage();
            getRouteResponseDTO.setResultMessage(resultMessageDTO);
            return getRouteResponseDTO;
        }

        if(exception[0] != null){
            ResultMessageDTO resultMessageDTO = MessageController.getFailureMessage();
            getRouteResponseDTO.setResultMessage(resultMessageDTO);
            return getRouteResponseDTO;
        }

        //can throw MessageException
        GetRouteResponseDTO result = toRouteDTO(jsonObjectResult);

        return result;
    }


    @Override
    public GetRouteResponseDTO getRouteByGeoPoints(List<GeoPoint> geoPoints){

        if(geoPoints.size() <= 1) return null;

        GeoPoint firstGeoPoint = geoPoints.get(0);
        String coordinates = firstGeoPoint.getLongitude() + "," + firstGeoPoint.getLatitude();
        for(int i = 1; i < geoPoints.size(); i++){
            coordinates = coordinates + ";";
            GeoPoint geoPoint = geoPoints.get(i);
            coordinates = coordinates + geoPoint.getLongitude() + "," + geoPoint.getLatitude();
        }

        //can throw UnsupportedEncodingException, ExecutionException, InterruptedException, MessageException
        return getRouteByCoordinates(coordinates);
    }




    public static GetRouteResponseDTO toRouteDTO(JsonObject jsonObject) {
        GetRouteResponseDTO getRouteResponseDTO = new GetRouteResponseDTO();

        if(!jsonObject.has("wayPoints")  || !jsonObject.has("tracks") )
        {
            ResultMessageDTO resultMessageDTO = ResultMessageDAO.toMessageDTO(jsonObject);
            getRouteResponseDTO.setResultMessage(resultMessageDTO);

            return getRouteResponseDTO;
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


        getRouteResponseDTO.setWayPoints(geoWayPoints);
        getRouteResponseDTO.setTracks(routeLegs);

        return getRouteResponseDTO;
    }


}
