package com.unina.natour.controllers;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.RequiresApi;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.unina.natour.R;
import com.unina.natour.controllers.utils.TimeUtils;
import com.unina.natour.dto.response.ItineraryElementResponseDTO;
import com.unina.natour.dto.response.ItineraryListResponseDTO;
import com.unina.natour.models.ElementItineraryModel;
import com.unina.natour.models.dao.implementation.ItineraryDAOImpl;
import com.unina.natour.models.dao.interfaces.ItineraryDAO;
import com.unina.natour.views.activities.NaTourActivity;
import com.unina.natour.views.listAdapters.ItineraryListAdapter;

import java.util.ArrayList;
import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.N)
public class ListaItinerariController extends NaTourController{

    public final static long CODE_ITINERARY_RANDOM = 0;
    public final static long CODE_ITINERARY_BY_USERNAME = 1;
    public final static long CODE_ITINERARY_BY_RESEARCH = 2;

    private String researchString;
    private long researchCode;

    private ItineraryListAdapter itineraryListAdapter;

    private ArrayList<ElementItineraryModel> elementsItineraryModel;
    private int page = 0;

    private ItineraryDAO itinearyDAO;


    public ListaItinerariController(NaTourActivity activity, long researchCode, String researchString){
        super(activity);

        this.elementsItineraryModel = new ArrayList<ElementItineraryModel>();

        this.itinearyDAO = new ItineraryDAOImpl(activity);

        if(researchCode < 0 || researchCode > 2){
            //TODO EXCEPTION
            return;
        }
        initModel(researchCode, researchString);
    }

    public void initModel(long researchCode, String researchString){
        ItineraryListResponseDTO itinerariesDTO;
        boolean doBelongToSameUser;

        if(researchCode == CODE_ITINERARY_RANDOM){
            itinerariesDTO = itinearyDAO.getRandomItineraryList();
            doBelongToSameUser = false;
        }
        else if (researchCode == CODE_ITINERARY_BY_USERNAME){
            itinerariesDTO = itinearyDAO.getUserItinearyList(researchString);
            doBelongToSameUser = true;
        }
        else if (researchCode == CODE_ITINERARY_BY_RESEARCH){
            itinerariesDTO = itinearyDAO.findByName(researchString);
            doBelongToSameUser = false;
        }
        else{
            //TODO
            showErrorMessage(0);
            getActivity().finish();
            return;
        }

        this.researchString = researchString;
        this.researchCode = researchCode;

        boolean result = dtoToModel(getActivity(), itinerariesDTO,elementsItineraryModel);

        this.itineraryListAdapter = new ItineraryListAdapter(getActivity(),elementsItineraryModel, doBelongToSameUser);
    }

    public void initList(NestedScrollView nestedScrollView_itineraries,
                         RecyclerView recyclerView_itineraries,
                         ProgressBar progressBar_itineraries)
    {
        recyclerView_itineraries.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView_itineraries.setAdapter(itineraryListAdapter);



        nestedScrollView_itineraries.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                    // in this method we are incrementing page number,
                    // making progress bar visible and calling get data method.
                    page++;
                    progressBar_itineraries.setVisibility(View.VISIBLE);

                    ItineraryListResponseDTO itinerariesDTO;

                    if(researchCode == CODE_ITINERARY_RANDOM){
                        itinerariesDTO = itinearyDAO.getRandomItineraryList();
                    }
                    else if (researchCode == CODE_ITINERARY_BY_USERNAME){
                        itinerariesDTO = itinearyDAO.getUserItinearyList(researchString);
                    }
                    else if (researchCode == CODE_ITINERARY_BY_RESEARCH){
                        itinerariesDTO = itinearyDAO.findByName(researchString);
                    }
                    else{
                        //TODO EXCEPTION
                        return;
                    }

                    ArrayList<ElementItineraryModel> nextPageElements = new ArrayList<ElementItineraryModel>();

                    boolean result = dtoToModel(getActivity(), itinerariesDTO, nextPageElements);
                    if(!result){
                        //TODO ERRORMESSAGE
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
        if(codeItineraryByResearch < 0 || codeItineraryByResearch > 2){
            //TODO EXCEPTION
            return;
        }

        elementsItineraryModel.clear();
        researchCode = codeItineraryByResearch;
        researchString = searchString;

        initModel(codeItineraryByResearch, searchString);
    }

    public static boolean dtoToModel(Context context, ItineraryElementResponseDTO dto, ElementItineraryModel model){

        model.clear();

        model.setItineraryId(dto.getItineraryId());
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

    public static boolean dtoToModel(Context context, ItineraryListResponseDTO dto, List<ElementItineraryModel> model){
        model.clear();

        List<ItineraryElementResponseDTO> dtos = dto.getItineraries();

        for(ItineraryElementResponseDTO elementDto : dtos){
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
