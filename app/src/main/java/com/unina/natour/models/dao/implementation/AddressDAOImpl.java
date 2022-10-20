package com.unina.natour.models.dao.implementation;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.unina.natour.controllers.MessageController;
import com.unina.natour.dto.response.GetAddressResponseDTO;
import com.unina.natour.dto.response.GetListAddressResponseDTO;
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
            url = URL + GET_ADDRESS + "/" + URLEncoder.encode(coordinates,"UTF-8");
        }
        catch (UnsupportedEncodingException e) {
           getAddressResponseDTO.setResultMessage(MessageController.getFailureMessage());
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
            ResultMessageDTO resultMessageDTO = MessageController.getFailureMessage();
            getAddressResponseDTO.setResultMessage(resultMessageDTO);
            return getAddressResponseDTO;
        }

        if(exception[0] != null){
            ResultMessageDTO resultMessageDTO = MessageController.getFailureMessage();
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
            ResultMessageDTO resultMessageDTO = MessageController.getFailureMessage();
            addressListDTO.setResultMessage(resultMessageDTO);
            return addressListDTO;
        }

        if(exception[0] != null){
            ResultMessageDTO resultMessageDTO = MessageController.getFailureMessage();
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

        getAddressResponseDTO.setResultMessage(MessageController.MESSAGE_SUCCESS);
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

            if(resultMessage.getCode() != MessageController.SUCCESS_CODE) {
                addressListDTO.setResultMessage(resultMessage);
                return addressListDTO;
            }
            addressList.add(getAddressResponseDTO);
        }

        addressListDTO.setListAddress(addressList);
        addressListDTO.setResultMessage(MessageController.MESSAGE_SUCCESS);
        return addressListDTO;
    }
}
