package com.unina.natour.views.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.unina.natour.R;
import com.unina.natour.controllers.HomeController;
import com.unina.natour.controllers.MainController;
import com.unina.natour.controllers.PianificaItinerarioController;
import com.unina.natour.views.dialogs.MessageDialog;

public class HomeFragment extends Fragment {

    View view;
    MessageDialog messageDialog;

    HomeController homeController;

    public static HomeFragment newInstance(Parcelable controller){
        HomeFragment homeFragment = new HomeFragment();

        Bundle args = new Bundle();
        args.putParcelable(MainController.KEY_CONTROLLER, controller);
        homeFragment.setArguments(args);

        return homeFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_home, container, false);

        Bundle args = getArguments();
        if(args != null){
            this.homeController = (HomeController) args.getParcelable(MainController.KEY_CONTROLLER);
            this.messageDialog = homeController.getMessageDialog();
        }
        else{
            this.messageDialog = new MessageDialog();
            this.messageDialog.setFragmentActivity(getActivity());
            this.homeController = new HomeController(getActivity(),messageDialog);
        }


        return view;
    }




}