package com.unina.natour.controllers;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.RequiresApi;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.unina.natour.R;
import com.unina.natour.controllers.utils.TimeUtils;
import com.unina.natour.dto.response.GetItineraryResponseDTO;
import com.unina.natour.dto.response.composted.GetItineraryWithUserResponseDTO;
import com.unina.natour.dto.response.GetListItineraryResponseDTO;
import com.unina.natour.dto.response.ResultMessageDTO;
import com.unina.natour.dto.response.composted.GetListItineraryWithUserResponseDTO;
import com.unina.natour.models.ElementItineraryModel;
import com.unina.natour.models.dao.implementation.ItineraryDAOImpl;
import com.unina.natour.models.dao.interfaces.ItineraryDAO;
import com.unina.natour.views.activities.NaTourActivity;
import com.unina.natour.views.listAdapters.ItineraryListAdapter;

import java.util.ArrayList;
import java.util.List;


public class ListaItinerariController extends NaTourController{

    public final static long CODE_ITINERARY_RANDOM = 0;
    public final static long CODE_ITINERARY_BY_USER_ID = 1;
    public final static long CODE_ITINERARY_BY_RESEARCH = 2;


    private ItineraryListAdapter itineraryListAdapter;

    private ArrayList<ElementItineraryModel> elementsItineraryModel;

    private long researchCode;
    private String researchString;
    private long userId;

    private ItineraryDAO itinearyDAO;

    public ListaItinerariController(NaTourActivity activity, long researchCode, String researchString, long userId){
        super(activity);
        String messageToShow = null;

        this.elementsItineraryModel = new ArrayList<ElementItineraryModel>();

        this.itinearyDAO = new ItineraryDAOImpl(activity);

        boolean result;
        if(researchCode == CODE_ITINERARY_RANDOM){
            result = initModel();
        }
        else if(researchCode == CODE_ITINERARY_BY_USER_ID && userId>0){
            result = initModel(userId);
        }
        else if(researchCode == CODE_ITINERARY_BY_RESEARCH && researchString != null && !researchString.isEmpty()){
            result = initModel(researchString);
        }
        else{
            messageToShow = activity.getString(R.string.Message_UnknownError);
            showErrorMessageAndBack(messageToShow);
            return;
        }

        if(!result){ return; }
    }

    public boolean initModel(){
        Activity activity = getActivity();
        String messageToShow = null;

        GetListItineraryWithUserResponseDTO getListItineraryWithUserResponseDTO = itinearyDAO.getListItineraryWithUserRandom();
        if(!ResultMessageController.isSuccess(getListItineraryWithUserResponseDTO.getResultMessage())){
            messageToShow = activity.getString(R.string.Message_UnknownError);
            showErrorMessageAndBack(messageToShow);
            return false;
        }


        boolean result = dtoToModel(getActivity(), getListItineraryWithUserResponseDTO,elementsItineraryModel);
        if(!result){
            messageToShow = activity.getString(R.string.Message_UnknownError);
            showErrorMessageAndBack(messageToShow);
            return false;
        }

        this.researchCode = CODE_ITINERARY_RANDOM;
        this.researchString = null;
        this.userId = -1;

        this.itineraryListAdapter = new ItineraryListAdapter(getActivity(),elementsItineraryModel, false);
        return true;
    }


    public boolean initModel(String researchString){
        Activity activity = getActivity();
        String messageToShow = null;

        GetListItineraryWithUserResponseDTO getListItineraryWithUserResponseDTO = itinearyDAO.getListItineraryWithUserByName(researchString, 0);
        if(ResultMessageController.isSuccess(getListItineraryWithUserResponseDTO.getResultMessage())){
            messageToShow = activity.getString(R.string.Message_UnknownError);
            showErrorMessageAndBack(messageToShow);
            return false;
        }

        boolean result = dtoToModel(getActivity(), getListItineraryWithUserResponseDTO,elementsItineraryModel);
        if(!result){
            messageToShow = activity.getString(R.string.Message_UnknownError);
            showErrorMessageAndBack(messageToShow);
            return false;
        }

        this.researchCode = CODE_ITINERARY_BY_RESEARCH;
        this.researchString = researchString;
        this.userId = -1;

        this.itineraryListAdapter = new ItineraryListAdapter(getActivity(),elementsItineraryModel, false);
        return true;
    }

    public boolean initModel(long userId){
        Activity activity = getActivity();
        String messageToShow = null;

        GetListItineraryResponseDTO itinerariesDTO = itinearyDAO.getListItineraryByIdUser(userId, 0);
        if(ResultMessageController.isSuccess(itinerariesDTO.getResultMessage())){
            messageToShow = activity.getString(R.string.Message_UnknownError);
            showErrorMessageAndBack(messageToShow);
            return false;
        }

        boolean result = dtoToModel(getActivity(), itinerariesDTO,elementsItineraryModel);
        if(!result){
            messageToShow = activity.getString(R.string.Message_UnknownError);
            showErrorMessageAndBack(messageToShow);
            return false;
        }

        this.researchCode = CODE_ITINERARY_BY_USER_ID;
        this.researchString = null;
        this.userId = userId;

        this.itineraryListAdapter = new ItineraryListAdapter(getActivity(),elementsItineraryModel, true);
        return true;
    }

