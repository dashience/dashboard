/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.admin.service;

import com.visumbu.vb.bean.LinkedInPostType;
import com.visumbu.vb.utils.DateUtils;
import com.visumbu.vb.utils.JsonSimpleUtils;
//import com.visumbu.vb.utils.ExampleConfig;
import com.visumbu.vb.utils.Rest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONArray;

import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

/**
 *
 * @author deldot
 */
@Service("linkedinService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class LinkedinService {

    public final String BASE_URL = "https://api.linkedin.com/v1/companies/";

    public List<Map<String, Object>> get(String oauthAccesToken, String dataSetReportName,
            Date startDate, Date endDate, String aggregation, String productSegment, Long companyId) {

        if (dataSetReportName.equalsIgnoreCase("companyProfile")) {
            return getCompanyProfile(oauthAccesToken, companyId);
        }

        if (dataSetReportName.equalsIgnoreCase("pagePerformance")) {
            return getPagePerfomanceByMonth(oauthAccesToken, companyId);
        }

        if (dataSetReportName.equalsIgnoreCase("pageFollowersPerformance")) {
            return getPageFollowersPerformance(oauthAccesToken, companyId, aggregation, productSegment, startDate, endDate);
        }

        if (dataSetReportName.equalsIgnoreCase("postPerformance")) {
            return getPostPerformance(oauthAccesToken, companyId, aggregation, productSegment);
        }

        return null;
    }

    public List<Map<String, Object>> getPageFollowersPerformance(String oauthAccessToken, Long companyId,
            String timeSegment, String productSegment, Date startDate, Date endDate) {

        try {
            String segment = null;

            String url = BASE_URL + companyId+"/company-statistics";
            
           

            MultiValueMap<String, String> valueMap = new LinkedMultiValueMap<>();
            valueMap.put("oauth2_access_token", Arrays.asList(oauthAccessToken));
            valueMap.put("format", Arrays.asList("json"));

            String data = Rest.getData(url, valueMap);
            JSONParser parser = new JSONParser();
            Object jsonObj = parser.parse(data);
            JSONObject object = (JSONObject) jsonObj;
            
             System.out.println("Linkedin In URL -->");
             System.out.println(data);

            if (timeSegment.equalsIgnoreCase("none") && productSegment.equalsIgnoreCase("overall")) {
                return getOverallPageFollowers(oauthAccessToken, companyId);
            } else if (timeSegment.equalsIgnoreCase("none") && productSegment.equalsIgnoreCase("employmentStatus")) {
                List<Map<String, Object>> returnMap = new ArrayList<>();
                Map employementStatus = new HashMap();
                employementStatus.put("employee_count", (long) ((Map) object.get("followStatistics")).get("employeeCount"));
                employementStatus.put("non_employee_count", (long) ((Map) object.get("followStatistics")).get("nonEmployeeCount"));
                returnMap.add(employementStatus);
                return returnMap;
            } else if (!timeSegment.equalsIgnoreCase("none") && productSegment.equalsIgnoreCase("historicalPageFollowers")) {
                try {

                    String startDateStr = DateUtils.dateToString(startDate, "YYYY-MM-dd");
                    String endDateStr = DateUtils.dateToString(endDate, "YYYY-MM-dd");

                    long startTime = DateUtils.dateToTimeStamp(startDateStr);
                    long endTime = DateUtils.dateToTimeStamp(endDateStr);

                    String granularity = timeSegment;

                    String historicUrl = BASE_URL + companyId + "/historical-follow-statistics";

                    MultiValueMap<String, String> valueMapData = new LinkedMultiValueMap<>();
                    valueMapData.put("oauth2_access_token", Arrays.asList(oauthAccessToken));
                    valueMapData.put("time-granularity", Arrays.asList(granularity));
                    valueMapData.put("start-timestamp", Arrays.asList(startTime + ""));
                    valueMapData.put("end-timestampn", Arrays.asList(endTime + ""));
                    valueMapData.put("format", Arrays.asList("json"));

                    String historicData = Rest.getData(historicUrl, valueMapData);

                    System.out.println(historicData);

                    JSONParser parserData = new JSONParser();
                    JSONObject jsonObjData = (JSONObject) parserData.parse(historicData);

                    List<Map<String, Object>> returnMap = new ArrayList<>();

                    List<Map<String, Object>> values = (List<Map<String, Object>>) jsonObjData.get("values");
                    for (Iterator<Map<String, Object>> iterator = values.iterator(); iterator.hasNext();) {
                        Map<String, Object> dataMap = iterator.next();
                        Map pageFollowers = new HashMap();
                        pageFollowers.put("organic_followers_count", dataMap.get("organicFollowerCount"));
                        pageFollowers.put("paid_followers_count", dataMap.get("paidFollowerCount"));
                        pageFollowers.put("time", dataMap.get("time"));
                        returnMap.add(pageFollowers);

                    }
                    return returnMap;
                } catch (ParseException ex) {
                    Logger.getLogger(LinkedinService.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                System.out.println("Product Segment -->"+productSegment);
                System.out.println("object is "+object);
                return getSegmentsData(object, productSegment);
            }

            //historical page followers for both organic and paid
            return null;
        } catch (ParseException ex) {
            Logger.getLogger(LinkedinService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public List<Map<String, Object>> getOverallPageFollowers(String oauthAccesToken, Long companyId) {

        try {

            String url = BASE_URL + "company-statistics";

            MultiValueMap<String, String> valueMap = new LinkedMultiValueMap<>();
            valueMap.put("oauth2_access_token", Arrays.asList(oauthAccesToken));
            valueMap.put("format", Arrays.asList("json"));

            String data = Rest.getData(url, valueMap);
            JSONParser parser = new JSONParser();
            JSONObject object = (JSONObject) parser.parse(data);

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
        } catch (ParseException ex) {
            Logger.getLogger(LinkedinService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public List<Map<String, Object>> getPagePerfomanceByMonth(String oauthAccessToken, Long CompanyId) {

        try {

            String url = BASE_URL + CompanyId + "/company-statistics";

            MultiValueMap<String, String> valueMap = new LinkedMultiValueMap<>();
            valueMap.put("oauth2_access_token", Arrays.asList(oauthAccessToken));
            valueMap.put("format", Arrays.asList("json"));

            String data = Rest.getData(url, valueMap);

            List<Map<String, Object>> returnMap = new ArrayList<>();
            JSONParser parser = new JSONParser();
            Object object = parser.parse(data);
            JSONObject jsonObj = (JSONObject) object;

            Map<String, Object> viewByMonth = (Map) ((Map) (jsonObj.get("statusUpdateStatistics"))).get("viewsByMonth");

            List<Map<String, Object>> values = (List<Map<String, Object>>) viewByMonth.get("values");
            for (Iterator<Map<String, Object>> iterator = values.iterator(); iterator.hasNext();) {
                Map<String, Object> dataMap = iterator.next();
                Map pageViews = new HashMap();
                pageViews.put("month", ((Map) dataMap.get("date")).get("month"));
                pageViews.put("year", ((Map) dataMap.get("date")).get("year"));
                pageViews.put("clicks", dataMap.get("clicks"));
                pageViews.put("likes", dataMap.get("likes"));
                pageViews.put("comments", dataMap.get("comments"));
                pageViews.put("shares", dataMap.get("shares"));
                pageViews.put("impressions", dataMap.get("impressions"));
                pageViews.put("engagements", dataMap.get("engagement"));
                returnMap.add(pageViews);
            }
            System.out.println("getPageViewsByMonth--->");
            System.out.println(returnMap);

            return returnMap;
        } catch (ParseException ex) {
            Logger.getLogger(LinkedinService.class.getName()).log(Level.SEVERE, null, ex);

        }
        return null;

    }

    public List<Map<String, Object>> getPostPerformance(String oauthAccessToken, Long companyId,
            String timeSgement, String productSegment) {
        if (productSegment.equalsIgnoreCase("overall")) {
            return getOverallPostMetrics(oauthAccessToken, companyId);

        } else if (productSegment.equalsIgnoreCase("recentPosts")) {
            return getRecentPosts(oauthAccessToken, companyId);

        } else if (productSegment.equalsIgnoreCase("postType")) {
            return getPostPerformanceByPostType(oauthAccessToken, companyId);
        }
        return null;

    }

    public List<Map<String, Object>> getPostPerformanceByPostType(String oauthAccessToken, Long companyId) {
        try {
            long likesCount = 0;
            String[] postType = {"status-update", "job-posting"};
            Map postMetrics = new HashMap();
            List<Map<String, Object>> returnMap = new ArrayList<>();
            for (String event : postType) {

                String url = BASE_URL + companyId + "/updates";

                MultiValueMap<String, String> valueMap = new LinkedMultiValueMap<>();
                valueMap.put("oauth2_access_token", Arrays.asList(oauthAccessToken));
                valueMap.put("event-type", Arrays.asList(event));
                valueMap.put("format", Arrays.asList("json"));

                String data = Rest.getData(url, valueMap);

                JSONParser parser = new JSONParser();
                JSONObject jsonObj = (JSONObject) parser.parse(data);

                long totalEvents = (long) jsonObj.get("_total");
                postMetrics.put(event, totalEvents);

            }
            returnMap.add(postMetrics);

            return returnMap;
        } catch (ParseException ex) {
            Logger.getLogger(LinkedinService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public List<Map<String, Object>> getRecentPosts(String oauthAccessToken, Long companyId) {

        try {

            String url = BASE_URL + companyId + "/updates";

            MultiValueMap<String, String> valueMap = new LinkedMultiValueMap<>();
            valueMap.put("oauth2_access_token", Arrays.asList(oauthAccessToken));
            valueMap.put("format", Arrays.asList("json"));

            String data = Rest.getData(url, valueMap);

            List<Map<String, Object>> returnMap = new ArrayList<>();
            JSONParser parser = new JSONParser();
            JSONObject jsonObj = (JSONObject) parser.parse(data);

            List<Map<String, Object>> values = (List<Map<String, Object>>) jsonObj.get("values");
            for (Iterator<Map<String, Object>> iterator = values.iterator(); iterator.hasNext();) {
                Map<String, Object> dataMap = iterator.next();
                Map postMetrics = new HashMap();
                postMetrics.put("post", ((Map) ((Map) ((Map) dataMap.get("updateContent")).get("companyStatusUpdate")).get("share")).get("comment"));
                postMetrics.put("total_likes", dataMap.get("numLikes"));
                postMetrics.put("total_comments", ((Map) dataMap.get("updateComments")).get("_total"));
                postMetrics.put("timestamp", dataMap.get("timestamp"));
                returnMap.add(postMetrics);
            }
            System.out.println("Recent posts-->");
            System.out.println(returnMap);
            return returnMap;
        } catch (ParseException ex) {
            Logger.getLogger(LinkedinService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public List<Map<String, Object>> getOverallPostMetrics(String oauthAccessToken, Long companyId) {

        try {
            long likesCount = 0;

            String url = BASE_URL + companyId + "/updates";

            MultiValueMap<String, String> valueMap = new LinkedMultiValueMap<>();
            valueMap.put("oauth2_access_token", Arrays.asList(oauthAccessToken));
            valueMap.put("format", Arrays.asList("json"));

            String data = Rest.getData(url, valueMap);

            List<Map<String, Object>> returnMap = new ArrayList<>();
            JSONParser parser = new JSONParser();
            Object object = parser.parse(data);
            JSONObject jsonObj = (JSONObject) object;

            long totalEvents = (long) jsonObj.get("_total");

            JSONArray jsonArray = (JSONArray) jsonObj.get("values");

            for (Iterator<Map<String, Object>> iterator = jsonArray.iterator(); iterator.hasNext();) {
                Map<String, Object> next = iterator.next();
                likesCount = likesCount + (long) next.get("numLikes");
            }

            Map eventMetrics = new HashMap();
            eventMetrics.put("totalEvents", totalEvents);
            eventMetrics.put("totalEventLikes", likesCount);

            returnMap.add(eventMetrics);

            return returnMap;
        } catch (ParseException ex) {
            Logger.getLogger(LinkedinService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public List<Map<String, Object>> getCompanyProfile(String oauthAccessToken, Long companyId) {

        try {

            String url = BASE_URL + companyId;
            MultiValueMap<String, String> valueMap = new LinkedMultiValueMap<>();
            valueMap.put("oauth2_access_token", Arrays.asList(oauthAccessToken));
            valueMap.put("format", Arrays.asList("json"));

            String data = Rest.getData(url, valueMap);

            JSONParser parser = new JSONParser();
            JSONObject json = (JSONObject) parser.parse(data);

            List<Map<String, Object>> returnMap = new ArrayList<>();
            returnMap.add(JsonSimpleUtils.jsonToMap(json));

            return returnMap;
        } catch (ParseException ex) {
            Logger.getLogger(LinkedinService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public List<Map<String, Object>> getSegmentsData(JSONObject object, String segment) {

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

    public List<Map<String, Object>> compareData(List<Map<String, Object>> returnMap, String segment) {
        Map<String, Object> postType = LinkedInPostType.getPostType(segment);
        Map mapping = new HashMap();
        List<Map<String, Object>> returnList = new ArrayList<>();

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
        returnList.add(mapping);
        return returnList;
    }

}