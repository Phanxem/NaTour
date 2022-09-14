package com.unina.natour.controllers;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentActivity;

import com.unina.natour.controllers.exceptionHandler.exceptions.ServerException;
import com.unina.natour.dto.UserDTO;
import com.unina.natour.models.ImpostaImmagineProfiloModel;
import com.unina.natour.models.ImpostaInfoOpzionaliProfiloModel;
import com.unina.natour.models.ModificaProfiloModel;
import com.unina.natour.models.dao.implementation.UserDAOImpl;
import com.unina.natour.models.dao.interfaces.UserDAO;
import com.unina.natour.views.activities.ModificaPasswordActivity;
import com.unina.natour.views.activities.ModificaProfiloActivity;
import com.unina.natour.views.activities.NaTourActivity;
import com.unina.natour.views.activities.PersonalizzaAccountImmagineActivity;
import com.unina.natour.views.dialogs.MessageDialog;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

@RequiresApi(api = Build.VERSION_CODES.P)
public class ModificaProfiloController extends NaTourController{

    public ModificaProfiloController(NaTourActivity activity) {
        super(activity);
    }

    public static void openModificaProfiloActivity(NaTourActivity fromActivity){
        Intent intent = new Intent(fromActivity, ModificaProfiloActivity.class);
        fromActivity.startActivity(intent);
    }

    public static void openModificaPasswordActivity(NaTourActivity fromActivity){
        Intent intent = new Intent(fromActivity, ModificaPasswordActivity.class);
        fromActivity.startActivity(intent);
    }

    public static void linkFacebookAccount(){
        //TODO
    }

    public static void linkGoogleAccount(){
        //TODO
    }


}
