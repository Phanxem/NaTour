package com.unina.natour.views.activities;

import androidx.annotation.RequiresApi;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.facebook.login.widget.LoginButton;
import com.unina.natour.R;
import com.unina.natour.controllers.AutenticazioneController;
import com.unina.natour.controllers.MainController;
import com.unina.natour.controllers.RecuperoPasswordController;
import com.unina.natour.controllers.RegistrazioneController;

@RequiresApi(api = Build.VERSION_CODES.N)
public class AutenticazioneActivity extends NaTourActivity {

    private AutenticazioneController autenticazioneController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_autenticazione);

        autenticazioneController = new AutenticazioneController(this);


        pressButtonSignIn();
        pressTextSignUp();
        pressTextPasswordRecovery();

        pressButtonFB();
    }

    private void pressButtonFB() {

        LoginButton loginButton = findViewById(R.id.SignIn_loginButton_loginFacebook);

        autenticazioneController.initButtonFB(loginButton);

    }


    public void pressButtonSignIn() {
        NaTourActivity activity = this;

        Button button_signIn = findViewById(R.id.SignIn_button_signIn);
        button_signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText textField_usernameEmail = findViewById(R.id.SignIn_textField_usernameEmail);
                String usernameEmail = String.valueOf(textField_usernameEmail.getText());

                EditText textField_password = findViewById(R.id.SignIn_passwordField_password);
                String password = String.valueOf(textField_password.getText());

                Boolean result = autenticazioneController.signIn(usernameEmail, password);

                if(result) MainController.openMainActivity(activity);
            }
        });
    }

    public void pressTextSignUp() {
        NaTourActivity activity = this;

        TextView textView_signUp = findViewById(R.id.SignIn_textView_registrati);
        textView_signUp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                RegistrazioneController.openRegistrazioneActivity(activity);
            }
        });
    }

    public void pressTextPasswordRecovery() {
        NaTourActivity activity = this;

        TextView textView_passwordRecovery = findViewById(R.id.SignIn_textView_recuperaPassword);
        textView_passwordRecovery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecuperoPasswordController.openIniziaRecuperoPasswordActivity(activity);
            }
        });
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        autenticazioneController.callbackFB(requestCode,resultCode,data);
        super.onActivityResult(requestCode, resultCode, data);
    }

}