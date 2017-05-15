/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

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

/**
 *
 * @author deeta
 */
public class FacebookOrganic {

    //***********************************************************
    public static List<Map<String,String>> getAdSetDetails() throws ParseException
    {
        String fbUrl = "https://graph.facebook.com/v2.8/act_10153963646170050/insights?fields=account_name%2Cimpressions%2Cclicks%2Cctr%2Ccpc%2Cspend%2Cactions%2Creach%2Ccost_per_action_type%2Cadset_name%2Ccampaign_name&level=adset&access_token=EAANFRJpxZBZC0BAAqAeGjVgawF8X58ZCYRU824xzKpDcCN49s3wMGqie9MRdUZBnSK8pTsFw3KSOvfof88Oib6CCIOZBlnYQkkeYJrYdyOTJoELEZAmFAFKMoBg5cWvgbdnXdHmZAcYwsJQ6xL1XnMd8m6Hz4C7SAESJQLb36Qh0VSR3gIhiJOw";
        String fbData = Rest.getData(fbUrl);
            JSONParser parser = new JSONParser();
            Object jsonObj = parser.parse(fbData);
            JSONObject array = (JSONObject) jsonObj;
            JSONArray dataArr = (JSONArray) array.get("data");
            List<Map<String, String>> dataValueList = new ArrayList();
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
            System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
            System.out.println(dataValueList);
            return dataValueList;  //getActions(actionsArr);
       
    }
    
