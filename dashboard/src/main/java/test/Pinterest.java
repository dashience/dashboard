/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

/**
 *
 * @author deeta
 */
import com.google.common.collect.HashBiMap;
import com.visumbu.vb.bean.ColumnDef;
import com.visumbu.vb.utils.JsonSimpleUtils;
import com.visumbu.vb.utils.Rest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.util.MultiValueMap;
import static test.Linkedin.getColumnDefObject;

public class Pinterest {

    public static void main(String args[]) throws ParseException {

        List<Map<String, String>> totalBoards = getFollowingsCount();
        System.out.println("-------------------------------------------------------");
        System.out.println(totalBoards);

    }

    public static List<Map<String, String>> getTopPinsTestMethod() {
        try {
            String fbUrl = "https://api.pinterest.com/v1/me/pins/?access_token=AZ3tcCqL10kF4AhAKjY4YHzUBwZJFLtfDUst59xD--hbPkA-ZQAAAAA&fields=id%2Clink%2Cnote%2Curl";
            String data = Rest.getData(fbUrl);
            JSONParser parser = new JSONParser();
            Object jsonObj = parser.parse(data);
            JSONObject json = (JSONObject) jsonObj;
            Map<String, Object> jsonToMap = JsonSimpleUtils.jsonToMap(json);
            Map returnMap = new HashMap<>();
            List<Map<String, Object>> fbData = (List<Map<String, Object>>) jsonToMap.get("data");
            List<Map<String, String>> returnData = new ArrayList<>();
            for (Iterator<Map<String, Object>> iterator = fbData.iterator(); iterator.hasNext();) {
                Map<String, Object> fbDataMap = iterator.next();
                Map<String, String> returnDataMap = new HashMap<>();
                returnDataMap.put("note", fbDataMap.get("note") + "");
                returnDataMap.put("url", fbDataMap.get("url") + "");
               
                returnData.add(returnDataMap);

            }
            System.out.println("********************&&&&&&&&&&&&&&&&&&&&&&&&&&*************************");
            System.out.println(returnData);
            System.out.println("********************&&&&&&&&&&&&&&&&&&&&&&&&&&*************************");
        } catch (ParseException ex) {
            Logger.getLogger(Pinterest.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static List<ColumnDef> getColumnDef(List<Map<String, String>> data) {
        List<ColumnDef> columnDefs = new ArrayList<>();
        for (Iterator<Map<String, String>> iterator = data.iterator(); iterator.hasNext();) {
            Map<String, String> mapData = iterator.next();
            for (Map.Entry<String, String> entrySet : mapData.entrySet()) {
                String key = entrySet.getKey();
                String value = entrySet.getValue();
                columnDefs.add(new ColumnDef(key, "string", key));
            }
            return columnDefs;
        }
        return columnDefs;
    }
    // getTopBoards,getTopPins,getFollowingsCount,getPinsLikeCount,getTotalBoards,getTotalPins

    public static List<Map<String, String>> getTopBoards() throws ParseException {

        String fbUrl = "https://api.pinterest.com/v1/me/boards/?access_token=AZ3tcCqL10kF4AhAKjY4YHzUBwZJFLtfDUst59xD--hbPkA-ZQAAAAA&fields=id%2Cname%2Curl%2Ccounts%2Ccreated_at%2Ccreator%2Cdescription%2Creason";
        String data = Rest.getData(fbUrl);
        JSONParser parser = new JSONParser();
        Object jsonObj = parser.parse(data);
        JSONObject json = (JSONObject) jsonObj;
        Map<String, Object> jsonToMap = JsonSimpleUtils.jsonToMap(json);
        Map returnMap = new HashMap<>();
        List<Map<String, Object>> fbData = (List<Map<String, Object>>) jsonToMap.get("data");
        List<Map<String, String>> returnData = new ArrayList<>();
        for (Iterator<Map<String, Object>> iterator = fbData.iterator(); iterator.hasNext();) {
            Map<String, Object> fbDataMap = iterator.next();
            Map<String, String> returnDataMap = new HashMap<>();
            returnDataMap.put("name", fbDataMap.get("name") + "");
            returnDataMap.put("description", fbDataMap.get("description") + "");
            returnDataMap.put("counts", ((Map) fbDataMap.get("counts")).get("pins") + "");
            returnData.add(returnDataMap);

        }
        return returnData;
    }

    public static List<Map<String, Object>> getTopPins() throws ParseException {

        String fbUrl = "https://api.pinterest.com/v1/me/pins/?access_token=AZ3tcCqL10kF4AhAKjY4YHzUBwZJFLtfDUst59xD--hbPkA-ZQAAAAA&fields=id%2Clink%2Cnote%2Curl";
        String data = Rest.getData(fbUrl);
        JSONParser parser = new JSONParser();
        Object jsonObj = parser.parse(data);
        JSONObject json = (JSONObject) jsonObj;
        Map<String, Object> jsonToMap = JsonSimpleUtils.jsonToMap(json);
        Map returnMap = new HashMap<>();
        List<Map<String, Object>> fbData = (List<Map<String, Object>>) jsonToMap.get("data");

        return fbData;
    }

    public static List<Map<String, String>> getFollowingsCount() throws ParseException {
        ArrayList<String> followingsApiUrls = new ArrayList<String>();

        followingsApiUrls.add("https://api.pinterest.com/v1/me/followers/?access_token=AXCeGz6mwYDUI1eKrMbJ4PFKErp9FLtlVR4iV_hD--hbPkA-ZQAAAAA&fields=first_name%2Cid%2Clast_name%2Curl%2Caccount_type%2Cbio%2Ccounts%2Cimage%2Ccreated_at%2Cusername");
        followingsApiUrls.add("https://api.pinterest.com/v1/me/followers/?access_token=AXCeGz6mwYDUI1eKrMbJ4PFKErp9FLtlVR4iV_hD--hbPkA-ZQAAAAA&fields=first_name%2Cid%2Clast_name%2Curl%2Caccount_type%2Cbio%2Ccounts%2Cimage%2Ccreated_at%2Cusername&cursor=Pz9Nakl5TXpveU56azVPVGMwTXpreU5ERXpOVGczTXpBNk9USXlNek0zTURVMU9UWTJNelk1TXpnek1WOUZ8ZDdiZWVlOWQ5NDZlMmE4MjgwZjcyZTAxY2YyM2NiZDVmOGE5MjllMWIwMWZjY2MxYThlNjAzMjg4Yzk1MjhiMg%3D%3D");
        followingsApiUrls.add("https://api.pinterest.com/v1/me/followers/?access_token=AXCeGz6mwYDUI1eKrMbJ4PFKErp9FLtlVR4iV_hD--hbPkA-ZQAAAAA&fields=first_name%2Cid%2Clast_name%2Curl%2Caccount_type%2Cbio%2Ccounts%2Cimage%2Ccreated_at%2Cusername&cursor=Pz9Nakl5TXpveE9EY3pNakU0TURNeE5Ua3pOelV6TURnNk9USXlNek0zTURVM01USTNOVE13TmpNeE5GOUZ8MTMzODE2NzlmMmYwNDMwYTc5NzU4MDg5YTE1OTU3Nzc4YTYzODFlNjFmY2YzN2ZkYzQyMzJkMDUwMzM5MWQ2MA%3D%3D");
        followingsApiUrls.add("https://api.pinterest.com/v1/me/followers/?access_token=AXCeGz6mwYDUI1eKrMbJ4PFKErp9FLtlVR4iV_hD--hbPkA-ZQAAAAA&fields=first_name%2Cid%2Clast_name%2Curl%2Caccount_type%2Cbio%2Ccounts%2Cimage%2Ccreated_at%2Cusername&cursor=Pz9Nakl5TXpveU5EVXlNelV5TnprMk1UYzFPVEF6TnpjNk9USXlNek0zTURVNE1UZzNOekUyT1RVM05WOUZ8MmU0YzRmZWYwYmZhM2JlZTRmZGM2MjM0NzViNWMzMTg5NDJjZmQ4YjljNGZhYjc1ZWIxN2QzMWQyZmY4ZmU2NA%3D%3D");
        followingsApiUrls.add("https://api.pinterest.com/v1/me/followers/?access_token=AXCeGz6mwYDUI1eKrMbJ4PFKErp9FLtlVR4iV_hD--hbPkA-ZQAAAAA&fields=first_name%2Cid%2Clast_name%2Curl%2Caccount_type%2Cbio%2Ccounts%2Cimage%2Ccreated_at%2Cusername&cursor=Pz9Nakl5TkRveU5UZzJNRFV6TkRFd01URXpOVEUwTVRRNk9USXlNek0zTURVMU16WXhPVFk0TVRVNU4xOUp8YWQxZjViZjlmNTQ2YTg2YzI3NGU0MmQ0Nzg5ODVjMmVmNTY2MDRlZDZjZDZhMzAzNzE5MTU5YjQ1NWVkZjc5NQ%3D%3D");

        String fbUrl = "https://api.pinterest.com/v1/me/likes/?access_token=AS94T9w2BZ8g5z1i47YGkp7c6U88FLtkVt0cCntD--hbPkA-ZQAAAAA&fields=id%2Clink%2Cnote%2Curl%2Cattribution%2Cboard%2Ccolor%2Ccounts%2Ccreated_at%2Coriginal_link%2Cmetadata%2Cimage%2Cmedia%2Ccreator";

        int maxCount = 0;
        for (int i = 0; i < followingsApiUrls.size(); i++) {
            int getFollowingsCount = processFollowings(followingsApiUrls.get(i));
            maxCount = maxCount + getFollowingsCount;
        }
        System.out.println("*******************Start*****************************");
        System.out.println(maxCount);
        System.out.println("****************End********************************");
        
        
        Map returnMap = new HashMap<>();
        returnMap.put("followings_count", maxCount);
        List<Map<String, String>> listData = new ArrayList<>();
        listData.add(returnMap);
        return listData;
    }

    public static int processFollowings(String fbUrl) throws ParseException {

        String data = Rest.getData(fbUrl);
        JSONParser parser = new JSONParser();
        Object jsonObj = parser.parse(data);
        JSONObject json = (JSONObject) jsonObj;
        Map<String, Object> jsonToMap = JsonSimpleUtils.jsonToMap(json);
        Map returnMap = new HashMap<>();
        List fbData = (List<Map>) jsonToMap.get("data");
        fbData.lastIndexOf(jsonObj);
        int followingsCount = fbData.size();
        return followingsCount;
    }

    public static List<Map<String, String>> getPinsLikeCount() throws ParseException {
        String fbUrl = "https://api.pinterest.com/v1/me/likes/?access_token=AS94T9w2BZ8g5z1i47YGkp7c6U88FLtkVt0cCntD--hbPkA-ZQAAAAA&fields=id%2Clink%2Cnote%2Curl%2Cattribution%2Cboard%2Ccolor%2Ccounts%2Ccreated_at%2Coriginal_link%2Cmetadata%2Cimage%2Cmedia%2Ccreator";
        String data = Rest.getData(fbUrl);
        JSONParser parser = new JSONParser();
        Object jsonObj = parser.parse(data);
        JSONObject json = (JSONObject) jsonObj;
        Map<String, Object> jsonToMap = JsonSimpleUtils.jsonToMap(json);
        Map returnMap = new HashMap<>();
        List fbData = (List<Map>) jsonToMap.get("data");
        fbData.lastIndexOf(jsonObj);
        String boardsCount = fbData.size() + "";
        Map<String, String> boardsSize = new HashMap<>();
        boardsSize.put("total_pin_likes", boardsCount);
        List<Map<String, String>> listData = new ArrayList<>();
        listData.add(boardsSize);
        return listData;
    }

    public static List<Map<String, String>> getTotalBoards() throws ParseException {
        String fbUrl = "https://api.pinterest.com/v1/me/boards/?access_token=AZ3tcCqL10kF4AhAKjY4YHzUBwZJFLtfDUst59xD--hbPkA-ZQAAAAA&fields=id%2Cname%2Curl%2Ccounts%2Ccreated_at%2Ccreator%2Cdescription%2Creason";
        String data = Rest.getData(fbUrl);
        JSONParser parser = new JSONParser();
        Object jsonObj = parser.parse(data);
        JSONObject json = (JSONObject) jsonObj;
        Map<String, Object> jsonToMap = JsonSimpleUtils.jsonToMap(json);
        Map returnMap = new HashMap<>();
        List fbData = (List<Map>) jsonToMap.get("data");
        fbData.lastIndexOf(jsonObj);
        String boardsCount = fbData.size() + "";
        Map<String, String> boardsSize = new HashMap<>();
        boardsSize.put("total_boards", boardsCount);
        List<Map<String, String>> listData = new ArrayList<>();
        listData.add(boardsSize);
        return listData;
    }

    public static List<Map<String, String>> getTotalPins() throws ParseException {
        String fbUrl = "https://api.pinterest.com/v1/me/pins/?access_token=AZ3tcCqL10kF4AhAKjY4YHzUBwZJFLtfDUst59xD--hbPkA-ZQAAAAA&fields=id%2Clink%2Cnote%2Curl";
        String data = Rest.getData(fbUrl);
        JSONParser parser = new JSONParser();
        Object jsonObj = parser.parse(data);
        JSONObject json = (JSONObject) jsonObj;
        Map<String, Object> jsonToMap = JsonSimpleUtils.jsonToMap(json);
        Map returnMap = new HashMap<>();
        List fbData = (List<Map>) jsonToMap.get("data");
        fbData.lastIndexOf(jsonObj);
        String pinsCount = fbData.size() + "";
        Map<String, String> pinsSize = new HashMap<>();
        pinsSize.put("total_pins", pinsCount);
        List<Map<String, String>> listData = new ArrayList<>();
        listData.add(pinsSize);
        return listData;
    }

    public static List<ColumnDef> getColumnDefObject(List<Map<String, Object>> data) {
        List<ColumnDef> columnDefs = new ArrayList<>();
        for (Iterator<Map<String, Object>> iterator = data.iterator(); iterator.hasNext();) {
            Map<String, Object> mapData = iterator.next();
            for (Map.Entry<String, Object> entrySet : mapData.entrySet()) {
                String key = entrySet.getKey();

                Object value = entrySet.getValue();
//                System.out.println(value.getClass());
                columnDefs.add(new ColumnDef(key, "string", key));

            }
            return columnDefs;
        }
        return columnDefs;
    }
}
