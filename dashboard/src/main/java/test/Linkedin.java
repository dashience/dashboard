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
import com.visumbu.vb.utils.Rest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import oauth.signpost.OAuth;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.OAuthProvider;
import oauth.signpost.basic.DefaultOAuthConsumer;
import oauth.signpost.basic.DefaultOAuthProvider;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.exception.OAuthNotAuthorizedException;
import org.apache.http.HttpException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import com.visumbu.vb.bean.LinkedInPostType;
import com.visumbu.vb.utils.JsonSimpleUtils;
import static com.visumbu.vb.utils.JsonSimpleUtils.jsonToMap;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.simple.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.LinkedMultiValueMap;

public class Linkedin {

    @Autowired
    private LinkedInPostType linkedInPostType;

    public static void main(String args[]) throws ParseException {

        String oauthAccesToken = "AQUSHmqcZjHHCok6jvxjvOsHDsJpIdnFMBaKCoR3i6MDEcGKO20IgcCPH4Ou5LdklLXBciAnlCSQflNJaS1DKpCuEHbGdgWt-Zr4BPiYVFvwPcNlzXx3DnYvkCMTj09uyheaMx-BkHyKdCCan04TdR5lDITfLywVNRTTIivWx7RpcKR7IiA";

        String updateKey = "UPDATE-c10671978-6297728614117007360";

        long companyId = 10671978;

        String eventType = "status-update";//job-posting or new-product or status-update

        String segment = "seniority";

        String granularity = "day";
        // old url:  String url = "https://api.linkedin.com/v1/companies/10671978/historical-status-update-statistics:(time,like-count,impression-count,click-count,engagement)?oauth2_access_token=AQVrr3w94F9NPdypSkVL_mY1hpRBlbg0DjsAymBxVnIvKw91gdapkEZt-hIUdzC34AZfgShbH17iWw0ef8VtT7gSKQsQ8mtPt2d9w_soy5FnKJaZgSHiT-Ug9MnzmB3fjlR2_tc6OoGmgeaMEuAHV3Yvnb-gzRg2TC4Aez2pUNR9jiv5WWM&time-granularity=day&start-timestamp=1466899200000&format=json";
//        String url = "https://api.linkedin.com/v1/companies/10671978/"
//                + "historical-status-update-statistics:(time,like-count,impression-count,click-count,engagement)?"
//                + "oauth2_access_token=AQWGEEChONDdAPohJfly-0yx2Zhxe0dZHFhypnvojVGY7FNekxjpT3YHy8EiMLBSl3CQwZX5Z4C0FW57lH2hnKpK1JRfbEhKjGTXVAochKVH4LcfZnux4eEU8BqpTDAkztoGpbMVHZHxRtxIILPvzTjH2ABdIK3_Iugjj0ev_ZnobCkA_XQ&time-granularity=day"
//                + "&start-timestamp=1506623400000&format=json";
//
//        MultiValueMap<String, String> valueMap = null;
//        String data = Rest.getData(url, valueMap);
//        JSONParser parser = new JSONParser();
//        Object jsonObj = parser.parse(data);
//        JSONObject json = (JSONObject) jsonObj;
//        Map<String, Object> jsonToMap = JsonSimpleUtils.jsonToMap(json);
//        System.out.println(jsonToMap.get("_total"));
//        List<Map<String, Object>> myData = (List<Map<String, Object>>) jsonToMap.get("values");
//
//        List<ColumnDef> columnDefObject = getColumnDefObject(myData);
//        Map returnMap = new HashMap();
//        returnMap.put("columnDef", columnDefObject);
//        returnMap.put("data", myData);
//        System.out.println(returnMap);
        // System.out.println(data);

//        List<Map<String, Object>> companyProfile = getCompanyProfile(oauthAccesToken, companyId);
//        List<Map<String, Object>> followersBySegments = getFollowersBySegments(oauthAccesToken, segment, companyId);
//        List<Map<String, Object>> events = getEventsMetrics(oauthAccesToken, companyId, eventType);
//        List<Map<String, Object>> companyLikesByEvent = getCompanyLikesByEvent(oauthAccesToken, updateKey);
//        List<Map<String, Object>> companySharesByEvent = getCompanySharesByEvent(oauthAccesToken, updateKey, companyId);
//        List<Map<String, Object>> historicalPageFollowers = gethistoricalPageFollowers(oauthAccesToken, companyId, granularity);//need to be done
        List<Map<String, Object>> pageViewsByMonth = getPageViewsByMonth(oauthAccesToken, companyId);
//        List<Map<String, Object>> recentPosts = getRecentPosts(oauthAccesToken, companyId);
    }

