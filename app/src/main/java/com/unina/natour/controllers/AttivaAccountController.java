package com.unina.natour.controllers;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;

import com.unina.natour.R;
import com.unina.natour.controllers.utils.StringsUtils;
import com.unina.natour.dto.request.SaveUserRequestDTO;
import com.unina.natour.dto.response.ResultMessageDTO;
import com.unina.natour.models.dao.implementation.AmplifyDAO;
import com.unina.natour.models.dao.implementation.UserDAOImpl;
import com.unina.natour.models.dao.interfaces.UserDAO;
import com.unina.natour.views.activities.AttivaAccountActivity;
import com.unina.natour.views.activities.AutenticazioneActivity;
import com.unina.natour.views.activities.NaTourActivity;
import com.unina.natour.views.activities.RegistrazioneActivity;

public class AttivaAccountController extends NaTourController{

    public static final String SHARED_PREFERENCES_ACCOUNT_ACTIVATION = "accountActivation";
    public static final String SHARED_PREFERENCES_USERNAME = "USERNAME";
    public static final String SHARED_PREFERENCES_PASSWORD = "PASSWORD";
    public static final String SHARED_PREFERENCES_EMAIL = "EMAIL";

    public static final String EXTRA_USERNAME = "USERNAME";
    public static final String EXTRA_PASSWORD = "PASSWORD";
    public static final String EXTRA_EMAIL = "EMAIL";

    private AutenticazioneController autenticazioneController;

    private String username;
    private String password;
    private String email;

    private AmplifyDAO amplifyDAO;
    private UserDAO userDAO;

    public AttivaAccountController(NaTourActivity activity){
        super(activity);

        this.autenticazioneController = new AutenticazioneController(activity);

        this.amplifyDAO = new AmplifyDAO();
        this.userDAO = new UserDAOImpl(getActivity());

        Intent intent = activity.getIntent();
        this.username = intent.getStringExtra(EXTRA_USERNAME);
        this.password = intent.getStringExtra(EXTRA_PASSWORD);
        this.email = intent.getStringExtra(EXTRA_EMAIL);

        if(!StringsUtils.areAllFieldsFull(username, password, email)){
            String messageToShow = activity.getString(R.string.Message_EmptyFieldError);
            showErrorMessageAndBack(messageToShow);
            return;
        }
    }

    public void initAccountActivation(){
        String packageName = getActivity().getApplicationContext().getPackageName();
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(packageName,Context.MODE_PRIVATE);
        SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();

        sharedPreferencesEditor.putBoolean(SHARED_PREFERENCES_ACCOUNT_ACTIVATION,true);
        sharedPreferencesEditor.putString(SHARED_PREFERENCES_USERNAME,username);
        sharedPreferencesEditor.putString(SHARED_PREFERENCES_PASSWORD,password);
        sharedPreferencesEditor.putString(SHARED_PREFERENCES_EMAIL,email);
        sharedPreferencesEditor.commit();
    }

