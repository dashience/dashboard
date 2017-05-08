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
import com.visumbu.vb.utils.JsonSimpleUtils;
import com.visumbu.vb.utils.Rest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.util.MultiValueMap;
import static test.Linkedin.getColumnDefObject;

public class Twitter {

    public static void main(String args[]) throws ParseException {
        String url = "https://api.twitter.com/1.1/users/lookup.json?screen_name=Enlivant&user_id=2964932975&oauth_consumer_key=DC0sePOBbQ8bYdC8r4Smg&oauth_signature_method=HMAC-SHA1&oauth_timestamp=1493999004&oauth_nonce=458542271&oauth_version=1.0&oauth_token=2964932975-RlNIi6QnoQtydUosFxNWUTuWgJlsJKCuGX4HmZS&oauth_signature=bfeMg7mE0MiRLT5xvvcb9fYa7KA%3D";

        MultiValueMap<String, String> valueMap = null;
        String data = Rest.getData(url, valueMap);

        JSONParser parser = new JSONParser();
        JSONArray jsonArray = (JSONArray) parser.parse(data);

        List<Map<String, Object>> myData = (List<Map<String, Object>>) jsonArray;
        List<Map<String, Object>> twitterData = new ArrayList<>();

        for (Iterator<Map<String, Object>> iterator = myData.iterator(); iterator.hasNext();) {
            Map<String, Object> mapData = iterator.next();
            Map<String, Object> twitterMapData = new HashMap<>();
            twitterMapData.put("followers_count", mapData.get("followers_count"));
            twitterMapData.put("friends_count", mapData.get("friends_count"));
            twitterMapData.put("listed_count", mapData.get("listed_count"));
            twitterMapData.put("statuses_count", mapData.get("statuses_count"));

            twitterData.add(twitterMapData);

        }
        List<ColumnDef> columnDefObject = getColumnDefObject(twitterData);
        Map returnMap = new HashMap();
        returnMap.put("columnDef", columnDefObject);
        returnMap.put("data", twitterData);

    }

    public static List<ColumnDef> getColumnDefObject(List<Map<String, Object>> data) {
        List<ColumnDef> columnDefs = new ArrayList<>();
        for (Iterator<Map<String, Object>> iterator = data.iterator(); iterator.hasNext();) {
            Map<String, Object> mapData = iterator.next();
            for (Map.Entry<String, Object> entrySet : mapData.entrySet()) {
                String key = entrySet.getKey();

                Object value = entrySet.getValue();
//                System.out.println(value.getClass());
                columnDefs.add(new ColumnDef(key, "string", key));

            }
            return columnDefs;
        }
        return columnDefs;
    }
}
