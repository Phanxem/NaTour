package com.unina.natour.amplify;

import android.util.Log;

import com.amazonaws.auth.AWSSessionCredentials;
import com.unina.natour.controllers.utils.TimeUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Locale;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import okhttp3.Request;
import okio.Buffer;

public class SignOkHttp {

    private static final byte[] HEX_ARRAY = "0123456789ABCDEF".getBytes(StandardCharsets.US_ASCII);

    private String test = "AWS4-HMAC-SHA256 " +
            "Credential=ASIAXZHTFLT45BFZ5D7Y/20220926/eu-west-1/execute-api/aws4_request, " +
            "SignedHeaders=host;x-amz-date;x-amz-security-token, " +
            "Signature=b97dcb20cc1f8ea604f6dd87206456f79397b66874cc4f6f045f27a7d0dfac62";


    private String test2 = "The Canonical String for this request should have been\n" +
            "GET\n" +

            "/testing/test\n" +

            "\n" +

            "host:vwyxm7bcre.execute-api.eu-west-1.amazonaws.com\n" +
            "x-amz-date:20220926T015001Z\n" +
            "x-amz-security-token:IQoJb3JpZ2luX2VjEDoaCWV1LXdlc3QtMSJHMEUCICyI3INDAeoh74cOA9/bx4lLYPUfpIYgxzGoSB9v+ivnAiEAyDA7whJ4fuuGy6yzZTxJpvGIpjFQ723JeSf5k6aqNMAq8AMI4///////////ARAAGgw1MzUyMzMzODc3NjkiDL+fw02VE2D3jKXEtyrEA1OoJ4La9DbJEYTOMcNhjpsjwb9cN8qgw42Fz1zGn4RnG2oAD40bT2xBOhXD2njrRFNMPy+EBw1t+sUo4EywBiq9XSqGXc3nTHUhvb4YCOBS8p9LoyZ8qt3kUA37kdeBMuFvWjsG2BumEF+unNDDwXq9H1jnUiYfE9yzy9kB6RmL94AL2OyFabFN4YeG8BXEos5zxxUXW7xecCokDvZprkOnwx/hE623mhRUBn7Vgi6v+4agYt1jhq6fNpMKMKoR4seT/whlU6rkA1Icx7+z8amLUMannVV/TqRp3K4+2pAZWjeHcvBmT9srEFxcQh9KF23Oy5R8IyX1uEEjlYAMDfAi58juEAXDE8gH6kbKSK5l+nAV08u0fTCLpA5J1HEYizJRcIFx+knO9CPvpi580N5doHN+qqXCczSuNQSYggP8T5thvzeybFU/LpXet6xhzrVBgjfbKV1lfBSKREY3U31fQOClirCfgRGu+PT/kFZaU0Ybc4hdeET2ergCZz81cgW02zgIWD+GIhz3b5bfcx2UYXoLay0HlYxfZptoi35eqDJB8RhOYG2OdPVTx1r3y7Q+gVNd7DIKtHRhQl54XX6Nx4WoMMmKxJkGOoQCqw9NaMaPF6y9PsaVGc8Tdf/lrWfUcLffGp4em6h7oJrukTe+qgJ0JbZBaFRQb89h0AnpC+elGhv+danL4HGfYw8DTmExUD10siuO1ugoeIGgcIC+zig6qskK+DqnDy8XdO3D2R8volnP9CqQHj6Wt4VAbAV//6i8LRhty4YuVcx4HiCPuXydVcKWXvu3I2cpUeI8khXmEEb7ErsSFXyhqFTMyfgF9CDbk7No0g2lYg4ZXsrq3e6nFnj7vp8jBRAOUyRYoURmKBd8yHLARBebupq1cC6M4i7fJd50OaDkOB66CMfS0Zio7HrgeAfUPDZiMceKjHpanDXUObFmmNoE1M87OLY=\n\n" +

            "host;x-amz-date;x-amz-security-token\n" +

            "e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855'\n\n" +


            "The String-to-Sign should have been\n" +
            "'AWS4-HMAC-SHA256\n" +
            "20220926T015001Z\n" +
            "20220926/eu-west-1/execute-api/aws4_request\n" +
            "edb8ea283bc36602054874bd413907f3b89525d11d2ec96fa4fa42b60a6cea60'\n}";


