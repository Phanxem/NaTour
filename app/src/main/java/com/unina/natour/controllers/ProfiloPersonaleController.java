package com.unina.natour.controllers;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentActivity;

import com.amplifyframework.auth.AuthUserAttribute;
import com.amplifyframework.core.Amplify;
import com.unina.natour.controllers.exceptionHandler.ExceptionHandler;
import com.unina.natour.controllers.exceptionHandler.exceptions.ServerException;
import com.unina.natour.controllers.exceptionHandler.exceptions.subAppException.FailureGetUserException;
import com.unina.natour.controllers.exceptionHandler.exceptions.subAppException.FailureGetUserProfileImageException;
import com.unina.natour.controllers.exceptionHandler.exceptions.subAppException.NotCompletedFindAddressException;
import com.unina.natour.controllers.exceptionHandler.exceptions.subAppException.NotCompletedGetUserException;
import com.unina.natour.controllers.exceptionHandler.exceptions.subAppException.NotCompletedGetUserProfileImageException;
import com.unina.natour.dto.UserDTO;
import com.unina.natour.models.ProfiloPersonaleModel;
import com.unina.natour.models.dao.implementation.UserDAOImpl;
import com.unina.natour.models.dao.interfaces.UserDAO;
import com.unina.natour.views.activities.NaTourActivity;
import com.unina.natour.views.dialogs.MessageDialog;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
@RequiresApi(api = Build.VERSION_CODES.N)
public class ProfiloPersonaleController extends NaTourController {

    ListaItinerariController listaItinerariController;

    ProfiloPersonaleModel profiloPersonaleModel;

    UserDAO userDAO;

    public ProfiloPersonaleController(NaTourActivity activity) {
        super(activity);

        //TODO this.username = Amplify.Auth.getCurrentUser().getUsername();
        String username = "user";

        this.userDAO = new UserDAOImpl(activity);

        this.listaItinerariController = new ListaItinerariController(activity, username);

        this.profiloPersonaleModel = new ProfiloPersonaleModel();
        //initModel();
    }



    public void initModel() {
        CompletableFuture<String> completableFuture = new CompletableFuture<String>();

        Amplify.Auth.fetchUserAttributes(
                attributes -> {
                    for (AuthUserAttribute attribute : attributes) {
                        if (attribute.getKey().getKeyString().equals("email")) {
                            completableFuture.complete(attribute.getValue());
                        }
                    }
                },
                error -> {
                    ExceptionHandler.handleMessageError(getMessageDialog(), error);
                    completableFuture.complete(null);
                }
        );

        String email = null;
        try {
            email = completableFuture.get();
        } catch (ExecutionException | InterruptedException e) {
            NotCompletedFindAddressException exception = new NotCompletedFindAddressException(e);
            ExceptionHandler.handleMessageError(getMessageDialog(), exception);
            return;
        }
        if (email == null) return;


        UserDTO userDTO = null;

        //TODO this.username = Amplify.Auth.getCurrentUser().getUsername();
        String username = "user";

        try {
            userDTO = userDAO.getUser(username);
        } catch (ExecutionException | InterruptedException e) {
            NotCompletedGetUserException exception = new NotCompletedGetUserException(e);
            ExceptionHandler.handleMessageError(getMessageDialog(), exception);
            return;
        } catch (ServerException e) {
            ExceptionHandler.handleMessageError(getMessageDialog(), e);
            return;
        } catch (IOException e) {
            FailureGetUserException exception = new FailureGetUserException(e);
            ExceptionHandler.handleMessageError(getMessageDialog(), exception);
            return;
        }


        Bitmap profileImage = null;
        try {
            profileImage = userDAO.getUserProfileImage(username);
        } catch (ExecutionException | InterruptedException e) {
            NotCompletedGetUserProfileImageException exception = new NotCompletedGetUserProfileImageException(e);
            ExceptionHandler.handleMessageError(getMessageDialog(), exception);
            return;
        } catch (ServerException e) {
            ExceptionHandler.handleMessageError(getMessageDialog(), e);
            return;
        } catch (IOException e) {
            FailureGetUserProfileImageException exception = new FailureGetUserProfileImageException(e);
            ExceptionHandler.handleMessageError(getMessageDialog(), exception);
            return;
        }


        profiloPersonaleModel.setEmail(email);


        profiloPersonaleModel.setId(userDTO.getId());
        profiloPersonaleModel.setUsername(userDTO.getUsername());
        profiloPersonaleModel.setPlaceOfResidence(userDTO.getPlaceOfResidence());
        profiloPersonaleModel.setDateOfBirth(userDTO.getDateOfBirth());

        profiloPersonaleModel.setFacebookLinked(userDTO.isFacebookLinked());
        profiloPersonaleModel.setGoogleLinked(userDTO.isGoogleLinked());

        profiloPersonaleModel.setProfileImage(profileImage);

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
}
