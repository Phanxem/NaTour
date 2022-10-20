package com.unina.natour.models.dao.implementation;

import com.unina.natour.controllers.MessageController;
import com.unina.natour.dto.response.GetListChatMessageResponseDTO;
import com.unina.natour.dto.response.GetChatMessageResponseDTO;
import com.unina.natour.models.dao.interfaces.ChatDAO;

import java.util.ArrayList;
import java.util.List;

public class ChatDAOImpl extends ServerDAO implements ChatDAO {

    @Override
    public GetListChatMessageResponseDTO getMessageByidsUser(long userId1, long userId2) {

        GetChatMessageResponseDTO test1 = new GetChatMessageResponseDTO();
        test1.setBody("hellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohello");
        test1.setDateOfInput("12/12/22");
        test1.setIdUser(11);
        test1.setIdChat(22);

        GetChatMessageResponseDTO test2 = new GetChatMessageResponseDTO();
        test2.setBody("hihihihihihiohellohelloheohellohelloheohellohelloheohellohellohehihihi");
        test2.setDateOfInput("12/12/22");
        test2.setIdUser(22);
        test2.setIdChat(11);

        GetChatMessageResponseDTO testP = new GetChatMessageResponseDTO();
        testP.setBody("PRIMO");
        testP.setDateOfInput("12/12/22");
        testP.setIdUser(11);
        testP.setIdChat(22);

        GetChatMessageResponseDTO testU = new GetChatMessageResponseDTO();
        testU.setBody("ULTIMO");
        testU.setDateOfInput("12/12/22");
        testU.setIdUser(22);
        testU.setIdChat(11);


        List<GetChatMessageResponseDTO> list = new ArrayList<GetChatMessageResponseDTO>();

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

        GetListChatMessageResponseDTO listTest = new GetListChatMessageResponseDTO();

        listTest.setListMessage(list);
        listTest.setResultMessage(MessageController.getSuccessMessage());

        return listTest;
    }
}
