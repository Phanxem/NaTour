package com.unina.natour.models.dao.implementation;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.unina.natour.controllers.ResultMessageController;
import com.unina.natour.controllers.utils.FileUtils;
import com.unina.natour.dto.response.GetGpxResponseDTO;
import com.unina.natour.dto.response.GetListItineraryResponseDTO;
import com.unina.natour.dto.response.GetListReportResponseDTO;
import com.unina.natour.dto.response.GetReportResponseDTO;
import com.unina.natour.dto.response.ResultMessageDTO;
import com.unina.natour.dto.request.SaveItineraryRequestDTO;
import com.unina.natour.dto.response.GetItineraryResponseDTO;
import com.unina.natour.dto.response.composted.GetItineraryWithReportResponseDTO;
import com.unina.natour.dto.response.composted.GetItineraryWithUserResponseDTO;
import com.unina.natour.dto.response.composted.GetListItineraryWithUserResponseDTO;
import com.unina.natour.dto.response.composted.GetUserWithImageResponseDTO;
import com.unina.natour.models.dao.interfaces.ItineraryDAO;
import com.unina.natour.models.dao.interfaces.ReportDAO;
import com.unina.natour.models.dao.interfaces.UserDAO;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import io.jenetics.jpx.GPX;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ItineraryDAOImpl extends ServerDAO implements ItineraryDAO {

    private static final String URL = SERVER_URL + "/itinerary";

    private static final int ELEMENT_PER_PAGE = 20;

    private static final String ADD_ITINERARY = "/add";


    private static final String PARAM_KEY_USERNAME = "usernameUser";
    private static final String BODY_KEY_NAME = "name";
    private static final String BODY_KEY_GPX = "gpx";
    private static final String BODY_KEY_DURATION = "duration";
    private static final String BODY_KEY_LENGHT = "lenght";
    private static final String BODY_KEY_DIFFICULTY = "difficulty";
    private static final String BODY_KEY_DESCRIPTION = "description";


    private static final String TAG = "UserDAO";

    private static final String TEST_USER = "user";

    private Context context;
    private ResultMessageDAO resultMessageDAO;
    private UserDAO userDAO;
    private ReportDAO reportDAO;

    public ItineraryDAOImpl(Context context){
        this.context = context;
        this.resultMessageDAO = new ResultMessageDAO();
        this.userDAO = new UserDAOImpl(context);
        this.reportDAO = new ReportDAOImpl();
    }


    @Override
    public GetItineraryResponseDTO getItineraryById(long idItinerary) {
        String url = URL + "/get/" + idItinerary;

        GetItineraryResponseDTO getItineraryResponseDTO = getItineraryResponseDTO(url);

        return getItineraryResponseDTO;
    }

    @Override
    public GetGpxResponseDTO getItineraryGpxById(long idItinerary) {
        GetGpxResponseDTO getGpxResponseDTO = new GetGpxResponseDTO();

        String url = URL + "/get/" + idItinerary + "/gpx";

        Request request = new Request.Builder()
                .url(url)
                .build();

        OkHttpClient client = new OkHttpClient();

        Call call = client.newCall(request);

        final IOException[] exception = {null};
        final boolean[] isSuccessful = {true};
        CompletableFuture<byte[]> completableFuture = new CompletableFuture<byte[]>();

        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                exception[0] = e;
                completableFuture.complete(null);
                return;
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if(!response.isSuccessful()){
                    isSuccessful[0] = false;
                    completableFuture.complete(null);
                    return;
                }

                completableFuture.complete(response.body().bytes());
            }
        });


        byte[] bytes = new byte[0];
        try {
            bytes = completableFuture.get();
        }
        catch (ExecutionException | InterruptedException e) {
            getGpxResponseDTO.setResultMessage(ResultMessageController.ERROR_MESSAGE_FAILURE_CLIENT);
            return getGpxResponseDTO;
        }

        if(exception[0] != null){
            getGpxResponseDTO.setResultMessage(ResultMessageController.ERROR_MESSAGE_FAILURE_CLIENT);
            return getGpxResponseDTO;
        }

        if(!isSuccessful[0]){
            getGpxResponseDTO.setResultMessage(ResultMessageController.ERROR_MESSAGE_FAILURE_CLIENT);
            return getGpxResponseDTO;
        }

        InputStream inputStream = new ByteArrayInputStream(bytes);

        //TODO da testare
        GPX gpx = null;
        try {
            gpx = GPX.read(inputStream);
        }
        catch (IOException e) {
            getGpxResponseDTO.setResultMessage(ResultMessageController.ERROR_MESSAGE_FAILURE_CLIENT);
            return getGpxResponseDTO;
        }


        getGpxResponseDTO.setGpx(gpx);
        getGpxResponseDTO.setResultMessage(ResultMessageController.SUCCESS_MESSAGE);

        return getGpxResponseDTO;
    }

    @Override
    public GetListItineraryResponseDTO getListItineraryByIdUser(long idUser, int page) {
        String url = URL + "/get/user/" + idUser + "?page=" + page;

        GetListItineraryResponseDTO getListItineraryResponseDTO = getListItineraryResponseDTO(url);

        return getListItineraryResponseDTO;
    }

    @Override
    public GetListItineraryResponseDTO getListItineraryRandom() {
        String url = URL + "/get/random";

        GetListItineraryResponseDTO getListItineraryResponseDTO = getListItineraryResponseDTO(url);

        return getListItineraryResponseDTO;
    }

    @Override
    public GetListItineraryResponseDTO getListItineraryByName(String name, int page) {
        String url = URL + "/search?name=" + name + "&page=" + page;

        GetListItineraryResponseDTO getListItineraryResponseDTO = getListItineraryResponseDTO(url);

        return getListItineraryResponseDTO;
    }


    @Override
    public ResultMessageDTO addItinerary(SaveItineraryRequestDTO saveItineraryRequest) {
        String url = URL + "/add";

        GPX gpx = saveItineraryRequest.getGpx();

        File file = null;
        try {
            file = FileUtils.toFile(context, gpx);
        }
        catch (IOException e) {
            return ResultMessageController.ERROR_MESSAGE_FAILURE_CLIENT;
        }

        RequestBody gpxRequestBody = RequestBody.create(file, MediaType.parse("application/octet-stream"));

        MultipartBody.Builder builder = new MultipartBody.Builder();

        builder.setType(MultipartBody.FORM);
        builder.addFormDataPart("name", saveItineraryRequest.getName());
        builder.addFormDataPart("gpx", null, gpxRequestBody);

        String stringDuration = String.valueOf(saveItineraryRequest.getDuration());
        builder.addFormDataPart("duration", stringDuration);

        String stringLenght = String.valueOf(saveItineraryRequest.getLenght());
        builder.addFormDataPart("lenght", stringLenght);

        String stringDifficulty = String.valueOf(saveItineraryRequest.getDifficulty());
        builder.addFormDataPart("difficulty", stringDifficulty);

        builder.addFormDataPart("description", saveItineraryRequest.getDescription());


        RequestBody requestBody = builder.build();

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        ResultMessageDTO resultMessageDTO = resultMessageDAO.fulfilRequest(request);

        return resultMessageDTO;
    }

    @Override
    public ResultMessageDTO deleteItineraryById(long idItinerary) {
        String url = URL + "/delete/" + idItinerary;

        Request request = new Request.Builder()
                .url(url)
                .delete()
                .build();

        ResultMessageDTO resultMessageDTO = resultMessageDAO.fulfilRequest(request);

        return resultMessageDTO;
    }



    //COMPOSITED
    @Override
    public GetListItineraryWithUserResponseDTO getListItineraryWithUserRandom() {

        //recupera lista itinerari
        GetListItineraryResponseDTO getListItineraryResponseDTO = getListItineraryRandom();
        List<GetItineraryResponseDTO> listItinerary = getListItineraryResponseDTO.getListItinerary();

        GetListItineraryWithUserResponseDTO getListItineraryWithUserResponseDTO = getListItineraryWithUserResponseDTO(listItinerary);

        return getListItineraryWithUserResponseDTO;
    }

    @Override
    public GetListItineraryWithUserResponseDTO getListItineraryWithUserByName(String name, int page) {

        //recupera lista itinerari
        GetListItineraryResponseDTO getListItineraryResponseDTO = getListItineraryByName(name, page);
        List<GetItineraryResponseDTO> listItinerary = getListItineraryResponseDTO.getListItinerary();

        GetListItineraryWithUserResponseDTO getListItineraryWithUserResponseDTO = getListItineraryWithUserResponseDTO(listItinerary);

        return getListItineraryWithUserResponseDTO;
    }

    @Override
    public GetItineraryWithReportResponseDTO getItineraryWithReportById(long idItinerary) {
        GetItineraryWithReportResponseDTO getItineraryWithReportResponseDTO = new GetItineraryWithReportResponseDTO();

        GetItineraryResponseDTO getItineraryResponseDTO = getItineraryById(idItinerary);
        if(!ResultMessageController.isSuccess(getItineraryResponseDTO.getResultMessage())){
            getItineraryWithReportResponseDTO.setResultMessage(getItineraryResponseDTO.getResultMessage());
            return getItineraryWithReportResponseDTO;
        }

        GetListReportResponseDTO getListReportResponseDTO = reportDAO.getReportByIdItinerary(idItinerary);
        if(!ResultMessageController.isSuccess(getItineraryResponseDTO.getResultMessage())){
            getItineraryWithReportResponseDTO.setResultMessage(getListReportResponseDTO.getResultMessage());
            return getItineraryWithReportResponseDTO;
        }

        getItineraryWithReportResponseDTO.setId(getItineraryResponseDTO.getId());
        getItineraryWithReportResponseDTO.setDescription(getItineraryResponseDTO.getDescription());
        getItineraryWithReportResponseDTO.setDifficulty(getItineraryResponseDTO.getDifficulty());
        getItineraryWithReportResponseDTO.setDuration(getItineraryResponseDTO.getDuration());
        getItineraryWithReportResponseDTO.setLenght(getItineraryResponseDTO.getLenght());
        getItineraryWithReportResponseDTO.setIdUser(getItineraryResponseDTO.getIdUser());
        getItineraryWithReportResponseDTO.setName(getItineraryResponseDTO.getName());

        List<GetReportResponseDTO> listReport = getListReportResponseDTO.getListReport();

        if(listReport.isEmpty()) getItineraryWithReportResponseDTO.setReported(false);
        else getItineraryWithReportResponseDTO.setReported(true);

        getItineraryWithReportResponseDTO.setResultMessage(ResultMessageController.SUCCESS_MESSAGE);

        return getItineraryWithReportResponseDTO;
    }




    private GetItineraryResponseDTO getItineraryResponseDTO(String url){
        GetItineraryResponseDTO getItineraryResponseDTO = new GetItineraryResponseDTO();

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
            getItineraryResponseDTO.setResultMessage(ResultMessageController.ERROR_MESSAGE_FAILURE_CLIENT);
            return getItineraryResponseDTO;
        }

        if(exception[0] != null){
            getItineraryResponseDTO.setResultMessage(ResultMessageController.ERROR_MESSAGE_FAILURE_CLIENT);
            return getItineraryResponseDTO;
        }

        ResultMessageDTO resultMessage = ResultMessageDAO.getResultMessage(jsonObjectResult);

        if(!ResultMessageController.isSuccess(resultMessage)){
            getItineraryResponseDTO.setResultMessage(resultMessage);
            return getItineraryResponseDTO;
        }

        getItineraryResponseDTO = toGetItineraryResponseDTO(jsonObjectResult);

        return getItineraryResponseDTO;
    }

    private GetListItineraryResponseDTO getListItineraryResponseDTO(String url){
        GetListItineraryResponseDTO getListItineraryResponseDTO = new GetListItineraryResponseDTO();

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
            getListItineraryResponseDTO.setResultMessage(ResultMessageController.ERROR_MESSAGE_FAILURE_CLIENT);
            return getListItineraryResponseDTO;
        }

        if(exception[0] != null){
            getListItineraryResponseDTO.setResultMessage(ResultMessageController.ERROR_MESSAGE_FAILURE_CLIENT);
            return getListItineraryResponseDTO;
        }

        ResultMessageDTO resultMessage = ResultMessageDAO.getResultMessage(jsonObjectResult);

        if(!ResultMessageController.isSuccess(resultMessage)){
            getListItineraryResponseDTO.setResultMessage(resultMessage);
            return getListItineraryResponseDTO;
        }

        getListItineraryResponseDTO = toGetListItineraryResponseDTO(jsonObjectResult);

        return getListItineraryResponseDTO;
    }


    private GetListItineraryWithUserResponseDTO getListItineraryWithUserResponseDTO(List<GetItineraryResponseDTO> listItinerary){

        GetListItineraryWithUserResponseDTO getListItineraryWithUserResponseDTO = new GetListItineraryWithUserResponseDTO();

        //inserisci gli idUser in un Map(long, Set<int>)
        //dove la key è l'idUser, e il value è l'insieme di index della lista itinerari corrispondenti all'idUser
        Map<Long, Set<Integer>> mapIdUser = new HashMap<Long, Set<Integer>>();

        GetItineraryResponseDTO tempItinerary = null;
        long tempIdUser = -1;
        for(int i = 0; i < listItinerary.size(); i++){
            tempItinerary = listItinerary.get(i);
            tempIdUser = tempItinerary.getIdUser();

            if(!mapIdUser.containsKey(tempIdUser)){
                Set<Integer> setIndex = new HashSet<Integer>();
                setIndex.add(i);
                mapIdUser.put(tempIdUser, setIndex);
            }
            else{
                Set<Integer> setIndex = mapIdUser.get(tempIdUser);
                setIndex.add(i);
                mapIdUser.replace(tempIdUser,setIndex);
            }
        }

        //Definiamo un array di UserWithImageDTO con listaItinerari.size() elementi
        GetUserWithImageResponseDTO[] arrayUser = new GetUserWithImageResponseDTO[listItinerary.size()];

        ExecutorService executor = Executors.newFixedThreadPool(ELEMENT_PER_PAGE);

        //avvio un for sulla Mappa degli idUser
        for(Map.Entry<Long, Set<Integer>> entry : mapIdUser.entrySet()){

            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    GetUserWithImageResponseDTO getUserWithImageResponseDTO = userDAO.getUserWithImageById(entry.getKey());

                    for(Integer index : entry.getValue()){
                        arrayUser[index] = getUserWithImageResponseDTO;
                    }

                }
            };

            executor.execute(runnable);
        }

        boolean finished = false;
        try {
            finished = executor.awaitTermination(1, TimeUnit.MINUTES);
        }
        catch (InterruptedException e) {
            getListItineraryWithUserResponseDTO.setResultMessage(ResultMessageController.ERROR_MESSAGE_FAILURE_CLIENT);
            return getListItineraryWithUserResponseDTO;
        }

        if(!finished){
            getListItineraryWithUserResponseDTO.setResultMessage(ResultMessageController.ERROR_MESSAGE_FAILURE_CLIENT);
            return getListItineraryWithUserResponseDTO;
        }


        List<GetItineraryWithUserResponseDTO> listItineraryWithUser = new ArrayList<GetItineraryWithUserResponseDTO>();

        for(int i = 0; i < listItinerary.size(); i++){
            GetItineraryResponseDTO getItineraryResponseDTO = listItinerary.get(i);
            GetUserWithImageResponseDTO getUserWithImageResponseDTO = arrayUser[i];

            GetItineraryWithUserResponseDTO getItineraryWithUserResponseDTO = new GetItineraryWithUserResponseDTO();

            getItineraryWithUserResponseDTO.setId(getItineraryResponseDTO.getId());
            getItineraryWithUserResponseDTO.setName(getItineraryResponseDTO.getName());
            getItineraryWithUserResponseDTO.setDescription(getItineraryResponseDTO.getDescription());
            getItineraryWithUserResponseDTO.setDuration(getItineraryResponseDTO.getDuration());
            getItineraryWithUserResponseDTO.setLenght(getItineraryResponseDTO.getLenght());
            getItineraryWithUserResponseDTO.setDifficulty(getItineraryResponseDTO.getDifficulty());

            if(ResultMessageController.isSuccess(getUserWithImageResponseDTO.getResultMessage())){
                getItineraryWithUserResponseDTO.setUsername(getUserWithImageResponseDTO.getUsername());
                getItineraryWithUserResponseDTO.setUserImage(getUserWithImageResponseDTO.getProfileImage());
            }

            listItineraryWithUser.add(getItineraryWithUserResponseDTO);
        }

        getListItineraryWithUserResponseDTO.setResultMessage(ResultMessageController.SUCCESS_MESSAGE);
        getListItineraryWithUserResponseDTO.setListItinerary(listItineraryWithUser);

        return getListItineraryWithUserResponseDTO;
    }

