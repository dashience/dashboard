/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import com.visumbu.vb.utils.Rest;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author sabari
 */
public class GoogleSpreadsheet {

    public static final String key = "AIzaSyDhyM1DBSeEO2KkcjiQnz0UBYAra3GSkmw";
    public static final String SHEET_URL = "https://sheets.googleapis.com/v4/spreadsheets/";

    public static List<Map<String, Object>> getSpreadSheetById(String spreadSheetId, String range) throws ParseException {

        String url = SHEET_URL + spreadSheetId + "/values/" + range + "?key=" + key;
        String spreadSheetData = Rest.getData(url);
        JSONParser parser = new JSONParser();
        Object jsonObj = parser.parse(spreadSheetData);
        JSONObject object = (JSONObject) jsonObj;

        List values = (List) object.get("values");
        int len = values.size();
        List colsHeader = (List) values.get(0);
        List colsData = (List) values.subList(1, len);

        Map<String, Object> dataMap = new HashMap<>();
        for (int i = 0; i < colsData.size(); i++) {
            List temp = (List) colsData.get(i);
            for (int j = 0; j < colsHeader.size(); j++) {
                dataMap.put((String) colsHeader.get(j), temp.get(j));
            }
        }
        System.out.println(dataMap);
        return null;
    }

    public static void main(String[] args) {

        try {
            String spreadSheetId = "1G6psxsWtZ7ItOaDySqVqRRcumo8ZEugBEWZhXawfm-Q";
            String range = "A1:Z999";
            List<Map<String, Object>> spreadSheetData = getSpreadSheetById(spreadSheetId, range);
        } catch (ParseException ex) {
            Logger.getLogger(GoogleSpreadsheet.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}