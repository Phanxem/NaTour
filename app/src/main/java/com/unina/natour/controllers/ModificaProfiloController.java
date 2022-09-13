package com.unina.natour.controllers;

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
import com.unina.natour.views.activities.NaTourActivity;
import com.unina.natour.views.dialogs.MessageDialog;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

@RequiresApi(api = Build.VERSION_CODES.P)
public class ModificaProfiloController extends NaTourController{

    InfoOpzionaliProfiloController infoOpzionaliProfiloController;
    ImmagineProfiloController immagineProfiloController;

    ModificaProfiloModel modificaProfiloModel;

    UserDAO userDAO;



    public ModificaProfiloController(NaTourActivity activity) {
        super(activity);
/*
        this.infoOpzionaliProfiloController = new InfoOpzionaliProfiloController(activity,messageDialog);
        this.immagineProfiloController = new ImmagineProfiloController(activity,messageDialog);



        ImpostaImmagineProfiloModel profileImageModel = immagineProfiloController.getImpostaImmagineProfiloModel();
        ImpostaInfoOpzionaliProfiloModel optionalInfoModel = infoOpzionaliProfiloController.getImpostaInfoOpzionaliProfiloModel();

        this.modificaProfiloModel = new ModificaProfiloModel(optionalInfoModel, profileImageModel);


        this.userDAO = new UserDAOImpl(activity);

        //String usename Amplify...
        String username = "test";

        UserDTO userDTO = null;
        try {
            userDTO = userDAO.getUser(username);
        }
        catch (ExecutionException | InterruptedException e) {
            //todo
            e.printStackTrace();
        }
        catch (ServerException exception) {
            //todo
            exception.printStackTrace();
        }
        catch (IOException e) {
            //todo
            e.printStackTrace();
        }

        Bitmap profileImage = null;
        try {
            profileImage = userDAO.getUserProfileImage(username);
        }
        catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        catch (ServerException exception) {
            exception.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }



        modificaProfiloModel.setByDTO(userDTO){

        }

        modificaProfiloModel.setProfileImage(profileImage){

        }
        */

    }


/*
    private ModificaProfiloModel toModel(UserDTO userDTO) {


    }
*/

}
