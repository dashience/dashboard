/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.api.admin.service;

import com.visumbu.api.bean.ReportPage;
import com.visumbu.api.utils.DateUtils;
import com.visumbu.api.utils.Rest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.httpclient.util.DateUtil;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

/**
 *
 * @author user
 */
@Service("reviewPushService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class ReviewPushService {

    private static String API_KEY = "942f3321f409125186f5e53c4e8c6cc7";
    private static String API_SECRET = "613006c8ff4650c29671f144d8362996";
    private static String API_URL = "https://dashboard.reviewpush.com/api/company/";

    private static final String REVIEW_PUSH_LOCATION_URL = "https://dashboard.reviewpush.com/api/company/locations"; //+ "/11415?api_key=942f3321f409125186f5e53c4e8c6cc7&api_secret=613006c8ff4650c29671f144d8362996&include=location,profile&limit=100";
    private static final String REVIEW_PUSH_REVIEWS_URL = "https://dashboard.reviewpush.com/api/company/reviews";
//            + "?api_key=942f3321f409125186f5e53c4e8c6cc7&api_secret=613006c8ff4650c29671f144d8362996&include=location,profile&limit=100&location_id=11415";

    public Object getReviews(Date startDate, Date endDate, String reviewPushId) {
        String startDateStr = DateUtils.dateToString(startDate, "YYYY-MM-dd");
        String endDateStr = DateUtils.dateToString(endDate, "YYYY-MM-dd");
        try {
            String url = REVIEW_PUSH_REVIEWS_URL + "?api_key=" + API_KEY + "&api_secret=" + API_SECRET + "&include=location,profile&limit=100&"
                    + "location_id=" + reviewPushId + "&date_from=" + startDateStr + "&date_to=" + endDateStr;
            String reviewData = Rest.getData(url);
            JSONParser parser = new JSONParser();
            JSONObject jsonObj = (JSONObject) parser.parse(reviewData);
            JSONArray dataArr = (JSONArray) jsonObj.get("data");
            List returnAll = new ArrayList<>();
            for (int i = 0; i < dataArr.size(); i++) {
                JSONObject dataArr1 = (JSONObject) dataArr.get(i);
                Map dataMap = getDataAsMap(dataArr1);
                dataMap.putAll(getDataAsMap((JSONObject) dataArr1.get("attributes")));
                dataMap.put("review_date", DateUtils.dateToString(DateUtils.toDate(dataMap.get("review_date") + "", "yyyy-MM-dd"), "MM/dd/yyyy"));
                returnAll.add(dataMap);
            }
            return returnAll;
        } catch (ParseException ex) {
            Logger.getLogger(ReviewPushService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private Map<String, String> getDataAsMap(JSONObject data) {
        List<Map<String, String>> returnList = new ArrayList<>();
        Set<String> keySet = data.keySet();
        Map<String, String> dataMap = new HashMap<>();
        for (Iterator<String> iterator = keySet.iterator(); iterator.hasNext();) {
            String key = iterator.next();
            if (!(data.get(key) instanceof JSONArray || data.get(key) instanceof JSONObject)) {
                String value = data.get(key) + "";
                dataMap.put(key, value);
            }
        }
        returnList.add(dataMap);
        return dataMap;
    }
    private Map<String, String> getDataAsMap(JSONObject data, String prefix) {
        List<Map<String, String>> returnList = new ArrayList<>();
        Set<String> keySet = data.keySet();
        Map<String, String> dataMap = new HashMap<>();
        for (Iterator<String> iterator = keySet.iterator(); iterator.hasNext();) {
            String key = iterator.next();
            if (!(data.get(key) instanceof JSONArray || data.get(key) instanceof JSONObject)) {
                String value = data.get(key) + "";
                dataMap.put(prefix + key, value);
            }
        }
        returnList.add(dataMap);
        return dataMap;
    }

    public Object getRatingSummaryByDealer(Date startDate, Date endDate, String reviewPushId) {
        try {
            String url = REVIEW_PUSH_LOCATION_URL + "/" + reviewPushId + "?api_key=" + API_KEY + "&api_secret=" + API_SECRET + "&include=location,profile&limit=100";
            String reviewData = Rest.getData(url);
            JSONParser parser = new JSONParser();
            JSONObject jsonObj = (JSONObject) parser.parse(reviewData);
            List returnAll = new ArrayList<>();
            if (jsonObj.get("data") instanceof JSONArray) {
                JSONArray dataArr = (JSONArray) jsonObj.get("data");
                for (int i = 0; i < dataArr.size(); i++) {
                    JSONObject dataArr1 = (JSONObject) dataArr.get(i);
                    Map dataMap = getDataAsMap(dataArr1);
                    dataMap.putAll(getDataAsMap((JSONObject) dataArr1.get("attributes")));
                    dataMap.putAll(getDataAsMap((JSONObject) ((JSONObject) dataArr1.get("attributes")).get("aggregate")));
                    returnAll.add(dataMap);
                }
            } else {
                JSONObject dataArr = (JSONObject) jsonObj.get("data");
                Map dataMap = getDataAsMap(dataArr);
                dataMap.putAll(getDataAsMap((JSONObject) dataArr.get("attributes")));
                dataMap.putAll(getDataAsMap((JSONObject) ((JSONObject) dataArr.get("attributes")).get("aggregate")));
                returnAll.add(dataMap);

            }
            return returnAll;
            //return jsonObj;
        } catch (ParseException ex) {
            Logger.getLogger(ReviewPushService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public Object getBySources(Date startDate, Date endDate, String reviewPushId) {
        String startDateStr = DateUtils.dateToString(startDate, "YYYY-MM-dd");
        String endDateStr = DateUtils.dateToString(endDate, "YYYY-MM-dd");
        try {
            String url = REVIEW_PUSH_REVIEWS_URL + "?api_key=" + API_KEY + "&api_secret=" + API_SECRET + "&include=location,profile&limit=100&"
                    + "location_id=" + reviewPushId + "&date_from=" + startDateStr + "&date_to=" + endDateStr;
            String reviewData = Rest.getData(url);
            JSONParser parser = new JSONParser();
            JSONObject jsonObj = (JSONObject) parser.parse(reviewData);
            JSONArray dataArr = (JSONArray) jsonObj.get("included");
            List returnAll = new ArrayList<>();
            for (int i = 1; i < dataArr.size(); i++) {
                JSONObject dataArr1 = (JSONObject) dataArr.get(i);
                Map dataMap = getDataAsMap(dataArr1);
                dataMap.putAll(getDataAsMap((JSONObject) dataArr1.get("attributes")));
                dataMap.putAll(getDataAsMap((JSONObject) ((JSONObject) dataArr1.get("attributes")).get("site"), "site_"));
                dataMap.putAll(getDataAsMap((JSONObject) ((JSONObject) dataArr1.get("attributes")).get("aggregate")));
                if(((String)dataMap.get("site_name")).equalsIgnoreCase("ReviewPush")){
                    dataMap.put("site_name", "Private Reviews");
                }
                returnAll.add(dataMap);
            }
            return returnAll;
        } catch (ParseException ex) {
            Logger.getLogger(ReviewPushService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
