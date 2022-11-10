package com.unina.natour.models.dao.interfaces;

import com.unina.natour.dto.response.GetIdChatResponseDTO;
import com.unina.natour.dto.response.GetListChatMessageResponseDTO;
import com.unina.natour.dto.response.HasMessageToReadResponseDTO;
import com.unina.natour.dto.response.ResultMessageDTO;
import com.unina.natour.dto.response.composted.GetListChatWithUserResponseDTO;

public interface ChatDAO {

    //GETs
    public GetListChatMessageResponseDTO getMessageByIdsUser(long idUser1, long idUser2, int page);

    public GetIdChatResponseDTO getChatByIdsUser(long idUser1, long idUser2);

    public ResultMessageDTO readAllMessageByIdsUser(long idUser1, long idUser2);

    public HasMessageToReadResponseDTO checkIfHasMessageToRead(long idUser);

    public ResultMessageDTO addChat(long idUser1, long idUser2);

    //COMPOSITEDs
    public GetListChatWithUserResponseDTO getListChatByIdUser(long idUser, int page);
}
