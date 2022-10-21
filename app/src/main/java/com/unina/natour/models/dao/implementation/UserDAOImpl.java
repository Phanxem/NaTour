package com.unina.natour.models.dao.implementation;

import android.content.Context;
import android.graphics.Bitmap;

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
import com.unina.natour.dto.response.composted.GetUserWithImageResponseDTO;
import com.unina.natour.models.dao.interfaces.UserDAO;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

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

    private static final String UPDATE_IMAGE = "/update/image";
    private static final String UPDATE_OPTIONAL_INFO = "/update/optionalInfo";



    private static final String BODY_KEY_IMAGE = "image";

    private static final String TAG = "UserDAO";

    private static final String TEST_USER = "user";


    private ResultMessageDAO resultMessageDAO;

    private Context context;

    public UserDAOImpl(Context context){
        this.context = context;
        this.resultMessageDAO = new ResultMessageDAO();
    }


/*
    @Override
    public UserResponseDTO getUser(String username){
        UserResponseDTO userResponseDTO = new UserResponseDTO();

        //String username = Amplify.Auth.getCurrentUser().getUsername();

        String url = URL + GET_USER + "?username=" + username;

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


        //can throw ExecutionException, InterruptedException
        JsonObject jsonObjectResult = null;
        try {
            jsonObjectResult = completableFuture.get();
        }
        catch (ExecutionException | InterruptedException e) {
            MessageResponseDTO messageResponseDTO = MessageController.getFailureMessage();
            userResponseDTO.setResultMessage(messageResponseDTO);
            return userResponseDTO;
        }

        if(exception[0] != null){
            MessageResponseDTO messageResponseDTO = MessageController.getFailureMessage();
            userResponseDTO.setResultMessage(messageResponseDTO);
            return userResponseDTO;
        }


        //RECUPERO L'IMMAGINE---

        url = URL + GET_USER_IMAGE + "?username=" + username;

        request = new Request.Builder()
                .url(url)
                .build();

        client = new OkHttpClient();

        call = client.newCall(request);

        final IOException[] exceptionImage = {null};
        final JsonObject[] jsonObject = {null};
        CompletableFuture<byte[]> completableFutureImage = new CompletableFuture<byte[]>();

        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                exceptionImage[0] = e;
                completableFuture.complete(null);
                return;
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if(!response.isSuccessful()){
                    String jsonStringResult = response.body().string();
                    JsonElement jsonElementResult = JsonParser.parseString(jsonStringResult);
                    jsonObject[0] = jsonElementResult.getAsJsonObject();
                    completableFutureImage.complete(null);
                    return;
                }
                completableFutureImage.complete(response.body().bytes());
            }
        });

        //can throw ExecutionException, InterruptedException
        byte[] bytes = new byte[0];
        try {
            bytes = completableFutureImage.get();
        }
        catch (ExecutionException | InterruptedException e) {
            MessageResponseDTO messageResponseDTO = MessageController.getFailureMessage();
            userResponseDTO.setResultMessage(messageResponseDTO);
            return userResponseDTO;
        }

        if(exception[0] != null){
            MessageResponseDTO messageResponseDTO = MessageController.getFailureMessage();
            userResponseDTO.setResultMessage(messageResponseDTO);
            return userResponseDTO;
        }

        if(jsonObject[0] != null){
            MessageResponseDTO messageResponseDTO = MessageDAOImpl.toMessageDTO(jsonObject[0]);
            userResponseDTO.setResultMessage(messageResponseDTO);
            return userResponseDTO;
        }

        Bitmap bitmap = FileUtils.toBitmap(bytes);


        //can throw MessageException
        UserResponseDTO result = toUserResponseDTO(jsonObjectResult, bitmap);

        return result;
    }
*/

    @Override
    public GetUserResponseDTO getUserById(long idUser) {
        String url = URL + "/get/" + idUser;

        GetUserResponseDTO getUserResponseDTO = getUserResponseDTO(url);

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
            getBitmapResponseDTO.setResultMessage(ResultMessageController.ERROR_MESSAGE_FAILURE_CLIENT);
            return getBitmapResponseDTO;
        }

        if(exception[0] != null){
            getBitmapResponseDTO.setResultMessage(ResultMessageController.ERROR_MESSAGE_FAILURE_CLIENT);
            return getBitmapResponseDTO;
        }

        if(!isSuccessful[0]){
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

        GetUserResponseDTO getUserResponseDTO = getUserResponseDTO(url);

        return getUserResponseDTO;
    }

    @Override
    public GetUserResponseDTO getUserByIdConnection(String idConnection) {
        String url = URL + "/get/connection/" + idConnection;

        GetUserResponseDTO getUserResponseDTO = getUserResponseDTO(url);

        return getUserResponseDTO;

    }


    @Override
    public GetListUserResponseDTO getListUserByUsername(String username, int page) {
        String url = URL + "/search?username=" + username + "&page=" + page;

        GetListUserResponseDTO getListUserResponseDTO = getListUserResponseDTO(url);

        return getListUserResponseDTO;
    }

    @Override
    public GetListUserResponseDTO getListUserWithConversation(long idUser, int page) {
        String url = URL + "/get/" + idUser + "/conversation?page=" + page;

        GetListUserResponseDTO getListUserResponseDTO = getListUserResponseDTO(url);

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
        String url = URL + "/update/" + idUser + "/image";

        File file = null;
        try {
            file = FileUtils.toPngFile(context, profileImage);
        }
        catch (IOException e) {
            return ResultMessageController.ERROR_MESSAGE_FAILURE_CLIENT;
        }

        RequestBody pngRequestBody = RequestBody.create(file, MediaType.parse("application/octet-stream"));

        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        builder.addFormDataPart("image",null, pngRequestBody);

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
        builder.add("placeOfResidence", saveOptionalInfoRequest.getPlaceOfResidence());
        builder.add("dateOfBirth", saveOptionalInfoRequest.getDateOfBirth());

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


    private GetUserResponseDTO getUserResponseDTO(String url){
        GetUserResponseDTO getUserResponseDTO = new GetUserResponseDTO();

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

    private GetListUserResponseDTO getListUserResponseDTO(String url){
        GetListUserResponseDTO getListUserResponseDTO = new GetListUserResponseDTO();

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

        if(!jsonObject.has("resultMessage") ){
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
            String placeOfResidence = jsonObject.get("placeOfResidence").getAsString();
            getUserResponseDTO.setPlaceOfResidence(placeOfResidence);
        }

        if(jsonObject.has("dateOfBirth")){
            String dateOfBirth = jsonObject.get("dateOfBirth").getAsString();
            getUserResponseDTO.setDateOfBirth(dateOfBirth);
        }

        return getUserResponseDTO;
    }

    public GetListUserResponseDTO toGetListUserResponseDTO(JsonObject jsonObject){
        GetListUserResponseDTO getListUserResponseDTO = new GetListUserResponseDTO();

        if(!jsonObject.has("resultMessage") ){
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

            GetUserResponseDTO getUserResponseDTO = toGetUserResponseDTO(jsonObject);
            listUser.add(getUserResponseDTO);
        }
        getListUserResponseDTO.setListUser(listUser);

        return getListUserResponseDTO;
    }





    //composite
    //get user with image
    //get list user with image


    //MOCK


/*
    public GetUserWithImageResponseDTO getUser(String username){
        GetUserWithImageResponseDTO test = new GetUserWithImageResponseDTO();
        test.setProfileImage(null);
        test.setUsername("Jennifer");
        test.setDateOfBirth("04/04/04");
        test.setId(482l);
        test.setPlaceOfResidence("America, latina");


        test.setResultMessage(ResultMessageController.getSuccessMessage());

        return test;
    }

    @Override
    public GetUserWithImageResponseDTO getUser(long id) {
        GetUserWithImageResponseDTO test = new GetUserWithImageResponseDTO();
        test.setProfileImage(null);
        test.setUsername("sdfgfgsf");
        test.setDateOfBirth("03/03/03");
        test.setId(48l);
        test.setPlaceOfResidence("sedano, volato, cazzi");


        test.setResultMessage(ResultMessageController.getSuccessMessage());

        return test;
    }


    @Override
    public ResultMessageDTO updateProfileImage(Bitmap profileImage) {

        //String username = Amplify.Auth.getCurrentUser().getUsername();
        String username = TEST_USER;

        //can throw IOException
        File file = null;
        try {
            file = FileUtils.toPngFile(context, profileImage);
        }
        catch (IOException e) {
            ResultMessageDTO resultMessageDTO = ResultMessageController.getFailureMessage();
            return resultMessageDTO;
        }
        RequestBody pngRequestBody = RequestBody.create(file, MediaType.parse("application/octet-stream"));

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart(BODY_KEY_IMAGE, file.getName(), pngRequestBody)
                .build();


        String url = URL + UPDATE_IMAGE + "?username=" + username;

        Request request = new Request.Builder()
                .url(url)
                .put(requestBody)
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
            ResultMessageDTO resultMessageDTO = ResultMessageController.getFailureMessage();
            return resultMessageDTO;
        }

        if(exception[0] != null){
            ResultMessageDTO resultMessageDTO = ResultMessageController.getFailureMessage();
            return resultMessageDTO;
        }

        ResultMessageDTO result = ResultMessageDAO.toMessageDTO(jsonObjectResult);

        return result;
    }


    @Override
    public ResultMessageDTO updateOptionalInfo(SaveUserOptionalInfoRequestDTO optionalInfo) {

        //String username = Amplify.Auth.getCurrentUser().getUsername();
        String username = TEST_USER;


        FormBody.Builder builder = new FormBody.Builder();
        if(optionalInfo.getPlaceOfResidence() != null) builder.add("placeOfResidence", optionalInfo.getPlaceOfResidence());
        if(optionalInfo.getDateOfBirth() != null) builder.add("dateOfBirth", optionalInfo.getDateOfBirth());

        RequestBody requestBody = builder.build();

        String url = URL + UPDATE_OPTIONAL_INFO + "?username=" + username;

        Request request = new Request.Builder()
                .url(url)
                .put(requestBody)
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
            ResultMessageDTO resultMessageDTO = ResultMessageController.getFailureMessage();
            return resultMessageDTO;
        }

        if(exception[0] != null){
            ResultMessageDTO resultMessageDTO = ResultMessageController.getFailureMessage();
            return resultMessageDTO;
        }


        ResultMessageDTO result = ResultMessageDAO.toMessageDTO(jsonObjectResult);

        return result;
    }

    @Override
    public ResultMessageDTO removeProfileImage() {
        //TODO
        return null;
    }

    @Override
    public ListUserChatResponseDTO findUserChatByConversation() {

        UserChatResponseDTO test1 = new UserChatResponseDTO();
        test1.setProfileImage(null);
        test1.setNameChat("Antonio");
        test1.setId(22l);
        test1.setHasMessagesToRead(false);

        UserChatResponseDTO test2 = new UserChatResponseDTO();
        test2.setProfileImage(null);
        test2.setNameChat("giacomo");
        test2.setId(23l);
        test2.setHasMessagesToRead(true);

        UserChatResponseDTO test3 = new UserChatResponseDTO();
        test3.setProfileImage(null);
        test3.setNameChat("massimo");
        test3.setId(24l);
        test3.setHasMessagesToRead(false);




        List<UserChatResponseDTO> result = new ArrayList<UserChatResponseDTO>();
        result.add(test1);
        result.add(test2);
        result.add(test3);
        result.add(test1);
        result.add(test2);
        result.add(test3);
        result.add(test1);
        result.add(test2);
        result.add(test3);
        result.add(test1);
        result.add(test2);
        result.add(test3);


        ListUserChatResponseDTO listUserChatResponseDTO = new ListUserChatResponseDTO();
        ResultMessageDTO resultMessageDTO = ResultMessageController.getSuccessMessage();
        listUserChatResponseDTO.setResultMessage(resultMessageDTO);
        listUserChatResponseDTO.setUsers(result);

        return listUserChatResponseDTO;
    }

    @Override
    public ListUserChatResponseDTO findUserChatByUsername(String researchString) {

        UserChatResponseDTO test1 = new UserChatResponseDTO();
        test1.setProfileImage(null);
        test1.setNameChat("Mayro");
        test1.setId(224l);
        test1.setHasMessagesToRead(false);

        UserChatResponseDTO test2 = new UserChatResponseDTO();
        test2.setProfileImage(null);
        test2.setNameChat("Sendo");
        test2.setId(234l);
        test2.setHasMessagesToRead(true);

        UserChatResponseDTO test3 = new UserChatResponseDTO();
        test3.setProfileImage(null);
        test3.setNameChat("Massima");
        test3.setId(24l);
        test3.setHasMessagesToRead(true);




        List<UserChatResponseDTO> result = new ArrayList<UserChatResponseDTO>();
        result.add(test1);
        result.add(test2);
        result.add(test3);
        result.add(test1);
        result.add(test2);
        result.add(test3);
        result.add(test1);
        result.add(test2);
        result.add(test3);
        result.add(test1);
        result.add(test2);
        result.add(test3);


        ListUserChatResponseDTO listUserChatResponseDTO = new ListUserChatResponseDTO();
        ResultMessageDTO resultMessageDTO = ResultMessageController.getSuccessMessage();
        listUserChatResponseDTO.setResultMessage(resultMessageDTO);
        listUserChatResponseDTO.setUsers(result);

        return listUserChatResponseDTO;
    }


*/









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
