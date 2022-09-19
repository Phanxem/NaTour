package com.unina.natour.controllers.utils;

public class StringsUtils {

    public static boolean areAllFieldsFull(String firstString, String... strings){
        if(firstString == null || firstString.isEmpty()) return false;

        for(String string : strings){
            if(string == null || string.isEmpty()) return false;
        }
        return true;
    }

}
