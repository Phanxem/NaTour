package com.unina.natour.views.activities;

import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.unina.natour.R;
//TODO da aggiustare (capire perché da errore se si importa solo la classe interessata)
import com.unina.natour.config.CurrentUserInfo;
import com.unina.natour.controllers.*;
import com.unina.natour.controllers.utils.TimeUtils;
import com.unina.natour.models.ProfiloModel;


public class ModificaProfiloActivity extends NaTourActivity {

    private ModificaProfiloController modificaProfiloController;
    private ProfiloController profiloController;

    private ProfiloModel profiloModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifica_profilo);

        modificaProfiloController = new ModificaProfiloController(this);

        profiloController = new ProfiloController(this, CurrentUserInfo.getId());
        profiloModel = profiloController.getModel();
        addModel(profiloModel);
        profiloModel.registerObserver(this);

        pressIconBack();
        pressTextUpdateImage();
        pressTextUpdateOptionalInfo();
        pressTextUpdatePassword();
    }

    @Override
    protected void onResume() {
        profiloController.initModel(CurrentUserInfo.getId());
        update();
        super.onResume();
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

    public void update(){

        if(profiloModel.getProfileImage() != null) {
            ImageView imageView_profileImage = findViewById(R.id.ModificaProfilo_imageView_immagineProfilo);
            imageView_profileImage.setImageBitmap(profiloModel.getProfileImage());
        }

        TextView textView_dateOfBirth = findViewById(R.id.ModificaProfilo_textView_dataNascita);
        String dateOfBirth = profiloModel.getDateOfBirth();
        if(dateOfBirth != null && !dateOfBirth.isEmpty()) textView_dateOfBirth.setText(TimeUtils.getDateWithoutHours(dateOfBirth));
        else textView_dateOfBirth.setText(getString(R.string.ModificaProfilo_textView_dataNascita_null));

        TextView textView_placeOfResidence = findViewById(R.id.ModificaProfilo_textView_luogoResidenza);
        String placeOfResidence = profiloModel.getPlaceOfResidence();
        if(placeOfResidence != null && !placeOfResidence.isEmpty()) textView_placeOfResidence.setText(profiloModel.getPlaceOfResidence());
        else textView_placeOfResidence.setText(getString(R.string.ModificaProfilo_textView_luogoResidenza_null));

        String currentIdentityProvider = CurrentUserInfo.getIdentityProvider();
        String cognitoIdentityProvider = this.getString(R.string.IdentityProvider_Cognito);

        TextView textView_email = findViewById(R.id.ModificaProfilo_textView_email);
        ConstraintLayout constraintLayout_accountInfo = findViewById(R.id.ModificaProfilo_constraintLayout_account);
        if(currentIdentityProvider.equals(cognitoIdentityProvider)){
            textView_email.setText(profiloModel.getEmail());
            constraintLayout_accountInfo.setVisibility(View.VISIBLE);
        }
        else{
            textView_email.setText("");
            constraintLayout_accountInfo.setVisibility(View.GONE);
        }


    }


}