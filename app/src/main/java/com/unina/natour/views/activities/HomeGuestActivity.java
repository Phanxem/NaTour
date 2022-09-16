package com.unina.natour.views.activities;

import android.os.Bundle;
import android.widget.ProgressBar;

import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.RecyclerView;

import com.unina.natour.R;
import com.unina.natour.controllers.HomeController;
import com.unina.natour.controllers.ListaItinerariController;

public class HomeGuestActivity extends NaTourActivity{

    HomeController homeController;
    ListaItinerariController listaItinerariController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_home);

        this.homeController = new HomeController(this);
        this.listaItinerariController = new ListaItinerariController(this,ListaItinerariController.CODE_ITINERARY_RANDOM, null);

        RecyclerView recyclerView_itineraries = findViewById(R.id.HomeF_recycleView_itinerari);
        NestedScrollView nestedScrollView_itineraries = findViewById(R.id.HomeF_nestedScrollView_itinerari);
        ProgressBar progressBar_itinearies = findViewById(R.id.HomeF_progressBar_itinerari);

        listaItinerariController.initItineraryList(nestedScrollView_itineraries,recyclerView_itineraries, progressBar_itinearies);

    }
}
