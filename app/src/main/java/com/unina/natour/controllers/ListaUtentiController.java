package com.unina.natour.controllers;

import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.unina.natour.controllers.exceptionHandler.ExceptionHandler;
import com.unina.natour.controllers.exceptionHandler.exceptions.ServerException;
import com.unina.natour.controllers.exceptionHandler.exceptions.subAppException.FailureGetUserProfileImageException;
import com.unina.natour.controllers.exceptionHandler.exceptions.subAppException.NotCompletedGetUserProfileImageException;
import com.unina.natour.controllers.utils.TimeUtils;
import com.unina.natour.dto.UserDTO;
import com.unina.natour.dto.response.ElementItineraryResponseDTO;
import com.unina.natour.models.ElementItineraryModel;
import com.unina.natour.models.ElementUserModel;
import com.unina.natour.models.dao.implementation.UserDAOImpl;
import com.unina.natour.models.dao.interfaces.ItineraryDAO;
import com.unina.natour.models.dao.interfaces.UserDAO;
import com.unina.natour.views.activities.NaTourActivity;
import com.unina.natour.views.listAdapters.ItineraryListAdapter;
import com.unina.natour.views.listAdapters.UserListAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

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
            //TODO EXCEPTION
            return;
        }
        initModel(researchCode, researchString);
    }

    public void initModel(long researchCode, String researchString){
        List<UserDTO> usersDTO;

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

        this.researchString = researchString;
        this.researchCode = researchCode;


        for(UserDTO element: usersDTO){
            Bitmap profileImage = null;
            try {
                profileImage = userDAO.getUserProfileImage(element.getUsername());
            } catch (ExecutionException | InterruptedException e) {
                NotCompletedGetUserProfileImageException exception = new NotCompletedGetUserProfileImageException(e);
                ExceptionHandler.handleMessageError(getMessageDialog(), exception);
                return;
            } catch (ServerException e) {
                ExceptionHandler.handleMessageError(getMessageDialog(), e);
                return;
            } catch (IOException e) {
                FailureGetUserProfileImageException exception = new FailureGetUserProfileImageException(e);
                ExceptionHandler.handleMessageError(getMessageDialog(), exception);
                return;
            }

            ElementUserModel modelElement = toModel(element, profileImage);
            elementsUserModel.add(modelElement);
        }

        this.userListAdapter = new UserListAdapter(getActivity(),elementsUserModel);
    }

    public void initList(RecyclerView recyclerView_users, NestedScrollView nestedScrollView_users, ProgressBar progressBar_users) {
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

                    List<UserDTO> usersDTO;

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
                    for(UserDTO element: usersDTO){

                        Bitmap profileImage = null;
                        try {
                            profileImage = userDAO.getUserProfileImage(element.getUsername());
                        } catch (ExecutionException | InterruptedException e) {
                            NotCompletedGetUserProfileImageException exception = new NotCompletedGetUserProfileImageException(e);
                            ExceptionHandler.handleMessageError(getMessageDialog(), exception);
                            return;
                        } catch (ServerException e) {
                            ExceptionHandler.handleMessageError(getMessageDialog(), e);
                            return;
                        } catch (IOException e) {
                            FailureGetUserProfileImageException exception = new FailureGetUserProfileImageException(e);
                            ExceptionHandler.handleMessageError(getMessageDialog(), exception);
                            return;
                        }

                        ElementUserModel modelElement = toModel(element, profileImage);
                        nextPageElements.add(modelElement);
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

    public ElementUserModel toModel(UserDTO dto, Bitmap profileImage){
        ElementUserModel model = new ElementUserModel();

        model.setUsername(dto.getUsername());
        model.setProfileImage(profileImage);

        return model;
    }

}