    /*
    GET
    vwyxm7bcre.execute-api.eu-west-1.amazonaws.com
    /testing/test
    host:test=1&pyro=sedano
    x-amz-date:20220927T134629Z
    x-amz-security-token:IQoJb3JpZ2luX2VjEF4aCWV1LXdlc3QtMSJHMEUCIBh48FUhjF3KVNu0j7TW3v+ZpVCDvLdZY066Z4kdG/fwAiEA+bMLG/tPVb28RTvUjpr8fLw9rASD3Wld/XNy4jbxdO8q5wMIFxAAGgw1MzUyMzMzODc3NjkiDOfB9290X6m5P8/2fyrEA1m8vfPYz+owz7yZINP74JgdVMAVS3U12XBl0qX4vHgyb+Jm/5Y//McQgJ/11qm4DsZuOCyNvRS1/IyCSuBPUiPYD9tLNld3xZA/sLITrTVTiE+pGTII9hKLR5xJWKCaOK8vbMJXNa7D+yvJxTE0lliAxguVxGoG3tGl6HBCF3B0Y4avQmVvmt9CwVgoMYs7fAmXbyZ8PhKs5Q4HNjeRtIEN7CVAR5sHqNrX7UTniYLLaly70oh1h6Jsw7/RJuUBUHomjXjJeXBM0ckekqi72dHY50z1gbldZmyTlirD3sQQT2DTNTuRE14SOGr0vQo7EnMitc+M0C1EAgyLcNsrPiD9syJmp83OnrGXERBqVVA4S0Zu88LcYUIXNM5kcZnB/HqeEIU0+3FaQGrJCc7PSLtLPj1cIR99HzNUs9OobtwW7PVnzNOBqPlWeNlgB2pmWE7me5AQR0uN2GAl5vL+WsZIr9stIJOw9hqxmyZo2SDs7peNoauEhnpsvFFy2NB4cfrlOqoLpnkfc0utgmtXzHW2N7+0CYJh6ydW7jtIoVSF1rLn85YJLOASetjGFPR/WN0Qlg5m+7S7TV+fUMCq1b/4lydqMLX9y5kGOoQClc2D3xr7tuX06VYSRbxOePu2HUyxqS4JucFgLyc6JPkIXTpFlZy6sv5UpJxnzytHlJhKSZ5BcwnySSnEtyjiUfiXQaQTvaL1aTwQWJ+AO3wjxn3x2pliYB4K/7XvvDtAXVlRt4F/rpv8LryX8SfAUxcVQL6VKdag63Akf17huA3MUTr5cQgx+jS9yXdjQpe/b8LyMabcZ/6xsU9rmsYlZwYbQUXBgjlgjN6/8CvTlRY39eBmohJq67qC4s3OvCCi95vBOLX4+2YYaNI68PpXO8IoDEEqpo+de16an3Jm2oZKqO7ZFZPP19C7J+svitWSUJgHFJYnGJmzBzJ+wb6ibfYDrHY=

    host;x-amz-date;x-amz-security-token
    e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855
     */



    //Signature=996db29c2ceedb50dc82398d170d0074e832b3d974d43a6deb2034de881e3c51
    //        e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855


    //STEP 1
    public static String getCanonicalRequest(String method, String uri, String query, String domain, ZonedDateTime time, String sessionToken, Request request ) throws IOException, NoSuchAlgorithmException {
        String httpRequestMethod = method;

        String canonicalURI = uri;

        String canonicalQueryString = query;


        String canonicalHeaders = "host:" + domain + '\n' +
                                  //"x-amz-date:" + TimeUtils.toISO8601String(ZonedDateTime.now(ZoneId.of("Europe/Dublin"))) + '\n' +
                                  "x-amz-date:" + TimeUtils.toISO8601String(time) + '\n' +
                                  "x-amz-security-token:" + sessionToken +'\n';


        String signedHeaders = "host;x-amz-date;x-amz-security-token";

        String notCodedRequest = requestBodyToString(request);
        String requestPayload = encodeHashHex(notCodedRequest);


        String canonicalRequest = httpRequestMethod + '\n' +
                                  canonicalURI + '\n' +
                                  canonicalQueryString + '\n' +
                                  canonicalHeaders + '\n' +
                                  signedHeaders + '\n' +
                                  requestPayload;

        String encodeCanonicalRequest = encodeHashHex(canonicalRequest);

        Log.i("------> notCodedRequest", "\n" + notCodedRequest);
        Log.i("------> canonicalRequest", "\n" + canonicalRequest);
        Log.i("------> encodedCanonicalRequest", "\n" + encodeCanonicalRequest);

        return encodeCanonicalRequest;
    }


