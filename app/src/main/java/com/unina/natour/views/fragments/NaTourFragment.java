package com.unina.natour.views.fragments;

import android.view.View;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.unina.natour.views.activities.NaTourActivity;
import com.unina.natour.views.observers.Observer;

public class NaTourFragment extends Fragment implements Observer {

    public final String TAG = this.getClass().getSimpleName();

    private View fragmentView;
    private NaTourActivity activity;

    public NaTourFragment(){ }

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


    @Override
    public void update() {

    }
}
