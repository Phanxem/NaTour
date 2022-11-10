package com.unina.natour.controllers;

import android.app.Activity;

import com.unina.natour.R;
import com.unina.natour.dto.response.composted.GetUserWithImageResponseDTO;
import com.unina.natour.models.ElementUserModel;
import com.unina.natour.models.dao.implementation.UserDAOImpl;
import com.unina.natour.models.dao.interfaces.UserDAO;
import com.unina.natour.views.activities.NaTourActivity;

import javax.xml.transform.Result;

public class CommunityController extends NaTourController {

    private ListaUtentiController listaUtentiController;

    private UserDAO userDAO;

    public CommunityController(NaTourActivity activity,
                               ResultMessageController resultMessageController,
                               ListaUtentiController listaUtentiController)
    {
        super(activity, resultMessageController);

        this.listaUtentiController = listaUtentiController;

    }

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
