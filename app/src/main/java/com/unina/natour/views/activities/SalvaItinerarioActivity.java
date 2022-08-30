package com.unina.natour.views.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.unina.natour.R;
import com.unina.natour.controllers.AutenticazioneController;
import com.unina.natour.controllers.SalvaItinerarioController;
import com.unina.natour.controllers.utils.TimeUtils;
import com.unina.natour.models.SalvaItinerarioModel;
import com.unina.natour.views.dialogs.MessageDialog;
import com.unina.natour.views.dialogs.SelectCountryDialog;
import com.unina.natour.views.dialogs.SelectDurationDialog;
import com.unina.natour.views.observers.Observer;
@RequiresApi(api = Build.VERSION_CODES.N)
public class SalvaItinerarioActivity extends AppCompatActivity
                                     implements Observer , SelectDurationDialog.OnDurationListener{

    private final static String TAG ="AutenticazioneActivity";

    private SalvaItinerarioController salvaItinerarioController;

    private SalvaItinerarioModel salvaItinerarioModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salva_itinerario);

        MessageDialog messageDialog = new MessageDialog();
        messageDialog.setFragmentActivity(this);

        salvaItinerarioController = new SalvaItinerarioController(this, messageDialog);
        salvaItinerarioModel = salvaItinerarioController.getModel();
        salvaItinerarioModel.registerObserver(this);


        TextView textView_duration = findViewById(R.id.SaveItinerary_textView_duration);
        String stringDuration = TimeUtils.toDurationString(salvaItinerarioModel.getDefaultDuration());
        textView_duration.setText(stringDuration);

        TextView textView_distance = findViewById(R.id.SaveItinerary_textView_distance);
        String stringDistance = TimeUtils.toDistanceString(salvaItinerarioModel.getDistance());
        textView_distance.setText(stringDistance);

        pressButtonDifficulyEasy();
        pressButtonDifficulyMedium();
        pressButtonDifficulyHard();
        pressTextUpdateDuration();
        pressIconCancelDuration();
        pressButtonSave();
        pressIconBack();


    }

    public void pressButtonDifficulyEasy(){
        Button button_easy = findViewById(R.id.SaveItinerary_button_difficoltàSemplice);
        button_easy.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                salvaItinerarioController.setDifficulty(SalvaItinerarioController.CODE_DIFFICULTY_EASY);
            }
        });
    }

    public void pressButtonDifficulyMedium(){
        Button button_medium = findViewById(R.id.SaveItinerary_button_difficoltàIntermedio);
        button_medium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                salvaItinerarioController.setDifficulty(SalvaItinerarioController.CODE_DIFFICULTY_MEDIUM);
            }
        });
    }

    public void pressButtonDifficulyHard(){
        Button button_hard = findViewById(R.id.SaveItinerary_button_difficoltàDifficile);
        button_hard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                salvaItinerarioController.setDifficulty(SalvaItinerarioController.CODE_DIFFICULTY_HARD);
            }
        });
    }

    public void pressTextUpdateDuration(){
        TextView textView_updateDuration = findViewById(R.id.SaveItinerary_textView_cambiaDurata);
        textView_updateDuration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectDurationDialog selectDurationDialog = new SelectDurationDialog();
                selectDurationDialog.show(getSupportFragmentManager(),TAG);
            }
        });
    }

    public void pressIconCancelDuration(){
        ImageView imageView_cancel = findViewById(R.id.SaveItinerary_imageView_cancelDuration);
        imageView_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                salvaItinerarioController.resetDuration();
            }
        });
    }

    public void pressButtonSave(){
        Button button_save = findViewById(R.id.SaveItinerary_button_salva);
        button_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editText_titolo = findViewById(R.id.SaveItinerary_textField_titoloItinerario);
                String titolo = editText_titolo.getText().toString();

                EditText editText_descrizione = findViewById(R.id.SaveItinerary_textField_descrizione);
                String descrizione = editText_descrizione.getText().toString();

                boolean result = salvaItinerarioController.saveItinerary(titolo, descrizione);
                //TODO if true go to main activity
            }
        });
    }

    public void pressIconBack(){
        ImageView imageView_back = findViewById(R.id.SaveItinerary_imageView_iconaIndietro);
        imageView_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                salvaItinerarioController.back();
            }
        });
    }

    @Override
    public void getDuration(Float duration) {
        salvaItinerarioController.setDuration(duration);
    }

    @Override
    public void update() {

        Integer difficulty = salvaItinerarioModel.getDifficulty();

        Button button_easy = findViewById(R.id.SaveItinerary_button_difficoltàSemplice);
        Button button_medium = findViewById(R.id.SaveItinerary_button_difficoltàIntermedio);
        Button button_hard = findViewById(R.id.SaveItinerary_button_difficoltàDifficile);
        if(difficulty == null){
            button_easy.setBackgroundTintList(this.getResources().getColorStateList(R.color.gray_blue));
            button_medium.setBackgroundTintList(this.getResources().getColorStateList(R.color.gray_blue));
            button_hard.setBackgroundTintList(this.getResources().getColorStateList(R.color.gray_blue));
        }
        else if (difficulty == SalvaItinerarioController.CODE_DIFFICULTY_EASY){
            button_easy.setBackgroundTintList(this.getResources().getColorStateList(R.color.blue));
            button_medium.setBackgroundTintList(this.getResources().getColorStateList(R.color.gray_blue));
            button_hard.setBackgroundTintList(this.getResources().getColorStateList(R.color.gray_blue));
        }
        else if (difficulty == SalvaItinerarioController.CODE_DIFFICULTY_MEDIUM){
            button_easy.setBackgroundTintList(this.getResources().getColorStateList(R.color.gray_blue));
            button_medium.setBackgroundTintList(this.getResources().getColorStateList(R.color.blue));
            button_hard.setBackgroundTintList(this.getResources().getColorStateList(R.color.gray_blue));
        }
        else {
            button_easy.setBackgroundTintList(this.getResources().getColorStateList(R.color.gray_blue));
            button_medium.setBackgroundTintList(this.getResources().getColorStateList(R.color.gray_blue));
            button_hard.setBackgroundTintList(this.getResources().getColorStateList(R.color.blue));
        }


        float duration = salvaItinerarioModel.getDuration();
        String stringDuration;
        TextView textView_duration = findViewById(R.id.SaveItinerary_textView_duration);

        TextView textView_updateDuration = findViewById(R.id.SaveItinerary_textView_cambiaDurata);
        ImageView imageView_cancelDuration = findViewById(R.id.SaveItinerary_imageView_cancelDuration);

        if(duration < 0){
            stringDuration = TimeUtils.toDurationString(salvaItinerarioModel.getDefaultDuration());
            textView_updateDuration.setVisibility(View.VISIBLE);
            imageView_cancelDuration.setVisibility(View.GONE);
        }
        else{
            stringDuration = TimeUtils.toDurationString(duration);
            textView_updateDuration.setVisibility(View.GONE);
            imageView_cancelDuration.setVisibility(View.VISIBLE);
        }
        textView_duration.setText(stringDuration);


    }


}