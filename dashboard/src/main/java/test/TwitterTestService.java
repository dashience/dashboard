/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

/**
 *
 * @author deldot
 */
import com.google.common.collect.HashBiMap;
import com.visumbu.vb.utils.Rest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class TwitterTestService {

    public static void main(String[] args) {
        List<Map<String, Object>> twitterPagePerformanceReport = getPagePerformance();
    }

    public static List<Map<String, Object>> getPagePerformance() {

        try {
            String twitterUrl = "https://api.twitter.com/1.1/users/lookup.json?"
                    + "screen_name=enlivant"
                    + "&user_id=2964932975"
                    + "&oauth_consumer_key=DC0sePOBbQ8bYdC8r4Smg"
                    + "&oauth_signature_method=HMAC-SHA1"
                    + "&oauth_timestamp=1496925396"
                    + "&oauth_nonce=2953828394"
                    + "&oauth_version=1.0"
                    + "&oauth_token=2964932975-RlNIi6QnoQtydUosFxNWUTuWgJlsJKCuGX4HmZS"
                    + "&oauth_signature=wYXbwgOTypLOUM%2BeRLt0DrcHABk%3D";

            String twitterData = Rest.getData(twitterUrl);
            JSONParser parser = new JSONParser();
            Object object = new Object();
            JSONArray jsonArray = new JSONArray();
            object = parser.parse(twitterData);
            jsonArray = (JSONArray) object;

            if (twitterUrl == null) {
                return null;
            }
            Map<String, Object> twitterObject = new HashMap<>();
            twitterObject.put("data", jsonArray);

            List<Map<String, Object>> obj = (List<Map<String, Object>>) twitterObject.get("data");
            List<Map<String, Object>> returnMap = new ArrayList();
            for (Iterator<Map<String, Object>> iterator = obj.iterator(); iterator.hasNext();) {
                Map<String, Object> next = iterator.next();
                Map twitterOrganicData = new HashMap();
                System.out.println("screen name-->" + next.get("screen_name"));
                twitterOrganicData.put("screen_name", next.get("screen_name"));
                twitterOrganicData.put("followers_count", next.get("followers_count"));
                twitterOrganicData.put("friends_count", next.get("friends_count"));
                twitterOrganicData.put("statuses_count", next.get("statuses_count"));
                twitterOrganicData.put("retweet_count", next.get("retweet_count"));
                twitterOrganicData.put("favorite_count", next.get("favorite_count"));
                returnMap.add(twitterOrganicData);
            };
            return returnMap;
        } catch (Exception ex) {
            Logger.getLogger(TwitterTestService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

}
