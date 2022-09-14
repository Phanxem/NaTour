package com.unina.natour.controllers;

import android.os.Parcel;
import android.os.Parcelable;

import com.unina.natour.views.activities.NaTourActivity;
import com.unina.natour.views.dialogs.MessageDialog;

public class NaTourController {

    public final String TAG = this.getClass().getSimpleName();

    private NaTourActivity activity;
    private MessageDialog messageDialog;

    public NaTourController(NaTourActivity activity){
        this.activity = activity;
        this.messageDialog = new MessageDialog();
        this.messageDialog.setNaTourActivity(activity);
    }

    public NaTourController(){}

    public NaTourActivity getActivity() {
        return activity;
    }

    public void setActivity(NaTourActivity activity) {
        this.activity = activity;
    }

    public MessageDialog getMessageDialog() {
        return messageDialog;
    }

    public void setMessageDialog(MessageDialog messageDialog) {
        this.messageDialog = messageDialog;
    }


//---

}
