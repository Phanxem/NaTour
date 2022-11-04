package com.unina.natour.controllers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.unina.natour.R;
import com.unina.natour.config.CurrentUserInfo;
import com.unina.natour.dto.response.GetCognitoEmailResponseDTO;
import com.unina.natour.dto.response.composted.GetUserWithImageResponseDTO;
import com.unina.natour.models.ProfiloModel;
import com.unina.natour.models.dao.implementation.AmplifyDAO;
import com.unina.natour.models.dao.implementation.UserDAOImpl;
import com.unina.natour.models.dao.interfaces.UserDAO;
import com.unina.natour.views.activities.NaTourActivity;
import com.unina.natour.views.activities.ProfiloUtenteActivity;

public class ProfiloController extends NaTourController {

    public static final String EXTRA_USER_ID = "USER_ID";
    private ListaItinerariController listaItinerariController;

    private ProfiloModel profiloModel;

    private AmplifyDAO amplifyDAO;
    private UserDAO userDAO;

    public ProfiloController(NaTourActivity activity, long idUser) {
        super(activity);
        String messageToShow = null;

        Log.e(TAG, "idUser: " + idUser);

        if(idUser < 0){
            messageToShow = activity.getString(R.string.Message_UnknownError);
            showErrorMessageAndBack(messageToShow);
            return;
        }

        this.listaItinerariController = new ListaItinerariController(getActivity(), ListaItinerariController.CODE_ITINERARY_BY_USER_ID, null, idUser);

        this.amplifyDAO = new AmplifyDAO();
        this.userDAO = new UserDAOImpl(activity);

        this.profiloModel = new ProfiloModel();
        boolean result = initModel(idUser);
        if(!result){return;}
    }



    public boolean initModel(long userId) {
        Activity activity = getActivity();
        String messageToShow = null;

        if(userId < 0){
            messageToShow = activity.getString(R.string.Message_UnknownError);
            showErrorMessageAndBack(messageToShow);
            return false;
        }

        if(!CurrentUserInfo.isSignedIn()){
            messageToShow = activity.getString(R.string.Message_UnknownError);
            showErrorMessageAndBack(messageToShow);
            return false;
        }



        String email = null;

        String currentIdentityProvider = CurrentUserInfo.getIdentityProvider();
        String cognitoIdentityProvider = getActivity().getString(R.string.IdentityProvider_Cognito);

        if(userId == CurrentUserInfo.getId() && currentIdentityProvider.equals(cognitoIdentityProvider)){
            GetCognitoEmailResponseDTO getCognitoEmailResponseDTO = amplifyDAO.getEmail();
            if(!ResultMessageController.isSuccess(getCognitoEmailResponseDTO.getResultMessage())){
                messageToShow = activity.getString(R.string.Message_UnknownError);
                showErrorMessageAndBack(messageToShow);
                return false;
            }
            email = getCognitoEmailResponseDTO.getEmail();
        }

        GetUserWithImageResponseDTO getUserWithImageResponseDTO = userDAO.getUserWithImageById(userId);
        if(!ResultMessageController.isSuccess(getUserWithImageResponseDTO.getResultMessage())){
            messageToShow = activity.getString(R.string.Message_UnknownError);
            showErrorMessageAndBack(messageToShow);
            return false;
        }


        boolean result = dtoToModel(getActivity(), getUserWithImageResponseDTO, email, profiloModel);
        if(!result){
            messageToShow = activity.getString(R.string.Message_UnknownError);
            showErrorMessageAndBack(messageToShow);
            return false;
        }

        return true;
    }


    public ProfiloModel getModel() {
        return profiloModel;
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



    public static boolean dtoToModel(Context context, GetUserWithImageResponseDTO userDto, String email, ProfiloModel model){
        model.clear();

        model.setEmail(email);


        model.setId(userDto.getId());
        model.setUsername(userDto.getUsername());
        model.setPlaceOfResidence(userDto.getPlaceOfResidence());
        model.setDateOfBirth(userDto.getDateOfBirth());

        model.setProfileImage(userDto.getProfileImage());

        return true;
    }
}