    public static List<Map<String, Object>> getRecentPosts(String oauthAccessToken, Long companyId) throws ParseException {

        /*
            Things to be done :
        --------------------------
        Need to convert timestamp to date format while displaying eaching post 
        
         */
        String url = "https://api.linkedin.com/v1/companies/" + companyId + "/updates?oauth2_access_token=" + oauthAccessToken
                + "&format=json";

        MultiValueMap<String, String> valueMap = null;
        String data = Rest.getData(url, valueMap);

        List<Map<String, Object>> returnMap = new ArrayList<>();
        JSONParser parser = new JSONParser();
        Object object = parser.parse(data);
        JSONObject jsonObj = (JSONObject) object;

        List<Map<String, Object>> values = (List<Map<String, Object>>) jsonObj.get("values");
        for (Iterator<Map<String, Object>> iterator = values.iterator(); iterator.hasNext();) {
            Map<String, Object> next = iterator.next();
            Map postMetrics = new HashMap();
            postMetrics.put("post", ((Map) ((Map) ((Map) next.get("updateContent")).get("companyStatusUpdate")).get("share")).get("comment"));
            postMetrics.put("total_likes", next.get("numLikes"));
            postMetrics.put("total_comments", ((Map) next.get("updateComments")).get("_total"));
            postMetrics.put("timestamp", next.get("timestamp"));
            returnMap.add(postMetrics);
        }
        System.out.println("Recent posts-->");
        System.out.println(returnMap);
        return returnMap;
    }

    public static List<Map<String, Object>> getPageViewsByMonth(String oauthAccessToken, Long CompanyId) throws ParseException {

        String url = "https://api.linkedin.com/v1/companies/" + CompanyId + "/company-statistics?"
                + "oauth2_access_token=" + oauthAccessToken + "&format=json";
        MultiValueMap<String, String> valueMap = null;
        String data = Rest.getData(url, valueMap);

        List<Map<String, Object>> returnMap = new ArrayList<>();
        JSONParser parser = new JSONParser();
        Object object = parser.parse(data);
        JSONObject jsonObj = (JSONObject) object;

     
        
//        
        Map viewByMonth = (Map) ((Map) (jsonObj.get("statusUpdateStatistics"))).get("viewsByMonth");
        
        
        System.out.println("page views object -->");
        System.out.println(JsonSimpleUtils.toMap(jsonObj));
        
        List<Map<String, Object>> values = (List<Map<String, Object>>) viewByMonth.get("values");
        for (Iterator<Map<String, Object>> iterator = values.iterator(); iterator.hasNext();) {
            Map<String, Object> next = iterator.next();
            Map pageViews = new HashMap();
            pageViews.put("month", ((Map) next.get("date")).get("month"));
            pageViews.put("year", ((Map) next.get("date")).get("year"));
            pageViews.put("clicks", next.get("clicks"));
            pageViews.put("likes", next.get("likes"));
            pageViews.put("comments", next.get("comments"));
            pageViews.put("shares", next.get("shares"));
            pageViews.put("impressions", next.get("impressions"));
            pageViews.put("engagements", next.get("engagement"));
            returnMap.add(pageViews);
        }
        System.out.println("getPageViewsByMonth--->");
        System.out.println(returnMap);
        return returnMap;

    }

    public static List<Map<String, Object>> getEventsMetrics(String oauthAccessToken, Long companyId, String eventType) throws ParseException {

        long likesCount = 0;
        String url = "https://api.linkedin.com/v1/companies/" + companyId + "/updates?oauth2_access_token=" + oauthAccessToken
                + "&event-type=" + eventType + "&format=json";

        MultiValueMap<String, String> valueMap = null;
        String data = Rest.getData(url, valueMap);

        List<Map<String, Object>> returnMap = new ArrayList<>();
        JSONParser parser = new JSONParser();
        Object object = parser.parse(data);
        JSONObject jsonObj = (JSONObject) object;

        System.out.println("Events Metrics are");

        long totalEvents = (long) jsonObj.get("_total");

        JSONArray jsonArray = (JSONArray) jsonObj.get("values");

        for (Iterator<Map<String, Object>> iterator = jsonArray.iterator(); iterator.hasNext();) {
            Map<String, Object> next = iterator.next();
            likesCount = likesCount + (long) next.get("numLikes");
        }
        Map eventMetrics = new HashMap();
        eventMetrics.put("totalEvents", totalEvents);
        eventMetrics.put("totalEventLikes", likesCount);
//
        returnMap.add(eventMetrics);

        System.out.println(returnMap);

        return returnMap;
    }

