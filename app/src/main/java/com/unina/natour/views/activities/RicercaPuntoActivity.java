package com.unina.natour.views.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.unina.natour.R;
import com.unina.natour.controllers.RicercaPuntoController;
import com.unina.natour.models.AddressModel;
import com.unina.natour.models.RicercaPuntoModel;
import com.unina.natour.views.observers.Observer;

import java.util.List;

public class RicercaPuntoActivity extends AppCompatActivity implements Observer {

    RicercaPuntoController ricercaPuntoController;

    RicercaPuntoModel ricercaPuntoModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ricerca_punto);

        ricercaPuntoController = new RicercaPuntoController(this);

        ricercaPuntoModel = ricercaPuntoController.getModel();
        ricercaPuntoModel.registerObserver(this);

        ListView listView = findViewById(R.id.PointSearch_listView_risultati);
        ricercaPuntoController.initListViewResultPoints(listView);

        pressIconBack();
        pressCurrentPositionOption();
        pressSelectFromMapOption();
        searchFromSearchBar();


    }

    public void pressIconBack() {
        ImageView imageView_iconBack = findViewById(R.id.PointSearch_imageView_iconaIndietro);
        imageView_iconBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public void pressCurrentPositionOption() {
        RelativeLayout relativeLayout_selectCurrentPosition = findViewById(R.id.PointSearch_relativeLayout_posizioneAttuale);
        relativeLayout_selectCurrentPosition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ricercaPuntoController.selectCurrentPosition();
            }
        });
    }


    public void pressSelectFromMapOption() {
        RelativeLayout relativeLayout_selectFromMap = findViewById(R.id.PointSearch_relativeLayout_selezionaDaMappa);
        relativeLayout_selectFromMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ricercaPuntoController.selectFromMap();
            }
        });
    }


    public void searchFromSearchBar(){
        EditText editText_barraRicerca = findViewById(R.id.PointSearch_searchView_barraRicerca);
        editText_barraRicerca.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER){
                    String searchString = editText_barraRicerca.getText().toString();
                    ricercaPuntoController.searchInterestPoint(searchString);
                    editText_barraRicerca.onEditorAction(EditorInfo.IME_ACTION_DONE);
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }





    @Override
    public void update() {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                updateOptionsSelectPoint();
            }
        });
    }

    private void updateOptionsSelectPoint(){

        ConstraintLayout constraintLayout_opzioniSelezione = findViewById(R.id.PointSearch_constraintLayout_opzioniSelezionePunto);
        ConstraintLayout constraintLayout_risultatiPunti = findViewById(R.id.PointSearch_constraintLayout_sezioneRisultati);

        if(ricercaPuntoModel.hasResultPoints()){
            constraintLayout_opzioniSelezione.setVisibility(View.GONE);
            constraintLayout_risultatiPunti.setVisibility(View.VISIBLE);
            return;
        }

        constraintLayout_opzioniSelezione.setVisibility(View.VISIBLE);
        constraintLayout_risultatiPunti.setVisibility(View.GONE);
    }
}