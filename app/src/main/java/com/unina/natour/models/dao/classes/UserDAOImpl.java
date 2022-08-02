package com.unina.natour.models.dao.classes;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.unina.natour.dto.MessageDTO;
import com.unina.natour.dto.OptionalInfoDTO;
import com.unina.natour.models.dao.interfaces.UserDAO;
import com.unina.natour.models.dao.jsonConverter.JsonConverter;

import java.io.File;
import java.io.IOException;
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

public class UserDAOImpl implements UserDAO {

    private static final String SERVER_URL = "http://192.168.1.3:8080/user";
    private static final String UPDATE_IMAGE = "/update/image";
    private static final String UPDATE_OPTIONA_INFO = "/update/optionalInfo";

    private static final String TAG = "UserDAO";

    private static final String TEST_USER = "user2";

    private Context context;

    public UserDAOImpl(Context context){
        this.context = context;
    }

    @Override
    public MessageDTO updateProfileImage(Bitmap profileImage) {

        File file = JsonConverter.toFile(context, profileImage);

        //String username = Amplify.Auth.getCurrentUser().getUsername();
        String username = TEST_USER;

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("image",
                        file.getName(),
                        RequestBody.create(file,
                                MediaType.parse("application/octet-stream")
                        )
                ).build();




        //---

        String url = SERVER_URL + UPDATE_IMAGE + "?username=" + username;

        Request request = new Request.Builder()
                .url(url)
                .put(requestBody)
                .build();

        OkHttpClient client = new OkHttpClient();

        Call call = client.newCall(request);

        CompletableFuture<MessageDTO> completableFuture = new CompletableFuture<MessageDTO>();

        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e("ERRORE: ", e.getMessage(), e);

                completableFuture.complete(null);
                return;
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if(!response.isSuccessful()){
                    Log.e("ERRORE: ","Risposta chiamata api fallita");
                    completableFuture.complete(null);
                    return;
                }


                String jsonStringResult = response.body().string();
                Log.i("JSON: ", jsonStringResult);

                JsonElement jsonElementResult = JsonParser.parseString(jsonStringResult);
                JsonObject jsonObjectResult = jsonElementResult.getAsJsonObject();

                MessageDTO messageDTO = JsonConverter.toMessageDTO(jsonObjectResult);

                completableFuture.complete(messageDTO);
            }
        });

        MessageDTO result = null;



        try {
            result = completableFuture.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return result;

    }

    @Override
    public MessageDTO updateOptionalInfo(OptionalInfoDTO optionalInfo) {

        //String username = Amplify.Auth.getCurrentUser().getUsername();
        String username = TEST_USER;



        FormBody.Builder builder = new FormBody.Builder();
        if(optionalInfo.getPlaceOfResidence() != null) builder.add("placeOfResidence", optionalInfo.getPlaceOfResidence());
        if(optionalInfo.getDateOfBirth() != null) builder.add("dateOfBirth", optionalInfo.getDateOfBirth());

        RequestBody requestBody = builder.build();

        String url = SERVER_URL + UPDATE_OPTIONA_INFO + "?username=" + username;

        Request request = new Request.Builder()
                .url(url)
                .put(requestBody)
                .build();

        OkHttpClient client = new OkHttpClient();

        Call call = client.newCall(request);

        CompletableFuture<MessageDTO> completableFuture = new CompletableFuture<MessageDTO>();

        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e("ERRORE: ", e.getMessage(), e);

                completableFuture.complete(null);
                return;
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if(!response.isSuccessful()){
                    Log.e("ERRORE: ","Risposta chiamata api fallita");
                    completableFuture.complete(null);
                    return;
                }


                String jsonStringResult = response.body().string();
                Log.i("JSON: ", jsonStringResult);

                JsonElement jsonElementResult = JsonParser.parseString(jsonStringResult);
                JsonObject jsonObjectResult = jsonElementResult.getAsJsonObject();

                MessageDTO messageDTO = JsonConverter.toMessageDTO(jsonObjectResult);

                completableFuture.complete(messageDTO);
            }
        });

        MessageDTO result = null;

        try {
            result = completableFuture.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return result;

    }
}
