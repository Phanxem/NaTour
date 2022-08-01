package com.unina.natour.models.dao.jsonConverter;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.google.gson.JsonObject;
import com.unina.natour.dto.MessageDTO;

import org.apache.commons.io.FileUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class JsonConverter {

    private static final String TAG = "ASF";

    public static File toFile(Bitmap bitmap){
        //Convert bitmap to byte array
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, byteArrayOutputStream);
        byte[] byteArrayBitmap = byteArrayOutputStream.toByteArray();

        File file = new File ("image.png");


        try {
            FileUtils.writeByteArrayToFile(file,byteArrayBitmap);
        } catch (IOException e) {
            Log.e(TAG, "error");
            e.printStackTrace();
        }


        return file;

    }

    public static File toFile2(Context context, Bitmap bitmap){
        //create a file to write bitmap data
        File f = new File(context.getCacheDir(), "bitmap.png");
        final boolean newFile;
        try {
            newFile = f.createNewFile();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        //Convert bitmap to byte array
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
        byte[] bitmapdata = bos.toByteArray();

        //write the bytes in file
        try {
            FileOutputStream fos = new FileOutputStream(f);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return f;
    }

    public static MessageDTO toMessageDTO(JsonObject jsonObjectResult) {

        if(!jsonObjectResult.has("code")  || !jsonObjectResult.has("message")){
            //TODO EXCEPTION
            return null;
        }

        long code = jsonObjectResult.get("code").getAsLong();
        String message = jsonObjectResult.get("message").getAsString();

        MessageDTO messageDTO = new MessageDTO(code,message);

        return messageDTO;
    }
/*
    public static UserDTO toUserDTO(JsonObject jsonObject){
        if(!jsonObject.has("id") ||
           !jsonObject.has("username") ||
           !jsonObject.has("profileImage") ||
           !jsonObject.has("placeOfResidence") ||
           !jsonObject.has("dateOfBirth") ||
           !jsonObject.has("gender") )
        {
            return null;
        }

        UserDTO userDTO = new UserDTO();

        userDTO.setId(jsonObject.get("id").getAsLong());
        userDTO.setUsername(jsonObject.get("username").getAsString());

        JsonArray jsonArray = jsonObject.get("profileImage").getAsJsonArray();


        //userDTO.setProfileImage(jsonObject.get("profileImage").getAsB);
    }
*/


}
