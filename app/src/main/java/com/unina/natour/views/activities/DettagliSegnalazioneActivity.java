package com.unina.natour.views.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.unina.natour.R;
import com.unina.natour.controllers.DettagliSegnalazioneController;
import com.unina.natour.controllers.SegnalaItinerarioController;
import com.unina.natour.models.DettagliSegnalazioneModel;
import com.unina.natour.views.dialogs.EliminaItinerarioDialog;
import com.unina.natour.views.dialogs.RimuoviSegnalazioneDialog;

import org.w3c.dom.Text;

public class DettagliSegnalazioneActivity extends NaTourActivity {

    private DettagliSegnalazioneController dettagliSegnalazioneController;

    private DettagliSegnalazioneModel dettagliSegnalazioneModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dettagli_segnalazione);

        this.dettagliSegnalazioneController = new DettagliSegnalazioneController(this);
        this.dettagliSegnalazioneModel = dettagliSegnalazioneController.getModel();

        pressIconBack();
        pressIconMenu();
    }

    @Override
    protected void onResume() {
        dettagliSegnalazioneController.initModel();
        update();
        super.onResume();
    }

    public void pressIconBack(){
        ImageView imageView_back = findViewById(R.id.ReportDetails_imageView_iconaIndietro);
        imageView_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public void pressIconMenu(){
        NaTourActivity activity = this;

        ImageView imageView_menu = findViewById(R.id.ReportDetails_imageView_iconaMenu);

        PopupMenu popupMenu = new PopupMenu(this,imageView_menu);
        popupMenu.getMenuInflater().inflate(R.menu.popup_menu_dettagli_segnalazione, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId() == R.id.DettagliSegnalazione_popupMenu_elimina){
                    RimuoviSegnalazioneDialog rimuoviSegnalazioneDialog = new RimuoviSegnalazioneDialog(dettagliSegnalazioneModel);
                    rimuoviSegnalazioneDialog.setNaTourActivity(activity);
                    rimuoviSegnalazioneDialog.showOverUi();
                    return true;
                }
                else return false;
            }
        });

        imageView_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupMenu.show();
            }
        });
    }

    public void update(){
        if(dettagliSegnalazioneController.isMyReport()){
            ImageView imageView_menu = findViewById(R.id.ReportDetails_imageView_iconaMenu);
            imageView_menu.setVisibility(View.VISIBLE);
        }

        TextView textView_itineraryName = findViewById(R.id.ReportDetails_textView_nomeItinerario);
        textView_itineraryName.setText(dettagliSegnalazioneModel.getItineraryName());

        TextView textView_reportName = findViewById(R.id.ReportDetails_textView_nomeSegnalazione);
        textView_reportName.setText(dettagliSegnalazioneModel.getReportName());

        TextView textView_dateOfInput = findViewById(R.id.ReportDetails_textView_dateOfInput);
        textView_dateOfInput.setText(dettagliSegnalazioneModel.getDateOfInput());

        TextView textView_descrizione = findViewById(R.id.ReportDetails_textView_descrizione);
        textView_descrizione.setText(dettagliSegnalazioneModel.getDescrizione());
    }



}