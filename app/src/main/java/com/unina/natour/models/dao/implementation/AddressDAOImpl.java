package com.unina.natour.models.dao.implementation;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.unina.natour.controllers.exceptionHandler.exceptions.ServerException;
import com.unina.natour.models.AddressModel;
import com.unina.natour.models.dao.converters.JsonConverter;
import com.unina.natour.models.dao.interfaces.AddressDAO;

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
public class AddressDAOImpl implements AddressDAO {

    private static final String SERVER_URL = "http://192.168.1.6:8080/address";

    private static final String GET_ADDRESS = "/get";
    private static final String SEARCH_ADDRESSES = "/search";


    @Override
    public AddressModel findAddressByGeoPoint(GeoPoint geoPoint) throws IOException, ExecutionException, InterruptedException, ServerException {
        String coordinates = geoPoint.getLongitude() + "," + geoPoint.getLatitude();

        //can throw UnsupportedEncodingException
        String url = SERVER_URL + GET_ADDRESS + "/" + URLEncoder.encode(coordinates,"UTF-8");

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
        if(jsonObjectResult != null) throw exception[0];

        //can throw MessageException
        AddressModel result = JsonConverter.toAddressModel(jsonObjectResult);

        return result;
    }

    @Override
    public List<AddressModel> findAddressesByQuery(String query) throws  IOException, ExecutionException, InterruptedException, ServerException {

        String url = SERVER_URL + SEARCH_ADDRESSES + "?" + "query=" + URLEncoder.encode(query,"UTF-8");

        Request request = new Request.Builder()
                .url(url)
                .build();

        OkHttpClient client = new OkHttpClient();

        Call call = client.newCall(request);

        final IOException[] exception = {null};
        CompletableFuture<JsonArray> completableFuture = new CompletableFuture<JsonArray>();

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
                JsonArray jsonArrayResult = jsonElementResult.getAsJsonArray();

                completableFuture.complete(jsonArrayResult);
            }
        });

        JsonArray jsonArrayResult =  completableFuture.get();

        if(jsonArrayResult == null) throw exception[0];

        List<AddressModel> result = JsonConverter.toListAddressModel(jsonArrayResult);

        return result;
    }
}
