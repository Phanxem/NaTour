package com.unina.natour.views.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.unina.natour.R;
import com.unina.natour.controllers.MainController;
import com.unina.natour.controllers.SegnalaItinerarioController;

public class SegnalaItinerarioActivity extends NaTourActivity {

    private SegnalaItinerarioController segnalaItinerarioController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_segnala_itinerario);

        segnalaItinerarioController = new SegnalaItinerarioController(this);

        pressIconBack();
        pressButtonReport();
    }

    public void pressIconBack(){
        NaTourActivity activity = this;

        ImageView imageView_iconBack = findViewById(R.id.ReportItinerary_imageView_iconaIndietro);
        imageView_iconBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public void pressButtonReport(){
        NaTourActivity activity = this;

        Button button_report = findViewById(R.id.ReportItinerary_button_segnala);
        button_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editText_titolo = findViewById(R.id.ReportItinerary_textField_titoloSegnalazione);
                String titolo = String.valueOf(editText_titolo.getText());

                EditText editText_descrizione = findViewById(R.id.ReportItinerary_textField_descrizione);
                String descrizione = String.valueOf(editText_descrizione.getText());

                boolean result = segnalaItinerarioController.inviaSegnalazione(titolo, descrizione);

                if(result) activity.finish();
            }
        });
    }
}