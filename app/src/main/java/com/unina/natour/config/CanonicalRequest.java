package com.unina.natour.config;

import okhttp3.RequestBody;

public class CanonicalRequest {
    private String httpRequestMethod;
    private String canonicalURI ;
    private String canonicalQueryString;
    private String canonicalHeaders;
    private String signedHeaders;
    private RequestBody requestPayload;

    public CanonicalRequest(String method, String domain, String uri, String query, RequestBody requestBody, String sessionToken){
        this.httpRequestMethod = method;
        this.canonicalURI = uri;
        this.canonicalQueryString = query;

    }






}
