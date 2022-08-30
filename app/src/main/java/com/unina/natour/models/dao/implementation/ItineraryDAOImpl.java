package com.unina.natour.models.dao.implementation;

import android.content.Context;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.unina.natour.controllers.exceptionHandler.exceptions.ServerException;
import com.unina.natour.dto.MessageDTO;
import com.unina.natour.dto.request.ItineraryRequestDTO;
import com.unina.natour.models.dao.converters.FileConverter;
import com.unina.natour.models.dao.converters.JsonConverter;
import com.unina.natour.models.dao.interfaces.ItineraryDAO;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import io.jenetics.jpx.GPX;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@RequiresApi(api = Build.VERSION_CODES.N)
public class ItineraryDAOImpl implements ItineraryDAO {

    private static final String URL = SERVER_URL + "/itinerary";

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

    public ItineraryDAOImpl(Context context){
        this.context = context;
    }


    @Override
    public MessageDTO addItinerary(ItineraryRequestDTO itineraryDTO) throws IOException, ExecutionException, InterruptedException, ServerException {

        //String username = Amplify.Auth.getCurrentUser().getUsername();
        String username = TEST_USER;


        GPX gpx = itineraryDTO.getGpx();
        //can throw IOException
        File file = FileConverter.toFile(context, gpx);
        RequestBody gpxRequestBody = RequestBody.create(file, MediaType.parse("application/octet-stream"));

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart(BODY_KEY_NAME, itineraryDTO.getName())
                .addFormDataPart(BODY_KEY_GPX, file.getName(), gpxRequestBody)
                .addFormDataPart(BODY_KEY_DURATION, itineraryDTO.getDuration().toString())
                .addFormDataPart(BODY_KEY_LENGHT, itineraryDTO.getLenght().toString())
                .addFormDataPart(BODY_KEY_DIFFICULTY,itineraryDTO.getDifficulty().toString())
                .addFormDataPart(BODY_KEY_DESCRIPTION, itineraryDTO.getDescription())
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

        JsonObject jsonObjectResult = completableFuture.get();

        if(jsonObjectResult == null) throw exception[0];

        MessageDTO result = JsonConverter.toMessageDTO(jsonObjectResult);

        return result;
    }
}
