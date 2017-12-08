/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.utils;

import static com.visumbu.vb.utils.JsonUtils.toList;
import static com.visumbu.vb.utils.JsonUtils.toMap;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.json.JSONArray;
import org.json.JSONObject;

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

    public static Map<String, Object> getAuthentionData(Map<String, Object> properties) {
        String oauth_nonce = oauthNonce();
        String oauth_timestamp = getTimeStamp();
        Map<String, Object> params = new HashMap<>();
        params = setParameters(properties);
        params.put("oauth_nonce", oauth_nonce);
        params.put("oauth_timestamp", oauth_timestamp);
        properties.put("oauth_nonce", oauth_nonce);
        properties.put("oauth_timestamp", oauth_timestamp);

        System.out.println("****************** SIGNATURET PROPERTIES **********************");
        System.out.println(properties);
        System.out.println("****************** SIGNATURET PROPERTIES **********************");
        String signature = generateSignature(properties);
        params.put("oauth_signature", signature);

        return params;

    }

    public static String generateSignature(Map<String, Object> properties) {
        try {
            String parameter_string = null;
            if (properties.containsKey("queryString")) {
                parameter_string = "oauth_consumer_key=" + properties.get("oauth_consumer_key") + "&oauth_nonce=" + properties.get("oauth_nonce")
                        + "&oauth_signature_method=" + properties.get("oauth_signature_method")
                        + "&oauth_timestamp=" + properties.get("oauth_timestamp") + "&oauth_token=" + OauthAuthentication.encode((String) properties.get("oauth_token"))
                        + "&oauth_version=1.0" + properties.get("queryString");
                System.out.println("parameter String-----" + parameter_string);
            } else {
                parameter_string = "oauth_consumer_key=" + properties.get("oauth_consumer_key") + "&oauth_nonce=" + properties.get("oauth_nonce")
                        + "&oauth_signature_method=" + properties.get("oauth_signature_method")
                        + "&oauth_timestamp=" + properties.get("oauth_timestamp") + "&oauth_token=" + OauthAuthentication.encode((String) properties.get("oauth_token"))
                        + "&oauth_version=" + properties.get("oauth_version");
            }
            String signature_base_string = properties.get("httpMethod") + "&" + OauthAuthentication.encode((String) properties.get("baseUrl")) + "&" + OauthAuthentication.encode(parameter_string);
            String oauth_signature = OauthAuthentication.computeSignature(signature_base_string, OauthAuthentication.encode((String) properties.get("consumerSecret")) + "&" + OauthAuthentication.encode((String) properties.get("tokenSecret")));
            String signature = OauthAuthentication.encode(oauth_signature);
            return signature;
        } catch (GeneralSecurityException ex) {
            Logger.getLogger(OauthAuthentication.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(OauthAuthentication.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static Map<String, Object> setParameters(Map<String, Object> properties) {
        Map<String, Object> map = new HashMap<String, Object>();
        for (Map.Entry<String, Object> entry : properties.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if (!key.contains("queryString")) {
                map.put(key, value);
            }
        }
        return map;
    }

}
