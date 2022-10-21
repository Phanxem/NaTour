package com.unina.natour.models.dao.implementation;

import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.unina.natour.controllers.ResultMessageController;
import com.unina.natour.dto.response.GetAddressResponseDTO;
import com.unina.natour.dto.response.GetListAddressResponseDTO;
import com.unina.natour.dto.response.GetListUserResponseDTO;
import com.unina.natour.dto.response.GetUserResponseDTO;
import com.unina.natour.dto.response.ResultMessageDTO;
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
    public GetAddressResponseDTO getAddressByGeoPoint(GeoPoint geoPoint) {
        GetAddressResponseDTO getAddressResponseDTO = new GetAddressResponseDTO();
        String coordinates = geoPoint.getLongitude() + "," + geoPoint.getLatitude();

        String url = null;
        try {
            url = URL +"/get/" + URLEncoder.encode(coordinates,"UTF-8");
        }
        catch (UnsupportedEncodingException e) {
            getAddressResponseDTO.setResultMessage(ResultMessageController.ERROR_MESSAGE_FAILURE_CLIENT);
            return getAddressResponseDTO;
        }

        getAddressResponseDTO = getAddressResponseDTO(url);

        return getAddressResponseDTO;
    }

    @Override
    public GetListAddressResponseDTO getAddressesByQuery(String query) {
        String url = URL + "/search?query=" + query;

        GetListAddressResponseDTO getListAddressResponseDTO = getListAddressResponseDTO(url);

        return getListAddressResponseDTO;
    }




    private GetAddressResponseDTO getAddressResponseDTO(String url){
        GetAddressResponseDTO getAddressResponseDTO = new GetAddressResponseDTO();

        Request request = new Request.Builder()
                .url(url)
                .build();

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

                String jsonStringResult = response.body().string();
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
            getAddressResponseDTO.setResultMessage(ResultMessageController.ERROR_MESSAGE_FAILURE_CLIENT);
            return getAddressResponseDTO;
        }

        if(exception[0] != null){
            getAddressResponseDTO.setResultMessage(ResultMessageController.ERROR_MESSAGE_FAILURE_CLIENT);
            return getAddressResponseDTO;
        }

        ResultMessageDTO resultMessage = ResultMessageDAO.getResultMessage(jsonObjectResult);

        if(!ResultMessageController.isSuccess(resultMessage)){
            getAddressResponseDTO.setResultMessage(resultMessage);
            return getAddressResponseDTO;
        }

        getAddressResponseDTO = toGetAddressResponseDTO(jsonObjectResult);

        return getAddressResponseDTO;
    }

    private GetListAddressResponseDTO getListAddressResponseDTO(String url){
        GetListAddressResponseDTO getListAddressResponseDTO = new GetListAddressResponseDTO();

        Request request = new Request.Builder()
                .url(url)
                .build();

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

                String jsonStringResult = response.body().string();
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
            getListAddressResponseDTO.setResultMessage(ResultMessageController.ERROR_MESSAGE_FAILURE_CLIENT);
            return getListAddressResponseDTO;
        }

        if(exception[0] != null){
            getListAddressResponseDTO.setResultMessage(ResultMessageController.ERROR_MESSAGE_FAILURE_CLIENT);
            return getListAddressResponseDTO;
        }

        ResultMessageDTO resultMessage = ResultMessageDAO.getResultMessage(jsonObjectResult);

        if(!ResultMessageController.isSuccess(resultMessage)){
            getListAddressResponseDTO.setResultMessage(resultMessage);
            return getListAddressResponseDTO;
        }

        getListAddressResponseDTO = toGetListAddressResponseDTO(jsonObjectResult);

        return getListAddressResponseDTO;
    }


//MAPPER

    public GetAddressResponseDTO toGetAddressResponseDTO(JsonObject jsonObject){
        GetAddressResponseDTO getAddressResponseDTO = new GetAddressResponseDTO();

        if(!jsonObject.has("resultMessage") ){
            JsonObject jsonResultMessage = jsonObject.get("resultMessage").getAsJsonObject();

            long code = jsonResultMessage.get("code").getAsLong();
            String message = jsonResultMessage.get("message").getAsString();

            ResultMessageDTO resultMessageDTO = new ResultMessageDTO(code,message);
            getAddressResponseDTO.setResultMessage(resultMessageDTO);
        }


        JsonObject jsonObjectPoint = jsonObject.get("point").getAsJsonObject();

        double lon = jsonObjectPoint.get("lon").getAsDouble();
        double lat = jsonObjectPoint.get("lat").getAsDouble();
        GeoPoint geoPoint = new GeoPoint(lat,lon);

        getAddressResponseDTO.setPoint(geoPoint);


        String addressName = jsonObject.get("addressName").getAsString();

        getAddressResponseDTO.setAddressName(addressName);

        return getAddressResponseDTO;
    }

    public GetListAddressResponseDTO toGetListAddressResponseDTO(JsonObject jsonObject){
        GetListAddressResponseDTO getListAddressResponseDTO = new GetListAddressResponseDTO();

        if(!jsonObject.has("resultMessage") ){
            JsonObject jsonResultMessage = jsonObject.get("resultMessage").getAsJsonObject();

            long code = jsonResultMessage.get("code").getAsLong();
            String message = jsonResultMessage.get("message").getAsString();

            ResultMessageDTO resultMessageDTO = new ResultMessageDTO(code,message);
            getListAddressResponseDTO.setResultMessage(resultMessageDTO);
        }

        JsonArray jsonArray = jsonObject.get("listUser").getAsJsonArray();
        List<GetAddressResponseDTO> listAddress = new ArrayList<GetAddressResponseDTO>();
        for(JsonElement jsonElement : jsonArray){
            JsonObject jsonObjectElement = jsonElement.getAsJsonObject();

            GetAddressResponseDTO getAddressResponseDTO = toGetAddressResponseDTO(jsonObject);
            listAddress.add(getAddressResponseDTO);
        }
        getListAddressResponseDTO.setListAddress(listAddress);

        return getListAddressResponseDTO;
    }


