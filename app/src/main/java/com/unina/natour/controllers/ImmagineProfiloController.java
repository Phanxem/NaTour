package com.unina.natour.controllers;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.app.ActivityCompat;

import com.unina.natour.controllers.utils.FileUtils;
import com.unina.natour.dto.response.ResultMessageDTO;
import com.unina.natour.dto.response.composted.GetUserWithImageResponseDTO;
import com.unina.natour.models.ImpostaImmagineProfiloModel;
import com.unina.natour.models.dao.implementation.UserDAOImpl;
import com.unina.natour.models.dao.interfaces.UserDAO;
import com.unina.natour.views.activities.NaTourActivity;
import com.unina.natour.views.activities.PersonalizzaAccountImmagineActivity;


import java.io.IOException;


public class ImmagineProfiloController extends NaTourController{

    public final static int REQUEST_CODE = 01;

    public final static int MIN_HEIGHT = 300;
    public final static int MIN_WIDTH = 300;
    private static final String EXTRA_FIRST_UPDATE = "FIRST_UPDATE";


    private ActivityResultLauncher<Intent> activityResultLauncherGallery;
    private ActivityResultLauncher<String> activityResultLauncherPermissions;

    private ImpostaImmagineProfiloModel impostaImmagineProfiloModel;

    private boolean isFirstUpdate;

    private UserDAO userDAO;


    public ImmagineProfiloController(NaTourActivity activity){
        super(activity);

        this.impostaImmagineProfiloModel = new ImpostaImmagineProfiloModel();

        this.activityResultLauncherGallery = activity.registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result == null || result.getResultCode() != Activity.RESULT_OK )
                        {
                            //FailureReadProfileImageException exception = new FailureReadProfileImageException();
                            //TODO
                            showErrorMessage(0);
                            return;
                        }
                        if(result.getData() == null){
                            return;
                        }

                        Uri uri = result.getData().getData();
                        if(uri == null){
                            //FailureReadProfileImageException exception = new FailureReadProfileImageException();
                            //TODO
                            showErrorMessage(0);
                            return;
                        }

                        Bitmap bitmap = null;
                        try {
                            bitmap = FileUtils.toBitmap(getActivity(),uri);
                        }
                        catch (IOException e) {
                            //TODO
                            showErrorMessage(0);
                            return;
                        }

                        Bitmap resizedBitmap = resizeBitmap(bitmap,MIN_WIDTH);
                        if(!isValidImage(resizedBitmap)){
                            //TODO
                            showErrorMessage(0);
                            return;
                        }

                        impostaImmagineProfiloModel.setProfileImage(bitmap);
                    }
                }
        );

        this.activityResultLauncherPermissions = activity.registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                new ActivityResultCallback<Boolean>() {
                    @Override
                    public void onActivityResult(Boolean result) {
                        if (result) openGallery();
                    }
                }
        );

        this.isFirstUpdate = getActivity().getIntent().getBooleanExtra(EXTRA_FIRST_UPDATE,false);

        this.userDAO = new UserDAOImpl(activity);

        boolean result = initModel();
        if(!result){
            //TODO
            showErrorMessage(0);
            getActivity().finish();
            return;
        }
    }

    public boolean initModel(){
        //TODO this.username = Amplify.Auth.getCurrentUser().getUsername();
        String username = "user";

        GetUserWithImageResponseDTO getUserWithImageResponseDTO = userDAO.getUser(username);
        ResultMessageDTO resultMessageDTO = getUserWithImageResponseDTO.getResultMessage();
        if(resultMessageDTO.getCode() != MessageController.SUCCESS_CODE){
            showErrorMessage(resultMessageDTO);
            return false;
        }

        boolean result = dtoToModel(getUserWithImageResponseDTO, impostaImmagineProfiloModel);
        if(!result){
            //TODO
            showErrorMessage(0);
            return false;
        }
        return true;
    }

    public ImpostaImmagineProfiloModel getModel() {
        return impostaImmagineProfiloModel;
    }


    public Boolean modificaImmagineProfilo(){
        Bitmap profileImage = impostaImmagineProfiloModel.getProfileImage();

        if(profileImage == null){
            ResultMessageDTO resultMessageDTO = userDAO.removeProfileImage();
            if(resultMessageDTO.getCode() != MessageController.SUCCESS_CODE){
                //TODO
                showErrorMessage(0);
                return false;
            }
            return true;
        }

        Bitmap resizedProfileImage = resizeBitmap(profileImage, MIN_WIDTH);

        ResultMessageDTO resultMessageDTO = userDAO.updateProfileImage(resizedProfileImage);
        if(resultMessageDTO.getCode() != MessageController.SUCCESS_CODE){
            //TODO
            showErrorMessage(0);
            return false;
        }

        return true;
    }

    private Bitmap resizeBitmap(Bitmap image, int minSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            height = minSize;
            width = (int) (height * bitmapRatio);

        }
        else {
            width = minSize;
            height = (int) (width / bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    public boolean isFirstUpdate(){
        return isFirstUpdate;
    }




    public boolean isValidImage(Bitmap bitmap){
        if(bitmap == null) return true;

        if((bitmap.getWidth() < MIN_WIDTH || bitmap.getHeight() < MIN_HEIGHT) ) return false;

        return true;
    }


    //---
    public void openGallery(){
        if(ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            activityResultLauncherPermissions.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
            return;
        }

        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        activityResultLauncherGallery.launch(intent);
    }



    public static void openPersonalizzaAccountImmagineActivity(NaTourActivity fromActivity){
        Intent intent = new Intent(fromActivity, PersonalizzaAccountImmagineActivity.class);
        fromActivity.startActivity(intent);
    }

    public static void openPersonalizzaAccountImmagineActivity(NaTourActivity fromActivity, boolean isFirstUpdate){
        if(!isFirstUpdate){
            openPersonalizzaAccountImmagineActivity(fromActivity);
            return;
        }
        Intent intent = new Intent(fromActivity, PersonalizzaAccountImmagineActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(EXTRA_FIRST_UPDATE,true);
        fromActivity.startActivity(intent);
        fromActivity.finish();
    }



    public static boolean dtoToModel(GetUserWithImageResponseDTO user, ImpostaImmagineProfiloModel model){

        model.clear();

        model.setProfileImage(user.getProfileImage());

        return true;

    }


}
