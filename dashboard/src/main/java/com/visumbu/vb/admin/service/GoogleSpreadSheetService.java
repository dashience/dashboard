/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.admin.service;

import com.visumbu.vb.utils.Rest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import test.GoogleSpreadsheet;
import static test.GoogleSpreadsheet.key;

/**
 *
 * @author dachience-dev-07
 */
public class GoogleSpreadSheetService {

    public static final String SHEET_URL = "https://sheets.googleapis.com/v4/spreadsheets/";

    public List<Map<String, Object>> get(String gSpreadSheetApiKey, String sheetName) {
        return getSheetData(gSpreadSheetApiKey, sheetName);
    }

    public List<Map<String, Object>> getSheetData(String gSpreadSheetApiKey, String sheetName) {
        String spreadSheetId = "1G6psxsWtZ7ItOaDySqVqRRcumo8ZEugBEWZhXawfm-Q";
//        String range = "A1:Z999";
        String url = SHEET_URL + spreadSheetId + "/values/" + sheetName + "?key=" + key;
        String spreadSheetData = Rest.getData(url);
        JSONParser parser = new JSONParser();
        List<Map<String, Object>> returnMap = new ArrayList<>();
        try {
            Object jsonObj = parser.parse(spreadSheetData);
            JSONObject object = (JSONObject) jsonObj;
            List values = (List) object.get("values");
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
        } catch (ParseException ex) {
            Logger.getLogger(GoogleSpreadsheet.class.getName()).log(Level.SEVERE, null, ex);
        }
        return returnMap;
    }
}
