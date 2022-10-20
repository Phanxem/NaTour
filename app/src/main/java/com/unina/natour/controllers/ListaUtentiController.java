package com.unina.natour.controllers;

import android.content.Context;
import android.view.View;
import android.widget.ProgressBar;

import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.unina.natour.dto.response.ResultMessageDTO;
import com.unina.natour.dto.response.composted.ListUserChatResponseDTO;
import com.unina.natour.dto.response.composted.UserChatResponseDTO;
import com.unina.natour.models.ElementUserModel;
import com.unina.natour.models.dao.implementation.UserDAOImpl;
import com.unina.natour.models.dao.interfaces.UserDAO;
import com.unina.natour.views.activities.NaTourActivity;
import com.unina.natour.views.listAdapters.UserListAdapter;

import java.util.ArrayList;
import java.util.List;

public class ListaUtentiController extends NaTourController{

    public final static long CODE_USER_WITH_CONVERSATION = 0;
    public final static long CODE_USER_BY_RESEARCH = 1;

    private String researchString;
    private long researchCode;

    private UserListAdapter userListAdapter;

    private ArrayList<ElementUserModel> elementsUserModel;
    private int page = 0;

    private UserDAO userDAO;


    public ListaUtentiController(NaTourActivity activity, long researchCode, String researchString) {
        super(activity);

        this.elementsUserModel = new ArrayList<ElementUserModel>();

        this.userDAO = new UserDAOImpl(activity);

        if(researchCode < 0 || researchCode > 1){
            //TODO
            showErrorMessage(0);
            //getActivity().finish();
            return;
        }
        boolean result = initModel(researchCode, researchString);
        if(!result){
            //TODO
            showErrorMessage(0);
            //getActivity().finish();
            return;
        }
    }

    public boolean initModel(long researchCode, String researchString){
        ListUserChatResponseDTO usersDTO = null;

        if(researchCode == CODE_USER_WITH_CONVERSATION){
            usersDTO = userDAO.findUserChatByConversation();
        }
        else if (researchCode == CODE_USER_BY_RESEARCH){
            usersDTO = userDAO.findUserChatByUsername(researchString);
        }
        else{
            //TODO
            showErrorMessage(0);
            return false;
        }

        ResultMessageDTO resultMessageDTO = usersDTO.getResultMessage();
        if(resultMessageDTO.getCode() != MessageController.SUCCESS_CODE){
            //TODO
            showErrorMessage(resultMessageDTO);
            return false;
        }

        this.researchString = researchString;
        this.researchCode = researchCode;


        boolean result = dtoToModel(getActivity(), usersDTO, elementsUserModel);
        if(!result){
            //TODO
            showErrorMessage(0);
            return false;
        }

        this.userListAdapter = new UserListAdapter(getActivity(),elementsUserModel, researchCode);

        return true;
    }

    public void initList(NestedScrollView nestedScrollView_users, RecyclerView recyclerView_users, ProgressBar progressBar_users) {
        recyclerView_users.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView_users.setAdapter(userListAdapter);


        nestedScrollView_users.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                    // in this method we are incrementing page number,
                    // making progress bar visible and calling get data method.
                    page++;
                    progressBar_users.setVisibility(View.VISIBLE);

                    ListUserChatResponseDTO usersDTO;

                    if(researchCode == CODE_USER_WITH_CONVERSATION){
                        usersDTO = userDAO.findUserChatByConversation();
                    }
                    else if (researchCode == CODE_USER_BY_RESEARCH){
                        usersDTO = userDAO.findUserChatByUsername(researchString);
                    }
                    else{
                        //TODO EXCEPTION
                        return;
                    }

                    ArrayList<ElementUserModel> nextPageElements = new ArrayList<ElementUserModel>();
                    boolean result = dtoToModel(getActivity(), usersDTO, nextPageElements);
                    if(!result){
                        //TODO ERROR
                    }


                    if(nextPageElements == null || nextPageElements.isEmpty()){
                        progressBar_users.setVisibility(View.GONE);
                        return;
                    }

                    elementsUserModel.addAll(nextPageElements);
                    userListAdapter.notifyDataSetChanged();
                }
            }
        });

    }

    public void updateList(long researchCode, String searchString) {
        if(researchCode < 0 || researchCode > 1){
            //TODO EXCEPTION
            return;
        }

        elementsUserModel.clear();
        this.researchCode = researchCode;
        this.researchString = searchString;

        initModel(researchCode, searchString);
    }


    public boolean dtoToModel(Context context, UserChatResponseDTO dto, ElementUserModel model){
        model.clear();

        model.setUserId(dto.getId());
        model.setUsername(dto.getNameChat());
        model.setProfileImage(dto.getProfileImage());
        model.setMessagesToRead(dto.hasMessagesToRead());
        return true;
    }

    public boolean dtoToModel(Context context, ListUserChatResponseDTO dto, List<ElementUserModel> model){
        model.clear();

        List<UserChatResponseDTO> usersDto = dto.getUsers();

        for(UserChatResponseDTO elementDto : usersDto){
            ElementUserModel elementModel = new ElementUserModel();
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
