package com.unina.natour.views.fragments;

import android.view.View;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.unina.natour.models.NaTourModel;
import com.unina.natour.views.activities.NaTourActivity;
import com.unina.natour.views.observers.Observer;

import java.util.ArrayList;
import java.util.List;

public class NaTourFragment extends Fragment implements Observer {

    public final String TAG = this.getClass().getSimpleName();

    private View fragmentView;
    private NaTourActivity activity;
    private List<NaTourModel> models;

    public NaTourFragment(){
        this.models = new ArrayList<NaTourModel>();
    }

    public View getFragmentView() {
        return fragmentView;
    }

    public void setFragmentView(View fragmentView) {
        this.fragmentView = fragmentView;
    }


    public NaTourActivity getNaTourActivity() {
        return activity;
    }

    public void setNaTourActivity(NaTourActivity activity) {
        this.activity = activity;
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
    public void onResume() {
        for(NaTourModel model : models){
            model.registerObserver(this);
        }
        super.onResume();
    }

    @Override
    public void onStop() {
        for(NaTourModel model : models){
            model.undergisterObserver(this);
        }
        super.onStop();
    }

    @Override
    public void update() {

    }
}
