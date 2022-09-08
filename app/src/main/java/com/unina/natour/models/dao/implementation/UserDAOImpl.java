package com.unina.natour.models.dao.implementation;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.unina.natour.controllers.exceptionHandler.exceptions.ServerException;
import com.unina.natour.dto.MessageDTO;
import com.unina.natour.dto.OptionalInfoDTO;
import com.unina.natour.dto.UserDTO;
import com.unina.natour.models.dao.converters.FileConverter;
import com.unina.natour.models.dao.converters.JsonConverter;
import com.unina.natour.models.dao.interfaces.UserDAO;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.MultipartReader;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

@RequiresApi(api = Build.VERSION_CODES.N)
public class UserDAOImpl implements UserDAO {

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

    @Override
    public UserDTO getUser(String username) throws ExecutionException, InterruptedException, IOException, ServerException {
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
        JsonObject jsonObjectResult = completableFuture.get();

        //can throw IOException
        if(jsonObjectResult == null) throw exception[0];

        //can throw MessageException
        UserDTO result = JsonConverter.toUserDTO(jsonObjectResult);

        return result;
    }


    @Override
    public Bitmap getUserProfileImage(String username) throws ExecutionException, InterruptedException, ServerException, IOException {
        String url = URL + GET_USER_IMAGE + "?username=" + username;

        Request request = new Request.Builder()
                .url(url)
                .build();

        OkHttpClient client = new OkHttpClient();

        Call call = client.newCall(request);

        final IOException[] exception = {null};
        final JsonObject[] jsonObject = {null};
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
                    String jsonStringResult = response.body().string();
                    JsonElement jsonElementResult = JsonParser.parseString(jsonStringResult);
                    jsonObject[0] = jsonElementResult.getAsJsonObject();
                    completableFuture.complete(null);
                }
                completableFuture.complete(response.body().bytes());
            }
        });

        //can throw ExecutionException, InterruptedException
        byte[] bytes = completableFuture.get();

        //can throw IOException, MessageException
        if(bytes == null){
            if(exception[0] == null){
                MessageDTO messageDTO = JsonConverter.toMessageDTO(jsonObject[0]);
                throw new ServerException(messageDTO);
            }
            else throw exception[0];
        }

        Bitmap result = FileConverter.toBitmap(bytes);

        return result;
    }


    @Override
    public MessageDTO updateProfileImage(Bitmap profileImage) throws IOException, ExecutionException, InterruptedException, ServerException {

        //String username = Amplify.Auth.getCurrentUser().getUsername();
        String username = TEST_USER;

        //can throw IOException
        File file = FileConverter.toPngFile(context, profileImage);
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

        JsonObject jsonObjectResult = completableFuture.get();

        if(jsonObjectResult == null) throw exception[0];

        MessageDTO result = JsonConverter.toMessageDTO(jsonObjectResult);

        return result;

    }


    @Override
    public MessageDTO updateOptionalInfo(OptionalInfoDTO optionalInfo) throws ExecutionException, InterruptedException, IOException, ServerException {

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

        JsonObject jsonObjectResult = completableFuture.get();

        if(jsonObjectResult == null) throw exception[0];

        MessageDTO result = JsonConverter.toMessageDTO(jsonObjectResult);

        return result;
    }

    @Override
    public MessageDTO removeProfileImage() {
        //TODO
        return null;
    }
}
