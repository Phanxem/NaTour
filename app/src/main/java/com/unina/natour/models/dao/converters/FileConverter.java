package com.unina.natour.models.dao.converters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class FileConverter {

    public static Bitmap toBitmap(byte[] bytes){
        if(bytes == null || bytes.length <= 0) return null;

        InputStream inputStream = new ByteArrayInputStream(bytes);
        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
        return bitmap;
    }

    public static File toPngFile(Context context, byte[] bytes){
        if(bytes == null || bytes.length <= 0) return null;

        File file = new File(context.getCacheDir(), "file.png");
        final boolean newFile;
        try {
            newFile = file.createNewFile();

            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(bytes);
            fileOutputStream.flush();
            fileOutputStream.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }



    public static File toPngFile(Context context, Bitmap bitmap){

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, byteArrayOutputStream);
        byte[] bitmapBytes = byteArrayOutputStream.toByteArray();


        return toPngFile(context, bitmapBytes);
    }

}
