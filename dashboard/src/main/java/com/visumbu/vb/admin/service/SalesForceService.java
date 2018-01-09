/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.admin.service;

/**
 *
 * @author dashience
 */
import com.visumbu.vb.utils.ApiUtils;
import com.visumbu.vb.utils.DateUtils;
import static com.visumbu.vb.utils.DateUtils.dateToTimeStamp;
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
 * @author deldot
 */
@Service("salesForceService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class SalesForceService {

    public List<Map<String, Object>> get(String reportName) {
        if (reportName.equalsIgnoreCase("overallPerformance")) {
            return getOverallPerformaceData();
        }
        if (reportName.equalsIgnoreCase("cityPerformance")) {
            return getCityPerformanceData();
        }

//        if (reportName.equalsIgnoreCase("")) {
//            return getLeadData();
//        }

        return null;
    }

//    public List<Map<String, Object>> getLeadData() {
//        try {
//            String url = "http://111.93.224.129:5000/city";
//            String data = Rest.getData(url);
//            JSONParser parser = new JSONParser();
//            Object jsonObj = parser.parse(data);
//            JSONObject json = (JSONObject) jsonObj;
//            List<Map<String, Object>> returnData = (List<Map<String, Object>>) json.get("data");
//            return returnData;
//        } catch (ParseException ex) {
//            Logger.getLogger(SalesForceService.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return null;
//    }

    public List<Map<String, Object>> getCityPerformanceData() {
        try {
            String url = "http://111.93.224.129:5000/city";
            String data = Rest.getData(url);
            JSONParser parser = new JSONParser();
            Object jsonObj = parser.parse(data);
            JSONObject json = (JSONObject) jsonObj;
            List<Map<String, Object>> returnData = (List<Map<String, Object>>) json.get("data");
            return returnData;
        } catch (ParseException ex) {
            Logger.getLogger(SalesForceService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public List<Map<String, Object>> getOverallPerformaceData() {
        try {
            String url = "http://111.93.224.129:5000/contacts";
            String salesForceData = Rest.getData(url);
            System.out.println("data -->" + salesForceData);
            JSONParser parser = new JSONParser();
            Object jsonObj = parser.parse(salesForceData);
            JSONObject json = (JSONObject) jsonObj;
            List<Map<String, Object>> returnData = (List<Map<String, Object>>) json.get("data");
            return returnData;
        } catch (ParseException ex) {
            Logger.getLogger(SalesForceService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
