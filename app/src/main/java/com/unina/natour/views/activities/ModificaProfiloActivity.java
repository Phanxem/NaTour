package com.unina.natour.views.activities;

import androidx.annotation.RequiresApi;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.unina.natour.R;
//TODO da aggiustare (capire perché da errore se si importa solo la classe interessata)
import com.unina.natour.controllers.*;
import com.unina.natour.controllers.utils.TimeUtils;
import com.unina.natour.models.ProfiloPersonaleModel;

import org.w3c.dom.Text;

@RequiresApi(api = Build.VERSION_CODES.P)
public class ModificaProfiloActivity extends NaTourActivity {

    public ModificaProfiloController modificaProfiloController;
    public ProfiloPersonaleController profiloPersonaleController;

    public ProfiloPersonaleModel profiloPersonaleModel;


    // username

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifica_profilo);

        modificaProfiloController = new ModificaProfiloController(this);

        profiloPersonaleController = new ProfiloPersonaleController(this);
        profiloPersonaleModel = profiloPersonaleController.getModel();
        addModel(profiloPersonaleModel);
        profiloPersonaleModel.registerObserver(this);

        profiloPersonaleController.initModel();

        pressIconBack();
        pressTextUpdateImage();
        pressTextUpdateOptionalInfo();
        pressTextUpdatePassword();

        update();
    }

    public void pressIconBack(){
        ImageView imageView_iconBack = findViewById(R.id.ModificaProfilo_imageView_iconBack);
        imageView_iconBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public void pressTextUpdateImage(){
        NaTourActivity activity = this;

        TextView textView_updateImage = findViewById(R.id.ModificaProfilo_textView_modificaImmagineProfilo);
        textView_updateImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImmagineProfiloController.openPersonalizzaAccountImmagineActivity(activity);
            }
        });
    }

    public void pressTextUpdateOptionalInfo(){
        NaTourActivity activity = this;

        TextView textView_updateOptionalInfo = findViewById(R.id.ModificaProfilo_textView_modificaOptionalInfo);
        textView_updateOptionalInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InfoOpzionaliProfiloController.openPersonalizzaAccountInfoOpzionaliActivity(activity);
            }
        });
    }

    public void pressTextUpdatePassword(){
        NaTourActivity activity = this;

        TextView textView_updatePassword = findViewById(R.id.ModificaProfilo_textView_modificaPassword);
        textView_updatePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ModificaPasswordController.openModificaPasswordActivity(activity);
            }
        });
    }

    public void pressButtonLinkFacebookAccount(){
        Button button_facebook = findViewById(R.id.ModificaProfilo_button_facebook);
        button_facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modificaProfiloController.linkFacebookAccount();
            }
        });
    }

    public void pressButtonLinkGoogleAccount(){
        Button button_google = findViewById(R.id.ModificaProfilo_button_google);
        button_google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modificaProfiloController.linkGoogleAccount();
            }
        });
    }

    public void update(){

        if(profiloPersonaleModel.getProfileImage() != null) {
            ImageView imageView_profileImage = findViewById(R.id.ModificaProfilo_imageView_immagineProfilo);
            imageView_profileImage.setImageBitmap(profiloPersonaleModel.getProfileImage());
        }

        TextView textView_dateOfBirth = findViewById(R.id.ModificaProfilo_textView_dataNascita);
        String dateOfBirth = profiloPersonaleModel.getDateOfBirth();
        if(dateOfBirth != null || dateOfBirth.isEmpty()) textView_dateOfBirth.setText(TimeUtils.getDateWithoutHours(dateOfBirth));
        else textView_dateOfBirth.setText(getString(R.string.ModificaProfilo_textView_dataNascita_null));

        TextView textView_placeOfResidence = findViewById(R.id.ModificaProfilo_textView_luogoResidenza);
        String placeOfResidence = profiloPersonaleModel.getPlaceOfResidence();
        if(placeOfResidence != null || placeOfResidence.isEmpty()) textView_placeOfResidence.setText(profiloPersonaleModel.getPlaceOfResidence());
        else textView_placeOfResidence.setText(getString(R.string.ModificaProfilo_textView_luogoResidenza_null));

        TextView textView_email = findViewById(R.id.ModificaProfilo_textView_email);
        textView_email.setText(profiloPersonaleModel.getEmail());

        //TODO definire una funzione nel controller che verifica se l'utente si è registrato via
        //facebook o google, in questi casi entrambi i tasti vengono disabilitati

        Button button_linkFacebook = findViewById(R.id.ModificaProfilo_button_facebook);
        if(profiloPersonaleModel.isFacebookLinked()) button_linkFacebook.setEnabled(false);
        else button_linkFacebook.setEnabled(true);

        Button button_linkGoogle = findViewById(R.id.ModificaProfilo_button_google);
        if(profiloPersonaleModel.isGoogleLinked()) button_linkGoogle.setEnabled(false);
        else button_linkGoogle.setEnabled(true);
    }

    @Override
    protected void onResume() {
        profiloPersonaleController.initModel();
        super.onResume();
    }

}