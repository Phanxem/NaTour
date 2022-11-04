package com.unina.natour.views.fragments;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.RecyclerView;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.unina.natour.R;
import com.unina.natour.controllers.HomeController;
import com.unina.natour.controllers.ListaItinerariController;


public class HomeFragment extends NaTourFragment {
    private View view;
    private HomeController homeController;
    private ListaItinerariController listaItinerariController;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_home, container, false);
        setFragmentView(view);

        this.homeController = new HomeController(getNaTourActivity());
        this.listaItinerariController = homeController.getListaItinerariController();

        RecyclerView recyclerView_itineraries = view.findViewById(R.id.HomeF_recycleView_itinerari);
        NestedScrollView nestedScrollView_itineraries = view.findViewById(R.id.HomeF_nestedScrollView_itinerari);
        ProgressBar progressBar_itinearies = view.findViewById(R.id.HomeF_progressBar_itinerari);

        listaItinerariController.initList(nestedScrollView_itineraries,recyclerView_itineraries, progressBar_itinearies);

        searchFromSearchBar();
        pressIconCancel();

        return view;
    }

    public void searchFromSearchBar(){
        TextView textView_potrebbeInteressarti = view.findViewById(R.id.HomeF_textView_potrebbeInteressarti);
        ImageView imageView_iconClose = view.findViewById(R.id.HomeF_imageView_cancelResearch);
        RecyclerView recyclerView_itineraries = view.findViewById(R.id.HomeF_recycleView_itinerari);
        NestedScrollView nestedScrollView_itineraries = view.findViewById(R.id.HomeF_nestedScrollView_itinerari);
        ProgressBar progressBar_itinearies = view.findViewById(R.id.HomeF_progressBar_itinerari);


        EditText editText_barraRicerca = view.findViewById(R.id.HomeF_editText_research);
        editText_barraRicerca.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER){
                    String searchString = editText_barraRicerca.getText().toString();
                    if(searchString != null && !searchString.isEmpty()) {
                        homeController.searchItinerary(searchString);
                        listaItinerariController.initList(nestedScrollView_itineraries,recyclerView_itineraries, progressBar_itinearies);

                        editText_barraRicerca.onEditorAction(EditorInfo.IME_ACTION_DONE);
                        textView_potrebbeInteressarti.setVisibility(View.GONE);
                        imageView_iconClose.setVisibility(View.VISIBLE);
                        return true;
                    }
                }
                return false;
            }
        });
    }

    public void pressIconCancel(){
        TextView textView_potrebbeInteressarti = view.findViewById(R.id.HomeF_textView_potrebbeInteressarti);
        EditText editText_barraRicerca = view.findViewById(R.id.HomeF_editText_research);
        RecyclerView recyclerView_itineraries = view.findViewById(R.id.HomeF_recycleView_itinerari);
        NestedScrollView nestedScrollView_itineraries = view.findViewById(R.id.HomeF_nestedScrollView_itinerari);
        ProgressBar progressBar_itinearies = view.findViewById(R.id.HomeF_progressBar_itinerari);

        ImageView imageView_iconClose = view.findViewById(R.id.HomeF_imageView_cancelResearch);
        imageView_iconClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homeController.cancelReseach();
                listaItinerariController.initList(nestedScrollView_itineraries,recyclerView_itineraries, progressBar_itinearies);

                editText_barraRicerca.setText("");
                textView_potrebbeInteressarti.setVisibility(View.VISIBLE);
                imageView_iconClose.setVisibility(View.GONE);
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();

        this.homeController = new HomeController(getNaTourActivity());
        this.listaItinerariController = homeController.getListaItinerariController();

        RecyclerView recyclerView_itineraries = view.findViewById(R.id.HomeF_recycleView_itinerari);
        NestedScrollView nestedScrollView_itineraries = view.findViewById(R.id.HomeF_nestedScrollView_itinerari);
        ProgressBar progressBar_itinearies = view.findViewById(R.id.HomeF_progressBar_itinerari);

        listaItinerariController.initList(nestedScrollView_itineraries,recyclerView_itineraries, progressBar_itinearies);

    }




}