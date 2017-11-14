/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.admin.service;

import java.util.List;
import java.util.Map;
import static test.GoogleSpreadsheet.key;

/**
 *
 * @author dachience-dev-07
 */
public class GoogleSpreadSheetService {
    public static final String SHEET_URL = "https://sheets.googleapis.com/v4/spreadsheets/";
    public List<Map<String, Object>> get(String gSpreadSheetApiKey) {
        activityMetrics(gSpreadSheetApiKey);
        return null;
    }
    
    public List<Map<String, Object>> activityMetrics(String gSpreadSheetApiKey){
        String spreadSheetId = "1G6psxsWtZ7ItOaDySqVqRRcumo8ZEugBEWZhXawfm-Q";
        String range = "A1:Z999";
        String url = SHEET_URL + spreadSheetId + "/values/" + range + "?key=" + key;
        return null;
    }
}
