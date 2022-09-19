package com.unina.natour.controllers;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.unina.natour.R;
import com.unina.natour.controllers.utils.StringsUtils;
import com.unina.natour.controllers.exceptionHandler.exceptions.ServerException;
import com.unina.natour.controllers.exceptionHandler.exceptions.subAppException.FailureGetUserException;
import com.unina.natour.controllers.exceptionHandler.exceptions.subAppException.NotCompletedGetUserException;
import com.unina.natour.dto.response.EmailResponseDTO;
import com.unina.natour.dto.response.MessageResponseDTO;
import com.unina.natour.dto.response.UserResponseDTO;
import com.unina.natour.models.ProfiloPersonaleModel;
import com.unina.natour.models.dao.implementation.AmplifyDAO;
import com.unina.natour.models.dao.implementation.UserDAOImpl;
import com.unina.natour.models.dao.interfaces.UserDAO;
import com.unina.natour.views.activities.NaTourActivity;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
@RequiresApi(api = Build.VERSION_CODES.N)
public class ProfiloPersonaleController extends NaTourController {

    private ListaItinerariController listaItinerariController;

    private ProfiloPersonaleModel profiloPersonaleModel;

    private AmplifyDAO amplifyDAO;
    private UserDAO userDAO;

    public ProfiloPersonaleController(NaTourActivity activity) {
        super(activity);

        //TODO this.username = Amplify.Auth.getCurrentUser().getUsername();
        String username = "user";

        this.amplifyDAO = new AmplifyDAO();
        this.userDAO = new UserDAOImpl(activity);

        this.listaItinerariController = new ListaItinerariController(activity, ListaItinerariController.CODE_ITINERARY_BY_USERNAME, username);

        this.profiloPersonaleModel = new ProfiloPersonaleModel();
        //initModel();
    }



    public void initModel() {
        EmailResponseDTO emailResponseDTO = amplifyDAO.getEmail();

        MessageResponseDTO messageResponseDTO = emailResponseDTO.getResultMessage();
        if(messageResponseDTO.getCode() != MessageController.SUCCESS_CODE){
            showErrorMessage(messageResponseDTO);
            return;
        }
        String email = emailResponseDTO.getEmail();


        //TODO this.username = Amplify.Auth.getCurrentUser().getUsername();
        String username = "user";
        UserResponseDTO userResponseDTO = userDAO.getUser(username);
        messageResponseDTO = userResponseDTO.getResultMessage();
        if(messageResponseDTO.getCode() != MessageController.SUCCESS_CODE){
            showErrorMessage(messageResponseDTO);
            return;
        }

        boolean result = dtoToModel(getActivity(), userResponseDTO, emailResponseDTO, profiloPersonaleModel);
    }


    public ProfiloPersonaleModel getModel() {
        return profiloPersonaleModel;
    }

    public void setProfiloPersonaleModel(ProfiloPersonaleModel profiloPersonaleModel) {
        this.profiloPersonaleModel = profiloPersonaleModel;
    }

    public ListaItinerariController getListaItinerariController() {
        return listaItinerariController;
    }

    public void setListaItinerariController(ListaItinerariController listaItinerariController) {
        this.listaItinerariController = listaItinerariController;
    }

    public static boolean dtoToModel(Context context, UserResponseDTO userDto, EmailResponseDTO emailDto, ProfiloPersonaleModel model){
        model.clear();

        model.setEmail(emailDto.getEmail());


        model.setId(userDto.getId());
        model.setUsername(userDto.getUsername());
        model.setPlaceOfResidence(userDto.getPlaceOfResidence());
        model.setDateOfBirth(userDto.getDateOfBirth());

        if(userDto.getProfileImage() != null) model.setProfileImage(userDto.getProfileImage());
        else {
            Bitmap genericProfileImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_generic_account);
            model.setProfileImage(genericProfileImage);
        }

        model.setFacebookLinked(userDto.isFacebookLinked());
        model.setGoogleLinked(userDto.isGoogleLinked());

        return true;
    }
}
