package com.unina.natour.views.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.unina.natour.R;
import com.unina.natour.controllers.RegistrazioneController;

public class RegistrazioneActivity extends AppCompatActivity {

    private final static String TAG ="RegistrazioneActivity";
    private RegistrazioneController registrazioneController;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrazione);

        registrazioneController = new RegistrazioneController(this);
    }

    public void pressButtonSignUp(View view) {
        Button button_signUp = findViewById(R.id.SignIn_button_signIn);
        button_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText textField_username = findViewById(R.id.SignUp_textField_username);
                String username = String.valueOf(textField_username.getText());

                EditText textField_password = findViewById(R.id.SignUp_passwordField_password);
                String password = String.valueOf(textField_password.getText());

                EditText textField_email = findViewById(R.id.SignUp_emailField_email);
                String email = String.valueOf(textField_email.getText());


                registrazioneController.signUp(username,email,password);
            }
        });
    }

    public void pressIconBack(View view) {
        registrazioneController.back();
    }



/*
    public void showPasswordPolicy(){

        final String PASSWORD_POLICY = "\nUna password valida deve:\n - Essere lunga almeno 8 caratteri\n - Deve contentere almeno un numero\n - Deve contenere almeno un carattere speciale\n - Deve contenere almeno una lettera maiuscola\n - Deve contentere almeno una lettera minuscola";

        //textView_passwordPolicy.setLayoutParams(new ViewGroup.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT));
        textView_passwordPolicy.setVisibility(View.VISIBLE);
        textView_passwordPolicy.setText(PASSWORD_POLICY);
    }

 */
}