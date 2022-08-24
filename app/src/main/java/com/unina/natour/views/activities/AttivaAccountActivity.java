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
import com.unina.natour.controllers.AutenticazioneController;
import com.unina.natour.controllers.ImpostaImmagineProfiloController;
import com.unina.natour.controllers.RegistrazioneController;
import com.unina.natour.views.dialogs.MessageDialog;
@RequiresApi(api = Build.VERSION_CODES.N)
public class AttivaAccountActivity extends AppCompatActivity {

    private final static String TAG ="AttivaAcountActivity";

    private AttivaAccountController attivaAccountController;
    private ImpostaImmagineProfiloController impostaImmagineProfiloController;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attiva_account);

        MessageDialog messageDialog = new MessageDialog();
        messageDialog.setSupportFragmentManager(getSupportFragmentManager());

        attivaAccountController = new AttivaAccountController(this, messageDialog);
        impostaImmagineProfiloController = new ImpostaImmagineProfiloController(this, messageDialog);

        attivaAccountController.initAccountActivation();

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

                Boolean result = attivaAccountController.activeAccount(code);

                Log.i(TAG, "IS: " + result);

                if(result) impostaImmagineProfiloController.openPersonalizzaAccountImmagineActivity(true);
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