package com.unina.natour.views.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

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
@RequiresApi(api = Build.VERSION_CODES.P)
public class AttivaAccountActivity extends NaTourActivity {

    private AttivaAccountController attivaAccountController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attiva_account);

        attivaAccountController = new AttivaAccountController(this);
        attivaAccountController.initAccountActivation();

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