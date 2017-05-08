/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.admin.controller;

import com.visumbu.vb.admin.service.UiService;
import com.visumbu.vb.admin.service.UserService;
import com.visumbu.vb.bean.ColumnDef;
import com.visumbu.vb.bean.LoginUserBean;
import com.visumbu.vb.bean.Permission;
import com.visumbu.vb.bean.UrlBean;
import com.visumbu.vb.bean.map.auth.SecurityAuthBean;
import com.visumbu.vb.bean.map.auth.SecurityAuthRoleBean;
import com.visumbu.vb.controller.BaseController;
import com.visumbu.vb.model.Account;
import com.visumbu.vb.model.AccountUser;
import com.visumbu.vb.model.Agency;
import com.visumbu.vb.model.AgencyLicence;
import com.visumbu.vb.model.AgencyProduct;
import com.visumbu.vb.model.Dealer;
import com.visumbu.vb.model.Property;
import com.visumbu.vb.model.UserAccount;
import com.visumbu.vb.model.VbUser;
import com.visumbu.vb.utils.JsonSimpleUtils;
import com.visumbu.vb.utils.Rest;
import com.visumbu.vb.utils.VbUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.collections.OrderedMap;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import static test.Linkedin.getColumnDefObject;
import static test.Pinterest.getColumnDefObject;
import static test.Pinterest.processFollowings;

/**
 *
 * @author jp
 */
@Controller
@RequestMapping("enlivant")
public class EnliventController extends BaseController {

    @Autowired
    private UserService userService;
    @Autowired
    private UiService uiService;

