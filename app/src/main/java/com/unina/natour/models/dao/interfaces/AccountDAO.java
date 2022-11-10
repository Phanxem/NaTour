package com.unina.natour.models.dao.interfaces;

import com.unina.natour.dto.response.GetAuthSessionResponseDTO;
import com.unina.natour.dto.response.GetEmailResponseDTO;
import com.unina.natour.dto.response.ResultMessageDTO;

public interface AccountDAO {

    public ResultMessageDTO signUp(String username, String email, String password);

    public ResultMessageDTO activateAccount(String username, String confirmationCode);

    public ResultMessageDTO resendActivationCode(String username);

    public ResultMessageDTO signIn(String username, String password);

    public ResultMessageDTO signOut();

    public ResultMessageDTO updatePassword(String oldPassword, String newPassword);

    public GetEmailResponseDTO getEmail();

    public ResultMessageDTO startPasswordRecovery(String username);

    public ResultMessageDTO completePasswordRecovery(String confirmationCode, String password);

    public GetAuthSessionResponseDTO fetchAuthSessione();


}
