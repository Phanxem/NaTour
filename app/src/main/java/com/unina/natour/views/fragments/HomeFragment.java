package com.unina.natour.views.fragments;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.unina.natour.R;
import com.unina.natour.controllers.DettagliItinerarioController;
import com.unina.natour.controllers.HomeController;
import com.unina.natour.controllers.ListaItinerariController;
import com.unina.natour.controllers.MainController;
import com.unina.natour.views.dialogs.MessageDialog;
@RequiresApi(api = Build.VERSION_CODES.N)
public class HomeFragment extends NaTourFragment {
    HomeController homeController;

    ListaItinerariController listaItinerariController;
/*
    public static HomeFragment newInstance(Parcelable controller){
        HomeFragment homeFragment = new HomeFragment();

        Bundle args = new Bundle();
        args.putParcelable(MainController.KEY_CONTROLLER, controller);
        homeFragment.setArguments(args);

        return homeFragment;
    }
*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_home, container, false);
        setFragmentView(view);
/*
        Bundle args = getArguments();
        if(args != null){
            this.homeController = (HomeController) args.getParcelable(MainController.KEY_CONTROLLER);
        }
        else{

        }
 */
        this.homeController = new HomeController(getNaTourActivity());
        this.listaItinerariController = new ListaItinerariController(getNaTourActivity(),null);


        //ListView listView_itineraries = view.findViewById(R.id.HomeF_listView_itineraries);
        //listaItinerariController.initItineraryList(listView_itineraries);

        RecyclerView recyclerView_itineraries = view.findViewById(R.id.HomeF_recycleView_itinerari);
        NestedScrollView nestedScrollView_itineraries = view.findViewById(R.id.HomeF_nestedScrollView_itinerari);
        ProgressBar progressBar_itinearies = view.findViewById(R.id.HomeF_progressBar_itinerari);

        listaItinerariController.initItineraryList(nestedScrollView_itineraries,recyclerView_itineraries, progressBar_itinearies);

        return view;
    }




}