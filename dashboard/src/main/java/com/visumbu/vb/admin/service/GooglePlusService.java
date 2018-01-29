/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.admin.service;

/**
 *
 * @author sabari
 */
import com.visumbu.vb.admin.dao.SettingsDao;
import com.visumbu.vb.utils.JsonSimpleUtils;
//import com.visumbu.vb.utils.ExampleConfig;
import com.visumbu.vb.utils.Rest;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
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
import org.springframework.util.LinkedMultiValueMap;

import org.springframework.util.MultiValueMap;

@Service("googlePlusService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)

public class GooglePlusService {

    
    public final String BASE_URL = "https://www.googleapis.com/plus/v1/people/";
    
    public List<Map<String, Object>> get(String gPlusAccountId, String pPlusApiKey, String reportName) {
        if (reportName.equalsIgnoreCase("activityPerformance")) {
            return activityMetrics(gPlusAccountId, pPlusApiKey);
        }
        return null;
    }

    public List<Map<String, Object>> activityMetrics(String gPlusAccountId, String gPlusApiKey) {
        try {
            long replies = 0, plusoners = 0, resharers = 0;
            
            String url=BASE_URL+gPlusAccountId+"/activities/public";

            MultiValueMap<String, String> valueMap = new LinkedMultiValueMap<>();
            valueMap.put("key", Arrays.asList(gPlusApiKey));
            String data = Rest.getData(url, valueMap);
            JSONParser parser = new JSONParser();
            Object object = parser.parse(data);
            JSONObject jsonObject = (JSONObject) object;
            JSONArray dataArr = (JSONArray) jsonObject.get("items");

            //get the total number of activity
            int totalActivity = jsonObject.size();
            List<Map<String, Object>> returnMap = new ArrayList<>();

            for (Iterator iterator = dataArr.iterator(); iterator.hasNext();) {
                JSONObject objects = (JSONObject) iterator.next();
                Map<String, Object> objectData = (Map<String, Object>) objects.get("object");
                long tempReplies = (long) ((Map) objectData.get("replies")).get("totalItems");
                long tempPlusoners = (long) ((Map) objectData.get("plusoners")).get("totalItems");
                long tempResharers = (long) ((Map) objectData.get("resharers")).get("totalItems");

                replies = replies + tempReplies;
                plusoners = tempPlusoners + tempReplies;
                resharers = tempResharers + tempReplies;
            }
            long totalEngagements = replies + plusoners + resharers;
            Map<String, Object> activityMetrics = new HashMap<>();
            activityMetrics.put("activityCount", totalActivity);
            activityMetrics.put("replies", replies);
            activityMetrics.put("plusOners", plusoners);
            activityMetrics.put("resharers", resharers);
            activityMetrics.put("total_engagements", totalEngagements);
            returnMap.add(activityMetrics);
            return returnMap;
        } catch (ParseException ex) {
            Logger.getLogger(GooglePlusService.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

}
