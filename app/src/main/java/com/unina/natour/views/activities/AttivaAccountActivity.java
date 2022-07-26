package com.unina.natour.views.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.unina.natour.R;
import com.unina.natour.controllers.AttivaAccountController;
import com.unina.natour.controllers.RegistrazioneController;

public class AttivaAccountActivity extends AppCompatActivity {

    private final static String TAG ="AttivaAcountActivity";
    private AttivaAccountController attivaAccountController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attiva_account);

        attivaAccountController = new AttivaAccountController(this);

        pressButtonConfirm();
        pressButtonCancel();
        pressTextResendCode();
    }

    public void pressButtonConfirm(){
        Button button_confirm = findViewById(R.id.AccountActivation_button_conferma);
        button_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText numberField_code = findViewById(R.id.AccountActivation_numberField_codice);
                String code = String.valueOf(numberField_code.getText());

                attivaAccountController.activeAccount(code);
            }
        });
    }

    public void pressButtonCancel(){
        Button button_cancel = findViewById(R.id.AccountActivation_button_annulla);
        button_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attivaAccountController.cancelAccountActivation();
            }
        });
    }

    public void pressTextResendCode(){
        Button button_cancel = findViewById(R.id.AccountActivation_button_annulla);
        button_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attivaAccountController.resendCode();
            }
        });
    }

    @Override
    public void onBackPressed() {
        attivaAccountController.back();
    }


}