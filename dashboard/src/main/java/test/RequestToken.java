///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
package test;

import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import sun.misc.BASE64Encoder;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import com.google.common.collect.HashBiMap;
import com.visumbu.vb.utils.ApiUtils;
import com.visumbu.vb.utils.JsonSimpleUtils;
import com.visumbu.vb.utils.Rest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class RequestToken {

    public static void main(String args[]) throws ParseException {
        String screen_name = "Enlivant";
        String user_id = "2964932975";
        String consumer_key = "VVy4f3sW4ZCPNW33ejTBiIdxu";
        String consumer_secret_key = "8zUdWToQYAvgGvLn7uQtlN5hFywYNogkcCCWUGV8cTompxXCQV";
        String oauth_secret_token = "yS3JfmCcOQec97h8q6AERBZTv77qU4d5rA4Q2Tp9Fiw21";
        String oauth_token = "2964932975-k4rUnSdpxutu5ndrCSKFteCtX4vCetQBN0yXoap";
//        String signature_url = "https://api.twitter.com/1.1/statuses/update.json?include_entities=true&"
//                + "status=" + "&"
//                + "oauth_consumer_key=" + consumer_key + "&"
//                + "oauth_signature_method=HMAC-SHA1&"
//                + "oauth_version=1.0&"
//                + "oauth_token=" + oauth_token + "&"
//                + "oauth_timestamp=1495092516&"
//                + "oauth_nonce=133041506";


        String authurl = "GEThttps://api.twitter.com/1.1/users/lookup.json?oauth_consumer_key=VVy4f3sW4ZCPNW33ejTBiIdxu&oauth_signature_method=HMAC-SHA1&oauth_version=1.0&oauth_token=2964932975-k4rUnSdpxutu5ndrCSKFteCtX4vCetQBN0yXoap&oauth_signature=LlLcNdFiv2n9JJiSUDHu6MxE2RE=&oauth_timestamp=1495092516&oauth_nonce=133041506";
        String encodedUrl = encode(authurl);
        
        String signature_url=encodedUrl;
//        String signature_url = "https://api.twitter.com/1.1/statuses/update.json";

        String oauthSignature = generateSignature(signature_url, consumer_secret_key, oauth_token);
//        System.out.println("**************************************");
//        System.out.println(oauthSignature);

        List<Map<String, String>> twitterResult = getTwitterOrganicData(screen_name, user_id, consumer_key, 
                consumer_secret_key, oauth_token, oauth_secret_token, oauthSignature);
    }

    
    private static String generateSignature(String signatueBaseStr, String oAuthConsumerSecret,
            String oAuthTokenSecret) {
        byte[] byteHMAC = null;
        try {
            Mac mac = Mac.getInstance("HmacSHA1");
            SecretKeySpec spec;
            if (null == oAuthTokenSecret) {
                String signingKey = encode(oAuthConsumerSecret) + '&';
                spec = new SecretKeySpec(signingKey.getBytes(), "HmacSHA1");
            } else {
                String signingKey = encode(oAuthConsumerSecret) + '&' + encode(oAuthTokenSecret);
                spec = new SecretKeySpec(signingKey.getBytes(), "HmacSHA1");
            }
            mac.init(spec);
            byteHMAC = mac.doFinal(signatueBaseStr.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new BASE64Encoder().encode(byteHMAC);
    }

    private static String encode(String value) {
        String encoded = "";
        try {
            encoded = URLEncoder.encode(value, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        String sb = "";
        char focus;
        for (int i = 0; i < encoded.length(); i++) {
            focus = encoded.charAt(i);
            if (focus == '*') {
                sb += "%2A";
            } else if (focus == '+') {
                sb += "%20";
            } else if (focus == '%' && i + 1 < encoded.length()
                    && encoded.charAt(i + 1) == '7' && encoded.charAt(i + 2) == 'E') {
                sb += '~';
                i += 2;
            } else {
                sb += focus;
            }
        }
        return sb.toString();
    }
    
    
    
    private static List<Map<String, String>> getTwitterOrganicData(String screenName, String userId, String consumerKey,
            String consumerSecretKey, String oauthToken, String ouathTokenSecret, String oauthSignature)
            throws ParseException {

//        String url = "https://api.twitter.com/1.1/users/lookup.json?screen_name=" + screenName + "&user_id=" + userId + "&"
//                + "oauth_consumer_key=" + consumerKey + "&"
//                + "oauth_signature_method=HMAC-SHA1&"
//                + "oauth_version=1.0&"
//                + "oauth_token=" + oauthToken + "&"
//                + "oauth_signature=" + oauthSignature + "&"
//                + "oauth_timestamp=1495092516&"
//                + "oauth_nonce=133041506";

        String url="https://api.twitter.com/1.1/users/lookup.json?screen_name=Enlivant&user_id=2964932975&oauth_consumer_key=DC0sePOBbQ8bYdC8r4Smg&oauth_signature_method=HMAC-SHA1&oauth_version=1.0&oauth_token=2964932975-RlNIi6QnoQtydUosFxNWUTuWgJlsJKCuGX4HmZS&oauth_signature=Bce48DgotM5cIDhi11lxo9A8WHA%3D&oauth_timestamp=1495092516&oauth_nonce=133041506";
        String twitterData = Rest.getData(url);
        JSONParser parser = new JSONParser();
        Object jsonObj = parser.parse(twitterData);

        System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
        System.out.println(jsonObj);
        return null;
    }

    
}

//
///**
// *
// * @author deldot
// */
//import com.google.gdata.client.authn.oauth.OAuthUtil;
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.UnsupportedEncodingException;
//import java.net.URLEncoder;
//import java.security.InvalidKeyException;
//import java.security.Key;
//import java.security.NoSuchAlgorithmException;
//
//import java.util.Properties;
//
//import javax.crypto.Mac;
//import javax.crypto.spec.SecretKeySpec;
//import org.apache.commons.lang.RandomStringUtils;
//import org.apache.http.HttpEntity;
//import org.apache.http.HttpResponse;
//import org.apache.http.client.ClientProtocolException;
//import org.apache.http.client.HttpClient;
//import org.apache.http.client.methods.HttpGet;
//import org.apache.http.impl.client.DefaultHttpClient;
//
//import org.apache.xerces.impl.dv.util.Base64;
//
//public class RequestToken {
//
//    private static final String REQUEST_TOKEN_URL = "http://api.twitter.com/oauth/request_token";
//    private static final String OAUTH_VERSION = "1.0";
//    private static final String SIGNATURE_METHOD = "HMAC-SHA1";
//    private static String consumerKey;
//    private static String consumerSecret;
//    private static String oauthToken;
//    private static String oauthTokenSecret;
//
//    public RequestToken(){
//        oauthToken = null;
//        oauthTokenSecret = null;
//        request();
//    }
//
//    public static void main(String[] args) {
//        Properties prop = new Properties();
//
//        try{
//            prop.load(new FileInputStream(new File("oauth.properties")));
//        } catch (IOException e){
//            e.printStackTrace();
//        }
//
////        consumerKey = prop.getProperty("oauth.consumer.key");
////        consumerSecret = prop.getProperty("oauth.consumer.secret");
//        consumerKey ="VVy4f3sW4ZCPNW33ejTBiIdxu";
//        consumerSecret ="8zUdWToQYAvgGvLn7uQtlN5hFywYNogkcCCWUGV8cTompxXCQV";
//
//        RequestToken requestToken = new RequestToken();
//    }
//
//    private void request() {
//        String requestParameters = getRequestParameters();
//
//        HttpClient httpClient = new DefaultHttpClient();
//
//        String signatureBaseString = getSignatureBaseString(requestParameters);
//        String keyString = getKeyString();
//        String signature = getSignature(signatureBaseString, keyString);
//
//        String req = REQUEST_TOKEN_URL + "?" + requestParameters + "&oauth_signature=" + signature;
//        HttpGet httpGet = new HttpGet(req);
//
//        HttpResponse response;
//        BufferedReader br = null;
//        try {
//            response = httpClient.execute(httpGet);
//
//            HttpEntity entity = response.getEntity();
//            if(entity != null){
//                InputStream in = entity.getContent();
//
//                br = new BufferedReader(new InputStreamReader(in));
//                String line = br.readLine();
//                System.out.println(line);
//            }
//        } catch (ClientProtocolException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }finally{
//            try {
//                br.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    private String getSignatureBaseString(String requestParameters) {
//        return "GET&" + OAuthUtil.URLEncode(REQUEST_TOKEN_URL) + "&" + OAuthUtil.URLEncode(requestParameters);
//    }
//
//    private String getRequestParameters() {
//        return
//            "oauth_consumer_key=" + consumerKey + "&" +
//            "oauth_nonce=" + OAuthUtil.URLEncode(OAuthUtil.getNonce()) + "&" +
//            "oauth_signature_method=" + SIGNATURE_METHOD + "&" +
//            "oauth_timestamp=" + OAuthUtil.getTimeStamp() + "&" +
//            "oauth_version=" + OAUTH_VERSION ;
//    }
//
//    private String getKeyString(){
//        if(oauthToken == null){
//            return consumerSecret + "&";
//        }else{
//            return consumerSecret + "&" + oauthToken;
//        }
//    }
//
//    private String getSignature(String signatureBaseString, String keyString){
//        String signature = null;
//        String algorithm = "HmacSHA1";
//        try {
//            Mac mac = Mac.getInstance(algorithm);
//            Key key= new SecretKeySpec(keyString.getBytes(), algorithm);
//
//            mac.init(key);
//            byte[] digest = mac.doFinal(signatureBaseString.getBytes());
//            signature = OAuthUtil.URLEncode(Base64.encode(digest));
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        } catch (InvalidKeyException e) {
//            e.printStackTrace();
//        }
//        return signature;
//    }
//}
