package com.unina.natour.views.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

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

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ProfiloUtenteActivity extends NaTourActivity{

    private ProfiloController profiloController;
    private ListaItinerariController listaItinerariController;

    private ProfiloModel profiloModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_profilo);

        this.profiloController = new ProfiloController(this);
        this.listaItinerariController = profiloController.getListaItinerariController();

        this.profiloModel = profiloController.getModel();


        RecyclerView recyclerView_itineraries = findViewById(R.id.Profilo_recycleView_itinerari);
        NestedScrollView nestedScrollView_itineraries = findViewById(R.id.Profilo_nestedScrollView_itinerari);
        ProgressBar progressBar_itinearies = findViewById(R.id.Profilo_progressBar_itinerari);

        listaItinerariController.initList(nestedScrollView_itineraries,recyclerView_itineraries, progressBar_itinearies);

        ImageView imageView_menuIcon = findViewById(R.id.Profilo_imageView_iconaMenu);
        TextView textView_titolo = findViewById(R.id.Profilo_textView_titolo);
        RelativeLayout relativeLayout_sendMessage = findViewById(R.id.Profilo_relativeLayout_sendMessage);

        imageView_menuIcon.setVisibility(View.GONE);
        textView_titolo.setVisibility(View.INVISIBLE);
        relativeLayout_sendMessage.setVisibility(View.VISIBLE);

        pressSendMessage();

        update();

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent();
                long userId = intent.getLongExtra(ProfiloController.EXTRA_USER_ID,-1);

                profiloController.initModel(userId);

                handler.post(new Runnable() {
                    @Override
                    public void run() { update(); }
                });
            }
        });
    }

    public void pressSendMessage() {
        NaTourActivity activity = this;
        RelativeLayout relativeLayout_sendMessage = findViewById(R.id.Profilo_relativeLayout_sendMessage);
        relativeLayout_sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //openChatUtenteActivity(this, userId);
            }
        });
    }

    public void update(){
        String username = profiloModel.getUsername();
        TextView textView_username = findViewById(R.id.Profilo_textView_username);
        if(username != null && !username.isEmpty()) {
            textView_username.setBackgroundColor(Color.TRANSPARENT);
            textView_username.setText(username);
        }
        else {
            //textView_username.setBackgroundColor(Color.GRAY);
            textView_username.setText("");
        }


        String email = profiloModel.getEmail();
        TextView textView_email = findViewById(R.id.Profilo_textView_email);
        textView_email.setText(email);
        if(email != null && !email.isEmpty()) {
            //textView_email.setBackgroundColor(Color.TRANSPARENT);
            textView_email.setText(email);
        }
        else {
            //textView_email.setBackgroundColor(Color.GRAY);
            textView_email.setText("");
        }

        LinearLayout linearLayout_residence = findViewById(R.id.Profilo_linearLayout_residence);
        if(profiloModel.getPlaceOfResidence() != null) {
            TextView textView_residence = findViewById(R.id.Profilo_textView_residence);
            textView_residence.setText(profiloModel.getPlaceOfResidence());
            linearLayout_residence.setVisibility(View.VISIBLE);
        }
        else linearLayout_residence.setVisibility(View.GONE);

        LinearLayout linearLayout_birth = findViewById(R.id.Profilo_linearLayout_birth);
        String dateOfBirth = profiloModel.getDateOfBirth();
        if(dateOfBirth != null && !dateOfBirth.isEmpty()) {
            TextView textView_birth = findViewById(R.id.Profilo_textView_birth);
            String date = TimeUtils.getDateWithoutHours(dateOfBirth);
            textView_birth.setText(date);
            linearLayout_birth.setVisibility(View.VISIBLE);
        }
        else linearLayout_birth.setVisibility(View.GONE);

        if(profiloModel.getProfileImage() != null){
            ImageView imageView_profilePicture = findViewById(R.id.Profilo_imageView_immagineProfilo);
            imageView_profilePicture.setImageBitmap(profiloModel.getProfileImage());
        }

    }

    @Override
    public void onResume() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent();
                long userId = intent.getLongExtra(ProfiloController.EXTRA_USER_ID,-1);

                profiloController.initModel(userId);

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        update();
                    }
                });
            }
        });
        super.onResume();
    }

}
