package com.unina.natour.views.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.unina.natour.R;
import com.unina.natour.controllers.AutenticazioneController;
import com.unina.natour.controllers.HomeController;
import com.unina.natour.controllers.RecuperoPasswordController;
import com.unina.natour.controllers.RegistrazioneController;
import com.unina.natour.controllers.SplashScreenController;

public class AutenticazioneActivity extends AppCompatActivity {

    private final static String TAG ="AutenticazioneActivity";

    private AutenticazioneController autenticazioneController;
    private RegistrazioneController registrazioneController;
    private RecuperoPasswordController recuperoPasswordController;
    private HomeController homeController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_autenticazione);

        autenticazioneController = new AutenticazioneController(this);
        registrazioneController = new RegistrazioneController(this);
        recuperoPasswordController = new RecuperoPasswordController(this);
        homeController = new HomeController(this);

        pressButtonSignIn();
        pressTextSignUp();
        pressTextPasswordRecovery();
    }

    public void pressButtonSignIn() {
        Button button_signIn = findViewById(R.id.SignIn_button_signIn);
        button_signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText textField_usernameEmail = findViewById(R.id.SignIn_textField_usernameEmail);
                String usernameEmail = String.valueOf(textField_usernameEmail.getText());

                EditText textField_password = findViewById(R.id.SignIn_passwordField_password);
                String password = String.valueOf(textField_password.getText());

                Boolean result = autenticazioneController.signIn(usernameEmail, password);

                if(result) homeController.openHomeActivity();
            }
        });
    }

    public void pressTextSignUp() {
        TextView textView_signUp = findViewById(R.id.SignIn_textView_registrati);
        textView_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrazioneController.openRegistrazioneActivity();
            }
        });
    }

    public void pressTextPasswordRecovery() {
        TextView textView_passwordRecovery = findViewById(R.id.SignIn_textView_recuperaPassword);
        textView_passwordRecovery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recuperoPasswordController.openIniziaRecuperoPasswordActivity();
            }
        });
    }
}