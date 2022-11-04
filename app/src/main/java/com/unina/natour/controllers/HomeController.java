package com.unina.natour.controllers;

import android.content.Intent;

import com.unina.natour.views.activities.AutenticazioneActivity;
import com.unina.natour.views.activities.HomeGuestActivity;
import com.unina.natour.views.activities.NaTourActivity;

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



    public static void openHomeGuestActivity(NaTourActivity fromActivity){
        Intent intent = new Intent(fromActivity, HomeGuestActivity.class);
        fromActivity.startActivity(intent);
        fromActivity.finish();
    }

}
