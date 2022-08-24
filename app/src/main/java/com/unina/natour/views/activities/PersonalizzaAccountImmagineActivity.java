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
import com.unina.natour.views.dialogs.MessageDialog;

@RequiresApi(api = Build.VERSION_CODES.N)
@SuppressLint("LongLogTag")
public class PersonalizzaAccountImmagineActivity extends AppCompatActivity {

    private final static String TAG ="PersonalizzaAccountImmagineActivity";

    private ImpostaImmagineProfiloController impostaImmagineProfiloController;
    private ImpostaInfoOpzionaliProfiloController impostaInfoOpzionaliProfiloController;

    private ActivityResultLauncher<Intent> startForResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personalizza_account_immagine);

        MessageDialog messageDialog = new MessageDialog();
        messageDialog.setSupportFragmentManager(getSupportFragmentManager());

        impostaImmagineProfiloController = new ImpostaImmagineProfiloController(this, messageDialog);
        impostaInfoOpzionaliProfiloController = new ImpostaInfoOpzionaliProfiloController(this, messageDialog);

        ImageView imageView_profileImage = findViewById(R.id.PersonalizzaAccount_imageView_immagine);
        startForResult = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @RequiresApi(api = Build.VERSION_CODES.P)
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        Bitmap bitmap = impostaImmagineProfiloController.getProfileImage(result);
                        if(bitmap != null) imageView_profileImage.setImageBitmap(bitmap);
                    }
                }
        );

        pressTextSetProfileImage();
        pressButtonNext();
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
                Boolean result = impostaImmagineProfiloController.modificaImmagineProfilo();

                if(result) impostaInfoOpzionaliProfiloController.openPersonalizzaAccountInfoOpzionaliActivity(true);
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