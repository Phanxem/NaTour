package com.unina.natour.views.activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.unina.natour.R;
import com.unina.natour.controllers.ImpostaImmagineProfiloController;

public class PersonalizzaAccountImmagineActivity extends AppCompatActivity {

    private final static String TAG ="PersonalizzaAccountImmagineActivity";

    private ImpostaImmagineProfiloController impostaImmagineProfiloController;

    private ActivityResultLauncher<Intent> startForResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personalizza_account_immagine);

        impostaImmagineProfiloController = new ImpostaImmagineProfiloController(this);
        startForResult = startForResult();

        pressTextSetProfileImage();
        pressButtonNext();
    }

    public ActivityResultLauncher<Intent> startForResult(){
        ImageView imageView_profileImage = findViewById(R.id.PersonalizzaAccount_imageView_immagine);

        ActivityResultLauncher<Intent> startForResult = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        Bitmap bitmap = impostaImmagineProfiloController.getProfileImage(result);
                        imageView_profileImage.setImageBitmap(bitmap);
                    }
                }
        );
        return startForResult;
    }



    public void pressTextSetProfileImage(){
        TextView textView_setProfileImage = findViewById(R.id.PersonalizzaAccount_textView_selezionaImmagine);
        textView_setProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                impostaImmagineProfiloController.openGallery(startForResult);
            }
        });
    }

    public void pressButtonNext(){
        Button button_next = findViewById(R.id.PersonalizzaAccount_button_avanti);
        button_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                impostaImmagineProfiloController.modificaImmagineProfilo();

                //open ACTIVIRY

            }
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == ImpostaImmagineProfiloController.REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.i(TAG , "Calling Permission is granted");

                impostaImmagineProfiloController.openGallery(startForResult);
            }
            else {
                //TODO ERRORE
                Log.i(TAG, "Calling Permission is denied");
            }
        }
    }


}