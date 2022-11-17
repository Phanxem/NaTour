package com.unina.natour.models.dao.implementation;

import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.unina.natour.controllers.ResultMessageController;
import com.unina.natour.dto.response.GetRouteLegResponseDTO;
import com.unina.natour.dto.response.ResultMessageDTO;
import com.unina.natour.dto.response.GetRouteResponseDTO;
import com.unina.natour.models.dao.interfaces.RouteDAO;

import org.osmdroid.util.GeoPoint;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
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


        String url = null;
        try {
            url = URL + GET_ROUTE + "/" + URLEncoder.encode(coordinates,"UTF-8");
        }
        catch (UnsupportedEncodingException e) {
            Log.e("TAG", "ERROR A");
            getRouteResponseDTO.setResultMessage(ResultMessageController.ERROR_MESSAGE_FAILURE_CLIENT);
            return getRouteResponseDTO;
        }

        Request request = new Request.Builder()
                .url(url)
                .build();

        getRouteResponseDTO = getRouteResponseDTO(request);

        return getRouteResponseDTO;
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

        Log.e("TAG", "BEFORE getRouteByCoordinates");

        return getRouteByCoordinates(coordinates);
    }



    private GetRouteResponseDTO getRouteResponseDTO(Request request){
        GetRouteResponseDTO getRouteResponseDTO = new GetRouteResponseDTO();

        OkHttpClient client = new OkHttpClient();

        Call call = client.newCall(request);

        final IOException[] exception = {null};
        CompletableFuture<JsonObject> completableFuture = new CompletableFuture<JsonObject>();

        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                exception[0] = e;
                completableFuture.complete(null);
                return;
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

                String jsonStringResult;
                if(response.code() == HttpURLConnection.HTTP_UNAUTHORIZED ||
                        response.code() == HttpURLConnection.HTTP_FORBIDDEN){
                    jsonStringResult = "{ \"code\": " + ResultMessageController.CODE_ERROR_UNAUTHORIZED + ", \"message\": \"" + ResultMessageController.MESSAGE_ERROR_UNAUTORIZED + "\" }";
                }
                else{
                    jsonStringResult = response.body().string();
                }

                JsonElement jsonElementResult = JsonParser.parseString(jsonStringResult);
                JsonObject jsonObjectResult = jsonElementResult.getAsJsonObject();

                completableFuture.complete(jsonObjectResult);
            }
        });



        JsonObject jsonObjectResult = null;
        try {
            jsonObjectResult = completableFuture.get();
        }
        catch (ExecutionException | InterruptedException e) {
            getRouteResponseDTO.setResultMessage(ResultMessageController.ERROR_MESSAGE_FAILURE_CLIENT);
            return getRouteResponseDTO;
        }

        if(exception[0] != null){
            getRouteResponseDTO.setResultMessage(ResultMessageController.ERROR_MESSAGE_FAILURE_CLIENT);
            return getRouteResponseDTO;
        }

        ResultMessageDTO resultMessage = ResultMessageDAO.getResultMessage(jsonObjectResult);

        if(!ResultMessageController.isSuccess(resultMessage)){
            getRouteResponseDTO.setResultMessage(resultMessage);
            return getRouteResponseDTO;
        }

        getRouteResponseDTO = toGetRouteResponseDTO(jsonObjectResult);

        return getRouteResponseDTO;
    }


    //MAPPER

    public static GetRouteResponseDTO toGetRouteResponseDTO(JsonObject jsonObject) {
        GetRouteResponseDTO getRouteResponseDTO = new GetRouteResponseDTO();

        if(!jsonObject.has("wayPoints")  || !jsonObject.has("tracks") )
        {
            ResultMessageDTO resultMessageDTO = ResultMessageDAO.getResultMessage(jsonObject);
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

        List<GetRouteLegResponseDTO> routeLegs = new ArrayList<GetRouteLegResponseDTO>();
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

            GetRouteLegResponseDTO getRouteLegResponseDTO = new GetRouteLegResponseDTO();
            getRouteLegResponseDTO.setStartingPoint(startingGeoPoint);
            getRouteLegResponseDTO.setDestinationPoint(destinationGeoPoint);
            getRouteLegResponseDTO.setTrack(geoPointsTrack);
            getRouteLegResponseDTO.setDuration(duration);
            getRouteLegResponseDTO.setDistance(distance);

            routeLegs.add(getRouteLegResponseDTO);
        }


        getRouteResponseDTO.setWayPoints(geoWayPoints);
        getRouteResponseDTO.setTracks(routeLegs);

        getRouteResponseDTO.setResultMessage(ResultMessageController.SUCCESS_MESSAGE);

        return getRouteResponseDTO;
    }


}
