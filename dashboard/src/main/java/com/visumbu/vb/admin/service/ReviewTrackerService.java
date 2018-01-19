/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.admin.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

/**
 *
 * @author sabari
 */
@Service("reviewTrackerService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class ReviewTrackerService {

    public static final String BASE_URL = "https://api.reviewtrackers.com/";
    public static final String authorizationHeaders = "Basic c2FnYXJAZGlnaXRhbGFuYWx5c3R0ZWFtLmNvbTpVU29vUjhFZHNIT20wb3FYZks1OHJfeW5JZkU9";

    public List<Map<String, Object>> get(String dataSetReportName, String reviewTrackerAcessToken, String reviewTrackerAccountId,
            Date startDate, Date endDate, String timeSegment, String productSegment,String reviewTrackerAccountUserName) {

        if (dataSetReportName.equalsIgnoreCase("getAccountUsers")) {
            return getAccountUsers(reviewTrackerAccountId, authorizationHeaders, startDate, endDate, timeSegment, 
                    productSegment,reviewTrackerAccountUserName);
        }
        if (dataSetReportName.equalsIgnoreCase("getAccountReviews")) {
            return getAccountReviews(reviewTrackerAccountId, authorizationHeaders, startDate, endDate, timeSegment, 
                    productSegment,reviewTrackerAccountUserName);
        }

        return null;
    }

    public static List<Map<String, Object>> getAccountReviews(String accountId, String authorizationHeaders, 
            Date startDate, Date endDate,String timeSegment, String productSegment,String reviewTrackerAccountUserName) {
        String url = BASE_URL + "reviews";
        MultiValueMap<String, String> valueMap = new LinkedMultiValueMap<>();
        valueMap.put("account_id", Arrays.asList(accountId));
        valueMap.put("per_page", Arrays.asList("500"));
        String data = getResponse(url, authorizationHeaders, valueMap);
        System.out.println(data); 
        return null;
    }

    public static List<Map<String, Object>> getAccountUsers(String accountId, String authorizationHeaders, Date startDate,
            Date endDate, String timeSegment, String productSegment,String reviewTrackerAccountUserName) {
        String url = BASE_URL + "users";
        MultiValueMap<String, String> valueMap = new LinkedMultiValueMap<>();
        valueMap.put("account_id", Arrays.asList(accountId));
        valueMap.put("per_page", Arrays.asList("500"));

        String data = getResponse(url, authorizationHeaders, valueMap);
        System.out.println(data);
        return null;
    }

    public static String getResponse(String url, String authorizationHeaders, MultiValueMap<String, String> params) {
        String urlString = url;
        if (params != null) {
            UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl(url).queryParams(params).build();
            urlString = uriComponents.toUriString();
        }
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

}
