/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.admin.service;

import com.visumbu.vb.admin.scheduler.service.ResourceManager;
import com.visumbu.vb.model.TokenDetails;
import com.visumbu.vb.utils.Rest;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

/**
 *
 * @author dashience
 */
@Service
public class LinkedInService {

    private String accessToken;
        public final String BASE_URL = "https://api.linkedin.com/v1/companies/";

    @Autowired
    private ResourceManager resourceManager;
    
//    public void setAccessToken(String accessToken) {
//        System.out.println("accesstoken set" + accessToken);
//        this.accessToken = accessToken;
//    }


    public String get(HttpServletRequest request,Long companyId) {
        String dataSetReportName = request.getParameter("dataSetName");
        List<TokenDetails> TokenDetails = resourceManager.getOauthToken(request,"linkedIn");
        String accessToken = TokenDetails.get(0).getTokenValue();

        if (dataSetReportName.equalsIgnoreCase("companyProfile")) {
            return getCompanyProfile(accessToken, companyId);
        }

        if (dataSetReportName.equalsIgnoreCase("pagePerformance")) {
            return getPagePerfomanceByMonth(accessToken, companyId);
        }

//        if (dataSetReportName.equalsIgnoreCase("pageFollowersPerformance")) {
//            return getPageFollowersPerformance(accessToken, companyId, aggregation, productSegment, startDate, endDate);
//        }
//
//        if (dataSetReportName.equalsIgnoreCase("postPerformance")) {
//            return getPostPerformance(accessToken, companyId, aggregation, productSegment);
//        }

        return null;
    }

    public List<Map<String, Object>> getPageFollowersPerformance(String oauthAccessToken, Long companyId,
            String timeSegment, String productSegment, Date startDate, Date endDate) {
            String segment = null;

            String url = BASE_URL + companyId + "/company-statistics";

            MultiValueMap<String, String> valueMap = new LinkedMultiValueMap<>();
            valueMap.put("oauth2_access_token", Arrays.asList(oauthAccessToken));
            valueMap.put("format", Arrays.asList("json"));

            String data = Rest.getData(url, valueMap);
        return null;
    }

    public List<Map<String, Object>> getOverallPageFollowers(String oauthAccesToken, Long companyId) {


            String url = BASE_URL + "company-statistics";

            MultiValueMap<String, String> valueMap = new LinkedMultiValueMap<>();
            valueMap.put("oauth2_access_token", Arrays.asList(oauthAccesToken));
            valueMap.put("format", Arrays.asList("json"));

            String data = Rest.getData(url, valueMap);
        return null;
    }

    public String getPagePerfomanceByMonth(String oauthAccessToken, Long CompanyId) {

            String url = BASE_URL + CompanyId + "/company-statistics";

            MultiValueMap<String, String> valueMap = new LinkedMultiValueMap<>();
            valueMap.put("oauth2_access_token", Arrays.asList(oauthAccessToken));
            valueMap.put("format", Arrays.asList("json"));

            return Rest.getData(url, valueMap);

    }

//    public List<Map<String, Object>> getPostPerformance(String oauthAccessToken, Long companyId,
//            String timeSgement, String productSegment) {
//        if (productSegment.equalsIgnoreCase("overall")) {
//            return getOverallPostMetrics(oauthAccessToken, companyId);
//
//        } else if (productSegment.equalsIgnoreCase("recentPosts")) {
//            return getRecentPosts(oauthAccessToken, companyId);
//
//        } else if (productSegment.equalsIgnoreCase("postType")) {
//            return getPostPerformanceByPostType(oauthAccessToken, companyId);
//        }
//        return null;
//
//    }
//
//    public List<Map<String, Object>> getPostPerformanceByPostType(String oauthAccessToken, Long companyId) {
//        long likesCount = 0;
//        String[] postType = {"status-update", "job-posting"};
//        Map postMetrics = new HashMap();
//        List<Map<String, Object>> returnMap = new ArrayList<>();
//        for (String event : postType) {
//
//            String url = BASE_URL + companyId + "/updates";
//
//            MultiValueMap<String, String> valueMap = new LinkedMultiValueMap<>();
//            valueMap.put("oauth2_access_token", Arrays.asList(oauthAccessToken));
//            valueMap.put("event-type", Arrays.asList(event));
//            valueMap.put("format", Arrays.asList("json"));
//
//            String data = Rest.getData(url, valueMap);
//        }
//        return null;
//    }
//
//    public List<Map<String, Object>> getRecentPosts(String oauthAccessToken, Long companyId) {
//
//        String url = BASE_URL + companyId + "/updates";
//
//        MultiValueMap<String, String> valueMap = new LinkedMultiValueMap<>();
//        valueMap.put("oauth2_access_token", Arrays.asList(oauthAccessToken));
//        valueMap.put("format", Arrays.asList("json"));
//
//        String data = Rest.getData(url, valueMap);
//        return null;
//    }
//
//    public List<Map<String, Object>> getOverallPostMetrics(String oauthAccessToken, Long companyId) {
//        long likesCount = 0;
//
//        String url = BASE_URL + companyId + "/updates";
//
//        MultiValueMap<String, String> valueMap = new LinkedMultiValueMap<>();
//        valueMap.put("oauth2_access_token", Arrays.asList(oauthAccessToken));
//        valueMap.put("format", Arrays.asList("json"));
//
//        String data = Rest.getData(url, valueMap);
//        return null;
//    }

    public String getCompanyProfile(String oauthAccessToken, Long companyId) {

        String url = BASE_URL + companyId;
        MultiValueMap<String, String> valueMap = new LinkedMultiValueMap<>();
        valueMap.put("oauth2_access_token", Arrays.asList(oauthAccessToken));
        valueMap.put("format", Arrays.asList("json"));

        String data = Rest.getData(url, valueMap);
        return data;
    }
}
