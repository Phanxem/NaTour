package com.unina.natour.controllers.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.util.Log;

import com.unina.natour.controllers.exceptionHandler.exceptions.subAppException.FailureReadProfileImageException;
import com.unina.natour.views.activities.NaTourActivity;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import io.jenetics.jpx.GPX;

public class FileUtils {

    public static final String EXTENSION_PDF = "pdf";
    public static final String EXTENSION_GPX = "gpx";

    public static boolean equalExtension(File file, String extension) {
        String fileExtension = getExtension(file);
        if(fileExtension == null) return false;

        if(fileExtension.equalsIgnoreCase(extension)) return true;

        return false;
    }

    public static String getExtension(File file) {
        String fileName = file.getName();

        int index = fileName.lastIndexOf('.');
        if (index > 0) return fileName.substring(index+1);
        else return null;
    }

    public static FileFilter extensionFileFilter(String extension, String... nextExtensions){
        FileFilter fileFilter = new FileFilter() {
            @Override
            public boolean accept(File file) {
                if(file.isHidden() || !file.canRead()) return false;

                //Log.i("FILE NAME", file.getName());

                if (file.isDirectory()) return true;

                if(equalExtension(file, extension)) return true;

                if(nextExtensions.length > 0){
                    for(String nextExtension : nextExtensions) {
                        if (equalExtension(file, nextExtension)) return true;
                    }
                }
                return false;
            }
        };

        return fileFilter;
    }

    public static Bitmap toBitmap(byte[] bytes){
        if(bytes == null || bytes.length <= 0) return null;

        InputStream inputStream = new ByteArrayInputStream(bytes);
        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
        return bitmap;
    }

    public static File toPngFile(Context context, byte[] bytes) throws IOException {
        if(bytes == null || bytes.length <= 0) return null;

        File file = new File(context.getCacheDir(), "file.png");
        final boolean newFile;

        newFile = file.createNewFile();

        FileOutputStream fileOutputStream = new FileOutputStream(file);
        fileOutputStream.write(bytes);
        fileOutputStream.flush();
        fileOutputStream.close();

        return file;
    }



    public static File toPngFile(Context context, Bitmap bitmap) throws IOException {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, byteArrayOutputStream);
        byte[] bitmapBytes = byteArrayOutputStream.toByteArray();


        return toPngFile(context, bitmapBytes);
    }


    public static File toFile(Context context, GPX gpx) throws IOException {
        File file = new File(context.getCacheDir(), "file.gpx");
        GPX.writer().write(gpx,file);

        return file;
    }


    public static Bitmap toBitmap(Context context, Uri uri) throws IOException {
        Bitmap bitmap = null;

        ImageDecoder.Source source = ImageDecoder.createSource(context.getContentResolver(), uri);
        bitmap = ImageDecoder.decodeBitmap(source);

        return bitmap;
    }




    public static String byteArrayToHex(byte[] a) {
        StringBuilder sb = new StringBuilder(a.length * 2);
        for(byte b: a)
            sb.append(String.format("%02x", b));
        return sb.toString();
    }


}
