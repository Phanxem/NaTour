package com.unina.natour.controllers;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.unina.natour.R;
import com.unina.natour.config.CurrentUserInfo;
import com.unina.natour.dto.response.composted.GetListChatWithUserResponseDTO;
import com.unina.natour.dto.response.composted.GetChatWithUserResponseDTO;
import com.unina.natour.dto.response.composted.GetListUserWithImageResponseDTO;
import com.unina.natour.dto.response.composted.GetUserWithImageResponseDTO;
import com.unina.natour.models.ElementUserModel;
import com.unina.natour.models.dao.implementation.ChatDAOImpl;
import com.unina.natour.models.dao.implementation.UserDAOImpl;
import com.unina.natour.models.dao.interfaces.ChatDAO;
import com.unina.natour.models.dao.interfaces.UserDAO;
import com.unina.natour.views.activities.NaTourActivity;
import com.unina.natour.views.listAdapters.UserListAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ListaUtentiController extends NaTourController{

    public final static long CODE_USER_WITH_CONVERSATION = 0;
    public final static long CODE_USER_BY_RESEARCH = 1;

    private String researchString = null;
    private long researchCode = -1;

    private UserListAdapter userListAdapter;

    private ArrayList<ElementUserModel> elementsUserModel;

    private UserDAO userDAO;
    private ChatDAO chatDAO;

    public ListaUtentiController(NaTourActivity activity,
                                 ResultMessageController resultMessageController,
                                 UserListAdapter userListAdapter,
                                 ArrayList<ElementUserModel> elementsUserModel,
                                 UserDAO userDAO,
                                 ChatDAO chatDAO,
                                 long researchCode,
                                 String researchString) {
        super(activity, resultMessageController);

        this.elementsUserModel = elementsUserModel;

        this.userDAO = userDAO;

        this.chatDAO = chatDAO;

        this.userListAdapter = userListAdapter;

        this.researchCode = researchCode;
        this. researchString = researchString;
    }

    public ListaUtentiController(NaTourActivity activity, long researchCode, String researchString) {
        super(activity);
        String messageToShow = null;


        this.elementsUserModel = new ArrayList<ElementUserModel>();

        this.userDAO = new UserDAOImpl(activity);

        this.chatDAO = new ChatDAOImpl(activity);

        if(researchCode < 0 || researchCode > 1){
            messageToShow = activity.getString(R.string.Message_UnknownError);
            showErrorMessageAndBack(messageToShow);
            return;
        }
        /*
        boolean result = initModel(researchCode, researchString);
        if(!result){
            return;
        }

        */
    }

    public boolean initModel(long researchCode, String researchString){
        Activity activity = getActivity();
        String messageToShow = null;

        if(!CurrentUserInfo.isSignedIn()){
            messageToShow = activity.getString(R.string.Message_UnknownError);
            showErrorMessageAndBack(messageToShow);
            return false;
        }

        boolean result = false;

        if(researchCode == CODE_USER_WITH_CONVERSATION){
            GetListChatWithUserResponseDTO getListChatWithUserResponseDTO = chatDAO.getListChatByIdUser(CurrentUserInfo.getId(), 0);

            if(!ResultMessageController.isSuccess(getListChatWithUserResponseDTO.getResultMessage())){
                messageToShow = activity.getString(R.string.Message_UnknownError);
                showErrorMessageAndBack(messageToShow);
                return false;
            }

            result = dtoToModel(getActivity(), getListChatWithUserResponseDTO, elementsUserModel);
            if(!result){
                messageToShow = activity.getString(R.string.Message_UnknownError);
                showErrorMessageAndBack(messageToShow);
                return false;
            }

        }
        else if (researchCode == CODE_USER_BY_RESEARCH){
            GetListUserWithImageResponseDTO getListUserWithImageResponseDTO = userDAO.getListUserWithImageByUsername(researchString, 0);

            if(!ResultMessageController.isSuccess(getListUserWithImageResponseDTO.getResultMessage())){
                messageToShow = activity.getString(R.string.Message_UnknownError);
                showErrorMessageAndBack(messageToShow);
                return false;
            }

            List<GetUserWithImageResponseDTO> listUser = new ArrayList<GetUserWithImageResponseDTO>();
            for(GetUserWithImageResponseDTO elementUser : getListUserWithImageResponseDTO.getListUser()){
                if(elementUser.getId() != CurrentUserInfo.getId()){
                    listUser.add(elementUser);
                }
            }

            getListUserWithImageResponseDTO.setListUser(listUser);

            result = dtoToModel(getActivity(), getListUserWithImageResponseDTO, elementsUserModel);
            if(!result){
                messageToShow = activity.getString(R.string.Message_UnknownError);
                showErrorMessageAndBack(messageToShow);
                return false;
            }
        }
        else{
            messageToShow = activity.getString(R.string.Message_UnknownError);
            showErrorMessageAndBack(messageToShow);
            return false;
        }


        this.researchString = researchString;
        this.researchCode = researchCode;

        this.userListAdapter = new UserListAdapter(getActivity(), elementsUserModel, researchCode);

        return true;
    }

    public void notifyDataSetChanged(){
        userListAdapter.notifyDataSetChanged();
    }

    public void initList(NestedScrollView nestedScrollView_users, RecyclerView recyclerView_users, ProgressBar progressBar_users) {
        Activity activity = getActivity();
        final String[] messageToShow = {null};

        recyclerView_users.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView_users.setAdapter(userListAdapter);


        nestedScrollView_users.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            int page = 0;

            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                    page++;
                    progressBar_users.setVisibility(View.VISIBLE);

                    GetListChatWithUserResponseDTO usersDTO;

                    boolean result = false;

                    if(researchCode == CODE_USER_WITH_CONVERSATION){
                        GetListChatWithUserResponseDTO getListChatWithUserResponseDTO = chatDAO.getListChatByIdUser(CurrentUserInfo.getId(), page);

                        if(!ResultMessageController.isSuccess(getListChatWithUserResponseDTO.getResultMessage())){
                            messageToShow[0] = activity.getString(R.string.Message_UnknownError);
                            showErrorMessage(messageToShow[0]);
                            return ;
                        }

                        List<GetChatWithUserResponseDTO> listChat = getListChatWithUserResponseDTO.getListChat();
                        if(listChat == null || listChat.isEmpty()){
                            progressBar_users.setVisibility(View.GONE);
                            return;
                        }

                        result = dtoToModel(getActivity(), getListChatWithUserResponseDTO, elementsUserModel);
                        if(!result){
                            messageToShow[0] = activity.getString(R.string.Message_UnknownError);
                            showErrorMessageAndBack(messageToShow[0]);
                            return;
                        }
                    }
                    else if (researchCode == CODE_USER_BY_RESEARCH){
                        GetListUserWithImageResponseDTO getListUserWithImageResponseDTO = userDAO.getListUserWithImageByUsername(researchString, page);

                        if(!ResultMessageController.isSuccess(getListUserWithImageResponseDTO.getResultMessage())){
                            messageToShow[0] = activity.getString(R.string.Message_UnknownError);
                            showErrorMessageAndBack(messageToShow[0]);
                            return;
                        }

                        List<GetUserWithImageResponseDTO> listUser = getListUserWithImageResponseDTO.getListUser();
                        if(listUser == null || listUser.isEmpty()){
                            progressBar_users.setVisibility(View.GONE);
                            return;
                        }

                        result = dtoToModel(getActivity(), getListUserWithImageResponseDTO, elementsUserModel);
                        if(!result){
                            messageToShow[0] = activity.getString(R.string.Message_UnknownError);
                            showErrorMessageAndBack(messageToShow[0]);
                            return;
                        }
                    }
                    else{
                        messageToShow[0] = activity.getString(R.string.Message_UnknownError);
                        showErrorMessageAndBack(messageToShow[0]);
                        return;
                    }

                    ArrayList<ElementUserModel> nextPageElements = new ArrayList<ElementUserModel>();



                    elementsUserModel.addAll(nextPageElements);
                    userListAdapter.notifyDataSetChanged();
                }
            }
        });

    }

    public boolean updateList(long researchCode, String searchString) {
        Activity activity = getActivity();
        String messageToShow = null;

        if(researchCode < 0 || researchCode > 1){
            messageToShow = activity.getString(R.string.Message_UnknownError);
            showErrorMessage(messageToShow);
            return false;
        }

        elementsUserModel.clear();
        this.researchCode = researchCode;
        this.researchString = searchString;

        initModel(researchCode, searchString);

        return true;
    }

    public void receiveMessage(long idUserSender){
        ElementUserModel tempUser = null;

        tempUser = elementsUserModel.get(0);
        if(tempUser.getUserId() == idUserSender){
            Log.e(TAG, "1");
            tempUser.setMessagesToRead(true);
            userListAdapter.notifyDataSetChanged();
            return;
        }

        for(int i = 1; i < elementsUserModel.size(); i++){
            tempUser = elementsUserModel.get(i);
            if(tempUser.getUserId() == idUserSender){
                Log.e("ChatWebSocketListener", ""+i);
                elementsUserModel.remove(i);
                tempUser.setMessagesToRead(true);
                elementsUserModel.add(0,tempUser);
                userListAdapter.notifyDataSetChanged();
                return;
            }
        }

        tempUser = findUserById(idUserSender);
        if(tempUser == null){
            return;
        }

        Log.e("ChatWebSocketListener", "new");

        tempUser.setMessagesToRead(true);
        elementsUserModel.add(0,tempUser);
        userListAdapter.notifyDataSetChanged();
    }

    public long getResearchCode() {
        return researchCode;
    }

    public ElementUserModel findUserById(long idSource) {
        Activity activity = getActivity();
        String messageToShow = null;

        GetUserWithImageResponseDTO getUserWithImageResponseDTO = userDAO.getUserWithImageById(idSource);
        if(!ResultMessageController.isSuccess(getUserWithImageResponseDTO.getResultMessage())){
            messageToShow = activity.getString(R.string.Message_UnknownError);
            showErrorMessageAndBack(messageToShow);
            return null;
        }

        ElementUserModel elementUserModel = new ElementUserModel();
        boolean result = dtoToModel(activity, getUserWithImageResponseDTO, elementUserModel);
        if(!result){
            messageToShow = activity.getString(R.string.Message_UnknownError);
            showErrorMessageAndBack(messageToShow);
            return null;
        }

        return elementUserModel;
    }

    public ArrayList<ElementUserModel> getElementsUserModel() {
        return elementsUserModel;
    }

    public boolean dtoToModel(Context context, GetChatWithUserResponseDTO dto, ElementUserModel model){
        model.clear();



        model.setUserId(dto.getIdUser());
        model.setUsername(dto.getNameChat());
        model.setProfileImage(dto.getProfileImage());
        model.setMessagesToRead(dto.hasMessagesToRead());

        return true;
    }

    public boolean dtoToModel(Context context, GetListChatWithUserResponseDTO dto, List<ElementUserModel> model){
        Activity activity = getActivity();
        String messageToShow = null;

        model.clear();

        List<GetChatWithUserResponseDTO> usersDto = dto.getListChat();

        for(GetChatWithUserResponseDTO elementDto : usersDto){
            ElementUserModel elementModel = new ElementUserModel();
            boolean result = dtoToModel(context, elementDto, elementModel);
            if(!result){
                messageToShow = activity.getString(R.string.Message_UnknownError);
                showErrorMessageAndBack(messageToShow);
                return false;
            }
            model.add(elementModel);
        }
        return true;
    }


    public boolean dtoToModel(Context context, GetUserWithImageResponseDTO dto, ElementUserModel model){
        model.clear();

        model.setUserId(dto.getId());
        model.setUsername(dto.getUsername());
        model.setProfileImage(dto.getProfileImage());
        model.setMessagesToRead(false);
        return true;
    }

    public boolean dtoToModel(Context context, GetListUserWithImageResponseDTO dto, List<ElementUserModel> model){
        Activity activity = getActivity();
        String messageToShow = null;

        model.clear();

        List<GetUserWithImageResponseDTO> usersDto = dto.getListUser();

        for(GetUserWithImageResponseDTO elementDto : usersDto){

            ElementUserModel elementModel = new ElementUserModel();
            boolean result = dtoToModel(context, elementDto, elementModel);
            if(!result){
                messageToShow = activity.getString(R.string.Message_UnknownError);
                showErrorMessageAndBack(messageToShow);
                return false;
            }
            model.add(elementModel);
        }
        return true;
    }

}