    @RequestMapping(value = "linkedIn", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    Map getLinkedIn(HttpServletRequest request, HttpServletResponse response) {
        try {
            String url = "https://api.linkedin.com/v1/companies/10671978/historical-status-update-statistics:(time,like-count,impression-count,click-count,engagement)?oauth2_access_token=AQVrr3w94F9NPdypSkVL_mY1hpRBlbg0DjsAymBxVnIvKw91gdapkEZt-hIUdzC34AZfgShbH17iWw0ef8VtT7gSKQsQ8mtPt2d9w_soy5FnKJaZgSHiT-Ug9MnzmB3fjlR2_tc6OoGmgeaMEuAHV3Yvnb-gzRg2TC4Aez2pUNR9jiv5WWM&time-granularity=day&start-timestamp=1466899200000&format=json";
            MultiValueMap<String, String> valueMap = null;
            String data = Rest.getData(url, valueMap);
            JSONParser parser = new JSONParser();
            Object jsonObj = parser.parse(data);
            JSONObject json = (JSONObject) jsonObj;
            Map<String, Object> jsonToMap = JsonSimpleUtils.jsonToMap(json);
            System.out.println(jsonToMap.get("_total"));
            List<Map<String, Object>> myData = (List<Map<String, Object>>) jsonToMap.get("values");

            List<ColumnDef> columnDefObject = getColumnDefObject(myData);
            Map returnMap = new HashMap();
            returnMap.put("columnDefs", columnDefObject);
            returnMap.put("data", myData);
            return returnMap;
        } catch (ParseException ex) {
            Logger.getLogger(EnliventController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @RequestMapping(value = "twitter", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    Map getTwitter(HttpServletRequest request, HttpServletResponse response) {
        try {
            String url = "https://api.twitter.com/1.1/users/lookup.json?screen_name=Enlivant&user_id=2964932975&oauth_consumer_key=DC0sePOBbQ8bYdC8r4Smg&oauth_signature_method=HMAC-SHA1&oauth_timestamp=1493999004&oauth_nonce=458542271&oauth_version=1.0&oauth_token=2964932975-RlNIi6QnoQtydUosFxNWUTuWgJlsJKCuGX4HmZS&oauth_signature=bfeMg7mE0MiRLT5xvvcb9fYa7KA%3D";

            MultiValueMap<String, String> valueMap = null;
            String data = Rest.getData(url, valueMap);

            JSONParser parser = new JSONParser();
            JSONArray jsonArray = (JSONArray) parser.parse(data);

            List<Map<String, Object>> myData = (List<Map<String, Object>>) jsonArray;
            List<Map<String, Object>> twitterData = new ArrayList<>();

            for (Iterator<Map<String, Object>> iterator = myData.iterator(); iterator.hasNext();) {
                Map<String, Object> mapData = iterator.next();
                Map<String, Object> twitterMapData = new HashMap<>();
                twitterMapData.put("followers_count", mapData.get("followers_count"));
                twitterMapData.put("friends_count", mapData.get("friends_count"));
                twitterMapData.put("listed_count", mapData.get("listed_count"));
                twitterMapData.put("statuses_count", mapData.get("statuses_count"));

                twitterData.add(twitterMapData);

            }
            List<ColumnDef> columnDefObject = getColumnDefObject(twitterData);
            Map returnMap = new HashMap();
            returnMap.put("columnDefs", columnDefObject);
            returnMap.put("data", twitterData);
            return returnMap;
        } catch (ParseException ex) {
            Logger.getLogger(EnliventController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @RequestMapping(value = "pinterest/topRecentBoards", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    List<Map<String, String>> getTopBoards() {

        try {
            String fbUrl = "https://api.pinterest.com/v1/me/boards/?access_token=AZ3tcCqL10kF4AhAKjY4YHzUBwZJFLtfDUst59xD--hbPkA-ZQAAAAA&fields=id%2Cname%2Curl%2Ccounts%2Ccreated_at%2Ccreator%2Cdescription%2Creason";
            String data = Rest.getData(fbUrl);
            JSONParser parser = new JSONParser();
            Object jsonObj = parser.parse(data);
            JSONObject json = (JSONObject) jsonObj;
            Map<String, Object> jsonToMap = JsonSimpleUtils.jsonToMap(json);
            Map returnMap = new HashMap<>();
            List<Map<String, Object>> fbData = (List<Map<String, Object>>) jsonToMap.get("data");
            List<ColumnDef> columnDefObject = getColumnDefObject(fbData);
            returnMap.put("columnDefs", columnDefObject);
            returnMap.put("data", fbData);
            List<Map<String, String>> listData = new ArrayList<>();
            listData.add(returnMap);
            return listData;
        } catch (ParseException ex) {
            Logger.getLogger(EnliventController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @RequestMapping(value = "pinterest/topRecentPins", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    List<Map<String, String>> getTopPins() {

        try {
            String fbUrl = "https://api.pinterest.com/v1/me/pins/?access_token=AZ3tcCqL10kF4AhAKjY4YHzUBwZJFLtfDUst59xD--hbPkA-ZQAAAAA&fields=id%2Clink%2Cnote%2Curl";
            String data = Rest.getData(fbUrl);
            JSONParser parser = new JSONParser();
            Object jsonObj = parser.parse(data);
            JSONObject json = (JSONObject) jsonObj;
            Map<String, Object> jsonToMap = JsonSimpleUtils.jsonToMap(json);
            Map returnMap = new HashMap<>();
            List<Map<String, Object>> fbData = (List<Map<String, Object>>) jsonToMap.get("data");
            List<ColumnDef> columnDefObject = getColumnDefObject(fbData);
            returnMap.put("columnDefs", columnDefObject);
            returnMap.put("data", fbData);
            List<Map<String, String>> listData = new ArrayList<>();
            listData.add(returnMap);
            return listData;
        } catch (ParseException ex) {
            Logger.getLogger(EnliventController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    @RequestMapping(value = "pinterest/pinsLikeCount", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    List<Map<String, String>> getPinsLikeCount(){
        try {
            String fbUrl = "https://api.pinterest.com/v1/me/likes/?access_token=AS94T9w2BZ8g5z1i47YGkp7c6U88FLtkVt0cCntD--hbPkA-ZQAAAAA&fields=id%2Clink%2Cnote%2Curl%2Cattribution%2Cboard%2Ccolor%2Ccounts%2Ccreated_at%2Coriginal_link%2Cmetadata%2Cimage%2Cmedia%2Ccreator";
            String data = Rest.getData(fbUrl);
            JSONParser parser = new JSONParser();
            Object jsonObj = parser.parse(data);
            JSONObject json = (JSONObject) jsonObj;
            Map<String, Object> jsonToMap = JsonSimpleUtils.jsonToMap(json);
            Map returnMap = new HashMap<>();
            List fbData = (List<Map>) jsonToMap.get("data");
            fbData.lastIndexOf(jsonObj);
            String likesCount = fbData.size() + "";
            Map<String, String> boardsSize = new HashMap<>();
            boardsSize.put("total_pin_likes", likesCount);
            List<Map<String, String>> listData = new ArrayList<>();
            listData.add(boardsSize);
            return listData;
        } catch (ParseException ex) {
            Logger.getLogger(EnliventController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @RequestMapping(value = "pinterest/followingsCount", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    List<Map<String, String>> getFollowingsCount() {
        ArrayList<String> followingsApiUrls = new ArrayList<String>();

        followingsApiUrls.add("https://api.pinterest.com/v1/me/followers/?access_token=AXCeGz6mwYDUI1eKrMbJ4PFKErp9FLtlVR4iV_hD--hbPkA-ZQAAAAA&fields=first_name%2Cid%2Clast_name%2Curl%2Caccount_type%2Cbio%2Ccounts%2Cimage%2Ccreated_at%2Cusername");
        followingsApiUrls.add("https://api.pinterest.com/v1/me/followers/?access_token=AXCeGz6mwYDUI1eKrMbJ4PFKErp9FLtlVR4iV_hD--hbPkA-ZQAAAAA&fields=first_name%2Cid%2Clast_name%2Curl%2Caccount_type%2Cbio%2Ccounts%2Cimage%2Ccreated_at%2Cusername&cursor=Pz9Nakl5TXpveU56azVPVGMwTXpreU5ERXpOVGczTXpBNk9USXlNek0zTURVMU9UWTJNelk1TXpnek1WOUZ8ZDdiZWVlOWQ5NDZlMmE4MjgwZjcyZTAxY2YyM2NiZDVmOGE5MjllMWIwMWZjY2MxYThlNjAzMjg4Yzk1MjhiMg%3D%3D");
        followingsApiUrls.add("https://api.pinterest.com/v1/me/followers/?access_token=AXCeGz6mwYDUI1eKrMbJ4PFKErp9FLtlVR4iV_hD--hbPkA-ZQAAAAA&fields=first_name%2Cid%2Clast_name%2Curl%2Caccount_type%2Cbio%2Ccounts%2Cimage%2Ccreated_at%2Cusername&cursor=Pz9Nakl5TXpveE9EY3pNakU0TURNeE5Ua3pOelV6TURnNk9USXlNek0zTURVM01USTNOVE13TmpNeE5GOUZ8MTMzODE2NzlmMmYwNDMwYTc5NzU4MDg5YTE1OTU3Nzc4YTYzODFlNjFmY2YzN2ZkYzQyMzJkMDUwMzM5MWQ2MA%3D%3D");
        followingsApiUrls.add("https://api.pinterest.com/v1/me/followers/?access_token=AXCeGz6mwYDUI1eKrMbJ4PFKErp9FLtlVR4iV_hD--hbPkA-ZQAAAAA&fields=first_name%2Cid%2Clast_name%2Curl%2Caccount_type%2Cbio%2Ccounts%2Cimage%2Ccreated_at%2Cusername&cursor=Pz9Nakl5TXpveU5EVXlNelV5TnprMk1UYzFPVEF6TnpjNk9USXlNek0zTURVNE1UZzNOekUyT1RVM05WOUZ8MmU0YzRmZWYwYmZhM2JlZTRmZGM2MjM0NzViNWMzMTg5NDJjZmQ4YjljNGZhYjc1ZWIxN2QzMWQyZmY4ZmU2NA%3D%3D");
        followingsApiUrls.add("https://api.pinterest.com/v1/me/followers/?access_token=AXCeGz6mwYDUI1eKrMbJ4PFKErp9FLtlVR4iV_hD--hbPkA-ZQAAAAA&fields=first_name%2Cid%2Clast_name%2Curl%2Caccount_type%2Cbio%2Ccounts%2Cimage%2Ccreated_at%2Cusername&cursor=Pz9Nakl5TkRveU5UZzJNRFV6TkRFd01URXpOVEUwTVRRNk9USXlNek0zTURVMU16WXhPVFk0TVRVNU4xOUp8YWQxZjViZjlmNTQ2YTg2YzI3NGU0MmQ0Nzg5ODVjMmVmNTY2MDRlZDZjZDZhMzAzNzE5MTU5YjQ1NWVkZjc5NQ%3D%3D");

        String fbUrl = "https://api.pinterest.com/v1/me/likes/?access_token=AS94T9w2BZ8g5z1i47YGkp7c6U88FLtkVt0cCntD--hbPkA-ZQAAAAA&fields=id%2Clink%2Cnote%2Curl%2Cattribution%2Cboard%2Ccolor%2Ccounts%2Ccreated_at%2Coriginal_link%2Cmetadata%2Cimage%2Cmedia%2Ccreator";

        int maxCount = 0;
        for (int i = 0; i < followingsApiUrls.size(); i++) {
            try {
                int getFollowingsCount = processFollowings(followingsApiUrls.get(i));
                maxCount = maxCount + getFollowingsCount;
            } catch (ParseException ex) {
                Logger.getLogger(EnliventController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        Map returnMap = new HashMap<>();
        returnMap.put("followings_count",maxCount);
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
    
    @RequestMapping(value = "pinterest/totalBoards", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    List<Map<String, String>> getTotalBoards() {
        try {
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
        } catch (ParseException ex) {
            Logger.getLogger(EnliventController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @RequestMapping(value = "pinterest/totalPins", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    List<Map<String, String>> getTotalPins() throws ParseException {
        try {
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
        } catch (ParseException ex) {
            Logger.getLogger(EnliventController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
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

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void handle(HttpMessageNotReadableException e) {
        e.printStackTrace();
    }
}
