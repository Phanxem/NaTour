package com.unina.natour.controllers;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.fragment.app.FragmentActivity;

import com.unina.natour.models.ProfiloPersonaleModel;
import com.unina.natour.models.dao.implementation.UserDAOImpl;
import com.unina.natour.models.dao.interfaces.UserDAO;
import com.unina.natour.views.activities.NaTourActivity;
import com.unina.natour.views.dialogs.MessageDialog;

public class CommunityController extends NaTourController {

    private ListaUtentiController listaUtentiController;

    public CommunityController(NaTourActivity activity){
        super(activity);

        this.listaUtentiController = new ListaUtentiController(activity, ListaUtentiController.CODE_USER_WITH_CONVERSATION, null);
        throw new NullPointerException();
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
