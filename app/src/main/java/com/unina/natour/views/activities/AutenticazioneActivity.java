package com.unina.natour.views.activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.facebook.Profile;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.common.SignInButton;
import com.unina.natour.R;
import com.unina.natour.controllers.AutenticazioneController;
import com.unina.natour.controllers.HomeController;
import com.unina.natour.controllers.MainController;
import com.unina.natour.controllers.RecuperoPasswordController;
import com.unina.natour.controllers.RegistrazioneController;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

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

        pressButtonSignInGuest();

        initButtonFacebook();
        initButtonGoogle();
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

    public void pressButtonSignInGuest(){
        NaTourActivity activity = this;

        Button button_signInGuest = findViewById(R.id.SignIn_button_signInGuest);
        button_signInGuest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeController.openHomeGuestActivity(activity);
            }
        });
    }

    private void initButtonFacebook() {

        LoginButton facebookLoginButton = findViewById(R.id.SignIn_loginButton_loginFacebook);

        autenticazioneController.initButtonFacebook(facebookLoginButton);

    }

    public void initButtonGoogle(){
        AutenticazioneActivity activity = this;
        ActivityResultLauncher<Intent> activityResultLauncherGoogleLogin;
        activityResultLauncherGoogleLogin = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>()
                {
                    @Override
                    public void onActivityResult(ActivityResult result) {

                        if(result == null || result.getData() == null) return;

                        activity.onActivityResult(AutenticazioneController.GOOGLE_LOGIN_CODE, result.getResultCode(), result.getData());
                    }
                }
        );

        SignInButton googleLoginButton = findViewById(R.id.SignIn_signInButton_loginGoogle);

        autenticazioneController.initButtonGoogle(googleLoginButton, activityResultLauncherGoogleLogin);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == AutenticazioneController.GOOGLE_LOGIN_CODE){
            //google
            autenticazioneController.callbackGoogle(data);
        }
        else{

            //facebook
            autenticazioneController.callbackFacebook(requestCode,resultCode,data);

            ExecutorService executorService = Executors.newSingleThreadExecutor();

            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    ExecutorService executorService = Executors.newSingleThreadExecutor();
                    executorService.execute(new Runnable() {
                        @Override
                        public void run() {
                            while(Profile.getCurrentProfile() == null){}
                        }
                    });

                    executorService.shutdown();
                    try {
                        executorService.awaitTermination(1,TimeUnit.MINUTES);
                    }
                    catch (InterruptedException e) {
                        return;
                    }
                    autenticazioneController.completeSignInWithFacebook();
                }
            });


        }
    }

    @Override
    protected void onResume() {
        //initButtonFacebook();
        super.onResume();
    }
}