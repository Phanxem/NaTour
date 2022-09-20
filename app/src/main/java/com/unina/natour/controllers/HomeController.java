package com.unina.natour.controllers;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;

import androidx.fragment.app.FragmentActivity;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.unina.natour.R;
import com.unina.natour.dto.response.ItineraryResponseDTO;
import com.unina.natour.models.dao.implementation.ItineraryDAOImpl;
import com.unina.natour.models.dao.interfaces.ItineraryDAO;
import com.unina.natour.views.activities.NaTourActivity;
import com.unina.natour.views.dialogs.MessageDialog;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public class HomeController extends NaTourController{

    private ListaItinerariController listaItinerariController;

    public HomeController(NaTourActivity activity) {
        super(activity);

        this.listaItinerariController = new ListaItinerariController(activity,ListaItinerariController.CODE_ITINERARY_RANDOM, null, -1);
    }

    public void searchItinerary(String searchString){
        listaItinerariController.updateList(ListaItinerariController.CODE_ITINERARY_BY_RESEARCH, searchString);
    }

    public void cancelReseach() {
        listaItinerariController.updateList(ListaItinerariController.CODE_ITINERARY_RANDOM, null);
    }

    public ListaItinerariController getListaItinerariController() {
        return listaItinerariController;
    }

    public void setListaItinerariController(ListaItinerariController listaItinerariController) {
        this.listaItinerariController = listaItinerariController;
    }


/*
    public void openAwsConfigurationFile(){
        Resources resources = getActivity().getResources();

        InputStream inputStream = resources.openRawResource(R.raw.awsconfiguration);
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        JsonElement jsonElement = JsonParser.parseReader(inputStreamReader);
        JsonObject jsonObject = jsonElement.getAsJsonObject();
    }
    */

}
