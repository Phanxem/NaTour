package com.unina.natour.dto.request;

import com.unina.natour.dto.response.MessageResponseDTO;

public class AccountInfoRequestDTO {

    private String email;
    private boolean isFacebookAccountLinked;
    private boolean isGoogleAccountLinked;


    public AccountInfoRequestDTO(){}

    public AccountInfoRequestDTO(String email, boolean isFacebookAccountLinked, boolean isGoogleAccountLinked){
        this.email = email;
        this.isFacebookAccountLinked = isFacebookAccountLinked;
        this.isGoogleAccountLinked = isGoogleAccountLinked;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isFacebookAccountLinked() {
        return isFacebookAccountLinked;
    }

    public void setFacebookAccountLinked(boolean facebookAccountLinked) {
        isFacebookAccountLinked = facebookAccountLinked;
    }

    public boolean isGoogleAccountLinked() {
        return isGoogleAccountLinked;
    }

    public void setGoogleAccountLinked(boolean googleAccountLinked) {
        isGoogleAccountLinked = googleAccountLinked;
    }
}