    public static List<Map<String, Object>> getCompanyProfile(String oauthAccessToken, Long companyId) throws ParseException {

//        String url = "https://api.linkedin.com/v1/companies/" + companyId + "?oauth2_access_token=" + oauthAccessToken + ""
//                + "&format=json";
        String url = "https://api.linkedin.com/v1/companies/" + companyId;
        MultiValueMap<String, String> valueMap = new LinkedMultiValueMap<>();
        valueMap.put("oauth2_access_token", Arrays.asList(oauthAccessToken));
        valueMap.put("format", Arrays.asList("json"));

        String data = Rest.getData(url, valueMap);

        System.out.println("Company Profile url");
        System.out.println(data);

        JSONParser parser = new JSONParser();
        JSONObject json = (JSONObject) parser.parse(data);

        List<Map<String, Object>> returnMap = new ArrayList<>();
        returnMap.add(JsonSimpleUtils.jsonToMap(json));

        return returnMap;
    }

    public static List<Map<String, Object>> getCompanyLikesByEvent(String oauthAccesToken, String updateKey) throws ParseException {

        String url = "https://api.linkedin.com/v1/companies/10671978/updates/"
                + "key=" + updateKey + "/likes?"
                + "oauth2_access_token=" + oauthAccesToken
                + "&format=json";

        MultiValueMap<String, String> valueMap = null;
        String data = Rest.getData(url, valueMap);

        JSONParser parser = new JSONParser();
        Object jsonObj = parser.parse(data);
        JSONObject json = (JSONObject) jsonObj;

        List<Map<String, Object>> returnMap = new ArrayList<>();
        Map likesMetrics = new HashMap();
        likesMetrics.put("company_likes_count", json.get("_total"));
        returnMap.add(likesMetrics);

        return returnMap;
    }

    public static List<Map<String, Object>> getCompanySharesByEvent(String oauthAccessToken, String updateKey,
            Long CompanyId) throws ParseException {
        String url = "https://api.linkedin.com/v1/companies/10671978/updates/key=" + updateKey + "/likes?"
                + "oauth2_access_token=" + oauthAccessToken + "&format=json";

        MultiValueMap<String, String> valueMap = null;
        String data = Rest.getData(url, valueMap);

        JSONParser parser = new JSONParser();
        Object jsonObj = parser.parse(data);
        JSONObject json = (JSONObject) jsonObj;

        List<Map<String, Object>> returnMap = new ArrayList<>();
        Map likesMetrics = new HashMap();
        likesMetrics.put("company_likes_count", json.get("_total"));
        returnMap.add(likesMetrics);

        return returnMap;

    }

    public static List<Map<String, Object>> gethistoricalPageFollowers(String oauthAccesToken, Long companyId, String granularity) throws ParseException {

        //timestamp between may to september 2017
        /*
            things to be done :
             get the startdate and endate and convert it into milliseconds
         */
        String url = "https://api.linkedin.com/v1/companies/" + companyId + "/historical-follow-statistics?"
                + "oauth2_access_token=" + oauthAccesToken + "&time-granularity=" + granularity
                + "&start-timestamp=1493596800000&end-timestamp=1504224000000&format=json";

        MultiValueMap<String, String> valueMap = null;
        String data = Rest.getData(url, valueMap);

        JSONParser parser = new JSONParser();
        Object object = parser.parse(data);
        JSONObject jsonObj = (JSONObject) object;

        List<Map<String, Object>> returnMap = new ArrayList<>();

        List<Map<String, Object>> values = (List<Map<String, Object>>) jsonObj.get("values");
        for (Iterator<Map<String, Object>> iterator = values.iterator(); iterator.hasNext();) {
            Map<String, Object> next = iterator.next();
            Map pageFollowers = new HashMap();
            pageFollowers.put("organic_followers_count", next.get("organicFollowerCount"));
            pageFollowers.put("paid_followers_count", next.get("paidFollowerCount"));
            pageFollowers.put("time", next.get("time"));
            returnMap.add(pageFollowers);
        }

        System.out.println("Historical page followers by month or day");
        System.out.println(returnMap);

        return returnMap;
    }

