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
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

import com.unina.natour.controllers.exceptionHandler.ExceptionHandler;
import com.unina.natour.controllers.exceptionHandler.exceptions.ServerException;
import com.unina.natour.controllers.exceptionHandler.exceptions.subAppException.FailureReadProfileImageException;
import com.unina.natour.controllers.exceptionHandler.exceptions.subAppException.FailureUpdateProfileImageException;
import com.unina.natour.controllers.exceptionHandler.exceptions.subAppException.InvalidProfileImageException;
import com.unina.natour.controllers.exceptionHandler.exceptions.subAppException.NotCompletedUpdateProfileImageException;
import com.unina.natour.dto.MessageDTO;
import com.unina.natour.models.dao.implementation.UserDAOImpl;
import com.unina.natour.models.dao.interfaces.UserDAO;
import com.unina.natour.views.activities.PersonalizzaAccountImmagineActivity;
import com.unina.natour.views.dialogs.MessageDialog;


import java.io.IOException;
import java.util.concurrent.ExecutionException;

@RequiresApi(api = Build.VERSION_CODES.N)
@SuppressLint("LongLogTag")
public class ImpostaImmagineProfiloController {

    private final static String TAG ="ImpostaImmagineProfiloController";

    public final static int REQUEST_CODE = 01;

    public final static int MIN_HEIGHT = 300;
    public final static int MIN_WIDTH = 300;

    Activity activity;
    MessageDialog messageDialog;

    private Bitmap profileImage;

    private UserDAO userDAO;


    public ImpostaImmagineProfiloController(Activity activity, MessageDialog messageDialog){
        this.activity = activity;
        this.messageDialog = messageDialog;

        this.userDAO = new UserDAOImpl(activity);
    }

    public MessageDialog getMessageDialog() {
        return messageDialog;
    }



    @RequiresApi(api = Build.VERSION_CODES.P)
    public Bitmap getProfileImage(ActivityResult result){

        if (result == null ||
            result.getResultCode() != Activity.RESULT_OK )
        {
            FailureReadProfileImageException exception = new FailureReadProfileImageException();
            ExceptionHandler.handleMessageError(messageDialog,exception);
            return null;
        }
        if(result.getData() == null){
            return null;
        }


        Uri uri = result.getData().getData();

        if(uri == null){
            FailureReadProfileImageException exception = new FailureReadProfileImageException();
            ExceptionHandler.handleMessageError(messageDialog,exception);
            return null;
        }

        ImageDecoder.Source source = ImageDecoder.createSource(activity.getContentResolver(), uri);
        Bitmap bitmap = null;
        try {
            bitmap = ImageDecoder.decodeBitmap(source);
            this.profileImage = bitmap;
        }
        catch (IOException e) {
            FailureReadProfileImageException exception = new FailureReadProfileImageException(e);
            ExceptionHandler.handleMessageError(messageDialog,exception);
            return null;
        }

        return bitmap;
    }


    public Boolean modificaImmagineProfilo(){
        if(!isValidBitmap(profileImage)){
            InvalidProfileImageException exception = new InvalidProfileImageException();
            ExceptionHandler.handleMessageError(messageDialog, exception);
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
            ExceptionHandler.handleMessageError(messageDialog,exception);
            return false;
        }
        catch (ExecutionException | InterruptedException e) {
            NotCompletedUpdateProfileImageException exception = new NotCompletedUpdateProfileImageException(e);
            ExceptionHandler.handleMessageError(messageDialog,exception);
            return false;
        }
        catch (ServerException e) {
            ExceptionHandler.handleMessageError(messageDialog,e);
            return false;
        }
        if(result == null){
            FailureUpdateProfileImageException exception = new FailureUpdateProfileImageException();
            ExceptionHandler.handleMessageError(messageDialog,exception);
            return false;
        }

        return true;
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



    //TODO da aggiornare
    public void openGallery(ActivityResultLauncher<Intent> startForResult){
        if(ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            Log.e(TAG, "permesso non dato");
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE);
            return;
        }

        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startForResult.launch(intent);
    }


    public void openPersonalizzaAccountImmagineActivity(){
        Intent intent = new Intent(activity, PersonalizzaAccountImmagineActivity.class);
        activity.startActivity(intent);
        activity.finish();
    }

    public void openPersonalizzaAccountImmagineActivity(boolean isFirstUpdate){
        Intent intent = new Intent(activity, PersonalizzaAccountImmagineActivity.class);
        if(isFirstUpdate) intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        activity.startActivity(intent);
        activity.finish();
    }


    //VALIDATORs---------------

    public boolean isValidBitmap(Bitmap bitmap){

        if(profileImage == null) return true;

        if((bitmap.getWidth() < MIN_WIDTH || bitmap.getHeight() < MIN_HEIGHT) ) return false;

        return true;
    }


}
