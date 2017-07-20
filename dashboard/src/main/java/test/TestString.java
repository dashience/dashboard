/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import com.google.common.base.CaseFormat;
import com.visumbu.vb.admin.controller.ProxyController;
import com.visumbu.vb.utils.JsonSimpleUtils;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author user
 */
public class TestString {
    public static void main(String argv[]) {
        System.out.println(CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, "THIS_IS_AN_EXAMPLE_STRING"));
        String jsonString = "{\"country\":[\"india\", \"us\"], \"state\":[\"tn\",\"vs\"]}";
        System.out.println(getQueryFilter(jsonString));
    }
    
        private static String getQueryFilter(String jsonDynamicFilter) {
        try {
            JSONParser parser = new JSONParser();
            Object jsonObj = parser.parse(jsonDynamicFilter);
            JSONObject json = (JSONObject) jsonObj;
            Map<String, Object> jsonToMap = (Map<String, Object>)JsonSimpleUtils.jsonToMap(json);
            List<String> queryString = new ArrayList<>();
            for (Map.Entry<String, Object> entry : jsonToMap.entrySet()) {
                String key = entry.getKey();
                List<String> value = (List<String>) entry.getValue();
                List<String> innerQuery = new ArrayList<>();
                for (Iterator<String> iterator = value.iterator(); iterator.hasNext();) {
                    String valueString = iterator.next();
                    innerQuery.add(key + " = " + "'" + valueString + "'");
                }
                String join = String.join(" OR ", innerQuery);
                // String output = key + " in " + join;
                queryString.add(" ( " + join + " ) ");
            }
            return String.join(" AND ", queryString);
            
        } catch (ParseException ex) {
            java.util.logging.Logger.getLogger(ProxyController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

}
