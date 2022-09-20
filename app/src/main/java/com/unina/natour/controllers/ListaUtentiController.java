package com.unina.natour.controllers;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.unina.natour.R;
import com.unina.natour.dto.response.UserListResponseDTO;
import com.unina.natour.dto.response.UserResponseDTO;
import com.unina.natour.models.ElementItineraryModel;
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
        UserListResponseDTO usersDTO = null;

        if(researchCode == CODE_USER_WITH_CONVERSATION){
            usersDTO = userDAO.getUserWithConversation();
        }
        else if (researchCode == CODE_USER_BY_RESEARCH){
            usersDTO = userDAO.getUserByUsername(researchString);
        }
        else{
            //TODO
            showErrorMessage(0);
            return false;
        }

        this.researchString = researchString;
        this.researchCode = researchCode;


        boolean result = dtoToModel(getActivity(), usersDTO, elementsUserModel);
        if(!result){
            //TODO
            showErrorMessage(0);
            getActivity().finish();
            return false;
        }

        this.userListAdapter = new UserListAdapter(getActivity(),elementsUserModel);

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

                    UserListResponseDTO usersDTO;

                    if(researchCode == CODE_USER_WITH_CONVERSATION){
                        usersDTO = userDAO.getUserWithConversation();
                    }
                    else if (researchCode == CODE_USER_BY_RESEARCH){
                        usersDTO = userDAO.getUserByUsername(researchString);
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
        researchCode = researchCode;
        researchString = searchString;

        initModel(researchCode, searchString);
    }


    public boolean dtoToModel(Context context, UserResponseDTO dto, ElementUserModel model){
        model.setUserId(dto.getId());
        model.setUsername(dto.getUsername());
        model.setProfileImage(dto.getProfileImage());
        return true;
    }

    public boolean dtoToModel(Context context, UserListResponseDTO dto, List<ElementUserModel> model){
        model.clear();

        List<UserResponseDTO> usersDto = dto.getUsers();

        for(UserResponseDTO elementDto : usersDto){
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
