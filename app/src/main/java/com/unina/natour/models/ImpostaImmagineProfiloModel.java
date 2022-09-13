package com.unina.natour.models;

import android.graphics.Bitmap;

import com.unina.natour.views.observers.Observable;
import com.unina.natour.views.observers.Observer;

import java.util.List;

import kotlin.collections.ArrayDeque;

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

}
