/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import com.visumbu.vb.utils.JsonSimpleUtils;
import com.visumbu.vb.utils.Rest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author deeta
 */
public class FacebookOrganic {

    public static Map getRecentPosts() throws ParseException {
        String fbUrl = "https://graph.facebook.com/v2.9/185042698207211/posts?fields=message,likes,comments&access_token=EAANFRJpxZBZC0BAAqAeGjVgawF8X58ZCYRU824xzKpDcCN49s3wMGqie9MRdUZBnSK8pTsFw3KSOvfof88Oib6CCIOZBlnYQkkeYJrYdyOTJoELEZAmFAFKMoBg5cWvgbdnXdHmZAcYwsJQ6xL1XnMd8m6Hz4C7SAESJQLb36Qh0VSR3gIhiJOw";
        String data = Rest.getData(fbUrl);
        JSONParser parser = new JSONParser();
        Object jsonObj = parser.parse(data);
        JSONObject json = (JSONObject) jsonObj;
        Map<String, Object> jsonToMap = JsonSimpleUtils.jsonToMap(json);
        Map returnMap = new HashMap<>();
        List<Map> fbData = (List<Map>)jsonToMap.get("data");
        List<Map<String,Object>> listData = new ArrayList<>();
        for (Iterator<Map> iterator = fbData.iterator(); iterator.hasNext();) {
            Map fbDataMap = iterator.next();
            Map<String, Object> fbDataObj = new HashMap<>();
            fbDataObj.put("message", fbDataMap.get("message"));
            fbDataObj.put("likes", ((List) ((Map)fbDataMap.get("likes")).get("data") ).size());
            listData.add(fbDataObj);
        }
        
        returnMap.put("data", listData);
        return returnMap;
    }
    
    
    public static List<Map<String,String>> get() throws ParseException {
        String fbUrl = "https://graph.facebook.com/v2.9/185042698207211/posts?fields=message,likes,comments&access_token=EAANFRJpxZBZC0BAAqAeGjVgawF8X58ZCYRU824xzKpDcCN49s3wMGqie9MRdUZBnSK8pTsFw3KSOvfof88Oib6CCIOZBlnYQkkeYJrYdyOTJoELEZAmFAFKMoBg5cWvgbdnXdHmZAcYwsJQ6xL1XnMd8m6Hz4C7SAESJQLb36Qh0VSR3gIhiJOw";
        String data = Rest.getData(fbUrl);
        JSONParser parser = new JSONParser();
        Object jsonObj = parser.parse(data);
        JSONObject json = (JSONObject) jsonObj;
        Map<String, Object> jsonToMap = JsonSimpleUtils.jsonToMap(json);
        Map returnMap = new HashMap<>();
        List<Map> fbData = (List<Map>)jsonToMap.get("data");
        List<Map<String,String>> listData = new ArrayList<>();
        
        
        return listData;
    }

    public static void main(String[] argv) throws ParseException {
        List<Map<String, String>> recentPosts = getTotalEngagements();
        System.out.println(recentPosts);
    }
    public static List<Map<String,String>>  getPageLikesByCity() throws ParseException {
        String fbUrl = "https://graph.facebook.com/185042698207211/insights/page_fans_city?access_token=EAANFRJpxZBZC0BAAqAeGjVgawF8X58ZCYRU824xzKpDcCN49s3wMGqie9MRdUZBnSK8pTsFw3KSOvfof88Oib6CCIOZBlnYQkkeYJrYdyOTJoELEZAmFAFKMoBg5cWvgbdnXdHmZAcYwsJQ6xL1XnMd8m6Hz4C7SAESJQLb36Qh0VSR3gIhiJOw";
        String data = Rest.getData(fbUrl);
        JSONParser parser = new JSONParser();
        Object jsonObj = parser.parse(data);
        JSONObject json = (JSONObject) jsonObj;
        Map<String, Object> jsonToMap = JsonSimpleUtils.jsonToMap(json);
        Map returnMap = new HashMap<>();
        List<Map> fbData = (List<Map>)jsonToMap.get("data");
        List<Map<String,String>> listData = new ArrayList<>();
        Map fbFansData = (Map)fbData.get(0);
        List fbLikesList = (List)fbFansData.get("values");
        System.out.println("=====================");
        System.out.println(fbLikesList.get(0));
        Map<String,Object> values = (Map<String,Object>)((Map)fbLikesList.get(0)).get("value");

        for (Map.Entry<String, Object> entry : values.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            Map<String, String> dataMap = new HashMap<>();
            dataMap.put(key, value + "");
            listData.add(dataMap);
        }        
        
        return listData;
    }

