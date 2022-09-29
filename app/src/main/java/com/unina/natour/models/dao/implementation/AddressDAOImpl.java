package com.unina.natour.models.dao.implementation;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.unina.natour.controllers.MessageController;
import com.unina.natour.dto.response.AddressResponseDTO;
import com.unina.natour.dto.response.AddressListResponseDTO;
import com.unina.natour.dto.response.MessageResponseDTO;
import com.unina.natour.models.dao.interfaces.AddressDAO;

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
public class AddressDAOImpl extends ServerDAO implements AddressDAO {

    private static final String URL = SERVER_URL + "/address";

    private static final String GET_ADDRESS = "/get";
    private static final String SEARCH_ADDRESSES = "/search";


    @Override
    public AddressResponseDTO findAddressByGeoPoint(GeoPoint geoPoint) {
        AddressResponseDTO addressResponseDTO = new AddressResponseDTO();
        String coordinates = geoPoint.getLongitude() + "," + geoPoint.getLatitude();

        String url = null;
        try {
            url = URL + GET_ADDRESS + "/" + URLEncoder.encode(coordinates,"UTF-8");
        }
        catch (UnsupportedEncodingException e) {
           addressResponseDTO.setResultMessage(MessageController.getFailureMessage());
           return addressResponseDTO;
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
            MessageResponseDTO messageResponseDTO = MessageController.getFailureMessage();
            addressResponseDTO.setResultMessage(messageResponseDTO);
            return addressResponseDTO;
        }

        if(exception[0] != null){
            MessageResponseDTO messageResponseDTO = MessageController.getFailureMessage();
            addressResponseDTO.setResultMessage(messageResponseDTO);
            return addressResponseDTO;
        }

        //can throw MessageException
        AddressResponseDTO result = toAddressDTO(jsonObjectResult);

        return result;
    }

    @Override
    public AddressListResponseDTO findAddressesByQuery(String query) {
        AddressListResponseDTO addressListDTO = new AddressListResponseDTO();

        String url = null;
        try {
            url = URL + SEARCH_ADDRESSES + "?" + "query=" + URLEncoder.encode(query,"UTF-8");
        }
        catch (UnsupportedEncodingException e) {
            addressListDTO.setResultMessage(MessageController.getFailureMessage());
            return addressListDTO;
        }

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

        JsonArray jsonArrayResult = null;
        try {
            jsonArrayResult = completableFuture.get();
        }
        catch (ExecutionException | InterruptedException e) {
            MessageResponseDTO messageResponseDTO = MessageController.getFailureMessage();
            addressListDTO.setResultMessage(messageResponseDTO);
            return addressListDTO;
        }

        if(exception[0] != null){
            MessageResponseDTO messageResponseDTO = MessageController.getFailureMessage();
            addressListDTO.setResultMessage(messageResponseDTO);
            return addressListDTO;
        }


        AddressListResponseDTO result = toAddressListDTO(jsonArrayResult);

        return result;
    }



    public static AddressResponseDTO toAddressDTO(JsonObject jsonObject){
        AddressResponseDTO addressResponseDTO = new AddressResponseDTO();

        if(!jsonObject.has("point")  || !jsonObject.has("addressLine") ) {
            MessageResponseDTO messageResponseDTO = MessageDAOImpl.toMessageDTO(jsonObject);
            addressResponseDTO.setResultMessage(messageResponseDTO);

            return addressResponseDTO;
        }

        JsonObject jsonObjectPoint = jsonObject.get("point").getAsJsonObject();

        double lon = jsonObjectPoint.get("lon").getAsDouble();
        double lat = jsonObjectPoint.get("lat").getAsDouble();
        GeoPoint geoPoint = new GeoPoint(lat,lon);

        String addressName = jsonObject.get("addressLine").getAsString();

        addressResponseDTO.setResultMessage(MessageController.MESSAGE_SUCCESS);
        addressResponseDTO.setPoint(geoPoint);
        addressResponseDTO.setAddressName(addressName);

        return addressResponseDTO;
    }

    public static AddressListResponseDTO toAddressListDTO(JsonArray jsonArray){
        AddressListResponseDTO addressListDTO = new AddressListResponseDTO();

        List<AddressResponseDTO> addressList = new ArrayList<AddressResponseDTO>();
        for(JsonElement jsonElement : jsonArray){
            JsonObject jsonObject = jsonElement.getAsJsonObject();

            AddressResponseDTO addressResponseDTO = toAddressDTO(jsonObject);
            MessageResponseDTO resultMessage = addressResponseDTO.getResultMessage();

            if(resultMessage.getCode() != MessageController.SUCCESS_CODE) {
                addressListDTO.setResultMessage(resultMessage);
                return addressListDTO;
            }
            addressList.add(addressResponseDTO);
        }

        addressListDTO.setAddresses(addressList);
        addressListDTO.setResultMessage(MessageController.MESSAGE_SUCCESS);
        return addressListDTO;
    }
}
