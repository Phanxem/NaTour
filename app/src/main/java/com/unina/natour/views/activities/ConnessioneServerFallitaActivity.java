package com.unina.natour.views.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.unina.natour.R;
import com.unina.natour.controllers.ImmagineProfiloController;
import com.unina.natour.controllers.SplashScreenController;

public class ConnessioneServerFallitaActivity extends NaTourActivity {

    private SplashScreenController splashScreenController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connessione_server_fallita);

        this.splashScreenController = new SplashScreenController(this);

        pressIconRetry();
    }

    public void pressIconRetry(){
        ImageView imageView_iconRetry = findViewById(R.id.ServerConnectionFailed_imageView_immagineRetry);
        imageView_iconRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                splashScreenController.redirectToRightActivity();
            }
        });
    }

    public static void openConnessioneServerFallitaActivity(NaTourActivity fromActivity){
        Intent intent = new Intent(fromActivity, ConnessioneServerFallitaActivity.class);
        fromActivity.startActivity(intent);
        fromActivity.finish();
    }
}