/*
    @Override
    public GetAddressResponseDTO getAddressByGeoPoint(GeoPoint geoPoint) {
        GetAddressResponseDTO getAddressResponseDTO = new GetAddressResponseDTO();
        String coordinates = geoPoint.getLongitude() + "," + geoPoint.getLatitude();

        String url = null;
        try {
            url = URL + GET_ADDRESS + "/" + URLEncoder.encode(coordinates,"UTF-8");
        }
        catch (UnsupportedEncodingException e) {
           getAddressResponseDTO.setResultMessage(ResultMessageController.getFailureMessage());
           return getAddressResponseDTO;
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
            ResultMessageDTO resultMessageDTO = ResultMessageController.getFailureMessage();
            getAddressResponseDTO.setResultMessage(resultMessageDTO);
            return getAddressResponseDTO;
        }

        if(exception[0] != null){
            ResultMessageDTO resultMessageDTO = ResultMessageController.getFailureMessage();
            getAddressResponseDTO.setResultMessage(resultMessageDTO);
            return getAddressResponseDTO;
        }

        //can throw MessageException
        GetAddressResponseDTO result = toAddressDTO(jsonObjectResult);

        return result;
    }

    @Override
    public GetListAddressResponseDTO getAddressesByQuery(String query) {
        GetListAddressResponseDTO addressListDTO = new GetListAddressResponseDTO();

        String url = null;
        try {
            url = URL + SEARCH_ADDRESSES + "?" + "query=" + URLEncoder.encode(query,"UTF-8");
        }
        catch (UnsupportedEncodingException e) {
            addressListDTO.setResultMessage(ResultMessageController.getFailureMessage());
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
            ResultMessageDTO resultMessageDTO = ResultMessageController.getFailureMessage();
            addressListDTO.setResultMessage(resultMessageDTO);
            return addressListDTO;
        }

        if(exception[0] != null){
            ResultMessageDTO resultMessageDTO = ResultMessageController.getFailureMessage();
            addressListDTO.setResultMessage(resultMessageDTO);
            return addressListDTO;
        }


        GetListAddressResponseDTO result = toAddressListDTO(jsonArrayResult);

        return result;
    }



    public static GetAddressResponseDTO toAddressDTO(JsonObject jsonObject){
        GetAddressResponseDTO getAddressResponseDTO = new GetAddressResponseDTO();

        if(!jsonObject.has("point")  || !jsonObject.has("addressLine") ) {
            ResultMessageDTO resultMessageDTO = ResultMessageDAO.toMessageDTO(jsonObject);
            getAddressResponseDTO.setResultMessage(resultMessageDTO);

            return getAddressResponseDTO;
        }

        JsonObject jsonObjectPoint = jsonObject.get("point").getAsJsonObject();

        double lon = jsonObjectPoint.get("lon").getAsDouble();
        double lat = jsonObjectPoint.get("lat").getAsDouble();
        GeoPoint geoPoint = new GeoPoint(lat,lon);

        String addressName = jsonObject.get("addressLine").getAsString();

        getAddressResponseDTO.setResultMessage(ResultMessageController.MESSAGE_SUCCESS);
        getAddressResponseDTO.setPoint(geoPoint);
        getAddressResponseDTO.setAddressName(addressName);

        return getAddressResponseDTO;
    }

    public static GetListAddressResponseDTO toAddressListDTO(JsonArray jsonArray){
        GetListAddressResponseDTO addressListDTO = new GetListAddressResponseDTO();

        List<GetAddressResponseDTO> addressList = new ArrayList<GetAddressResponseDTO>();
        for(JsonElement jsonElement : jsonArray){
            JsonObject jsonObject = jsonElement.getAsJsonObject();

            GetAddressResponseDTO getAddressResponseDTO = toAddressDTO(jsonObject);
            ResultMessageDTO resultMessage = getAddressResponseDTO.getResultMessage();

            if(resultMessage.getCode() != ResultMessageController.SUCCESS_CODE) {
                addressListDTO.setResultMessage(resultMessage);
                return addressListDTO;
            }
            addressList.add(getAddressResponseDTO);
        }

        addressListDTO.setListAddress(addressList);
        addressListDTO.setResultMessage(ResultMessageController.MESSAGE_SUCCESS);
        return addressListDTO;
    }



 */
}

