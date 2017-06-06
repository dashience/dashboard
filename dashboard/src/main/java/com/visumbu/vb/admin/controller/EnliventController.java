/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.admin.controller;

import com.visumbu.vb.admin.service.EnlivantService;
import static com.visumbu.vb.admin.service.EnlivantService.getTopBoards;
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
import com.visumbu.vb.utils.DateUtils;
import com.visumbu.vb.utils.JsonSimpleUtils;
import com.visumbu.vb.utils.Rest;
import com.visumbu.vb.utils.VbUtils;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TimeZone;
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
//import static test.Pinterest.getColumnDefObject;
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

    public String LINKEDIN_ACCESS_TOKEN = "AQVrr3w94F9NPdypSkVL_mY1hpRBlbg0DjsAymBxVnIvKw91gdapkEZt-hIUdzC34AZfgShbH17iWw0ef8VtT7gSKQsQ8mtPt2d9w_soy5FnKJaZgSHiT-Ug9MnzmB3fjlR2_tc6OoGmgeaMEuAHV3Yvnb-gzRg2TC4Aez2pUNR9jiv5WWM";

    @RequestMapping(value = "linkedIn", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    Map getLinkedIn(HttpServletRequest request, HttpServletResponse response) {
        try {
//            String accountId = request.getParameter("linkedinAccountId");
                String endDate=request.getParameter("endDate");//for linkedin we are using enddate
              Long timeStamp=DateUtils.dateToTimeStamp(endDate);
              System.out.println("timestamp-->");
            String accountId ="10671978";
            if (accountId != null) {
                String url = "https://api.linkedin.com/v1/companies/" + accountId + "/"
                        + "historical-status-update-statistics:(time,like-count,impression-count,click-count,engagement)?"
                        + "oauth2_access_token=" + LINKEDIN_ACCESS_TOKEN
                        + "&time-granularity=day&start-timestamp=1466899200000&format=json";
                MultiValueMap<String, String> valueMap = null;
                String data = Rest.getData(url, valueMap);
                JSONParser parser = new JSONParser();
                Object jsonObj = parser.parse(data);
                JSONObject json = (JSONObject) jsonObj;
                Map<String, Object> jsonToMap = JsonSimpleUtils.jsonToMap(json);
                System.out.println(jsonToMap.get("_total"));
                try {
                    List<Map<String, Object>> myData = (List<Map<String, Object>>) jsonToMap.get("values");

                    List<Map<String, Object>> linkedInData = new ArrayList<>();

                    for (Iterator<Map<String, Object>> iterator = myData.iterator(); iterator.hasNext();) {
                        Map<String, Object> twitterMapData = new HashMap<>();
                        Map<String, Object> mapData = iterator.next();
                        twitterMapData.put("clickCount", mapData.get("clickCount"));
                        twitterMapData.put("engagement", mapData.get("engagement"));
                        twitterMapData.put("likeCount", mapData.get("likeCount"));
                        System.out.println(mapData.get("time"));
                        Timestamp timestamp = new Timestamp((long) mapData.get("time"));
                        Date date = new Date(timestamp.getTime());
                        DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
                        TimeZone obj = TimeZone.getTimeZone("CST");
                        formatter.setTimeZone(obj);
                        System.out.println("***************** Time**********");
//                System.out.println(date);
                        twitterMapData.put("time", formatter.format(date));
                        twitterMapData.put("impressionCount", mapData.get("impressionCount"));

                        linkedInData.add(twitterMapData);

                    }

                    System.out.println(linkedInData);
                    List<ColumnDef> columnDefObject = getColumnDefObject(linkedInData);

//            List<ColumnDef> columnDefObject = getColumnDefObject(myData);
                    Map returnMap = new HashMap();
                    returnMap.put("columnDefs", columnDefObject);
                    returnMap.put("data", linkedInData);
                    return returnMap;
                } catch (NullPointerException e) {
                    Logger.getLogger(EnliventController.class.getName()).log(Level.SEVERE, null, e);
                    return null;
                }
            }
            else {
                return null;
            }
        } catch (ParseException ex) {
            Logger.getLogger(EnliventController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @RequestMapping(value = "twitter", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    Map getTwitter(HttpServletRequest request, HttpServletResponse response) {
        try {

            String url = "https://api.twitter.com/1.1/users/lookup.json?screen_name=Enlivant&user_id=2964932975&oauth_consumer_key=DC0sePOBbQ8bYdC8r4Smg&oauth_signature_method=HMAC-SHA1&oauth_timestamp=1494260023&oauth_nonce=-242141800&oauth_version=1.0&oauth_token=2964932975-RlNIi6QnoQtydUosFxNWUTuWgJlsJKCuGX4HmZS&oauth_signature=%2Bhm2aCgnsjQ6YW%2B5isNho9%2B6OvM%3D";

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
                twitterMapData.put("favourites_count", mapData.get("favourites_count"));

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

    private List<ColumnDef> getColumnDefs(List<Map<String, String>> data) {
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

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void handle(HttpMessageNotReadableException e) {
        e.printStackTrace();
    }
}
