package com.unina.natour.views.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Message;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.unina.natour.R;
import com.unina.natour.controllers.CommunityController;
import com.unina.natour.controllers.HomeController;
import com.unina.natour.controllers.MainController;
import com.unina.natour.views.dialogs.MessageDialog;

public class CommunityFragment extends NaTourFragment {
    CommunityController communityController;

    public static CommunityFragment newInstance(Parcelable controller){
        CommunityFragment communityFragment = new CommunityFragment();

        Bundle args = new Bundle();
        args.putParcelable(MainController.KEY_CONTROLLER, controller);
        communityFragment.setArguments(args);

        return communityFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_community, container, false);
        setFragmentView(view);


        Bundle args = getArguments();
        if(args != null){
            this.communityController = (CommunityController) args.getParcelable(MainController.KEY_CONTROLLER);
        }
        else{
            this.communityController = new CommunityController(getNaTourActivity());
        }

        return view;
    }
}