/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.admin.service;

import com.visumbu.vb.admin.controller.EnliventController;
import static com.visumbu.vb.admin.controller.EnliventController.getColumnDefObject;
import com.visumbu.vb.admin.dao.SettingsDao;
import com.visumbu.vb.bean.ColumnDef;
import com.visumbu.vb.model.Settings;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

import com.visumbu.vb.utils.ApiUtils;
import com.visumbu.vb.utils.DateUtils;
import com.visumbu.vb.utils.JsonSimpleUtils;
//import com.visumbu.vb.utils.ExampleConfig;
import com.visumbu.vb.utils.Rest;
import com.visumbu.vb.utils.SettingsProperty;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
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
import org.springframework.util.MultiValueMap;

/**
 *
 * @author deldot
 */
@Service("linkedinService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class LinkedinService {

    @Autowired
    private SettingsDao settingsDao;

    public List<Map<String, Object>> get(Long accountId,String linkedinAccessToken, String dataSetReportName, Date startDate, Date endDate,
            String aggregation, String productSegment) {

        //get thea access token from settings
//        List<Settings> linkedinAccessToken = settingsDao.getProperty("linkedinAccessToken");
//        String accessToken = SettingsProperty.getSettingsProperty(linkedinAccessToken, "linkedinAccessToken");

        System.out.println("Linkedin AccountId-->" + accountId);
        System.out.println("Linkedin Report Name--->" + dataSetReportName);
        if (dataSetReportName.equalsIgnoreCase("organic")) {
            System.out.println("inside linkediin orgainc");
            return getOrganicData(linkedinAccessToken, accountId, startDate, endDate, aggregation, productSegment);
        }

        return null;
    }

    private List<Map<String, Object>> getOrganicData(String accessToken, Long accountId, Date startDate, Date endDate,
            String aggregation, String productSegment) {
        try {

            String startDateStr = DateUtils.dateToString(startDate, "YYYY-MM-dd");
            String endDateStr = DateUtils.dateToString(endDate, "YYYY-MM-dd");

            Long timeStamp = DateUtils.dateToTimeStamp(endDateStr);

            if (accountId != null) {
                String url = "https://api.linkedin.com/v1/companies/" + accountId + "/"
                        + "historical-status-update-statistics:(time,like-count,impression-count,click-count,engagement)?"
                        + "oauth2_access_token=" + accessToken
                        + "&time-granularity=day&start-timestamp=" + timeStamp + "&format=json";
                MultiValueMap<String, String> valueMap = null;
                String data = Rest.getData(url, valueMap);
                JSONParser parser = new JSONParser();
                Object jsonObj = parser.parse(data);
                JSONObject json = (JSONObject) jsonObj;
                Map<String, Object> jsonToMap = JsonSimpleUtils.jsonToMap(json);
                System.out.println(jsonToMap.get("_total"));
                List<Map<String, Object>> linkedInData = new ArrayList<>();
                try {
                    List<Map<String, Object>> myData = (List<Map<String, Object>>) jsonToMap.get("values");

                    List<Map<String, String>> dataValueList = new ArrayList();
                    for (Iterator<Map<String, Object>> iterator = myData.iterator(); iterator.hasNext();) {
                        Map<String, Object> twitterMapData = new HashMap<>();
                        Map<String, Object> mapData = iterator.next();
                        twitterMapData.put("clickCount", mapData.get("clickCount") + "");
                        twitterMapData.put("engagement", mapData.get("engagement") + "");
                        twitterMapData.put("likeCount", mapData.get("likeCount") + "");
                        System.out.println(mapData.get("time"));
                        Timestamp timestamp = new Timestamp((long) mapData.get("time"));
                        Date date = new Date(timestamp.getTime());
                        DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
                        TimeZone obj = TimeZone.getTimeZone("CST");
                        formatter.setTimeZone(obj);
                        System.out.println("***************** Time**********");
//                System.out.println(date);
                        twitterMapData.put("time", formatter.format(date));
                        twitterMapData.put("impressionCount", mapData.get("impressionCount") + "");

                        linkedInData.add(twitterMapData);

                    }

//                    System.out.println(linkedInData);
//                    List<ColumnDef> columnDefObject = getColumnDefObject(linkedInData);
//
////            List<ColumnDef> columnDefObject = getColumnDefObject(myData);
//                    Map returnMap = new HashMap();
//                    returnMap.put("columnDefs", columnDefObject);
//                    returnMap.put("data", linkedInData);
//                    dataValueList.add(returnMap);
                    return linkedInData;
                } catch (NullPointerException e) {
                    Logger.getLogger(EnliventController.class.getName()).log(Level.SEVERE, null, e);
                    return null;
                }
            } else {
                return null;
            }
        } catch (Exception ex) {
            return null;
        }
    }

}