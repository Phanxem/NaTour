package com.unina.natour.controllers.utils;

import java.util.regex.Pattern;

public class EmailUtils {

    public static boolean isEmail(String email){
        if (email == null) return false;

        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

        Pattern pattern = Pattern.compile(emailRegex);

        return pattern.matcher(email).matches();
    }
}
