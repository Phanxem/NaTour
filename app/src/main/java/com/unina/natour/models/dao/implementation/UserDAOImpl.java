package com.unina.natour.models.dao.implementation;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.unina.natour.controllers.ResultMessageController;
import com.unina.natour.controllers.utils.FileUtils;
import com.unina.natour.dto.request.SaveUserRequestDTO;
import com.unina.natour.dto.response.GetBitmapResponseDTO;
import com.unina.natour.dto.response.GetListUserResponseDTO;
import com.unina.natour.dto.response.GetUserResponseDTO;
import com.unina.natour.dto.response.ResultMessageDTO;
import com.unina.natour.dto.request.SaveUserOptionalInfoRequestDTO;
import com.unina.natour.dto.response.composted.GetListUserWithImageResponseDTO;
import com.unina.natour.dto.response.composted.GetUserWithImageResponseDTO;
import com.unina.natour.models.dao.interfaces.UserDAO;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UserDAOImpl extends ServerDAO implements UserDAO {

    private static final String URL = SERVER_URL + "/user";


    private ResultMessageDAO resultMessageDAO;

    private Context context;

    public UserDAOImpl(Context context){
        this.context = context;
        this.resultMessageDAO = new ResultMessageDAO();
    }


    @Override
    public GetUserResponseDTO getUserById(long idUser) {
        String url = URL + "/get/" + idUser;

        Request request = new Request.Builder()
                .url(url)
                .build();

        GetUserResponseDTO getUserResponseDTO = getUserResponseDTO(request);

        return getUserResponseDTO;
    }

    @Override
    public GetBitmapResponseDTO getUserImageById(long idUser) {
        GetBitmapResponseDTO getBitmapResponseDTO = new GetBitmapResponseDTO();

        String url = URL + "/get/" + idUser + "/image";

        Request request = new Request.Builder()
                .url(url)
                .build();

        OkHttpClient client = new OkHttpClient();

        Call call = client.newCall(request);

        final IOException[] exception = {null};
        final boolean[] isSuccessful = {true};
        final boolean[] isAutorized = {true};
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

                    if(response.code() == HttpURLConnection.HTTP_UNAUTHORIZED ||
                       response.code() == HttpURLConnection.HTTP_FORBIDDEN)
                    {
                        isAutorized[0] = false;
                    }

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
            getBitmapResponseDTO.setResultMessage(ResultMessageController.ERROR_MESSAGE_FAILURE_CLIENT);
            return getBitmapResponseDTO;
        }

        if(exception[0] != null){
            getBitmapResponseDTO.setResultMessage(ResultMessageController.ERROR_MESSAGE_FAILURE_CLIENT);
            return getBitmapResponseDTO;
        }

        if(!isSuccessful[0]){
            if(!isAutorized[0]){
                getBitmapResponseDTO.setResultMessage(ResultMessageController.ERROR_MESSAGE_UNAUTORIZED);
                return getBitmapResponseDTO;
            }
            getBitmapResponseDTO.setResultMessage(ResultMessageController.ERROR_MESSAGE_FAILURE_CLIENT);
            return getBitmapResponseDTO;
        }

        Bitmap bitmap = FileUtils.toBitmap(bytes);


        getBitmapResponseDTO.setBitmap(bitmap);
        getBitmapResponseDTO.setResultMessage(ResultMessageController.SUCCESS_MESSAGE);

        return getBitmapResponseDTO;
    }

    @Override
    public GetUserResponseDTO getUserByIdP(String identityProvider, String idIdentityProvided) {
        String url = URL + "/get/identityProvider?identityProvider=" + identityProvider + "&idIdentityProvided=" + idIdentityProvided;

        Request request = new Request.Builder()
                .url(url)
                .build();

        GetUserResponseDTO getUserResponseDTO = getUserResponseDTO(request);

        return getUserResponseDTO;
    }

    @Override
    public GetUserResponseDTO getUserByIdConnection(String idConnection) {
        String url = URL + "/get/connection/" + idConnection;

        Request request = new Request.Builder()
                .url(url)
                .build();

        GetUserResponseDTO getUserResponseDTO = getUserResponseDTO(request);

        return getUserResponseDTO;

    }


    @Override
    public GetListUserResponseDTO getListUserByUsername(String username, int page) {
        String url = URL + "/search?username=" + username + "&page=" + page;

        Request request = new Request.Builder()
                .url(url)
                .build();

        GetListUserResponseDTO getListUserResponseDTO = getListUserResponseDTO(request);

        return getListUserResponseDTO;
    }

    @Override
    public GetListUserResponseDTO getListUserWithConversation(long idUser, int page) {
        String url = URL + "/get/" + idUser + "/conversation?page=" + page;

        Request request = new Request.Builder()
                .url(url)
                .build();

        GetListUserResponseDTO getListUserResponseDTO = getListUserResponseDTO(request);

        return getListUserResponseDTO;
    }







    @Override
    public ResultMessageDTO addUser(SaveUserRequestDTO saveUserRequest) {
        String url = URL + "/add";

        FormBody.Builder builder = new FormBody.Builder();
        builder.add("username", saveUserRequest.getUsername());
        builder.add("identityProvider", saveUserRequest.getIdentityProvider());
        builder.add("idIdentityProvided", saveUserRequest.getIdIdentityProvided());

        RequestBody requestBody = builder.build();

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        ResultMessageDTO resultMessageDTO = resultMessageDAO.fulfilRequest(request);
        return resultMessageDTO;
    }

    @Override
    public ResultMessageDTO updateProfileImage(long idUser, Bitmap profileImage) {

        String url =  URL + "/update/" + idUser + "/image";
        File file = null;
        try {
            file = FileUtils.toPngFile(context, profileImage);
        }
        catch (IOException e) {
            Log.e("UserDAO", "conversione fallita");
            return ResultMessageController.ERROR_MESSAGE_FAILURE_CLIENT;
        }

        RequestBody pngRequestBody = RequestBody.create(file, MediaType.parse("application/octet-stream"));

        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        builder.addFormDataPart("image", file.getName(), pngRequestBody);

        RequestBody requestBody = builder.build();

        Request request = new Request.Builder()
                .url(url)
                .put(requestBody)
                .build();

        ResultMessageDTO resultMessageDTO = resultMessageDAO.fulfilRequest(request);
        return resultMessageDTO;
    }

    @Override
    public ResultMessageDTO updateOptionalInfo(long idUser, SaveUserOptionalInfoRequestDTO saveOptionalInfoRequest) {
        String url = URL + "/update/" + idUser + "/optionalInfo";

        FormBody.Builder builder = new FormBody.Builder();
        if(saveOptionalInfoRequest.getPlaceOfResidence() != null){
            builder.add("placeOfResidence", saveOptionalInfoRequest.getPlaceOfResidence());
        }
        if(saveOptionalInfoRequest.getDateOfBirth() != null) {
            builder.add("dateOfBirth", saveOptionalInfoRequest.getDateOfBirth());
        }
        RequestBody requestBody = builder.build();


        Request request = new Request.Builder()
                .url(url)
                .put(requestBody)
                .build();

        ResultMessageDTO resultMessageDTO = resultMessageDAO.fulfilRequest(request);
        return resultMessageDTO;
    }



    @Override
    public ResultMessageDTO cancelRegistrationUser(String idCognitoUser) {
        String url = URL + "/delete?idIdentityProvided=" + idCognitoUser;

        Request request = new Request.Builder()
                .url(url)
                .delete()
                .build();

        ResultMessageDTO resultMessageDTO = resultMessageDAO.fulfilRequest(request);
        return resultMessageDTO;
    }



    @Override
    public GetUserWithImageResponseDTO getUserWithImageById(long idUser){
        GetUserWithImageResponseDTO getUserWithImageResponseDTO = new GetUserWithImageResponseDTO();

        GetUserResponseDTO getUserResponseDTO = getUserById(idUser);
        if(!ResultMessageController.isSuccess(getUserResponseDTO.getResultMessage())){
            getUserWithImageResponseDTO.setResultMessage(getUserResponseDTO.getResultMessage());
            return getUserWithImageResponseDTO;
        }

        GetBitmapResponseDTO getBitmapResponseDTO = getUserImageById(idUser);
        if(!ResultMessageController.isSuccess(getBitmapResponseDTO.getResultMessage())){
            getUserWithImageResponseDTO.setResultMessage(getBitmapResponseDTO.getResultMessage());
            return getUserWithImageResponseDTO;
        }

        getUserWithImageResponseDTO.setId(getUserResponseDTO.getId());
        getUserWithImageResponseDTO.setUsername(getUserResponseDTO.getUsername());
        getUserWithImageResponseDTO.setProfileImage(getBitmapResponseDTO.getBitmap());
        getUserWithImageResponseDTO.setPlaceOfResidence(getUserResponseDTO.getPlaceOfResidence());
        getUserWithImageResponseDTO.setDateOfBirth(getUserResponseDTO.getDateOfBirth());

        getUserWithImageResponseDTO.setResultMessage(ResultMessageController.SUCCESS_MESSAGE);

        return getUserWithImageResponseDTO;
    }

    @Override
    public GetListUserWithImageResponseDTO getListUserWithImageByUsername(String username, int page) {

        GetListUserWithImageResponseDTO getListUserWithImageResponseDTO = new GetListUserWithImageResponseDTO();

        GetListUserResponseDTO getListUserResponseDTO = getListUserByUsername(username, page);
        if(!ResultMessageController.isSuccess(getListUserResponseDTO.getResultMessage())){
            getListUserWithImageResponseDTO.setResultMessage(getListUserResponseDTO.getResultMessage());
            return getListUserWithImageResponseDTO;
        }

        List<GetUserResponseDTO> listUser = getListUserResponseDTO.getListUser();

        GetBitmapResponseDTO[] arrayImage = new GetBitmapResponseDTO[listUser.size()];

        if(listUser.isEmpty()){
            List<GetUserWithImageResponseDTO> listUserDTO = new ArrayList<GetUserWithImageResponseDTO>();
            getListUserWithImageResponseDTO.setListUser(listUserDTO);
            getListUserWithImageResponseDTO.setResultMessage(ResultMessageController.SUCCESS_MESSAGE);

            return getListUserWithImageResponseDTO;
        }
        ExecutorService executor = Executors.newFixedThreadPool(listUser.size());

        for(int i = 0; i < listUser.size(); i++){
            int iTemp = i;

            Runnable runnable = new Runnable() {
                @Override
                public void run() {

                    GetUserResponseDTO user = listUser.get(iTemp);

                    GetBitmapResponseDTO getBitmapResponseDTO = getUserImageById(user.getId());

                    arrayImage[iTemp] = getBitmapResponseDTO;
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
            getListUserWithImageResponseDTO.setResultMessage(ResultMessageController.ERROR_MESSAGE_FAILURE_CLIENT);
            return getListUserWithImageResponseDTO;
        }

        if(!finished){
            getListUserWithImageResponseDTO.setResultMessage(ResultMessageController.ERROR_MESSAGE_FAILURE_CLIENT);
            return getListUserWithImageResponseDTO;
        }

        List<GetUserWithImageResponseDTO> listUserImage = new ArrayList<GetUserWithImageResponseDTO>();
        for(int i = 0; i < listUser.size(); i++){
            GetUserWithImageResponseDTO getUserWithImageResponseDTO = new GetUserWithImageResponseDTO();

            GetUserResponseDTO getUserResponseDTO = listUser.get(i);
            GetBitmapResponseDTO getBitmapResponseDTO = arrayImage[i];

            getUserWithImageResponseDTO.setId(getUserResponseDTO.getId());
            getUserWithImageResponseDTO.setUsername(getUserResponseDTO.getUsername());
            getUserWithImageResponseDTO.setProfileImage(getBitmapResponseDTO.getBitmap());
            getUserWithImageResponseDTO.setPlaceOfResidence(getUserResponseDTO.getPlaceOfResidence());
            getUserWithImageResponseDTO.setDateOfBirth(getUserResponseDTO.getDateOfBirth());

            listUserImage.add(getUserWithImageResponseDTO);
        }

        getListUserWithImageResponseDTO.setListUser(listUserImage);
        getListUserWithImageResponseDTO.setResultMessage(ResultMessageController.SUCCESS_MESSAGE);

        return getListUserWithImageResponseDTO;
    }




    private GetUserResponseDTO getUserResponseDTO(Request request){
        GetUserResponseDTO getUserResponseDTO = new GetUserResponseDTO();


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
            getUserResponseDTO.setResultMessage(ResultMessageController.ERROR_MESSAGE_FAILURE_CLIENT);
            return getUserResponseDTO;
        }

        if(exception[0] != null){
            getUserResponseDTO.setResultMessage(ResultMessageController.ERROR_MESSAGE_FAILURE_CLIENT);
            return getUserResponseDTO;
        }

        ResultMessageDTO resultMessage = ResultMessageDAO.getResultMessage(jsonObjectResult);

        if(!ResultMessageController.isSuccess(resultMessage)){
            getUserResponseDTO.setResultMessage(resultMessage);
            return getUserResponseDTO;
        }

        getUserResponseDTO = toGetUserResponseDTO(jsonObjectResult);

        return getUserResponseDTO;
    }

    private GetListUserResponseDTO getListUserResponseDTO(Request request){
        GetListUserResponseDTO getListUserResponseDTO = new GetListUserResponseDTO();

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
            getListUserResponseDTO.setResultMessage(ResultMessageController.ERROR_MESSAGE_FAILURE_CLIENT);
            return getListUserResponseDTO;
        }

        if(exception[0] != null){
            getListUserResponseDTO.setResultMessage(ResultMessageController.ERROR_MESSAGE_FAILURE_CLIENT);
            return getListUserResponseDTO;
        }

        ResultMessageDTO resultMessage = ResultMessageDAO.getResultMessage(jsonObjectResult);

        if(!ResultMessageController.isSuccess(resultMessage)){
            getListUserResponseDTO.setResultMessage(resultMessage);
            return getListUserResponseDTO;
        }

        getListUserResponseDTO = toGetListUserResponseDTO(jsonObjectResult);

        return getListUserResponseDTO;
    }


