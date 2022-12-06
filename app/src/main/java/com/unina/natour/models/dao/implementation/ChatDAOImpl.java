package com.unina.natour.models.dao.implementation;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.unina.natour.config.CurrentUserInfo;
import com.unina.natour.controllers.ResultMessageController;
import com.unina.natour.dto.response.GetBitmapResponseDTO;
import com.unina.natour.dto.response.GetIdChatResponseDTO;
import com.unina.natour.dto.response.GetListChatMessageResponseDTO;
import com.unina.natour.dto.response.GetChatMessageResponseDTO;
import com.unina.natour.dto.response.GetListUserResponseDTO;
import com.unina.natour.dto.response.GetUserResponseDTO;
import com.unina.natour.dto.response.HasMessageToReadResponseDTO;
import com.unina.natour.dto.response.ResultMessageDTO;
import com.unina.natour.dto.response.composted.GetChatWithUserResponseDTO;
import com.unina.natour.dto.response.composted.GetListChatWithUserResponseDTO;
import com.unina.natour.models.dao.interfaces.ChatDAO;
import com.unina.natour.models.dao.interfaces.UserDAO;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.xml.transform.Result;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ChatDAOImpl extends ServerDAO implements ChatDAO {

    private static final String URL = SERVER_URL + "/chat";

    private UserDAO userDAO;

    public ChatDAOImpl(Context context){
        this.userDAO = new UserDAOImpl(context);
    }

    @Override
    public GetListChatMessageResponseDTO getMessageByIdsUser(long idUser1, long idUser2, int page) {
        String url = URL + "/get/messages?idUser1=" + idUser1 + "&idUser2=" + idUser2 + "&page=" + page;

        Request request = new Request.Builder()
                .url(url)
                .build();

        GetListChatMessageResponseDTO getListChatMessageResponseDTO = getListChatMessageResponseDTO(request);

        return getListChatMessageResponseDTO;
    }

    @Override
    public GetIdChatResponseDTO getChatByIdsUser(long idUser1, long idUser2) {
        String url = URL + "/get/users?idUser1=" + idUser1 + "&idUser2=" + idUser2;

        GetIdChatResponseDTO getIdChatResponseDTO = new GetIdChatResponseDTO();

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
            getIdChatResponseDTO.setResultMessage(ResultMessageController.ERROR_MESSAGE_FAILURE_CLIENT);
            return getIdChatResponseDTO;
        }

        if(exception[0] != null){
            getIdChatResponseDTO.setResultMessage(ResultMessageController.ERROR_MESSAGE_FAILURE_CLIENT);
            return getIdChatResponseDTO;
        }

        ResultMessageDTO resultMessage = ResultMessageDAO.getResultMessage(jsonObjectResult);

        if(!ResultMessageController.isSuccess(resultMessage)){
            getIdChatResponseDTO.setResultMessage(resultMessage);
            return getIdChatResponseDTO;
        }

        getIdChatResponseDTO = toGetIdChatResponseDTO(jsonObjectResult);

        return getIdChatResponseDTO;
    }

    @Override
    public ResultMessageDTO readAllMessageByIdsUser(long idUser1, long idUser2) {
        String url = URL + "/readAllMessage?idUser1=" + idUser1 + "&idUser2=" + idUser2;

        RequestBody requestBody = RequestBody.create(new byte[0]);

        Request request = new Request.Builder()
                .url(url)
                .put(requestBody)
                .build();

        ResultMessageDTO resultMessageDTO = resultMessageDAO.fulfilRequest(request);
        return resultMessageDTO;
    }

    @Override
    public HasMessageToReadResponseDTO checkIfHasMessageToRead(long idUser) {
        String url = URL + "/has/messageToRead/" + idUser;

        HasMessageToReadResponseDTO hasMessageToReadResponseDTO = new HasMessageToReadResponseDTO();

        Request request = new Request.Builder()
                .url(url)
                .build();

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
            hasMessageToReadResponseDTO.setResultMessage(ResultMessageController.ERROR_MESSAGE_FAILURE_CLIENT);
            return hasMessageToReadResponseDTO;
        }

        if(exception[0] != null){
            hasMessageToReadResponseDTO.setResultMessage(ResultMessageController.ERROR_MESSAGE_FAILURE_CLIENT);
            return hasMessageToReadResponseDTO;
        }

        ResultMessageDTO resultMessage = ResultMessageDAO.getResultMessage(jsonObjectResult);

        if(!ResultMessageController.isSuccess(resultMessage)){
            if(resultMessage.getCode() == ResultMessageController.ERROR_CODE_NOT_FOUND){
                hasMessageToReadResponseDTO.setResultMessage(ResultMessageController.SUCCESS_MESSAGE);
                hasMessageToReadResponseDTO.setHasMessageToRead(false);
                return hasMessageToReadResponseDTO;
            }
            hasMessageToReadResponseDTO.setResultMessage(resultMessage);
            return hasMessageToReadResponseDTO;
        }

        hasMessageToReadResponseDTO = toHasMessageToReadResponseDTO(jsonObjectResult);

        return hasMessageToReadResponseDTO;
    }

    @Override
    public ResultMessageDTO addChat(long idUser1, long idUser2) {
        String url = URL + "/add?idUser1=" + idUser1 + "&idUser2=" + idUser2;

        RequestBody requestBody = RequestBody.create(new byte[0]);

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        ResultMessageDTO resultMessageDTO = resultMessageDAO.fulfilRequest(request);
        return resultMessageDTO;
    }

    @Override
    public GetListChatWithUserResponseDTO getListChatByIdUser(long idUser, int page) {

        GetListChatWithUserResponseDTO getListChatWithUserResponseDTO = new GetListChatWithUserResponseDTO();

        //RECUPERO GLI UTENTI CON CONVERSAZIONI (dal più recente al meno recente)
        GetListUserResponseDTO getListUserResponseDTO = userDAO.getListUserWithConversation(idUser, page);
        if(!ResultMessageController.isSuccess(getListUserResponseDTO.getResultMessage())){
            getListChatWithUserResponseDTO.setResultMessage(getListUserResponseDTO.getResultMessage());
            return getListChatWithUserResponseDTO;
        }

        List<GetUserResponseDTO> listUser = getListUserResponseDTO.getListUser();


        if(listUser.isEmpty()){
            getListChatWithUserResponseDTO.setResultMessage(ResultMessageController.SUCCESS_MESSAGE);
            return getListChatWithUserResponseDTO;
        }

        //PER OGNI UTENTE RECUPERO L'IMMAGINI DI PROFILO
        GetBitmapResponseDTO[] arrayImage = new GetBitmapResponseDTO[listUser.size()];
        ExecutorService executor = Executors.newFixedThreadPool(listUser.size());
        for(int i = 0; i < listUser.size(); i++){
            int iTemp = i;

            Runnable runnable = new Runnable() {
                @Override
                public void run() {

                    GetUserResponseDTO user = listUser.get(iTemp);

                    GetBitmapResponseDTO getBitmapResponseDTO = userDAO.getUserImageById(user.getId());

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
            getListChatWithUserResponseDTO.setResultMessage(ResultMessageController.ERROR_MESSAGE_FAILURE_CLIENT);
            return getListChatWithUserResponseDTO;
        }

        if(!finished){
            getListChatWithUserResponseDTO.setResultMessage(ResultMessageController.ERROR_MESSAGE_FAILURE_CLIENT);
            return getListChatWithUserResponseDTO;
        }


        //PER OGNI UTENTE RECUPERO GLI ULTIMI MESSAGGI DELLA CHAT
        executor = Executors.newFixedThreadPool(listUser.size());
        boolean[] arrayMessageToRead = new boolean[listUser.size()];
        for(int i = 0; i < listUser.size(); i++){
            int iTemp = i;

            Runnable runnable = new Runnable() {
                @Override
                public void run() {

                    GetUserResponseDTO user = listUser.get(iTemp);

                    GetListChatMessageResponseDTO getListChatMessageResponseDTO = getMessageByIdsUser(CurrentUserInfo.getId(), user.getId(),0);
                    List<GetChatMessageResponseDTO> listMessage = getListChatMessageResponseDTO.getListMessage();

                    if(listMessage == null || listMessage.isEmpty()){
                        arrayMessageToRead[iTemp] = false;
                        return;
                    }

                    GetChatMessageResponseDTO tempMessage;
                    //for(int j = listMessage.size()-1; j>=0; j--){
                    for(int j = 0; j<listMessage.size(); j++){
                        tempMessage = listMessage.get(j);

                        Log.e("ChatDAO", "| " + tempMessage.getId() + ", " +  tempMessage.getIdUser() + ", " + tempMessage.getBody() + ", " + tempMessage.isToRead() + " |");

                        if(tempMessage.getIdUser() != CurrentUserInfo.getId()){
                            if(tempMessage.isToRead()) arrayMessageToRead[iTemp] = true;
                            else arrayMessageToRead[iTemp] = false;
                            return;
                        }
                    }
                    arrayMessageToRead[iTemp] = false;

                }
            };



            executor.execute(runnable);
        }
        executor.shutdown();

        finished = false;
        try {
            finished = executor.awaitTermination(1, TimeUnit.MINUTES);
        }
        catch (InterruptedException e) {
            getListChatWithUserResponseDTO.setResultMessage(ResultMessageController.ERROR_MESSAGE_FAILURE_CLIENT);
            return getListChatWithUserResponseDTO;
        }

        if(!finished){
            getListChatWithUserResponseDTO.setResultMessage(ResultMessageController.ERROR_MESSAGE_FAILURE_CLIENT);
            return getListChatWithUserResponseDTO;
        }

        List<GetChatWithUserResponseDTO> listChat = new ArrayList<GetChatWithUserResponseDTO>();
        for(int i = 0; i < listUser.size(); i++){
            GetChatWithUserResponseDTO getChatWithUserResponseDTO = new GetChatWithUserResponseDTO();

            GetUserResponseDTO getUserResponseDTO = listUser.get(i);
            GetBitmapResponseDTO getBitmapResponseDTO = arrayImage[i];

            getChatWithUserResponseDTO.setIdUser(getUserResponseDTO.getId());
            getChatWithUserResponseDTO.setNameChat(getUserResponseDTO.getUsername());
            getChatWithUserResponseDTO.setProfileImage(getBitmapResponseDTO.getBitmap());
            getChatWithUserResponseDTO.setHasMessagesToRead(arrayMessageToRead[i]);

            getChatWithUserResponseDTO.setResultMessage(ResultMessageController.SUCCESS_MESSAGE);

            listChat.add(getChatWithUserResponseDTO);
        }

        getListChatWithUserResponseDTO.setListChat(listChat);
        getListChatWithUserResponseDTO.setResultMessage(ResultMessageController.SUCCESS_MESSAGE);

        return getListChatWithUserResponseDTO;
    }


    private GetListChatMessageResponseDTO getListChatMessageResponseDTO(Request request){
        GetListChatMessageResponseDTO getListChatMessageResponseDTO = new GetListChatMessageResponseDTO();

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
            getListChatMessageResponseDTO.setResultMessage(ResultMessageController.ERROR_MESSAGE_FAILURE_CLIENT);
            return getListChatMessageResponseDTO;
        }

        if(exception[0] != null){
            getListChatMessageResponseDTO.setResultMessage(ResultMessageController.ERROR_MESSAGE_FAILURE_CLIENT);
            return getListChatMessageResponseDTO;
        }

        ResultMessageDTO resultMessage = ResultMessageDAO.getResultMessage(jsonObjectResult);

        if(!ResultMessageController.isSuccess(resultMessage)){
            getListChatMessageResponseDTO.setResultMessage(resultMessage);
            return getListChatMessageResponseDTO;
        }

        getListChatMessageResponseDTO = toGetListChatMessageResponseDTO(jsonObjectResult);

        return getListChatMessageResponseDTO;
    }


//MAPPER

    public GetChatMessageResponseDTO toGetChatMessageResponseDTO(JsonObject jsonObject){
        GetChatMessageResponseDTO getChatMessageResponseDTO = new GetChatMessageResponseDTO();

        if(jsonObject.has("resultMessage") ){
            JsonObject jsonResultMessage = jsonObject.get("resultMessage").getAsJsonObject();

            long code = jsonResultMessage.get("code").getAsLong();
            String message = jsonResultMessage.get("message").getAsString();

            ResultMessageDTO resultMessageDTO = new ResultMessageDTO(code,message);
            getChatMessageResponseDTO.setResultMessage(resultMessageDTO);
        }

        long id = jsonObject.get("id").getAsLong();
        getChatMessageResponseDTO.setId(id);

        String body = jsonObject.get("body").getAsString();
        getChatMessageResponseDTO.setBody(body);

        String dateOfInput = jsonObject.get("dateOfInput").getAsString();
        getChatMessageResponseDTO.setDateOfInput(dateOfInput);

        long idUser = jsonObject.get("idUser").getAsLong();
        getChatMessageResponseDTO.setIdUser(idUser);

        long idChat = jsonObject.get("idChat").getAsLong();
        getChatMessageResponseDTO.setIdChat(idChat);

        boolean toRead = jsonObject.get("toRead").getAsBoolean();
        getChatMessageResponseDTO.setToRead(toRead);

        return getChatMessageResponseDTO;
    }

    public GetListChatMessageResponseDTO toGetListChatMessageResponseDTO(JsonObject jsonObject){
        GetListChatMessageResponseDTO getListChatMessageResponseDTO = new GetListChatMessageResponseDTO();

        if(jsonObject.has("resultMessage") ){
            JsonObject jsonResultMessage = jsonObject.get("resultMessage").getAsJsonObject();

            long code = jsonResultMessage.get("code").getAsLong();
            String message = jsonResultMessage.get("message").getAsString();

            ResultMessageDTO resultMessageDTO = new ResultMessageDTO(code,message);
            getListChatMessageResponseDTO.setResultMessage(resultMessageDTO);
        }

        long idChat = jsonObject.get("idChat").getAsLong();
        getListChatMessageResponseDTO.setIdChat(idChat);

        JsonArray jsonArray = jsonObject.get("listMessage").getAsJsonArray();
        List<GetChatMessageResponseDTO> listMessage = new ArrayList<GetChatMessageResponseDTO>();
        for(JsonElement jsonElement : jsonArray){
            JsonObject jsonObjectElement = jsonElement.getAsJsonObject();

            GetChatMessageResponseDTO getChatMessageResponseDTO = toGetChatMessageResponseDTO(jsonObjectElement);
            listMessage.add(getChatMessageResponseDTO);
        }
        getListChatMessageResponseDTO.setListMessage(listMessage);

        return getListChatMessageResponseDTO;
    }

    public GetIdChatResponseDTO toGetIdChatResponseDTO(JsonObject jsonObject){
        GetIdChatResponseDTO getIdChatResponseDTO = new GetIdChatResponseDTO();

        if(jsonObject.has("resultMessage") ){
            JsonObject jsonResultMessage = jsonObject.get("resultMessage").getAsJsonObject();

            long code = jsonResultMessage.get("code").getAsLong();
            String message = jsonResultMessage.get("message").getAsString();

            ResultMessageDTO resultMessageDTO = new ResultMessageDTO(code,message);
            getIdChatResponseDTO.setResultMessage(resultMessageDTO);
        }

        long idChat = jsonObject.get("id").getAsLong();
        getIdChatResponseDTO.setId(idChat);

        return getIdChatResponseDTO;
    }

    public HasMessageToReadResponseDTO toHasMessageToReadResponseDTO(JsonObject jsonObject){
        HasMessageToReadResponseDTO hasMessageToReadResponseDTO = new HasMessageToReadResponseDTO();

        if(jsonObject.has("resultMessage") ){
            JsonObject jsonResultMessage = jsonObject.get("resultMessage").getAsJsonObject();

            long code = jsonResultMessage.get("code").getAsLong();
            String message = jsonResultMessage.get("message").getAsString();

            ResultMessageDTO resultMessageDTO = new ResultMessageDTO(code,message);
            hasMessageToReadResponseDTO.setResultMessage(resultMessageDTO);
        }

        boolean hasMessageToRead = jsonObject.get("hasMessageToRead").getAsBoolean();
        hasMessageToReadResponseDTO.setHasMessageToRead(hasMessageToRead);

        return hasMessageToReadResponseDTO;
    }

}
