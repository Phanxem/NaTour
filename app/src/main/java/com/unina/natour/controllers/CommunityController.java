package com.unina.natour.controllers;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.fragment.app.FragmentActivity;

import com.unina.natour.models.ProfiloPersonaleModel;
import com.unina.natour.models.dao.implementation.UserDAOImpl;
import com.unina.natour.models.dao.interfaces.UserDAO;
import com.unina.natour.views.dialogs.MessageDialog;

public class CommunityController implements Parcelable {

    private final static String TAG ="ProfiloPersonaleController";

    FragmentActivity activity;
    MessageDialog messageDialog;


    public CommunityController(FragmentActivity activity, MessageDialog messageDialog){
        this.activity = activity;
        this.messageDialog = messageDialog;
    }

    public MessageDialog getMessageDialog() {
        return messageDialog;
    }
















    protected CommunityController(Parcel in) {
    }

    public static final Creator<CommunityController> CREATOR = new Creator<CommunityController>() {
        @Override
        public CommunityController createFromParcel(Parcel in) {
            return new CommunityController(in);
        }

        @Override
        public CommunityController[] newArray(int size) {
            return new CommunityController[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }
}
