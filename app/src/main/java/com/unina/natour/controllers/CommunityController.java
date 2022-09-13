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


    public CommunityController(NaTourActivity activity){
        super(activity);
    }

}
