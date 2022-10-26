package com.unina.natour.views.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.unina.natour.R;
import com.unina.natour.controllers.AttivaAccountController;
import com.unina.natour.controllers.ImmagineProfiloController;
import com.unina.natour.views.dialogs.MessageDialog;

public class AttivaAccountActivity extends NaTourActivity {

    private AttivaAccountController attivaAccountController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attiva_account);

        attivaAccountController = new AttivaAccountController(this);
        attivaAccountController.initAccountActivation();

        Intent intent = this.getIntent();
        String username = intent.getStringExtra(AttivaAccountController.EXTRA_USERNAME);
        String email = intent.getStringExtra(AttivaAccountController.EXTRA_EMAIL);

        TextView textView_username = findViewById(R.id.AccountActivation_textView_username);
        TextView textView_email = findViewById(R.id.AccountActivation_textView_email);

        textView_username.setText(username);
        textView_email.setText(email);

        pressButtonConfirm();
        pressButtonCancel();
        pressTextResendCode();
    }

    public void pressButtonConfirm(){
        NaTourActivity activity = this;

        Button button_confirm = findViewById(R.id.AccountActivation_button_conferma);
        button_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText numberField_code = findViewById(R.id.AccountActivation_numberField_codice);
                String code = String.valueOf(numberField_code.getText());

                Boolean result = attivaAccountController.activeAccount(code);

                Log.i(TAG, "IS: " + result);

                if(result) ImmagineProfiloController.openPersonalizzaAccountImmagineActivity(activity,true);
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
        TextView textView_resend = findViewById(R.id.AccountActivation_textView_reinviaCodice);
        textView_resend.setOnClickListener(new View.OnClickListener() {
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