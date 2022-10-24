package com.unina.natour.models.dao.implementation;

import android.content.Context;

import androidx.annotation.NonNull;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.unina.natour.controllers.ResultMessageController;
import com.unina.natour.dto.response.GetBitmapResponseDTO;
import com.unina.natour.dto.response.GetListChatMessageResponseDTO;
import com.unina.natour.dto.response.GetChatMessageResponseDTO;
import com.unina.natour.dto.response.GetListUserResponseDTO;
import com.unina.natour.dto.response.GetUserResponseDTO;
import com.unina.natour.dto.response.ResultMessageDTO;
import com.unina.natour.dto.response.composted.GetChatWithUserResponseDTO;
import com.unina.natour.dto.response.composted.GetListChatWithUserResponseDTO;
import com.unina.natour.models.dao.interfaces.ChatDAO;
import com.unina.natour.models.dao.interfaces.UserDAO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
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

        GetListChatMessageResponseDTO getListChatMessageResponseDTO = getListChatMessageResponseDTO(url);

        return getListChatMessageResponseDTO;
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
        boolean[] arrayMessageToRead = new boolean[listUser.size()];
        for(int i = 0; i < listUser.size(); i++){
            int iTemp = i;

            //TODO definire la classe col quale recuperare l'id attuale
            long myId = 0;

            Runnable runnable = new Runnable() {
                @Override
                public void run() {

                    GetUserResponseDTO user = listUser.get(iTemp);

                    GetListChatMessageResponseDTO getListChatMessageResponseDTO = getMessageByIdsUser(myId, user.getId(),0);
                    List<GetChatMessageResponseDTO> listMessage = getListChatMessageResponseDTO.getListMessage();

                    if(listMessage == null || listMessage.isEmpty()) arrayMessageToRead[iTemp] = false;
                    else {
                        GetChatMessageResponseDTO lastMessage = listMessage.get(0);
                        if (lastMessage.isToRead()) arrayMessageToRead[iTemp] = true;
                        else arrayMessageToRead[iTemp] = false;
                    }
                }
            };

            executor.execute(runnable);
        }

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


    private GetListChatMessageResponseDTO getListChatMessageResponseDTO(String url){
        GetListChatMessageResponseDTO getListChatMessageResponseDTO = new GetListChatMessageResponseDTO();

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

        if(!jsonObject.has("resultMessage") ){
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

        return getChatMessageResponseDTO;
    }

    public GetListChatMessageResponseDTO toGetListChatMessageResponseDTO(JsonObject jsonObject){
        GetListChatMessageResponseDTO getListChatMessageResponseDTO = new GetListChatMessageResponseDTO();

        if(!jsonObject.has("resultMessage") ){
            JsonObject jsonResultMessage = jsonObject.get("resultMessage").getAsJsonObject();

            long code = jsonResultMessage.get("code").getAsLong();
            String message = jsonResultMessage.get("message").getAsString();

            ResultMessageDTO resultMessageDTO = new ResultMessageDTO(code,message);
            getListChatMessageResponseDTO.setResultMessage(resultMessageDTO);
        }

        JsonArray jsonArray = jsonObject.get("listUser").getAsJsonArray();
        List<GetChatMessageResponseDTO> listMessage = new ArrayList<GetChatMessageResponseDTO>();
        for(JsonElement jsonElement : jsonArray){
            JsonObject jsonObjectElement = jsonElement.getAsJsonObject();

            GetChatMessageResponseDTO getChatMessageResponseDTO = toGetChatMessageResponseDTO(jsonObject);
            listMessage.add(getChatMessageResponseDTO);
        }
        getListChatMessageResponseDTO.setListMessage(listMessage);

        return getListChatMessageResponseDTO;
    }


    /*
    @Override
    public GetListChatMessageResponseDTO getMessageByidsUser(long userId1, long userId2) {

        GetChatMessageResponseDTO test1 = new GetChatMessageResponseDTO();
        test1.setBody("hellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohello");
        test1.setDateOfInput("12/12/22");
        test1.setIdUser(11);
        test1.setIdChat(22);

        GetChatMessageResponseDTO test2 = new GetChatMessageResponseDTO();
        test2.setBody("hihihihihihiohellohelloheohellohelloheohellohelloheohellohellohehihihi");
        test2.setDateOfInput("12/12/22");
        test2.setIdUser(22);
        test2.setIdChat(11);

        GetChatMessageResponseDTO testP = new GetChatMessageResponseDTO();
        testP.setBody("PRIMO");
        testP.setDateOfInput("12/12/22");
        testP.setIdUser(11);
        testP.setIdChat(22);

        GetChatMessageResponseDTO testU = new GetChatMessageResponseDTO();
        testU.setBody("ULTIMO");
        testU.setDateOfInput("12/12/22");
        testU.setIdUser(22);
        testU.setIdChat(11);


        List<GetChatMessageResponseDTO> list = new ArrayList<GetChatMessageResponseDTO>();

        list.add(testP);
        list.add(test1);
        list.add(test2);
        list.add(test2);
        list.add(test2);
        list.add(test2);
        list.add(test2);
        list.add(test2);
        list.add(test2);
        list.add(test2);
        list.add(test2);
        list.add(test2);
        list.add(test2);
        list.add(test2);
        list.add(test2);
        list.add(test2);
        list.add(test2);
        list.add(test2);
        list.add(test2);
        list.add(test2);
        list.add(testU);

        GetListChatMessageResponseDTO listTest = new GetListChatMessageResponseDTO();

        listTest.setListMessage(list);
        listTest.setResultMessage(ResultMessageController.getSuccessMessage());

        return listTest;
    }

     */
}
