/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.util.Calendar;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author dashience
 */
public class OauthAuthentication {


    public static String encode(String value) {
        {
            String encoded = null;
            try {
                encoded = URLEncoder.encode(value, "UTF-8");
            } catch (UnsupportedEncodingException ignore) {
            }
            StringBuilder buf = new StringBuilder(encoded.length());
            char focus;
            for (int i = 0; i < encoded.length(); i++) {
                focus = encoded.charAt(i);
                if (focus == '*') {
                    buf.append("%2A");
                } else if (focus == '+') {
                    buf.append("%20");
                } else if (focus == '%' && (i + 1) < encoded.length()
                        && encoded.charAt(i + 1) == '7' && encoded.charAt(i + 2) == 'E') {
                    buf.append('~');
                    i += 2;
                } else {
                    buf.append(focus);
                }
            }
            return buf.toString();
        }

    }

    public static String computeSignature(String baseString, String keyString) throws GeneralSecurityException,
            UnsupportedEncodingException {
        SecretKey secretKey = null;

        byte[] keyBytes = keyString.getBytes();
        secretKey = new SecretKeySpec(keyBytes, "HmacSHA1");

        Mac mac = Mac.getInstance("HmacSHA1");
        mac.init(secretKey);

        byte[] text = baseString.getBytes();

        return new String(org.apache.commons.codec.binary.Base64.encodeBase64(mac.doFinal(text))).trim();
    }

    public static String generateTimeStamp() {
        Calendar tempcal = Calendar.getInstance();
        long ts = tempcal.getTimeInMillis();// get current time in milliseconds
        String oauth_timestamp = (new Long(ts / 1000)).toString();
        return oauth_timestamp;
    }

//    public String generateSignature(String signatueBaseStr, String oAuthConsumerSecret, String oAuthTokenSecret) {
//        byte[] byteHMAC = null;
//        try {
//            Mac mac = Mac.getInstance("HmacSHA1");
//            SecretKeySpec spec;
//            if (null == oAuthTokenSecret) {
//                String signingKey = encode(oAuthConsumerSecret) + '&';
//                spec = new SecretKeySpec(signingKey.getBytes(), "HmacSHA1");
//            } else {
//                String signingKey = encode(oAuthConsumerSecret) + '&' + encode(oAuthTokenSecret);
//                spec = new SecretKeySpec(signingKey.getBytes(), "HmacSHA1");
//            }
//            mac.init(spec);
//            byteHMAC = mac.doFinal(signatueBaseStr.getBytes());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return new BASE64Encoder().encode(byteHMAC);
//    }

    public static String oauthNonce() {
        String uuid_string = UUID.randomUUID().toString();
        uuid_string = uuid_string.replaceAll("-", "");
        return uuid_string;
    }

    public static String getTimeStamp() {
        Calendar tempcal = Calendar.getInstance();
        long ts = tempcal.getTimeInMillis();// get current time in milliseconds
        return (new Long(ts / 1000)).toString();
    }

    public static String generateSignature(Map <String,String> properties,String type) {
            String parameter_string=null;
            String oauth_signature = null;
            
        try { 
            if(type.equals("getToken")){
                
                parameter_string = "oauth_consumer_key=" + properties.get("oauth_consumer_key") + "&oauth_nonce=" + properties.get("oauth_nonce")
                    + "&oauth_signature_method=" + properties.get("oauth_signature_method")
                    + "&oauth_timestamp=" +  properties.get("oauth_timestamp") + 
                    "&oauth_version="+properties.get("oauth_version");
                String signature_base_string = properties.get("httpMethod") + "&" + OauthAuthentication.encode(properties.get("baseUrl")) + "&" + OauthAuthentication.encode(parameter_string);
                oauth_signature = OauthAuthentication.computeSignature(signature_base_string, OauthAuthentication.encode( properties.get("consumerSecret")) + "&");
            }
                
            else{
            if(properties.containsKey("queryString")) {
                parameter_string = "oauth_consumer_key=" + properties.get("oauth_consumer_key") + "&oauth_nonce=" + properties.get("oauth_nonce")
                    + "&oauth_signature_method=" + properties.get("oauth_signature_method")
                    + "&oauth_timestamp=" +  properties.get("oauth_timestamp") + "&oauth_token=" + OauthAuthentication.encode(properties.get("oauth_token")) +
                    "&oauth_version=1.0"+properties.get("queryString");
                System.out.println("parameter String-----"+parameter_string);
            }else{
               
                 parameter_string = "oauth_consumer_key=" + properties.get("oauth_consumer_key") + "&oauth_nonce=" + properties.get("oauth_nonce")
                    + "&oauth_signature_method=" + properties.get("oauth_signature_method")
                    + "&oauth_timestamp=" +  properties.get("oauth_timestamp") + "&oauth_token=" + OauthAuthentication.encode(properties.get("oauth_token")) +
                    "&oauth_version="+properties.get("oauth_version");
                 
            }
             String signature_base_string = properties.get("httpMethod") + "&" + OauthAuthentication.encode(properties.get("baseUrl")) + "&" + OauthAuthentication.encode(parameter_string);
            oauth_signature = OauthAuthentication.computeSignature(signature_base_string, OauthAuthentication.encode( properties.get("consumerSecret")) + "&" + OauthAuthentication.encode( properties.get("tokenSecret")));
            }
            

            
           
            String signature = OauthAuthentication.encode(oauth_signature);
            return signature;
        } catch (GeneralSecurityException | UnsupportedEncodingException ex) {
            Logger.getLogger(OauthAuthentication.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

}
