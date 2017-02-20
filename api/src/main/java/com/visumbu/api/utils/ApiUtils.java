/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.api.utils;

import com.visumbu.api.bean.AccountDetails;
import com.visumbu.api.bing.report.xml.bean.Data;
import com.visumbu.api.dashboard.bean.AdPerformanceReportBean;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author user
 */
public class ApiUtils {

    public static String getDisplayAdDescription(AdPerformanceReportBean reportBean) {
        String adDescription = "";
        adDescription = reportBean.getHeadline().replaceAll("’", "'") + "<br/>"
                + ((reportBean.getCreativeFinalUrls() == null || reportBean.getCreativeFinalUrls().isEmpty()) ? "" : (reportBean.getCreativeFinalUrls() + "<br/>"));
        return adDescription;
    }

    public static String getPaidAdDescription(AdPerformanceReportBean reportBean) {
        String adDescription = "";
        adDescription = reportBean.getDescription().replaceAll("’", "'") + "<br/>"
                + ((reportBean.getDescription1() == null || reportBean.getDescription1().isEmpty()) ? "" : (reportBean.getDescription1().replaceAll("’", "'") + "<br/>"))
                + ((reportBean.getDisplayUrl() == null || reportBean.getDisplayUrl().isEmpty()) ? "" : (reportBean.getDisplayUrl() + "<br/>"));
        if (reportBean.getDescription() == null || reportBean.getDescription().isEmpty() || reportBean.getDescription().equalsIgnoreCase("--")) {
            adDescription
                    = ((reportBean.getDescription1() == null || reportBean.getDescription1().isEmpty()) ? "" : (reportBean.getDescription1().replaceAll("’", "'") + "<br/>"))
                    + ((reportBean.getDescription2() == null || reportBean.getDescription2().isEmpty()) ? "" : (reportBean.getDescription2().replaceAll("’", "'") + "<br/>"));
        }
        return adDescription;
    }

    public static Integer toInteger(String string) {
        try {
            return toDouble(string).intValue();
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
        String line = "";
        String cvsSplitBy = ",";
        ClassLoader classLoader = ApiUtils.class.getClassLoader();
        File file = new File(classLoader.getResource("adwords/AdWordsCity.csv").getFile());
        try (BufferedReader br = new BufferedReader(new java.io.FileReader(file))) {
            while ((line = br.readLine()) != null) {
                String[] city = line.split(cvsSplitBy);
                if (cityId.equalsIgnoreCase(city[0])) {
                    return city[1];
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Unknown";
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

    public static String removePercent(String value) {
        if (value == null) {
            return "0.0";
        }
        value = value.replaceAll("%", "");
        try {
            return (Double.parseDouble(value) / 100.0) + "";
        } catch (Exception e) {

        }
        return "0.0";
    }

    public static Data removePercent(Data value) {
        Data data = new Data();
        String value1 = value.getValue();
        if (value == null) {
            data.setValue("0.0");
            return data;
        }
        value1 = value1.replaceAll("%", "");
        try {
            data.setValue((Double.parseDouble(value1) / 100.0) + "");
            return data;
        } catch (Exception e) {

        }
        data.setValue("0.0");
        return data;
    }

    public static String toMins(String seconds) {
        Double secondsInt = toDouble(seconds);
        Double minsInt = secondsInt/60;
        secondsInt = secondsInt%60;
        return String.format("%02d", minsInt.intValue()) + ":" + String.format("%02d", secondsInt.intValue());
    }

     public static void main(String[] argv) {
        System.out.println(toMins("7468"));
    }

}
