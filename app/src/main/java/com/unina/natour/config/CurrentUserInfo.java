package com.unina.natour.config;

public class CurrentUserInfo {

    private static Long id;
    private static String identityProvider;
    private static String idIdentityProvided;

    private static boolean isSignedIn = false;


    public static void set(long id, String identityProvider, String idIdentityProvided){
        CurrentUserInfo.id = id;
        CurrentUserInfo.identityProvider = identityProvider;
        CurrentUserInfo.idIdentityProvided = idIdentityProvided;

        CurrentUserInfo.isSignedIn = true;
    }

    public static void clear(){
        CurrentUserInfo.id = -1l;
        CurrentUserInfo.identityProvider = null;
        CurrentUserInfo.idIdentityProvided = null;

        CurrentUserInfo.isSignedIn = false;
    }

    public static long getId() {
        return id;
    }
    public static String getIdentityProvider() {
        return identityProvider;
    }
    public static String getIdIdentityProvided() {
        return idIdentityProvided;
    }
    public static boolean isSignedIn(){return isSignedIn;}


}