//MAPPER

    public GetItineraryResponseDTO toGetItineraryResponseDTO(JsonObject jsonObject){
        GetItineraryResponseDTO getItineraryResponseDTO = new GetItineraryResponseDTO();

        if(!jsonObject.has("resultMessage") ){
            JsonObject jsonResultMessage = jsonObject.get("resultMessage").getAsJsonObject();

            long code = jsonResultMessage.get("code").getAsLong();
            String message = jsonResultMessage.get("message").getAsString();

            ResultMessageDTO resultMessageDTO = new ResultMessageDTO(code,message);
            getItineraryResponseDTO.setResultMessage(resultMessageDTO);
        }

        long id = jsonObject.get("id").getAsLong();
        getItineraryResponseDTO.setId(id);

        String name = jsonObject.get("name").getAsString();
        getItineraryResponseDTO.setName(name);

        float duration = jsonObject.get("duration").getAsFloat();
        getItineraryResponseDTO.setDuration(duration);

        float lenght = jsonObject.get("lenght").getAsFloat();
        getItineraryResponseDTO.setLenght(lenght);

        int difficulty = jsonObject.get("difficulty").getAsInt();
        getItineraryResponseDTO.setDifficulty(difficulty);

        if(jsonObject.has("description")) {
            String description = jsonObject.get("description").getAsString();
            getItineraryResponseDTO.setDescription(description);
        }

        long idUser = jsonObject.get("idUser").getAsLong();
        getItineraryResponseDTO.setIdUser(idUser);

        return getItineraryResponseDTO;
    }

    public GetListItineraryResponseDTO toGetListItineraryResponseDTO(JsonObject jsonObject){
        GetListItineraryResponseDTO getListItineraryResponseDTO = new GetListItineraryResponseDTO();

        if(!jsonObject.has("resultMessage") ){
            JsonObject jsonResultMessage = jsonObject.get("resultMessage").getAsJsonObject();

            long code = jsonResultMessage.get("code").getAsLong();
            String message = jsonResultMessage.get("message").getAsString();

            ResultMessageDTO resultMessageDTO = new ResultMessageDTO(code,message);
            getListItineraryResponseDTO.setResultMessage(resultMessageDTO);
        }

        JsonArray jsonArray = jsonObject.get("listUser").getAsJsonArray();
        List<GetItineraryResponseDTO> listItinerary = new ArrayList<GetItineraryResponseDTO>();
        for(JsonElement jsonElement : jsonArray){
            JsonObject jsonObjectElement = jsonElement.getAsJsonObject();

            GetItineraryResponseDTO getItineraryResponseDTO = toGetItineraryResponseDTO(jsonObject);
            listItinerary.add(getItineraryResponseDTO);
        }
        getListItineraryResponseDTO.setListItinerary(listItinerary);

        return getListItineraryResponseDTO;
    }




