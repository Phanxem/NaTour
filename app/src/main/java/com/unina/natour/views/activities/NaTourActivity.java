package com.unina.natour.views.activities;

import androidx.appcompat.app.AppCompatActivity;

import com.unina.natour.models.NaTourModel;
import com.unina.natour.config.observers.Observer;

import java.util.ArrayList;
import java.util.List;

public class NaTourActivity extends AppCompatActivity implements Observer {

    public final String TAG = this.getClass().getSimpleName();

    private List<NaTourModel> models;

    public NaTourActivity(){
        this.models = new ArrayList<NaTourModel>();
    }

    public List<NaTourModel> getModels() {
        return models;
    }

    public void setModels(List<NaTourModel> models) {
        this.models = models;
    }

    public void addModel(NaTourModel model){
        this.models.add(model);
    }

    public void removeModel(NaTourModel model){
        if(!models.isEmpty()) this.models.remove(model);
    }

    @Override
    protected void onResume() {
        for(NaTourModel model : models){
            model.registerObserver(this);
            model.notifyObservers();
        }
        super.onResume();
    }

    @Override
    protected void onStop() {
        for(NaTourModel model : models){
            model.undergisterObserver(this);
        }
        super.onStop();
    }

    @Override
    public void update() {

    }
}
