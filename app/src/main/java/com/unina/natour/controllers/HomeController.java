package com.unina.natour.controllers;

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

}
