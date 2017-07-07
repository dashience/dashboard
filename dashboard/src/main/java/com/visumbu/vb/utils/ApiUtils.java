/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.utils;

import com.google.api.ads.adwords.lib.jaxb.v201609.ReportDefinitionReportType;
import com.visumbu.api.bing.report.xml.bean.Data;
import com.visumbu.vb.admin.service.AdwordsService;
import com.visumbu.vb.bean.AdwordsReport;
import com.visumbu.vb.bean.GaReport;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author user
 */
public class ApiUtils {

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
        if(cityId == null) {
            return null;
        }
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
        Double minsInt = secondsInt / 60;
        secondsInt = secondsInt % 60;
        return String.format("%02d", minsInt.intValue()) + ":" + String.format("%02d", secondsInt.intValue());
    }

    public static void main(String[] argv) {
        System.out.println(toMins("7468"));
    }

    public static Map<String, AdwordsReport> getAllAdwordsReports() {
        Map<String, AdwordsReport> adwordsReports = new HashMap<>();
        adwordsReports.put("accountPerformance",
                new AdwordsReport("accountPerformance", new String[]{"AccountDescriptiveName", "Impressions", "Clicks",
                    "SearchImpressionShare", "SearchExactMatchImpressionShare", "SearchBudgetLostImpressionShare", "SearchRankLostImpressionShare",
                    "ContentImpressionShare", "ContentBudgetLostImpressionShare", "ContentRankLostImpressionShare",
                    "Conversions", "AveragePosition", "AllConversions",
                    "AverageCpc", "Ctr", "Cost", "CostPerConversion", "ConversionRate", "ExternalCustomerId","AdNetworkType2"},
                        ReportDefinitionReportType.ACCOUNT_PERFORMANCE_REPORT));
        adwordsReports.put("campaignPerformance",
                new AdwordsReport("campaignPerformance", new String[]{"CampaignId", "AccountDescriptiveName", "CampaignName",
                    "Impressions", "Clicks", "Amount", "SearchImpressionShare", "SearchExactMatchImpressionShare", "SearchBudgetLostImpressionShare", "SearchRankLostImpressionShare",
                    "ContentImpressionShare", "ContentBudgetLostImpressionShare", "ContentRankLostImpressionShare", "Conversions", "AveragePosition", "AllConversions",
                    "AverageCpc", "Ctr", "Cost", "CostPerConversion", "ConversionRate", "ExternalCustomerId","AdNetworkType2"},
                        ReportDefinitionReportType.CAMPAIGN_PERFORMANCE_REPORT));

        adwordsReports.put("adGroupPerformance",
                new AdwordsReport("adGroupPerformance", new String[]{"AccountDescriptiveName", "Impressions", "Clicks", "AdGroupId", "AdGroupName",
                    "Conversions", "AveragePosition",
                    "AllConversions", "AverageCpc", "Ctr", "Cost", "CostPerConversion", "ConversionRate", "ExternalCustomerId","AdNetworkType2"},
                        ReportDefinitionReportType.ADGROUP_PERFORMANCE_REPORT));

        adwordsReports.put("keywordPerformance",
                new AdwordsReport("keywordPerformance", new String[]{"CampaignId", "AccountDescriptiveName", "CampaignName", "Impressions", "Clicks", "AdGroupId", "AdGroupName",
                    "Conversions", "AveragePosition", "AllConversions", "AdGroupName", "AdGroupId", "FirstPositionCpc", "TopOfPageCpc", "Criteria",
                    "AverageCpc", "Ctr", "Cost", "CostPerConversion", "ConversionRate", "ExternalCustomerId",
                    "CpcBid", "BidType", "QualityScore", "CriteriaDestinationUrl", "FinalUrls", "FinalMobileUrls", "KeywordMatchType", "FirstPageCpc"},
                        ReportDefinitionReportType.KEYWORDS_PERFORMANCE_REPORT));

        adwordsReports.put("adPerformance",
                new AdwordsReport("adPerformance", new String[]{"CampaignId", "AccountDescriptiveName", "CampaignName", "Impressions", "Clicks", "AdGroupId", "AdGroupName",
                    "ImageAdUrl", "Conversions", "AveragePosition", "AllConversions", "AdGroupName", "AdGroupId",
                    "Headline", "HeadlinePart1", "HeadlinePart2", "ShortHeadline", "LongHeadline",
                    "Path1", "Path2", "AdType", "Description", "Description1", "Description2", "DisplayUrl", "CreativeFinalUrls", "CreativeFinalMobileUrls",
                    "CreativeDestinationUrl", "AverageCpc", "Ctr", "Cost", "CostPerConversion", "ConversionRate", "ExternalCustomerId"},
                        ReportDefinitionReportType.AD_PERFORMANCE_REPORT));

        adwordsReports.put("geoPerformance",
                new AdwordsReport("geoPerformance", new String[]{"AccountDescriptiveName", "Impressions", "Clicks",
                    "Conversions", "AveragePosition", "AllConversions", "VideoViewRate", "VideoViews", "CountryCriteriaId",
                    "AverageCpc", "Ctr", "Cost", "CostPerConversion", "ConversionRate", "ExternalCustomerId"},
                        ReportDefinitionReportType.GEO_PERFORMANCE_REPORT));

        adwordsReports.put("videoPerformance",
                new AdwordsReport("videoPerformance", new String[]{"AccountDescriptiveName", "Impressions", "Clicks", "AdGroupId", "AdGroupName",
                    "CampaignId", "CampaignName", "CreativeId", "Engagements", "EngagementRate", "Conversions",
                    "Ctr", "Cost", "CostPerConversion", "ConversionRate", "ExternalCustomerId",
                    "VideoId", "VideoTitle", "VideoViews", "VideoViewRate", "VideoQuartile100Rate", "VideoQuartile25Rate", "VideoQuartile50Rate", "VideoQuartile75Rate"},
                        ReportDefinitionReportType.VIDEO_PERFORMANCE_REPORT));

        adwordsReports.put("placementReport",
                new AdwordsReport("placementReport", new String[]{"AccountDescriptiveName", "CampaignId", "CampaignName", "Impressions", "Clicks",
                    "Conversions", "AllConversions", "AdGroupName", "AdGroupId", "AverageCpc", "Ctr", "Cost", "CostPerConversion",
                    "ConversionRate", "ExternalCustomerId", "CriteriaDestinationUrl", "Criteria", "FinalUrls", "FinalMobileUrls"},
                        ReportDefinitionReportType.PLACEMENT_PERFORMANCE_REPORT));
        adwordsReports.put("finalUrlReport",
                new AdwordsReport("finalUrlReport", new String[]{"AccountDescriptiveName", "CampaignId", "CampaignName", "Impressions", "Clicks",
                    "Conversions", "AveragePosition", "AllConversions", "AdGroupName", "AdGroupId", "AverageCpc", "Ctr", "Cost", "CostPerConversion",
                    "ConversionRate", "ExternalCustomerId", "CriteriaTypeName", "EffectiveFinalUrl"},
                        ReportDefinitionReportType.FINAL_URL_REPORT));
        adwordsReports.put("searchQueryReport",
                new AdwordsReport("searchQueryReport", new String[]{"AccountDescriptiveName", "CampaignId", "CampaignName", "Impressions", "Clicks", "AdGroupId", "AdGroupName",
                    "Conversions", "AveragePosition", "AllConversions", "AdGroupName", "AdGroupId", "AverageCpc", "Ctr", "Cost", "CostPerConversion",
                    "ConversionRate", "ExternalCustomerId", "DestinationUrl", "FinalUrl", "KeywordTextMatchingQuery", "KeywordId", "Query","QueryMatchTypeWithVariant"},
                        ReportDefinitionReportType.SEARCH_QUERY_PERFORMANCE_REPORT));

        return adwordsReports;
    }

    public static Map<String, GaReport> getAllGaReports() {
        Map<String, GaReport> gaReports = new HashMap<>();
        gaReports.put("overallPerformance",
                new GaReport("overallPerformance", "ga:visits,visits;ga:sessions,sessions;ga:percentNewSessions,percentNewSessions;ga:pageViews,pageViews;ga:exitRate,exitRate;ga:bounceRate,bounceRate;ga:avgTimeOnPage,avgTimeOnPage;ga:users,users;ga:newUsers,newUsers;ga:sessionDuration,sessionDuration",
                        null, null));
        gaReports.put("referralReport",
                new GaReport("referralReport", "ga:visits,visits;ga:sessions,sessions;ga:percentNewSessions,percentNewSessions;ga:pageViews,pageViews;ga:exitRate,exitRate;ga:bounceRate,bounceRate;ga:avgTimeOnPage,avgTimeOnPage;ga:users,users;ga:newUsers,newUsers;ga:avgSessionDuration,avgSessionDuration",
                        "ga:fullReferrer", null));
        gaReports.put("pagePerformance",
                new GaReport("pagePerformance", "ga:visits,visits;ga:sessions,sessions;ga:percentNewSessions,percentNewSessions;ga:pageViews,pageViews;ga:exitRate,exitRate;ga:bounceRate,bounceRate;ga:avgTimeOnPage,avgTimeOnPage;ga:users,users;ga:newUsers,newUsers;ga:avgSessionDuration,avgSessionDuration",
                        "ga:pagePath", null));
        gaReports.put("visitorsTypeReport",
                new GaReport("visitorsTypeReport", "ga:visits,visits;ga:sessions,sessions;ga:percentNewSessions,percentNewSessions;ga:pageViews,pageViews;ga:exitRate,exitRate;ga:bounceRate,bounceRate;ga:avgTimeOnPage,avgTimeOnPage;ga:users,users;ga:newUsers,newUsers;ga:avgSessionDuration,avgSessionDuration",
                        "ga:userType", null));
        gaReports.put("frequencyReport",
                new GaReport("frequencyReport", "ga:visits,visits;ga:sessions,sessions;ga:percentNewSessions,percentNewSessions;ga:pageViews,pageViews;ga:exitRate,exitRate;ga:bounceRate,bounceRate;ga:avgTimeOnPage,avgTimeOnPage;ga:users,users;ga:newUsers,newUsers;ga:avgSessionDuration,avgSessionDuration",
                        "ga:sessionCount", null));
        gaReports.put("goalsReport",
                new GaReport("goalsReport", "ga:sessions,sessions;ga:bounceRate,bounceRate;ga:percentNewSessions,percentNewSessions;ga:avgTimeOnPage,avgTimeOnPage;ga:goal1Completions,goals1;ga:goal2Completions,goals2;ga:goal3Completions,goals3;ga:goal4Completions,goals4;ga:goal5Completions,goals5;ga:goal6Completions,goals6",
                        null, null));
        gaReports.put("pageGoalsReport",
                new GaReport("pageGoalsReport", "ga:sessions,sessions;ga:bounceRate,bounceRate;ga:avgTimeOnPage,avgTimeOnPage;ga:goal1Completions,goals1;ga:goal2Completions,goals2;ga:goal3Completions,goals3;ga:goal4Completions,goals4;ga:goal5Completions,goals5;ga:goal6Completions,goals6",
                        "ga:pagePath", null));
        gaReports.put("eventsReport",
                new GaReport("eventsReport", "ga:totalEvents,totalEvents;ga:uniqueEvents,uniqueEvents;ga:eventValue,eventValue;ga:avgEventValue,avgEventValue", "ga:eventCategory", null));
        
        return gaReports;
    }

}
