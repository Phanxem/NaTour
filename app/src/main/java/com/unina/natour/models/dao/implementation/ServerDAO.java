package com.unina.natour.models.dao.implementation;

import android.content.Context;
import android.util.Log;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSSessionCredentials;
import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobile.auth.core.IdentityManager;
import com.amazonaws.mobile.config.AWSConfiguration;
import com.unina.natour.config.CurrentUserInfo;
import com.unina.natour.controllers.utils.SignAWSv4Utils;
import com.unina.natour.controllers.utils.TimeUtils;
import com.unina.natour.dto.response.ResultMessageDTO;
import com.unina.natour.views.activities.NaTourActivity;

import java.io.IOException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;

public class ServerDAO {

    public static final String DOMAIN = "192.168.1.5:8080";
    //public static final String DOMAIN = "m4xyqnli3i.execute-api.eu-west-1.amazonaws.com/Production";

    public static final String SERVER_URL = "http://" + DOMAIN;

    private static final String REGION = "eu-west-1";
    private static final String SERVICE = "execute-api";
    private static final ZoneId ZONE_ID = ZoneId.of("Europe/Dublin");

    public ResultMessageDAO resultMessageDAO;

    public ServerDAO(){
        this.resultMessageDAO = new ResultMessageDAO();
    }


    public static Request signRequest(Request request){
        Log.e("ServerDAO", "START SIGNING");

        if(!CurrentUserInfo.isSignedIn()) return request;

        AWSCredentials awsCredentials = CurrentUserInfo.getCredentials();
        AWSSessionCredentials awsSessionCredentials = null;

        if(awsCredentials instanceof AWSSessionCredentials){
            awsSessionCredentials = (AWSSessionCredentials) awsCredentials;
        }
        else{
            awsSessionCredentials = new AWSSessionCredentials() {
                @Override
                public String getSessionToken() {
                    return null;
                }

                @Override
                public String getAWSAccessKeyId() {
                    return awsCredentials.getAWSAccessKeyId();
                }

                @Override
                public String getAWSSecretKey() {
                    return awsCredentials.getAWSSecretKey();
                }
            };

        }

        URL url = request.url().url();
        String method = request.method().toString().toUpperCase();
        ZonedDateTime time = ZonedDateTime.now(ZONE_ID);

        String domain = url.getHost();
        //String domain = DOMAIN;
        String urlString = url.toString();

        String[] strings = urlString.split(domain);

        String uri = "";
        String query = "";
        if(strings[1].contains("?")){
            String[] uriQuery = strings[0].split("/?");
            uri = uriQuery[0];
            query = uriQuery[1];
        }
        else{
            uri = strings[1];
        }

        Log.i("SERVER DAO", "test\nurl: " + url + "\nmethod: " + method + "\nuri: " + uri + "\nquery: " + query);

        String canonicalRequest = null;
        try {
            canonicalRequest = getCanonicalRequest(method,uri,query,domain,time, awsSessionCredentials.getSessionToken(), request);
        }
        catch (IOException | NoSuchAlgorithmException e) {
            return request;
        }

        String stringToSign = getStringToSign(time, REGION, SERVICE, canonicalRequest);

        byte[] derivedSigningKey = new byte[0];
        try {
            derivedSigningKey = getDerivedSigningKey(awsSessionCredentials.getAWSSecretKey(), time, REGION, SERVICE);
        }
        catch (Exception e) {
            return request;
        }

        String signature = getSignature(stringToSign, derivedSigningKey);

        String authorization = getAuthorization(awsSessionCredentials.getAWSAccessKeyId(), time, REGION, SERVICE, signature);


        Log.e("DAO","\n - \"X-Amz-Date\"" +  TimeUtils.toISO8601String(time) + "\n " +
                "\"Authorization\"" +  authorization +"\n " +
                "\"X-Amz-Security-Token\"" +  awsSessionCredentials.getSessionToken() + "\n ");


        Request.Builder requestBuilder = request.newBuilder();
        requestBuilder.addHeader("X-Amz-Date", TimeUtils.toISO8601String(time));
        requestBuilder.addHeader("Authorization", authorization);
        if(awsSessionCredentials.getSessionToken() != null){
            requestBuilder.addHeader("X-Amz-Security-Token", awsSessionCredentials.getSessionToken());
        }
        Request signedRequest = requestBuilder.build();

        return signedRequest;
    }


