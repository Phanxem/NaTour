package com.unina.natour.config;

public class CurrentUserInfo {

    private static Long id;
    private static String identityProvider;
    private static String idIdentityProvided;

    private static boolean isGuest = true;


    public static void set(long id, String identityProvider, String idIdentityProvided){
        CurrentUserInfo.id = id;
        CurrentUserInfo.identityProvider = identityProvider;
        CurrentUserInfo.idIdentityProvided = idIdentityProvided;

        CurrentUserInfo.isGuest = false;
    }

    public static void clear(){
        CurrentUserInfo.id = null;
        CurrentUserInfo.identityProvider = null;
        CurrentUserInfo.idIdentityProvided = null;

        CurrentUserInfo.isGuest = true;
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
    public static boolean isGuest(){return isGuest;}


}
