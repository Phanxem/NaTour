package com.unina.natour.views.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.unina.natour.R;
import com.unina.natour.controllers.AutenticazioneController;
import com.unina.natour.controllers.RecuperoPasswordController;

public class CompletaRecuperoPasswordActivity extends AppCompatActivity {

    private final static String TAG ="CompletaRecuperoPasswordActivity";

    private RecuperoPasswordController recuperoPasswordController;
    private AutenticazioneController autenticazioneController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completa_recupero_password);

        recuperoPasswordController = new RecuperoPasswordController(this);
        autenticazioneController = new AutenticazioneController(this);

        pressButtonComplete();
        pressIconBack();
    }


    public void pressButtonComplete(){
        Button button_complete = findViewById(R.id.CompletaRecuperoPassword_button_conferma);
        button_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText textField_code = findViewById(R.id.CompletaRecuperoPassword_editNumber_codice);
                String code = String.valueOf(textField_code.getText());

                EditText textField_password1 = findViewById(R.id.CompletaRecuperoPassword_editPassword_password1);
                String password1 = String.valueOf(textField_password1.getText());

                EditText textField_password2 = findViewById(R.id.CompletaRecuperoPassword_editPassword_password2);
                String password2 = String.valueOf(textField_password2.getText());

                Boolean result = recuperoPasswordController.completePasswordRecovery(code,password1,password2);

                if(result) autenticazioneController.openAutenticazioneActivity();
            }
        });
    }


    public void pressIconBack(){
        ImageView icon_back = findViewById(R.id.CompletaRecuperoPassword_imageView_iconaIndietro);
        icon_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recuperoPasswordController.back();
            }
        });
    }

}