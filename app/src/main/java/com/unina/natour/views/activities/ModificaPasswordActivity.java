package com.unina.natour.views.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.unina.natour.R;
import com.unina.natour.controllers.ModificaPasswordController;
import com.unina.natour.controllers.ModificaProfiloController;
@RequiresApi(api = Build.VERSION_CODES.P)
public class ModificaPasswordActivity extends NaTourActivity {

    ModificaPasswordController modificaProfiloController;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifica_password);

        this.modificaProfiloController = new ModificaPasswordController(this);

        pressButtonUpdate();
    }

    public void pressButtonUpdate(){
        NaTourActivity activity = this;

        Button button_update = findViewById(R.id.ModificaPassword_button_update);
        button_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editText_oldPassword = findViewById(R.id.ModificaPassword_editText_oldPassword);
                EditText editText_newPassword = findViewById(R.id.ModificaPassword_editText_newPassword);
                EditText editText_newPassword2 = findViewById(R.id.ModificaPassword_editText_newPassword2);

                String oldPassword = String.valueOf(editText_oldPassword.getText());
                String newPassword = String.valueOf(editText_newPassword.getText());
                String newPassword2 = String.valueOf(editText_newPassword2.getText());

                boolean result = modificaProfiloController.updatePassword(oldPassword, newPassword, newPassword2);

                if(result) activity.finish();
            }
        });
    }
}