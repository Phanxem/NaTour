package com.unina.natour;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.junit.Assert.*;

import android.os.Message;
import android.support.v4.media.MediaMetadataCompat;

import com.unina.natour.controllers.AutenticazioneController;
import com.unina.natour.controllers.RegistrazioneController;
import com.unina.natour.controllers.ResultMessageController;
import com.unina.natour.dto.response.GetCognitoAuthSessionResponseDTO;
import com.unina.natour.dto.response.GetCognitoEmailResponseDTO;
import com.unina.natour.models.dao.implementation.AmplifyDAO;
import com.unina.natour.views.activities.NaTourActivity;
import com.unina.natour.views.dialogs.MessageDialog;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class RegistrazioneControllerTest {
    private RegistrazioneController registrazioneController;

    NaTourActivity activity;
    ResultMessageController resultMessageController;
    AutenticazioneController autenticazioneController;
    AmplifyDAO amplifyDAO;

    @Before
    public void setUp(){

        activity = mock(NaTourActivity.class);

        autenticazioneController = mock(AutenticazioneController.class);
        when(autenticazioneController.signIn(anyString(), anyString()))
                .thenReturn(true);



        amplifyDAO = mock(AmplifyDAO.class);

        when(amplifyDAO.signUp(anyString(), anyString(), anyString()))
                .thenReturn(ResultMessageController.SUCCESS_MESSAGE);

        when(amplifyDAO.signIn(anyString(), anyString()))
                .thenReturn(ResultMessageController.SUCCESS_MESSAGE);

        when(amplifyDAO.activateAccount(anyString(), anyString()))
                .thenReturn(ResultMessageController.SUCCESS_MESSAGE);

        when(amplifyDAO.startPasswordRecovery(anyString()))
                .thenReturn(ResultMessageController.SUCCESS_MESSAGE);

        when(amplifyDAO.completePasswordRecovery(anyString(), anyString()))
                .thenReturn(ResultMessageController.SUCCESS_MESSAGE);

        when(amplifyDAO.resendActivationCode(anyString()))
                .thenReturn(ResultMessageController.SUCCESS_MESSAGE);

        when(amplifyDAO.signOut())
                .thenReturn(ResultMessageController.SUCCESS_MESSAGE);

        when(amplifyDAO.updatePassword(anyString(), anyString()))
                .thenReturn(ResultMessageController.SUCCESS_MESSAGE);

        GetCognitoAuthSessionResponseDTO getCognitoAuthSessionResponseDTO = new GetCognitoAuthSessionResponseDTO();
        getCognitoAuthSessionResponseDTO.setResultMessage(ResultMessageController.SUCCESS_MESSAGE);
        when(amplifyDAO.fetchAuthSessione())
                .thenReturn(getCognitoAuthSessionResponseDTO);

        GetCognitoEmailResponseDTO getCognitoEmailResponseDTO = new GetCognitoEmailResponseDTO();
        getCognitoEmailResponseDTO.setResultMessage(ResultMessageController.SUCCESS_MESSAGE);
        when(amplifyDAO.getEmail())
                .thenReturn(getCognitoEmailResponseDTO);


        resultMessageController = mock(ResultMessageController.class);

        MessageDialog messageDialog = new MessageDialog();
        messageDialog.setMessage("test");
        when(resultMessageController.getMessageDialog())
                .thenReturn(messageDialog);

        when(resultMessageController.getActivity())
                .thenReturn(activity);



        registrazioneController = new RegistrazioneController(activity, resultMessageController,autenticazioneController,amplifyDAO);
    }

    //Test Case 1 ---
    @Test
    public void testSignUpWithUsernameNullAndEmailIsRealEmailAndPasswordLength12(){
        String username = null;
        String email = "emailTest@test.it";
        String password = "passwordTest";

        boolean result = registrazioneController.signUp(username,email,password);

        assertFalse(result);
    }

    //Test Case 2 ---
    @Test
    public void testSignUpWithUsernameEmptyAndEmailRealEmailAndPasswordLength12(){
        String username = "";
        String email = "emailTest@test.it";
        String password = "passwordTest";

        boolean result = registrazioneController.signUp(username,email,password);

        assertFalse(result);
    }

    //Test Case 3 ---
    @Test
    public void testSignUpWithUsernameNotEmptyAndEmailNullAndPasswordLength12(){
        String username = "usernameTest";
        String email = null;
        String password = "passwordTest";

        boolean result = registrazioneController.signUp(username,email,password);

        assertFalse(result);
    }

    //Test Case 4 ---
    @Test
    public void testSignUpWithUsernameNotEmptyAndEmailEmptyAndPasswordLength12(){
        String username = "usernameTest";
        String email = "";
        String password = "passwordTest";

        boolean result = registrazioneController.signUp(username,email,password);

        assertFalse(result);
    }

    //Test Case 5 ---
    @Test
    public void testSignUpWithUsernameNotEmptyAndEmailNotRealEmailAndPasswordLength12(){
        String username = "usernameTest";
        String email = "emailTest";
        String password = "passwordTest";

        boolean result = registrazioneController.signUp(username,email,password);

        assertFalse(result);
    }

    //Test Case 6 ---
    @Test
    public void testSignUpWithUsernameNotEmptyAndEmailIsRealEmailAndPasswordNull(){
        String username = "usernameTest";
        String email = "emailTest@test.it";
        String password = null;

        boolean result = registrazioneController.signUp(username,email,password);

        assertFalse(result);
    }

    //Test Case 7 ---
    @Test
    public void testSignUpWithUsernameNotEmptyAndEmailIsRealEmailAndPasswordLength4(){
        String username = "usernameTest";
        String email = "emailTest@test.it";
        String password = "test";

        boolean result = registrazioneController.signUp(username,email,password);

        assertFalse(result);
    }

    //Test Case 8 ---
    @Test
    public void testSignUpWithUsernameNotEmptyAndEmailIsRealEmailAndPasswordLength12(){
        String username = "usernameTest";
        String email = "emailTest@test.it";
        String password = "passwordTest";

        boolean result = registrazioneController.signUp(username,email,password);

        assertTrue(result);
    }
}
