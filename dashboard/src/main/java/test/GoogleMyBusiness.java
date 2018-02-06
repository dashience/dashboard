/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author dashience
 */
public class GoogleMyBusiness {
    private static final String ACCESS_TOKEN ="ya29.GlxYBYwhO2CQ8ZjkeQlYgijt7KuJI6kBzvTUynoLppR3jPj_o83ttbIQ8RxZTohs8c8gwY8aoYeXVQX9u0GCZK_U5pF1TwhEnGQw5ajamN_teRfWAH2JXujW_-Vb3Q";
    private static HashMap<String, String> locationListArray;
    public static void main(String[] args) throws ParseException{
        getInsights();
    }
    public static void getInsights() throws ParseException {
        
        String url ="https://mybusiness.googleapis.com/v4/accounts/100108146938550060624/locations:reportInsights?access_token="+ACCESS_TOKEN;
        JSONParser parser = new JSONParser();
        Object jsonObject=parser.parse(url);
        
        
    }
    
    public static String getAppendedResponse(String requestBody, String url) {
        List<String> locations = new ArrayList<>();
        StringBuilder responseString = new StringBuilder();
        for (Map.Entry<String, String> entry : locationListArray.entrySet()) {
            String key = entry.getKey();
            locations.add(key);
        }
        int listSize = locations.size();
        for (int i = 0; i < listSize;) {
            StringBuilder locationString = new StringBuilder();
            if (listSize < 10) {
                locationString.append(locations.toString());
            } else {
                List<String> limitedLocations = new ArrayList<>();
                limitedLocations = locations.subList(i, i + 9);
                locationString.append(limitedLocations.toString());
            }
            String payload = "{\"locationNames\":" + locationString.toString() + requestBody;
            System.out.println("payload----------->" + payload);
//            responseString.append(getResponse(url, null, payload));
            i = i + 9;
        }
        System.out.println("responseString-------->" + responseString.toString());
        return responseString.toString();
    }
}
