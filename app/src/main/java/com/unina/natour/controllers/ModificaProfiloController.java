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
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.unina.natour.controllers.exceptionHandler.ExceptionHandler;
import com.unina.natour.views.dialogs.MessageDialog;

import java.io.IOException;

public class ModificaProfiloController {

    private final static String TAG ="ModificaProfiloController";

    public final static int REQUEST_CODE = 01;

    Activity activity;
    MessageDialog messageDialog;

    private Uri imageUri;

    public ModificaProfiloController(Activity activity){
        this.activity = activity;
        this.messageDialog = new MessageDialog(activity);
    }

    public Uri getProfileImage(ActivityResult result){

        if (result == null) {
            //TODO ERROR
            return null;
        }

        if (result.getResultCode() != Activity.RESULT_OK) {
            //TODO ERROR
            return null;
        }

        if (result.getData() == null) {
            Log.e(TAG, "DATA null");
            //TODO ERROR
            return null;
        }

        Uri uri = result.getData().getData();

        if(uri != null) this.imageUri = uri;

        return uri;
    }

    public void setProfileImage(){
        if(imageUri == null){
            return;
        }

        ImageDecoder.Source source = ImageDecoder.createSource(activity.getContentResolver(), imageUri);

        try {
            Bitmap bitmap = ImageDecoder.decodeBitmap(source);
            //POST request al server con il bitmap come argomento
            Log.i(TAG, "immagine impostata");
        }
        catch (IOException e) {
            ExceptionHandler.handleMessageError(messageDialog,e);
        }
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

}
