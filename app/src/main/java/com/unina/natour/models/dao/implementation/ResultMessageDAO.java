package com.unina.natour.models.dao.implementation;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.unina.natour.controllers.ResultMessageController;
import com.unina.natour.dto.response.ResultMessageDTO;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

//TODO togliere i Log.e

public class ResultMessageDAO {

    public ResultMessageDTO fulfilRequest(Request request){
        OkHttpClient client = new OkHttpClient();

        Call call = client.newCall(request);

        final IOException[] exception = {null};
        CompletableFuture<JsonObject> completableFuture = new CompletableFuture<JsonObject>();

        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e("ResultMessageDAO", "Response FAILURE");

                exception[0] = e;
                completableFuture.complete(null);
                return;
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                Log.e("ResultMessageDAO", "Response SUCCESS");

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
            Log.e("ResultMessageDAO", "FAILURE1");
            return ResultMessageController.ERROR_MESSAGE_FAILURE_CLIENT;
        }

        if(exception[0] != null){
            exception[0].printStackTrace();
            Log.e("ResultMessageDAO", "FAILURE2");
            return ResultMessageController.ERROR_MESSAGE_FAILURE_CLIENT;
        }

        ResultMessageDTO result = ResultMessageDAO.getResultMessage(jsonObjectResult);

        return result;
    }


    public static ResultMessageDTO getResultMessage(JsonObject jsonObject){
        JsonObject jsonResultMessage = jsonObject;

        if(jsonObject == null) return null;

        if(jsonObject.has("resultMessage") ){
            JsonElement jsonElement = jsonObject.get("resultMessage");
            if(jsonElement.isJsonNull()) return null;

            jsonResultMessage = jsonObject.get("resultMessage").getAsJsonObject();
        }

        if(!jsonResultMessage.has("code")  || !jsonResultMessage.has("message")){
            return null;
        }

        long code = jsonResultMessage.get("code").getAsLong();
        String message = jsonResultMessage.get("message").getAsString();

        ResultMessageDTO resultMessageDTO = new ResultMessageDTO(code,message);

        return resultMessageDTO;
    }

}
