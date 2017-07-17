/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.admin.service;

import com.google.api.ads.adwords.lib.client.AdWordsSession;
import com.google.api.ads.common.lib.auth.OfflineCredentials;
import com.google.api.ads.common.lib.exception.OAuthException;
import com.google.api.ads.common.lib.exception.ValidationException;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.ads.adwords.lib.client.reporting.ReportingConfiguration;
import com.google.api.ads.adwords.lib.jaxb.v201609.DateRange;
import com.google.api.ads.adwords.lib.jaxb.v201609.DownloadFormat;
import com.google.api.ads.adwords.lib.jaxb.v201609.Predicate;
import com.google.api.ads.adwords.lib.jaxb.v201609.PredicateOperator;
import com.google.api.ads.adwords.lib.jaxb.v201609.ReportDefinition;
import com.google.api.ads.adwords.lib.jaxb.v201609.ReportDefinitionDateRangeType;
import com.google.api.ads.adwords.lib.jaxb.v201609.ReportDefinitionReportType;
import com.google.api.ads.adwords.lib.utils.ReportDownloadResponse;
import com.google.api.ads.adwords.lib.utils.ReportDownloadResponseException;
import com.google.api.ads.adwords.lib.utils.ReportException;
import com.google.api.ads.adwords.lib.utils.v201609.ReportDownloader;
import com.google.common.collect.Lists;
import com.google.api.ads.adwords.lib.jaxb.v201609.Selector;
import com.visumbu.api.adwords.report.xml.bean.AccountReport;
import com.visumbu.api.adwords.report.xml.bean.AccountReportRow;
import com.visumbu.api.adwords.report.xml.bean.AdGroupReportRow;
import com.visumbu.api.adwords.report.xml.bean.AdReport;
import com.visumbu.api.adwords.report.xml.bean.AdReportRow;
import com.visumbu.api.adwords.report.xml.bean.AddGroupReport;
import com.visumbu.api.adwords.report.xml.bean.CampaignReport;
import com.visumbu.api.adwords.report.xml.bean.CampaignReportRow;
import com.visumbu.api.adwords.report.xml.bean.GeoReport;
import com.visumbu.api.adwords.report.xml.bean.GeoReportRow;
import com.visumbu.api.adwords.report.xml.bean.VideoReport;
import com.visumbu.api.adwords.report.xml.bean.VideoReportRow;
import com.visumbu.vb.bean.AdwordsReport;
import com.visumbu.vb.utils.ApiUtils;
import com.visumbu.vb.utils.DateUtils;
import com.visumbu.vb.utils.FileReader;
import com.visumbu.vb.utils.XmlUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang3.RandomStringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.visumbu.vb.admin.dao.SettingsDao;
import com.visumbu.vb.model.Settings;
import com.visumbu.vb.utils.SettingsProperty;

/**
 *
 * @author jp
 */
