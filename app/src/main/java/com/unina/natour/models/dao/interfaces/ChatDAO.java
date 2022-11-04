package com.unina.natour.models.dao.interfaces;

import com.unina.natour.dto.response.GetListChatMessageResponseDTO;
import com.unina.natour.dto.response.ResultMessageDTO;
import com.unina.natour.dto.response.composted.GetListChatWithUserResponseDTO;

public interface ChatDAO {

    //GETs
    public GetListChatMessageResponseDTO getMessageByIdsUser(long userId1, long userId2, int page);

    public ResultMessageDTO readAllMessageByIdsUser(long userId1, long userId2);

    //COMPOSITEDs
    public GetListChatWithUserResponseDTO getListChatByIdUser(long idUser, int page);
}
