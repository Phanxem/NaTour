package com.unina.natour.controllers;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentActivity;

import com.amplifyframework.auth.AuthUserAttribute;
import com.amplifyframework.core.Amplify;
import com.unina.natour.controllers.exceptionHandler.exceptions.ServerException;
import com.unina.natour.dto.UserDTO;
import com.unina.natour.models.ProfiloPersonaleModel;
import com.unina.natour.models.dao.implementation.UserDAOImpl;
import com.unina.natour.models.dao.interfaces.UserDAO;
import com.unina.natour.views.dialogs.MessageDialog;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class ProfiloPersonaleController implements Parcelable {

    private final static String USERNAME = "user";

    private final static String TAG ="ProfiloPersonaleController";
    FragmentActivity activity;
    MessageDialog messageDialog;

    ProfiloPersonaleModel profiloPersonaleModel;

    UserDAO userDAO;

    @RequiresApi(api = Build.VERSION_CODES.N)
    public ProfiloPersonaleController(FragmentActivity activity){
        this.activity = activity;
        this.messageDialog = new MessageDialog(activity);

        this.userDAO = new UserDAOImpl(activity);

        this.profiloPersonaleModel = initModel();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private ProfiloPersonaleModel initModel(){

        CompletableFuture<String> completableFuture = new CompletableFuture<String>();

        Amplify.Auth.fetchUserAttributes(
                attributes -> {
                    for(AuthUserAttribute attribute : attributes){
                        if(attribute.getKey().getKeyString().equals("email")){
                            completableFuture.complete(attribute.getValue());
                            Log.i("AuthDemo", "User attributes = " + attribute.getValue());
                        }
                    }
                },
                error -> {
                    completableFuture.complete(null);
                    Log.e("AuthDemo", "Failed to fetch user attributes.", error);
                }
        );

        String email = null;

        try {
            email = completableFuture.get();
        }
        catch (ExecutionException e) {
            e.printStackTrace();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }


        UserDTO userDTO = null;
        try {
            userDTO = userDAO.getUser(USERNAME);
        }
        catch (ExecutionException e) {
            e.printStackTrace();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        catch (UnknownException e) {
            e.printStackTrace();
        }
        catch (ServerException e) {
            e.printStackTrace();
        }

        Bitmap profileImage = userDAO.getUserProfileImage(USERNAME);

        ProfiloPersonaleModel profiloPersonaleModel = new ProfiloPersonaleModel();

        profiloPersonaleModel.setId(userDTO.getId());
        profiloPersonaleModel.setUsername(userDTO.getUsername());
        profiloPersonaleModel.setEmail(email);

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
