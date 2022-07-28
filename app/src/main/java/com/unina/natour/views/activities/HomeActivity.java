package com.unina.natour.views.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.unina.natour.R;
import com.unina.natour.controllers.AutenticazioneController;
import com.unina.natour.controllers.DisconnessioneController;
import com.unina.natour.controllers.HomeController;

public class HomeActivity extends AppCompatActivity {

    private final static String TAG ="HomeActivity";

    private HomeController homeController;

    private DisconnessioneController disconnessioneController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        homeController = new HomeController(this);
        disconnessioneController = new DisconnessioneController(this);

        pressButtonSignOut();
    }

    public void pressButtonSignOut() {
        Button button_signOut = findViewById(R.id.Home_button_test);
        button_signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                disconnessioneController.signOut();
            }
        });
    }
}