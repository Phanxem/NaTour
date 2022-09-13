package com.unina.natour.controllers;

import android.os.Parcel;
import android.os.Parcelable;

import com.unina.natour.views.activities.NaTourActivity;
import com.unina.natour.views.dialogs.MessageDialog;

public class NaTourController implements Parcelable {

    public final String TAG = this.getClass().getSimpleName();

    private NaTourActivity activity;
    private MessageDialog messageDialog;

    public NaTourController(NaTourActivity activity){
        this.activity = activity;
        this.messageDialog = new MessageDialog();
        this.messageDialog.setNaTourActivity(activity);
    }

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

    protected NaTourController(Parcel in) {
    }

    public static final Creator<NaTourController> CREATOR = new Creator<NaTourController>() {
        @Override
        public NaTourController createFromParcel(Parcel in) {
            return new NaTourController(in);
        }

        @Override
        public NaTourController[] newArray(int size) {
            return new NaTourController[size];
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
