package com.unina.natour.controllers;

import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.RequiresApi;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.unina.natour.controllers.utils.TimeUtils;
import com.unina.natour.dto.response.ElementItineraryResponseDTO;
import com.unina.natour.models.ElementItineraryModel;
import com.unina.natour.models.dao.implementation.ItineraryDAOImpl;
import com.unina.natour.models.dao.interfaces.ItineraryDAO;
import com.unina.natour.views.dialogs.MessageDialog;
import com.unina.natour.views.listAdapters.ItineraryListAdapter;

import java.util.ArrayList;
import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.N)
public class ListaItinerariController {

    private final static String TAG ="HomeController";

    FragmentActivity activity;
    MessageDialog messageDialog;
    String username;

    DettagliItinerarioController dettagliItinerarioController;

    ItineraryListAdapter itineraryListAdapter;

    ArrayList<ElementItineraryModel> elementsItineraryModel;
    int page = 0;

    ItineraryDAO itinearyDAO;


    public ListaItinerariController(FragmentActivity activity, MessageDialog messageDialog, String username){
        this.activity = activity;
        this.messageDialog = messageDialog;
        this.username = username;

        this.dettagliItinerarioController = new DettagliItinerarioController(activity, messageDialog);

        this.elementsItineraryModel = new ArrayList<ElementItineraryModel>();

        this.itinearyDAO = new ItineraryDAOImpl(activity);

        List<ElementItineraryResponseDTO> itinerariesDTO;
        boolean doBelongToSameUser;
        if(username == null || username.isEmpty()){
            itinerariesDTO = itinearyDAO.getRandomItineraryList();
            doBelongToSameUser = false;
        }
        else{
            itinerariesDTO = itinearyDAO.getUserItinearyList(username);
            doBelongToSameUser = true;
        }

        for(ElementItineraryResponseDTO element: itinerariesDTO){
            ElementItineraryModel modelElement = toElementItineraryModel(element);
            elementsItineraryModel.add(modelElement);
        }

        this.itineraryListAdapter = new ItineraryListAdapter(activity,elementsItineraryModel, doBelongToSameUser);
    }

    public void initItineraryList(NestedScrollView nestedScrollView_itineraries,
                                  RecyclerView recyclerView_itineraries,
                                  ProgressBar progressBar_itineraries)
    {
        recyclerView_itineraries.setLayoutManager(new LinearLayoutManager(activity));
        recyclerView_itineraries.setAdapter(itineraryListAdapter);

        nestedScrollView_itineraries.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                    // in this method we are incrementing page number,
                    // making progress bar visible and calling get data method.
                    page++;
                    progressBar_itineraries.setVisibility(View.VISIBLE);


                    List<ElementItineraryResponseDTO> itinerariesDTO;

                    if(username == null || username.isEmpty()){
                        itinerariesDTO = itinearyDAO.getRandomItineraryList();
                    }
                    else{
                        itinerariesDTO = itinearyDAO.getUserItinearyList(username);
                    }

                    ArrayList<ElementItineraryModel> nextPageElements = new ArrayList<ElementItineraryModel>();
                    for(ElementItineraryResponseDTO element: itinerariesDTO){
                        ElementItineraryModel modelElement = toElementItineraryModel(element);
                        nextPageElements.add(modelElement);
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


    public MessageDialog getMessageDialog() {
        return messageDialog;
    }









    public ElementItineraryModel toElementItineraryModel(ElementItineraryResponseDTO dto){
        ElementItineraryModel model = new ElementItineraryModel();

        model.setItineraryId(dto.getItineraryId());
        model.setDescription(dto.getDescription());
        int difficulty = dto.getDifficulty();
        if(difficulty == 0) model.setDifficulty("Facile");
        else if(difficulty == 1) model.setDifficulty("Facile");
        else if(difficulty == 2) model.setDifficulty("Facile");
        else{
            Log.i(TAG, "errore conversione dto to model");
            return null;
        }

        float duration = dto.getDuration();
        String stringDuration = TimeUtils.toDurationString(duration);
        model.setDuration(stringDuration);

        float lenght = dto.getLenght();
        String stringLenght = TimeUtils.toDistanceString(lenght);
        model.setLenght(stringLenght);

        model.setName(dto.getName());

        model.setUserImage(dto.getUserImage());

        model.setUsername(dto.getUsername());

        return model;
    }
}