/*
    @Override
    public ResultMessageDTO addItinerary(SaveItineraryRequestDTO saveItineraryRequest) {
        ResultMessageDTO resultMessageDTO;

        //String username = Amplify.Auth.getCurrentUser().getUsername();
        String username = TEST_USER;


        GPX gpx = saveItineraryRequest.getGpx();
        //can throw IOException
        File file = null;
        try {
            file = FileUtils.toFile(context, gpx);
        } catch (IOException e) {
            resultMessageDTO = ResultMessageController.getFailureMessage();
            return resultMessageDTO;
        }
        RequestBody gpxRequestBody = RequestBody.create(file, MediaType.parse("application/octet-stream"));

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart(BODY_KEY_NAME, saveItineraryRequest.getName())
                .addFormDataPart(BODY_KEY_GPX, file.getName(), gpxRequestBody)
                .addFormDataPart(BODY_KEY_DURATION, saveItineraryRequest.getDuration().toString())
                .addFormDataPart(BODY_KEY_LENGHT, saveItineraryRequest.getLenght().toString())
                .addFormDataPart(BODY_KEY_DIFFICULTY, saveItineraryRequest.getDifficulty().toString())
                .addFormDataPart(BODY_KEY_DESCRIPTION, saveItineraryRequest.getDescription())
                .build();



        String url = URL + ADD_ITINERARY + "?" + PARAM_KEY_USERNAME + "=" + username;

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
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
            resultMessageDTO = ResultMessageController.getFailureMessage();
            return resultMessageDTO;
        }

        if(exception[0] != null){
            resultMessageDTO = ResultMessageController.getFailureMessage();
            return resultMessageDTO;
        }

        ResultMessageDTO result = ResultMessageDAO.toMessageDTO(jsonObjectResult);

        return result;
    }

    //TODO da implementare

    @Override
    public GetListItineraryResponseDTO getListItineraryRandom() {

        ItineraryElementResponseDTO test1 = new ItineraryElementResponseDTO();
        test1.setId(1);
        test1.setDifficulty(1);
        test1.setDescription("1genocide cutter pyroinesis hadoke shoryuken tatsumakisempukiaku genocide cutter pyroinesis hadoke shoryuken tatsumakisempukiaku genocide cutter pyroinesis hadoke shoryuken tatsumakisempukiaku genocide cutter pyroinesis hadoke shoryuken tatsumakisempukiaku genocide cutter pyroinesis hadoke shoryuken tatsumakisempukiaku genocide cutter pyroinesis hadoke shoryuken tatsumakisempukiaku genocide cutter pyroinesis hadoke shoryuken tatsumakisempukiaku genocide cutter pyroinesis hadoke shoryuken tatsumakisempukiaku genocide cutter pyroinesis hadoke shoryuken tatsumakisempukiaku genocide cutter pyroinesis hadoke shoryuken tatsumakisempukiaku genocide cutter pyroinesis hadoke shoryuken tatsumakisempukiaku genocide cutter pyroinesis hadoke shoryuken tatsumakisempukiaku genocide cutter pyroinesis hadoke shoryuken tatsumakisempukiaku genocide cutter pyroinesis hadoke shoryuken tatsumakisempukiaku genocide cutter pyroinesis hadoke shoryuken tatsumakisempukiaku genocide cutter pyroinesis hadoke shoryuken tatsumakisempukiaku" +
                "genocide cutter pyroinesis hadoke shoryuken tatsumakisempukiaku genocide cutter pyroinesis hadoke shoryuken tatsumakisempukiaku genocide cutter pyroinesis hadoke shoryuken tatsumakisempukiaku genocide cutter pyroinesis hadoke shoryuken tatsumakisempukiaku genocide cutter pyroinesis hadoke shoryuken tatsumakisempukiaku genocide cutter pyroinesis hadoke shoryuken tatsumakisempukiaku genocide cutter pyroinesis hadoke shoryuken tatsumakisempukiaku genocide cutter pyroinesis hadoke shoryuken tatsumakisempukiaku genocide cutter pyroinesis hadoke shoryuken tatsumakisempukiaku genocide cutter pyroinesis hadoke shoryuken tatsumakisempukiaku genocide cutter pyroinesis hadoke shoryuken tatsumakisempukiaku genocide cutter pyroinesis hadoke shoryuken tatsumakisempukiaku genocide cutter pyroinesis hadoke shoryuken tatsumakisempukiaku genocide cutter pyroinesis hadoke shoryuken tatsumakisempukiaku genocide cutter pyroinesis hadoke shoryuken tatsumakisempukiaku genocide cutter pyroinesis hadoke shoryuken tatsumakisempukiaku2");
        test1.setDuration(33.3f);
        test1.setLenght(33.3f);
        test1.setName("test1");
        test1.setUserImage(null);
        test1.setUsername("usertest1");

        ItineraryElementResponseDTO test2 = new ItineraryElementResponseDTO();
        test2.setId(2);
        test2.setDifficulty(0);
        test2.setDescription("dsghhjfghdgsfghgjhfgsdfghhgfa");
        test2.setDuration(45.3f);
        test2.setLenght(76.3f);
        test2.setName("test12");
        test2.setUserImage(null);
        test2.setUsername("usertest12");

        ItineraryElementResponseDTO test3 = new ItineraryElementResponseDTO();
        test3.setId(3);
        test3.setDifficulty(2);
        test3.setDescription("afjdfòknf asofòkaòsdmasfnaoienlk ");
        test3.setDuration(88.6f);
        test3.setLenght(234.1f);
        test3.setName("test3");
        test3.setUserImage(null);
        test3.setUsername("usertest3");

        List<ItineraryElementResponseDTO> response = new ArrayList<ItineraryElementResponseDTO>();
        response.add(test1);
        response.add(test2);
        response.add(test3);
        response.add(test1);
        response.add(test2);
        response.add(test3);
        response.add(test1);
        response.add(test2);
        response.add(test3);

        GetListItineraryResponseDTO result = new GetListItineraryResponseDTO();
        ResultMessageDTO resultMessageDTO = ResultMessageController.getSuccessMessage();
        result.setListItinerary(response);
        result.setResultMessage(resultMessageDTO);


        return result;
    }

    @Override
    public GetListItineraryResponseDTO getListItineraryByIdUser(long userId) {
        ItineraryElementResponseDTO test1 = new ItineraryElementResponseDTO();
        test1.setId(1);
        test1.setDifficulty(1);
        test1.setDescription("1genocide cutter pyroinesis hadoke shoryuken tatsumakisempukiaku genocide cutter pyroinesis hadoke shoryuken tatsumakisempukiaku genocide cutter pyroinesis hadoke shoryuken tatsumakisempukiaku genocide cutter pyroinesis hadoke shoryuken tatsumakisempukiaku genocide cutter pyroinesis hadoke shoryuken tatsumakisempukiaku genocide cutter pyroinesis hadoke shoryuken tatsumakisempukiaku genocide cutter pyroinesis hadoke shoryuken tatsumakisempukiaku genocide cutter pyroinesis hadoke shoryuken tatsumakisempukiaku genocide cutter pyroinesis hadoke shoryuken tatsumakisempukiaku genocide cutter pyroinesis hadoke shoryuken tatsumakisempukiaku genocide cutter pyroinesis hadoke shoryuken tatsumakisempukiaku genocide cutter pyroinesis hadoke shoryuken tatsumakisempukiaku genocide cutter pyroinesis hadoke shoryuken tatsumakisempukiaku genocide cutter pyroinesis hadoke shoryuken tatsumakisempukiaku genocide cutter pyroinesis hadoke shoryuken tatsumakisempukiaku genocide cutter pyroinesis hadoke shoryuken tatsumakisempukiaku" +
                "genocide cutter pyroinesis hadoke shoryuken tatsumakisempukiaku genocide cutter pyroinesis hadoke shoryuken tatsumakisempukiaku genocide cutter pyroinesis hadoke shoryuken tatsumakisempukiaku genocide cutter pyroinesis hadoke shoryuken tatsumakisempukiaku genocide cutter pyroinesis hadoke shoryuken tatsumakisempukiaku genocide cutter pyroinesis hadoke shoryuken tatsumakisempukiaku genocide cutter pyroinesis hadoke shoryuken tatsumakisempukiaku genocide cutter pyroinesis hadoke shoryuken tatsumakisempukiaku genocide cutter pyroinesis hadoke shoryuken tatsumakisempukiaku genocide cutter pyroinesis hadoke shoryuken tatsumakisempukiaku genocide cutter pyroinesis hadoke shoryuken tatsumakisempukiaku genocide cutter pyroinesis hadoke shoryuken tatsumakisempukiaku genocide cutter pyroinesis hadoke shoryuken tatsumakisempukiaku genocide cutter pyroinesis hadoke shoryuken tatsumakisempukiaku genocide cutter pyroinesis hadoke shoryuken tatsumakisempukiaku genocide cutter pyroinesis hadoke shoryuken tatsumakisempukiaku2");
        test1.setDuration(33.3f);
        test1.setLenght(33.3f);
        test1.setName("test1");
        test1.setUserImage(null);
        test1.setUsername("usertest1");

        ItineraryElementResponseDTO test2 = new ItineraryElementResponseDTO();
        test2.setId(2);
        test2.setDifficulty(0);
        test2.setDescription("dsghhjfghdgsfghgjhfgsdfghhgfa");
        test2.setDuration(45.3f);
        test2.setLenght(76.3f);
        test2.setName("test12");
        test2.setUserImage(null);
        test2.setUsername("usertest12");

        ItineraryElementResponseDTO test3 = new ItineraryElementResponseDTO();
        test3.setId(3);
        test3.setDifficulty(2);
        test3.setDescription("afjdfòknf asofòkaòsdmasfnaoienlk ");
        test3.setDuration(88.6f);
        test3.setLenght(234.1f);
        test3.setName("test3");
        test3.setUserImage(null);
        test3.setUsername("usertest3");

        List<ItineraryElementResponseDTO> response = new ArrayList<ItineraryElementResponseDTO>();
        response.add(test1);
        response.add(test2);
        response.add(test3);
        response.add(test1);
        response.add(test2);
        response.add(test3);
        response.add(test1);
        response.add(test2);
        response.add(test3);

        GetListItineraryResponseDTO result = new GetListItineraryResponseDTO();
        ResultMessageDTO resultMessageDTO = ResultMessageController.getSuccessMessage();
        result.setListItinerary(response);
        result.setResultMessage(resultMessageDTO);


        return result;
    }

    @Override
    public GetItineraryResponseDTO getItineraryById(long idItinerary) {

        GetItineraryResponseDTO dto = new GetItineraryResponseDTO();

        dto.setName("TEST342");
        dto.setDescription("genosgenosgenos genosgenosgenosgenosgenosgenos genosgenosgenosgenosgenosgenos genosgenosgenosgenosgenosgenos genosgenosgenosgenosgenosgenos genosgenosgenosgenosgenosgenos genosgenosgenosgenosgenosgenos genosgenosgenosgenosgenosgenos genosgenosgenosgenosgenosgenos genosgenosgenosgenosgenosgenos genosgenosgenosgenosgenosgenos genosgenosgenosgenosgenosgenos genosgenosgenosgenosgenosgenos genosgenosgenosgenosgenosgenos genosgenosgenosgenosgenosgenos genosgenosgenosgenosgenosgenos genosgenosgenosgenosgenosgenos genosgenosgenosgenosgenosgenos genosgenosgenosgenosgenosgenos genosgenosgenosgenosgenosgenos genosgenosgenosgenosgenosgenos genosgenosgenosgenosgenosgenos genosgenosgenosgenosgenosgenos genosgenosgenosgenosgenosgenos genosgenosgenosgenosgenosgenos genosgenosgenosgenosgenosgenos genosgenosgenosgenosgenosgenos genosgenosgenosgenosgenosgenos genosgenosgenosgenosgenosgenos genosgenosgenosgenosgenosgenos genosgenosgenosgenosgenosgenos genosgenosgenosgenosgenosgenos genosgenosgenosgenosgenosgenos genosgenosgenosgenosgenosgenos genosgenosgenos");
        //dto.setDescription("sedano armato");
        dto.setDifficulty(1);
        dto.setId(44);
        dto.setHasBeenReported(true);
        dto.setLenght(6666f);
        dto.setDuration(77777f);

        WayPoint wayPoint1 = WayPoint.builder().build(48.20100,16.31651);
        WayPoint wayPoint2 = WayPoint.builder().build(48.20133,16.31610);
        WayPoint wayPoint3 = WayPoint.builder().build(48.20188,16.31569);

        GPX gpx = GPX.builder()
                .addWayPoint(wayPoint1)
                .addWayPoint(wayPoint2)
                .addWayPoint(wayPoint3)
                .addTrack(track -> track
                        .addSegment(segment -> segment
                                .addPoint(p -> p.lat(48.20100).lon(16.31651))
                                .addPoint(p -> p.lat(48.20112).lon(16.31639))
                                .addPoint(p -> p.lat(48.20126).lon(16.31622))
                                .addPoint(p -> p.lat(48.20133).lon(16.31610))
                                .addPoint(p -> p.lat(48.20144).lon(16.31609))
                                .addPoint(p -> p.lat(48.20155).lon(16.31589))
                                .addPoint(p -> p.lat(48.20166).lon(16.31578))
                                .addPoint(p -> p.lat(48.20177).lon(16.31572))
                                .addPoint(p -> p.lat(48.20188).lon(16.31569))
                        )
                )
                .build();

        dto.setGpx(gpx);

        ResultMessageDTO resultMessageDTO = ResultMessageController.getSuccessMessage();
        dto.setResultMessage(resultMessageDTO);

        return dto;
    }

    @Override
    public ResultMessageDTO deleteItineraryById(long idItinerary) {
        return null;
    }

    @Override
    public GetListItineraryResponseDTO getListItineraryByName(String name) {

        ItineraryElementResponseDTO test1 = new ItineraryElementResponseDTO();
        test1.setId(98);
        test1.setDifficulty(2);
        test1.setDescription("1genocide cutter per pyroikisempukiaku genocide cutter pyroinshoryuken tatsumakisempukiaku genocide cutter pyroinesis hadoke shoryuken tatsumakisempukiaku genocide cutter pyroinesis hadoke shoryuken tatsumakisempukiaku genocide cutter pyroinesis hadoke shoryuken tatsumakisempukiaku genocide cutter pyroinesis hadoke shoryuken tatsumakisempukiaku genocide cutter pyroinesis hadoke shoryuken tatsumakisempukiaku genocide cutter pyroinesis hadoke shoryuken tatsumakisempukiaku genocide cutter pyroinesis hadoke shoryuken tatsumakisempukiaku genocide cutter pyroinesis hadoke shoryuken tatsumakisempukiaku genocide cutter pyroinesis hadoke shoryuken tatsumakisempukiaku genocide cutter pyroinesis hadoke shoryuken tatsumakisempukiaku genocide cutter pyroinesis hadoke shoryuken tatsumakisempukiaku genocide cutter pyroinesis hadoke shoryuken tatsumakisempukiaku genocide cutter pyroinesis hadoke shoryuken tatsumakisempukiaku2");
        test1.setDuration(335.3f);
        test1.setLenght(334.3f);
        test1.setName("test154322");
        test1.setUserImage(null);
        test1.setUsername("usertest1432");


        List<ItineraryElementResponseDTO> response = new ArrayList<ItineraryElementResponseDTO>();
        response.add(test1);
        response.add(test1);
        response.add(test1);
        response.add(test1);

        GetListItineraryResponseDTO result = new GetListItineraryResponseDTO();
        ResultMessageDTO resultMessageDTO = ResultMessageController.getSuccessMessage();
        result.setListItinerary(response);
        result.setResultMessage(resultMessageDTO);

        return result;
    }

*/



    /*

    @Override
    public ItineraryListResponseDTO getUserItinearyList(String username) {
        ItineraryElementResponseDTO test1 = new ItineraryElementResponseDTO();
        test1.setItineraryId(5);
        test1.setDifficulty(2);
        test1.setDescription("sasdgsd dgsdfsdfsdfs");
        test1.setDuration(332.3f);
        test1.setLenght(3345.3f);
        test1.setName("test18");


        ItineraryElementResponseDTO test2 = new ItineraryElementResponseDTO();
        test2.setItineraryId(20);
        test2.setDifficulty(1);
        test2.setDescription("dfsfsdfsdfsdfsdfsdfsdfsdfsdfds");
        test2.setDuration(4345.3f);
        test2.setLenght(726.3f);
        test2.setName("test121");




        List<ItineraryElementResponseDTO> response = new ArrayList<ItineraryElementResponseDTO>();
        response.add(test1);
        response.add(test2);
        response.add(test1);
        response.add(test2);
        response.add(test1);
        response.add(test2);
        response.add(test1);
        response.add(test2);

        ItineraryListResponseDTO result = new ItineraryListResponseDTO();
        MessageResponseDTO messageResponseDTO = MessageController.getSuccessMessage();
        result.setItineraries(response);
        result.setResultMessage(messageResponseDTO);

        return result;
    }

    */


}
