package com.unina.natour.controllers;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Log;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.unina.natour.controllers.exceptionHandler.ExceptionHandler;
import com.unina.natour.controllers.exceptionHandler.exceptions.ServerException;
import com.unina.natour.controllers.exceptionHandler.exceptions.subAppException.FailureReadProfileImageException;
import com.unina.natour.controllers.exceptionHandler.exceptions.subAppException.FailureUpdateProfileImageException;
import com.unina.natour.controllers.exceptionHandler.exceptions.subAppException.InvalidProfileImageException;
import com.unina.natour.controllers.exceptionHandler.exceptions.subAppException.NotCompletedUpdateProfileImageException;
import com.unina.natour.dto.MessageDTO;
import com.unina.natour.models.ImpostaImmagineProfiloModel;
import com.unina.natour.models.dao.implementation.UserDAOImpl;
import com.unina.natour.models.dao.interfaces.UserDAO;
import com.unina.natour.views.activities.NaTourActivity;
import com.unina.natour.views.activities.PersonalizzaAccountImmagineActivity;
import com.unina.natour.views.dialogs.MessageDialog;


import java.io.IOException;
import java.util.concurrent.ExecutionException;

@RequiresApi(api = Build.VERSION_CODES.P)
@SuppressLint("LongLogTag")
public class ImmagineProfiloController extends NaTourController{

    public final static int REQUEST_CODE = 01;

    public final static int MIN_HEIGHT = 300;
    public final static int MIN_WIDTH = 300;


    private ActivityResultLauncher<Intent> activityResultLauncherGallery;
    private ActivityResultLauncher<String> activityResultLauncherPermissions;

    private ImpostaImmagineProfiloModel impostaImmagineProfiloModel;

    private UserDAO userDAO;


    public ImmagineProfiloController(NaTourActivity activity){
        super(activity);

        this.impostaImmagineProfiloModel = new ImpostaImmagineProfiloModel();

        this.activityResultLauncherGallery = activity.registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result == null ||
                                result.getResultCode() != Activity.RESULT_OK )
                        {
                            FailureReadProfileImageException exception = new FailureReadProfileImageException();
                            ExceptionHandler.handleMessageError(getMessageDialog(),exception);
                            return;
                        }
                        if(result.getData() == null){
                            return;
                        }

                        Uri uri = result.getData().getData();
                        if(uri == null){
                            FailureReadProfileImageException exception = new FailureReadProfileImageException();
                            ExceptionHandler.handleMessageError(getMessageDialog(),exception);
                            return;
                        }

                        Bitmap bitmap = getBitmap(uri);

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

        this.userDAO = new UserDAOImpl(activity);
    }

    public ImpostaImmagineProfiloModel getImpostaImmagineProfiloModel() {
        return impostaImmagineProfiloModel;
    }


    public Boolean modificaImmagineProfilo(){
        Bitmap profileImage = impostaImmagineProfiloModel.getProfileImage();

        if(!isValidBitmap(profileImage)){
            InvalidProfileImageException exception = new InvalidProfileImageException();
            ExceptionHandler.handleMessageError(getMessageDialog(), exception);
            return false;
        }

        MessageDTO result = null;

        if(profileImage == null){
            result = userDAO.removeProfileImage();
            return true;
        }


        Bitmap resizedProfileImage = resizeBitmap(profileImage, MIN_WIDTH);

        try {
            result = userDAO.updateProfileImage(resizedProfileImage);
        }
        catch (IOException e) {
            FailureUpdateProfileImageException exception = new FailureUpdateProfileImageException(e);
            ExceptionHandler.handleMessageError(getMessageDialog(),exception);
            return false;
        }
        catch (ExecutionException | InterruptedException e) {
            NotCompletedUpdateProfileImageException exception = new NotCompletedUpdateProfileImageException(e);
            ExceptionHandler.handleMessageError(getMessageDialog(),exception);
            return false;
        }
        catch (ServerException e) {
            ExceptionHandler.handleMessageError(getMessageDialog(),e);
            return false;
        }
        if(result == null){
            FailureUpdateProfileImageException exception = new FailureUpdateProfileImageException();
            ExceptionHandler.handleMessageError(getMessageDialog(),exception);
            return false;
        }

        return true;
    }


    public Bitmap getBitmap(Uri uri){
        Bitmap bitmap = null;

        ImageDecoder.Source source = ImageDecoder.createSource(getActivity().getContentResolver(), uri);

        try {
            bitmap = ImageDecoder.decodeBitmap(source);
        }
        catch (IOException e) {
            FailureReadProfileImageException exception = new FailureReadProfileImageException(e);
            ExceptionHandler.handleMessageError(getMessageDialog(),exception);
            return null;
        }

        return bitmap;
    }

    public Bitmap resizeBitmap(Bitmap image, int minSize) {
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


    public void openGallery(){
        if(ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            activityResultLauncherPermissions.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
            return;
        }

        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        activityResultLauncherGallery.launch(intent);
    }

    public void openPersonalizzaAccountImmagineActivity(){
        Intent intent = new Intent(getActivity(), PersonalizzaAccountImmagineActivity.class);
        getActivity().startActivity(intent);
        getActivity().finish();
    }

    public void openPersonalizzaAccountImmagineActivity(boolean isFirstUpdate){
        Intent intent = new Intent(getActivity(), PersonalizzaAccountImmagineActivity.class);
        if(isFirstUpdate) intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        getActivity().startActivity(intent);
        getActivity().finish();
    }


    //VALIDATORs---------------

    public boolean isValidBitmap(Bitmap bitmap){

        if(bitmap == null) return true;

        if((bitmap.getWidth() < MIN_WIDTH || bitmap.getHeight() < MIN_HEIGHT) ) return false;

        return true;
    }



}
