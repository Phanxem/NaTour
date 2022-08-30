package com.unina.natour.views.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.unina.natour.R;
import com.unina.natour.controllers.ImpostaImmagineProfiloController;
import com.unina.natour.controllers.*;
import com.unina.natour.models.ImpostaImmagineProfiloModel;
import com.unina.natour.views.dialogs.MessageDialog;
import com.unina.natour.views.observers.Observer;

@RequiresApi(api = Build.VERSION_CODES.P)
@SuppressLint("LongLogTag")
public class PersonalizzaAccountImmagineActivity extends AppCompatActivity implements Observer {

    private final static String TAG ="PersonalizzaAccountImmagineActivity";

    private ImpostaImmagineProfiloController impostaImmagineProfiloController;
    private ImpostaInfoOpzionaliProfiloController impostaInfoOpzionaliProfiloController;

    private ImpostaImmagineProfiloModel impostaImmagineProfiloModel;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personalizza_account_immagine);

        MessageDialog messageDialog = new MessageDialog();
        messageDialog.setFragmentActivity(this);

        impostaImmagineProfiloController = new ImpostaImmagineProfiloController(this, messageDialog);
        impostaImmagineProfiloModel = impostaImmagineProfiloController.getImpostaImmagineProfiloModel();
        impostaImmagineProfiloModel.registerObserver(this);

        impostaInfoOpzionaliProfiloController = new ImpostaInfoOpzionaliProfiloController(this, messageDialog);

        pressTextSetProfileImage();
        pressButtonNext();
    }

    public void pressTextSetProfileImage(){
        TextView textView_setProfileImage = findViewById(R.id.PersonalizzaAccount_textView_selezionaImmagine);
        textView_setProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                impostaImmagineProfiloController.openGallery();
            }
        });
    }

    public void pressButtonNext(){
        Button button_next = findViewById(R.id.PersonalizzaAccount_button_avanti);
        button_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean result = impostaImmagineProfiloController.modificaImmagineProfilo();

                if(result) impostaInfoOpzionaliProfiloController.openPersonalizzaAccountInfoOpzionaliActivity(true);
            }
        });
    }

    @Override
    public void update() {
        ImageView imageView_immagineProfilo = findViewById(R.id.PersonalizzaAccount_imageView_immagine);
        Bitmap immagineProfilo = impostaImmagineProfiloModel.getProfileImage();
        imageView_immagineProfilo.setImageBitmap(immagineProfilo);
    }
}