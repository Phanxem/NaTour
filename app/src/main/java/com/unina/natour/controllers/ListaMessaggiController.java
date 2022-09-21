package com.unina.natour.controllers;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.unina.natour.controllers.utils.TimeUtils;
import com.unina.natour.dto.response.ChatMessageListResponseDTO;
import com.unina.natour.dto.response.ChatMessageResponseDTO;
import com.unina.natour.dto.response.MessageResponseDTO;
import com.unina.natour.dto.response.UserListResponseDTO;
import com.unina.natour.dto.response.UserResponseDTO;
import com.unina.natour.models.ElementMessageModel;
import com.unina.natour.models.ElementUserModel;
import com.unina.natour.models.dao.implementation.ChatMessageDAOImpl;
import com.unina.natour.models.dao.interfaces.ChatMessageDAO;
import com.unina.natour.views.activities.NaTourActivity;
import com.unina.natour.views.listAdapters.MessagesListAdapter;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ListaMessaggiController extends  NaTourController{

    private long userId1;
    private long userId2;

    private MessagesListAdapter messagesListAdapter;

    private ArrayList<ElementMessageModel> elementsMessageModel;
    private int page = 0;

    private ChatMessageDAO chatMessageDAO;

    public ListaMessaggiController(NaTourActivity activity, long userId1, long userId2){
        super(activity);

        //TODO test
        //this.userId1 = userId1;
        this.userId1 = 11;
        this.userId2 = userId2;

        this.elementsMessageModel = new ArrayList<ElementMessageModel>();

        this.chatMessageDAO = new ChatMessageDAOImpl();

        boolean result = initModel();
        if(!result){
            //TODO
            showErrorMessage(0);
            //getActivity().finish();
            return;
        }
    }

    private boolean initModel() {
        ChatMessageListResponseDTO chatMessageListResponseDTO = chatMessageDAO.findMessageByUserIds(userId1, userId2);
        MessageResponseDTO messageResponseDTO = chatMessageListResponseDTO.getResultMessage();
        if(messageResponseDTO.getCode() != MessageController.SUCCESS_CODE){
            //TODO
            showErrorMessage(messageResponseDTO);
            return false;
        }

        boolean result = dtoToModel(getActivity(), chatMessageListResponseDTO, elementsMessageModel);
        if(!result){
            showErrorMessage(0);
            return false;
        }

        Log.i(TAG + "--------...------", "size: "+ elementsMessageModel.size());

        this.messagesListAdapter = new MessagesListAdapter(getActivity(),elementsMessageModel);
        messagesListAdapter.notifyDataSetChanged();
        return true;
    }

    public void initList(NestedScrollView nestedScrollView_users, RecyclerView recyclerView_users, ProgressBar progressBar_users) {
        recyclerView_users.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView_users.setAdapter(messagesListAdapter);
        nestedScrollView_users.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                    // in this method we are incrementing page number,
                    // making progress bar visible and calling get data method.
                    page++;
                    progressBar_users.setVisibility(View.VISIBLE);

                    ChatMessageListResponseDTO chatMessageListResponseDTO = chatMessageDAO.findMessageByUserIds(userId1, userId2);
                    MessageResponseDTO messageResponseDTO = chatMessageListResponseDTO.getResultMessage();
                    if(messageResponseDTO.getCode() != MessageController.SUCCESS_CODE){
                        //TODO
                        showErrorMessage(messageResponseDTO);
                        return;
                    }

                    ArrayList<ElementMessageModel> nextPageElements = new ArrayList<ElementMessageModel>();
                    boolean result = dtoToModel(getActivity(), chatMessageListResponseDTO, nextPageElements);
                    if(!result){
                        showErrorMessage(0);
                        //TODO ERROR
                    }


                    if(nextPageElements == null || nextPageElements.isEmpty()){
                        progressBar_users.setVisibility(View.GONE);
                        return;
                    }

                    elementsMessageModel.addAll(nextPageElements);
                    messagesListAdapter.notifyDataSetChanged();
                }
            }
        });

    }

    public boolean dtoToModel(Context context, ChatMessageResponseDTO dto, ElementMessageModel model){
        model.clear();

        model.setMessage(dto.getMessage());

        Calendar calendar = null;
        try {
            calendar = TimeUtils.toCalendar(dto.getDateOfInput());
        }
        catch (ParseException e) {
            showErrorMessage(0);
            return false;
        }
        model.setTime(calendar);

        if(userId1 == dto.getUserSourceId()) model.setType(ElementMessageModel.CODE_MESSAGE_SENT);
        else model.setType(ElementMessageModel.CODE_MESSAGE_RECEIVED);

        return true;
    }

    public boolean dtoToModel(Context context, ChatMessageListResponseDTO dto, List<ElementMessageModel> model){
        model.clear();

        List<ChatMessageResponseDTO> usersDto = dto.getMessages();

        for(ChatMessageResponseDTO elementDto : usersDto){
            ElementMessageModel elementModel = new ElementMessageModel();
            boolean result = dtoToModel(context, elementDto, elementModel);
            if(!result){
                //TODO ERROR
                return false;
            }
            model.add(elementModel);
        }
        return true;
    }

}
