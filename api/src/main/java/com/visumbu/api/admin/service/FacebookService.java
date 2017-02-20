/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.api.admin.service;

import com.visumbu.api.utils.ApiUtils;
import com.visumbu.api.utils.DateUtils;
import com.visumbu.api.utils.ExampleConfig;
import com.visumbu.api.utils.Rest;
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

    public final String ACCESS_TOKEN = ExampleConfig.ACCESS_TOKEN;
    // public final Long ACCOUNT_ID = ExampleConfig.ACCOUNT_ID;
    public final String APP_SECRET = ExampleConfig.APP_SECRET;
    public final String BASE_URL = "https://graph.facebook.com/v2.8/act_";
    public final String BASE_URL_FEED = "https://graph.facebook.com/v2.8/";
    //public final APIContext context = new APIContext(ACCESS_TOKEN).enableDebug(true);

    public String getFbPublishedPosts(Long accountId) {
        String url = BASE_URL + accountId + "/insights?fields=adset_name%2Cclicks%2Cimpressions&date_preset=today&access_token=" + ACCESS_TOKEN;
        return Rest.getData(url);
    }

    public Object getAccountPerformance(Long accountId, Date startDate, Date endDate, String aggregation) {
        try {
            String startDateStr = DateUtils.dateToString(startDate, "YYYY-MM-dd");
            String endDateStr = DateUtils.dateToString(endDate, "YYYY-MM-dd");

            String url = BASE_URL + accountId + "/insights?fields=account_name,impressions,clicks,ctr,cpc,spend,actions,reach,cost_per_action_type,adset_name&"
                    + "time_range[since]=" + startDateStr + "&time_range[until]=" + endDateStr
                    + "&access_token=" + ACCESS_TOKEN;
            if(aggregation.equalsIgnoreCase("weekly")) {
                url +="&time_increment=7";
            }
            if(aggregation.equalsIgnoreCase("daily")) {
                url +="&time_increment=1";
            }
            String fbData = Rest.getData(url);
            JSONParser parser = new JSONParser();
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
                Map<String, String> dataList = getDataValue(data);
                if (actionsArr != null) {
                    dataList.putAll(getActionsData(actionsArr, "actions_"));
                }
                if (costPerActionTypeArr != null) {
                    dataList.putAll(getActionsData(costPerActionTypeArr, "cost_"));
                }
                dataList.put("ctr", ApiUtils.removePercent(dataList.get("ctr")));
                dataValueList.add(dataList);
            }
            return dataValueList;  //getActions(actionsArr);
            //return getActions(actions); //array.get("data");
        } catch (ParseException ex) {
            Logger.getLogger(FacebookService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public Object getCampaignPerformance(Long accountId, Date startDate, Date endDate) {
        try {
            String startDateStr = DateUtils.dateToString(startDate, "YYYY-MM-dd");
            String endDateStr = DateUtils.dateToString(endDate, "YYYY-MM-dd");
//https://graph.facebook.com/v2.8/act_10153963646170050/insights?level=campaign&fields=account_name,impressions,clicks,ctr,cpc,spend,actions,reach,cost_per_action_type,campaign_name&time_range[since]=2016-10-01&time_range[until]=2016-10-31&access_token=EAANFRJpxZBZC0BAAqAeGjVgawF8X58ZCYRU824xzKpDcCN49s3wMGqie9MRdUZBnSK8pTsFw3KSOvfof88Oib6CCIOZBlnYQkkeYJrYdyOTJoELEZAmFAFKMoBg5cWvgbdnXdHmZAcYwsJQ6xL1XnMd8m6Hz4C7SAESJQLb36Qh0VSR3gIhiJOw

            String url = BASE_URL + accountId + "/insights?level=campaign&fields=account_name,impressions,clicks,ctr,cpc,spend,actions,reach,cost_per_action_type,campaign_name&"
                    + "time_range[since]=" + startDateStr + "&time_range[until]=" + endDateStr
                    + "&access_token=" + ACCESS_TOKEN;
            String fbData = Rest.getData(url);
            JSONParser parser = new JSONParser();
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
                Map<String, String> dataList = getDataValue(data);
                if (actionsArr != null) {
                    dataList.putAll(getActionsData(actionsArr, "actions_"));
                }
                if (costPerActionTypeArr != null) {
                    dataList.putAll(getActionsData(costPerActionTypeArr, "cost_"));
                }
                                dataList.put("ctr", ApiUtils.removePercent(dataList.get("ctr")));

                dataValueList.add(dataList);
            }
            return dataValueList;  //getActions(actionsArr);
            //return getActions(actions); //array.get("data");
        } catch (ParseException ex) {
            Logger.getLogger(FacebookService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public Object getAdPerformance(Long accountId, Date startDate, Date endDate) {
        try {
            String startDateStr = DateUtils.dateToString(startDate, "YYYY-MM-dd");
            String endDateStr = DateUtils.dateToString(endDate, "YYYY-MM-dd");
//https://graph.facebook.com/v2.8/act_10153963646170050/insights?level=ad&fields=account_name,impressions,clicks,ctr,cpc,spend,actions,reach,cost_per_action_type,adset_name&time_range[since]=2016-10-01&time_range[until]=2016-10-31&access_token=EAANFRJpxZBZC0BAAqAeGjVgawF8X58ZCYRU824xzKpDcCN49s3wMGqie9MRdUZBnSK8pTsFw3KSOvfof88Oib6CCIOZBlnYQkkeYJrYdyOTJoELEZAmFAFKMoBg5cWvgbdnXdHmZAcYwsJQ6xL1XnMd8m6Hz4C7SAESJQLb36Qh0VSR3gIhiJOw

            String url = BASE_URL + accountId + "/insights?level=ad&fields=account_name,impressions,clicks,ctr,cpc,spend,actions,reach,cost_per_action_type,adset_name&"
                    + "time_range[since]=" + startDateStr + "&time_range[until]=" + endDateStr
                    + "&access_token=" + ACCESS_TOKEN;
            String fbData = Rest.getData(url);
            JSONParser parser = new JSONParser();
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
                Map<String, String> dataList = getDataValue(data);
                if (actionsArr != null) {
                    dataList.putAll(getActionsData(actionsArr, "actions_"));
                }
                if (costPerActionTypeArr != null) {
                    dataList.putAll(getActionsData(costPerActionTypeArr, "cost_"));
                }
                dataList.put("ctr", ApiUtils.removePercent(dataList.get("ctr")));
                dataValueList.add(dataList);
            }
            return dataValueList;  //getActions(actionsArr);
            //return getActions(actions); //array.get("data");
        } catch (ParseException ex) {
            Logger.getLogger(FacebookService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public Object getDevicePerformance(Long accountId,Date startDate, Date endDate) {
        try {
            String startDateStr = DateUtils.dateToString(startDate, "YYYY-MM-dd");
            String endDateStr = DateUtils.dateToString(endDate, "YYYY-MM-dd");
//https://graph.facebook.com/v2.8/act_10153963646170050/insights?fields=clicks,impressions,ctr,spend,actions,cost_per_action_type&time_range[since]=2016-10-01&time_range[until]=2016-10-31&breakdowns=impression_device&access_token=EAANFRJpxZBZC0BAAqAeGjVgawF8X58ZCYRU824xzKpDcCN49s3wMGqie9MRdUZBnSK8pTsFw3KSOvfof88Oib6CCIOZBlnYQkkeYJrYdyOTJoELEZAmFAFKMoBg5cWvgbdnXdHmZAcYwsJQ6xL1XnMd8m6Hz4C7SAESJQLb36Qh0VSR3gIhiJOw 

            String url = BASE_URL + accountId + "/insights?fields=clicks,impressions,ctr,cpc,spend,actions,cost_per_action_type&breakdowns=impression_device&"
                    + "time_range[since]=" + startDateStr + "&time_range[until]=" + endDateStr
                    + "&access_token=" + ACCESS_TOKEN;
            String fbData = Rest.getData(url);
            JSONParser parser = new JSONParser();
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
                Map<String, String> dataList = getDataValue(data);
                if (actionsArr != null) {
                    dataList.putAll(getActionsData(actionsArr, "actions_"));
                }
                if (costPerActionTypeArr != null) {
                    dataList.putAll(getActionsData(costPerActionTypeArr, "cost_"));
                }
                dataList.put("ctr", ApiUtils.removePercent(dataList.get("ctr")));
                dataValueList.add(dataList);
            }
            return dataValueList;
            //return getActions(actions); //array.get("data");
        } catch (ParseException ex) {
            Logger.getLogger(FacebookService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public Object getAgePerformance(Long accountId,Date startDate, Date endDate) {
        try {
            String startDateStr = DateUtils.dateToString(startDate, "YYYY-MM-dd");
            String endDateStr = DateUtils.dateToString(endDate, "YYYY-MM-dd");
//https://graph.facebook.com/v2.8/act_10153963646170050/insights?fields=clicks,impressions,ctr,spend,actions,cost_per_action_type&time_range[since]=2016-10-01&time_range[until]=2016-10-31&breakdowns=age&access_token=EAANFRJpxZBZC0BAAqAeGjVgawF8X58ZCYRU824xzKpDcCN49s3wMGqie9MRdUZBnSK8pTsFw3KSOvfof88Oib6CCIOZBlnYQkkeYJrYdyOTJoELEZAmFAFKMoBg5cWvgbdnXdHmZAcYwsJQ6xL1XnMd8m6Hz4C7SAESJQLb36Qh0VSR3gIhiJOw

            String url = BASE_URL + accountId + "/insights?fields=clicks,impressions,ctr,cpc,spend,actions,cost_per_action_type&breakdowns=age&"
                    + "time_range[since]=" + startDateStr + "&time_range[until]=" + endDateStr
                    + "&access_token=" + ACCESS_TOKEN;
            String fbData = Rest.getData(url);
            JSONParser parser = new JSONParser();
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
                Map<String, String> dataList = getDataValue(data);
                if (actionsArr != null) {
                    dataList.putAll(getActionsData(actionsArr, "actions_"));
                }
                if (costPerActionTypeArr != null) {
                    dataList.putAll(getActionsData(costPerActionTypeArr, "cost_"));
                }
                dataList.put("ctr", ApiUtils.removePercent(dataList.get("ctr")));
                dataValueList.add(dataList);
            }
            return dataValueList;
            //return getActions(actions); //array.get("data");
        } catch (ParseException ex) {
            Logger.getLogger(FacebookService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public Object getGenderPerformance(Long accountId, Date startDate, Date endDate) {
        try {
            String startDateStr = DateUtils.dateToString(startDate, "YYYY-MM-dd");
            String endDateStr = DateUtils.dateToString(endDate, "YYYY-MM-dd");
//https://graph.facebook.com/v2.8/act_10153963646170050/insights?fields=clicks,impressions,ctr,spend,actions,cost_per_action_type&time_range[since]=2016-10-01&time_range[until]=2016-10-31&breakdowns=gender&access_token=EAANFRJpxZBZC0BAAqAeGjVgawF8X58ZCYRU824xzKpDcCN49s3wMGqie9MRdUZBnSK8pTsFw3KSOvfof88Oib6CCIOZBlnYQkkeYJrYdyOTJoELEZAmFAFKMoBg5cWvgbdnXdHmZAcYwsJQ6xL1XnMd8m6Hz4C7SAESJQLb36Qh0VSR3gIhiJOw

            String url = BASE_URL + accountId + "/insights?fields=clicks,impressions,ctr,cpc,spend,actions,cost_per_action_type&breakdowns=gender&"
                    + "time_range[since]=" + startDateStr + "&time_range[until]=" + endDateStr
                    + "&access_token=" + ACCESS_TOKEN;
            String fbData = Rest.getData(url);
            JSONParser parser = new JSONParser();
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
                Map<String, String> dataList = getDataValue(data);
                if (actionsArr != null) {
                    dataList.putAll(getActionsData(actionsArr, "actions_"));
                }
                if (costPerActionTypeArr != null) {
                    dataList.putAll(getActionsData(costPerActionTypeArr, "cost_"));
                }
                dataList.put("ctr", ApiUtils.removePercent(dataList.get("ctr")));
                dataValueList.add(dataList);
            }
            return dataValueList;
            //return getActions(actions); //array.get("data");
        } catch (ParseException ex) {
            Logger.getLogger(FacebookService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public List<Map<String, String>> getPostPerformance(Long accountId, Date startDate, Date endDate) {
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
            String fbData = Rest.getData(url);
            JSONParser parser = new JSONParser();
            Object jsonObj = parser.parse(fbData);
            JSONObject array = (JSONObject) jsonObj;
            JSONArray dataArr = (JSONArray) array.get("data");
            List<Map<String, String>> dataValueList = new ArrayList();
            for (int i = 0; i < dataArr.size(); i++) {
                JSONObject data = (JSONObject) dataArr.get(i);
                Map<String, String> dataList = getDataValue(data);
                dataList.put("created_time", DateUtils.dateToString(DateUtils.toDate(dataList.get("created_time").replace("+0000", "").replace("T", " "), "yyyy-MM-dd HH:mm:ss"), "MM/dd/yyyy HH:mm"));
                dataList.put("date", startDateStr);
                dataList.put("reactions", getActionsCount((JSONObject) data.get("reactions")) + "");
                dataList.put("likes", getActionsCount((JSONObject) data.get("likes")) + "");
                dataList.put("comments", getActionsCount((JSONObject) data.get("comments")) + "");
                dataList.put("shares", getShareCount((JSONObject) data.get("shares")) + "");
                dataList.put("engagements", (Long.parseLong(dataList.get("shares")) + Long.parseLong(dataList.get("likes")) + Long.parseLong(dataList.get("comments"))) + "");
                dataValueList.add(dataList);
            }
            return dataValueList;
            //return getActions(actions); //array.get("data");
        } catch (ParseException ex) {
            Logger.getLogger(FacebookService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private List getPostSummary(Long accountId, Date startDate, Date endDate) {
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
            String fbData = Rest.getData(url);
            JSONParser parser = new JSONParser();
            Object jsonObj = parser.parse(fbData);
            JSONObject array = (JSONObject) jsonObj;
            JSONArray dataArr = (JSONArray) array.get("data");
            List dataValueList = new ArrayList();
            for (int i = 0; i < dataArr.size(); i++) {
                JSONObject data = (JSONObject) dataArr.get(i);
                Map<String, String> dataList = getDataValue(data);
                dataList.put("date", startDateStr);
                dataList.put("reactions", getActionsCount((JSONObject) data.get("reactions")) + "");
                dataList.put("likes", getActionsCount((JSONObject) data.get("likes")) + "");
                dataList.put("comments", getActionsCount((JSONObject) data.get("comments")) + "");
                dataList.put("shares", getShareCount((JSONObject) data.get("shares")) + "");
                dataList.put("engagements", (Long.parseLong(dataList.get("shares")) + Long.parseLong(dataList.get("likes")) + Long.parseLong(dataList.get("comments"))) + "");
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

    public Object getLast12WeeksPerformance(Long accountId, Date startDate, Date endDate) {
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
                returnAll.addAll(getPostSummary(accountId, weekStart, DateUtils.getNextWeek(weekStart)));
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

    private Map<String, String> getDataValue(JSONObject data) {
        List<Map<String, String>> returnList = new ArrayList<>();
        Set<String> keySet = data.keySet();
        Map<String, String> dataMap = new HashMap<>();
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

    public Object getInstagramPerformance(Long accountId, Date startDate, Date endDate) {
        try {
            String url = BASE_URL + accountId + "/insights?"
                    //+ "fields=campaigns{insights{campaign_name,clicks,impressions,ctr,cpc,actions,cost_per_action_type,spend,account_name}}"
                    + "fields=impressions%2Cclicks%2Cctr%2Ccpc%2Cspend%2Cactions%2Caccount_name%2Ccost_per_action_type"
                    + "&access_token=" + ACCESS_TOKEN;
            String fbData = Rest.getData(url);
            JSONParser parser = new JSONParser();
            Object jsonObj = parser.parse(fbData);
            JSONObject array = (JSONObject) jsonObj;
            return array.get("data");
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
                Map<String, String> dataList = getDataValue(data);
                if (actionsArr != null) {
                    dataList.putAll(getActionsData(actionsArr, "actions_"));
                }
                if (costPerActionTypeArr != null) {
                    dataList.putAll(getActionsData(costPerActionTypeArr, "cost_"));
                }
                dataList.put("date_start", DateUtils.dateToString(DateUtils.toDate(dataList.get("date_start"), "yyyy-MM-dd"), "MM/dd/yyyy"));
                dataList.put("date_stop", DateUtils.dateToString(DateUtils.toDate(dataList.get("date_stop"), "yyyy-MM-dd"), "MM/dd/yyyy"));
                dataList.put("ctr", ApiUtils.removePercent(dataList.get("ctr")));
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
            Object jsonObj = parser.parse(fbData);
            JSONObject array = (JSONObject) jsonObj;
            return array.get("data");
        } catch (ParseException ex) {
            Logger.getLogger(FacebookService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

}
