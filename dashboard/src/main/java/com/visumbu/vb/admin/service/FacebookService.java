/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.admin.service;

import com.visumbu.vb.utils.ApiUtils;
import com.visumbu.vb.utils.DateUtils;
import com.visumbu.vb.utils.JsonSimpleUtils;
//import com.visumbu.vb.utils.ExampleConfig;
import com.visumbu.vb.utils.Rest;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author jp
 */
@Service("facebookService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class FacebookService {

//    public final String ACCESS_TOKEN = ExampleConfig.ACCESS_TOKEN;
//   old-- public String ACCESS_TOKEN = "EAAXL1ZCQXlg0BAPqilppp0oaYcitsMNxK0EReU3ght4VP50BAFunLsXNE4GAYRJ4VtYjr67GbJYVHwx1wn80aWSg26l27MPI7NH1m86JZBsgWy5yz4P98x8DoGDGD1wwJ5lDBxXRHDA2ZC1rYdZBroXpZA7qOS2CJQYdwKgdHWAZDZD";
    public String ACCESS_TOKEN = "EAAUAycrj0GsBAM3EgwLcQjz5zywESZBpHN76cERZCaxEZC9ZAzMjRzRxIznWM3u8s4DBwUvhMaQAGglDOIa9tSV7ZCVf9ZBajV9aA6khaCRmEZAQhIHUInBVYZBZAT5nycwniZCozuLcjhTm0eW5tAUxIugmvxszsivmh5ZClzuMZApZBJxd0RZBIDk1r0";
    public String ORGANIC_ACCESS_TOKEN = "EAAUAycrj0GsBAM3EgwLcQjz5zywESZBpHN76cERZCaxEZC9ZAzMjRzRxIznWM3u8s4DBwUvhMaQAGglDOIa9tSV7ZCVf9ZBajV9aA6khaCRmEZAQhIHUInBVYZBZAT5nycwniZCozuLcjhTm0eW5tAUxIugmvxszsivmh5ZClzuMZApZBJxd0RZBIDk1r0";

    public final String APP_SECRET = "b6659b47ba7b2b11179247bb3cd84f70";
    // public final Long ACCOUNT_ID = ExampleConfig.ACCOUNT_ID;
//    public final String APP_SECRET = ExampleConfig.APP_SECRET;
    public final String BASE_URL = "https://graph.facebook.com/v2.8/act_";
    public final String BASE_URL_FEED = "https://graph.facebook.com/v2.8/";
    //public final APIContext context = new APIContext(ACCESS_TOKEN).enableDebug(true);

    public List<Map<String, Object>> get(String accessToken, String dataSet, Long accountId, Long organicAccountId, Date startDate, Date endDate, String aggregation,String productSegment) {
        this.ACCESS_TOKEN = accessToken;
        if (aggregation == null) {
            aggregation = "";
        }
        if (dataSet.equalsIgnoreCase("accountPerformance")) {
            return getAccountPerformance(accountId, startDate, endDate, aggregation);
        }
        if (dataSet.equalsIgnoreCase("campaignPerformance")) {
            return getCampaignPerformance(accountId, startDate, endDate, aggregation);
        }
        if (dataSet.equalsIgnoreCase("adPerformance")) {
            return getAdPerformance(accountId, startDate, endDate, aggregation);
        }
        if (dataSet.equalsIgnoreCase("devicePerformance")) {
            return getDevicePerformance(accountId, startDate, endDate, aggregation);
        }
        if (dataSet.equalsIgnoreCase("agePerformance")) {
            return getAgePerformance(accountId, startDate, endDate, aggregation);
        }
        if (dataSet.equalsIgnoreCase("genderPerformance")) {
            return getGenderPerformance(accountId, startDate, endDate, aggregation);
        }
        if (dataSet.equalsIgnoreCase("postPerformance")) {
            return getPostPerformance(accountId, startDate, endDate, aggregation);
        }
        if (dataSet.equalsIgnoreCase("postSummary")) {
            return getPostSummary(accountId, startDate, endDate, aggregation);
        }
        if (dataSet.equalsIgnoreCase("instagramPerformance")) {
            return getInstagramPerformance(accountId, startDate, endDate, aggregation);
        }
        if (dataSet.equalsIgnoreCase("recentPostPerformance")) {
            return getRecentPostPerformance(organicAccountId, startDate, endDate, aggregation);
        }
        if (dataSet.equalsIgnoreCase("totalOrganicLikes")) {
            return getTotalOrganicLikes(organicAccountId, startDate, endDate, aggregation);
        }
        if (dataSet.equalsIgnoreCase("totalPageViews")) {
            return getTotalPageViews(organicAccountId, startDate, endDate, aggregation);
        }
        if (dataSet.equalsIgnoreCase("totalEngagements")) {
            return getTotalEngagements(organicAccountId, startDate, endDate, aggregation);
        }
        if (dataSet.equalsIgnoreCase("totalReach")) {
            return getTotalReach(organicAccountId, startDate, endDate, aggregation);
        }
        if (dataSet.equalsIgnoreCase("pageLikesByCity")) {
            return getPageLikesByCity(organicAccountId, startDate, endDate, aggregation);
        }
        //getTotalOrganicLikes

        return null;
    }

    public String getFbPublishedPosts(Long accountId) {
        String url = BASE_URL + accountId + "/insights?fields=adset_name%2Cclicks%2Cimpressions&date_preset=today&access_token=" + ACCESS_TOKEN;
        return Rest.getData(url);
    }

    public List<Map<String, Object>> getPageLikesByCity(Long accountId, Date startDate, Date endDate, String aggregation) {
        try {
//            String fbUrl = "https://graph.facebook.com/" + accountId + "/insights/page_fans_city?access_token=" + ACCESS_TOKEN;
            String fbUrl = "https://graph.facebook.com/v2.9/185042698207211/insights/page_fans_city?access_token=" + ORGANIC_ACCESS_TOKEN;
            String data = Rest.getData(fbUrl);
            JSONParser parser = new JSONParser();
            if (data == null) {
                return null;
            }
            Object jsonObj = parser.parse(data);
            JSONObject json = (JSONObject) jsonObj;
            Map<String, Object> jsonToMap = JsonSimpleUtils.jsonToMap(json);
            Map returnMap = new HashMap<>();
            List<Map> fbData = (List<Map>) jsonToMap.get("data");
            List<Map<String, Object>> listData = new ArrayList<>();
            Map fbFansData = (Map) fbData.get(0);
            List fbLikesList = (List) fbFansData.get("values");
            System.out.print("**************** City by likes --------------------");
            Map<String, Object> values = (Map<String, Object>) ((Map) fbLikesList.get(0)).get("value");
            System.out.print(values);

            for (Map.Entry<String, Object> entry : values.entrySet()) {
                String key = entry.getKey();
                System.out.print("key--->" + key);
                Object value = entry.getValue();
                System.out.print("values ----->");
                System.out.print(value);
                Map<String, Object> dataMap = new HashMap<>();
                dataMap.put("city", key + "");
                dataMap.put("likes", value + "");
                listData.add(dataMap);
            }

            return listData;
        } catch (ParseException ex) {
            Logger.getLogger(FacebookService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public List<Map<String, Object>> getTotalReach(Long accountId, Date startDate, Date endDate, String aggregation) {
        try {
//            String fbUrl = "https://graph.facebook.com/" + accountId + "/insights/page_impressions_organic_unique?access_token=" + ACCESS_TOKEN;
            String fbUrl = "https://graph.facebook.com/v2.9/185042698207211/insights/page_impressions_organic_unique?access_token=" + ORGANIC_ACCESS_TOKEN;
            String data = Rest.getData(fbUrl);
            JSONParser parser = new JSONParser();
            if (data == null) {
                return null;
            }
            Object jsonObj = parser.parse(data);
            JSONObject json = (JSONObject) jsonObj;
            Map<String, Object> jsonToMap = JsonSimpleUtils.jsonToMap(json);
            Map returnMap = new HashMap<>();
            List fbData = (List<Map>) jsonToMap.get("data");
            List<Map<String, Object>> listData = new ArrayList<>();
            Map fbFansData = (Map) fbData.get(2);
            List fbLikesList = (List) fbFansData.get("values");
            System.out.println("=====================");
            System.out.println(fbLikesList.get(0));
            String values = ((Map) fbLikesList.get(0)).get("value") + "";
            Map<String, Object> returnMapData = new HashMap<>();
            returnMapData.put("reach", values);
            listData.add(returnMapData);
            return listData;
        } catch (ParseException ex) {
            Logger.getLogger(FacebookService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public List<Map<String, Object>> getTotalEngagements(Long accountId, Date startDate, Date endDate, String aggregation) {
        try {
//            String fbUrl = "https://graph.facebook.com/" + accountId + "/insights/page_engaged_users?access_token=" + ACCESS_TOKEN;
            String fbUrl = "https://graph.facebook.com/v2.9/185042698207211/insights/page_engaged_users?access_token=" + ORGANIC_ACCESS_TOKEN;
            String data = Rest.getData(fbUrl);
            JSONParser parser = new JSONParser();
            if (data == null) {
                return null;
            }
            Object jsonObj = parser.parse(data);
            JSONObject json = (JSONObject) jsonObj;
            Map<String, Object> jsonToMap = JsonSimpleUtils.jsonToMap(json);
            Map returnMap = new HashMap<>();
            List fbData = (List<Map>) jsonToMap.get("data");
            List<Map<String, Object>> listData = new ArrayList<>();
            Map fbFansData = (Map) fbData.get(2);
            List fbLikesList = (List) fbFansData.get("values");
            String values = ((Map) fbLikesList.get(0)).get("value") + "";
            Map<String, Object> returnMapData = new HashMap<>();
            returnMapData.put("engagements", values);
            listData.add(returnMapData);
            return listData;
        } catch (ParseException ex) {
            Logger.getLogger(FacebookService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public List<Map<String, Object>> getTotalPageViews(Long accountId, Date startDate, Date endDate, String aggregation) {
        try {

            String fbUrl = "https://graph.facebook.com/v2.9/185042698207211/insights/page_views_total?access_token=" + ORGANIC_ACCESS_TOKEN;
//            String fbUrl = "https://graph.facebook.com/" + accountId + "/insights/page_views_total?access_token=" + ACCESS_TOKEN;
            String data = Rest.getData(fbUrl);
            JSONParser parser = new JSONParser();
            if (data == null) {
                return null;
            }
            Object jsonObj = parser.parse(data);
            JSONObject json = (JSONObject) jsonObj;
            Map<String, Object> jsonToMap = JsonSimpleUtils.jsonToMap(json);
            Map returnMap = new HashMap<>();
            List fbData = (List<Map>) jsonToMap.get("data");
            List<Map<String, Object>> listData = new ArrayList<>();
            Map fbFansData = (Map) fbData.get(2);
            List fbLikesList = (List) fbFansData.get("values");
            System.out.println("=====================");
            System.out.println(fbLikesList.get(0));
            String values = ((Map) fbLikesList.get(0)).get("value") + "";
            Map<String, Object> returnMapData = new HashMap<>();
            returnMapData.put("page_views", values);
            listData.add(returnMapData);
            return listData;
        } catch (ParseException ex) {
            Logger.getLogger(FacebookService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public List<Map<String, Object>> getRecentPostPerformance(Long accountId, Date startDate, Date endDate, String aggregation) {
        try {
            String startDateStr = DateUtils.dateToString(startDate, "YYYY-MM-dd");
            String endDateStr = DateUtils.dateToString(endDate, "YYYY-MM-dd");
//            String url = "https://graph.facebook.com/v2.9/" + accountId + "posts?fields=message,likes,comments"
//                    + "&access_token=" + ACCESS_TOKEN;
            String fbUrl = "https://graph.facebook.com/v2.9/185042698207211/posts?fields=message,likes,comments&access_token=" + ORGANIC_ACCESS_TOKEN;
            String data = Rest.getData(fbUrl);
            System.out.println("data ---> " + data);
            JSONParser parser = new JSONParser();
            if (data == null) {
                return null;
            }
            Object jsonObj = parser.parse(data);
            JSONObject json = (JSONObject) jsonObj;
            Map<String, Object> jsonToMap = JsonSimpleUtils.jsonToMap(json);
            Map returnMap = new HashMap<>();
            List<Map> fbData = (List<Map>) jsonToMap.get("data");
            List<Map<String, Object>> listData = new ArrayList<>();
            for (Iterator<Map> iterator = fbData.iterator(); iterator.hasNext();) {
                Map fbDataMap = iterator.next();
                Map<String, Object> fbDataObj = new HashMap<>();
                fbDataObj.put("message", fbDataMap.get("message") + "");
                fbDataObj.put("likes", ((List) ((Map) fbDataMap.get("likes")).get("data")).size() + "");
                listData.add(fbDataObj);

            }
            return listData;
        } catch (ParseException ex) {
            Logger.getLogger(FacebookService.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    public List<Map<String, Object>> getTotalOrganicLikes(Long accountId, Date startDate, Date endDate, String aggregation) {
        try {
            // String fbUrl = "https://graph.facebook.com/v2.9/" + accountId + "/insights/page_fans?access_token=" + ACCESS_TOKEN;
            String fbUrl = "https://graph.facebook.com/v2.9/185042698207211/insights/page_fans?access_token=" + ORGANIC_ACCESS_TOKEN;
            String data = Rest.getData(fbUrl);
            JSONParser parser = new JSONParser();
            if (data == null) {
                return null;
            }
            Object jsonObj = parser.parse(data);
            JSONObject json = (JSONObject) jsonObj;
            Map<String, Object> jsonToMap = JsonSimpleUtils.jsonToMap(json);
            Map returnMap = new HashMap<>();
            List fbData = (List<Map>) jsonToMap.get("data");
            List<Map<String, Object>> listData = new ArrayList<>();
            Map fbFansData = (Map) fbData.get(0);
            List fbLikesList = (List) fbFansData.get("values");
            String values = ((Map) fbLikesList.get(0)).get("value") + "";
            Map<String, Object> returnMapData = new HashMap<>();
            returnMapData.put("page_fans", values);
            listData.add(returnMapData);
            return listData;
        } catch (ParseException ex) {
            Logger.getLogger(FacebookService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public List<Map<String, Object>> getAccountPerformance(Long accountId, Date startDate, Date endDate, String aggregation) {
        try {
            String startDateStr = DateUtils.dateToString(startDate, "YYYY-MM-dd");
            String endDateStr = DateUtils.dateToString(endDate, "YYYY-MM-dd");
            System.out.println("account Performance");

            String url = BASE_URL + accountId + "/insights?fields=account_name,impressions,clicks,ctr,cpc,spend,actions,reach,cost_per_action_type,adset_name&"
                    + "time_range[since]=" + startDateStr + "&time_range[until]=" + endDateStr
                    + "&access_token=" + ACCESS_TOKEN;
            if (aggregation.equalsIgnoreCase("week")) {
                url += "&time_increment=7";
            }
            if (aggregation.equalsIgnoreCase("day")) {
                url += "&time_increment=1";
            }
            if (aggregation.equalsIgnoreCase("month")) {
                url += "&time_increment=30";
            }
            if (aggregation.equalsIgnoreCase("year")) {
                url += "&time_increment=365";
            }
            String fbData = Rest.getData(url);
            JSONParser parser = new JSONParser();
            if (fbData == null) {
                return null;
            }
            Object jsonObj = parser.parse(fbData);
            JSONObject array = (JSONObject) jsonObj;
            JSONArray dataArr = (JSONArray) array.get("data");
            List<Map<String, Object>> dataValueList = new ArrayList();
            for (int i = 0; i < dataArr.size(); i++) {
                JSONObject data = (JSONObject) dataArr.get(i);
                JSONArray actionsArr = (JSONArray) data.get("actions");
                //JSONObject actions = (JSONObject) actionsArr.get(0);
                List<Map<String, Object>> returnList = new ArrayList<>();
                JSONArray costPerActionTypeArr = (JSONArray) data.get("cost_per_action_type");
                Map<String, Object> dataList = getDataValue(data);
                if (actionsArr != null) {
                    dataList.putAll(getActionsData(actionsArr, "actions_"));
                }
                if (costPerActionTypeArr != null) {
                    dataList.putAll(getActionsData(costPerActionTypeArr, "cost_"));
                }
                dataList.put("ctr", ApiUtils.removePercent(dataList.get("ctr")+""));
                dataValueList.add(dataList);
            }
            return dataValueList;  //getActions(actionsArr);
            //return getActions(actions); //array.get("data");
        } catch (ParseException ex) {
            Logger.getLogger(FacebookService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public List<Map<String, Object>> getCampaignPerformance(Long accountId, Date startDate, Date endDate, String aggregation) {
        try {
            String startDateStr = DateUtils.dateToString(startDate, "YYYY-MM-dd");
            String endDateStr = DateUtils.dateToString(endDate, "YYYY-MM-dd");
//https://graph.facebook.com/v2.8/act_10153963646170050/insights?level=campaign&fields=account_name,impressions,clicks,ctr,cpc,spend,actions,reach,cost_per_action_type,campaign_name&time_range[since]=2016-10-01&time_range[until]=2016-10-31&access_token=EAANFRJpxZBZC0BAAqAeGjVgawF8X58ZCYRU824xzKpDcCN49s3wMGqie9MRdUZBnSK8pTsFw3KSOvfof88Oib6CCIOZBlnYQkkeYJrYdyOTJoELEZAmFAFKMoBg5cWvgbdnXdHmZAcYwsJQ6xL1XnMd8m6Hz4C7SAESJQLb36Qh0VSR3gIhiJOw

            String url = BASE_URL + accountId + "/insights?level=campaign&fields=account_name,impressions,clicks,ctr,cpc,spend,actions,reach,cost_per_action_type,campaign_name&"
                    + "time_range[since]=" + startDateStr + "&time_range[until]=" + endDateStr
                    + "&access_token=" + ACCESS_TOKEN;
            if (aggregation.equalsIgnoreCase("weekly")) {
                url += "&time_increment=7";
            }
            if (aggregation.equalsIgnoreCase("daily")) {
                url += "&time_increment=1";
            }
            String fbData = Rest.getData(url);
            JSONParser parser = new JSONParser();
            if (fbData == null) {
                return null;
            }
            Object jsonObj = parser.parse(fbData);
            JSONObject array = (JSONObject) jsonObj;
            JSONArray dataArr = (JSONArray) array.get("data");
            List<Map<String, Object>> dataValueList = new ArrayList();
            for (int i = 0; i < dataArr.size(); i++) {
                JSONObject data = (JSONObject) dataArr.get(i);
                JSONArray actionsArr = (JSONArray) data.get("actions");
                //JSONObject actions = (JSONObject) actionsArr.get(0);
                List<Map<String, Object>> returnList = new ArrayList<>();
                JSONArray costPerActionTypeArr = (JSONArray) data.get("cost_per_action_type");
                Map<String, Object> dataList = getDataValue(data);
                if (actionsArr != null) {
                    dataList.putAll(getActionsData(actionsArr, "actions_"));
                }
                if (costPerActionTypeArr != null) {
                    dataList.putAll(getActionsData(costPerActionTypeArr, "cost_"));
                }
                dataList.put("ctr", ApiUtils.removePercent(dataList.get("ctr")+""));

                dataValueList.add(dataList);
            }
            return dataValueList;  //getActions(actionsArr);
            //return getActions(actions); //array.get("data");
        } catch (ParseException ex) {
            Logger.getLogger(FacebookService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public List<Map<String, Object>> getAdPerformance(Long accountId, Date startDate, Date endDate, String aggregation) {
        try {
            String startDateStr = DateUtils.dateToString(startDate, "YYYY-MM-dd");
            String endDateStr = DateUtils.dateToString(endDate, "YYYY-MM-dd");
//https://graph.facebook.com/v2.8/act_10153963646170050/insights?level=ad&fields=account_name,impressions,clicks,ctr,cpc,spend,actions,reach,cost_per_action_type,adset_name&time_range[since]=2016-10-01&time_range[until]=2016-10-31&access_token=EAANFRJpxZBZC0BAAqAeGjVgawF8X58ZCYRU824xzKpDcCN49s3wMGqie9MRdUZBnSK8pTsFw3KSOvfof88Oib6CCIOZBlnYQkkeYJrYdyOTJoELEZAmFAFKMoBg5cWvgbdnXdHmZAcYwsJQ6xL1XnMd8m6Hz4C7SAESJQLb36Qh0VSR3gIhiJOw

            String url = BASE_URL + accountId + "/insights?level=ad&fields=account_name,impressions,clicks,ctr,cpc,spend,actions,reach,cost_per_action_type,adset_name&"
                    + "time_range[since]=" + startDateStr + "&time_range[until]=" + endDateStr
                    + "&access_token=" + ACCESS_TOKEN;
            if (aggregation.equalsIgnoreCase("weekly")) {
                url += "&time_increment=7";
            }
            if (aggregation.equalsIgnoreCase("daily")) {
                url += "&time_increment=1";
            }
            String fbData = Rest.getData(url);
            JSONParser parser = new JSONParser();
            if (fbData == null) {
                return null;
            }
            Object jsonObj = parser.parse(fbData);
            JSONObject array = (JSONObject) jsonObj;
            JSONArray dataArr = (JSONArray) array.get("data");
            List<Map<String, Object>> dataValueList = new ArrayList();
            for (int i = 0; i < dataArr.size(); i++) {
                JSONObject data = (JSONObject) dataArr.get(i);
                JSONArray actionsArr = (JSONArray) data.get("actions");
                //JSONObject actions = (JSONObject) actionsArr.get(0);
                List<Map<String, Object>> returnList = new ArrayList<>();
                JSONArray costPerActionTypeArr = (JSONArray) data.get("cost_per_action_type");
                Map<String, Object> dataList = getDataValue(data);
                if (actionsArr != null) {
                    dataList.putAll(getActionsData(actionsArr, "actions_"));
                }
                if (costPerActionTypeArr != null) {
                    dataList.putAll(getActionsData(costPerActionTypeArr, "cost_"));
                }
                dataList.put("ctr", ApiUtils.removePercent(dataList.get("ctr")+""));
                dataValueList.add(dataList);
            }
            return dataValueList;  //getActions(actionsArr);
            //return getActions(actions); //array.get("data");
        } catch (ParseException ex) {
            Logger.getLogger(FacebookService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public List<Map<String, Object>> getDevicePerformance(Long accountId, Date startDate, Date endDate, String aggregation) {
        try {
            String startDateStr = DateUtils.dateToString(startDate, "YYYY-MM-dd");
            String endDateStr = DateUtils.dateToString(endDate, "YYYY-MM-dd");
//https://graph.facebook.com/v2.8/act_10153963646170050/insights?fields=clicks,impressions,ctr,spend,actions,cost_per_action_type&time_range[since]=2016-10-01&time_range[until]=2016-10-31&breakdowns=impression_device&access_token=EAANFRJpxZBZC0BAAqAeGjVgawF8X58ZCYRU824xzKpDcCN49s3wMGqie9MRdUZBnSK8pTsFw3KSOvfof88Oib6CCIOZBlnYQkkeYJrYdyOTJoELEZAmFAFKMoBg5cWvgbdnXdHmZAcYwsJQ6xL1XnMd8m6Hz4C7SAESJQLb36Qh0VSR3gIhiJOw 

            String url = BASE_URL + accountId + "/insights?fields=clicks,impressions,ctr,cpc,spend,actions,cost_per_action_type&breakdowns=impression_device&"
                    + "time_range[since]=" + startDateStr + "&time_range[until]=" + endDateStr
                    + "&access_token=" + ACCESS_TOKEN;
            if (aggregation.equalsIgnoreCase("weekly")) {
                url += "&time_increment=7";
            }
            if (aggregation.equalsIgnoreCase("daily")) {
                url += "&time_increment=1";
            }
            String fbData = Rest.getData(url);
            JSONParser parser = new JSONParser();
            if (fbData == null) {
                return null;
            }
            Object jsonObj = parser.parse(fbData);
            JSONObject array = (JSONObject) jsonObj;
            JSONArray dataArr = (JSONArray) array.get("data");
            List<Map<String, Object>> dataValueList = new ArrayList();
            for (int i = 0; i < dataArr.size(); i++) {
                JSONObject data = (JSONObject) dataArr.get(i);
                JSONArray actionsArr = (JSONArray) data.get("actions");
                //JSONObject actions = (JSONObject) actionsArr.get(0);
                List<Map<String, Object>> returnList = new ArrayList<>();
                JSONArray costPerActionTypeArr = (JSONArray) data.get("cost_per_action_type");
                Map<String, Object> dataList = getDataValue(data);
                if (actionsArr != null) {
                    dataList.putAll(getActionsData(actionsArr, "actions_"));
                }
                if (costPerActionTypeArr != null) {
                    dataList.putAll(getActionsData(costPerActionTypeArr, "cost_"));
                }
                dataList.put("ctr", ApiUtils.removePercent(dataList.get("ctr")+""));
                dataValueList.add(dataList);
            }
            return dataValueList;
            //return getActions(actions); //array.get("data");
        } catch (ParseException ex) {
            Logger.getLogger(FacebookService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public List<Map<String, Object>> getAgePerformance(Long accountId, Date startDate, Date endDate, String aggregation) {
        try {
            String startDateStr = DateUtils.dateToString(startDate, "YYYY-MM-dd");
            String endDateStr = DateUtils.dateToString(endDate, "YYYY-MM-dd");
//https://graph.facebook.com/v2.8/act_10153963646170050/insights?fields=clicks,impressions,ctr,spend,actions,cost_per_action_type&time_range[since]=2016-10-01&time_range[until]=2016-10-31&breakdowns=age&access_token=EAANFRJpxZBZC0BAAqAeGjVgawF8X58ZCYRU824xzKpDcCN49s3wMGqie9MRdUZBnSK8pTsFw3KSOvfof88Oib6CCIOZBlnYQkkeYJrYdyOTJoELEZAmFAFKMoBg5cWvgbdnXdHmZAcYwsJQ6xL1XnMd8m6Hz4C7SAESJQLb36Qh0VSR3gIhiJOw

            String url = BASE_URL + accountId + "/insights?fields=clicks,impressions,ctr,cpc,spend,actions,cost_per_action_type&breakdowns=age&"
                    + "time_range[since]=" + startDateStr + "&time_range[until]=" + endDateStr
                    + "&access_token=" + ACCESS_TOKEN;
            if (aggregation.equalsIgnoreCase("weekly")) {
                url += "&time_increment=7";
            }
            if (aggregation.equalsIgnoreCase("daily")) {
                url += "&time_increment=1";
            }
            String fbData = Rest.getData(url);
            JSONParser parser = new JSONParser();
            if (fbData == null) {
                return null;
            }
            Object jsonObj = parser.parse(fbData);
            JSONObject array = (JSONObject) jsonObj;
            JSONArray dataArr = (JSONArray) array.get("data");
            List<Map<String, Object>> dataValueList = new ArrayList();
            for (int i = 0; i < dataArr.size(); i++) {
                JSONObject data = (JSONObject) dataArr.get(i);
                JSONArray actionsArr = (JSONArray) data.get("actions");
                //JSONObject actions = (JSONObject) actionsArr.get(0);
                List<Map<String, Object>> returnList = new ArrayList<>();
                JSONArray costPerActionTypeArr = (JSONArray) data.get("cost_per_action_type");
                Map<String, Object> dataList = getDataValue(data);
                if (actionsArr != null) {
                    dataList.putAll(getActionsData(actionsArr, "actions_"));
                }
                if (costPerActionTypeArr != null) {
                    dataList.putAll(getActionsData(costPerActionTypeArr, "cost_"));
                }
                dataList.put("ctr", ApiUtils.removePercent(dataList.get("ctr")+""));
                dataValueList.add(dataList);
            }
            return dataValueList;
            //return getActions(actions); //array.get("data");
        } catch (ParseException ex) {
            Logger.getLogger(FacebookService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public List<Map<String, Object>> getGenderPerformance(Long accountId, Date startDate, Date endDate, String aggregation) {
        try {
            String startDateStr = DateUtils.dateToString(startDate, "YYYY-MM-dd");
            String endDateStr = DateUtils.dateToString(endDate, "YYYY-MM-dd");
//https://graph.facebook.com/v2.8/act_10153963646170050/insights?fields=clicks,impressions,ctr,spend,actions,cost_per_action_type&time_range[since]=2016-10-01&time_range[until]=2016-10-31&breakdowns=gender&access_token=EAANFRJpxZBZC0BAAqAeGjVgawF8X58ZCYRU824xzKpDcCN49s3wMGqie9MRdUZBnSK8pTsFw3KSOvfof88Oib6CCIOZBlnYQkkeYJrYdyOTJoELEZAmFAFKMoBg5cWvgbdnXdHmZAcYwsJQ6xL1XnMd8m6Hz4C7SAESJQLb36Qh0VSR3gIhiJOw

            String url = BASE_URL + accountId + "/insights?fields=clicks,impressions,ctr,cpc,spend,actions,cost_per_action_type&breakdowns=gender&"
                    + "time_range[since]=" + startDateStr + "&time_range[until]=" + endDateStr
                    + "&access_token=" + ACCESS_TOKEN;
            if (aggregation.equalsIgnoreCase("weekly")) {
                url += "&time_increment=7";
            }
            if (aggregation.equalsIgnoreCase("daily")) {
                url += "&time_increment=1";
            }
            String fbData = Rest.getData(url);
            JSONParser parser = new JSONParser();
            if (fbData == null) {
                return null;
            }
            Object jsonObj = parser.parse(fbData);
            JSONObject array = (JSONObject) jsonObj;
            JSONArray dataArr = (JSONArray) array.get("data");
            List<Map<String, Object>> dataValueList = new ArrayList<>();
            for (int i = 0; i < dataArr.size(); i++) {
                JSONObject data = (JSONObject) dataArr.get(i);
                JSONArray actionsArr = (JSONArray) data.get("actions");
                //JSONObject actions = (JSONObject) actionsArr.get(0);
                List<Map<String, Object>> returnList = new ArrayList<>();
                JSONArray costPerActionTypeArr = (JSONArray) data.get("cost_per_action_type");
                Map<String, Object> dataList = getDataValue(data);
                if (actionsArr != null) {
                    dataList.putAll(getActionsData(actionsArr, "actions_"));
                }
                if (costPerActionTypeArr != null) {
                    dataList.putAll(getActionsData(costPerActionTypeArr, "cost_"));
                }
                dataList.put("ctr", ApiUtils.removePercent(dataList.get("ctr")+""));
                dataValueList.add(dataList);
            }
            return dataValueList;
            //return getActions(actions); //array.get("data");
        } catch (ParseException ex) {
            Logger.getLogger(FacebookService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public List<Map<String, Object>> getPostPerformance(Long accountId, Date startDate, Date endDate, String aggregation) {
        try {
            String startDateStr = DateUtils.dateToString(startDate, "YYYY-MM-dd");
            String endDateStr = DateUtils.dateToString(endDate, "YYYY-MM-dd");
// https://graph.facebook.com/v2.8/110571071477/feed?fields=shares,likes,message,reactions,comments{message,comment_count,created_time,like_count},created_time,type&use_actual_created_time_for_backdated_post=true&until=2016-10-31&since=2015-10-01
            //&access_token=EAANFRJpxZBZC0BAAqAeGjVgawF8X58ZCYRU824xzKpDcCN49s3wMGqie9MRdUZBnSK8pTsFw3KSOvfof88Oib6CCIOZBlnYQkkeYJrYdyOTJoELEZAmFAFKMoBg5cWvgbdnXdHmZAcYwsJQ6xL1XnMd8m6Hz4C7SAESJQLb36Qh0VSR3gIhiJOw&limit=100

            String url = BASE_URL_FEED + accountId + "/feed?fields=shares,likes,message,reactions,comments{message,comment_count,created_time,like_count},created_time,type&use_actual_created_time_for_backdated_post=true&limit=100"
                    + "time_range[since]=" + startDateStr + "&time_range[until]=" + endDateStr
                    + "&access_token=" + ACCESS_TOKEN;
//            String url = BASE_URL_FEED + "110571071477" + "/feed?fields=shares,likes,message,reactions,comments{message,comment_count,created_time,like_count},created_time,type&use_actual_created_time_for_backdated_post=true&limit=100"
//                    + "time_range[since]=" + startDateStr + "&time_range[until]=" + endDateStr
//                    + "&access_token=" + ACCESS_TOKEN;
            if (aggregation.equalsIgnoreCase("weekly")) {
                url += "&time_increment=7";
            }
            if (aggregation.equalsIgnoreCase("daily")) {
                url += "&time_increment=1";
            }
            String fbData = Rest.getData(url);
            JSONParser parser = new JSONParser();
            if (fbData == null) {
                return null;
            }
            Object jsonObj = parser.parse(fbData);
            JSONObject array = (JSONObject) jsonObj;
            JSONArray dataArr = (JSONArray) array.get("data");
            List<Map<String, Object>> dataValueList = new ArrayList();
            for (int i = 0; i < dataArr.size(); i++) {
                JSONObject data = (JSONObject) dataArr.get(i);
                Map<String, Object> dataList = getDataValue(data);
                dataList.put("created_time", DateUtils.dateToString(DateUtils.toDate(dataList.get("created_time")+"".replace("+0000", "").replace("T", " "), "yyyy-MM-dd HH:mm:ss"), "MM/dd/yyyy HH:mm"));
                dataList.put("date", startDateStr);
                dataList.put("reactions", getActionsCount((JSONObject) data.get("reactions")) + "");
                dataList.put("likes", getActionsCount((JSONObject) data.get("likes")) + "");
                dataList.put("comments", getActionsCount((JSONObject) data.get("comments")) + "");
                dataList.put("shares", getShareCount((JSONObject) data.get("shares")) + "");
                dataList.put("engagements", (Long.parseLong(dataList.get("shares")+"") + Long.parseLong(dataList.get("likes")+"") + Long.parseLong(dataList.get("comments")+"")) + "");
                dataValueList.add(dataList);
            }
            return dataValueList;
            //return getActions(actions); //array.get("data");
        } catch (ParseException ex) {
            Logger.getLogger(FacebookService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private List<Map<String, Object>> getPostSummary(Long accountId, Date startDate, Date endDate, String aggregation) {
        try {
            String startDateStr = DateUtils.dateToString(startDate, "YYYY-MM-dd");
            String endDateStr = DateUtils.dateToString(endDate, "YYYY-MM-dd");
// https://graph.facebook.com/v2.8/110571071477/feed?fields=shares,likes,message,reactions,comments{message,comment_count,created_time,like_count},created_time,type&use_actual_created_time_for_backdated_post=true&until=2016-10-31&since=2015-10-01
            //&access_token=EAANFRJpxZBZC0BAAqAeGjVgawF8X58ZCYRU824xzKpDcCN49s3wMGqie9MRdUZBnSK8pTsFw3KSOvfof88Oib6CCIOZBlnYQkkeYJrYdyOTJoELEZAmFAFKMoBg5cWvgbdnXdHmZAcYwsJQ6xL1XnMd8m6Hz4C7SAESJQLb36Qh0VSR3gIhiJOw&limit=100

            System.out.println("Getting post summary for " + startDateStr + endDateStr);
            String url = BASE_URL_FEED + accountId + "/feed?fields=shares,likes,reactions,comments{comment_count,created_time,like_count}&limit=100"
                    + "time_range[since]=" + startDateStr + "&time_range[until]=" + endDateStr
                    + "&access_token=" + ACCESS_TOKEN;
//            String url = BASE_URL_FEED + "110571071477" + "/feed?fields=shares,likes,reactions,comments{comment_count,created_time,like_count}&limit=100"
//                    + "time_range[since]=" + startDateStr + "&time_range[until]=" + endDateStr
//                    + "&access_token=" + ACCESS_TOKEN;
            if (aggregation.equalsIgnoreCase("weekly")) {
                url += "&time_increment=7";
            }
            if (aggregation.equalsIgnoreCase("daily")) {
                url += "&time_increment=1";
            }
            String fbData = Rest.getData(url);
            JSONParser parser = new JSONParser();
            if (fbData == null) {
                return null;
            }
            Object jsonObj = parser.parse(fbData);
            JSONObject array = (JSONObject) jsonObj;
            JSONArray dataArr = (JSONArray) array.get("data");
            List<Map<String, Object>> dataValueList = new ArrayList();
            for (int i = 0; i < dataArr.size(); i++) {
                JSONObject data = (JSONObject) dataArr.get(i);
                Map<String, Object> dataList = getDataValue(data);
                dataList.put("date", startDateStr);
                dataList.put("reactions", getActionsCount((JSONObject) data.get("reactions")) + "");
                dataList.put("likes", getActionsCount((JSONObject) data.get("likes")) + "");
                dataList.put("comments", getActionsCount((JSONObject) data.get("comments")) + "");
                dataList.put("shares", getShareCount((JSONObject) data.get("shares")) + "");
                dataList.put("engagements", (Long.parseLong(dataList.get("shares")+"") + Long.parseLong(dataList.get("likes")+"") + Long.parseLong(dataList.get("comments")+"")) + "");
                dataValueList.add(dataList);
            }
            return dataValueList;
            //return getActions(actions); //array.get("data");
        } catch (ParseException ex) {
            Logger.getLogger(FacebookService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static void main(String[] argv) {
        System.out.println(DateUtils.dateToString(DateUtils.toDate("2017-01-26T20:21:33+0000".replace("+0000", "").replace("T", " "), "yyyy-MM-dd HH:mm:ss"), "MM-dd-yyyy HH:mm"));
        /*
         Date startDate = DateUtils.get12WeeksBack("");
         Date endDate = new Date();
         Date currentStart = startDate;
         System.out.println(DateUtils.timeDiff(endDate, currentStart));

         while (DateUtils.timeDiff(endDate, currentStart) > 0) {
         System.out.println("Getting data for week " + currentStart);
         Date weekStart = DateUtils.getStartDateOfWeek(currentStart);
         //returnAll.addAll(getPostSummary(weekStart, DateUtils.getNextWeek(weekStart)));
         currentStart = DateUtils.getNextWeek(currentStart);
         }*/
    }

    public Object getLast12WeeksPerformance(Long accountId, Date startDate, Date endDate, String aggregation) {
        try {
            Date currentStart = startDate;
            List returnAll = new ArrayList();
            System.out.println("Start Date" + currentStart);
            System.out.println("End Date" + endDate);
            while (DateUtils.dateDiffInSec(endDate, currentStart) > 0) {
                System.out.println("Getting data for week " + currentStart);
                System.out.println(" Date Difference " + DateUtils.timeDiff(endDate, currentStart));
                System.out.println(" End Date " + endDate);
                Date weekStart = DateUtils.getStartDateOfWeek(currentStart);
                returnAll.addAll(getPostSummary(accountId, weekStart, DateUtils.getNextWeek(weekStart), aggregation));
                currentStart = DateUtils.getNextWeek(currentStart);
            }
            return returnAll;
        } catch (Exception e) {

        }
        return null;
    }
//
//    public void main(String[] argv) {
//        Date startDate = DateUtils.get12WeeksBack("");
//        Date endDate = new Date();
//        getLast12WeeksPerformance(startDate, endDate);
//    }
//    

    private Map<String, Object> getDataValue(JSONObject data) {
        List<Map<String, Object>> returnList = new ArrayList<>();
        Set<String> keySet = data.keySet();
        Map<String, Object> dataMap = new HashMap<>();
        for (Iterator<String> iterator = keySet.iterator(); iterator.hasNext();) {
            String key = iterator.next();
            if (!(data.get(key) instanceof JSONArray || data.get(key) instanceof JSONObject)) {
                Object value = data.get(key);
                dataMap.put(key, value);
            }
        }
        returnList.add(dataMap);
        return dataMap;
    }

    private Map<String, String> getActionsData(JSONArray actions, String prefix) {
        Map<String, String> actionMap = new HashMap<>();
        for (int i = 0; i < actions.size(); i++) {
            JSONObject action = (JSONObject) actions.get(i);
            actionMap.put(prefix + (String) action.get("action_type"), (String) action.get("value"));
            //returnList.add(actionMap);

        }
        return actionMap;
    }

    private Integer getActionsCount(JSONObject actions) {
        if (actions == null) {
            return 0;
        }
        JSONArray data = (JSONArray) actions.get("data");
        if (data == null) {
            return 0;
        }
        return data.size();
    }

    private Object getShareCount(JSONObject actions) {
        if (actions == null) {
            return 0L;
        }

        return actions.get("count");
    }

    public List<Map<String, Object>> getInstagramPerformance(Long accountId, Date startDate, Date endDate, String aggregation) {
        try {
            String url = BASE_URL + accountId + "/insights?"
                    //+ "fields=campaigns{insights{campaign_name,clicks,impressions,ctr,cpc,actions,cost_per_action_type,spend,account_name}}"
                    + "fields=impressions%2Cclicks%2Cctr%2Ccpc%2Cspend%2Cactions%2Caccount_name%2Ccost_per_action_type"
                    + "&access_token=" + ACCESS_TOKEN;
            if (aggregation.equalsIgnoreCase("week")) {
                url += "&time_increment=7";
            }
            if (aggregation.equalsIgnoreCase("day")) {
                url += "&time_increment=1";
            }
            if (aggregation.equalsIgnoreCase("month")) {
                url += "&time_increment=30";
            }
            if (aggregation.equalsIgnoreCase("year")) {
                url += "&time_increment=365";
            }
            String fbData = Rest.getData(url);
            JSONParser parser = new JSONParser();
            if (fbData == null) {
                return null;
            }
            Object jsonObj = parser.parse(fbData);
            JSONObject array = (JSONObject) jsonObj;
            JSONArray dataArr = (JSONArray) array.get("data");

            List<Map<String, Object>> dataValueList = new ArrayList();
            for (int i = 0; i < dataArr.size(); i++) {
                JSONObject data = (JSONObject) dataArr.get(i);
                Map<String, Object> dataList = getDataValue(data);

                dataValueList.add(dataList);
            }
            return dataValueList;
        } catch (ParseException ex) {
            Logger.getLogger(FacebookService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public Object getLast12WeeksPerformanceData(Long accountId, Date startDate, Date endDate) {
        try {
            String startDateStr = DateUtils.dateToString(startDate, "YYYY-MM-dd");
            String endDateStr = DateUtils.dateToString(endDate, "YYYY-MM-dd");
            String url = BASE_URL + accountId + "/insights?"
                    + "fields=clicks,impressions,ctr,spend,actions,cost_per_action_type&time_increment=7&time_range[since]=" + startDateStr
                    + "&time_range[until]=" + endDateStr
                    + "&access_token=" + ACCESS_TOKEN;
            //https://graph.facebook.com/v2.8/act_10153963646170050/insights?fields=clicks,impressions,ctr,spend,actions,cost_per_action_type&time_range[since]=2016-10-01&time_range[until]=2016-10-31&time_increment=7&access_token=EAANFRJpxZBZC0BAAqAeGjVgawF8X58ZCYRU824xzKpDcCN49s3wMGqie9MRdUZBnSK8pTsFw3KSOvfof88Oib6CCIOZBlnYQkkeYJrYdyOTJoELEZAmFAFKMoBg5cWvgbdnXdHmZAcYwsJQ6xL1XnMd8m6Hz4C7SAESJQLb36Qh0VSR3gIhiJOw

            String fbData = Rest.getData(url);
            JSONParser parser = new JSONParser();
            if (fbData == null) {
                return null;
            }
            Object jsonObj = parser.parse(fbData);
            JSONObject array = (JSONObject) jsonObj;

            JSONArray dataArr = (JSONArray) array.get("data");
            List dataValueList = new ArrayList();
            for (int i = 0; i < dataArr.size(); i++) {
                JSONObject data = (JSONObject) dataArr.get(i);
                JSONArray actionsArr = (JSONArray) data.get("actions");
                //JSONObject actions = (JSONObject) actionsArr.get(0);
                List<Map<String, Object>> returnList = new ArrayList<>();
                JSONArray costPerActionTypeArr = (JSONArray) data.get("cost_per_action_type");
                Map<String, Object> dataList = getDataValue(data);
                if (actionsArr != null) {
                    dataList.putAll(getActionsData(actionsArr, "actions_"));
                }
                if (costPerActionTypeArr != null) {
                    dataList.putAll(getActionsData(costPerActionTypeArr, "cost_"));
                }
                dataList.put("date_start", DateUtils.dateToString(DateUtils.toDate(dataList.get("date_start")+"", "yyyy-MM-dd"), "MM/dd/yyyy"));
                dataList.put("date_stop", DateUtils.dateToString(DateUtils.toDate(dataList.get("date_stop")+"", "yyyy-MM-dd"), "MM/dd/yyyy"));
                dataList.put("ctr", ApiUtils.removePercent(dataList.get("ctr")+""));
                dataValueList.add(dataList);
            }
            return dataValueList;
        } catch (ParseException ex) {
            Logger.getLogger(FacebookService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public Object getLast12WeeksData(Long accountId, Date startDate, Date endDate) {
        try {
            String url = BASE_URL + accountId + "/insights?"
                    //+ "fields=campaigns{insights{campaign_name,clicks,impressions,ctr,cpc,actions,cost_per_action_type,spend,account_name}}"
                    + "fields=impressions%2Cclicks%2Cctr%2Ccpc%2Cspend%2Cactions%2Caccount_name%2Ccost_per_action_type"
                    + "&access_token=" + ACCESS_TOKEN;
            String fbData = Rest.getData(url);
            JSONParser parser = new JSONParser();
            if (fbData == null) {
                return null;
            }
            Object jsonObj = parser.parse(fbData);
            JSONObject array = (JSONObject) jsonObj;
            return array.get("data");
        } catch (ParseException ex) {
            Logger.getLogger(FacebookService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public Object getDayOfWeekData(Long accountId, Date startDate, Date endDate) {
        try {
            String url = BASE_URL + accountId + "/insights?"
                    //+ "fields=campaigns{insights{campaign_name,clicks,impressions,ctr,cpc,actions,cost_per_action_type,spend,account_name}}"
                    + "fields=impressions%2Cclicks%2Cctr%2Ccpc%2Cspend%2Cactions%2Caccount_name%2Ccost_per_action_type"
                    + "&access_token=" + ACCESS_TOKEN;
            String fbData = Rest.getData(url);
            JSONParser parser = new JSONParser();
            if (fbData == null) {
                return null;
            }
            Object jsonObj = parser.parse(fbData);
            JSONObject array = (JSONObject) jsonObj;
            return array.get("data");
        } catch (ParseException ex) {
            Logger.getLogger(FacebookService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

}
