package com.unina.natour.views.fragments;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.RecyclerView;

import com.unina.natour.R;
import com.unina.natour.controllers.AutenticazioneController;
import com.unina.natour.controllers.DisconnessioneController;
import com.unina.natour.controllers.ListaItinerariController;
import com.unina.natour.controllers.ModificaProfiloController;
import com.unina.natour.controllers.ProfiloController;
import com.unina.natour.controllers.utils.TimeUtils;
import com.unina.natour.models.ProfiloModel;
import com.unina.natour.views.activities.NaTourActivity;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RequiresApi(api = Build.VERSION_CODES.P)
public class ProfiloPersonaleFragment extends NaTourFragment{

    ProfiloController profiloController;

    ListaItinerariController listaItinerariController;
    DisconnessioneController disconnessioneController;


    ProfiloModel profiloModel;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profilo, container, false);
        setFragmentView(view);
        this.profiloController = new ProfiloController(getNaTourActivity());
        this.disconnessioneController = new DisconnessioneController(getNaTourActivity());
        this.listaItinerariController = profiloController.getListaItinerariController();

        this.profiloModel = profiloController.getModel();


        RecyclerView recyclerView_itineraries = view.findViewById(R.id.Profilo_recycleView_itinerari);
        NestedScrollView nestedScrollView_itineraries = view.findViewById(R.id.Profilo_nestedScrollView_itinerari);
        ProgressBar progressBar_itinearies = view.findViewById(R.id.Profilo_progressBar_itinerari);

        listaItinerariController.initList(nestedScrollView_itineraries,recyclerView_itineraries, progressBar_itinearies);

        pressMenuIcon();

        update();

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                profiloController.initModel();

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        update();
                    }
                });
            }
        });


        return view;
    }



    public void pressMenuIcon() {
        View view = getFragmentView();
        NaTourActivity activity = getNaTourActivity();
        ImageView imageView_iconMenu = view.findViewById(R.id.Profilo_imageView_iconaMenu);

        PopupMenu popupMenu = new PopupMenu(view.getContext(),imageView_iconMenu);
        popupMenu.getMenuInflater().inflate(R.menu.popup_menu_profilo_personale, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId() == R.id.ProfiloPersonaleF_popupMenu_modifica){
                    ModificaProfiloController.openModificaProfiloActivity(activity);
                    return true;
                }
                if(item.getItemId() == R.id.ProfiloPersonaleF_popupMenu_esci){
                    Boolean result = disconnessioneController.signOut();

                    if(result) AutenticazioneController.openAutenticazioneActivity(activity);

                    return true;
                }
                else return false;

            }
        });


        imageView_iconMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupMenu.show();
            }
        });
    }


    public void update(){
        View view = getFragmentView();

        String username = profiloModel.getUsername();
        TextView textView_username = view.findViewById(R.id.Profilo_textView_username);
        if(username != null && !username.isEmpty()) {
            textView_username.setBackgroundColor(Color.TRANSPARENT);
            textView_username.setText(username);
        }
        else {
            textView_username.setBackgroundColor(Color.GRAY);
            textView_username.setText("");
        }


        String email = profiloModel.getEmail();
        TextView textView_email = view.findViewById(R.id.Profilo_textView_email);
        textView_email.setText(email);
        if(email != null && !email.isEmpty()) {
            textView_email.setBackgroundColor(Color.TRANSPARENT);
            textView_email.setText(email);
        }
        else {
            textView_email.setBackgroundColor(Color.GRAY);
            textView_email.setText("");
        }

        LinearLayout linearLayout_residence = view.findViewById(R.id.Profilo_linearLayout_residence);
        if(profiloModel.getPlaceOfResidence() != null) {
            TextView textView_residence = view.findViewById(R.id.Profilo_textView_residence);
            textView_residence.setText(profiloModel.getPlaceOfResidence());
            linearLayout_residence.setVisibility(View.VISIBLE);
        }
        else linearLayout_residence.setVisibility(View.GONE);

        LinearLayout linearLayout_birth = view.findViewById(R.id.Profilo_linearLayout_birth);
        String dateOfBirth = profiloModel.getDateOfBirth();
        if(dateOfBirth != null && !dateOfBirth.isEmpty()) {
            TextView textView_birth = view.findViewById(R.id.Profilo_textView_birth);
            String date = TimeUtils.getDateWithoutHours(dateOfBirth);
            textView_birth.setText(date);
            linearLayout_birth.setVisibility(View.VISIBLE);
        }
        else linearLayout_birth.setVisibility(View.GONE);

        if(profiloModel.getProfileImage() != null){
            ImageView imageView_profilePicture = view.findViewById(R.id.Profilo_imageView_immagineProfilo);
            imageView_profilePicture.setImageBitmap(profiloModel.getProfileImage());
        }

    }

    @Override
    public void onResume() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
/*
        executorService.execute(new Runnable() {
            @Override
            public void run() {

                profiloController.initModel();

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        update();
                    }
                });
            }
        });

 */
        super.onResume();
    }

}