package com.unina.natour.views.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.unina.natour.R;
import com.unina.natour.controllers.AutenticazioneController;
import com.unina.natour.controllers.ProfiloPersonaleController;

import java.io.InputStream;
import java.io.InputStreamReader;

public class ProfiloPersonaleActivity extends AppCompatActivity {

    private final static String TAG ="ProfiloPersonaleActivity";

    private ProfiloPersonaleController profiloPersonaleController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profilo_personale);
    }

    public void pressButtonTest() {
        Button button_test = findViewById(R.id.ProfiloPersonale_button);
        button_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //profiloPersonaleController.callApiTest();
            }
        });
    }
}