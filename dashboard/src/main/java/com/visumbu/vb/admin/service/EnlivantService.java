/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.admin.service;

import com.visumbu.vb.bean.ColumnDef;
import com.visumbu.vb.utils.JsonSimpleUtils;
import com.visumbu.vb.utils.Rest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import static test.Pinterest.getColumnDefObject;

/**
 *
 * @author deeta
 */
@Service("enlivantService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class EnlivantService {

    public List<Map<String, String>> getReport(String reportName) throws ParseException {
        
        if(reportName.equalsIgnoreCase("getTopBoards"))
        {
            return getTopBoards();
        }
        return null;
        
    
    }
    
    
     public static List<Map<String, String>> getTopBoards() throws ParseException {

        String fbUrl = "https://api.pinterest.com/v1/me/boards/?access_token=AZ3tcCqL10kF4AhAKjY4YHzUBwZJFLtfDUst59xD--hbPkA-ZQAAAAA&fields=id%2Cname%2Curl%2Ccounts%2Ccreated_at%2Ccreator%2Cdescription%2Creason";
        String data = Rest.getData(fbUrl);
        JSONParser parser = new JSONParser();
        Object jsonObj = parser.parse(data);
        JSONObject json = (JSONObject) jsonObj;
        Map<String, Object> jsonToMap = JsonSimpleUtils.jsonToMap(json);
        Map returnMap = new HashMap<>();
        List<Map<String, Object>> fbData = (List<Map<String, Object>>) jsonToMap.get("data");
        List<ColumnDef> columnDefObject = getColumnDefObject(fbData);
        returnMap.put("columnDef", columnDefObject);
        returnMap.put("data", fbData);
        List<Map<String, String>> listData = new ArrayList<>();
        listData.add(returnMap);
        System.out.println("**************Serviece layer *********************");
        System.out.println(listData);
        return listData;
    }
   
}
