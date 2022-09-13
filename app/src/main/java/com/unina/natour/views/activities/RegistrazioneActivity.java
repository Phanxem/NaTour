package com.unina.natour.views.activities;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.unina.natour.R;
import com.unina.natour.controllers.AttivaAccountController;
import com.unina.natour.controllers.RegistrazioneController;
import com.unina.natour.views.dialogs.MessageDialog;

@RequiresApi(api = Build.VERSION_CODES.N)
public class RegistrazioneActivity extends NaTourActivity {

    private RegistrazioneController registrazioneController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrazione);

        registrazioneController = new RegistrazioneController(this);

        pressButtonSignUp();
        pressIconBack();
    }

    public void pressButtonSignUp() {
        NaTourActivity activity = this;

        Button button_signUp = findViewById(R.id.SignUp_button_signUp);
        button_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText textField_username = findViewById(R.id.SignUp_textField_username);
                String username = String.valueOf(textField_username.getText());

                EditText textField_password = findViewById(R.id.SignUp_passwordField_password);
                String password = String.valueOf(textField_password.getText());

                EditText textField_email = findViewById(R.id.SignUp_emailField_email);
                String email = String.valueOf(textField_email.getText());


                Boolean result = registrazioneController.signUp(username,email,password);

                if(result) AttivaAccountController.openAttivaAccountActivity(activity, username, password);
            }
        });
    }

    public void pressIconBack() {
        ImageView icon_back = findViewById(R.id.SignUp_imageView_iconaIndietro);
        icon_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrazioneController.back();
            }
        });
    }

}