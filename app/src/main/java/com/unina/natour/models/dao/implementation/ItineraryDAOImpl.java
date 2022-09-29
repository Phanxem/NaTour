package com.unina.natour.models.dao.implementation;

import android.content.Context;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.unina.natour.controllers.MessageController;
import com.unina.natour.controllers.utils.FileUtils;
import com.unina.natour.dto.response.ItineraryListResponseDTO;
import com.unina.natour.dto.response.MessageResponseDTO;
import com.unina.natour.dto.request.ItineraryRequestDTO;
import com.unina.natour.dto.response.ItineraryElementResponseDTO;
import com.unina.natour.dto.response.ItineraryResponseDTO;
import com.unina.natour.models.dao.interfaces.ItineraryDAO;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import io.jenetics.jpx.GPX;
import io.jenetics.jpx.WayPoint;
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
    public MessageResponseDTO addItinerary(ItineraryRequestDTO itineraryDTO) {
        MessageResponseDTO messageResponseDTO;

        //String username = Amplify.Auth.getCurrentUser().getUsername();
        String username = TEST_USER;


        GPX gpx = itineraryDTO.getGpx();
        //can throw IOException
        File file = null;
        try {
            file = FileUtils.toFile(context, gpx);
        } catch (IOException e) {
            messageResponseDTO = MessageController.getFailureMessage();
            return messageResponseDTO;
        }
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

        JsonObject jsonObjectResult = null;
        try {
            jsonObjectResult = completableFuture.get();
        }
        catch (ExecutionException | InterruptedException e) {
            messageResponseDTO = MessageController.getFailureMessage();
            return messageResponseDTO;
        }

        if(exception[0] != null){
            messageResponseDTO = MessageController.getFailureMessage();
            return messageResponseDTO;
        }

        MessageResponseDTO result = MessageDAOImpl.toMessageDTO(jsonObjectResult);

        return result;
    }

    //TODO da implementare

    @Override
    public ItineraryListResponseDTO getRandomItineraryList() {

        ItineraryElementResponseDTO test1 = new ItineraryElementResponseDTO();
        test1.setItineraryId(1);
        test1.setDifficulty(1);
        test1.setDescription("1genocide cutter pyroinesis hadoke shoryuken tatsumakisempukiaku genocide cutter pyroinesis hadoke shoryuken tatsumakisempukiaku genocide cutter pyroinesis hadoke shoryuken tatsumakisempukiaku genocide cutter pyroinesis hadoke shoryuken tatsumakisempukiaku genocide cutter pyroinesis hadoke shoryuken tatsumakisempukiaku genocide cutter pyroinesis hadoke shoryuken tatsumakisempukiaku genocide cutter pyroinesis hadoke shoryuken tatsumakisempukiaku genocide cutter pyroinesis hadoke shoryuken tatsumakisempukiaku genocide cutter pyroinesis hadoke shoryuken tatsumakisempukiaku genocide cutter pyroinesis hadoke shoryuken tatsumakisempukiaku genocide cutter pyroinesis hadoke shoryuken tatsumakisempukiaku genocide cutter pyroinesis hadoke shoryuken tatsumakisempukiaku genocide cutter pyroinesis hadoke shoryuken tatsumakisempukiaku genocide cutter pyroinesis hadoke shoryuken tatsumakisempukiaku genocide cutter pyroinesis hadoke shoryuken tatsumakisempukiaku genocide cutter pyroinesis hadoke shoryuken tatsumakisempukiaku" +
                "genocide cutter pyroinesis hadoke shoryuken tatsumakisempukiaku genocide cutter pyroinesis hadoke shoryuken tatsumakisempukiaku genocide cutter pyroinesis hadoke shoryuken tatsumakisempukiaku genocide cutter pyroinesis hadoke shoryuken tatsumakisempukiaku genocide cutter pyroinesis hadoke shoryuken tatsumakisempukiaku genocide cutter pyroinesis hadoke shoryuken tatsumakisempukiaku genocide cutter pyroinesis hadoke shoryuken tatsumakisempukiaku genocide cutter pyroinesis hadoke shoryuken tatsumakisempukiaku genocide cutter pyroinesis hadoke shoryuken tatsumakisempukiaku genocide cutter pyroinesis hadoke shoryuken tatsumakisempukiaku genocide cutter pyroinesis hadoke shoryuken tatsumakisempukiaku genocide cutter pyroinesis hadoke shoryuken tatsumakisempukiaku genocide cutter pyroinesis hadoke shoryuken tatsumakisempukiaku genocide cutter pyroinesis hadoke shoryuken tatsumakisempukiaku genocide cutter pyroinesis hadoke shoryuken tatsumakisempukiaku genocide cutter pyroinesis hadoke shoryuken tatsumakisempukiaku2");
        test1.setDuration(33.3f);
        test1.setLenght(33.3f);
        test1.setName("test1");
        test1.setUserImage(null);
        test1.setUsername("usertest1");

        ItineraryElementResponseDTO test2 = new ItineraryElementResponseDTO();
        test2.setItineraryId(2);
        test2.setDifficulty(0);
        test2.setDescription("dsghhjfghdgsfghgjhfgsdfghhgfa");
        test2.setDuration(45.3f);
        test2.setLenght(76.3f);
        test2.setName("test12");
        test2.setUserImage(null);
        test2.setUsername("usertest12");

        ItineraryElementResponseDTO test3 = new ItineraryElementResponseDTO();
        test3.setItineraryId(3);
        test3.setDifficulty(2);
        test3.setDescription("afjdfòknf asofòkaòsdmasfnaoienlk ");
        test3.setDuration(88.6f);
        test3.setLenght(234.1f);
        test3.setName("test3");
        test3.setUserImage(null);
        test3.setUsername("usertest3");

        List<ItineraryElementResponseDTO> response = new ArrayList<ItineraryElementResponseDTO>();
        response.add(test1);
        response.add(test2);
        response.add(test3);
        response.add(test1);
        response.add(test2);
        response.add(test3);
        response.add(test1);
        response.add(test2);
        response.add(test3);

        ItineraryListResponseDTO result = new ItineraryListResponseDTO();
        MessageResponseDTO messageResponseDTO = MessageController.getSuccessMessage();
        result.setItineraries(response);
        result.setResultMessage(messageResponseDTO);


        return result;
    }

    @Override
    public ItineraryListResponseDTO findByUserId(long userId) {
        ItineraryElementResponseDTO test1 = new ItineraryElementResponseDTO();
        test1.setItineraryId(1);
        test1.setDifficulty(1);
        test1.setDescription("1genocide cutter pyroinesis hadoke shoryuken tatsumakisempukiaku genocide cutter pyroinesis hadoke shoryuken tatsumakisempukiaku genocide cutter pyroinesis hadoke shoryuken tatsumakisempukiaku genocide cutter pyroinesis hadoke shoryuken tatsumakisempukiaku genocide cutter pyroinesis hadoke shoryuken tatsumakisempukiaku genocide cutter pyroinesis hadoke shoryuken tatsumakisempukiaku genocide cutter pyroinesis hadoke shoryuken tatsumakisempukiaku genocide cutter pyroinesis hadoke shoryuken tatsumakisempukiaku genocide cutter pyroinesis hadoke shoryuken tatsumakisempukiaku genocide cutter pyroinesis hadoke shoryuken tatsumakisempukiaku genocide cutter pyroinesis hadoke shoryuken tatsumakisempukiaku genocide cutter pyroinesis hadoke shoryuken tatsumakisempukiaku genocide cutter pyroinesis hadoke shoryuken tatsumakisempukiaku genocide cutter pyroinesis hadoke shoryuken tatsumakisempukiaku genocide cutter pyroinesis hadoke shoryuken tatsumakisempukiaku genocide cutter pyroinesis hadoke shoryuken tatsumakisempukiaku" +
                "genocide cutter pyroinesis hadoke shoryuken tatsumakisempukiaku genocide cutter pyroinesis hadoke shoryuken tatsumakisempukiaku genocide cutter pyroinesis hadoke shoryuken tatsumakisempukiaku genocide cutter pyroinesis hadoke shoryuken tatsumakisempukiaku genocide cutter pyroinesis hadoke shoryuken tatsumakisempukiaku genocide cutter pyroinesis hadoke shoryuken tatsumakisempukiaku genocide cutter pyroinesis hadoke shoryuken tatsumakisempukiaku genocide cutter pyroinesis hadoke shoryuken tatsumakisempukiaku genocide cutter pyroinesis hadoke shoryuken tatsumakisempukiaku genocide cutter pyroinesis hadoke shoryuken tatsumakisempukiaku genocide cutter pyroinesis hadoke shoryuken tatsumakisempukiaku genocide cutter pyroinesis hadoke shoryuken tatsumakisempukiaku genocide cutter pyroinesis hadoke shoryuken tatsumakisempukiaku genocide cutter pyroinesis hadoke shoryuken tatsumakisempukiaku genocide cutter pyroinesis hadoke shoryuken tatsumakisempukiaku genocide cutter pyroinesis hadoke shoryuken tatsumakisempukiaku2");
        test1.setDuration(33.3f);
        test1.setLenght(33.3f);
        test1.setName("test1");
        test1.setUserImage(null);
        test1.setUsername("usertest1");

        ItineraryElementResponseDTO test2 = new ItineraryElementResponseDTO();
        test2.setItineraryId(2);
        test2.setDifficulty(0);
        test2.setDescription("dsghhjfghdgsfghgjhfgsdfghhgfa");
        test2.setDuration(45.3f);
        test2.setLenght(76.3f);
        test2.setName("test12");
        test2.setUserImage(null);
        test2.setUsername("usertest12");

        ItineraryElementResponseDTO test3 = new ItineraryElementResponseDTO();
        test3.setItineraryId(3);
        test3.setDifficulty(2);
        test3.setDescription("afjdfòknf asofòkaòsdmasfnaoienlk ");
        test3.setDuration(88.6f);
        test3.setLenght(234.1f);
        test3.setName("test3");
        test3.setUserImage(null);
        test3.setUsername("usertest3");

        List<ItineraryElementResponseDTO> response = new ArrayList<ItineraryElementResponseDTO>();
        response.add(test1);
        response.add(test2);
        response.add(test3);
        response.add(test1);
        response.add(test2);
        response.add(test3);
        response.add(test1);
        response.add(test2);
        response.add(test3);

        ItineraryListResponseDTO result = new ItineraryListResponseDTO();
        MessageResponseDTO messageResponseDTO = MessageController.getSuccessMessage();
        result.setItineraries(response);
        result.setResultMessage(messageResponseDTO);


        return result;
    }

    /*

    @Override
    public ItineraryListResponseDTO getUserItinearyList(String username) {
        ItineraryElementResponseDTO test1 = new ItineraryElementResponseDTO();
        test1.setItineraryId(5);
        test1.setDifficulty(2);
        test1.setDescription("sasdgsd dgsdfsdfsdfs");
        test1.setDuration(332.3f);
        test1.setLenght(3345.3f);
        test1.setName("test18");


        ItineraryElementResponseDTO test2 = new ItineraryElementResponseDTO();
        test2.setItineraryId(20);
        test2.setDifficulty(1);
        test2.setDescription("dfsfsdfsdfsdfsdfsdfsdfsdfsdfds");
        test2.setDuration(4345.3f);
        test2.setLenght(726.3f);
        test2.setName("test121");




        List<ItineraryElementResponseDTO> response = new ArrayList<ItineraryElementResponseDTO>();
        response.add(test1);
        response.add(test2);
        response.add(test1);
        response.add(test2);
        response.add(test1);
        response.add(test2);
        response.add(test1);
        response.add(test2);

        ItineraryListResponseDTO result = new ItineraryListResponseDTO();
        MessageResponseDTO messageResponseDTO = MessageController.getSuccessMessage();
        result.setItineraries(response);
        result.setResultMessage(messageResponseDTO);

        return result;
    }

    */

    @Override
    public ItineraryResponseDTO findById(long itineraryId) {

        ItineraryResponseDTO dto = new ItineraryResponseDTO();

        dto.setName("TEST342");
        dto.setDescription("genosgenosgenos genosgenosgenosgenosgenosgenos genosgenosgenosgenosgenosgenos genosgenosgenosgenosgenosgenos genosgenosgenosgenosgenosgenos genosgenosgenosgenosgenosgenos genosgenosgenosgenosgenosgenos genosgenosgenosgenosgenosgenos genosgenosgenosgenosgenosgenos genosgenosgenosgenosgenosgenos genosgenosgenosgenosgenosgenos genosgenosgenosgenosgenosgenos genosgenosgenosgenosgenosgenos genosgenosgenosgenosgenosgenos genosgenosgenosgenosgenosgenos genosgenosgenosgenosgenosgenos genosgenosgenosgenosgenosgenos genosgenosgenosgenosgenosgenos genosgenosgenosgenosgenosgenos genosgenosgenosgenosgenosgenos genosgenosgenosgenosgenosgenos genosgenosgenosgenosgenosgenos genosgenosgenosgenosgenosgenos genosgenosgenosgenosgenosgenos genosgenosgenosgenosgenosgenos genosgenosgenosgenosgenosgenos genosgenosgenosgenosgenosgenos genosgenosgenosgenosgenosgenos genosgenosgenosgenosgenosgenos genosgenosgenosgenosgenosgenos genosgenosgenosgenosgenosgenos genosgenosgenosgenosgenosgenos genosgenosgenosgenosgenosgenos genosgenosgenosgenosgenosgenos genosgenosgenos");
        //dto.setDescription("sedano armato");
        dto.setDifficulty(1);
        dto.setId(44);
        dto.setHasBeenReported(true);
        dto.setLenght(6666f);
        dto.setDuration(77777f);

        WayPoint wayPoint1 = WayPoint.builder().build(48.20100,16.31651);
        WayPoint wayPoint2 = WayPoint.builder().build(48.20133,16.31610);
        WayPoint wayPoint3 = WayPoint.builder().build(48.20188,16.31569);

        GPX gpx = GPX.builder()
                .addWayPoint(wayPoint1)
                .addWayPoint(wayPoint2)
                .addWayPoint(wayPoint3)
                .addTrack(track -> track
                        .addSegment(segment -> segment
                                .addPoint(p -> p.lat(48.20100).lon(16.31651))
                                .addPoint(p -> p.lat(48.20112).lon(16.31639))
                                .addPoint(p -> p.lat(48.20126).lon(16.31622))
                                .addPoint(p -> p.lat(48.20133).lon(16.31610))
                                .addPoint(p -> p.lat(48.20144).lon(16.31609))
                                .addPoint(p -> p.lat(48.20155).lon(16.31589))
                                .addPoint(p -> p.lat(48.20166).lon(16.31578))
                                .addPoint(p -> p.lat(48.20177).lon(16.31572))
                                .addPoint(p -> p.lat(48.20188).lon(16.31569))
                        )
                )
                .build();

        dto.setGpx(gpx);

        MessageResponseDTO messageResponseDTO = MessageController.getSuccessMessage();
        dto.setResultMessage(messageResponseDTO);

        return dto;
    }

    @Override
    public MessageResponseDTO deleteById(long itinerayId) {
        return null;
    }

    @Override
    public ItineraryListResponseDTO findByName(String researchString) {

        ItineraryElementResponseDTO test1 = new ItineraryElementResponseDTO();
        test1.setItineraryId(98);
        test1.setDifficulty(2);
        test1.setDescription("1genocide cutter per pyroikisempukiaku genocide cutter pyroinshoryuken tatsumakisempukiaku genocide cutter pyroinesis hadoke shoryuken tatsumakisempukiaku genocide cutter pyroinesis hadoke shoryuken tatsumakisempukiaku genocide cutter pyroinesis hadoke shoryuken tatsumakisempukiaku genocide cutter pyroinesis hadoke shoryuken tatsumakisempukiaku genocide cutter pyroinesis hadoke shoryuken tatsumakisempukiaku genocide cutter pyroinesis hadoke shoryuken tatsumakisempukiaku genocide cutter pyroinesis hadoke shoryuken tatsumakisempukiaku genocide cutter pyroinesis hadoke shoryuken tatsumakisempukiaku genocide cutter pyroinesis hadoke shoryuken tatsumakisempukiaku genocide cutter pyroinesis hadoke shoryuken tatsumakisempukiaku genocide cutter pyroinesis hadoke shoryuken tatsumakisempukiaku genocide cutter pyroinesis hadoke shoryuken tatsumakisempukiaku genocide cutter pyroinesis hadoke shoryuken tatsumakisempukiaku2");
        test1.setDuration(335.3f);
        test1.setLenght(334.3f);
        test1.setName("test154322");
        test1.setUserImage(null);
        test1.setUsername("usertest1432");


        List<ItineraryElementResponseDTO> response = new ArrayList<ItineraryElementResponseDTO>();
        response.add(test1);
        response.add(test1);
        response.add(test1);
        response.add(test1);

        ItineraryListResponseDTO result = new ItineraryListResponseDTO();
        MessageResponseDTO messageResponseDTO = MessageController.getSuccessMessage();
        result.setItineraries(response);
        result.setResultMessage(messageResponseDTO);

        return result;
    }
}
