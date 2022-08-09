package com.unina.natour.controllers;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.core.app.ActivityCompat;

import com.unina.natour.controllers.exceptionHandler.ExceptionHandler;
import com.unina.natour.controllers.exceptionHandler.clientException.ClientException;
import com.unina.natour.dto.MessageDTO;
import com.unina.natour.models.dao.classes.UserDAOImpl;
import com.unina.natour.models.dao.interfaces.UserDAO;
import com.unina.natour.views.activities.PersonalizzaAccountImmagineActivity;
import com.unina.natour.views.dialogs.MessageDialog;

import com.unina.natour.controllers.*;


import java.io.IOException;

public class ImpostaImmagineProfiloController {

    private final static String TAG ="ImpostaImmagineProfiloController";

    public final static int REQUEST_CODE = 01;

    public final static int MIN_HEIGHT = 300;
    public final static int MIN_WIDTH = 300;

    Activity activity;
    MessageDialog messageDialog;

    ImpostaInfoOpzionaliProfiloController impostaInfoOpzionaliProfiloController;

    private Bitmap profileImage;

    private UserDAO userDAO;

    public ImpostaImmagineProfiloController(Activity activity){
        this.activity = activity;
        this.messageDialog = new MessageDialog(activity);

        impostaInfoOpzionaliProfiloController = new ImpostaInfoOpzionaliProfiloController(activity);

        this.userDAO = new UserDAOImpl(activity);
    }

    public Bitmap getProfileImage(ActivityResult result){

        if (result == null) {
            ExceptionHandler.handleMessageError(messageDialog,new ClientException());
            return null;
        }

        if (result.getResultCode() != Activity.RESULT_OK) {
            ExceptionHandler.handleMessageError(messageDialog,new ClientException());
            return null;
        }

        if (result.getData() == null) {
            Log.e(TAG, "DATA null");
            ExceptionHandler.handleMessageError(messageDialog,new ClientException());
            return null;
        }

        Uri uri = result.getData().getData();

        if(uri == null){
            ExceptionHandler.handleMessageError(messageDialog,new ClientException());
            return null;
        }

        ImageDecoder.Source source = ImageDecoder.createSource(activity.getContentResolver(), uri);
        Bitmap bitmap = null;
        try {
            bitmap = ImageDecoder.decodeBitmap(source);
            this.profileImage = bitmap;
        } catch (IOException e) {
            ExceptionHandler.handleMessageError(messageDialog,e);
        }

        return bitmap;
    }

    public void modificaImmagineProfilo(){
        if(profileImage == null){
            return;
        }

        if(!isValidBitmap(profileImage)){
            ExceptionHandler.handleMessageError(messageDialog,new ClientException());
            Log.e(TAG, "immagein non valida");
            return;
        }

        Bitmap resizedProfileImage = resizeBitmap(profileImage, MIN_WIDTH);

        MessageDTO result = userDAO.updateProfileImage(resizedProfileImage);
        if(result == null) {
            ExceptionHandler.handleMessageError(messageDialog,new ClientException());
            Log.e(TAG, "ERRORE");
        }

        Log.i(TAG, "immagine impostata");
        impostaInfoOpzionaliProfiloController.openPersonalizzaAccountInfoOpzionaliActivity();

    }


    public Bitmap resizeBitmap(Bitmap image, int minSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            height = minSize;
            width = (int) (height * bitmapRatio);

        } else {
            width = minSize;
            height = (int) (width / bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }



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


    //VALIDATORs---------------

    public boolean isValidBitmap(Bitmap bitmap){

        if( profileImage != null && (bitmap.getWidth() < MIN_WIDTH || bitmap.getHeight() < MIN_HEIGHT) ){
            ExceptionHandler.handleMessageError(messageDialog,new ClientException());
            return false;
        }

        return true;
    }


}
