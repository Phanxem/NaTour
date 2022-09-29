package com.unina.natour.models.dao.implementation;

import android.content.Context;
import android.graphics.Bitmap;

import androidx.annotation.NonNull;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.unina.natour.controllers.MessageController;
import com.unina.natour.controllers.utils.FileUtils;
import com.unina.natour.dto.response.MessageResponseDTO;
import com.unina.natour.dto.request.OptionalInfoRequestDTO;
import com.unina.natour.dto.response.UserChatResponseDTO;
import com.unina.natour.dto.response.UserChatListResponseDTO;
import com.unina.natour.dto.response.UserResponseDTO;
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

    private static final String GET_USER = "/get";
    private static final String GET_USER_IMAGE = "/get/image";

    private static final String BODY_KEY_IMAGE = "image";

    private static final String TAG = "UserDAO";

    private static final String TEST_USER = "user";

    private Context context;

    public UserDAOImpl(Context context){
        this.context = context;
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

    public UserResponseDTO getUser(String username){
        UserResponseDTO test = new UserResponseDTO();
        test.setProfileImage(null);
        test.setUsername("Jennifer");
        test.setDateOfBirth("04/04/04");
        test.setId(482l);
        test.setPlaceOfResidence("America, latina");
        test.setFacebookLinked(false);
        test.setGoogleLinked(false);

        test.setResultMessage(MessageController.getSuccessMessage());

        return test;
    }

    @Override
    public UserResponseDTO getUser(long id) {
        UserResponseDTO test = new UserResponseDTO();
        test.setProfileImage(null);
        test.setUsername("sdfgfgsf");
        test.setDateOfBirth("03/03/03");
        test.setId(48l);
        test.setPlaceOfResidence("sedano, volato, cazzi");
        test.setFacebookLinked(false);
        test.setGoogleLinked(false);

        test.setResultMessage(MessageController.getSuccessMessage());

        return test;
    }


    @Override
    public MessageResponseDTO updateProfileImage(Bitmap profileImage) {

        //String username = Amplify.Auth.getCurrentUser().getUsername();
        String username = TEST_USER;

        //can throw IOException
        File file = null;
        try {
            file = FileUtils.toPngFile(context, profileImage);
        }
        catch (IOException e) {
            MessageResponseDTO messageResponseDTO = MessageController.getFailureMessage();
            return messageResponseDTO;
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
            MessageResponseDTO messageResponseDTO = MessageController.getFailureMessage();
            return messageResponseDTO;
        }

        if(exception[0] != null){
            MessageResponseDTO messageResponseDTO = MessageController.getFailureMessage();
            return messageResponseDTO;
        }

        MessageResponseDTO result = MessageDAOImpl.toMessageDTO(jsonObjectResult);

        return result;
    }


    @Override
    public MessageResponseDTO updateOptionalInfo(OptionalInfoRequestDTO optionalInfo) {

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
            MessageResponseDTO messageResponseDTO = MessageController.getFailureMessage();
            return messageResponseDTO;
        }

        if(exception[0] != null){
            MessageResponseDTO messageResponseDTO = MessageController.getFailureMessage();
            return messageResponseDTO;
        }


        MessageResponseDTO result = MessageDAOImpl.toMessageDTO(jsonObjectResult);

        return result;
    }

    @Override
    public MessageResponseDTO removeProfileImage() {
        //TODO
        return null;
    }

    @Override
    public UserChatListResponseDTO findUserChatByConversation() {

        UserChatResponseDTO test1 = new UserChatResponseDTO();
        test1.setProfileImage(null);
        test1.setUsername("Antonio");
        test1.setId(22l);
        test1.setMessagesToRead(false);

        UserChatResponseDTO test2 = new UserChatResponseDTO();
        test2.setProfileImage(null);
        test2.setUsername("giacomo");
        test2.setId(23l);
        test2.setMessagesToRead(true);

        UserChatResponseDTO test3 = new UserChatResponseDTO();
        test3.setProfileImage(null);
        test3.setUsername("massimo");
        test3.setId(24l);
        test3.setMessagesToRead(false);




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


        UserChatListResponseDTO userChatListResponseDTO = new UserChatListResponseDTO();
        MessageResponseDTO messageResponseDTO = MessageController.getSuccessMessage();
        userChatListResponseDTO.setResultMessage(messageResponseDTO);
        userChatListResponseDTO.setUsers(result);

        return userChatListResponseDTO;
    }

    @Override
    public UserChatListResponseDTO findUserChatByUsername(String researchString) {

        UserChatResponseDTO test1 = new UserChatResponseDTO();
        test1.setProfileImage(null);
        test1.setUsername("Mayro");
        test1.setId(224l);
        test1.setMessagesToRead(false);

        UserChatResponseDTO test2 = new UserChatResponseDTO();
        test2.setProfileImage(null);
        test2.setUsername("Sendo");
        test2.setId(234l);
        test2.setMessagesToRead(true);

        UserChatResponseDTO test3 = new UserChatResponseDTO();
        test3.setProfileImage(null);
        test3.setUsername("Massima");
        test3.setId(24l);
        test3.setMessagesToRead(true);




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


        UserChatListResponseDTO userChatListResponseDTO = new UserChatListResponseDTO();
        MessageResponseDTO messageResponseDTO = MessageController.getSuccessMessage();
        userChatListResponseDTO.setResultMessage(messageResponseDTO);
        userChatListResponseDTO.setUsers(result);

        return userChatListResponseDTO;
    }







    public UserResponseDTO toUserResponseDTO(JsonObject jsonObject, Bitmap bitmap){
        UserResponseDTO userResponseDTO = new UserResponseDTO();

        if(!jsonObject.has("id")  || !jsonObject.has("username") ) {
            MessageResponseDTO messageResponseDTO = MessageDAOImpl.toMessageDTO(jsonObject);
            userResponseDTO.setResultMessage(messageResponseDTO);
        }

        long id = jsonObject.get("id").getAsLong();
        String username = jsonObject.get("username").getAsString();

        userResponseDTO.setId(id);
        userResponseDTO.setUsername(username);

        if(jsonObject.has("placeOfResidence")){
            String placeOfResidence = jsonObject.get("placeOfResidence").getAsString();
            userResponseDTO.setPlaceOfResidence(placeOfResidence);
        }

        if(jsonObject.has("dateOfBirth")){
            String dateOfBirth = jsonObject.get("dateOfBirth").getAsString();
            userResponseDTO.setDateOfBirth(dateOfBirth);
        }

        userResponseDTO.setProfileImage(bitmap);

        return userResponseDTO;
    }
}
