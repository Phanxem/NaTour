package com.unina.natour.views.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.unina.natour.R;
//TODO da aggiustare (capire perché da errore se si importa solo la classe interessata)
import com.unina.natour.controllers.*;
import com.unina.natour.views.dialogs.MessageDialog;

@RequiresApi(api = Build.VERSION_CODES.P)
public class ModificaProfiloActivity extends NaTourActivity {

    public ModificaProfiloController modificaProfiloController;



    // username

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifica_profilo);

        modificaProfiloController = new ModificaProfiloController(this);

        //modificaProfiloController.initModel(username);


    }

    public void pressIconBack(){
        ImageView imageView_iconBack = findViewById(R.id.ModificaProfilo_imageView_iconBack);
        imageView_iconBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //todo back
            }
        });
    }

    public void pressTextUpdateImage(){
        TextView textView_updateImage = findViewById(R.id.ModificaProfilo_textView_modificaImmagineProfilo);
        textView_updateImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    public void pressTextUpdateOptionalInfo(){
        TextView textView_updateOptionalInfo = findViewById(R.id.ModificaProfilo_textView_modificaOptionalInfo);
        textView_updateOptionalInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    public void pressTextUpdatePassword(){
        TextView textView_updatePassword = findViewById(R.id.ModificaProfilo_textView_modificaPassword);
        textView_updatePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    public void pressButtonLinkFacebookAccount(){
        Button button_facebook = findViewById(R.id.ModificaProfilo_button_facebook);
        button_facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    public void pressButtonLinkGoogleAccount(){
        Button button_google = findViewById(R.id.ModificaProfilo_button_google);
        button_google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

}