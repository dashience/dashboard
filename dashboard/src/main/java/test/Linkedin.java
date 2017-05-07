/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

/**
 *
 * @author deeta
 */
import com.visumbu.vb.bean.ColumnDef;
import com.visumbu.vb.model.DefaultFieldProperties;
import com.visumbu.vb.utils.JsonSimpleUtils;
import com.visumbu.vb.utils.JsonUtils;
import com.visumbu.vb.utils.Rest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.util.MultiValueMap;

public class Linkedin {

    public static void main(String args[]) throws ParseException {

        String url = "https://api.linkedin.com/v1/companies/10671978/historical-status-update-statistics:(time,like-count,impression-count,click-count,engagement)?oauth2_access_token=AQVrr3w94F9NPdypSkVL_mY1hpRBlbg0DjsAymBxVnIvKw91gdapkEZt-hIUdzC34AZfgShbH17iWw0ef8VtT7gSKQsQ8mtPt2d9w_soy5FnKJaZgSHiT-Ug9MnzmB3fjlR2_tc6OoGmgeaMEuAHV3Yvnb-gzRg2TC4Aez2pUNR9jiv5WWM&time-granularity=day&start-timestamp=1466899200000&format=json";
        MultiValueMap<String, String> valueMap = null;
        String data = Rest.getData(url, valueMap);
        JSONParser parser = new JSONParser();
        Object jsonObj = parser.parse(data);
        JSONObject json = (JSONObject) jsonObj;
        Map<String, Object> jsonToMap = JsonSimpleUtils.jsonToMap(json);
        System.out.println(jsonToMap.get("_total"));
        List<Map<String, Object>> myData = (List<Map<String, Object>>) jsonToMap.get("values");

        List<ColumnDef> columnDefObject = getColumnDefObject(myData);
        Map returnMap = new HashMap();
        returnMap.put("columnDef", columnDefObject);
        returnMap.put("data", myData);
        System.out.println(returnMap);
        // System.out.println(data);

    }

    public static List<ColumnDef> getColumnDefObject(List<Map<String, Object>> data) {
        List<ColumnDef> columnDefs = new ArrayList<>();
        for (Iterator<Map<String, Object>> iterator = data.iterator(); iterator.hasNext();) {
            Map<String, Object> mapData = iterator.next();
            for (Map.Entry<String, Object> entrySet : mapData.entrySet()) {
                String key = entrySet.getKey();

                Object value = entrySet.getValue();
                System.out.println(value.getClass());
                columnDefs.add(new ColumnDef(key, "string", key));

            }
            return columnDefs;
        }
        return columnDefs;
    }

}
