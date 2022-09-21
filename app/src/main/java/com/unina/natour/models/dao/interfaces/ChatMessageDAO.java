package com.unina.natour.models.dao.interfaces;

import com.unina.natour.dto.response.ChatMessageListResponseDTO;

public interface ChatMessageDAO {
    ChatMessageListResponseDTO findMessageByUserIds(long userId1, long userId2);
}