@Service("adwordsService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class AdwordsService {

    @Autowired
    private SettingsDao settingsDao;

    private static final int PAGE_SIZE = 100;
    public static final String XML_FILE_DIR = "/tmp/";
    //private static String clientId = "162577857765-r9dvqjb6i7atjvjftdc8dq5pp80n8j2g.apps.googleusercontent.com";
//    
    //private static String clientSecret = "UxF3VNJFWfNBEQ86reUTk09M";

    //private static String refreshToken = "1/75VMEAe7i9UOm69maPpsPMaYH1e58R1xUGlulN--3Pg";
//    private static String refreshToken = "1/0u_BRufKySoAJwzSUaN6YDkpJiCTZ-swHBp-TMi_HYjPuJ9BIEFVAKhwewnySfK3";
    //private static String developerToken = "X4glgfA7zjlwzeL3jNQjkw";
//    private static String developerToken = "I-Ivbh_yb3mE7O8L7KRTFA";
    // -- Latest token details
    //latest developer token
//    private static String developerToken = "zA56sMrpDz_1Hrc_AbRwyA";
    //latest client scret
//      private static String clientSecret = "Z144G8Oqa15m93REF0k5z-vJ"; //new one
    //latest referesh token below
//    private static String refreshToken = "1/i4W9G31Xej2OklH_wihE8AxcDDqd9UeH4Vsi6eJaQshIGy3zvEMB0svvFz4NOj-b";
    //latest client id
//    private static String clientId = "117202177181-dfpvmamitf7acnn8ginmp5d0e813u584.apps.googleusercontent.com";
    private AdWordsSession getSession(String accountId) {
        try {

            //code to get access token from settings
            List<Settings> adwordsClientId = settingsDao.getProperty("adwordsClientId");
            String clientId = SettingsProperty.getSettingsProperty(adwordsClientId, "adwordsClientId");

            //adwords client secret
            List<Settings> adwordsClientSecret = settingsDao.getProperty("adwordsClientSecret");
            String clientSecret = SettingsProperty.getSettingsProperty(adwordsClientSecret, "adwordsClientSecret");

            //adwords developer token
            List<Settings> adwordsDeveloperToken = settingsDao.getProperty("adwordsDeveloperToken");
            String developerToken = SettingsProperty.getSettingsProperty(adwordsDeveloperToken, "adwordsDeveloperToken");

            //adwords referesh token
            List<Settings> adwordsRefreshToken = settingsDao.getProperty("adwordsRefreshToken");
            String refreshToken = SettingsProperty.getSettingsProperty(adwordsRefreshToken, "adwordsRefreshToken");
            
            System.out.println("Adword Refresh Token-->" + refreshToken);

            Credential credential = new OfflineCredentials.Builder()
                    .forApi(OfflineCredentials.Api.ADWORDS)
                    .withClientSecrets(clientId, clientSecret)
                    .withRefreshToken(refreshToken)
                    .build()
                    .generateCredential();

            // Construct an AdWordsSession.
            AdWordsSession session = new AdWordsSession.Builder()
                    .withDeveloperToken(developerToken)
                    .withClientCustomerId(accountId)
                    //.withClientCustomerId("581-484-4675")
                    // ...
                    .withOAuth2Credential(credential)
                    .build();

            return session;
        } catch (ValidationException ex) {
            Logger.getLogger(AdwordsService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (OAuthException ex) {
            Logger.getLogger(AdwordsService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public List<Map<String, Object>> get(String dataSetReportName, String adwordsAccountId, Date startDate,
            Date endDate, String aggregation, String productSegment, String filters) {

        List<Settings> adwordsClientId = settingsDao.getProperty("adwordsClientId");
        String clientId = SettingsProperty.getSettingsProperty(adwordsClientId, "adwordsClientId");

        if (aggregation == null) {
            aggregation = "";
        }
        if (productSegment.equalsIgnoreCase("none")) {
            productSegment = null;
        }
        System.out.println("Report Name ==> " + dataSetReportName);
        System.out.println("Adwords Account Id ==> " + adwordsAccountId);
        return getAdwordsReport(dataSetReportName, startDate, endDate, clientId, productSegment, productSegment, filters);
//        if (dataSetReportName.equalsIgnoreCase("accountPerformance")) {
//            AccountReport accountReport = getAccountReport(startDate, endDate, adwordsAccountId, aggregation, productSegment, filters);
//            List<AccountReportRow> accountReportRow = accountReport.getAccountReportRow();
//            return toMap(accountReportRow);
//        } else if (dataSetReportName.equalsIgnoreCase("campaignPerformance")) {
//            CampaignReport campaignReport = getCampaignReport(startDate, endDate, adwordsAccountId, aggregation, productSegment, filters);
//            List<CampaignReportRow> campaignReportRow = campaignReport.getCampaignReportRow();
//            return toMap(campaignReportRow);
//        } else if (dataSetReportName.equalsIgnoreCase("adGroupPerformance")) {
//            AddGroupReport adGroupReport = getAdGroupReport(startDate, endDate, adwordsAccountId, aggregation, productSegment, filters);
//            List<AdGroupReportRow> adGroupReportRow = adGroupReport.getAdGroupReportRow();
//            return toMap(adGroupReportRow);
//        } else if (dataSetReportName.equalsIgnoreCase("keywordPerformance")) {
//            return null;
//        } else if (dataSetReportName.equalsIgnoreCase("adPerformance")) {
//            AdReport adReport = getAdReport(startDate, endDate, adwordsAccountId, aggregation, productSegment, filters);
//            List<AdReportRow> adReportRow = adReport.getAdReportRow();
//            return toMap(adReportRow);
//        } else if (dataSetReportName.equalsIgnoreCase("geoPerformance")) {
//            GeoReport geoReport = getGeoReport(startDate, endDate, adwordsAccountId, aggregation, productSegment, filters);
//            List<GeoReportRow> geoReportRow = geoReport.getGeoReportRow();
//            return toMap(geoReportRow);
//        } else if (dataSetReportName.equalsIgnoreCase("videoPerformance")) {
//            VideoReport videoReport = getVideoReport(startDate, endDate, adwordsAccountId, aggregation, productSegment, filters);
//            List<VideoReportRow> videoReportRow = videoReport.getVideoReportRow();
//            return toMap(videoReportRow);
//        }
//        return null;
    }

    private List<Map<String, String>> toMap(List data) {
        List<Map<String, String>> returnMap = new ArrayList<>();
        for (Iterator iterator = data.iterator(); iterator.hasNext();) {
            Object dataObject = iterator.next();
            ObjectMapper mapper = new ObjectMapper();
            Map<String, String> map
                    = mapper.convertValue(dataObject, new TypeReference<Map<String, String>>() {
                    });
            returnMap.add(map);
        }
        return returnMap;
    }

    public String getTimeSegment(String timeSegment) {
        if (timeSegment == null) {
            return null;
        }
        String aggregationDuration = null;
        if (timeSegment.equalsIgnoreCase("day")) {
            aggregationDuration = "Date";
        } else if (timeSegment.equalsIgnoreCase("week")) {
            aggregationDuration = "Week";
        } else if (timeSegment.equalsIgnoreCase("month")) {
            aggregationDuration = "Month";
        } else if (timeSegment.equalsIgnoreCase("quarter")) {
            aggregationDuration = "Quarter";
        } else if (timeSegment.equalsIgnoreCase("year")) {
            aggregationDuration = "Year";
        } else if (timeSegment.equalsIgnoreCase("dayOfWeek")) {
            aggregationDuration = "DayOfWeek";
        } else if (timeSegment.equalsIgnoreCase("hourOfDay")) {
            aggregationDuration = "HourOfDay";
        }
        return aggregationDuration;
    }

    public String getProductSegment(String productSegment) {
        if (productSegment == null) {
            return null;
        }
        String productSegmentValue = null;
        if (productSegment.equalsIgnoreCase("device")) {
            productSegmentValue = "Device";
        } else if (productSegment.equalsIgnoreCase("network search partner")) {
            productSegmentValue = "AdNetworkType1";
        } else if (productSegment.equalsIgnoreCase("region")) {
            productSegmentValue = "RegionCriteriaId,CountryCriteriaId";
        } else if (productSegment.equalsIgnoreCase("city")) {
            productSegmentValue = "CityCriteriaId,RegionCriteriaId,CountryCriteriaId";
        } else if (productSegment.equalsIgnoreCase("country")) {
            productSegmentValue = "CountryCriteriaId";
        } else if (productSegment.equalsIgnoreCase("metro")) {
            productSegmentValue = "MetroCriteriaId,CityCriteriaId,RegionCriteriaId,CountryCriteriaId";
        } else if (productSegment.equalsIgnoreCase("zip")) {
            productSegmentValue = "MostSpecificCriteriaId,MetroCriteriaId,CityCriteriaId,RegionCriteriaId,CountryCriteriaId";
        } else if (productSegment.equalsIgnoreCase("region device")) {
            productSegmentValue = "Device,RegionCriteriaId,CountryCriteriaId";
        } else if (productSegment.equalsIgnoreCase("city device")) {
            productSegmentValue = "Device,CityCriteriaId,RegionCriteriaId,CountryCriteriaId";
        } else if (productSegment.equalsIgnoreCase("country device")) {
            productSegmentValue = "Device,CountryCriteriaId";
        } else if (productSegment.equalsIgnoreCase("metro device")) {
            productSegmentValue = "Device,MetroCriteriaId,CityCriteriaId,RegionCriteriaId,CountryCriteriaId";
        } else if (productSegment.equalsIgnoreCase("zip device")) {
            productSegmentValue = "Device,MostSpecificCriteriaId,MetroCriteriaId,CityCriteriaId,RegionCriteriaId,CountryCriteriaId";
        }
        return productSegmentValue;
    }

    public Selector addTimeSelector(Selector selector, String timeSegment) {
        String timeSegmentValue = timeSegment; //getTimeSegment(timeSegment);
        if (timeSegmentValue != null) {
            String[] timeSegmentArr = timeSegmentValue.split(",");
            for (int i = 0; i < timeSegmentArr.length; i++) {
                String timeSegmentData = timeSegmentArr[i];
                selector.getFields().add(timeSegmentData);
            }
        }

        return selector;
    }

    public Selector addProductSelector(Selector selector, String productSegment) {

        String productSegmentValue = productSegment; //getProductSegment(productSegment);
        if (productSegmentValue != null) {
            String[] productSegmentArr = productSegmentValue.split(",");
            for (int i = 0; i < productSegmentArr.length; i++) {
                String productSegmentName = productSegmentArr[i];
                selector.getFields().add(productSegmentName);
            }
        }
        System.out.println("ADDED FIELDS - Product Segments " + selector.getFields());
        return selector;
    }

    public Selector setAdNetworkTypeFilters(Selector selector, String filters) {
        if (filters == null) {
            return selector;
        }
        String[] filterArr = filters.split(",");
        for (int i = 0; i < filterArr.length; i++) {
            String filter = filterArr[i];
            final Predicate predicate = new Predicate();
            predicate.setField("AdNetworkType1");
            predicate.setOperator(PredicateOperator.IN);
            predicate.getValues().add(filter);
            final Collection<Predicate> predicates = new ArrayList<>();
            predicates.add(predicate);
            selector.getPredicates().add(predicate);
        }
        return selector;
    }

//    public CampaignReport getCampaignReport(Date startDate, Date endDate, String accountId, String timeSegment, String productSegment, String filter) {
//        AdWordsSession session = getSession(accountId);
//        Selector selector = new Selector();
//        selector.getFields().addAll(Lists.newArrayList("CampaignId", "AccountDescriptiveName", "CampaignName",
//                "Impressions", "Clicks", "Amount",
//                "SearchImpressionShare", "SearchExactMatchImpressionShare", "SearchBudgetLostImpressionShare", "SearchRankLostImpressionShare",
//                "ContentImpressionShare", "ContentBudgetLostImpressionShare", "ContentRankLostImpressionShare",
//                "Conversions", "AveragePosition", "AllConversions",
//                "AverageCpc", "Ctr", "Cost", "CostPerConversion", "ConversionRate", "ExternalCustomerId"
//        ));
//
//        addTimeSelector(selector, timeSegment);
//        addProductSelector(selector, productSegment);
//        setAdNetworkTypeFilters(selector, filter);
//
//        // Create report definition.
//        ReportDefinition reportDefinition = new ReportDefinition();
//        reportDefinition.setReportName("Criteria performance report #" + System.currentTimeMillis());
//        reportDefinition.setDateRangeType(ReportDefinitionDateRangeType.CUSTOM_DATE);
//        DateRange dateRange = new DateRange();
//        dateRange.setMin(DateUtils.getAdWordsStartDate(startDate));
//        dateRange.setMax(DateUtils.getAdWordsEndDate(endDate));
//        selector.setDateRange(dateRange);
//        reportDefinition.setReportType(ReportDefinitionReportType.CAMPAIGN_PERFORMANCE_REPORT);
//        reportDefinition.setDownloadFormat(DownloadFormat.XML);
//
//        // Optional: Set the reporting configuration of the session to suppress header, column name, or
//        // summary rows in the report output. You can also configure this via your ads.properties
//        // configuration file. See AdWordsSession.Builder.from(Configuration) for details.
//        // In addition, you can set whether you want to explicitly include or exclude zero impression
//        // rows.
//        ReportingConfiguration reportingConfiguration
//                = new ReportingConfiguration.Builder()
//                .skipReportHeader(true)
//                .skipColumnHeader(true)
//                .skipReportSummary(true)
//                // Enable to allow rows with zero impressions to show.
//                .includeZeroImpressions(false)
//                .build();
//        session.setReportingConfiguration(reportingConfiguration);
//
//        reportDefinition.setSelector(selector);
//
//        try {
//            String filename = XML_FILE_DIR + "adwords-" + RandomStringUtils.randomAlphanumeric(32).toUpperCase() + ".xml";
//            // Set the property api.adwords.reportDownloadTimeout or call
//            // ReportDownloader.setReportDownloadTimeout to set a timeout (in milliseconds)
//            // for CONNECT and READ in report downloads.
//            ReportDownloadResponse response
//                    = new ReportDownloader(session).downloadReport(reportDefinition);
//            //BufferedReader reader = new BufferedReader(new InputStreamReader(response.getInputStream(), Charsets.UTF_8));
//            response.saveToFile(filename);
//
//            CampaignReport report = (CampaignReport) FileReader.readXML(filename, CampaignReport.class);
//            System.out.println(report);
//            System.out.printf("Report successfully downloaded to: %s%n", filename);
//            return report;
//        } catch (ReportDownloadResponseException e) {
//            System.out.printf("Report was not downloaded due to: %s%n", e);
//        } catch (ReportException ex) {
//            Logger.getLogger(AdwordsService.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (IOException ex) {
//            Logger.getLogger(AdwordsService.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return null;
//    }
//
//    public AccountReport getAccountReport(Date startDate, Date endDate, String accountId, String timeSegment, String productSegment, String filter) {
//        AdWordsSession session = getSession(accountId);
//        Selector selector = new Selector();
//        selector.getFields().addAll(Lists.newArrayList("AccountDescriptiveName", "Impressions", "Clicks",
//                "SearchImpressionShare", "SearchExactMatchImpressionShare", "SearchBudgetLostImpressionShare", "SearchRankLostImpressionShare",
//                "ContentImpressionShare", "ContentBudgetLostImpressionShare", "ContentRankLostImpressionShare",
//                "Conversions", "AveragePosition", "AllConversions",
//                "AverageCpc", "Ctr", "Cost", "CostPerConversion", "ConversionRate", "ExternalCustomerId"
//        ));
//
//        addTimeSelector(selector, timeSegment);
//        addProductSelector(selector, productSegment);
//        setAdNetworkTypeFilters(selector, filter);
//
//        // Create report definition.
//        ReportDefinition reportDefinition = new ReportDefinition();
//        reportDefinition.setReportName("Criteria performance report #" + System.currentTimeMillis());
//        reportDefinition.setDateRangeType(ReportDefinitionDateRangeType.CUSTOM_DATE);
//        DateRange dateRange = new DateRange();
//        dateRange.setMin(DateUtils.getAdWordsStartDate(startDate));
//        dateRange.setMax(DateUtils.getAdWordsEndDate(endDate));
//        selector.setDateRange(dateRange);
//        reportDefinition.setReportType(ReportDefinitionReportType.ACCOUNT_PERFORMANCE_REPORT);
//        reportDefinition.setDownloadFormat(DownloadFormat.XML);
//
//        // Optional: Set the reporting configuration of the session to suppress header, column name, or
//        // summary rows in the report output. You can also configure this via your ads.properties
//        // configuration file. See AdWordsSession.Builder.from(Configuration) for details.
//        // In addition, you can set whether you want to explicitly include or exclude zero impression
//        // rows.
//        ReportingConfiguration reportingConfiguration
//                = new ReportingConfiguration.Builder()
//                .skipReportHeader(true)
//                .skipColumnHeader(true)
//                .skipReportSummary(true)
//                // Enable to allow rows with zero impressions to show.
//                .includeZeroImpressions(false)
//                .build();
//        session.setReportingConfiguration(reportingConfiguration);
//
//        reportDefinition.setSelector(selector);
//
//        try {
//            String filename = XML_FILE_DIR + "adwords-" + RandomStringUtils.randomAlphanumeric(32).toUpperCase() + ".xml";
//            // Set the property api.adwords.reportDownloadTimeout or call
//            // ReportDownloader.setReportDownloadTimeout to set a timeout (in milliseconds)
//            // for CONNECT and READ in report downloads.
//            ReportDownloadResponse response
//                    = new ReportDownloader(session).downloadReport(reportDefinition);
//            //BufferedReader reader = new BufferedReader(new InputStreamReader(response.getInputStream(), Charsets.UTF_8));
//            response.saveToFile(filename);
//
//            AccountReport report = (AccountReport) FileReader.readXML(filename, AccountReport.class);
//            System.out.println(report);
//            System.out.printf("Report successfully downloaded to: %s%n", filename);
//            return report;
//        } catch (ReportDownloadResponseException e) {
//            System.out.printf("Report was not downloaded due to: %s%n", e);
//        } catch (ReportException ex) {
//            Logger.getLogger(AdwordsService.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (IOException ex) {
//            Logger.getLogger(AdwordsService.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return null;
//    }
//
//    public VideoReport getVideoReport(Date startDate, Date endDate, String accountId, String timeSegment, String productSegment, String filters) {
//        AdWordsSession session = getSession(accountId);
//        Selector selector = new Selector();
//        selector.getFields().addAll(Lists.newArrayList("AccountDescriptiveName", "Impressions", "Clicks", "AdGroupId", "AdGroupName",
//                "CampaignId", "CampaignName", "CreativeId", "Engagements", "EngagementRate",
//                "Conversions", "AveragePosition", "AllConversions", "AverageCpc", "Ctr", "Cost", "CostPerConversion", "ConversionRate", "ExternalCustomerId",
//                "VideoId", "VideoTitle", "VideoViews", "VideoViewRate", "VideoQuartile100Rate", "VideoQuartile25Rate", "VideoQuartile50Rate", "VideoQuartile75Rate"
//        ));
//
//        addTimeSelector(selector, timeSegment);
//        addProductSelector(selector, productSegment);
//        setAdNetworkTypeFilters(selector, filters);
//
//        // Create report definition.
//        ReportDefinition reportDefinition = new ReportDefinition();
//        reportDefinition.setReportName("Criteria performance report #" + System.currentTimeMillis());
//        reportDefinition.setDateRangeType(ReportDefinitionDateRangeType.CUSTOM_DATE);
//        DateRange dateRange = new DateRange();
//        dateRange.setMin(DateUtils.getAdWordsStartDate(startDate));
//        dateRange.setMax(DateUtils.getAdWordsEndDate(endDate));
//        selector.setDateRange(dateRange);
//        reportDefinition.setReportType(ReportDefinitionReportType.VIDEO_PERFORMANCE_REPORT);
//        reportDefinition.setDownloadFormat(DownloadFormat.XML);
//
//        // Optional: Set the reporting configuration of the session to suppress header, column name, or
//        // summary rows in the report output. You can also configure this via your ads.properties
//        // configuration file. See AdWordsSession.Builder.from(Configuration) for details.
//        // In addition, you can set whether you want to explicitly include or exclude zero impression
//        // rows.
//        ReportingConfiguration reportingConfiguration
//                = new ReportingConfiguration.Builder()
//                .skipReportHeader(true)
//                .skipColumnHeader(true)
//                .skipReportSummary(true)
//                // Enable to allow rows with zero impressions to show.
//                .includeZeroImpressions(false)
//                .build();
//        session.setReportingConfiguration(reportingConfiguration);
//
//        reportDefinition.setSelector(selector);
//
//        try {
//            String filename = XML_FILE_DIR + "adwords-" + RandomStringUtils.randomAlphanumeric(32).toUpperCase() + ".xml";
//            // Set the property api.adwords.reportDownloadTimeout or call
//            // ReportDownloader.setReportDownloadTimeout to set a timeout (in milliseconds)
//            // for CONNECT and READ in report downloads.
//            ReportDownloadResponse response
//                    = new ReportDownloader(session).downloadReport(reportDefinition);
//            //BufferedReader reader = new BufferedReader(new InputStreamReader(response.getInputStream(), Charsets.UTF_8));
//            response.saveToFile(filename);
//
//            VideoReport report = (VideoReport) FileReader.readXML(filename, VideoReport.class);
//            System.out.println(report);
//            System.out.printf("Report successfully downloaded to: %s%n", filename);
//            return report;
//        } catch (ReportDownloadResponseException e) {
//            System.out.printf("Report was not downloaded due to: %s%n", e);
//        } catch (ReportException ex) {
//            Logger.getLogger(AdwordsService.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (IOException ex) {
//            Logger.getLogger(AdwordsService.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return null;
//    }
//    public AddGroupReport getAdGroupReport(Date startDate, Date endDate, String accountId, String timeSegment, String productSegment, String filter) {
//        AdWordsSession session = getSession(accountId);
//        Selector selector = new Selector();
//        selector.getFields().addAll(Lists.newArrayList("AccountDescriptiveName", "Impressions", "Clicks", "AdGroupId", "AdGroupName",
//                "SearchImpressionShare", "SearchExactMatchImpressionShare", "SearchBudgetLostImpressionShare", "SearchRankLostImpressionShare",
//                "ContentImpressionShare", "ContentBudgetLostImpressionShare", "ContentRankLostImpressionShare",
//                "Conversions", "AveragePosition", "AllConversions",
//                "AverageCpc", "Ctr", "Cost", "CostPerConversion", "ConversionRate", "ExternalCustomerId"
//        ));
//
//        addTimeSelector(selector, timeSegment);
//        addProductSelector(selector, productSegment);
//        setAdNetworkTypeFilters(selector, filter);
//
//        // Create report definition.
//        ReportDefinition reportDefinition = new ReportDefinition();
//        reportDefinition.setReportName("Criteria performance report #" + System.currentTimeMillis());
//        reportDefinition.setDateRangeType(ReportDefinitionDateRangeType.CUSTOM_DATE);
//        DateRange dateRange = new DateRange();
//        dateRange.setMin(DateUtils.getAdWordsStartDate(startDate));
//        dateRange.setMax(DateUtils.getAdWordsEndDate(endDate));
//        selector.setDateRange(dateRange);
//        reportDefinition.setReportType(ReportDefinitionReportType.ADGROUP_PERFORMANCE_REPORT);
//        reportDefinition.setDownloadFormat(DownloadFormat.XML);
//
//        // Optional: Set the reporting configuration of the session to suppress header, column name, or
//        // summary rows in the report output. You can also configure this via your ads.properties
//        // configuration file. See AdWordsSession.Builder.from(Configuration) for details.
//        // In addition, you can set whether you want to explicitly include or exclude zero impression
//        // rows.
//        ReportingConfiguration reportingConfiguration
//                = new ReportingConfiguration.Builder()
//                .skipReportHeader(true)
//                .skipColumnHeader(true)
//                .skipReportSummary(true)
//                // Enable to allow rows with zero impressions to show.
//                .includeZeroImpressions(false)
//                .build();
//        session.setReportingConfiguration(reportingConfiguration);
//
//        reportDefinition.setSelector(selector);
//
//        try {
//            String filename = XML_FILE_DIR + "adwords-" + RandomStringUtils.randomAlphanumeric(32).toUpperCase() + ".xml";
//            // Set the property api.adwords.reportDownloadTimeout or call
//            // ReportDownloader.setReportDownloadTimeout to set a timeout (in milliseconds)
//            // for CONNECT and READ in report downloads.
//            ReportDownloadResponse response
//                    = new ReportDownloader(session).downloadReport(reportDefinition);
//            //BufferedReader reader = new BufferedReader(new InputStreamReader(response.getInputStream(), Charsets.UTF_8));
//            response.saveToFile(filename);
//
//            AddGroupReport report = (AddGroupReport) FileReader.readXML(filename, AddGroupReport.class);
//            System.out.println(report);
//            System.out.printf("Report successfully downloaded to: %s%n", filename);
//            return report;
//        } catch (ReportDownloadResponseException e) {
//            System.out.printf("Report was not downloaded due to: %s%n", e);
//        } catch (ReportException ex) {
//            Logger.getLogger(AdwordsService.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (IOException ex) {
//            Logger.getLogger(AdwordsService.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return null;
//    }
//
//    public AdReport getAdReport(Date startDate, Date endDate, String accountId, String timeSegment, String productSegment, String filter) {
//        AdWordsSession session = getSession(accountId);
//        Selector selector = new Selector();
//        selector.getFields().addAll(Lists.newArrayList("CampaignId", "AccountDescriptiveName", "CampaignName", "Impressions", "Clicks", "AdGroupId", "AdGroupName",
//                "ImageAdUrl", "Conversions", "AveragePosition", "AllConversions", "AdGroupName", "AdGroupId",
//                "Headline", "HeadlinePart1", "HeadlinePart2", "ShortHeadline", "LongHeadline", "CpcBid",
//                "Path1", "Path2", "AdType", "Description", "Description1", "Description2", "DisplayUrl", "CreativeFinalUrls", "CreativeFinalMobileUrls",
//                "CreativeDestinationUrl", "AverageCpc", "Ctr", "Cost", "CostPerConversion", "ConversionRate", "ExternalCustomerId"
//        ));
//
//        addTimeSelector(selector, timeSegment);
//        addProductSelector(selector, productSegment);
//        setAdNetworkTypeFilters(selector, filter);
//
//        // Create report definition.
//        ReportDefinition reportDefinition = new ReportDefinition();
//        reportDefinition.setReportName("Criteria performance report #" + System.currentTimeMillis());
//        reportDefinition.setDateRangeType(ReportDefinitionDateRangeType.CUSTOM_DATE);
//        DateRange dateRange = new DateRange();
//        dateRange.setMin(DateUtils.getAdWordsStartDate(startDate));
//        dateRange.setMax(DateUtils.getAdWordsEndDate(endDate));
//        selector.setDateRange(dateRange);
//        reportDefinition.setReportType(ReportDefinitionReportType.AD_PERFORMANCE_REPORT);
//        reportDefinition.setDownloadFormat(DownloadFormat.XML);
//
//        // Optional: Set the reporting configuration of the session to suppress header, column name, or
//        // summary rows in the report output. You can also configure this via your ads.properties
//        // configuration file. See AdWordsSession.Builder.from(Configuration) for details.
//        // In addition, you can set whether you want to explicitly include or exclude zero impression
//        // rows.
//        ReportingConfiguration reportingConfiguration
//                = new ReportingConfiguration.Builder()
//                .skipReportHeader(true)
//                .skipColumnHeader(true)
//                .skipReportSummary(true)
//                // Enable to allow rows with zero impressions to show.
//                .includeZeroImpressions(false)
//                .build();
//        session.setReportingConfiguration(reportingConfiguration);
//
//        reportDefinition.setSelector(selector);
//
//        try {
//            String filename = XML_FILE_DIR + "adwords-" + RandomStringUtils.randomAlphanumeric(32).toUpperCase() + ".xml";
//            // Set the property api.adwords.reportDownloadTimeout or call
//            // ReportDownloader.setReportDownloadTimeout to set a timeout (in milliseconds)
//            // for CONNECT and READ in report downloads.
//            ReportDownloadResponse response
//                    = new ReportDownloader(session).downloadReport(reportDefinition);
//            //BufferedReader reader = new BufferedReader(new InputStreamReader(response.getInputStream(), Charsets.UTF_8));
//            response.saveToFile(filename);
//
//            AdReport report = (AdReport) FileReader.readXML(filename, AdReport.class);
//            System.out.println(report);
//            System.out.printf("Report successfully downloaded to: %s%n", filename);
//            return report;
//        } catch (ReportDownloadResponseException e) {
//            System.out.printf("Report was not downloaded due to: %s%n", e);
//        } catch (ReportException ex) {
//            Logger.getLogger(AdwordsService.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (IOException ex) {
//            Logger.getLogger(AdwordsService.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return null;
//    }
//
//    public GeoReport getGeoReport(Date startDate, Date endDate, String accountId, String timeSegment, String productSegment, String filter) {
//        AdWordsSession session = getSession(accountId);
//        Selector selector = new Selector();
//        selector.getFields().addAll(Lists.newArrayList("CampaignId", "AccountDescriptiveName", "CampaignName", "Impressions", "Clicks",
//                "Conversions", "AveragePosition", "AllConversions", "AdGroupName", "AdGroupId", "VideoViewRate", "VideoViews",
//                "AverageCpc", "Ctr", "Cost", "CostPerConversion", "ConversionRate", "ExternalCustomerId"
//        ));
//        addTimeSelector(selector, timeSegment);
//        addProductSelector(selector, productSegment);
//        setAdNetworkTypeFilters(selector, filter);
//
//        // Create report definition.
//        ReportDefinition reportDefinition = new ReportDefinition();
//        reportDefinition.setReportName("Criteria performance report #" + System.currentTimeMillis());
//        reportDefinition.setDateRangeType(ReportDefinitionDateRangeType.CUSTOM_DATE);
//        DateRange dateRange = new DateRange();
//        dateRange.setMin(DateUtils.getAdWordsStartDate(startDate));
//        dateRange.setMax(DateUtils.getAdWordsEndDate(endDate));
//        selector.setDateRange(dateRange);
//        reportDefinition.setReportType(ReportDefinitionReportType.GEO_PERFORMANCE_REPORT);
//        reportDefinition.setDownloadFormat(DownloadFormat.XML);
//
//        // Optional: Set the reporting configuration of the session to suppress header, column name, or
//        // summary rows in the report output. You can also configure this via your ads.properties
//        // configuration file. See AdWordsSession.Builder.from(Configuration) for details.
//        // In addition, you can set whether you want to explicitly include or exclude zero impression
//        // rows.
//        ReportingConfiguration reportingConfiguration
//                = new ReportingConfiguration.Builder()
//                .skipReportHeader(true)
//                .skipColumnHeader(true)
//                .skipReportSummary(true)
//                // Enable to allow rows with zero impressions to show.
//                .includeZeroImpressions(false)
//                .build();
//        session.setReportingConfiguration(reportingConfiguration);
//
//        reportDefinition.setSelector(selector);
//
//        try {
//            String filename = XML_FILE_DIR + "adwords-" + RandomStringUtils.randomAlphanumeric(32).toUpperCase() + ".xml";
//            // Set the property api.adwords.reportDownloadTimeout or call
//            // ReportDownloader.setReportDownloadTimeout to set a timeout (in milliseconds)
//            // for CONNECT and READ in report downloads.
//            ReportDownloadResponse response
//                    = new ReportDownloader(session).downloadReport(reportDefinition);
//            //BufferedReader reader = new BufferedReader(new InputStreamReader(response.getInputStream(), Charsets.UTF_8));
//            response.saveToFile(filename);
//
//            GeoReport report = (GeoReport) FileReader.readXML(filename, GeoReport.class);
//            System.out.println(report);
//            System.out.printf("Report successfully downloaded to: %s%n", filename);
//            return report;
//        } catch (ReportDownloadResponseException e) {
//            System.out.printf("Report was not downloaded due to: %s%n", e);
//        } catch (ReportException ex) {
//            Logger.getLogger(AdwordsService.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (IOException ex) {
//            Logger.getLogger(AdwordsService.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return null;
//    }
    public Object adWordsAsMap(Date startDate, Date endDate, String accountId, String[] fields, Map<String, String> filter, String aggregation, String reportType) {
        AdWordsSession session = getSession(accountId);
        com.google.api.ads.adwords.lib.jaxb.v201609.Selector selector = new com.google.api.ads.adwords.lib.jaxb.v201609.Selector();
        selector.getFields().addAll(Lists.newArrayList(fields));

        if (filter != null) {
            for (Map.Entry<String, String> entrySet : filter.entrySet()) {
                String filterName = entrySet.getKey();
                String filterValue = entrySet.getValue();
                final Predicate predicate = new Predicate();
                predicate.setField(filterName);
                predicate.setOperator(PredicateOperator.IN);
                predicate.getValues().add(filterValue);
                selector.getPredicates().add(predicate);
            }
        }
        // Create report definition.
        ReportDefinition reportDefinition = new ReportDefinition();
        reportDefinition.setReportName("Call Conversion report #" + System.currentTimeMillis());
        reportDefinition.setDateRangeType(ReportDefinitionDateRangeType.CUSTOM_DATE);
        DateRange dateRange = new DateRange();
        dateRange.setMin(DateUtils.getAdWordsStartDate(startDate));
        dateRange.setMax(DateUtils.getAdWordsEndDate(endDate));
        selector.setDateRange(dateRange);
        if (reportType.equalsIgnoreCase("campaign")) {
            reportDefinition.setReportType(ReportDefinitionReportType.CAMPAIGN_PERFORMANCE_REPORT);
        } else if (reportType.equalsIgnoreCase("account")) {
            reportDefinition.setReportType(ReportDefinitionReportType.ACCOUNT_PERFORMANCE_REPORT);
        } else {
            reportDefinition.setReportType(ReportDefinitionReportType.ACCOUNT_PERFORMANCE_REPORT);

        }
        reportDefinition.setDownloadFormat(DownloadFormat.XML);

        // Optional: Set the reporting configuration of the session to suppress header, column name, or
        // summary rows in the report output. You can also configure this via your ads.properties
        // configuration file. See AdWordsSession.Builder.from(Configuration) for details.
        // In addition, you can set whether you want to explicitly include or exclude zero impression
        // rows.
        ReportingConfiguration reportingConfiguration
                = new ReportingConfiguration.Builder()
                        .skipReportHeader(true)
                        .skipColumnHeader(true)
                        .skipReportSummary(true)
                        // Enable to allow rows with zero impressions to show.
                        .includeZeroImpressions(false)
                        .build();
        session.setReportingConfiguration(reportingConfiguration);

        reportDefinition.setSelector(selector);

        try {
            String filename = XML_FILE_DIR + "adwords-" + RandomStringUtils.randomAlphanumeric(32).toUpperCase() + ".xml";
            // Set the property api.adwords.reportDownloadTimeout or call
            // ReportDownloader.setReportDownloadTimeout to set a timeout (in milliseconds)
            // for CONNECT and READ in report downloads.
            ReportDownloadResponse response
                    = new ReportDownloader(session).downloadReport(reportDefinition);
            //BufferedReader reader = new BufferedReader(new InputStreamReader(response.getInputStream(), Charsets.UTF_8));
            response.saveToFile(filename);

            return XmlUtils.getAsMap(filename);

//            CallConversionReport report = (CallConversionReport) FileReader.readXML(filename, CallConversionReport.class);
//            System.out.println(report);
//            System.out.printf("Report successfully downloaded to: %s%n", filename);
//            return report;
        } catch (ReportDownloadResponseException e) {
            System.out.printf("Report was not downloaded due to: %s%n", e);
        } catch (ReportException ex) {
            Logger.getLogger(AdwordsService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(AdwordsService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
//
//    public AdReport getKeywordReport(Date startDate, Date endDate, String accountId, String timeSegment, String productSegment, String filter) {
//        AdWordsSession session = getSession(accountId);
//        Selector selector = new Selector();
//        selector.getFields().addAll(Lists.newArrayList("CampaignId", "AccountDescriptiveName", "CampaignName", "Impressions", "Clicks", "AdGroupId", "AdGroupName",
//                "Conversions", "AveragePosition", "AllConversions", "AdGroupName", "AdGroupId",
//                "AverageCpc", "Ctr", "Cost", "CostPerConversion", "ConversionRate", "ExternalCustomerId",
//                "CpcBid", "BidType", "QualityScore", "CriteriaDestinationUrl", "FinalUrls", "FinalMobileUrls", "KeywordMatchType", "FirstPageCpc",
//                "FirstPositionCpc", "TopOfPageCpc", "Criteria"
//        ));
//
//        addTimeSelector(selector, timeSegment);
//        addProductSelector(selector, productSegment);
//        setAdNetworkTypeFilters(selector, filter);
//
//        // Create report definition.
//        ReportDefinition reportDefinition = new ReportDefinition();
//        reportDefinition.setReportName("Criteria performance report #" + System.currentTimeMillis());
//        reportDefinition.setDateRangeType(ReportDefinitionDateRangeType.CUSTOM_DATE);
//        DateRange dateRange = new DateRange();
//        dateRange.setMin(DateUtils.getAdWordsStartDate(startDate));
//        dateRange.setMax(DateUtils.getAdWordsEndDate(endDate));
//        selector.setDateRange(dateRange);
//        reportDefinition.setReportType(ReportDefinitionReportType.KEYWORDS_PERFORMANCE_REPORT);
//        reportDefinition.setDownloadFormat(DownloadFormat.XML);
//
//        // Optional: Set the reporting configuration of the session to suppress header, column name, or
//        // summary rows in the report output. You can also configure this via your ads.properties
//        // configuration file. See AdWordsSession.Builder.from(Configuration) for details.
//        // In addition, you can set whether you want to explicitly include or exclude zero impression
//        // rows.
//        ReportingConfiguration reportingConfiguration
//                = new ReportingConfiguration.Builder()
//                .skipReportHeader(true)
//                .skipColumnHeader(true)
//                .skipReportSummary(true)
//                // Enable to allow rows with zero impressions to show.
//                .includeZeroImpressions(false)
//                .build();
//        session.setReportingConfiguration(reportingConfiguration);
//
//        reportDefinition.setSelector(selector);
//
//        try {
//            String filename = XML_FILE_DIR + "adwords-" + RandomStringUtils.randomAlphanumeric(32).toUpperCase() + ".xml";
//            // Set the property api.adwords.reportDownloadTimeout or call
//            // ReportDownloader.setReportDownloadTimeout to set a timeout (in milliseconds)
//            // for CONNECT and READ in report downloads.
//            ReportDownloadResponse response
//                    = new ReportDownloader(session).downloadReport(reportDefinition);
//            //BufferedReader reader = new BufferedReader(new InputStreamReader(response.getInputStream(), Charsets.UTF_8));
//            response.saveToFile(filename);
//
//            AdReport report = (AdReport) FileReader.readXML(filename, AdReport.class);
//            System.out.println(report);
//            System.out.printf("Report successfully downloaded to: %s%n", filename);
//            return report;
//        } catch (ReportDownloadResponseException e) {
//            System.out.printf("Report was not downloaded due to: %s%n", e);
//        } catch (ReportException ex) {
//            Logger.getLogger(AdwordsService.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (IOException ex) {
//            Logger.getLogger(AdwordsService.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return null;
//    }
//
//    public AdReport getSearchQueryReport(Date startDate, Date endDate, String accountId, String timeSegment, String productSegment, String filter) {
//        AdWordsSession session = getSession(accountId);
//        Selector selector = new Selector();
//        selector.getFields().addAll(Lists.newArrayList("AccountDescriptiveName", "CampaignId", "CampaignName", "Impressions", "Clicks", "AdGroupId", "AdGroupName",
//                "Conversions", "AveragePosition", "AllConversions", "AdGroupName", "AdGroupId",
//                "AverageCpc", "Ctr", "Cost", "CostPerConversion", "ConversionRate", "ExternalCustomerId",
//                "CpcBid", "BidType", "QualityScore", "DestinationUrl", "FinalUrl", "KeywordTextMatchingQuery", "KeywordId", "Query"
//        ));
//
//        addTimeSelector(selector, timeSegment);
//        addProductSelector(selector, productSegment);
//        setAdNetworkTypeFilters(selector, filter);
//
//        // Create report definition.
//        ReportDefinition reportDefinition = new ReportDefinition();
//        reportDefinition.setReportName("Criteria performance report #" + System.currentTimeMillis());
//        reportDefinition.setDateRangeType(ReportDefinitionDateRangeType.CUSTOM_DATE);
//        DateRange dateRange = new DateRange();
//        dateRange.setMin(DateUtils.getAdWordsStartDate(startDate));
//        dateRange.setMax(DateUtils.getAdWordsEndDate(endDate));
//        selector.setDateRange(dateRange);
//        reportDefinition.setReportType(ReportDefinitionReportType.SEARCH_QUERY_PERFORMANCE_REPORT);
//        reportDefinition.setDownloadFormat(DownloadFormat.XML);
//
//        // Optional: Set the reporting configuration of the session to suppress header, column name, or
//        // summary rows in the report output. You can also configure this via your ads.properties
//        // configuration file. See AdWordsSession.Builder.from(Configuration) for details.
//        // In addition, you can set whether you want to explicitly include or exclude zero impression
//        // rows.
//        ReportingConfiguration reportingConfiguration
//                = new ReportingConfiguration.Builder()
//                .skipReportHeader(true)
//                .skipColumnHeader(true)
//                .skipReportSummary(true)
//                // Enable to allow rows with zero impressions to show.
//                .includeZeroImpressions(false)
//                .build();
//        session.setReportingConfiguration(reportingConfiguration);
//
//        reportDefinition.setSelector(selector);
//
//        try {
//            String filename = XML_FILE_DIR + "adwords-" + RandomStringUtils.randomAlphanumeric(32).toUpperCase() + ".xml";
//            // Set the property api.adwords.reportDownloadTimeout or call
//            // ReportDownloader.setReportDownloadTimeout to set a timeout (in milliseconds)
//            // for CONNECT and READ in report downloads.
//            ReportDownloadResponse response
//                    = new ReportDownloader(session).downloadReport(reportDefinition);
//            //BufferedReader reader = new BufferedReader(new InputStreamReader(response.getInputStream(), Charsets.UTF_8));
//            response.saveToFile(filename);
//
//            AdReport report = (AdReport) FileReader.readXML(filename, AdReport.class);
//            System.out.println(report);
//            System.out.printf("Report successfully downloaded to: %s%n", filename);
//            return report;
//        } catch (ReportDownloadResponseException e) {
//            System.out.printf("Report was not downloaded due to: %s%n", e);
//        } catch (ReportException ex) {
//            Logger.getLogger(AdwordsService.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (IOException ex) {
//            Logger.getLogger(AdwordsService.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return null;
//    }
////
//    public Object getPlacementReport(Date startDate, Date endDate, String accountId, String timeSegment, String productSegment, String filter) {
//        AdWordsSession session = getSession(accountId);
//        Selector selector = new Selector();
//        selector.getFields().addAll(Lists.newArrayList("AccountDescriptiveName", "CampaignId", "CampaignName", "Impressions", "Clicks",
//                "Conversions", "AveragePosition", "AllConversions", "AdGroupName", "AdGroupId",
//                "AverageCpc", "Ctr", "Cost", "CostPerConversion", "ConversionRate", "ExternalCustomerId",
//                "CpcBid", "BidType", "CriteriaDestinationUrl", "Criteria", "FinalUrls", "FinalMobileUrls"
//        ));
//
//        addTimeSelector(selector, timeSegment);
//        addProductSelector(selector, productSegment);
//        setAdNetworkTypeFilters(selector, filter);
//
//        // Create report definition.
//        ReportDefinition reportDefinition = new ReportDefinition();
//        reportDefinition.setReportName("Criteria performance report #" + System.currentTimeMillis());
//        reportDefinition.setDateRangeType(ReportDefinitionDateRangeType.CUSTOM_DATE);
//        DateRange dateRange = new DateRange();
//        dateRange.setMin(DateUtils.getAdWordsStartDate(startDate));
//        dateRange.setMax(DateUtils.getAdWordsEndDate(endDate));
//        selector.setDateRange(dateRange);
//        reportDefinition.setReportType(ReportDefinitionReportType.PLACEMENT_PERFORMANCE_REPORT);
//        reportDefinition.setDownloadFormat(DownloadFormat.XML);
//
//        // Optional: Set the reporting configuration of the session to suppress header, column name, or
//        // summary rows in the report output. You can also configure this via your ads.properties
//        // configuration file. See AdWordsSession.Builder.from(Configuration) for details.
//        // In addition, you can set whether you want to explicitly include or exclude zero impression
//        // rows.
//        ReportingConfiguration reportingConfiguration
//                = new ReportingConfiguration.Builder()
//                .skipReportHeader(true)
//                .skipColumnHeader(true)
//                .skipReportSummary(true)
//                // Enable to allow rows with zero impressions to show.
//                .includeZeroImpressions(false)
//                .build();
//        session.setReportingConfiguration(reportingConfiguration);
//
//        reportDefinition.setSelector(selector);
//
//        try {
//            String filename = XML_FILE_DIR + "adwords-" + RandomStringUtils.randomAlphanumeric(32).toUpperCase() + ".xml";
//            // Set the property api.adwords.reportDownloadTimeout or call
//            // ReportDownloader.setReportDownloadTimeout to set a timeout (in milliseconds)
//            // for CONNECT and READ in report downloads.
//            ReportDownloadResponse response
//                    = new ReportDownloader(session).downloadReport(reportDefinition);
//            response.saveToFile(filename);
//            return XmlUtils.getAsMap(filename);
//        } catch (ReportDownloadResponseException e) {
//            System.out.printf("Report was not downloaded due to: %s%n", e);
//        } catch (ReportException ex) {
//            Logger.getLogger(AdwordsService.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (IOException ex) {
//            Logger.getLogger(AdwordsService.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return null;
//    }
//    
    Map<String, AdwordsReport> adwordsReports = ApiUtils.getAllAdwordsReports();

    public List<Map<String, Object>> getAdwordsReport(String reportName, Date startDate, Date endDate, String accountId,
            String timeSegment, String productSegment, String filter) {
        AdwordsReport adwordsData = adwordsReports.get(reportName);
        System.out.println(adwordsData);
        String[] fields = adwordsData.getFields();
        ReportDefinitionReportType reportType = adwordsData.getReportType();
        System.out.println("Adwords Account Id-->" + accountId);
        AdWordsSession session = getSession(accountId);
        Selector selector = new Selector();
        ArrayList<String> fieldList = Lists.newArrayList(fields);
        if (timeSegment != null && timeSegment.equalsIgnoreCase("HourOfDay")) {
            fieldList.remove("AllConversions");
        }
        if(filter == null || !filter.equalsIgnoreCase("ALL")) {
            fieldList.remove("AdNetworkType2");
        }
        selector.getFields().addAll(fieldList);
        System.out.println("Time Segment ===> " + timeSegment);
        System.out.println("Product Segment ===> " + productSegment);
        System.out.println("Filter ===> " + filter);
        selector = addTimeSelector(selector, timeSegment);
        selector = addProductSelector(selector, productSegment);
        selector = setAdNetworkTypeFilters(selector, filter);
        System.out.println("Selector " + selector.getFields());
        // Create report definition.
        ReportDefinition reportDefinition = new ReportDefinition();
        reportDefinition.setReportName("Criteria performance report #" + System.currentTimeMillis());
        reportDefinition.setDateRangeType(ReportDefinitionDateRangeType.CUSTOM_DATE);
        DateRange dateRange = new DateRange();
        dateRange.setMin(DateUtils.getAdWordsStartDate(startDate));
        dateRange.setMax(DateUtils.getAdWordsEndDate(endDate));
        selector.setDateRange(dateRange);
        reportDefinition.setReportType(reportType);
        reportDefinition.setDownloadFormat(DownloadFormat.XML);

        // Optional: Set the reporting configuration of the session to suppress header, column name, or
        // summary rows in the report output. You can also configure this via your ads.properties
        // configuration file. See AdWordsSession.Builder.from(Configuration) for details.
        // In addition, you can set whether you want to explicitly include or exclude zero impression
        // rows.
        ReportingConfiguration reportingConfiguration
                = new ReportingConfiguration.Builder()
                        .skipReportHeader(true)
                        .skipColumnHeader(true)
                        .skipReportSummary(true)
                        // Enable to allow rows with zero impressions to show.
                        .includeZeroImpressions(false)
                        .build();
        session.setReportingConfiguration(reportingConfiguration);

        reportDefinition.setSelector(selector);

        try {
            String filename = XML_FILE_DIR + "adwords-" + RandomStringUtils.randomAlphanumeric(32).toUpperCase() + ".xml";
            // Set the property api.adwords.reportDownloadTimeout or call
            // ReportDownloader.setReportDownloadTimeout to set a timeout (in milliseconds)
            // for CONNECT and READ in report downloads.
            ReportDownloadResponse response
                    = new ReportDownloader(session).downloadReport(reportDefinition);
            //BufferedReader reader = new BufferedReader(new InputStreamReader(response.getInputStream(), Charsets.UTF_8));
            response.saveToFile(filename);
            Map dataMap = (Map) XmlUtils.getAsMap(filename);
            dataMap = (Map) dataMap.get("report");
            if (dataMap == null) {
                return null;
            }
            dataMap = (Map) dataMap.get("table");
            if (dataMap == null) {
                return null;
            }
            if (dataMap.get("row") instanceof Map) {
                List<Map<String, Object>> returnList = new ArrayList<>();
                returnList.add((Map<String, Object>) dataMap.get("row"));
                return returnList;
            }
            return (List<Map<String, Object>>) dataMap.get("row");
//            AdReport report = (AdReport) FileReader.readXML(filename, AdReport.class);
//            System.out.println(report);
//            System.out.printf("Report successfully downloaded to: %s%n", filename);
//            return report;
        } catch (ReportDownloadResponseException e) {
            System.out.printf("Report was not downloaded due to: %s%n", e);
        } catch (ReportException ex) {
            Logger.getLogger(AdwordsService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(AdwordsService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
//
//    public List<Map<String, Object>> getFinalUrlReport(Date startDate, Date endDate, String accountId, String timeSegment, String productSegment, String filter) {
//        AdWordsSession session = getSession(accountId);
//        Selector selector = new Selector();
//        selector.getFields().addAll(Lists.newArrayList("AccountDescriptiveName", "CampaignId", "CampaignName", "Impressions", "Clicks",
//                "Conversions", "AveragePosition", "AllConversions", "AdGroupName", "AdGroupId",
//                "AverageCpc", "Ctr", "Cost", "CostPerConversion", "ConversionRate", "ExternalCustomerId",
//                "CriteriaTypeName", "EffectiveFinalUrl"
//        ));
//
//        addTimeSelector(selector, timeSegment);
//        addProductSelector(selector, productSegment);
//        setAdNetworkTypeFilters(selector, filter);
//
//        // Create report definition.
//        ReportDefinition reportDefinition = new ReportDefinition();
//        reportDefinition.setReportName("Criteria performance report #" + System.currentTimeMillis());
//        reportDefinition.setDateRangeType(ReportDefinitionDateRangeType.CUSTOM_DATE);
//        DateRange dateRange = new DateRange();
//        dateRange.setMin(DateUtils.getAdWordsStartDate(startDate));
//        dateRange.setMax(DateUtils.getAdWordsEndDate(endDate));
//        selector.setDateRange(dateRange);
//        reportDefinition.setReportType(ReportDefinitionReportType.FINAL_URL_REPORT);
//        reportDefinition.setDownloadFormat(DownloadFormat.XML);
//
//        // Optional: Set the reporting configuration of the session to suppress header, column name, or
//        // summary rows in the report output. You can also configure this via your ads.properties
//        // configuration file. See AdWordsSession.Builder.from(Configuration) for details.
//        // In addition, you can set whether you want to explicitly include or exclude zero impression
//        // rows.
//        ReportingConfiguration reportingConfiguration
//                = new ReportingConfiguration.Builder()
//                .skipReportHeader(true)
//                .skipColumnHeader(true)
//                .skipReportSummary(true)
//                // Enable to allow rows with zero impressions to show.
//                .includeZeroImpressions(false)
//                .build();
//        session.setReportingConfiguration(reportingConfiguration);
//
//        reportDefinition.setSelector(selector);
//
//        try {
//            String filename = XML_FILE_DIR + "adwords-" + RandomStringUtils.randomAlphanumeric(32).toUpperCase() + ".xml";
//            // Set the property api.adwords.reportDownloadTimeout or call
//            // ReportDownloader.setReportDownloadTimeout to set a timeout (in milliseconds)
//            // for CONNECT and READ in report downloads.
//            ReportDownloadResponse response
//                    = new ReportDownloader(session).downloadReport(reportDefinition);
//            //BufferedReader reader = new BufferedReader(new InputStreamReader(response.getInputStream(), Charsets.UTF_8));
//            response.saveToFile(filename);
//            Map dataMap = (Map) XmlUtils.getAsMap(filename);
//            dataMap = (Map) dataMap.get("report");
//            dataMap = (Map) dataMap.get("table");
//            return (List<Map<String, Object>>) dataMap.get("row");
////            AdReport report = (AdReport) FileReader.readXML(filename, AdReport.class);
////            System.out.println(report);
////            System.out.printf("Report successfully downloaded to: %s%n", filename);
////            return report;
//        } catch (ReportDownloadResponseException e) {
//            System.out.printf("Report was not downloaded due to: %s%n", e);
//        } catch (ReportException ex) {
//            Logger.getLogger(AdwordsService.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (IOException ex) {
//            Logger.getLogger(AdwordsService.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return null;
//    }

}
