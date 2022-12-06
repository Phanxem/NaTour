package com.unina.natour.models.dao.implementation;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.unina.natour.controllers.ResultMessageController;
import com.unina.natour.dto.request.SaveReportRequestDTO;
import com.unina.natour.dto.response.ResultMessageDTO;
import com.unina.natour.dto.response.GetListReportResponseDTO;
import com.unina.natour.dto.response.GetReportResponseDTO;
import com.unina.natour.models.dao.interfaces.ReportDAO;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ReportDAOImpl extends ServerDAO  implements ReportDAO {

    private static final String URL = SERVER_URL + "/report";

    @Override
    public GetReportResponseDTO getReportById(long idReport) {
        String url = URL + "/get/" + idReport;

        Request request = new Request.Builder()
                .url(url)
                .build();

        GetReportResponseDTO getReportResponseDTO =  getReportResponseDTO(request);

        return getReportResponseDTO;
    }


    @Override
    public GetListReportResponseDTO getReportByIdItinerary(long idItinerary) {
        String url = URL + "/get/itinerary/" + idItinerary;

        Request request = new Request.Builder()
                .url(url)
                .build();

        GetListReportResponseDTO getListReportResponseDTO =  getListReportResponseDTO(request);

        return getListReportResponseDTO;
    }



    @Override
    public ResultMessageDTO addReport(SaveReportRequestDTO saveReportRequestDTO) {
        String url = URL + "/add";

        FormBody.Builder builder = new FormBody.Builder();
        builder.add("name", saveReportRequestDTO.getName());
        builder.add("dateOfInput", saveReportRequestDTO.getDateOfInput());
        builder.add("description", saveReportRequestDTO.getDescription());
        String stringIdItinerary = String.valueOf(saveReportRequestDTO.getIdItinerary());
        builder.add("idItinerary", stringIdItinerary);
        String stringIdUser = String.valueOf(saveReportRequestDTO.getIdUser());
        builder.add("idUser", stringIdUser);

        RequestBody requestBody = builder.build();

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        ResultMessageDTO resultMessageDTO = resultMessageDAO.fulfilRequest(request);
        return resultMessageDTO;
    }

    @Override
    public ResultMessageDTO deleteReportById(long idReport) {
        String url = URL + "/delete/" + idReport;

        Request request = new Request.Builder()
                .url(url)
                .delete()
                .build();

        ResultMessageDTO resultMessageDTO = resultMessageDAO.fulfilRequest(request);
        return resultMessageDTO;
    }




    private GetReportResponseDTO getReportResponseDTO(Request request){
        GetReportResponseDTO getReportResponseDTO = new GetReportResponseDTO();

        OkHttpClient client = new OkHttpClient();

        Request signedRequest = ServerDAO.signRequest(request);

        Call call = client.newCall(signedRequest);

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
            getReportResponseDTO.setResultMessage(ResultMessageController.ERROR_MESSAGE_FAILURE_CLIENT);
            return getReportResponseDTO;
        }

        if(exception[0] != null){
            getReportResponseDTO.setResultMessage(ResultMessageController.ERROR_MESSAGE_FAILURE_CLIENT);
            return getReportResponseDTO;
        }

        ResultMessageDTO resultMessage = ResultMessageDAO.getResultMessage(jsonObjectResult);

        if(!ResultMessageController.isSuccess(resultMessage)){
            getReportResponseDTO.setResultMessage(resultMessage);
            return getReportResponseDTO;
        }

        getReportResponseDTO = toGetReportResponseDTO(jsonObjectResult);

        return getReportResponseDTO;
    }

    private GetListReportResponseDTO getListReportResponseDTO(Request request){
        GetListReportResponseDTO getListReportResponseDTO = new GetListReportResponseDTO();

        OkHttpClient client = new OkHttpClient();

        Request signedRequest = ServerDAO.signRequest(request);

        Call call = client.newCall(signedRequest);

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
            getListReportResponseDTO.setResultMessage(ResultMessageController.ERROR_MESSAGE_FAILURE_CLIENT);
            return getListReportResponseDTO;
        }

        if(exception[0] != null){
            getListReportResponseDTO.setResultMessage(ResultMessageController.ERROR_MESSAGE_FAILURE_CLIENT);
            return getListReportResponseDTO;
        }

        ResultMessageDTO resultMessage = ResultMessageDAO.getResultMessage(jsonObjectResult);

        if(!ResultMessageController.isSuccess(resultMessage)){
            getListReportResponseDTO.setResultMessage(resultMessage);
            return getListReportResponseDTO;
        }

        getListReportResponseDTO = toGetListReportResponseDTO(jsonObjectResult);

        return getListReportResponseDTO;
    }


//MAPPER
    public GetReportResponseDTO toGetReportResponseDTO(JsonObject jsonObject){
        GetReportResponseDTO getReportResponseDTO = new GetReportResponseDTO();

        Log.e("ReportDAO", jsonObject.toString());

        if(jsonObject.has("resultMessage") ){
            JsonObject jsonResultMessage = jsonObject.get("resultMessage").getAsJsonObject();

            long code = jsonResultMessage.get("code").getAsLong();
            String message = jsonResultMessage.get("message").getAsString();

            ResultMessageDTO resultMessageDTO = new ResultMessageDTO(code,message);
            getReportResponseDTO.setResultMessage(resultMessageDTO);
        }

        long id = jsonObject.get("id").getAsLong();
        getReportResponseDTO.setId(id);

        String name = jsonObject.get("name").getAsString();
        getReportResponseDTO.setName(name);

        String dateOfInput = jsonObject.get("dateOfInput").getAsString();
        getReportResponseDTO.setDateOfInput(dateOfInput);

        if(jsonObject.has("description")){
            String description = jsonObject.get("description").getAsString();
            getReportResponseDTO.setDescription(description);
        }

        long idUser = jsonObject.get("idUser").getAsLong();
        getReportResponseDTO.setIdUser(idUser);

        long idItinerary = jsonObject.get("idItinerary").getAsLong();
        getReportResponseDTO.setIdItinerary(idItinerary);

        String nameItinerary = jsonObject.get("nameItinerary").getAsString();
        getReportResponseDTO.setNameItinerary(nameItinerary);

        return getReportResponseDTO;
    }

    public GetListReportResponseDTO toGetListReportResponseDTO(JsonObject jsonObject){
        GetListReportResponseDTO getListReportResponseDTO = new GetListReportResponseDTO();

        if(jsonObject.has("resultMessage") ){
            JsonObject jsonResultMessage = jsonObject.get("resultMessage").getAsJsonObject();

            long code = jsonResultMessage.get("code").getAsLong();
            String message = jsonResultMessage.get("message").getAsString();

            ResultMessageDTO resultMessageDTO = new ResultMessageDTO(code,message);
            getListReportResponseDTO.setResultMessage(resultMessageDTO);
        }

        JsonArray jsonArray = jsonObject.get("listReport").getAsJsonArray();
        List<GetReportResponseDTO> listReport = new ArrayList<GetReportResponseDTO>();
        for(JsonElement jsonElement : jsonArray){
            JsonObject jsonObjectElement = jsonElement.getAsJsonObject();

            GetReportResponseDTO getReportResponseDTO = toGetReportResponseDTO(jsonObjectElement);
            listReport.add(getReportResponseDTO);
        }
        getListReportResponseDTO.setListReport(listReport);

        return getListReportResponseDTO;
    }

}
