package com.unina.natour.models;

import android.graphics.Bitmap;

public class ImpostaImmagineProfiloModel extends NaTourModel {
    private Bitmap profileImage;


    public ImpostaImmagineProfiloModel(){
        super();
    }


    public Bitmap getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(Bitmap profileImage) {
        this.profileImage = profileImage;
        notifyObservers();
    }

    public void clear(){
        this.profileImage = null;
    }

}
