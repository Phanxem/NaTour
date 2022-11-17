package com.unina.natour.config;

import com.amazonaws.auth.AWSCredentials;

public class Credentials {
    private String identityProvider;
    private AWSCredentials awsCredentials;

    public String getIdentityProvider() {
        return identityProvider;
    }

    public void setIdentityProvider(String identityProvider) {
        this.identityProvider = identityProvider;
    }

    public AWSCredentials getAwsCredentials() {

        return awsCredentials;
    }

    public void setAwsCredentials(AWSCredentials awsCredentials) {
        this.awsCredentials = awsCredentials;
    }
}
