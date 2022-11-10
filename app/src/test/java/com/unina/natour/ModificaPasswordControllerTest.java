package com.unina.natour;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.junit.Assert.*;

import com.unina.natour.controllers.AutenticazioneController;
import com.unina.natour.controllers.ModificaPasswordController;
import com.unina.natour.controllers.ResultMessageController;
import com.unina.natour.dto.response.GetAuthSessionResponseDTO;
import com.unina.natour.dto.response.GetEmailResponseDTO;
import com.unina.natour.models.dao.implementation.AmplifyDAO;
import com.unina.natour.models.dao.interfaces.AccountDAO;
import com.unina.natour.views.activities.NaTourActivity;
import com.unina.natour.views.dialogs.MessageDialog;

import org.junit.Before;
import org.junit.Test;

import java.nio.channels.AcceptPendingException;

public class ModificaPasswordControllerTest {
    private ModificaPasswordController modificaPasswordController;

    private NaTourActivity activity;
    private ResultMessageController resultMessageController;
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


        modificaPasswordController = new ModificaPasswordController(activity,resultMessageController,accountDAO);
    }

    //Test Case 1 ---
    @Test
    public void testUpdatePasswordWithOldPasswordNullAndNewPassword1Lenght15AndNewPassword2Lenght15(){
        String oldPassword = null;
        String newPassword1 = "newPasswordTest";
        String newPassword2 = "newPasswordTest";

        boolean result = modificaPasswordController.updatePassword(oldPassword, newPassword1, newPassword2);

        assertFalse(result);
    }

    //Test Case 2 ---
    @Test
    public void testUpdatePasswordWithOldPasswordLenght7AndNewPassword1Lenght15AndNewPassword2Lenght15(){
        String oldPassword = "oldPass";
        String newPassword1 = "newPasswordTest";
        String newPassword2 = "newPasswordTest";

        boolean result = modificaPasswordController.updatePassword(oldPassword, newPassword1, newPassword2);

        assertFalse(result);
    }

    //Test Case 3 ---
    @Test
    public void testUpdatePasswordWithOldPasswordLenght15AndNewPassword1NullAndNewPassword2Lenght15(){
        String oldPassword = "oldPasswordTest";
        String newPassword1 = null;
        String newPassword2 = "newPasswordTest";

        boolean result = modificaPasswordController.updatePassword(oldPassword, newPassword1, newPassword2);

        assertFalse(result);
    }

    //Test Case 4 ---
    @Test
    public void testUpdatePasswordWithOldPasswordLenght15AndNewPassword1Lenght7AndNewPassword2Lenght15(){
        String oldPassword = "oldPasswordTest";
        String newPassword1 = "newPass";
        String newPassword2 = "newPasswordTest";

        boolean result = modificaPasswordController.updatePassword(oldPassword, newPassword1, newPassword2);

        assertFalse(result);
    }

    //Test Case 5 ---
    @Test
    public void testUpdatePasswordWithOldPasswordLenght15AndNewPassword1Lenght15AndNewPassword2Null(){
        String oldPassword = "oldPasswordTest";
        String newPassword1 = "newPasswordTest";
        String newPassword2 = null;

        boolean result = modificaPasswordController.updatePassword(oldPassword, newPassword1, newPassword2);

        assertFalse(result);
    }

    //Test Case 6 ---
    @Test
    public void testUpdatePasswordWithOldPasswordLenght15AndNewPassword1Lenght15AndNewPassword2Lenght7(){
        String oldPassword = "oldPasswordTest";
        String newPassword1 = "newPasswordTest";
        String newPassword2 = "newPass";

        boolean result = modificaPasswordController.updatePassword(oldPassword, newPassword1, newPassword2);

        assertFalse(result);
    }

    //Test Case 7 ---
    @Test
    public void testUpdatePasswordWithOldPasswordLenght15AndNewPassword1Lenght16AndNewPassword2Lenght16AndUnmatchedNewPasswords(){
        String oldPassword = "oldPasswordTest";
        String newPassword1 = "newPasswordTest1";
        String newPassword2 = "newPasswordTest2";

        boolean result = modificaPasswordController.updatePassword(oldPassword, newPassword1, newPassword2);

        assertFalse(result);
    }

    //Test Case 8 ---
    @Test
    public void testUpdatePasswordWithOldPasswordLenght15AndNewPassword1Lenght15AndNewPassword2Lenght15AndMatchedNewPasswords(){
        String oldPassword = "oldPasswordTest";
        String newPassword1 = "newPasswordTest";
        String newPassword2 = "newPasswordTest";

        boolean result = modificaPasswordController.updatePassword(oldPassword, newPassword1, newPassword2);

        assertTrue(result);
    }

}
