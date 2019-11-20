/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.api.utils;

import com.visumbu.api.bean.AccountDetails;
import com.visumbu.api.dashboard.bean.AdPerformanceReportBean;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author user
 */
public class ApiUtils {

    public static String getDisplayAdDescription(AdPerformanceReportBean reportBean) {
        String adDescription = "";
        adDescription = reportBean.getHeadline() + "<br/>"
                + ((reportBean.getCreativeFinalUrls() == null || reportBean.getCreativeFinalUrls().isEmpty()) ? "" : (reportBean.getCreativeFinalUrls() + "<br/>"));
        return adDescription;
    }

    public static String getPaidAdDescription(AdPerformanceReportBean reportBean) {
        String adDescription = "";
        adDescription = reportBean.getDescription() + "<br/>"
                + ((reportBean.getDescription1() == null || reportBean.getDescription1().isEmpty()) ? "" : (reportBean.getDescription1() + "<br/>"))
                + ((reportBean.getDisplayUrl() == null || reportBean.getDisplayUrl().isEmpty()) ? "" : (reportBean.getDisplayUrl() + "<br/>"));
        if (reportBean.getDescription() == null || reportBean.getDescription().isEmpty() || reportBean.getDescription().equalsIgnoreCase("--")) {
            adDescription
                    = ((reportBean.getDescription1() == null || reportBean.getDescription1().isEmpty()) ? "" : (reportBean.getDescription1() + "<br/>"))
                    + ((reportBean.getDescription2() == null || reportBean.getDescription2().isEmpty()) ? "" : (reportBean.getDescription2() + "<br/>"));
        }
        return adDescription;
    }

    public static Integer toInteger(String string) {
        try {
            return Integer.parseInt(string);
        } catch (Exception e) {

        }
        return 0;
    }

    public static Double toDouble(String string) {
        try {
            return Double.parseDouble(string);
        } catch (Exception e) {

        }
        return 0.0;
    }

    public static String getCityById(String cityId) {
        System.out.println("For City Id " + cityId);
        String line = "";
        String cvsSplitBy = ",";
        ClassLoader classLoader = ApiUtils.class.getClassLoader();
        File file = new File(classLoader.getResource("adwords/AdWordsCity.csv").getFile());
        try (BufferedReader br = new BufferedReader(new java.io.FileReader(file))) {
            while ((line = br.readLine()) != null) {
                String[] city = line.split(cvsSplitBy);
                if (cityId.equalsIgnoreCase(city[0])) {
                    System.out.println("For City Id " + cityId + " --- " + city[1]);
                    return city[1];
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void main(String[] argv) {
        System.out.println(toGaAccountId("5360684369"));
    }

    public static String toGaAccountId(String ppcBingAccountId) {
        return ppcBingAccountId.substring(0, 3) + "-" + ppcBingAccountId.substring(3, 6) + "-" + ppcBingAccountId.substring(6);
    }

    public static AccountDetails toAccountDetails(HttpServletRequest request, String serviceName) {
        String ppcBingAccountId = request.getParameter(serviceName + "BingAccountId");
        Long bingAccountId = null;
        try {
            bingAccountId = Long.parseLong(ppcBingAccountId);
        } catch (Exception e) {

        }
        String ppcGoogleAdwordsAccountId = request.getParameter(serviceName + "GoogleAdwordsAccountId");
        String adwordsAccountId = null;
        try {
            adwordsAccountId = ApiUtils.toGaAccountId(ppcGoogleAdwordsAccountId);
        } catch (Exception e) {

        }
        String ppcGoogleAnalyticsProfileId = request.getParameter(serviceName + "GoogleAnalyticsProfileId");
        System.out.println(serviceName + "GoogleAnalyticsProfileId");
        System.out.println(ppcGoogleAnalyticsProfileId);
        System.out.println(request.getParameter("seoGoogleAnalyticsProfileId"));
        String analyticsProfileId = null;
        try {
            analyticsProfileId = ppcGoogleAnalyticsProfileId;
        } catch (Exception e) {

        }
        String ppcGoogleAnalyticsAccountId = request.getParameter(serviceName + "GoogleAnalyticsAccountId");
        String analyticsAccountId = null;
        try {
            analyticsAccountId = ppcGoogleAnalyticsAccountId;
        } catch (Exception e) {

        }
        String ppcCenturyAccountId = request.getParameter(serviceName + "CenturyAccountId");
        Long centuryAccountId = null;
        try {
            centuryAccountId = Long.parseLong(ppcCenturyAccountId);
        } catch (Exception e) {

        }
        String facebookSmAccountIdStr = request.getParameter(serviceName + "FacebookSmAccountId");
        Long facebookSmAccountId = null;
        try {
            facebookSmAccountId = Long.parseLong(facebookSmAccountIdStr);
        } catch (Exception e) {

        }
        String facebookAccountIdStr = request.getParameter(serviceName + "FacebookAdsAccountId");
        Long facebookAccountId = null;
        try {
            facebookAccountId = Long.parseLong(facebookAccountIdStr);
        } catch (Exception e) {

        }

        String reviewpushAccountIdStr = request.getParameter(serviceName + "ReviewpushAccountId");
        Long reviewpushAccountId = null;
        try {
            reviewpushAccountId = Long.parseLong(reviewpushAccountIdStr);
        } catch (Exception e) {

        }

        AccountDetails accountDetails = new AccountDetails();
        accountDetails.setBingAccountId(bingAccountId);
        accountDetails.setAdwordsAccountId(adwordsAccountId);
        accountDetails.setAnalyticsProfileId(analyticsProfileId);
        accountDetails.setAnalyticsAccountId(analyticsAccountId);
        accountDetails.setCenturyAccountId(centuryAccountId);
        accountDetails.setFacebookAccountId(facebookAccountId);
        accountDetails.setFacebookSmAccountId(facebookSmAccountId);
        accountDetails.setReviewPushAccountId(reviewpushAccountId + "");
        return accountDetails;
    }

    public static String compileQuery(String query, Map<String, String[]> parameters) {
        if (query == null) {
            return "";
        }
        Map<String, String> templateVariable = new HashMap<String, String>();

        for (Map.Entry<String, String[]> entrySet : parameters.entrySet()) {
            String key = entrySet.getKey();
            String[] value = entrySet.getValue();
            if (value != null) {
                templateVariable.put(key, joinStringArray(value));
            }
        }

        Set<String> templateKeys = templateVariable.keySet();
        System.out.println("Message Template " + query);
        for (String templateKey : templateKeys) {
            System.out.println("Compiling " + templateKey + " value " + templateVariable.get(templateKey));
            String value = templateVariable.get(templateKey);
            value = value == null ? "-" : value;
            try {
                query = query.replaceAll("(?i)" + Matcher.quoteReplacement("$" + templateKey + "$"), value);
            } catch (Exception e) {
                System.out.println(e.getMessage());
                //e.printStackTrace();
            }
        }
        return query;
    }

    public static String joinStringArray(String[] stringArray) {
        if (stringArray.length > 0) {
            StringBuilder nameBuilder = new StringBuilder();
            for (String n : stringArray) {
                nameBuilder.append("'").append(n.replace("'", "\\'")).append("',");
            }
            nameBuilder.deleteCharAt(nameBuilder.length() - 1);
            return nameBuilder.toString();
        } else {
            return "";
        }
    }

}
