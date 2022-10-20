package com.unina.natour.dto.request;

public class SaveUserRequestDTO {
    private String username;
    private String identityProvider;
    private String idIdentityProvided;


    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getIdentityProvider() {
        return identityProvider;
    }
    public void setIdentityProvider(String identityProvider) {
        this.identityProvider = identityProvider;
    }
    public String getIdIdentityProvided() {
        return idIdentityProvided;
    }
    public void setIdIdentityProvided(String idIdentityProvided) {
        this.idIdentityProvided = idIdentityProvided;
    }

}
