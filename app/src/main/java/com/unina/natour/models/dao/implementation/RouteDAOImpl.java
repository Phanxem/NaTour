package com.unina.natour.models.dao.implementation;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.unina.natour.controllers.exceptionHandler.exceptions.ServerException;
import com.unina.natour.dto.RouteDTO;
import com.unina.natour.models.dao.converters.JsonConverter;
import com.unina.natour.models.dao.interfaces.RouteDAO;

import org.osmdroid.util.GeoPoint;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
@RequiresApi(api = Build.VERSION_CODES.N)
public class RouteDAOImpl implements RouteDAO {
    private static final String URL = SERVER_URL + "/route";

    private static final String GET_ROUTE = "/get";


    @Override
    public RouteDTO findRouteByCoordinates(String coordinates) throws IOException, ExecutionException, InterruptedException, ServerException {
        //can throw UnsupportedEncodingException
        String url = URL + GET_ROUTE + "/" + URLEncoder.encode(coordinates,"UTF-8");

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
        JsonObject jsonObjectResult = completableFuture.get();

        //can throw IOException
        if(jsonObjectResult == null) throw exception[0];

        //can throw MessageException
        RouteDTO result = JsonConverter.toRouteDTO(jsonObjectResult);

        return result;
    }


    @Override
    public RouteDTO findRouteByGeoPoints(List<GeoPoint> geoPoints) throws IOException, ExecutionException, InterruptedException, ServerException {

        if(geoPoints.size() <= 1) return null;

        GeoPoint firstGeoPoint = geoPoints.get(0);
        String coordinates = firstGeoPoint.getLongitude() + "," + firstGeoPoint.getLatitude();
        for(int i = 1; i < geoPoints.size(); i++){
            coordinates = coordinates + ";";
            GeoPoint geoPoint = geoPoints.get(i);
            coordinates = coordinates + geoPoint.getLongitude() + "," + geoPoint.getLatitude();
        }

        //can throw UnsupportedEncodingException, ExecutionException, InterruptedException, MessageException
        return findRouteByCoordinates(coordinates);
    }
}
