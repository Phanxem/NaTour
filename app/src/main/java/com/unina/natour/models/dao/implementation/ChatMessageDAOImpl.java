package com.unina.natour.models.dao.implementation;

import android.util.Log;

import com.unina.natour.controllers.MessageController;
import com.unina.natour.dto.response.ChatMessageListResponseDTO;
import com.unina.natour.dto.response.ChatMessageResponseDTO;
import com.unina.natour.dto.response.MessageResponseDTO;
import com.unina.natour.models.dao.interfaces.ChatMessageDAO;

import java.util.ArrayList;
import java.util.List;

public class ChatMessageDAOImpl implements ChatMessageDAO {

    @Override
    public ChatMessageListResponseDTO findMessageByUserIds(long userId1, long userId2) {

        ChatMessageResponseDTO test1 = new ChatMessageResponseDTO();
        test1.setMessage("hellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohello");
        test1.setDateOfInput("12/12/22");
        test1.setUserSourceId(11);
        test1.setUserDestinationId(22);

        ChatMessageResponseDTO test2 = new ChatMessageResponseDTO();
        test2.setMessage("hihihihihihiohellohelloheohellohelloheohellohelloheohellohellohehihihi");
        test2.setDateOfInput("12/12/22");
        test2.setUserSourceId(22);
        test2.setUserDestinationId(11);

        ChatMessageResponseDTO testP = new ChatMessageResponseDTO();
        testP.setMessage("PRIMO");
        testP.setDateOfInput("12/12/22");
        testP.setUserSourceId(11);
        testP.setUserDestinationId(22);

        ChatMessageResponseDTO testU = new ChatMessageResponseDTO();
        testU.setMessage("ULTIMO");
        testU.setDateOfInput("12/12/22");
        testU.setUserSourceId(22);
        testU.setUserDestinationId(11);


        List<ChatMessageResponseDTO> list = new ArrayList<ChatMessageResponseDTO>();

        list.add(testP);
        list.add(test1);
        list.add(test2);
        list.add(test2);
        list.add(test2);
        list.add(test2);
        list.add(test2);
        list.add(test2);
        list.add(test2);
        list.add(test2);
        list.add(test2);
        list.add(test2);
        list.add(test2);
        list.add(test2);
        list.add(test2);
        list.add(test2);
        list.add(test2);
        list.add(test2);
        list.add(test2);
        list.add(test2);
        list.add(testU);

        ChatMessageListResponseDTO listTest = new ChatMessageListResponseDTO();

        listTest.setMessages(list);
        listTest.setResultMessage(MessageController.getSuccessMessage());

        return listTest;
    }
}
