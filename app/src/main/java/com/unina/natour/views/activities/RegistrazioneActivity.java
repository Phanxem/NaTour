package com.unina.natour.views.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.unina.natour.R;
import com.unina.natour.controllers.AttivaAccountController;
import com.unina.natour.controllers.RegistrazioneController;

public class RegistrazioneActivity extends AppCompatActivity {

    private final static String TAG ="RegistrazioneActivity";

    private RegistrazioneController registrazioneController;
    private AttivaAccountController attivaAccountController;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrazione);

        registrazioneController = new RegistrazioneController(this);
        attivaAccountController = new AttivaAccountController(this);

        pressButtonSignUp();
        pressIconBack();
    }

    public void pressButtonSignUp() {
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

                if(result) attivaAccountController.openAttivaAccountActivity(username, password);
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



/*
    public void showPasswordPolicy(){

        final String PASSWORD_POLICY = "\nUna password valida deve:\n - Essere lunga almeno 8 caratteri\n - Deve contentere almeno un numero\n - Deve contenere almeno un carattere speciale\n - Deve contenere almeno una lettera maiuscola\n - Deve contentere almeno una lettera minuscola";

        //textView_passwordPolicy.setLayoutParams(new ViewGroup.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT));
        textView_passwordPolicy.setVisibility(View.VISIBLE);
        textView_passwordPolicy.setText(PASSWORD_POLICY);
    }

 */
}