    public boolean activeAccount(String code){
        Activity activity = getActivity();
        String messageToShow = null;

        if(!StringsUtils.areAllFieldsFull(code)) {
            messageToShow = activity.getString(R.string.Message_EmptyFieldError);
            showErrorMessage(messageToShow);
            return false;
        }

        ResultMessageDTO resultMessageDTO = amplifyDAO.activateAccount(username, code);
        if(!ResultMessageController.isSuccess(resultMessageDTO)){

            if(resultMessageDTO.getCode() == ResultMessageController.ERROR_CODE_AMPLIFY){
                messageToShow = ResultMessageController.findMessageFromAmplifyMessage(activity, resultMessageDTO.getMessage());
                showErrorMessage(messageToShow);
                return false;
            }

            messageToShow = activity.getString(R.string.Message_UnknownError);
            showErrorMessage(messageToShow);
            return false;
        }

        Context applicationContext = activity.getApplicationContext();
        String packageName = applicationContext.getPackageName();
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(packageName,Context.MODE_PRIVATE);
        SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();

        sharedPreferencesEditor.remove(SHARED_PREFERENCES_ACCOUNT_ACTIVATION);
        sharedPreferencesEditor.remove(SHARED_PREFERENCES_USERNAME);
        sharedPreferencesEditor.remove(SHARED_PREFERENCES_PASSWORD);
        sharedPreferencesEditor.remove(SHARED_PREFERENCES_EMAIL);
        sharedPreferencesEditor.commit();

        String identityProvider = activity.getString(R.string.IdentityProvider_Cognito);

        SaveUserRequestDTO saveUserRequestDTO = new SaveUserRequestDTO();
        saveUserRequestDTO.setUsername(username);
        saveUserRequestDTO.setIdentityProvider(identityProvider);
        saveUserRequestDTO.setIdIdentityProvided(username);

        resultMessageDTO = userDAO.addUser(saveUserRequestDTO);
        if(!ResultMessageController.isSuccess(resultMessageDTO)){
            messageToShow = activity.getString(R.string.Message_UnknownError);
            showErrorMessageAndBack(messageToShow);
            return false;
        }

        resultMessageDTO = amplifyDAO.signIn(username,password);
        if(!ResultMessageController.isSuccess(resultMessageDTO)){
            if(resultMessageDTO.getCode() == ResultMessageController.ERROR_CODE_AMPLIFY){
                messageToShow = ResultMessageController.findMessageFromAmplifyMessage(activity, resultMessageDTO.getMessage());
                showErrorMessage(messageToShow);
                return false;
            }

            messageToShow = activity.getString(R.string.Message_UnknownError);
            showErrorMessage(messageToShow);
            return false;
        }

        return true;
    }

    public boolean cancelAccountActivation(){
        Activity activity = getActivity();
        String messageToShow = null;

        ResultMessageDTO resultMessageDTO = userDAO.cancelRegistrationUser(username);
        if(!ResultMessageController.isSuccess(resultMessageDTO)){
            messageToShow = activity.getString(R.string.Message_UnknownError);
            showErrorMessage(messageToShow);
            return false;
        }

        String packageName = getActivity().getApplicationContext().getPackageName();
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(packageName,Context.MODE_PRIVATE);
        SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();

        sharedPreferencesEditor.remove(SHARED_PREFERENCES_ACCOUNT_ACTIVATION);
        sharedPreferencesEditor.remove(SHARED_PREFERENCES_USERNAME);
        sharedPreferencesEditor.remove(SHARED_PREFERENCES_PASSWORD);
        sharedPreferencesEditor.remove(SHARED_PREFERENCES_EMAIL);
        sharedPreferencesEditor.commit();

        getActivity().finish();

        return true;
    }

    public void back(){
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getActivity().startActivity(intent);
    }

    public boolean resendCode(){
        Activity activity = getActivity();
        String messageToShow = null;

        ResultMessageDTO resultMessageDTO = amplifyDAO.resendActivationCode(username);
        if(!ResultMessageController.isSuccess(resultMessageDTO)){

            if(resultMessageDTO.getCode() == ResultMessageController.ERROR_CODE_AMPLIFY){
                messageToShow = ResultMessageController.findMessageFromAmplifyMessage(activity, resultMessageDTO.getMessage());
                showErrorMessage(messageToShow);
                return false;
            }

            messageToShow = activity.getString(R.string.Message_UnknownError);
            showErrorMessage(messageToShow);
            return false;
        }

        return true;
    }


    public static void openAttivaAccountActivity(NaTourActivity fromActivity, String username, String password, String email){
        if( !(fromActivity instanceof RegistrazioneActivity) ){
            Intent intentAutenticazioneActivity = new Intent(fromActivity, AutenticazioneActivity.class);
            intentAutenticazioneActivity.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            fromActivity.startActivity(intentAutenticazioneActivity);

            Intent intentRegistrazioneActivity = new Intent(fromActivity, RegistrazioneActivity.class);
            intentRegistrazioneActivity.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            fromActivity.startActivity(intentRegistrazioneActivity);
        }

        Intent intent = new Intent(fromActivity, AttivaAccountActivity.class);
        intent.putExtra(EXTRA_USERNAME,username);
        intent.putExtra(EXTRA_PASSWORD,password);
        intent.putExtra(EXTRA_EMAIL,email);
        fromActivity.startActivity(intent);
    }

}
