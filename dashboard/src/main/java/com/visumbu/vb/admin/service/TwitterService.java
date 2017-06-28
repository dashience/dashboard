/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.admin.service;

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

/**
 *
 * @author deldot
 */
@Service("twitterService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class TwitterService {

    public List<Map<String, Object>> get(String reportName, String twitterAccountId, String twitterScreenName, 
            String twitterOauthToken, String twitterOauthSignature, String twitterOauthNonce,String twitterOauthConsumerKey,
            Date startDate, Date endDate, String timeSegment, String productSegment) {

        if (reportName.equalsIgnoreCase("pagePerformance")) {
            return getPagePerformanceReport(twitterAccountId, twitterScreenName, twitterOauthToken, twitterOauthSignature,
                    twitterOauthNonce, twitterOauthConsumerKey,startDate, endDate, timeSegment, productSegment);
        }
        return null;
    }

    private List<Map<String, Object>> getPagePerformanceReport(String twitterAccountId, String twitterScreenName,
            String twitterOauthToken, String twitterOauthSignature, String twitterOauthNonce,String twitterOauthConsumerKey,
            Date startDate,Date endDate, String timeSegment, String productSegment) {
        try {
            String startDateStr = DateUtils.dateToString(startDate, "YYYY-MM-dd");
            String endDateStr = DateUtils.dateToString(endDate, "YYYY-MM-dd");
            
            Long timeStamp=dateToTimeStamp(endDateStr);//to be added in feature 
            
// https://api.twitter.com/1.1/users/lookup.json?screen_name=enlivant&user_id=2964932975&oauth_consumer_key=DC0sePOBbQ8bYdC8r4Smg&oauth_signature_method=HMAC-SHA1&oauth_timestamp=1496925396&oauth_nonce=2953828394&oauth_version=1.0&oauth_token=2964932975-RlNIi6QnoQtydUosFxNWUTuWgJlsJKCuGX4HmZS&oauth_signature=wYXbwgOTypLOUM%2BeRLt0DrcHABk%3D
//            String twitterUrl = "https://api.twitter.com/1.1/users/lookup.json?"
//                    + "screen_name=enlivant"
//                    + "&user_id=2964932975"
//                    + "&oauth_consumer_key=DC0sePOBbQ8bYdC8r4Smg"
//                    + "&oauth_signature_method=HMAC-SHA1"
//                    + "&oauth_timestamp=1496925396"
//                    + "&oauth_nonce=2953828394"
//                    + "&oauth_version=1.0"
//                    + "&oauth_token=2964932975-RlNIi6QnoQtydUosFxNWUTuWgJlsJKCuGX4HmZS"
//                    + "&oauth_signature=wYXbwgOTypLOUM%2BeRLt0DrcHABk%3D";
            String twitterUrl = "https://api.twitter.com/1.1/users/lookup.json?"
                    + "screen_name="+twitterScreenName
                    + "&user_id="+twitterAccountId
                    + "&oauth_consumer_key="+twitterOauthConsumerKey
                    + "&oauth_signature_method=HMAC-SHA1"
                    + "&oauth_timestamp=1496925396"
                    + "&oauth_nonce="+twitterOauthNonce
                    + "&oauth_version=1.0"
                    + "&oauth_token="+twitterOauthToken
                    + "&oauth_signature="+twitterOauthSignature;

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
                twitterOrganicData.put("favourites_count", next.get("favourites_count"));
                twitterOrganicData.put("retweet_count", next.get("retweet_count"));
                twitterOrganicData.put("favorite_count", next.get("favorite_count"));
                returnMap.add(twitterOrganicData);
            };
            System.out.println("*************************** TWITTER SERVICE*******************");
            System.out.println(returnMap);
            return returnMap;
        } catch (ParseException ex) {
            Logger.getLogger(TwitterService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

}
