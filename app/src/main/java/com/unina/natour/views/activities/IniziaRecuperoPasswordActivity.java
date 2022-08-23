package com.unina.natour.views.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.unina.natour.R;
import com.unina.natour.controllers.RecuperoPasswordController;

public class IniziaRecuperoPasswordActivity extends AppCompatActivity {

    private final static String TAG ="IniziaRecuperoPasswordActivity";

    private RecuperoPasswordController recuperoPasswordController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inizia_recupero_password);

        recuperoPasswordController = new RecuperoPasswordController(this);

        pressButtonNext();
        pressIconBack();
    }

    public void pressButtonNext(){
        Button button_next = findViewById(R.id.StartPasswordRecovery_button_avanti);
        button_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText textField_username = findViewById(R.id.StartPasswordRecovery_editText_username);
                String username = String.valueOf(textField_username.getText());

                Boolean result = recuperoPasswordController.startPasswordRecovery(username);

                if(result) recuperoPasswordController.openCompletaRecuperoPasswordActivity();
            }
        });
    }


    public void pressIconBack(){
        ImageView icon_back = findViewById(R.id.StartPasswordRecovery_imageView_iconaIndietro);
        icon_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recuperoPasswordController.back();
            }
        });
    }
}