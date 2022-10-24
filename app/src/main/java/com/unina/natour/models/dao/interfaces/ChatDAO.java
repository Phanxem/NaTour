package com.unina.natour.models.dao.interfaces;

import com.unina.natour.dto.response.GetListChatMessageResponseDTO;
import com.unina.natour.dto.response.composted.GetListChatWithUserResponseDTO;

public interface ChatDAO {

    //GETs
    public GetListChatMessageResponseDTO getMessageByIdsUser(long userId1, long userId2, int page);

    //COMPOSITEDs
    public GetListChatWithUserResponseDTO getListChatByIdUser(long idUser, int page);
}