    //STEP 2
    public static String getStringToSign(ZonedDateTime time, String region, String service, String canonicalRequest){
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
    public static byte[] getDerivedSigningKey(String secretKey, ZonedDateTime time, String region, String service) throws Exception {
        byte[] kSecret = ("AWS4" + secretKey).getBytes("UTF-8");

        String shortDate = TimeUtils.toShortString(time);
        byte[] kDate = hmacSHA256(shortDate, kSecret);

        byte[] kRegion = hmacSHA256(region, kDate);
        byte[] kService = hmacSHA256(service, kRegion);
        byte[] kSigning = hmacSHA256("aws4_request", kService);

        return kSigning;
    }


    //STEP 4
    public static String getSignature(String stringToSign, byte[] derivedSigningKey){

        byte[] signature = null;
        try {
            signature = hmacSHA256(stringToSign, derivedSigningKey);
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }

        String encodedSignature = bytesToHex(signature);

        Log.i("------> encodedSignature", "\n" + encodedSignature);

        return encodedSignature;
    }


    //STEP 5
    public static String getAuthorization(String accessKey, ZonedDateTime time, String region, String service, String signature){
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





/*
    public static Request getSignedRequest(String url, AWSSessionCredentials awsSessionCredentials, String region, String service) throws Exception {

        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);

        String iso8601Date = TimeUtils.toISO8601String(calendar);
        Log.e("TAG--------", "fulltime: " + iso8601Date);
        String shortDate = TimeUtils.toShortString(calendar);

        String signature = getSignatureKey(awsSessionCredentials.getAWSSecretKey(),TimeUtils.toShortString(calendar),region, service);

        String algorithm = "AWS4-HMAC-SHA256";
        String aws4_request = "aws4_request";

        ZonedDateTime zonedDateTime = ZonedDateTime.now(ZoneId.of("Europe/Dublin"));

        String authorization = algorithm + " " +
                               "Credential=" + awsSessionCredentials.getAWSAccessKeyId() + "/" + shortDate + "/" + region + "/" + service + "/" + aws4_request + ", " +
                               "SignedHeaders=host;x-amz-date;x-amz-security-token, " +
                               "Signature=" + signature;

        Request request = new Request.Builder()
                .addHeader("authorization", authorization)
                .addHeader("x-amz-date", TimeUtils.toISO8601String(ZonedDateTime.now(ZoneId.of("Europe/Dublin")).minusHours(1)))
                .addHeader("x-amz-security-token", awsSessionCredentials.getSessionToken())
                .url(url)
                .build();

        return request;
    }
*/




    private static byte[] hmacSHA256(String data, byte[] key) throws Exception {
        String algorithm="HmacSHA256";
        Mac mac = Mac.getInstance(algorithm);
        mac.init(new SecretKeySpec(key, algorithm));
        return mac.doFinal(data.getBytes("UTF-8"));
    }








    private static String requestBodyToString(Request request) throws IOException {
        Request requestCopy = request.newBuilder().build();
        Buffer buffer = new Buffer();
        if(requestCopy.body() == null) return "";
        requestCopy.body().writeTo(buffer);
        return buffer.readUtf8();
    }

    /*
    private static String requestBodyToString(Request request) throws IOException {
        Request requestCopy = request.newBuilder().build();
        Buffer buffer = new Buffer();
        requestCopy.body().writeTo(buffer);
        return buffer.readUtf8();
    }
    */

    private static String encodeHashHex(String originalString) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] encodedhash = digest.digest(originalString.getBytes(StandardCharsets.UTF_8));
        return bytesToHex(encodedhash);
    }

    private static String bytesToHex(byte[] bytes) {
        byte[] hexChars = new byte[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars, StandardCharsets.UTF_8).toLowerCase();
    }

}
