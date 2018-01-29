/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

/**
 *
 * @author sabari
 */
import com.visumbu.vb.utils.ApiUtils;
import com.visumbu.vb.utils.DateUtils;
import static com.visumbu.vb.utils.DateUtils.dateToTimeStamp;
import com.visumbu.vb.utils.JsonSimpleUtils;
//import com.visumbu.vb.utils.ExampleConfig;
import com.visumbu.vb.utils.Rest;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.util.MultiValueMap;

public class GooglePlus {

    public static void main(String[] args) throws ParseException {
        List<Map<String, Object>> activityList = getActivityMetrics();
    }

    private static List<Map<String, Object>> getActivityMetrics() throws ParseException {

        long replies = 0,plusoners = 0,resharers = 0;
        String url = "https://www.googleapis.com/plus/v1/people/104869273610744594745/activities/public?key=AIzaSyCgsecbxxUDqC-J8Zq028EFSyhuEkFNX70";

        MultiValueMap<String, String> valueMap = null;
        String data = Rest.getData(url, valueMap);

        JSONParser parser = new JSONParser();
        Object object = parser.parse(data);

        JSONObject jsonObject = (JSONObject) object;

        JSONArray dataArr = (JSONArray) jsonObject.get("items");

        //get the total number of activity
        int totalAcitivity = jsonObject.size();

        System.out.println("Activity Count -->" + totalAcitivity);

        System.out.println("Starts below");

        List<Map<String, Object>> returnMap = new ArrayList<>();

        for (Iterator iterator = dataArr.iterator(); iterator.hasNext();) {
            Object next = iterator.next();
            JSONObject objects = (JSONObject) next;
            Map<String, Object> objectData = (Map<String, Object>) objects.get("object");

            System.out.println("type -->" + ((Map) objectData.get("replies")).get("totalItems").getClass().getName());

            long tempReplies = (long) ((Map) objectData.get("replies")).get("totalItems");
            long tempPlusoners = (long) ((Map) objectData.get("plusoners")).get("totalItems");
            long tempResharers = (long) ((Map) objectData.get("resharers")).get("totalItems");

            replies = replies + tempReplies;
            plusoners = tempPlusoners + tempReplies;
            resharers = tempResharers + tempReplies;

            System.out.println(((Map) objectData.get("replies")).get("totalItems"));

        }
        long totalEngagements = replies + plusoners + resharers;

        System.out.println("Total Replies -->" + replies);
        System.out.println("Total plusoners -->" + plusoners);
        System.out.println("Total resharers -->" + resharers);
        System.out.println("Total Engagements -->" + totalEngagements);

        Map<String, Object> activityMetrics = new HashMap<>();

        activityMetrics.put("activity_count", totalAcitivity);
        activityMetrics.put("replies", replies);
        activityMetrics.put("plusoners", plusoners);
        activityMetrics.put("resharers", resharers);
        activityMetrics.put("total_engagements", totalEngagements);

        returnMap.add(activityMetrics);

        System.out.println("final data");
        System.out.println(returnMap);

        return null;
    }
}
