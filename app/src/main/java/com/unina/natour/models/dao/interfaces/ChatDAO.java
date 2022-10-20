package com.unina.natour.models.dao.interfaces;

import com.unina.natour.dto.response.GetListChatMessageResponseDTO;

public interface ChatDAO {

    //GETs
    GetListChatMessageResponseDTO getMessageByidsUser(long userId1, long userId2, int page);


}
