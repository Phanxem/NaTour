package com.unina.natour.views.activities;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.unina.natour.R;
import com.unina.natour.controllers.ImmagineProfiloController;
import com.unina.natour.controllers.InfoOpzionaliProfiloController;
import com.unina.natour.models.ImpostaImmagineProfiloModel;
import com.unina.natour.views.dialogs.MessageDialog;
import com.unina.natour.views.observers.Observer;

@RequiresApi(api = Build.VERSION_CODES.P)
@SuppressLint("LongLogTag")
public class PersonalizzaAccountImmagineActivity extends NaTourActivity {


    private ImmagineProfiloController immagineProfiloController;

    private ImpostaImmagineProfiloModel impostaImmagineProfiloModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personalizza_account_immagine);

        immagineProfiloController = new ImmagineProfiloController(this);

        impostaImmagineProfiloModel = immagineProfiloController.getImpostaImmagineProfiloModel();
        impostaImmagineProfiloModel.registerObserver(this);
        addModel(impostaImmagineProfiloModel);

        pressTextSetProfileImage();
        pressButtonNext();

        update();
    }

    public void pressTextSetProfileImage(){
        TextView textView_setProfileImage = findViewById(R.id.PersonalizzaAccount_textView_selezionaImmagine);
        textView_setProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                immagineProfiloController.openGallery();
            }
        });
    }

    public void pressButtonNext(){
        NaTourActivity activity = this;

        Button button_next = findViewById(R.id.PersonalizzaAccount_button_avanti);
        button_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean result = immagineProfiloController.modificaImmagineProfilo();

                if(result){
                    if(immagineProfiloController.isFirstUpdate()){
                        InfoOpzionaliProfiloController.openPersonalizzaAccountInfoOpzionaliActivity(activity, true);
                        return;
                    }
                    activity.finish();
                }


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