    public static List<Map<String, Object>> getFollowersBySegments(String oauthAccesToken, String segment,
            Long companyId) throws ParseException {

        if (segment.equalsIgnoreCase("overAll")) {
            List<Map<String, Object>> overAllFollowersBySegments = getFollowersCount(oauthAccesToken, companyId);
            System.out.println("oveall");
            System.out.println(overAllFollowersBySegments);
            return overAllFollowersBySegments;
        } else {
            String url = "https://api.linkedin.com/v1/companies/" + companyId + "/company-statistics?"
                    + "oauth2_access_token=" + oauthAccesToken + "&format=json";

            MultiValueMap<String, String> valueMap = null;
            String data = Rest.getData(url, valueMap);
            JSONParser parser = new JSONParser();
            Object jsonObj = parser.parse(data);
            JSONObject object = (JSONObject) jsonObj;

            if (segment.equalsIgnoreCase("country")) {
                segment = "countries";
                return getSegmentsData(object, segment);
            } else if (segment.equalsIgnoreCase("regions")) {
                segment = "regions";
                return getSegmentsData(object, segment);
            } else if (segment.equalsIgnoreCase("jobFunction")) {
                segment = "functions";
                return getSegmentsData(object, segment);
            } else if (segment.equalsIgnoreCase("seniority")) {
                segment = "seniorities";
                return getSegmentsData(object, segment);
            } else if (segment.equalsIgnoreCase("industries")) {
                segment = "industries";
                return getSegmentsData(object, segment);
            } else if (segment.equalsIgnoreCase("companySize")) {
                segment = "companySizes";
                return getSegmentsData(object, segment);
            } else if (segment.equalsIgnoreCase("employmentStatus")) {
                List<Map<String, Object>> returnMap = new ArrayList<>();
                Map employementStatus = new HashMap();
                employementStatus.put("employee_count", (Map) ((Map) object.get("followStatistics")).get("employeeCount"));
                employementStatus.put("non_employee_count", (Map) ((Map) object.get("followStatistics")).get("nonEmployeeCount"));
                returnMap.add(employementStatus);
                return returnMap;
            }
        }
        return null;
    }

    public static List<Map<String, Object>> getSegmentsData(JSONObject object, String segment) {

        List<Map<String, Object>> returnMap = new ArrayList<>();
        Map segmentData = new HashMap();
        Map<String, Object> segments = (Map) ((Map) object.get("followStatistics")).get(segment);
        List<Map<String, Object>> values = (List<Map<String, Object>>) segments.get("values");
        for (Iterator<Map<String, Object>> iterator = values.iterator(); iterator.hasNext();) {
            Map<String, Object> next = iterator.next();
            segmentData.put(next.get("entryKey"), next.get("entryValue"));
        }
        returnMap.add(segmentData);

        if (!segment.equalsIgnoreCase("regions")) {
            return compareData(returnMap, segment);
        } else {
            return returnMap;
        }
    }

    public static List<Map<String, Object>> getFollowersCount(String oauthAccesToken, Long companyId) throws ParseException {

        String url = "https://api.linkedin.com/v1/companies/" + companyId + "/company-statistics?"
                + "oauth2_access_token=" + oauthAccesToken + "&format=json";

        MultiValueMap<String, String> valueMap = null;
        String data = Rest.getData(url, valueMap);
        JSONParser parser = new JSONParser();
        Object jsonObj = parser.parse(data);
        JSONObject object = (JSONObject) jsonObj;

        List<Map<String, Object>> returnMap = new ArrayList<>();

        Map<String, Object> followStatistics = (Map) object.get("followStatistics");

        Map<String, Object> followersMetrics = new HashMap<>();
        followersMetrics.put("total_followers", followStatistics.get("count"));
        followersMetrics.put("country_followers", ((Map) followStatistics.get("countries")).get("_total"));
        followersMetrics.put("jobfunction_followers", ((Map) followStatistics.get("functions")).get("_total"));
        followersMetrics.put("industries_followers", ((Map) followStatistics.get("industries")).get("_total"));
        followersMetrics.put("region_followers", ((Map) followStatistics.get("regions")).get("_total"));
        followersMetrics.put("seniorities_followers", ((Map) followStatistics.get("seniorities")).get("_total"));
        followersMetrics.put("company_size_followers", ((Map) followStatistics.get("companySizes")).get("_total"));
        followersMetrics.put("employee_count", followStatistics.get("employeeCount"));
        followersMetrics.put("nonemployee_count", followStatistics.get("nonEmployeeCount"));
        returnMap.add(followersMetrics);

        return returnMap;
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

    public static List<Map<String, Object>> compareData(List<Map<String, Object>> returnMap, String segment) {

        Map<String, Object> postType = LinkedInPostType.getPostType(segment);

        Map mapping = new HashMap();
        List<Map<String, Object>> map = new ArrayList<>();

        for (Iterator<Map<String, Object>> iterator = returnMap.iterator(); iterator.hasNext();) {
            Map<String, Object> next = iterator.next();
            for (Map.Entry<String, Object> entry : next.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                if (postType.containsKey(key)) {
                    mapping.put(postType.get(key), value);
                }
            }
        }
        map.add(mapping);
        return map;
    }

}
