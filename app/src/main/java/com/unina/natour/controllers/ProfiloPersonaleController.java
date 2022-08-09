package com.unina.natour.controllers;

import android.app.Activity;
import android.graphics.Bitmap;
import android.util.Log;

import com.amplifyframework.auth.AuthUser;
import com.amplifyframework.auth.AuthUserAttribute;
import com.amplifyframework.core.Amplify;
import com.unina.natour.dto.UserDTO;
import com.unina.natour.models.ProfiloPersonaleModel;
import com.unina.natour.models.dao.classes.UserDAOImpl;
import com.unina.natour.models.dao.interfaces.UserDAO;
import com.unina.natour.views.dialogs.MessageDialog;

public class ProfiloPersonaleController {

    private final static String USERNAME = "user";

    private final static String TAG ="ProfiloPersonaleController";
    Activity activity;
    MessageDialog messageDialog;

    ProfiloPersonaleModel profiloPersonaleModel;

    UserDAO userDAO;

    public ProfiloPersonaleController(Activity activity){
        this.activity = activity;
        this.messageDialog = new MessageDialog(activity);

        this.userDAO = new UserDAOImpl(activity);

        initModel();
    }


    public void initModel(){


        Amplify.Auth.fetchUserAttributes(
                attributes -> {
                    for(AuthUserAttribute attribute : attributes){
                        if(attribute.getKey().getKeyString().equals("email")){
                            Log.i("AuthDemo", "User attributes = " + attribute.getValue());
                        }
                    }
                },
                error -> Log.e("AuthDemo", "Failed to fetch user attributes.", error)
        );


        UserDTO userDTO = userDAO.getUser(USERNAME);
        Bitmap profileImage = userDAO.getUserProfileImage(USERNAME);


        //String email =  amplify congito function

        this.profiloPersonaleModel = new ProfiloPersonaleModel();
        this.profiloPersonaleModel.setId(userDTO.getId());
        this.profiloPersonaleModel.setUsername(userDTO.getUsername());
        this.profiloPersonaleModel.setPlaceOfResidence(userDTO.getPlaceOfResidence());

        if(userDTO.getDateOfBirth() != null){
            String string = userDTO.getDateOfBirth();
            this.profiloPersonaleModel.setDateOfBirth(string.substring(0,8));
        }
        else this.profiloPersonaleModel.setDateOfBirth(null);



        this.profiloPersonaleModel.setProfileImage(profileImage);
    }

    public ProfiloPersonaleModel getProfiloPersonaleModel() {
        return profiloPersonaleModel;
    }

    public void setProfiloPersonaleModel(ProfiloPersonaleModel profiloPersonaleModel) {
        this.profiloPersonaleModel = profiloPersonaleModel;
    }

    /*
    public void openPersonalizzaAccountImmagineProfiloActivity(){
        Intent intent = new Intent(activity, RegistrazioneActivity.class);
        activity.startActivity(intent);
    }

    public void openPersonalizzaAccountImmagineProfiloActivity(){
        Intent intent = new Intent(activity, RegistrazioneActivity.class);
        activity.startActivity(intent);
    }
*/

}
