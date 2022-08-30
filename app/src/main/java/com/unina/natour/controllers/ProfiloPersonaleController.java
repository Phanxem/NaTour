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
import com.unina.natour.views.dialogs.MessageDialog;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
@RequiresApi(api = Build.VERSION_CODES.N)
public class ProfiloPersonaleController implements Parcelable {

    private final static String TAG ="ProfiloPersonaleController";

    private final static String USERNAME = "user";

    FragmentActivity activity;
    MessageDialog messageDialog;

    ProfiloPersonaleModel profiloPersonaleModel;

    UserDAO userDAO;

    public ProfiloPersonaleController(FragmentActivity activity, MessageDialog messageDialog){
        this.activity = activity;
        this.messageDialog = messageDialog;

        this.userDAO = new UserDAOImpl(activity);

        this.profiloPersonaleModel = initModel();
    }

    public MessageDialog getMessageDialog() {
        return messageDialog;
    }

    private ProfiloPersonaleModel initModel(){

        CompletableFuture<String> completableFuture = new CompletableFuture<String>();

        Amplify.Auth.fetchUserAttributes(
                attributes -> {
                    for(AuthUserAttribute attribute : attributes){
                        if(attribute.getKey().getKeyString().equals("email")){
                            completableFuture.complete(attribute.getValue());
                        }
                    }
                },
                error -> {
                    ExceptionHandler.handleMessageError(messageDialog,error);
                    completableFuture.complete(null);
                }
        );

        String email = null;
        try {
            email = completableFuture.get();
        }
        catch (ExecutionException | InterruptedException e) {
            NotCompletedFindAddressException exception = new NotCompletedFindAddressException(e);
            ExceptionHandler.handleMessageError(messageDialog,exception);
            return new ProfiloPersonaleModel();
        }
        if(email == null) return new ProfiloPersonaleModel();


        UserDTO userDTO = null;

        try {
            userDTO = userDAO.getUser(USERNAME);
        }
        catch (ExecutionException | InterruptedException e) {
            NotCompletedGetUserException exception = new NotCompletedGetUserException(e);
            ExceptionHandler.handleMessageError(messageDialog,exception);
            return new ProfiloPersonaleModel();
        }
        catch (ServerException e) {
            ExceptionHandler.handleMessageError(messageDialog,e);
            return new ProfiloPersonaleModel();
        }
        catch (IOException e) {
            FailureGetUserException exception = new FailureGetUserException(e);
            ExceptionHandler.handleMessageError(messageDialog,exception);
            return new ProfiloPersonaleModel();
        }


        Bitmap profileImage = null;
        try {
            profileImage = userDAO.getUserProfileImage(USERNAME);
        }
        catch (ExecutionException | InterruptedException e) {
            NotCompletedGetUserProfileImageException exception = new NotCompletedGetUserProfileImageException(e);
            ExceptionHandler.handleMessageError(messageDialog,exception);
            return new ProfiloPersonaleModel();
        }
        catch (ServerException e) {
            ExceptionHandler.handleMessageError(messageDialog,e);
            return new ProfiloPersonaleModel();
        }
        catch (IOException e) {
            FailureGetUserProfileImageException exception = new FailureGetUserProfileImageException(e);
            ExceptionHandler.handleMessageError(messageDialog,exception);
            return new ProfiloPersonaleModel();
        }

        ProfiloPersonaleModel profiloPersonaleModel = new ProfiloPersonaleModel();


        profiloPersonaleModel.setEmail(email);


        profiloPersonaleModel.setId(userDTO.getId());
        profiloPersonaleModel.setUsername(userDTO.getUsername());
        profiloPersonaleModel.setPlaceOfResidence(userDTO.getPlaceOfResidence());
        profiloPersonaleModel.setDateOfBirth(userDTO.getDateOfBirth());

        profiloPersonaleModel.setProfileImage(profileImage);

        return profiloPersonaleModel;
    }

    public ProfiloPersonaleModel getProfiloPersonaleModel() {
        return profiloPersonaleModel;
    }

    public void setProfiloPersonaleModel(ProfiloPersonaleModel profiloPersonaleModel) {
        this.profiloPersonaleModel = profiloPersonaleModel;
    }







    protected ProfiloPersonaleController(Parcel in) { }

    public static final Creator<ProfiloPersonaleController> CREATOR = new Creator<ProfiloPersonaleController>() {
        @Override
        public ProfiloPersonaleController createFromParcel(Parcel in) {
            return new ProfiloPersonaleController(in);
        }

        @Override
        public ProfiloPersonaleController[] newArray(int size) {
            return new ProfiloPersonaleController[size];
        }
    };

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) { }
}
