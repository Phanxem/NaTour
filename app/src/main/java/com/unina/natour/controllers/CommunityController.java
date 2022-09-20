package com.unina.natour.controllers;

import com.unina.natour.views.activities.NaTourActivity;

public class CommunityController extends NaTourController {

    private ListaUtentiController listaUtentiController;

    public CommunityController(NaTourActivity activity){
        super(activity);

        this.listaUtentiController = new ListaUtentiController(activity, ListaUtentiController.CODE_USER_WITH_CONVERSATION, null);
    }

    public void searchUser(String searchString){
        listaUtentiController.updateList(listaUtentiController.CODE_USER_BY_RESEARCH, searchString);
    }

    public void cancelReseach() {
        listaUtentiController.updateList(ListaUtentiController.CODE_USER_WITH_CONVERSATION, null);
    }

    public ListaUtentiController getListaUtentiController() {
        return listaUtentiController;
    }

    public void setListaUtentiController(ListaUtentiController listaUtentiController) {
        this.listaUtentiController = listaUtentiController;
    }


}
