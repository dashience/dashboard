/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import com.visumbu.vb.utils.Rest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.collections.map.MultiValueMap;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

/**
 *
 * @author sabari
 */
public class ReviewTracker {

    public static final String accessToken = "USooR8EdsHOm0oqXfK58r_ynIfE=";
    public static final String accountId = "595ee7d2ec14906a57aacb08";
    public static final String BASE_URL = "https://api.reviewtrackers.com/";
    public static final String userId = "5a15893a0c41a7608f1111cd";

    public static void main(String[] args) throws ParseException {
        String userName = "sagar@digitalanalystteam.com";
        String password = "XYWmToJB";

        String authorizationHeaders = "Basic c2FnYXJAZGlnaXRhbGFuYWx5c3R0ZWFtLmNvbTpVU29vUjhFZHNIT20wb3FYZks1OHJfeW5JZkU9";

        String reportName = "getAccountUsers";

        if (reportName.equalsIgnoreCase("getAccountUsers")) {
            getAccountUsers(accountId, authorizationHeaders);
        }
        if (reportName.equalsIgnoreCase("getAccountReviews")) {
            getAccountReviews(accountId, authorizationHeaders);
        }

    }

    public static void getAccountReviews(String accountId, String authorizationHeaders) {
        String url = BASE_URL + "reviews";
        org.springframework.util.MultiValueMap<String, String> valueMap = new LinkedMultiValueMap<>();
        valueMap.put("account_id", Arrays.asList(accountId));
        valueMap.put("per_page", Arrays.asList("500"));
        String data = getResponse(url, authorizationHeaders, valueMap);
        System.out.println(data);
    }

    public static void getAccountUsers(String accountId, String authorizationHeaders) {
        String url = BASE_URL + "users";
        org.springframework.util.MultiValueMap<String, String> valueMap = new LinkedMultiValueMap<>();
        valueMap.put("account_id", Arrays.asList(accountId));
        valueMap.put("per_page", Arrays.asList("500"));

        String data = getResponse(url, authorizationHeaders, valueMap);
        System.out.println(data);
    }

    public static String getResponse(String url, String authorizationHeaders, org.springframework.util.MultiValueMap<String, String> params) {
        String urlString = url;
        if (params != null) {
            UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl(url).queryParams(params).build();
            urlString = uriComponents.toUriString();
        }
        System.out.println("url -->" + urlString);
        String jsonResponse = null;
        try {
            URL myURL = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) myURL.openConnection();
            conn.setRequestProperty("Authorization", authorizationHeaders);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "application/vnd.rtx.auth.v2.hal+json");
            conn.setRequestProperty("Content-Language", "en-US");
            conn.setUseCaches(false);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            int responseCode = conn.getResponseCode();

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));
            String output;
            while ((output = br.readLine()) != null) {
                jsonResponse += output;
            }
        } catch (IOException e) {
        }
        return jsonResponse;
    }

    public String buildBasicAuthorizationString(String username, String password) {
        String credentials = username + ":" + password;
        return "Basic " + encodeData(credentials);
    }

    public static String encodeData(String encodeData) {
        try {
            String asB64 = Base64.getEncoder().encodeToString(encodeData.getBytes("utf-8"));
            return asB64;
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(ReviewTracker.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static byte[] decodeData(String decodeData) {
        try {
            byte[] asBytes = Base64.getDecoder().decode(decodeData);
            System.out.println(new String(asBytes, "utf-8"));
            return asBytes;
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(ReviewTracker.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

}
