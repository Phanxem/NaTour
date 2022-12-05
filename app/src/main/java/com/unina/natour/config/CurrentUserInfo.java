package com.unina.natour.config;

import com.amazonaws.auth.AWSCredentials;

public class CurrentUserInfo {

    private static Long id;
    private static String identityProvider;
    private static String idIdentityProvided;

    private static AWSCredentials credentials;


    public static void set(long id, String identityProvider, String idIdentityProvided, AWSCredentials credentials){
        CurrentUserInfo.id = id;
        CurrentUserInfo.identityProvider = identityProvider;
        CurrentUserInfo.idIdentityProvided = idIdentityProvided;

        CurrentUserInfo.credentials = credentials;
    }

    public static void set(long id, String identityProvider, String idIdentityProvided){
        CurrentUserInfo.id = id;
        CurrentUserInfo.identityProvider = identityProvider;
        CurrentUserInfo.idIdentityProvided = idIdentityProvided;
    }

    public static void setCredentials(AWSCredentials credentials){
        CurrentUserInfo.credentials = credentials;
    }

    public static void clear(){
        CurrentUserInfo.id = -1l;
        CurrentUserInfo.identityProvider = null;
        CurrentUserInfo.idIdentityProvided = null;

        CurrentUserInfo.credentials = null;
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
    public static AWSCredentials getCredentials() {
        return credentials;
    }

    public static boolean isSignedIn(){
        if(CurrentUserInfo.credentials == null) return false;
        return true;
    }


}
