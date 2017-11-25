/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.admin.service;

import com.visumbu.vb.utils.Rest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.collections.map.MultiValueMap;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;

/**
 *
 * @author dachience-dev-07
 */
@Service("googleSpreadSheetService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class GoogleSpreadSheetService {

    public static final String SHEET_URL = "https://sheets.googleapis.com/v4/spreadsheets/";
    public static final String spreadSheetId = "1G6psxsWtZ7ItOaDySqVqRRcumo8ZEugBEWZhXawfm-Q";

    public List<Map<String, Object>> get(String dataSetReportName, Date startDate, Date endDate, String aggregation, String productSegment,
            String gSpreadSheetApiKey, String sheetName) {
        if (dataSetReportName.equalsIgnoreCase("sheetData")) {
            return getSheetData(gSpreadSheetApiKey, sheetName);
        }
        if (dataSetReportName.equalsIgnoreCase("sheets")) {
            return getSheets(gSpreadSheetApiKey);
        }
        return null;
    }

    public List<Map<String, Object>> getSheetData(String gSpreadSheetApiKey, String sheetName) {
        String url = SHEET_URL + spreadSheetId + "/values/" + sheetName;

//        org.springframework.util.MultiValueMap<String, String> valueMap = new LinkedMultiValueMap<>();
//        valueMap.add("key", Arrays.asList(gSpreadSheetApiKey));
        String spreadSheetData = Rest.getData(url);
        JSONParser parser = new JSONParser();
        List<Map<String, Object>> returnMap = new ArrayList<>();
        try {
            Object jsonObj = parser.parse(spreadSheetData);
            JSONObject object = (JSONObject) jsonObj;
            List values = (List) object.get("values");
            returnMap = parseSheetData(values);
        } catch (ParseException ex) {
            Logger.getLogger(GoogleSpreadSheetService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return returnMap;
    }

    public static List<Map<String, Object>> getSheets(String gSpreadSheetApiKey) {

        String url = SHEET_URL + spreadSheetId + gSpreadSheetApiKey;
        String sheetData = Rest.getData(url);
        JSONParser parser = new JSONParser();
        List<Map<String, Object>> returnMap = new ArrayList<>();
        try {
            Object jsonObj = parser.parse(sheetData);
            JSONObject object = (JSONObject) jsonObj;

            List<Map<String, Object>> data = (List) object.get("sheets");
            for (Iterator<Map<String, Object>> iterator = data.iterator(); iterator.hasNext();) {
                Map<String, Object> next = iterator.next();
                Map mapData = new HashMap();
                mapData.put("title", ((Map) next.get("properties")).get("title"));
                returnMap.add(mapData);
            }
        } catch (ParseException ex) {
            Logger.getLogger(GoogleSpreadSheetService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return returnMap;
    }

    public List<Map<String, Object>> parseSheetData(List values) {
        List<Map<String, Object>> returnMap = new ArrayList<>();
        int len = values.size();
        List colsHeader = (List) values.get(0);
        List colsData = (List) values.subList(1, len);
        for (int i = 0; i < colsData.size(); i++) {
            Map<String, Object> dataMap = new HashMap<>();
            List temp = (List) colsData.get(i);
            for (int j = 0; j < colsHeader.size(); j++) {
                if (j < temp.size()) {
                    dataMap.put((String) colsHeader.get(j), temp.get(j));
                } else {
                    dataMap.put((String) colsHeader.get(j), "");
                }
            }
            returnMap.add(dataMap);
        }
        return null;
    }

}