    public static List<Map<String, String>> getAdName() throws ParseException {
        String fbUrl = "https://graph.facebook.com/v2.9/act_10153963646170050/insights?fields=account_name%2Cimpressions%2Cclicks%2Cctr%2Ccpc%2Cspend%2Cactions%2Creach%2Ccost_per_action_type%2Cadset_name%2Cad_name%2Ccampaign_name&level=ad&access_token=EAANFRJpxZBZC0BAAqAeGjVgawF8X58ZCYRU824xzKpDcCN49s3wMGqie9MRdUZBnSK8pTsFw3KSOvfof88Oib6CCIOZBlnYQkkeYJrYdyOTJoELEZAmFAFKMoBg5cWvgbdnXdHmZAcYwsJQ6xL1XnMd8m6Hz4C7SAESJQLb36Qh0VSR3gIhiJOw";
        String fbData = Rest.getData(fbUrl);
            JSONParser parser = new JSONParser();
            Object jsonObj = parser.parse(fbData);
            JSONObject array = (JSONObject) jsonObj;
            JSONArray dataArr = (JSONArray) array.get("data");
            List<Map<String, String>> dataValueList = new ArrayList();
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
            System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
            System.out.println(dataValueList);
            return dataValueList;  //getActions(actionsArr);
          
//        String data = Rest.getData(fbUrl);
//        JSONParser parser = new JSONParser();
//        Object object = parser.parse(data);
//        JSONObject jsonObject = (JSONObject) object;
//        Map<String, Object> jsonToMap = JsonSimpleUtils.jsonToMap(jsonObject);
//        List<Map> arrayData = (List<Map>) jsonToMap.get("data");
//        List<Map<String,String>> listData=new ArrayList<>();
//        for (Iterator<Map> iterator = arrayData.iterator(); iterator.hasNext();) {
//            Map adObject = iterator.next();
//            Map obj=new HashMap();
//            obj.put("account_name",adObject.get("account_name"));
//            obj.put("impressions",adObject.get("impressions"));
//            obj.put("clicks",adObject.get("clicks"));
//            obj.put("ctr",adObject.get("ctr"));
//            obj.put("cpc",adObject.get("cpc"));
//            obj.put("reach",adObject.get("reach"));
//            obj.put("adset_name",adObject.get("adset_name"));
//            obj.put("ad_name",adObject.get("ad_name"));
//            obj.put("campaign_name",adObject.get("campaign_name"));
//            List<Map> array=(List<Map>)adObject.get("actions");
//            
//            listData.add(obj);
//        }
//        System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
//        System.out.println(listData);
//        return listData;
    }
    public static Map<String, String> getDataValue(JSONObject data) {
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
    
    public static Map<String, String> getActionsData(JSONArray actions, String prefix) {
        Map<String, String> actionMap = new HashMap<>();
        for (int i = 0; i < actions.size(); i++) {
            JSONObject action = (JSONObject) actions.get(i);
            actionMap.put(prefix + (String) action.get("action_type"), (String) action.get("value"));
            //returnList.add(actionMap);

        }
        return actionMap;
    }

    public static List<Map<String, String>> getReactionsTotal() throws ParseException {
        String fbUrl = "https://graph.facebook.com/v2.9/185042698207211/posts?fields=message,reactions.type(LIKE).summary(total_count).limit(0).as(like),reactions.type(LOVE).summary(total_count).limit(0).as(love),reactions.type(WOW).summary(total_count).limit(0).as(wow),reactions.type(HAHA).summary(total_count).limit(0).as(haha),reactions.type(SAD).summary(total_count).limit(0).as(sad),reactions.type(ANGRY).summary(total_count).limit(0).as(angry)&access_token=EAANFRJpxZBZC0BAAqAeGjVgawF8X58ZCYRU824xzKpDcCN49s3wMGqie9MRdUZBnSK8pTsFw3KSOvfof88Oib6CCIOZBlnYQkkeYJrYdyOTJoELEZAmFAFKMoBg5cWvgbdnXdHmZAcYwsJQ6xL1XnMd8m6Hz4C7SAESJQLb36Qh0VSR3gIhiJOw&until=1462818600";
        String data = Rest.getData(fbUrl);
        JSONParser parser = new JSONParser();
        Object object = parser.parse(data);
        JSONObject jsonObject = (JSONObject) object;
        Map<String, Object> jsonToMap = JsonSimpleUtils.jsonToMap(jsonObject);
        List<Map> arrayData = (List<Map>) jsonToMap.get("data");
        List<Map<String, String>> listData = new ArrayList<>();
        for (Iterator<Map> iterator = arrayData.iterator(); iterator.hasNext();) {
            Map reactionsData = iterator.next();
            Map<String, String> reactionObject = new HashMap<>();
            reactionObject.put("message", reactionsData.get("message") + "");
            // ((List) ((Map) fbDataMap.get("likes")).get("data")).size());
//            reactionObject.put("love",((List)((Map)reactionsData.get("love")).get("summary")).get(0)+"");
            reactionObject.put("like", ((Map) ((Map) reactionsData.get("like")).get("summary")).get("total_count") + "");
            reactionObject.put("wow", ((Map) ((Map) reactionsData.get("wow")).get("summary")).get("total_count") + "");
            reactionObject.put("haha", ((Map) ((Map) reactionsData.get("haha")).get("summary")).get("total_count") + "");
            reactionObject.put("sad", ((Map) ((Map) reactionsData.get("sad")).get("summary")).get("total_count") + "");
            reactionObject.put("angry", ((Map) ((Map) reactionsData.get("angry")).get("summary")).get("total_count") + "");
            listData.add(reactionObject);
        }
        System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
        System.out.println(listData);
        return null;
    }

    public static List<Map<String, String>> getViewsByAgeGender() {
        try {
            String fbUrl = "https://graph.facebook.com/185042698207211/insights?pretty=0&until=1476489600&metric=page_views_by_age_gender_logged_in_unique&period=days_28&access_token=EAANFRJpxZBZC0BAAqAeGjVgawF8X58ZCYRU824xzKpDcCN49s3wMGqie9MRdUZBnSK8pTsFw3KSOvfof88Oib6CCIOZBlnYQkkeYJrYdyOTJoELEZAmFAFKMoBg5cWvgbdnXdHmZAcYwsJQ6xL1XnMd8m6Hz4C7SAESJQLb36Qh0VSR3gIhiJOw";
            String data = Rest.getData(fbUrl);
            JSONParser parser = new JSONParser();
            Object jsonObj = parser.parse(data);
            JSONObject json = (JSONObject) jsonObj;
            Map<String, Object> jsonToMap = JsonSimpleUtils.jsonToMap(json);
            List<Map> arrayData = (List<Map>) jsonToMap.get("data");
            Map<String, Object> object = arrayData.get(0);
            List<Map<String, Object>> arrayList = (List<Map<String, Object>>) object.get("values");
            Map<String, Object> arrayObject = (Map<String, Object>) arrayList.get(0).get("value");
            List<Map<String, String>> listData = new ArrayList<>();
            System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
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
//                System.out.println(value);
            }
            System.out.println(listData);

//[{25-34={U=0, F=2, M=2}, 18-24={U=0, F=1, M=1}, 55-64={U=0, F=5, M=4}, UNKNOWN={U=0, F=0, M=0}, 13-17={U=0, F=0, M=0}, 35-44={U=0, F=4, M=4}, 65+={U=0, F=2, M=9}, <13={U=0, F=0, M=0}, 45-54={U=0, F=5, M=5}}]
//            for (Iterator<Map<String, Object>> iterator = valueData.iterator(); iterator.hasNext();) {
//                Map<String, Object> viewsAgeGenderObject = iterator.next();
//                Map dataMap = new HashMap();
//                dataMap.put("period","25-34");
//                dataMap.put("Unknown",((Map) viewsAgeGenderObject.get("25-34")).get("U"));
//                dataMap.put("Male",((Map) viewsAgeGenderObject.get("25-34")).get("M"));
//                dataMap.put("Female",((Map) viewsAgeGenderObject.get("25-34")).get("F"));
//                listData.add(dataMap);
//                 dataMap.put("period","18-24");
//                dataMap.put("Unknown",((Map) viewsAgeGenderObject.get("18-24")).get("U"));
//                dataMap.put("Male",((Map) viewsAgeGenderObject.get("18-24")).get("M"));
//                dataMap.put("Female",((Map) viewsAgeGenderObject.get("18-24")).get("F"));
////                dataMap.put("25-34", ((Map) viewsAgeGenderObject.get("25-34")).get("U"));
////                dataMap.put("18-24", ((Map) viewsAgeGenderObject.get("18-24")).get("U"));
////                dataMap.put("55-64", ((Map) viewsAgeGenderObject.get("55-64")).get("U"));
////                dataMap.put("unkknown", ((Map) viewsAgeGenderObject.get("UNKNOWN")).get("U"));
////                dataMap.put("13-17", ((Map) viewsAgeGenderObject.get("13-17")).get("U"));
//                listData.add(dataMap);
//            }
            //[{25-34=0, 18-24=0, unkknown=0, 55-64=0, 13-17=0}]
//            Map<String, String> arrayList = (Map<String, String>) ((Map)(Map<String, String>) object.get("values")).get("value");
//            System.out.println(listData);
            return listData;
        } catch (ParseException ex) {
            Logger.getLogger(FacebookOrganic.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static List<Map<String, String>> getViewsByDay() throws ParseException {

        String fbUrl = "https://graph.facebook.com/v2.9/185042698207211/insights/page_views_total?access_token=EAANFRJpxZBZC0BAAqAeGjVgawF8X58ZCYRU824xzKpDcCN49s3wMGqie9MRdUZBnSK8pTsFw3KSOvfof88Oib6CCIOZBlnYQkkeYJrYdyOTJoELEZAmFAFKMoBg5cWvgbdnXdHmZAcYwsJQ6xL1XnMd8m6Hz4C7SAESJQLb36Qh0VSR3gIhiJOw&since=1460226600&until=1462818600&period=day&limit=50";
        String data = Rest.getData(fbUrl);
        JSONParser parser = new JSONParser();
        Object jsonObj = parser.parse(data);
        JSONObject json = (JSONObject) jsonObj;
        Map<String, Object> jsonToMap = JsonSimpleUtils.jsonToMap(json);
        List<Map> arrayData = (List<Map>) jsonToMap.get("data");
        Map<String, Object> object = arrayData.get(0);
        List<Map> listArrayObject = (List<Map>) object.get("values");
        System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
        List<Map<String, String>> listData = new ArrayList<>();

        for (Iterator<Map> iterator = listArrayObject.iterator(); iterator.hasNext();) {
            Map next = iterator.next();
            Map<String, String> dataMap = new HashMap<>();
            System.out.println("value=" + next.get("value"));
            System.out.println("end_time=" + next.get("end_time"));
            dataMap.put("value", next.get("value") + "");
            dataMap.put("value", next.get("end_time") + "");
            listData.add(dataMap);

        }
        System.out.println(listData);
        return listData;

    }

    public static List<Map<String, String>> getViewsByDevice() throws ParseException {
        String fbUrl = "https://graph.facebook.com/185042698207211/insights?pretty=0&until=1476489600&metric=page_views_by_site_logged_in_unique&period=days_28&access_token=EAANFRJpxZBZC0BAAqAeGjVgawF8X58ZCYRU824xzKpDcCN49s3wMGqie9MRdUZBnSK8pTsFw3KSOvfof88Oib6CCIOZBlnYQkkeYJrYdyOTJoELEZAmFAFKMoBg5cWvgbdnXdHmZAcYwsJQ6xL1XnMd8m6Hz4C7SAESJQLb36Qh0VSR3gIhiJOw";
        String data = Rest.getData(fbUrl);
        JSONParser parser = new JSONParser();
        Object jsonObj = parser.parse(data);
        JSONObject json = (JSONObject) jsonObj;
        Map<String, Object> jsonToMap = JsonSimpleUtils.jsonToMap(json);
        List<Map> arrayData = (List<Map>) jsonToMap.get("data");
        List<Map> deviceData = (List<Map>) arrayData.get(0).get("values");
        List<Map<String, Object>> valueData = new ArrayList();
        System.out.println("*********************************");
        for (Iterator<Map> iterator = deviceData.iterator(); iterator.hasNext();) {
            Map valuesData = iterator.next();
            valueData.add((Map<String, Object>) valuesData.get("value"));
        }

        Map<String, Object> listData = new HashMap();
        listData.put("data", valueData);
        List<Map> deviceViewData = (List<Map>) listData.get("data");
        Map<String, String> returnMap = new HashMap();
        List<Map<String, String>> viewByDeviceListData = new ArrayList<>();
        System.out.println("*********************************");
        System.out.println(deviceViewData);
        for (Iterator<Map> iterator = deviceViewData.iterator(); iterator.hasNext();) {
            Map fbViewDeviceData = iterator.next();
            returnMap.put("OTHER", fbViewDeviceData.get("OTHER") + "");
            returnMap.put("WWW", fbViewDeviceData.get("WWW") + "");
            returnMap.put("API", fbViewDeviceData.get("API") + "");
            returnMap.put("MOBILE", fbViewDeviceData.get("MOBILE") + "");
            viewByDeviceListData.add(returnMap);
        }
        System.out.println(viewByDeviceListData);
        return viewByDeviceListData;
    }

    // ***********************************************************
    public static Map getRecentPosts() throws ParseException {
        String fbUrl = "https://graph.facebook.com/v2.9/185042698207211/posts?fields=message,likes,comments&access_token=EAANFRJpxZBZC0BAAqAeGjVgawF8X58ZCYRU824xzKpDcCN49s3wMGqie9MRdUZBnSK8pTsFw3KSOvfof88Oib6CCIOZBlnYQkkeYJrYdyOTJoELEZAmFAFKMoBg5cWvgbdnXdHmZAcYwsJQ6xL1XnMd8m6Hz4C7SAESJQLb36Qh0VSR3gIhiJOw";
        String data = Rest.getData(fbUrl);
        JSONParser parser = new JSONParser();
        Object jsonObj = parser.parse(data);
        JSONObject json = (JSONObject) jsonObj;
        Map<String, Object> jsonToMap = JsonSimpleUtils.jsonToMap(json);
        Map returnMap = new HashMap<>();
        List<Map> fbData = (List<Map>) jsonToMap.get("data");
        List<Map<String, Object>> listData = new ArrayList<>();
        for (Iterator<Map> iterator = fbData.iterator(); iterator.hasNext();) {
            Map fbDataMap = iterator.next();
            Map<String, Object> fbDataObj = new HashMap<>();
            fbDataObj.put("message", fbDataMap.get("message"));
            fbDataObj.put("likes", ((List) ((Map) fbDataMap.get("likes")).get("data")).size());
            listData.add(fbDataObj);
        }

        returnMap.put("data", listData);
        return returnMap;
    }

    public static List<Map<String, String>> get() throws ParseException {
        String fbUrl = "https://graph.facebook.com/v2.9/185042698207211/posts?fields=message,likes,comments&access_token=EAANFRJpxZBZC0BAAqAeGjVgawF8X58ZCYRU824xzKpDcCN49s3wMGqie9MRdUZBnSK8pTsFw3KSOvfof88Oib6CCIOZBlnYQkkeYJrYdyOTJoELEZAmFAFKMoBg5cWvgbdnXdHmZAcYwsJQ6xL1XnMd8m6Hz4C7SAESJQLb36Qh0VSR3gIhiJOw";
        String data = Rest.getData(fbUrl);
        JSONParser parser = new JSONParser();
        Object jsonObj = parser.parse(data);
        JSONObject json = (JSONObject) jsonObj;
        Map<String, Object> jsonToMap = JsonSimpleUtils.jsonToMap(json);
        Map returnMap = new HashMap<>();
        List<Map> fbData = (List<Map>) jsonToMap.get("data");
        List<Map<String, String>> listData = new ArrayList<>();

        return listData;
    }

    public static void main(String[] argv) throws ParseException {
//        List<Map<String, String>> recentPosts = getTotalOrganicLikes();
        List<Map<String, String>> recentPosts = (List<Map<String, String>>) getAdSetDetails();
//        System.out.println(recentPosts);
    }

    public static List<Map<String, String>> getPageLikesByCity() throws ParseException {
        String fbUrl = "https://graph.facebook.com/185042698207211/insights/page_fans_city?access_token=EAANFRJpxZBZC0BAAqAeGjVgawF8X58ZCYRU824xzKpDcCN49s3wMGqie9MRdUZBnSK8pTsFw3KSOvfof88Oib6CCIOZBlnYQkkeYJrYdyOTJoELEZAmFAFKMoBg5cWvgbdnXdHmZAcYwsJQ6xL1XnMd8m6Hz4C7SAESJQLb36Qh0VSR3gIhiJOw";
        String data = Rest.getData(fbUrl);
        JSONParser parser = new JSONParser();
        Object jsonObj = parser.parse(data);
        JSONObject json = (JSONObject) jsonObj;
        Map<String, Object> jsonToMap = JsonSimpleUtils.jsonToMap(json);
        Map returnMap = new HashMap<>();
        List<Map> fbData = (List<Map>) jsonToMap.get("data");
        List<Map<String, String>> listData = new ArrayList<>();
        Map fbFansData = (Map) fbData.get(0);
        List fbLikesList = (List) fbFansData.get("values");
        System.out.println("=====================");
        System.out.println(fbLikesList.get(0));
        Map<String, Object> values = (Map<String, Object>) ((Map) fbLikesList.get(0)).get("value");

        for (Map.Entry<String, Object> entry : values.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            Map<String, String> dataMap = new HashMap<>();
            dataMap.put(key, value + "");
            listData.add(dataMap);
        }

        return listData;
    }

    public static List<Map<String, String>> getTotalPageViews() throws ParseException {
        String fbUrl = "https://graph.facebook.com/185042698207211/insights/page_views_total?access_token=EAANFRJpxZBZC0BAAqAeGjVgawF8X58ZCYRU824xzKpDcCN49s3wMGqie9MRdUZBnSK8pTsFw3KSOvfof88Oib6CCIOZBlnYQkkeYJrYdyOTJoELEZAmFAFKMoBg5cWvgbdnXdHmZAcYwsJQ6xL1XnMd8m6Hz4C7SAESJQLb36Qh0VSR3gIhiJOw";
        String data = Rest.getData(fbUrl);
        JSONParser parser = new JSONParser();
        Object jsonObj = parser.parse(data);
        JSONObject json = (JSONObject) jsonObj;
        Map<String, Object> jsonToMap = JsonSimpleUtils.jsonToMap(json);
        Map returnMap = new HashMap<>();
        List fbData = (List<Map>) jsonToMap.get("data");
        List<Map<String, String>> listData = new ArrayList<>();
        Map fbFansData = (Map) fbData.get(2);
        List fbLikesList = (List) fbFansData.get("values");
        System.out.println("=====================");
        System.out.println(fbLikesList.get(0));
        String values = ((Map) fbLikesList.get(0)).get("value") + "";
        Map<String, String> returnMapData = new HashMap<>();
        returnMapData.put("page_fans", values);
        listData.add(returnMapData);
        return listData;
    }

    public static List<Map<String, String>> getTotalEngagements() throws ParseException {
        String fbUrl = "https://graph.facebook.com/185042698207211/insights/page_engaged_users?access_token=EAANFRJpxZBZC0BAAqAeGjVgawF8X58ZCYRU824xzKpDcCN49s3wMGqie9MRdUZBnSK8pTsFw3KSOvfof88Oib6CCIOZBlnYQkkeYJrYdyOTJoELEZAmFAFKMoBg5cWvgbdnXdHmZAcYwsJQ6xL1XnMd8m6Hz4C7SAESJQLb36Qh0VSR3gIhiJOw";
        String data = Rest.getData(fbUrl);
        JSONParser parser = new JSONParser();
        Object jsonObj = parser.parse(data);
        JSONObject json = (JSONObject) jsonObj;
        Map<String, Object> jsonToMap = JsonSimpleUtils.jsonToMap(json);
        Map returnMap = new HashMap<>();
        List fbData = (List<Map>) jsonToMap.get("data");
        List<Map<String, String>> listData = new ArrayList<>();
        Map fbFansData = (Map) fbData.get(2);
        List fbLikesList = (List) fbFansData.get("values");
        System.out.println("=====================");
        System.out.println(fbLikesList.get(0));
        String values = ((Map) fbLikesList.get(0)).get("value") + "";
        Map<String, String> returnMapData = new HashMap<>();
        returnMapData.put("engagements", values);
        listData.add(returnMapData);
        return listData;
    }

    public static List<Map<String, String>> getTotalReach() throws ParseException {
        String fbUrl = "https://graph.facebook.com/185042698207211/insights/page_impressions_organic_unique?access_token=EAANFRJpxZBZC0BAAqAeGjVgawF8X58ZCYRU824xzKpDcCN49s3wMGqie9MRdUZBnSK8pTsFw3KSOvfof88Oib6CCIOZBlnYQkkeYJrYdyOTJoELEZAmFAFKMoBg5cWvgbdnXdHmZAcYwsJQ6xL1XnMd8m6Hz4C7SAESJQLb36Qh0VSR3gIhiJOw";
        String data = Rest.getData(fbUrl);
        JSONParser parser = new JSONParser();
        Object jsonObj = parser.parse(data);
        JSONObject json = (JSONObject) jsonObj;
        Map<String, Object> jsonToMap = JsonSimpleUtils.jsonToMap(json);
        Map returnMap = new HashMap<>();
        List fbData = (List<Map>) jsonToMap.get("data");
        List<Map<String, String>> listData = new ArrayList<>();
        Map fbFansData = (Map) fbData.get(2);
        List fbLikesList = (List) fbFansData.get("values");
        System.out.println("=====================");
        System.out.println(fbLikesList.get(0));
        String values = ((Map) fbLikesList.get(0)).get("value") + "";
        Map<String, String> returnMapData = new HashMap<>();
        returnMapData.put("reach", values);
        listData.add(returnMapData);
        return listData;
    }

    public static List<Map<String, String>> getTotalOrganicLikes() throws ParseException {
        String fbUrl = "https://graph.facebook.com/v2.9/185042698207211/insights/page_fans?access_token=EAANFRJpxZBZC0BAAqAeGjVgawF8X58ZCYRU824xzKpDcCN49s3wMGqie9MRdUZBnSK8pTsFw3KSOvfof88Oib6CCIOZBlnYQkkeYJrYdyOTJoELEZAmFAFKMoBg5cWvgbdnXdHmZAcYwsJQ6xL1XnMd8m6Hz4C7SAESJQLb36Qh0VSR3gIhiJOw";
        String data = Rest.getData(fbUrl);
        JSONParser parser = new JSONParser();
        Object jsonObj = parser.parse(data);
        JSONObject json = (JSONObject) jsonObj;
        Map<String, Object> jsonToMap = JsonSimpleUtils.jsonToMap(json);
        Map returnMap = new HashMap<>();
        List fbData = (List<Map>) jsonToMap.get("data");
        List<Map<String, String>> listData = new ArrayList<>();
        Map fbFansData = (Map) fbData.get(0);
        List fbLikesList = (List) fbFansData.get("values");
        System.out.println("=====================");
        System.out.println(fbLikesList.get(0));
        String values = ((Map) fbLikesList.get(0)).get("value") + "";
        Map<String, String> returnMapData = new HashMap<>();
        returnMapData.put("page_fans", values);
        listData.add(returnMapData);
        return listData;
    }

}
