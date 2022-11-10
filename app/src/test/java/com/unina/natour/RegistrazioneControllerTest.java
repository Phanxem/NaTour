package com.unina.natour;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.junit.Assert.*;

import com.unina.natour.controllers.AutenticazioneController;
import com.unina.natour.controllers.RegistrazioneController;
import com.unina.natour.controllers.ResultMessageController;
import com.unina.natour.dto.response.GetAuthSessionResponseDTO;
import com.unina.natour.dto.response.GetEmailResponseDTO;
import com.unina.natour.models.dao.implementation.AmplifyDAO;
import com.unina.natour.models.dao.interfaces.AccountDAO;
import com.unina.natour.views.activities.NaTourActivity;
import com.unina.natour.views.dialogs.MessageDialog;

import org.junit.Before;
import org.junit.Test;

public class RegistrazioneControllerTest {
    private RegistrazioneController registrazioneController;

    private NaTourActivity activity;
    private ResultMessageController resultMessageController;
    private AutenticazioneController autenticazioneController;
    private AccountDAO accountDAO;

    @Before
    public void setUp(){

        activity = mock(NaTourActivity.class);


        resultMessageController = mock(ResultMessageController.class);

        MessageDialog messageDialog = new MessageDialog();
        messageDialog.setMessage("test");
        when(resultMessageController.getMessageDialog())
                .thenReturn(messageDialog);

        when(resultMessageController.getActivity())
                .thenReturn(activity);


        autenticazioneController = mock(AutenticazioneController.class);
        when(autenticazioneController.signIn(anyString(), anyString()))
                .thenReturn(true);





        accountDAO = mock(AmplifyDAO.class);

        when(accountDAO.signUp(anyString(), anyString(), anyString()))
                .thenReturn(ResultMessageController.SUCCESS_MESSAGE);

        when(accountDAO.signIn(anyString(), anyString()))
                .thenReturn(ResultMessageController.SUCCESS_MESSAGE);

        when(accountDAO.activateAccount(anyString(), anyString()))
                .thenReturn(ResultMessageController.SUCCESS_MESSAGE);

        when(accountDAO.startPasswordRecovery(anyString()))
                .thenReturn(ResultMessageController.SUCCESS_MESSAGE);

        when(accountDAO.completePasswordRecovery(anyString(), anyString()))
                .thenReturn(ResultMessageController.SUCCESS_MESSAGE);

        when(accountDAO.resendActivationCode(anyString()))
                .thenReturn(ResultMessageController.SUCCESS_MESSAGE);

        when(accountDAO.signOut())
                .thenReturn(ResultMessageController.SUCCESS_MESSAGE);

        when(accountDAO.updatePassword(anyString(), anyString()))
                .thenReturn(ResultMessageController.SUCCESS_MESSAGE);

        GetAuthSessionResponseDTO getAuthSessionResponseDTO = new GetAuthSessionResponseDTO();
        getAuthSessionResponseDTO.setResultMessage(ResultMessageController.SUCCESS_MESSAGE);
        when(accountDAO.fetchAuthSessione())
                .thenReturn(getAuthSessionResponseDTO);

        GetEmailResponseDTO getEmailResponseDTO = new GetEmailResponseDTO();
        getEmailResponseDTO.setResultMessage(ResultMessageController.SUCCESS_MESSAGE);
        when(accountDAO.getEmail())
                .thenReturn(getEmailResponseDTO);


        registrazioneController = new RegistrazioneController(activity, resultMessageController,autenticazioneController,accountDAO);
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
