/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.admin.service;

import com.google.common.collect.HashBiMap;
import com.visumbu.vb.utils.DateUtils;
import com.visumbu.vb.utils.OauthAuthentication;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
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
    public static String authorizationHeaders;

    public List<Map<String, Object>> get(String dataSetReportName, String reviewTrackerAcessToken, String reviewTrackerAccountId,
            Date startDate, Date endDate, String timeSegment, String productSegment, String reviewTrackerAccountUserName) {
        System.out.println("DatasetReport Name -->" + dataSetReportName);

        String startDateStr = DateUtils.dateToString(startDate, "YYYY-MM-dd");
        String endDateStr = DateUtils.dateToString(endDate, "YYYY-MM-dd");
        authorizationHeaders = OauthAuthentication.buildBasicAuthorizationString(reviewTrackerAccountUserName, reviewTrackerAcessToken);
        if (dataSetReportName.equalsIgnoreCase("getAccountUsers")) {
            return getAccountUsers(reviewTrackerAccountId, authorizationHeaders, startDateStr, endDateStr, timeSegment, productSegment);
        }
        if (dataSetReportName.equalsIgnoreCase("accountReviews")) {
            return getAccountReviews(reviewTrackerAccountId, authorizationHeaders, startDateStr, endDateStr, timeSegment, productSegment);
        }
        if (dataSetReportName.equalsIgnoreCase("overallPerformance")) {
            return getMonthlyReviews(reviewTrackerAccountId, authorizationHeaders, startDateStr, endDateStr, timeSegment, productSegment);
        }
        if (dataSetReportName.equalsIgnoreCase("ratingsBySource")) {
            return getSourcesRating(reviewTrackerAccountId, authorizationHeaders, startDateStr, endDateStr, timeSegment, productSegment);
        }
        if (dataSetReportName.equalsIgnoreCase("overallRatings")) {
            return overallReviewsByRatings(reviewTrackerAccountId, authorizationHeaders, startDateStr, endDateStr, timeSegment, productSegment);
        }
        if (dataSetReportName.equalsIgnoreCase("reviewData")) {
            return reviewData(reviewTrackerAccountId, authorizationHeaders, startDateStr, endDateStr, timeSegment, productSegment);
        }
        return null;
    }

    public static List<Map<String, Object>> getAccountReviews(String accountId, String authorizationHeaders,
            String startDateStr, String endDateStr, String timeSegment, String productSegment) {
        System.out.println("account Id -->" + accountId);
        System.out.println("startDateStr Id -->" + startDateStr);
        System.out.println("endDateStr Id -->" + endDateStr);
        System.out.println("timeSegment Id -->" + timeSegment);
        System.out.println("productSegment Id -->" + productSegment);
        System.out.println("authorizationHeaders Id -->" + authorizationHeaders);
        try {
            List<Map<String, Object>> returnMap = new ArrayList();
            String url = BASE_URL + "reviews";
            MultiValueMap<String, String> valueMap = new LinkedMultiValueMap<>();
            valueMap.put("account_id", Arrays.asList(accountId));
            valueMap.put("per_page", Arrays.asList("500"));
            String data = getResponse(url, authorizationHeaders, valueMap);
            System.out.println("response  data ------->" + data);
            JSONParser parser = new JSONParser();
            Object jsonObj = parser.parse(data);
            JSONObject array = (JSONObject) jsonObj;
            Map<String, Object> reviews = (Map<String, Object>) array.get("_embedded");
            List<Map<String, Object>> allReviewData = (List<Map<String, Object>>) reviews.get("reviews");
            for (Iterator<Map<String, Object>> iterator = allReviewData.iterator(); iterator.hasNext();) {
                Map<String, Object> monthData = iterator.next();
                Map<String, Object> reviewData = new HashMap<>();
                reviewData.put("content", monthData.get("content"));
                reviewData.put("locationName", monthData.get("location_name"));
                reviewData.put("locationState", monthData.get("location_state"));
                reviewData.put("locationZipcode", monthData.get("location_zipcode"));
                reviewData.put("locationCity", monthData.get("location_city"));
                reviewData.put("publishedOn", monthData.get("published_on"));
                reviewData.put("sourceName", monthData.get("source_name"));
                reviewData.put("exactRating", monthData.get("exact_rating"));
                reviewData.put("locationAddress", monthData.get("location_address"));
                returnMap.add(reviewData);
            }
            return returnMap;

        } catch (ParseException ex) {
            Logger.getLogger(ReviewTrackerService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static List<Map<String, Object>> getMonthlyReviews(String accountId, String authorizationHeaders,
            String startDateStr, String endDateStr, String timeSegment, String productSegment) {
        try {
            List<Map<String, Object>> returnMap = new ArrayList();
            String url = BASE_URL + "metrics/" + accountId + "/overview/monthly?";
            MultiValueMap<String, String> valueMap = new LinkedMultiValueMap<>();
            valueMap.put("account_id", Arrays.asList(accountId));
            valueMap.put("month_after", Arrays.asList(startDateStr));
            valueMap.put("month_before", Arrays.asList(endDateStr));
            String data = getResponse(url, authorizationHeaders, valueMap);
            System.out.println(data);
            JSONParser parser = new JSONParser();
            Object jsonObj = parser.parse(data);
            JSONObject array = (JSONObject) jsonObj;
            List<Map<String, Object>> mapData = (List<Map<String, Object>>) array.get("monthly");
            for (Iterator<Map<String, Object>> iterator = mapData.iterator(); iterator.hasNext();) {
                Map<String, Object> monthData = iterator.next();
                Map<String, Object> reviewData = new HashMap<>();
                reviewData.put("month", monthData.get("month"));
                reviewData.put("avgRating", monthData.get("avg_rating"));
                reviewData.put("totalReviews", monthData.get("total_reviews"));
                returnMap.add(reviewData);
            }
            return returnMap;
        } catch (ParseException ex) {
            Logger.getLogger(ReviewTrackerService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static List<Map<String, Object>> getSourcesRating(String accountId, String authorizationHeaders,
            String startDateStr, String endDateStr, String timeSegment, String productSegment) {
        try {
            List<Map<String, Object>> returnMap = new ArrayList();
            String url = BASE_URL + "metrics/" + accountId + "/sources?";
            MultiValueMap<String, String> valueMap = new LinkedMultiValueMap<>();
            valueMap.put("account_id", Arrays.asList(accountId));
            valueMap.put("month_after", Arrays.asList(startDateStr));
            valueMap.put("month_before", Arrays.asList(endDateStr));
            String data = getResponse(url, authorizationHeaders, valueMap);
            System.out.println("URL ===>" + url);
            System.out.println("data----------------" + data);
            JSONParser parser = new JSONParser();
            Object jsonObj = parser.parse(data);
            JSONObject array = (JSONObject) jsonObj;
            List<Map<String, Object>> mapData = (List<Map<String, Object>>) array.get("sources");
            for (Iterator<Map<String, Object>> iterator = mapData.iterator(); iterator.hasNext();) {
                Map<String, Object> monthData = iterator.next();
                Map<String, Object> reviewData = new HashMap<>();
                reviewData.put("sourceName", monthData.get("source_name"));
                reviewData.put("avgRating", monthData.get("avg_rating"));
                reviewData.put("totalReviews", monthData.get("total_reviews"));
                returnMap.add(reviewData);
            }
            return returnMap;
        } catch (ParseException ex) {
            Logger.getLogger(ReviewTrackerService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static List<Map<String, Object>> reviewData(String accountId, String authorizationHeaders,
            String startDateStr, String endDateStr, String timeSegment, String productSegment) {
        try {
            List<Map<String, Object>> returnMap = new ArrayList();
            String url = BASE_URL + "metrics/" + accountId + "/overview?";
            MultiValueMap<String, String> valueMap = new LinkedMultiValueMap<>();
            valueMap.put("account_id", Arrays.asList(accountId));
            valueMap.put("month_after", Arrays.asList(startDateStr));
            valueMap.put("month_before", Arrays.asList(endDateStr));

            String data = getResponse(url, authorizationHeaders, valueMap);
            System.out.println(data);
            JSONParser parser = new JSONParser();
            Object jsonObj = parser.parse(data);
            JSONObject array = (JSONObject) jsonObj;
            Map<String, Object> mapData = new HashMap<>();
            mapData.put("totalReviews", array.get("total_reviews"));
            mapData.put("reputationScore", array.get("avg_rating"));
            returnMap.add(mapData);
            System.out.println("Review Data -->" + returnMap);
            return returnMap;
        } catch (ParseException ex) {
            Logger.getLogger(ReviewTrackerService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static List<Map<String, Object>> overallReviewsByRatings(String accountId, String authorizationHeaders,
            String startDateStr, String endDateStr, String timeSegment, String productSegment) {
        try {
            List<Map<String, Object>> returnMap = new ArrayList();
            String url = BASE_URL + "metrics/" + accountId + "/overview?";
            MultiValueMap<String, String> valueMap = new LinkedMultiValueMap<>();
            valueMap.put("account_id", Arrays.asList(accountId));
            valueMap.put("month_after", Arrays.asList(startDateStr));
            valueMap.put("month_before", Arrays.asList(endDateStr));

            String data = getResponse(url, authorizationHeaders, valueMap);
            System.out.println(data);
            JSONParser parser = new JSONParser();
            Object jsonObj = parser.parse(data);
            JSONObject array = (JSONObject) jsonObj;
            Map<String, Object> mapData = (Map<String, Object>) array.get("ratings");
            for (Map.Entry<String, Object> entry : mapData.entrySet()) {
                Map<String, Object> reviewData = new HashMap<>();
                String key = entry.getKey();
                String value = entry.getValue() + "";
                reviewData.put("stars", key);
                reviewData.put("reviews", value);
                returnMap.add(reviewData);
            }
            return returnMap;
        } catch (ParseException ex) {
            Logger.getLogger(ReviewTrackerService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static List<Map<String, Object>> getAccountUsers(String accountId, String authorizationHeaders, String startDateStr,
            String endDateStr, String timeSegment, String productSegment) {
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
        String jsonResponse = "";
        try {
            URL myURL = new URL(urlString);
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