    //STEP 1
    private static String getCanonicalRequest(String method, String uri, String query, String domain, ZonedDateTime time, String sessionToken, Request request ) throws IOException, NoSuchAlgorithmException {
        String httpRequestMethod = method;

        String canonicalURI = uri;

        String canonicalQueryString = query;


        String canonicalHeaders = "host:" + domain + '\n' +
                                  "x-amz-date:" + TimeUtils.toISO8601String(time) + '\n';
        if(sessionToken != null){
            canonicalHeaders = canonicalHeaders + "x-amz-security-token:" + sessionToken +'\n';
        }

        String signedHeaders = "host;x-amz-date;x-amz-security-token";

        String notCodedRequest = SignAWSv4Utils.requestBodyToString(request);
        String requestPayload = SignAWSv4Utils.encodeHashHex(notCodedRequest);


        String canonicalRequest = httpRequestMethod + '\n' +
                canonicalURI + '\n' +
                canonicalQueryString + '\n' +
                canonicalHeaders + '\n' +
                signedHeaders + '\n' +
                requestPayload;

        String encodeCanonicalRequest = SignAWSv4Utils.encodeHashHex(canonicalRequest);

        Log.i("------> notCodedRequest", "\n" + notCodedRequest);
        Log.i("------> canonicalRequest", "\n" + canonicalRequest);
        Log.i("------> encodedCanonicalRequest", "\n" + encodeCanonicalRequest);

        return encodeCanonicalRequest;
    }


    //STEP 2
    private static String getStringToSign(ZonedDateTime time, String region, String service, String canonicalRequest){
        String algorithm = "AWS4-HMAC-SHA256";

        String requestDateTime = TimeUtils.toISO8601String(time);

        String credentialScope = TimeUtils.toShortString(time) + "/" +
                region + "/" +
                service + "/" +
                "aws4_request";

        String hashedCanonicalRequest = canonicalRequest;

        String stringToSign = algorithm + '\n' +
                requestDateTime + '\n' +
                credentialScope + '\n' +
                hashedCanonicalRequest;

        Log.i("------> stringToSign", "\n" + stringToSign);

        return stringToSign;
    }


    //STEP 3
    private static byte[] getDerivedSigningKey(String secretKey, ZonedDateTime time, String region, String service) throws Exception {
        byte[] kSecret = ("AWS4" + secretKey).getBytes("UTF-8");

        String shortDate = TimeUtils.toShortString(time);
        byte[] kDate = SignAWSv4Utils.hmacSHA256(shortDate, kSecret);

        byte[] kRegion = SignAWSv4Utils.hmacSHA256(region, kDate);
        byte[] kService = SignAWSv4Utils.hmacSHA256(service, kRegion);
        byte[] kSigning = SignAWSv4Utils.hmacSHA256("aws4_request", kService);

        return kSigning;
    }


    //STEP 4
    private static String getSignature(String stringToSign, byte[] derivedSigningKey){

        byte[] signature = null;
        try {
            signature = SignAWSv4Utils.hmacSHA256(stringToSign, derivedSigningKey);
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }

        String encodedSignature = SignAWSv4Utils.bytesToHex(signature);

        Log.i("------> encodedSignature", "\n" + encodedSignature);

        return encodedSignature;
    }


    //STEP 5
    private static String getAuthorization(String accessKey, ZonedDateTime time, String region, String service, String signature){
        String algorithm = "AWS4-HMAC-SHA256";

        String credential = accessKey + "/" +
                TimeUtils.toShortString(time) + "/" +
                region + "/" +
                service + "/" +
                "aws4_request";

        String signedHeaders = "host;x-amz-date;x-amz-security-token";

        //String signature = signature;

        String authorization = algorithm + " " +
                "Credential=" + credential + ", " +
                "SignedHeaders=" + signedHeaders + ", " +
                "Signature=" + signature;

        Log.i("------> authorization", "\n" + authorization);

        return authorization;


    }




    public ResultMessageDTO testServer(){
        String url = SERVER_URL + "/server/test";

        Request request = new Request.Builder()
                .url(url)
                .build();

        ResultMessageDTO resultMessageDTO = resultMessageDAO.fulfilRequest(request);

        return resultMessageDTO;
    }

}