    public static List<Map<String,String>> getTotalPageViews() throws ParseException {
        String fbUrl = "https://graph.facebook.com/185042698207211/insights/page_views_total?access_token=EAANFRJpxZBZC0BAAqAeGjVgawF8X58ZCYRU824xzKpDcCN49s3wMGqie9MRdUZBnSK8pTsFw3KSOvfof88Oib6CCIOZBlnYQkkeYJrYdyOTJoELEZAmFAFKMoBg5cWvgbdnXdHmZAcYwsJQ6xL1XnMd8m6Hz4C7SAESJQLb36Qh0VSR3gIhiJOw";
        String data = Rest.getData(fbUrl);
        JSONParser parser = new JSONParser();
        Object jsonObj = parser.parse(data);
        JSONObject json = (JSONObject) jsonObj;
        Map<String, Object> jsonToMap = JsonSimpleUtils.jsonToMap(json);
        Map returnMap = new HashMap<>();
        List fbData = (List<Map>)jsonToMap.get("data");
        List<Map<String,String>> listData = new ArrayList<>();
        Map fbFansData = (Map)fbData.get(2);
        List fbLikesList = (List)fbFansData.get("values");
        System.out.println("=====================");
        System.out.println(fbLikesList.get(0));
        String values = ((Map)fbLikesList.get(0)).get("value") + "";
        Map<String, String> returnMapData = new HashMap<>();
        returnMapData.put("page_fans", values);
        listData.add(returnMapData);
        return listData;        
    }

    public static List<Map<String,String>> getTotalEngagements() throws ParseException {
        String fbUrl = "https://graph.facebook.com/185042698207211/insights/page_engaged_users?access_token=EAANFRJpxZBZC0BAAqAeGjVgawF8X58ZCYRU824xzKpDcCN49s3wMGqie9MRdUZBnSK8pTsFw3KSOvfof88Oib6CCIOZBlnYQkkeYJrYdyOTJoELEZAmFAFKMoBg5cWvgbdnXdHmZAcYwsJQ6xL1XnMd8m6Hz4C7SAESJQLb36Qh0VSR3gIhiJOw";
        String data = Rest.getData(fbUrl);
        JSONParser parser = new JSONParser();
        Object jsonObj = parser.parse(data);
        JSONObject json = (JSONObject) jsonObj;
        Map<String, Object> jsonToMap = JsonSimpleUtils.jsonToMap(json);
        Map returnMap = new HashMap<>();
        List fbData = (List<Map>)jsonToMap.get("data");
        List<Map<String,String>> listData = new ArrayList<>();
        Map fbFansData = (Map)fbData.get(2);
        List fbLikesList = (List)fbFansData.get("values");
        System.out.println("=====================");
        System.out.println(fbLikesList.get(0));
        String values = ((Map)fbLikesList.get(0)).get("value") + "";
        Map<String, String> returnMapData = new HashMap<>();
        returnMapData.put("engagements", values);
        listData.add(returnMapData);
        return listData;        
    }
    
    public static List<Map<String,String>> getTotalReach() throws ParseException {
        String fbUrl = "https://graph.facebook.com/185042698207211/insights/page_impressions_organic_unique?access_token=EAANFRJpxZBZC0BAAqAeGjVgawF8X58ZCYRU824xzKpDcCN49s3wMGqie9MRdUZBnSK8pTsFw3KSOvfof88Oib6CCIOZBlnYQkkeYJrYdyOTJoELEZAmFAFKMoBg5cWvgbdnXdHmZAcYwsJQ6xL1XnMd8m6Hz4C7SAESJQLb36Qh0VSR3gIhiJOw";
        String data = Rest.getData(fbUrl);
        JSONParser parser = new JSONParser();
        Object jsonObj = parser.parse(data);
        JSONObject json = (JSONObject) jsonObj;
        Map<String, Object> jsonToMap = JsonSimpleUtils.jsonToMap(json);
        Map returnMap = new HashMap<>();
        List fbData = (List<Map>)jsonToMap.get("data");
        List<Map<String,String>> listData = new ArrayList<>();
        Map fbFansData = (Map)fbData.get(2);
        List fbLikesList = (List)fbFansData.get("values");
        System.out.println("=====================");
        System.out.println(fbLikesList.get(0));
        String values = ((Map)fbLikesList.get(0)).get("value") + "";
        Map<String, String> returnMapData = new HashMap<>();
        returnMapData.put("reach", values);
        listData.add(returnMapData);
        return listData;        
    }
    
    
    public static List<Map<String,String>> getTotalOrganicLikes() throws ParseException {
        String fbUrl = "https://graph.facebook.com/v2.9/185042698207211/insights/page_fans?access_token=EAANFRJpxZBZC0BAAqAeGjVgawF8X58ZCYRU824xzKpDcCN49s3wMGqie9MRdUZBnSK8pTsFw3KSOvfof88Oib6CCIOZBlnYQkkeYJrYdyOTJoELEZAmFAFKMoBg5cWvgbdnXdHmZAcYwsJQ6xL1XnMd8m6Hz4C7SAESJQLb36Qh0VSR3gIhiJOw";
        String data = Rest.getData(fbUrl);
        JSONParser parser = new JSONParser();
        Object jsonObj = parser.parse(data);
        JSONObject json = (JSONObject) jsonObj;
        Map<String, Object> jsonToMap = JsonSimpleUtils.jsonToMap(json);
        Map returnMap = new HashMap<>();
        List fbData = (List<Map>)jsonToMap.get("data");
        List<Map<String,String>> listData = new ArrayList<>();
        Map fbFansData = (Map)fbData.get(0);
        List fbLikesList = (List)fbFansData.get("values");
        System.out.println("=====================");
        System.out.println(fbLikesList.get(0));
        String values = ((Map)fbLikesList.get(0)).get("value") + "";
        Map<String, String> returnMapData = new HashMap<>();
        returnMapData.put("page_fans", values);
        listData.add(returnMapData);
        return listData;
    }
    
}
