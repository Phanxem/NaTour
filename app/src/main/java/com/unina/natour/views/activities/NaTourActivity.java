package com.unina.natour.views.activities;

import androidx.fragment.app.FragmentActivity;

import com.unina.natour.views.observers.Observer;

public class NaTourActivity extends FragmentActivity implements Observer {

    public final String TAG = this.getClass().getSimpleName();

    public NaTourActivity(){
    }


    @Override
    public void update() {

    }
}
