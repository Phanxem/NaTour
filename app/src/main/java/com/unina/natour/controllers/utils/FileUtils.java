package com.unina.natour.controllers.utils;

import android.util.Log;

import java.io.File;
import java.io.FileFilter;

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

}
