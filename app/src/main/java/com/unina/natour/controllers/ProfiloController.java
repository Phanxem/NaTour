package com.unina.natour.controllers;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.unina.natour.dto.response.EmailResponseDTO;
import com.unina.natour.dto.response.MessageResponseDTO;
import com.unina.natour.dto.response.UserResponseDTO;
import com.unina.natour.models.ProfiloModel;
import com.unina.natour.models.dao.implementation.AmplifyDAO;
import com.unina.natour.models.dao.implementation.UserDAOImpl;
import com.unina.natour.models.dao.interfaces.UserDAO;
import com.unina.natour.views.activities.NaTourActivity;
import com.unina.natour.views.activities.PersonalizzaAccountInfoOpzionaliActivity;
import com.unina.natour.views.activities.ProfiloUtenteActivity;

@RequiresApi(api = Build.VERSION_CODES.N)
public class ProfiloController extends NaTourController {

    private static final String EXTRA_USER_ID = "USER_ID";
    private ListaItinerariController listaItinerariController;

    private ProfiloModel profiloModel;
    private boolean isMyProfile;

    private AmplifyDAO amplifyDAO;
    private UserDAO userDAO;

    public ProfiloController(NaTourActivity activity) {
        super(activity);

        this.amplifyDAO = new AmplifyDAO();
        this.userDAO = new UserDAOImpl(activity);

        Intent intent = new Intent();
        long userId = intent.getLongExtra(EXTRA_USER_ID,-1);


        if(userId < 0){
            //TODO this.username = Amplify.Auth.getCurrentUser().getUsername();
            String username = "user";
            isMyProfile = true;

            UserResponseDTO userResponseDTO = userDAO.getUser(username);
            MessageResponseDTO messageResponseDTO = userResponseDTO.getResultMessage();
            if(messageResponseDTO.getCode() != MessageController.SUCCESS_CODE){
                showErrorMessage(messageResponseDTO);
                activity.finish();
                return;
            }

            userId = userResponseDTO.getId();
        }

        else isMyProfile = false;

        this.listaItinerariController = new ListaItinerariController(activity, ListaItinerariController.CODE_ITINERARY_BY_USER_ID, null, userId);

        this.profiloModel = new ProfiloModel();
        //initModel();
    }



    public void initModel() {
        EmailResponseDTO emailResponseDTO = amplifyDAO.getEmail();

        MessageResponseDTO messageResponseDTO = emailResponseDTO.getResultMessage();
        if(messageResponseDTO.getCode() != MessageController.SUCCESS_CODE){
            Log.i(TAG,"ERROR1");
            showErrorMessage(messageResponseDTO);
            return;
        }
        String email = emailResponseDTO.getEmail();


        //TODO this.username = Amplify.Auth.getCurrentUser().getUsername();
        String username = "user";
        UserResponseDTO userResponseDTO = userDAO.getUser(username);
        messageResponseDTO = userResponseDTO.getResultMessage();
        if(messageResponseDTO.getCode() != MessageController.SUCCESS_CODE){
            Log.i(TAG,"ERROR2");
            showErrorMessage(messageResponseDTO);
            return;
        }

        boolean result = dtoToModel(getActivity(), userResponseDTO, emailResponseDTO, profiloModel);
        if(!result){
            Log.i(TAG,"ERROR3");
            //TODO
            showErrorMessage(0);
            return;
        }
    }


    public ProfiloModel getModel() {
        return profiloModel;
    }

    public boolean isMyProfile(){
        return isMyProfile;
    }

    public void setProfiloPersonaleModel(ProfiloModel profiloModel) {
        this.profiloModel = profiloModel;
    }

    public ListaItinerariController getListaItinerariController() {
        return listaItinerariController;
    }

    public void setListaItinerariController(ListaItinerariController listaItinerariController) {
        this.listaItinerariController = listaItinerariController;
    }


    public static void openProfiloUtenteActivity(NaTourActivity fromActivity, long userId){

        Intent intent = new Intent(fromActivity, ProfiloUtenteActivity.class);
        intent.putExtra(EXTRA_USER_ID,userId);
        fromActivity.startActivity(intent);
    }



    public static boolean dtoToModel(Context context, UserResponseDTO userDto, EmailResponseDTO emailDto, ProfiloModel model){
        model.clear();

        model.setEmail(emailDto.getEmail());


        model.setId(userDto.getId());
        model.setUsername(userDto.getUsername());
        model.setPlaceOfResidence(userDto.getPlaceOfResidence());
        model.setDateOfBirth(userDto.getDateOfBirth());

        model.setProfileImage(userDto.getProfileImage());


        model.setFacebookLinked(userDto.isFacebookLinked());
        model.setGoogleLinked(userDto.isGoogleLinked());

        return true;
    }
}
