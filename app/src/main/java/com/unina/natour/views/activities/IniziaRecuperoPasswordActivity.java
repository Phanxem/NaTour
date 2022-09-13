package com.unina.natour.views.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.unina.natour.R;
import com.unina.natour.controllers.RecuperoPasswordController;
import com.unina.natour.views.dialogs.MessageDialog;

@RequiresApi(api = Build.VERSION_CODES.N)
public class IniziaRecuperoPasswordActivity extends NaTourActivity {

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
        NaTourActivity activity = this;

        Button button_next = findViewById(R.id.StartPasswordRecovery_button_avanti);
        button_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText textField_username = findViewById(R.id.StartPasswordRecovery_editText_username);
                String username = String.valueOf(textField_username.getText());

                Boolean result = recuperoPasswordController.startPasswordRecovery(username);

                if(result) RecuperoPasswordController.openCompletaRecuperoPasswordActivity(activity);
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