    public void initList(NestedScrollView nestedScrollView_itineraries,
                         RecyclerView recyclerView_itineraries,
                         ProgressBar progressBar_itineraries)
    {
        Activity activity = getActivity();
        final String[] messageToShow = {null};

        recyclerView_itineraries.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView_itineraries.setAdapter(itineraryListAdapter);

        nestedScrollView_itineraries.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            int page = 0;
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                    page++;
                    progressBar_itineraries.setVisibility(View.VISIBLE);

                    GetListItineraryResponseDTO itinerariesDTO;

                    if(researchCode == CODE_ITINERARY_RANDOM){
                        itinerariesDTO = itinearyDAO.getListItineraryRandom();
                    }
                    else if (researchCode == CODE_ITINERARY_BY_USER_ID){
                        itinerariesDTO = itinearyDAO.getListItineraryByIdUser(userId, page);
                    }
                    else if (researchCode == CODE_ITINERARY_BY_RESEARCH){
                        itinerariesDTO = itinearyDAO.getListItineraryByName(researchString, page);
                    }
                    else{
                        messageToShow[0] = activity.getString(R.string.Message_UnknownError);
                        showErrorMessageAndBack(messageToShow[0]);
                        return ;
                    }

                    ArrayList<ElementItineraryModel> nextPageElements = new ArrayList<ElementItineraryModel>();

                    boolean result = dtoToModel(getActivity(), itinerariesDTO, nextPageElements);
                    if(!result){
                        messageToShow[0] = activity.getString(R.string.Message_UnknownError);
                        showErrorMessageAndBack(messageToShow[0]);
                        return;
                    }

                    if(nextPageElements == null || nextPageElements.isEmpty()){
                        progressBar_itineraries.setVisibility(View.GONE);
                        return;
                    }

                    elementsItineraryModel.addAll(nextPageElements);
                    itineraryListAdapter.notifyDataSetChanged();
                }
            }
        });
    }


    public void updateList(long codeItineraryByResearch, String searchString) {
        Activity activity = getActivity();
        String messageToShow = null;

        if(codeItineraryByResearch < 0 || codeItineraryByResearch > 2){
            messageToShow = activity.getString(R.string.Message_UnknownError);
            showErrorMessageAndBack(messageToShow);
            return ;
        }

        elementsItineraryModel.clear();
        researchCode = codeItineraryByResearch;
        researchString = searchString;

        if(researchCode == CODE_ITINERARY_RANDOM) initModel();
        else if(researchCode == CODE_ITINERARY_BY_USER_ID) initModel(userId);
        else initModel(researchString);

    }

    public static boolean dtoToModel(Context context, GetItineraryWithUserResponseDTO dto, ElementItineraryModel model){

        model.clear();

        model.setItineraryId(dto.getId());
        model.setDescription(dto.getDescription());
        int difficulty = dto.getDifficulty();
        if(difficulty == 0) model.setDifficulty("Facile");
        else if(difficulty == 1) model.setDifficulty("Facile");
        else if(difficulty == 2) model.setDifficulty("Facile");
        else return false;

        float duration = dto.getDuration();
        String stringDuration = TimeUtils.toDurationString(duration);
        model.setDuration(stringDuration);

        float lenght = dto.getLenght();
        String stringLenght = TimeUtils.toDistanceString(lenght);
        model.setLenght(stringLenght);

        model.setName(dto.getName());

        model.setUserImage(dto.getUserImage());
        if(dto.getUserImage() != null) model.setUserImage(dto.getUserImage());
        else {
            Bitmap genericProfileImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_generic_account);
            model.setUserImage(genericProfileImage);
        }

        model.setUsername(dto.getUsername());

        return true;
    }

    public static boolean dtoToModel(Context context, GetItineraryResponseDTO dto, ElementItineraryModel model){

        model.clear();

        model.setItineraryId(dto.getId());
        model.setDescription(dto.getDescription());
        int difficulty = dto.getDifficulty();
        if(difficulty == 0) model.setDifficulty("Facile");
        else if(difficulty == 1) model.setDifficulty("Facile");
        else if(difficulty == 2) model.setDifficulty("Facile");
        else return false;

        float duration = dto.getDuration();
        String stringDuration = TimeUtils.toDurationString(duration);
        model.setDuration(stringDuration);

        float lenght = dto.getLenght();
        String stringLenght = TimeUtils.toDistanceString(lenght);
        model.setLenght(stringLenght);

        model.setName(dto.getName());

        return true;
    }


    public static boolean dtoToModel(Context context, GetListItineraryWithUserResponseDTO dto, List<ElementItineraryModel> model){
        model.clear();

        List<GetItineraryWithUserResponseDTO> dtos = dto.getListItinerary();

        for(GetItineraryWithUserResponseDTO elementDto : dtos){
            ElementItineraryModel elementModel = new ElementItineraryModel();
            boolean result = dtoToModel(context, elementDto, elementModel);
            if(!result) {
                model.clear();
                return false;
            }
            model.add(elementModel);
        }

        return true;
    }

    public static boolean dtoToModel(Context context, GetListItineraryResponseDTO dto, List<ElementItineraryModel> model){
        model.clear();

        List<GetItineraryResponseDTO> dtos = dto.getListItinerary();

        for(GetItineraryResponseDTO elementDto : dtos){
            ElementItineraryModel elementModel = new ElementItineraryModel();
            boolean result = dtoToModel(context, elementDto, elementModel);
            if(!result) {
                model.clear();
                return false;
            }
            model.add(elementModel);
        }

        return true;
    }

}
