package com.unina.natour.models.dao.implementation;

import android.content.Context;
import android.util.Log;

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
import com.unina.natour.dto.response.composted.GetItineraryWithGpxAndUserAndReportResponseDTO;
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
        //String url = URL + "/add";

        String url = "https://m4xyqnli3i.execute-api.eu-west-1.amazonaws.com/Production/itinerary/add";

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
        builder.addFormDataPart("gpx", file.getName(), gpxRequestBody);

        String stringDuration = String.valueOf(saveItineraryRequest.getDuration());
        builder.addFormDataPart("duration", stringDuration);

        String stringLenght = String.valueOf(saveItineraryRequest.getLenght());
        builder.addFormDataPart("lenght", stringLenght);

        String stringDifficulty = String.valueOf(saveItineraryRequest.getDifficulty());
        builder.addFormDataPart("difficulty", stringDifficulty);

        builder.addFormDataPart("description", saveItineraryRequest.getDescription());

        builder.addFormDataPart("idUser", saveItineraryRequest.getIdUser());


        RequestBody requestBody = builder.build();

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();



        Request signedRequest;
        try {
            signedRequest = ServerDAO.signRequest(request, context);
        } catch (Exception e) {
            signedRequest = request;
        }

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
    public GetItineraryWithGpxAndUserAndReportResponseDTO getItineraryWithGpxAndUserAndReportById(long idItinerary) {
        GetItineraryWithGpxAndUserAndReportResponseDTO getItineraryWithGpxAndUserAndReportResponseDTO = new GetItineraryWithGpxAndUserAndReportResponseDTO();

        GetItineraryResponseDTO getItineraryResponseDTO = getItineraryById(idItinerary);
        if(!ResultMessageController.isSuccess(getItineraryResponseDTO.getResultMessage())){
            getItineraryWithGpxAndUserAndReportResponseDTO.setResultMessage(getItineraryResponseDTO.getResultMessage());
            return getItineraryWithGpxAndUserAndReportResponseDTO;
        }

        GetListReportResponseDTO getListReportResponseDTO = reportDAO.getReportByIdItinerary(idItinerary);
        if(!ResultMessageController.isSuccess(getItineraryResponseDTO.getResultMessage())){
            getItineraryWithGpxAndUserAndReportResponseDTO.setResultMessage(getListReportResponseDTO.getResultMessage());
            return getItineraryWithGpxAndUserAndReportResponseDTO;
        }

        GetGpxResponseDTO getGpxResponseDTO = getItineraryGpxById(idItinerary);
        if(!ResultMessageController.isSuccess(getItineraryResponseDTO.getResultMessage())){
            getItineraryWithGpxAndUserAndReportResponseDTO.setResultMessage(getGpxResponseDTO.getResultMessage());
            return getItineraryWithGpxAndUserAndReportResponseDTO;
        }

        GetUserWithImageResponseDTO getUserWithImageResponseDTO = userDAO.getUserWithImageById(getItineraryResponseDTO.getIdUser());
        if(!ResultMessageController.isSuccess(getUserWithImageResponseDTO.getResultMessage())){
            getItineraryWithGpxAndUserAndReportResponseDTO.setResultMessage(getGpxResponseDTO.getResultMessage());
            return getItineraryWithGpxAndUserAndReportResponseDTO;
        }

        getItineraryWithGpxAndUserAndReportResponseDTO.setId(getItineraryResponseDTO.getId());
        getItineraryWithGpxAndUserAndReportResponseDTO.setName(getItineraryResponseDTO.getName());
        getItineraryWithGpxAndUserAndReportResponseDTO.setDescription(getItineraryResponseDTO.getDescription());
        getItineraryWithGpxAndUserAndReportResponseDTO.setDifficulty(getItineraryResponseDTO.getDifficulty());
        getItineraryWithGpxAndUserAndReportResponseDTO.setDuration(getItineraryResponseDTO.getDuration());
        getItineraryWithGpxAndUserAndReportResponseDTO.setLenght(getItineraryResponseDTO.getLenght());

        getItineraryWithGpxAndUserAndReportResponseDTO.setIdUser(getItineraryResponseDTO.getIdUser());
        getItineraryWithGpxAndUserAndReportResponseDTO.setUsername(getUserWithImageResponseDTO.getUsername());
        getItineraryWithGpxAndUserAndReportResponseDTO.setProfileImage(getUserWithImageResponseDTO.getProfileImage());

        List<GetReportResponseDTO> listReport = getListReportResponseDTO.getListReport();

        if(listReport.isEmpty()) getItineraryWithGpxAndUserAndReportResponseDTO.setReported(false);
        else getItineraryWithGpxAndUserAndReportResponseDTO.setReported(true);


        getItineraryWithGpxAndUserAndReportResponseDTO.setGpx(getGpxResponseDTO.getGpx());

        getItineraryWithGpxAndUserAndReportResponseDTO.setResultMessage(ResultMessageController.SUCCESS_MESSAGE);

        return getItineraryWithGpxAndUserAndReportResponseDTO;
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

        if(listItinerary.isEmpty()){
            getListItineraryWithUserResponseDTO.setListItinerary(new ArrayList<GetItineraryWithUserResponseDTO>());
            getListItineraryWithUserResponseDTO.setResultMessage(ResultMessageController.SUCCESS_MESSAGE);
            return getListItineraryWithUserResponseDTO;
        }

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
        executor.shutdown();

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

        Log.e("TAG-->", jsonObject.toString() );

        if(jsonObject.has("resultMessage") ){
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

        if(jsonObject.has("resultMessage") ){
            JsonObject jsonResultMessage = jsonObject.get("resultMessage").getAsJsonObject();

            long code = jsonResultMessage.get("code").getAsLong();
            String message = jsonResultMessage.get("message").getAsString();

            ResultMessageDTO resultMessageDTO = new ResultMessageDTO(code,message);
            getListItineraryResponseDTO.setResultMessage(resultMessageDTO);
        }

        JsonArray jsonArray = jsonObject.get("listItinerary").getAsJsonArray();
        List<GetItineraryResponseDTO> listItinerary = new ArrayList<GetItineraryResponseDTO>();
        for(JsonElement jsonElement : jsonArray){
            JsonObject jsonObjectElement = jsonElement.getAsJsonObject();

            GetItineraryResponseDTO getItineraryResponseDTO = toGetItineraryResponseDTO(jsonObjectElement);
            listItinerary.add(getItineraryResponseDTO);
        }
        getListItineraryResponseDTO.setListItinerary(listItinerary);

        return getListItineraryResponseDTO;
    }


}
