package com.unina.natour.controllers;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;

import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.unina.natour.R;
import com.unina.natour.config.CurrentUserInfo;
import com.unina.natour.controllers.utils.TimeUtils;
import com.unina.natour.dto.response.GetListChatMessageResponseDTO;
import com.unina.natour.dto.response.GetChatMessageResponseDTO;
import com.unina.natour.models.ElementMessageModel;
import com.unina.natour.models.dao.implementation.ChatDAOImpl;
import com.unina.natour.models.dao.interfaces.ChatDAO;
import com.unina.natour.views.activities.NaTourActivity;
import com.unina.natour.views.listAdapters.MessagesListAdapter;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ListaMessaggiController extends  NaTourController{

    //private long myUserId;
    private long idUserDestination;

    private MessagesListAdapter messagesListAdapter;

    private ArrayList<ElementMessageModel> elementsMessageModel;

    private ChatDAO chatDAO;

    public ListaMessaggiController(NaTourActivity activity, long idUserDestination){
        super(activity);
        String messageToShow = null;

        this.idUserDestination = idUserDestination;

        this.elementsMessageModel = new ArrayList<ElementMessageModel>();

        this.chatDAO = new ChatDAOImpl(activity);

        boolean result = initModel();
        if(!result){
            return;
        }

    }

    private boolean initModel() {
        Activity activity = getActivity();
        String messageToShow = null;

        if(CurrentUserInfo.isGuest()){
            messageToShow = activity.getString(R.string.Message_UnknownError);
            showErrorMessageAndBack(messageToShow);
            return false;
        }

        GetListChatMessageResponseDTO getListChatMessageResponseDTO = chatDAO.getMessageByIdsUser(CurrentUserInfo.getId(), idUserDestination, 0);
        if(!ResultMessageController.isSuccess(getListChatMessageResponseDTO.getResultMessage())){
            messageToShow = activity.getString(R.string.Message_UnknownError);
            showErrorMessageAndBack(messageToShow);
            return false;
        }

        boolean result = dtoToModel(getActivity(), getListChatMessageResponseDTO, elementsMessageModel);
        if(!result){
            messageToShow = activity.getString(R.string.Message_UnknownError);
            showErrorMessageAndBack(messageToShow);
            return false;
        }

        this.messagesListAdapter = new MessagesListAdapter(getActivity(),elementsMessageModel);
        messagesListAdapter.notifyDataSetChanged();

        return true;
    }

    public void initList(NestedScrollView nestedScrollView_messages, RecyclerView recyclerView_messages, ProgressBar progressBar_messages) {
        Activity activity = getActivity();
        final String[] messageToShow = {null};

        recyclerView_messages.setAdapter(messagesListAdapter);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setReverseLayout(true);

        recyclerView_messages.setLayoutManager(manager);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                nestedScrollView_messages.fullScroll(View.FOCUS_DOWN);
            }
        },
        350);


        nestedScrollView_messages.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            int page = 0;

            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY < oldScrollY && scrollY ==  0) {
                    // in this method we are incrementing page number,
                    // making progress bar visible and calling get data method.
                    page++;
                    progressBar_messages.setVisibility(View.VISIBLE);

                    GetListChatMessageResponseDTO getListChatMessageResponseDTO = chatDAO.getMessageByIdsUser(CurrentUserInfo.getId(), idUserDestination, page);
                    if(!ResultMessageController.isSuccess(getListChatMessageResponseDTO.getResultMessage())){
                        messageToShow[0] = activity.getString(R.string.Message_UnknownError);
                        showErrorMessage(messageToShow[0]);
                        return;
                    }

                    ArrayList<ElementMessageModel> nextPageElements = new ArrayList<ElementMessageModel>();
                    boolean result = dtoToModel(getActivity(), getListChatMessageResponseDTO, nextPageElements);
                    if(!result){
                        messageToShow[0] = activity.getString(R.string.Message_UnknownError);
                        showErrorMessage(messageToShow[0]);
                        return;
                    }

                    if(nextPageElements == null || nextPageElements.isEmpty()){
                        progressBar_messages.setVisibility(View.GONE);
                        return;
                    }

                    int oldHeight = v.getChildAt(0).getMeasuredHeight();

                    elementsMessageModel.addAll(nextPageElements);
                    messagesListAdapter.notifyDataSetChanged();

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            int newHeight = v.getChildAt(0).getMeasuredHeight();
                            int oldFirstElementHeight = newHeight - oldHeight;
                            v.scrollTo(scrollX, oldFirstElementHeight);
                        }
                    },
                    350);
                }
            }
        });

    }


    public void addMessageSent(String message, Calendar dateOfInput){
        ElementMessageModel messageModel = new ElementMessageModel();
        messageModel.setMessage(message);
        messageModel.setTime(dateOfInput);
        messageModel.setType(ElementMessageModel.CODE_MESSAGE_SENT);

        elementsMessageModel.add(0,messageModel);
        messagesListAdapter.notifyDataSetChanged();

    }

    public void addMessageReceived(String message, Calendar dateOfInput){
        ElementMessageModel messageModel = new ElementMessageModel();
        messageModel.setMessage(message);
        messageModel.setTime(dateOfInput);
        messageModel.setType(ElementMessageModel.CODE_MESSAGE_RECEIVED);

        elementsMessageModel.add(0,messageModel);
        messagesListAdapter.notifyDataSetChanged();

    }


    public boolean dtoToModel(Context context, GetChatMessageResponseDTO dto, ElementMessageModel model){
        Activity activity = getActivity();
        String messageToShow = null;

        model.clear();
        model.setMessage(dto.getBody());

        Calendar calendar = null;
        try {
            calendar = TimeUtils.toCalendar(dto.getDateOfInput());
        }
        catch (ParseException e) {
            messageToShow = activity.getString(R.string.Message_UnknownError);
            showErrorMessage(messageToShow);
            return false;
        }
        model.setTime(calendar);

        if(CurrentUserInfo.getId() == dto.getIdUser()) model.setType(ElementMessageModel.CODE_MESSAGE_SENT);
        else model.setType(ElementMessageModel.CODE_MESSAGE_RECEIVED);

        return true;
    }

    public boolean dtoToModel(Context context, GetListChatMessageResponseDTO dto, List<ElementMessageModel> model){
        model.clear();

        List<GetChatMessageResponseDTO> usersDto = dto.getListMessage();

        for(GetChatMessageResponseDTO elementDto : usersDto){
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
