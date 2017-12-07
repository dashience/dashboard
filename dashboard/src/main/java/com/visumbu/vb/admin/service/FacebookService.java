/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.admin.service;

import com.visumbu.vb.admin.dao.SettingsDao;
import com.visumbu.vb.model.Settings;
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
import org.springframework.beans.factory.annotation.Autowired;

import com.visumbu.vb.utils.SettingsProperty;

/**
 *
 * @author jp
 */
@Service("facebookService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class FacebookService {

    @Autowired
    private SettingsDao settingsDao;

//    public String ACCESS_TOKEN = "EAAUAycrj0GsBAM3EgwLcQjz5zywESZBpHN76cERZCaxEZC9ZAzMjRzRxIznWM3u8s4DBwUvhMaQAGglDOIa9tSV7ZCVf9ZBajV9aA6khaCRmEZAQhIHUInBVYZBZAT5nycwniZCozuLcjhTm0eW5tAUxIugmvxszsivmh5ZClzuMZApZBJxd0RZBIDk1r0";
//    public String ACCESS_TOKEN = "EAAUAycrj0GsBAM3EgwLcQjz5zywESZBpHN76cERZCaxEZC9ZAzMjRzRxIznWM3u8s4DBwUvhMaQAGglDOIa9tSV7ZCVf9ZBajV9aA6khaCRmEZAQhIHUInBVYZBZAT5nycwniZCozuLcjhTm0eW5tAUxIugmvxszsivmh5ZClzuMZApZBJxd0RZBIDk1r0";
    public final String APP_SECRET = "b6659b47ba7b2b11179247bb3cd84f70";
    // public final Long ACCOUNT_ID = ExampleConfig.ACCOUNT_ID;
//    public final String APP_SECRET = ExampleConfig.APP_SECRET;
    public final String BASE_URL = "https://graph.facebook.com/v2.11/act_";
    public final String BASE_URL_FEED = "https://graph.facebook.com/v2.11/";
    //public final APIContext context = new APIContext(ACCESS_TOKEN).enableDebug(true);

    public String ACCESS_TOKEN = "";

    public List<Map<String, Object>> get(String dataSet, Long accountId, Long organicAccountId, Date startDate, Date endDate, String aggregation, String productSegement) {
//        this.ACCESS_TOKEN = accessToken;

        //code to get access token from settings
        List<Settings> facebookAccessToken = settingsDao.getProperty("facebookAccessToken");
        String fbAccessToken = SettingsProperty.getSettingsProperty(facebookAccessToken, "facebookAccessToken");
        this.ACCESS_TOKEN = fbAccessToken;

        if (aggregation == null) {
            aggregation = "";
        }
        if (dataSet.equalsIgnoreCase("accountPerformance")) {
            return getAccountPerformance(accountId, startDate, endDate, aggregation, productSegement);
        }
        if (dataSet.equalsIgnoreCase("campaignPerformance")) {
            return getCampaignPerformance(accountId, startDate, endDate, aggregation, productSegement);
        }
        if (dataSet.equalsIgnoreCase("adPerformance")) {
            return getAdPerformance(accountId, startDate, endDate, aggregation, productSegement);
        }
        if (dataSet.equalsIgnoreCase("adSetPerformance")) {
            return getAdSetPerformance(accountId, startDate, endDate, aggregation, productSegement);
        }
        if (dataSet.equalsIgnoreCase("agePerformance")) {
            return getAgePerformance(accountId, startDate, endDate, aggregation, productSegement);
        }
        if (dataSet.equalsIgnoreCase("genderPerformance")) {
            return getGenderPerformance(accountId, startDate, endDate, aggregation, productSegement);
        }

        if (dataSet.equalsIgnoreCase("instagramPerformance")) {
            return getInstagramPerformance(accountId, startDate, endDate, aggregation);
        }

        if (dataSet.equalsIgnoreCase("postPerformance")) {
            return getPostPerformance(organicAccountId, startDate, endDate, aggregation, productSegement);
        }
        if (dataSet.equalsIgnoreCase("engagements")) {
            return getTotalEngagements(organicAccountId, startDate, endDate, aggregation, productSegement);
        }
        if (dataSet.equalsIgnoreCase("reach")) {
            return getTotalReach(organicAccountId, startDate, endDate, aggregation, productSegement);
        }
        if (dataSet.equalsIgnoreCase("pageLikes")) {
            return getPageLikes(organicAccountId, startDate, endDate, aggregation, productSegement);
        }
        if (dataSet.equalsIgnoreCase("pageViews")) {
            return getPageViews(organicAccountId, startDate, endDate, aggregation, productSegement);
        }

        if (dataSet.equalsIgnoreCase("pageReactions")) {
            return getPageReactions(organicAccountId, startDate, endDate, aggregation, productSegement);
        }
        return null;
    }

    public String getFbPublishedPosts(Long accountId) {
        String url = BASE_URL + accountId + "/insights?fields=adset_name%2Cclicks%2Cimpressions&date_preset=today&access_token=" + ACCESS_TOKEN;
        return Rest.getData(url);
    }

    public List<Map<String, Object>> getAdSetPerformance(Long accountId, Date startDate, Date endDate, String aggregation, String productSegement) {
        try {
            String startDateStr = DateUtils.dateToString(startDate, "YYYY-MM-dd");
            String endDateStr = DateUtils.dateToString(endDate, "YYYY-MM-dd");

            String url = BASE_URL + accountId + "/insights?fields=account_name%2Cimpressions%2Cclicks%2Cctr%2Ccpc%2Cspend%2Cactions%2Creach%2Ccost_per_action_type%2Cadset_name%2Ccampaign_name&level=adset&"
                    + "time_range[since]=" + startDateStr + "&time_range[until]=" + endDateStr
                    + "&access_token=" + ACCESS_TOKEN;
            System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
            System.out.println(url);
            System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");

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
                List<Map<String, String>> returnList = new ArrayList<>();
                JSONArray costPerActionTypeArr = (JSONArray) data.get("cost_per_action_type");
                Map<String, Object> dataList = getDataValue(data);
                if (actionsArr != null) {
                    dataList.putAll(getActionsData(actionsArr, "actions_"));
                }
                if (costPerActionTypeArr != null) {
                    dataList.putAll(getActionsData(costPerActionTypeArr, "cost_"));
                }
                dataList.put("ctr", ApiUtils.removePercent(dataList.get("ctr") + ""));
                dataValueList.add(dataList);
            }
            return dataValueList;  //getActions(actionsArr);
            //return getActions(actions); //array.get("data");
        } catch (ParseException ex) {
            Logger.getLogger(FacebookService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public List<Map<String, Object>> getPageReactions(Long accountId, Date startDate, Date endDate, String aggregation, String productSegment) {
        try {
            //ACCESS_TOKEN

            String startDateStr = DateUtils.dateToString(startDate, "YYYY-MM-dd");
            String endDateStr = DateUtils.dateToString(endDate, "YYYY-MM-dd");

            if (aggregation.equalsIgnoreCase("day") || (aggregation.equalsIgnoreCase("none") && productSegment.equalsIgnoreCase("none"))) {
                String fbUrl = "https://graph.facebook.com/v2.9/" + accountId + "/posts?fields=message,updated_time,"
                        + "reactions.type(LIKE).summary(total_count).limit(0).as(like),"
                        + "reactions.type(LOVE).summary(total_count).limit(0).as(love),"
                        + "reactions.type(WOW).summary(total_count).limit(0).as(wow),"
                        + "reactions.type(HAHA).summary(total_count).limit(0).as(haha),"
                        + "reactions.type(SAD).summary(total_count).limit(0).as(sad),"
                        + "reactions.type(ANGRY).summary(total_count).limit(0).as(angry)&"
                        + "access_token=" + ACCESS_TOKEN
                        + "&until=" + endDateStr;

                System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
                System.out.println(fbUrl);
                System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");

                String data = Rest.getData(fbUrl);
                JSONParser parser = new JSONParser();
                if (data == null) {
                    return null;
                }
                Object object = parser.parse(data);
                JSONObject jsonObject = (JSONObject) object;
                Map<String, Object> jsonToMap = JsonSimpleUtils.jsonToMap(jsonObject);
                List<Map> arrayData = (List<Map>) jsonToMap.get("data");
                List<Map<String, Object>> listData = new ArrayList<>();
                for (Iterator<Map> iterator = arrayData.iterator(); iterator.hasNext();) {
                    Map reactionsData = iterator.next();
                    Map<String, Object> reactionObject = new HashMap<>();
                    reactionObject.put("message", reactionsData.get("message") + "");
                    reactionObject.put("updated_time", reactionsData.get("updated_time") + "");
                    reactionObject.put("like", ((Map) ((Map) reactionsData.get("like")).get("summary")).get("total_count") + "");
                    reactionObject.put("wow", ((Map) ((Map) reactionsData.get("wow")).get("summary")).get("total_count") + "");
                    reactionObject.put("haha", ((Map) ((Map) reactionsData.get("haha")).get("summary")).get("total_count") + "");
                    reactionObject.put("sad", ((Map) ((Map) reactionsData.get("sad")).get("summary")).get("total_count") + "");
                    reactionObject.put("angry", ((Map) ((Map) reactionsData.get("angry")).get("summary")).get("total_count") + "");
                    listData.add(reactionObject);
                }
                System.out.println(listData);
                return listData;
            }

        } catch (ParseException ex) {
            Logger.getLogger(FacebookService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public List<Map<String, Object>> getPageViews(Long accountId, Date startDate, Date endDate, String aggregation, String productSegment) {
        try {

            String startDateStr = DateUtils.dateToString(startDate, "YYYY-MM-dd");
            String endDateStr = DateUtils.dateToString(endDate, "YYYY-MM-dd");

            System.out.println("-------------- Account ID -->" + accountId);
            if (aggregation.equalsIgnoreCase("day")) {

                String fbUrl = "https://graph.facebook.com/v2.9/" + accountId + "/insights/page_views_total?"
                        + "access_token=" + ACCESS_TOKEN + "&since=" + startDateStr + "&until=" + endDateStr + "&period=day&limit=50";
                System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
                System.out.println(fbUrl);
                System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");

                String data = Rest.getData(fbUrl);
                JSONParser parser = new JSONParser();
                if (data == null) {
                    return null;
                }
                Object jsonObj = parser.parse(data);
                JSONObject json = (JSONObject) jsonObj;
                Map<String, Object> jsonToMap = JsonSimpleUtils.jsonToMap(json);
                List<Map> arrayData = (List<Map>) jsonToMap.get("data");
                Map<String, Object> object = arrayData.get(0);
                List<Map> listArrayObject = (List<Map>) object.get("values");
                System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
                List<Map<String, Object>> listData = new ArrayList<>();

                for (Iterator<Map> iterator = listArrayObject.iterator(); iterator.hasNext();) {
                    Map next = iterator.next();
                    Map<String, Object> dataMap = new HashMap<>();
                    System.out.println("value=" + next.get("value"));
                    System.out.println("end_time=" + next.get("end_time"));
                    dataMap.put("value", next.get("value") + "");
                    dataMap.put("end_time", next.get("end_time") + "");
                    listData.add(dataMap);

                }
                System.out.println(listData);
                return listData;
            }

            if (productSegment.equalsIgnoreCase("device")) {
                //            String fbUrl = "https://graph.facebook.com/185042698207211/insights?pretty=0&until=1476489600&metric=page_views_by_site_logged_in_unique&period=days_28&access_token=EAANFRJpxZBZC0BAAqAeGjVgawF8X58ZCYRU824xzKpDcCN49s3wMGqie9MRdUZBnSK8pTsFw3KSOvfof88Oib6CCIOZBlnYQkkeYJrYdyOTJoELEZAmFAFKMoBg5cWvgbdnXdHmZAcYwsJQ6xL1XnMd8m6Hz4C7SAESJQLb36Qh0VSR3gIhiJOw";
                String fbUrl = "https://graph.facebook.com/" + accountId + "/insights?pretty=0&until=" + endDateStr + "&"
                        + "metric=page_views_by_site_logged_in_unique&period=days_28&access_token=" + ACCESS_TOKEN;

                System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
                System.out.println(fbUrl);
                System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");

                String data = Rest.getData(fbUrl);
                JSONParser parser = new JSONParser();
                if (data == null) {
                    return null;
                }
                Object jsonObj = parser.parse(data);
                JSONObject json = (JSONObject) jsonObj;
                Map<String, Object> jsonToMap = JsonSimpleUtils.jsonToMap(json);
                List<Map> arrayData = (List<Map>) jsonToMap.get("data");
                List<Map> deviceData = (List<Map>) arrayData.get(0).get("values");
                List<Map<String, Object>> valueData = new ArrayList();

                System.out.println("*********************************");
                int objectLength = deviceData.size() - 1;
                List<Map<String, Object>> viewByDeviceListData = new ArrayList<>();
                try {
                    Map<String, Object> objectArray = (Map<String, Object>) deviceData.get(objectLength).get("value");

                    objectArray.remove("API");
                    viewByDeviceListData.add(objectArray);
                    return viewByDeviceListData;
                } catch (NullPointerException ex) {
                    return viewByDeviceListData;
                }
            }

            if (productSegment.equalsIgnoreCase("gender")) {
                String fbUrl = "https://graph.facebook.com/" + accountId + "/insights?pretty=0&"
                        + "until=" + endDateStr + "&metric=page_views_by_age_gender_logged_in_unique&period=days_28&"
                        + "access_token=" + ACCESS_TOKEN;

                System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
                System.out.println(fbUrl);
                System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");

                String data = Rest.getData(fbUrl);
                JSONParser parser = new JSONParser();
                if (data == null) {
                    return null;
                }
                Object jsonObj = parser.parse(data);
                JSONObject json = (JSONObject) jsonObj;
                Map<String, Object> jsonToMap = JsonSimpleUtils.jsonToMap(json);
                List<Map> arrayData = (List<Map>) jsonToMap.get("data");
                Map<String, Object> object = arrayData.get(0);
                List<Map<String, Object>> arrayList = (List<Map<String, Object>>) object.get("values");
                System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
                System.out.println(arrayList);
                int objectLength = arrayList.size() - 1;
                System.out.println("Object Length=" + objectLength);
                Map<String, Object> arrayObject = (Map<String, Object>) arrayList.get(objectLength).get("value");
                List<Map<String, Object>> listData = new ArrayList<>();
                for (Map.Entry<String, Object> entry : arrayObject.entrySet()) {
                    String key = entry.getKey();
                    Object value = entry.getValue();
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("data", value);
                    Map dataMap = new HashMap();
                    dataMap.put("period", key);
                    dataMap.put("Male", ((Map) jsonObject.get("data")).get("M"));
                    dataMap.put("FeMale", ((Map) jsonObject.get("data")).get("F"));
                    dataMap.put("UnKnown", ((Map) jsonObject.get("data")).get("U"));
                    listData.add(dataMap);
                }

                return listData;
            }

            //get total page views
            if (aggregation.equalsIgnoreCase("none") && productSegment.equalsIgnoreCase("none")) {
                String fbUrl = "https://graph.facebook.com/v2.9/" + accountId + "/insights/page_views_total?access_token="
                        + ACCESS_TOKEN + "&until=" + endDateStr;

                System.out.println("************************************");
                System.out.println(fbUrl);
                System.out.println("************************************");

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
                int objectLength = fbLikesList.size() - 1;
                try {
                    String values = ((Map) fbLikesList.get(objectLength)).get("value") + "";
                    Map<String, Object> returnMapData = new HashMap<>();
                    returnMapData.put("page_views", values);
                    listData.add(returnMapData);
                    return listData;
                } catch (NullPointerException ex) {
                    return listData;
                }
            }

        } catch (ParseException ex) {
            Logger.getLogger(FacebookService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public List<Map<String, Object>> getPageLikes(Long accountId, Date startDate, Date endDate, String aggregation, String productSegement) {
        try {
//            String fbUrl = "https://graph.facebook.com/" + accountId + "/insights/page_fans_city?access_token=" + ACCESS_TOKEN;
            String startDateStr = DateUtils.dateToString(startDate, "YYYY-MM-dd");
            String endDateStr = DateUtils.dateToString(endDate, "YYYY-MM-dd");
            System.out.println("Product Segement---->" + productSegement);

            if (productSegement.equalsIgnoreCase("city")) {
                String fbUrl = "https://graph.facebook.com/v2.9/" + accountId + "/insights/page_fans_city?access_token="
                        + ACCESS_TOKEN + "&until=" + endDateStr;
                System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
                System.out.println(fbUrl);
                System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
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
                int objectLength = fbLikesList.size() - 1;
                System.out.print("**************** City by likes --------------------");
                try {
                    Map<String, Object> values = (Map<String, Object>) ((Map) fbLikesList.get(objectLength)).get("value");
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
                } catch (NullPointerException ex) {
                    return listData;
                }
            }
            //get total organic likes
            if (aggregation.equalsIgnoreCase("none") && productSegement.equalsIgnoreCase("none")) {
                String fbUrl = "https://graph.facebook.com/v2.9/" + accountId + "/insights/page_fans?access_token="
                        + ACCESS_TOKEN + "&until=" + endDateStr;

                System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
                System.out.println(fbUrl);
                System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");

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
                int objectLength = fbLikesList.size() - 1;
                try {
                    String values = ((Map) fbLikesList.get(objectLength)).get("value") + "";
                    Map<String, Object> returnMapData = new HashMap<>();
                    returnMapData.put("page_fans", values);
                    listData.add(returnMapData);
                    return listData;
                } catch (NullPointerException ex) {
                    return listData;
                }
            }

        } catch (ParseException ex) {
            Logger.getLogger(FacebookService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public List<Map<String, Object>> getTotalReach(Long accountId, Date startDate, Date endDate, String aggregation, String productSegement) {
        try {
            String startDateStr = DateUtils.dateToString(startDate, "YYYY-MM-dd");
            String endDateStr = DateUtils.dateToString(endDate, "YYYY-MM-dd");

            String fbUrl = "https://graph.facebook.com/v2.9/" + accountId + "/insights/page_impressions_unique?"
                    + "access_token=" + ACCESS_TOKEN + "&until=" + endDateStr;

            System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
            System.out.println(fbUrl);
            System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");

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
//            System.out.println("=====================");
            System.out.println(fbLikesList.get(0));
            int ObjectLength = fbLikesList.size() - 1;
            try {
                String values = ((Map) fbLikesList.get(ObjectLength)).get("value") + "";
                Map<String, Object> returnMapData = new HashMap<>();
                returnMapData.put("reach", values);
                listData.add(returnMapData);
                System.out.println("=====================");
                System.out.println(listData);
                return listData;
            } catch (NullPointerException ex) {
                return listData;
            }
        } catch (ParseException ex) {
            Logger.getLogger(FacebookService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public List<Map<String, Object>> getTotalEngagements(Long accountId, Date startDate, Date endDate, String aggregation, String productSegement) {
        try {

            String startDateStr = DateUtils.dateToString(startDate, "YYYY-MM-dd");
            String endDateStr = DateUtils.dateToString(endDate, "YYYY-MM-dd");

            String fbUrl = "https://graph.facebook.com/v2.9/" + accountId + "/insights/page_engaged_users?access_token="
                    + ACCESS_TOKEN + "&until=" + endDateStr;
            System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
            System.out.println(fbUrl);
            System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");

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
            int objectLength = fbLikesList.size() - 1;
            try {
                String values = ((Map) fbLikesList.get(objectLength)).get("value") + "";
                Map<String, Object> returnMapData = new HashMap<>();
                returnMapData.put("engagements", values);
                listData.add(returnMapData);
                return listData;
            } catch (NullPointerException ex) {
                return listData;
            }
        } catch (ParseException ex) {
            Logger.getLogger(FacebookService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public List<Map<String, Object>> getPostPerformance(Long accountId, Date startDate, Date endDate, String aggregation, String productSegment) {
        try {
            String startDateStr = DateUtils.dateToString(startDate, "YYYY-MM-dd");
            String endDateStr = DateUtils.dateToString(endDate, "YYYY-MM-dd");
            String fbUrl = "https://graph.facebook.com/v2.9/" + accountId + "/posts?fields=message,"
                    + "reactions.type(LIKE).summary(total_count).limit(0).as(like),comments&access_token="
                    + ACCESS_TOKEN + "&until=" + endDateStr;

            System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
            System.out.println(fbUrl);
            System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");

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
            for (Iterator<Map> iterator = fbData.iterator(); iterator.hasNext();) {
                Map fbDataMap = iterator.next();
                Map<String, Object> fbDataObj = new HashMap<>();
                fbDataObj.put("message", fbDataMap.get("message") + "");
                if (fbDataMap.containsKey("like")) {
                    List<Map> data2 = new ArrayList<>();
                    data2.add((Map) fbDataMap.get("like"));
                    for (Iterator<Map> iterator1 = data2.iterator(); iterator1.hasNext();) {
                        Map next = iterator1.next();
                        List<Map> data3 = new ArrayList<>();
                        data3.add((Map) next.get("summary"));
                        for (Iterator<Map> iterator2 = data3.iterator(); iterator2.hasNext();) {
                            Map next1 = iterator2.next();

                            fbDataObj.put("likes", next1.get("total_count") + "");
                        }
                    }
//                    fbDataObj.put("likes", ((List)((Map) fbDataMap.get("like")).get("summary")) + "");
                } else {
                    fbDataObj.put("likes", "0");
                }
                listData.add(fbDataObj);

            }
            return listData;
        } catch (ParseException ex) {
            Logger.getLogger(FacebookService.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    public List<Map<String, Object>> getAccountPerformance(Long accountId, Date startDate, Date endDate, String aggregation, String productSegment) {
        try {
            String startDateStr = DateUtils.dateToString(startDate, "YYYY-MM-dd");
            String endDateStr = DateUtils.dateToString(endDate, "YYYY-MM-dd");

            if (productSegment.equalsIgnoreCase("device")) {
                List<Map<String, Object>> accountDevicePerformance = getDevicePerformance(accountId, startDate, endDate, aggregation);
                return accountDevicePerformance;
            } else {

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
                    url += "&time_increment=monthly";
                }
//            if (aggregation.equalsIgnoreCase("year")) {
//                url += "&time_increment=365";
//            }

                System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
                System.out.println(url);
                System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");

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
                    List<Map<String, String>> returnList = new ArrayList<>();
                    JSONArray costPerActionTypeArr = (JSONArray) data.get("cost_per_action_type");
                    Map<String, Object> dataList = getDataValue(data);
                    if (actionsArr != null) {
                        dataList.putAll(getActionsData(actionsArr, "actions_"));
                    }
                    if (costPerActionTypeArr != null) {
                        dataList.putAll(getActionsData(costPerActionTypeArr, "cost_"));
                    }
                    dataList.put("ctr", ApiUtils.removePercent(dataList.get("ctr") + ""));
                    dataValueList.add(dataList);
                }
                return dataValueList;  //getActions(actionsArr);
            }
            //return getActions(actions); //array.get("data");
        } catch (ParseException ex) {
            Logger.getLogger(FacebookService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IndexOutOfBoundsException ex) {
            return null;
        }
        return null;
    }

    public List<Map<String, Object>> getCampaignPerformance(Long accountId, Date startDate, Date endDate, String aggregation, String productSegement) {
        try {
            String startDateStr = DateUtils.dateToString(startDate, "YYYY-MM-dd");
            String endDateStr = DateUtils.dateToString(endDate, "YYYY-MM-dd");

            String url = BASE_URL + accountId + "/insights?level=campaign&fields=account_name,impressions,clicks,ctr,cpc,spend,actions,reach,cost_per_action_type,campaign_name&"
                    + "time_range[since]=" + startDateStr + "&time_range[until]=" + endDateStr
                    + "&access_token=" + ACCESS_TOKEN + "&limit=500";
            if (productSegement.equalsIgnoreCase("device")) {
                url += "&breakdowns=impression_device";
            }
            if (aggregation.equalsIgnoreCase("week")) {
                url += "&time_increment=7";
            }
            if (aggregation.equalsIgnoreCase("day")) {
                url += "&time_increment=1";
            }

            System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
            System.out.println(url);
            System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");

            String fbData = Rest.getData(url);
            JSONParser parser = new JSONParser();
            if (fbData == null) {
                return null;
            }
            Object jsonObj = parser.parse(fbData);
            JSONObject array = (JSONObject) jsonObj;
            JSONArray dataArr = (JSONArray) array.get("data");
            List<Map<String, Object>> dataValueList = new ArrayList();
            Map<String, Object> dataMap = new HashMap();
            for (int i = 0; i < dataArr.size(); i++) {
                JSONObject data = (JSONObject) dataArr.get(i);
                JSONArray actionsArr = (JSONArray) data.get("actions");
                //JSONObject actions = (JSONObject) actionsArr.get(0);
//                List<Map<String, String>> returnList = new ArrayList<>();
                JSONArray costPerActionTypeArr = (JSONArray) data.get("cost_per_action_type");
                dataMap = getDataValue(data);
                if (actionsArr != null) {
                    dataMap.putAll(getActionsData(actionsArr, "actions_"));
                }
                if (costPerActionTypeArr != null) {
                    dataMap.putAll(getActionsData(costPerActionTypeArr, "cost_"));
                }
                dataMap.put("ctr", ApiUtils.removePercent(dataMap.get("ctr") + ""));

                dataValueList.add(dataMap);
            }
            return dataValueList;  //getActions(actionsArr);
            //return getActions(actions); //array.get("data");
        } catch (ParseException ex) {
            Logger.getLogger(FacebookService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IndexOutOfBoundsException ex) {
            return null;
        }
        return null;
    }

    public List<Map<String, Object>> getAdPerformance(Long accountId, Date startDate, Date endDate, String aggregation, String productSegement) {
        try {
            String startDateStr = DateUtils.dateToString(startDate, "YYYY-MM-dd");
            String endDateStr = DateUtils.dateToString(endDate, "YYYY-MM-dd");

            String url = BASE_URL + accountId + "/insights?fields=account_name%2Cimpressions%2Cclicks%2Cctr%2Ccpc%2Cspend%2Cactions%2Creach%2Ccost_per_action_type%2Cadset_name%2Cad_name%2Ccampaign_name&level=ad&"
                    + "time_range[since]=" + startDateStr + "&time_range[until]=" + endDateStr
                    + "&access_token=" + ACCESS_TOKEN + "&limit=100";

            System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
            System.out.println(url);
            System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");

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
                List<Map<String, String>> returnList = new ArrayList<>();
                JSONArray costPerActionTypeArr = (JSONArray) data.get("cost_per_action_type");
                Map<String, Object> dataList = getDataValue(data);
                if (actionsArr != null) {
                    dataList.putAll(getActionsData(actionsArr, "actions_"));
                }
                if (costPerActionTypeArr != null) {
                    dataList.putAll(getActionsData(costPerActionTypeArr, "cost_"));
                }
                dataList.put("ctr", ApiUtils.removePercent(dataList.get("ctr") + ""));
                dataValueList.add(dataList);
            }
            return dataValueList;  //getActions(actionsArr);
            //return getActions(actions); //array.get("data");
        } catch (ParseException ex) {
            Logger.getLogger(FacebookService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IndexOutOfBoundsException ex) {
            return null;
        }
        return null;
    }

    public List<Map<String, Object>> getDevicePerformance(Long accountId, Date startDate, Date endDate, String aggregation) {
        try {
            String startDateStr = DateUtils.dateToString(startDate, "YYYY-MM-dd");
            String endDateStr = DateUtils.dateToString(endDate, "YYYY-MM-dd");

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
            Map<String, Object> dataList = new HashMap();
            for (int i = 0; i < dataArr.size(); i++) {
                JSONObject data = (JSONObject) dataArr.get(i);
                JSONArray actionsArr = (JSONArray) data.get("actions");
                //JSONObject actions = (JSONObject) actionsArr.get(0);
                List<Map<String, String>> returnList = new ArrayList<>();
                JSONArray costPerActionTypeArr = (JSONArray) data.get("cost_per_action_type");
                dataList = getDataValue(data);
                if (actionsArr != null) {
                    dataList.putAll(getActionsData(actionsArr, "actions_"));
                }
                if (costPerActionTypeArr != null) {
                    dataList.putAll(getActionsData(costPerActionTypeArr, "cost_"));
                }
                dataList.put("ctr", ApiUtils.removePercent(dataList.get("ctr") + ""));
                dataValueList.add(dataList);
            }
            return dataValueList;
            //return getActions(actions); //array.get("data");
        } catch (ParseException ex) {
            Logger.getLogger(FacebookService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IndexOutOfBoundsException ex) {
            return null;
        }
        return null;
    }

    public List<Map<String, Object>> getAgePerformance(Long accountId, Date startDate, Date endDate, String aggregation, String productSegement) {
        try {
            String startDateStr = DateUtils.dateToString(startDate, "YYYY-MM-dd");
            String endDateStr = DateUtils.dateToString(endDate, "YYYY-MM-dd");

            if (productSegement.equalsIgnoreCase("age") || productSegement.equalsIgnoreCase("none")) {
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
                    List<Map<String, String>> returnList = new ArrayList<>();
                    JSONArray costPerActionTypeArr = (JSONArray) data.get("cost_per_action_type");
                    Map<String, Object> dataList = getDataValue(data);
                    if (actionsArr != null) {
                        dataList.putAll(getActionsData(actionsArr, "actions_"));
                    }
                    if (costPerActionTypeArr != null) {
                        dataList.putAll(getActionsData(costPerActionTypeArr, "cost_"));
                    }
                    dataList.put("ctr", ApiUtils.removePercent(dataList.get("ctr") + ""));
                    dataValueList.add(dataList);
                }
                return dataValueList;
            }
            //return getActions(actions); //array.get("data");
        } catch (ParseException ex) {
            Logger.getLogger(FacebookService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IndexOutOfBoundsException ex) {
            return null;
        }
        return null;
    }

    public List<Map<String, Object>> getGenderPerformance(Long accountId, Date startDate, Date endDate, String aggregation, String productSegement) {
        try {
            String startDateStr = DateUtils.dateToString(startDate, "YYYY-MM-dd");
            String endDateStr = DateUtils.dateToString(endDate, "YYYY-MM-dd");

            if (productSegement.equalsIgnoreCase("gender") || productSegement.equalsIgnoreCase("none")) {
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
                    List<Map<String, String>> returnList = new ArrayList<>();
                    JSONArray costPerActionTypeArr = (JSONArray) data.get("cost_per_action_type");
                    Map<String, Object> dataList = getDataValue(data);
                    if (actionsArr != null) {
                        dataList.putAll(getActionsData(actionsArr, "actions_"));
                    }
                    if (costPerActionTypeArr != null) {
                        dataList.putAll(getActionsData(costPerActionTypeArr, "cost_"));
                    }
                    dataList.put("ctr", ApiUtils.removePercent(dataList.get("ctr") + ""));
                    dataValueList.add(dataList);
                }
                return dataValueList;
            }
            //return getActions(actions); //array.get("data");
        } catch (ParseException ex) {
            Logger.getLogger(FacebookService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IndexOutOfBoundsException ex) {
            return null;
        }
        return null;
    }

    public List<Map<String, Object>> getPostPerformance(Long accountId, Date startDate, Date endDate, String aggregation) {
        try {
            String startDateStr = DateUtils.dateToString(startDate, "YYYY-MM-dd");
            String endDateStr = DateUtils.dateToString(endDate, "YYYY-MM-dd");
            String url = "https://graph.facebook.com/v2.8/" + accountId + "/feed?fields=shares,likes,message,reactions,"
                    + "comments{message,comment_count,created_time,like_count},created_time,type&"
                    + "use_actual_created_time_for_backdated_post=true&limit=100&"
                    + "time_range[since]=" + startDateStr + "&time_range[until]=" + endDateStr
                    + "&access_token=" + ACCESS_TOKEN;

            System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
            System.out.println(url);
            System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");

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
                dataList.put("created_time", DateUtils.dateToString(DateUtils.toDate(dataList.get("created_time") + "".replace("+0000", "").replace("T", " "), "yyyy-MM-dd HH:mm:ss"), "MM/dd/yyyy HH:mm"));
                dataList.put("date", startDateStr);
                dataList.put("reactions", getActionsCount((JSONObject) data.get("reactions")) + "");
                dataList.put("likes", getActionsCount((JSONObject) data.get("likes")) + "");
                dataList.put("comments", getActionsCount((JSONObject) data.get("comments")) + "");
                dataList.put("shares", getShareCount((JSONObject) data.get("shares")) + "");
                dataList.put("engagements", (Long.parseLong(dataList.get("shares") + "") + Long.parseLong(dataList.get("likes") + "") + Long.parseLong(dataList.get("comments") + "")) + "");
                dataValueList.add(dataList);
            }
            return dataValueList;
            //return getActions(actions); //array.get("data");
        } catch (ParseException ex) {
            Logger.getLogger(FacebookService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IndexOutOfBoundsException ex) {
            return null;
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
                returnAll.addAll(getPostPerformance(accountId, weekStart, DateUtils.getNextWeek(weekStart), aggregation));
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
                String value = (String) data.get(key);
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
                List<Map<String, String>> returnList = new ArrayList<>();
                JSONArray costPerActionTypeArr = (JSONArray) data.get("cost_per_action_type");
                Map<String, Object> dataList = getDataValue(data);
                if (actionsArr != null) {
                    dataList.putAll(getActionsData(actionsArr, "actions_"));
                }
                if (costPerActionTypeArr != null) {
                    dataList.putAll(getActionsData(costPerActionTypeArr, "cost_"));
                }
                dataList.put("date_start", DateUtils.dateToString(DateUtils.toDate(dataList.get("date_start") + "", "yyyy-MM-dd"), "MM/dd/yyyy"));
                dataList.put("date_stop", DateUtils.dateToString(DateUtils.toDate(dataList.get("date_stop") + "", "yyyy-MM-dd"), "MM/dd/yyyy"));
                dataList.put("ctr", ApiUtils.removePercent(dataList.get("ctr") + ""));
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