//MAPPER

    public GetUserResponseDTO toGetUserResponseDTO(JsonObject jsonObject){
        GetUserResponseDTO getUserResponseDTO = new GetUserResponseDTO();

        if(jsonObject.has("resultMessage") ){
            JsonObject jsonResultMessage = jsonObject.get("resultMessage").getAsJsonObject();

            long code = jsonResultMessage.get("code").getAsLong();
            String message = jsonResultMessage.get("message").getAsString();

            ResultMessageDTO resultMessageDTO = new ResultMessageDTO(code,message);
            getUserResponseDTO.setResultMessage(resultMessageDTO);
        }

        long id = jsonObject.get("id").getAsLong();
        getUserResponseDTO.setId(id);

        String username = jsonObject.get("username").getAsString();
        getUserResponseDTO.setUsername(username);

        if(jsonObject.has("placeOfResidence")){
            JsonElement jsonElement = jsonObject.get("placeOfResidence");
            if(!jsonElement.isJsonNull()){
                String placeOfResidence = jsonObject.get("placeOfResidence").getAsString();
                getUserResponseDTO.setPlaceOfResidence(placeOfResidence);
            }
        }

        if(jsonObject.has("dateOfBirth")){
            JsonElement jsonElement = jsonObject.get("dateOfBirth");
            if(!jsonElement.isJsonNull()){
                String dateOfBirth = jsonObject.get("dateOfBirth").getAsString();
                getUserResponseDTO.setDateOfBirth(dateOfBirth);
            }
        }

        return getUserResponseDTO;
    }

    public GetListUserResponseDTO toGetListUserResponseDTO(JsonObject jsonObject){
        GetListUserResponseDTO getListUserResponseDTO = new GetListUserResponseDTO();

        if(jsonObject.has("resultMessage") ){
            JsonObject jsonResultMessage = jsonObject.get("resultMessage").getAsJsonObject();

            long code = jsonResultMessage.get("code").getAsLong();
            String message = jsonResultMessage.get("message").getAsString();

            ResultMessageDTO resultMessageDTO = new ResultMessageDTO(code,message);
            getListUserResponseDTO.setResultMessage(resultMessageDTO);
        }

        JsonArray jsonArray = jsonObject.get("listUser").getAsJsonArray();
        List<GetUserResponseDTO> listUser = new ArrayList<GetUserResponseDTO>();
        for(JsonElement jsonElement : jsonArray){
            JsonObject jsonObjectElement = jsonElement.getAsJsonObject();

            GetUserResponseDTO getUserResponseDTO = toGetUserResponseDTO(jsonObjectElement);
            listUser.add(getUserResponseDTO);
        }
        getListUserResponseDTO.setListUser(listUser);

        return getListUserResponseDTO;
    }



    public GetUserWithImageResponseDTO toGetUserResponseDTO(JsonObject jsonObject, Bitmap bitmap){
        GetUserWithImageResponseDTO getUserWithImageResponseDTO = new GetUserWithImageResponseDTO();

        if(!jsonObject.has("id")  || !jsonObject.has("username") ) {
            ResultMessageDTO resultMessageDTO = ResultMessageDAO.getResultMessage(jsonObject);
            getUserWithImageResponseDTO.setResultMessage(resultMessageDTO);
        }

        long id = jsonObject.get("id").getAsLong();
        String username = jsonObject.get("username").getAsString();

        getUserWithImageResponseDTO.setId(id);
        getUserWithImageResponseDTO.setUsername(username);

        if(jsonObject.has("placeOfResidence")){
            String placeOfResidence = jsonObject.get("placeOfResidence").getAsString();
            getUserWithImageResponseDTO.setPlaceOfResidence(placeOfResidence);
        }

        if(jsonObject.has("dateOfBirth")){
            String dateOfBirth = jsonObject.get("dateOfBirth").getAsString();
            getUserWithImageResponseDTO.setDateOfBirth(dateOfBirth);
        }

        getUserWithImageResponseDTO.setProfileImage(bitmap);

        return getUserWithImageResponseDTO;
    }
}
