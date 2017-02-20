/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.api.admin.service;

import com.google.api.ads.adwords.axis.factory.AdWordsServices;
import com.google.api.ads.adwords.axis.v201609.cm.Campaign;
import com.google.api.ads.adwords.axis.v201609.cm.CampaignPage;
import com.google.api.ads.adwords.axis.v201609.cm.CampaignServiceInterface;
import com.google.api.ads.adwords.axis.v201609.cm.Selector;
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
import com.visumbu.api.adwords.report.xml.bean.AccountDayOfWeekReport;
import com.visumbu.api.adwords.report.xml.bean.AccountDeviceReport;
import com.visumbu.api.adwords.report.xml.bean.AccountHourOfDayReport;
import com.visumbu.api.adwords.report.xml.bean.AccountReport;
import com.visumbu.api.adwords.report.xml.bean.AdReport;
import com.visumbu.api.adwords.report.xml.bean.AddGroupReport;
import com.visumbu.api.adwords.report.xml.bean.CallConversionReport;
import com.visumbu.api.adwords.report.xml.bean.CampaignDeviceReport;
import com.visumbu.api.adwords.report.xml.bean.CampaignPerformanceReport;
import com.visumbu.api.adwords.report.xml.bean.CampaignReport;
import com.visumbu.api.adwords.report.xml.bean.GeoReport;
import com.visumbu.api.adwords.report.xml.bean.VideoReport;
import com.visumbu.api.bing.report.xml.bean.KeywordPerformanceReport;
import com.visumbu.api.utils.DateUtils;
import com.visumbu.api.utils.FileReader;
import com.visumbu.api.utils.XmlUtils;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author jp
 */
@Service("adwordsService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class AdwordsService {

    private static final int PAGE_SIZE = 100;
    public static final String XML_FILE_DIR = "/tmp/";
    private static String clientId = "162577857765-r9dvqjb6i7atjvjftdc8dq5pp80n8j2g.apps.googleusercontent.com";
    private static String clientSecret = "UxF3VNJFWfNBEQ86reUTk09M";
    private static String refreshToken = "1/75VMEAe7i9UOm69maPpsPMaYH1e58R1xUGlulN--3Pg";
    private static String developerToken = "X4glgfA7zjlwzeL3jNQjkw";

    private AdWordsSession getSession(String accountId) {
        try {
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

    public CampaignReport getCampaignReport(Date startDate, Date endDate, String accountId, String filter) {
        AdWordsSession session = getSession(accountId);
        com.google.api.ads.adwords.lib.jaxb.v201609.Selector selector = new com.google.api.ads.adwords.lib.jaxb.v201609.Selector();
        selector.getFields().addAll(Lists.newArrayList("CampaignId", "AccountDescriptiveName", "CampaignName",
                "Impressions", "Clicks",
                "SearchExactMatchImpressionShare", "SearchBudgetLostImpressionShare", "SearchRankLostImpressionShare",
                "Conversions", "SearchImpressionShare", "AveragePosition", "AllConversions",
                "NumOfflineInteractions",
                "AverageCpc", "Ctr", "Cost", "CostPerConversion", "Amount", "ConversionRate"
        ));
        if (filter != null) {
            final Predicate predicate = new Predicate();

            predicate.setField("AdNetworkType1");
            predicate.setOperator(PredicateOperator.IN);
            predicate.getValues().add(filter);
            final Collection<Predicate> predicates = new ArrayList<>();
            predicates.add(predicate);
            selector.getPredicates().add(predicate);
        }
        // Create report definition.
        ReportDefinition reportDefinition = new ReportDefinition();
        reportDefinition.setReportName("Criteria performance report #" + System.currentTimeMillis());
        reportDefinition.setDateRangeType(ReportDefinitionDateRangeType.CUSTOM_DATE);
        DateRange dateRange = new DateRange();
        dateRange.setMin(DateUtils.getAdWordsStartDate(startDate));
        dateRange.setMax(DateUtils.getAdWordsEndDate(endDate));
        selector.setDateRange(dateRange);
        reportDefinition.setReportType(ReportDefinitionReportType.CAMPAIGN_PERFORMANCE_REPORT);
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

            CampaignReport report = (CampaignReport) FileReader.readXML(filename, CampaignReport.class);
            System.out.println(report);
            System.out.printf("Report successfully downloaded to: %s%n", filename);
            return report;
        } catch (ReportDownloadResponseException e) {
            System.out.printf("Report was not downloaded due to: %s%n", e);
        } catch (ReportException ex) {
            Logger.getLogger(AdwordsService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(AdwordsService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public AccountReport getAccountReport(Date startDate, Date endDate, String accountId, String aggregation, String filter) {
        AdWordsSession session = getSession(accountId);
        String aggregationDuration = "Date";
        if (aggregation.equalsIgnoreCase("weekly")) {
            aggregationDuration = "Week";
        } else if (aggregation.equalsIgnoreCase("dayOfWeek")) {
            aggregationDuration = "DayOfWeek";
        } else if (aggregation.equalsIgnoreCase("hourOfDay")) {
            aggregationDuration = "HourOfDay";
        } else if (aggregation == "") {
            aggregationDuration = "";
        }
        com.google.api.ads.adwords.lib.jaxb.v201609.Selector selector = new com.google.api.ads.adwords.lib.jaxb.v201609.Selector();
        if (aggregation.equalsIgnoreCase("hourOfDay")) {
            selector.getFields().addAll(Lists.newArrayList("VideoViews", "VideoViewRate", "AccountDescriptiveName",
                    "Impressions", "Clicks", aggregationDuration,
                    "SearchExactMatchImpressionShare", "SearchBudgetLostImpressionShare", "SearchRankLostImpressionShare",
                    "Conversions", "SearchImpressionShare", "AveragePosition",
                    "AverageCpc", "Ctr", "Cost", "CostPerConversion", "ConversionRate"
            ));
        } else if (aggregation.isEmpty()) {
            selector.getFields().addAll(Lists.newArrayList("VideoViews", "VideoViewRate", "AccountDescriptiveName",
                    "Impressions", "Clicks",
                    "SearchExactMatchImpressionShare", "SearchBudgetLostImpressionShare", "SearchRankLostImpressionShare",
                    "Conversions", "SearchImpressionShare", "AveragePosition", "AllConversions",
                    "AverageCpc", "Ctr", "Cost", "CostPerConversion", "ConversionRate"
            ));
        } else {
            selector.getFields().addAll(Lists.newArrayList("VideoViews", "VideoViewRate", "AccountDescriptiveName",
                    "Impressions", "Clicks", aggregationDuration,
                    "SearchExactMatchImpressionShare", "SearchBudgetLostImpressionShare", "SearchRankLostImpressionShare",
                    "Conversions", "SearchImpressionShare", "AveragePosition", "AllConversions",
                    "AverageCpc", "Ctr", "Cost", "CostPerConversion", "ConversionRate"
            ));
        }

        if (filter != null) {
            final Predicate predicate = new Predicate();

            predicate.setField("AdNetworkType1");
            predicate.setOperator(PredicateOperator.IN);
            predicate.getValues().add(filter);
            final Collection<Predicate> predicates = new ArrayList<>();
            predicates.add(predicate);
            selector.getPredicates().add(predicate);
        }

        // Create report definition.
        ReportDefinition reportDefinition = new ReportDefinition();
        reportDefinition.setReportName("Criteria performance report #" + System.currentTimeMillis());
        reportDefinition.setDateRangeType(ReportDefinitionDateRangeType.CUSTOM_DATE);
        DateRange dateRange = new DateRange();
        dateRange.setMin(DateUtils.getAdWordsStartDate(startDate));
        dateRange.setMax(DateUtils.getAdWordsEndDate(endDate));
        selector.setDateRange(dateRange);
        reportDefinition.setReportType(ReportDefinitionReportType.ACCOUNT_PERFORMANCE_REPORT);
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

            AccountReport report = (AccountReport) FileReader.readXML(filename, AccountReport.class);
            System.out.println(report);
            System.out.printf("Report successfully downloaded to: %s%n", filename);
            return report;
        } catch (ReportDownloadResponseException e) {
            System.out.printf("Report was not downloaded due to: %s%n", e);
        } catch (ReportException ex) {
            Logger.getLogger(AdwordsService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(AdwordsService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public VideoReport getVideoCampaignReport(Date startDate, Date endDate, String accountId, String aggregation, String filter) {
        AdWordsSession session = getSession(accountId);

        com.google.api.ads.adwords.lib.jaxb.v201609.Selector selector = new com.google.api.ads.adwords.lib.jaxb.v201609.Selector();
        if (aggregation.equalsIgnoreCase("weekly")) {
            selector.getFields().addAll(Lists.newArrayList("VideoViews", "VideoViewRate", "AccountDescriptiveName",
                    "Impressions", "Clicks", "Week", "CampaignName",
                    "VideoQuartile100Rate", "VideoQuartile25Rate", "VideoQuartile50Rate", "VideoQuartile75Rate",
                    "Conversions",
                    "Ctr", "Cost", "CostPerConversion", "ConversionRate"
            ));
        } else if (aggregation.equalsIgnoreCase("daily")) {
            selector.getFields().addAll(Lists.newArrayList("VideoViews", "VideoViewRate", "AccountDescriptiveName",
                    "Impressions", "Clicks", "Date", "CampaignName",
                    "VideoQuartile100Rate", "VideoQuartile25Rate", "VideoQuartile50Rate", "VideoQuartile75Rate",
                    "Conversions",
                    "Ctr", "Cost", "CostPerConversion", "ConversionRate"
            ));
        } else if (aggregation.equalsIgnoreCase("dayOfWeek")) {
            selector.getFields().addAll(Lists.newArrayList("VideoViews", "VideoViewRate", "AccountDescriptiveName",
                    "Impressions", "Clicks", "DayOfWeek", "CampaignName",
                    "VideoQuartile100Rate", "VideoQuartile25Rate", "VideoQuartile50Rate", "VideoQuartile75Rate",
                    "Conversions",
                    "Ctr", "Cost", "CostPerConversion", "ConversionRate"
            ));
        } else {
            selector.getFields().addAll(Lists.newArrayList("VideoViews", "VideoViewRate", "AccountDescriptiveName",
                    "Impressions", "Clicks", "CampaignName",
                    "VideoQuartile100Rate", "VideoQuartile25Rate", "VideoQuartile50Rate", "VideoQuartile75Rate",
                    "Conversions",
                    "Ctr", "Cost", "CostPerConversion", "ConversionRate"
            ));
        }
        if (filter != null) {
            final Predicate predicate = new Predicate();

            predicate.setField("AdNetworkType1");
            predicate.setOperator(PredicateOperator.IN);
            predicate.getValues().add(filter);
            final Collection<Predicate> predicates = new ArrayList<>();
            predicates.add(predicate);
            selector.getPredicates().add(predicate);
        }

        // Create report definition.
        ReportDefinition reportDefinition = new ReportDefinition();
        reportDefinition.setReportName("Criteria performance report #" + System.currentTimeMillis());
        reportDefinition.setDateRangeType(ReportDefinitionDateRangeType.CUSTOM_DATE);
        DateRange dateRange = new DateRange();
        dateRange.setMin(DateUtils.getAdWordsStartDate(startDate));
        dateRange.setMax(DateUtils.getAdWordsEndDate(endDate));
        selector.setDateRange(dateRange);
        reportDefinition.setReportType(ReportDefinitionReportType.VIDEO_PERFORMANCE_REPORT);
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

            VideoReport report = (VideoReport) FileReader.readXML(filename, VideoReport.class);
            System.out.println(report);
            System.out.printf("Report successfully downloaded to: %s%n", filename);
            return report;
        } catch (ReportDownloadResponseException e) {
            System.out.printf("Report was not downloaded due to: %s%n", e);
        } catch (ReportException ex) {
            Logger.getLogger(AdwordsService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(AdwordsService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public VideoReport getVideoCampaignDeviceReport(Date startDate, Date endDate, String accountId, String aggregation, String filter) {
        AdWordsSession session = getSession(accountId);

        com.google.api.ads.adwords.lib.jaxb.v201609.Selector selector = new com.google.api.ads.adwords.lib.jaxb.v201609.Selector();
        if (aggregation.equalsIgnoreCase("weekly")) {
            selector.getFields().addAll(Lists.newArrayList("VideoViews", "VideoViewRate", "AccountDescriptiveName",
                    "Impressions", "Clicks", "Week", "CampaignName", "Device",
                    "VideoQuartile100Rate", "VideoQuartile25Rate", "VideoQuartile50Rate", "VideoQuartile75Rate",
                    "Conversions",
                    "Ctr", "Cost", "CostPerConversion", "ConversionRate"
            ));
        } else if (aggregation.equalsIgnoreCase("daily")) {
            selector.getFields().addAll(Lists.newArrayList("VideoViews", "VideoViewRate", "AccountDescriptiveName",
                    "Impressions", "Clicks", "Date", "CampaignName", "Device",
                    "VideoQuartile100Rate", "VideoQuartile25Rate", "VideoQuartile50Rate", "VideoQuartile75Rate",
                    "Conversions",
                    "Ctr", "Cost", "CostPerConversion", "ConversionRate"
            ));
        } else if (aggregation.equalsIgnoreCase("dayOfWeek")) {
            selector.getFields().addAll(Lists.newArrayList("VideoViews", "VideoViewRate", "AccountDescriptiveName",
                    "Impressions", "Clicks", "DayOfWeek", "CampaignName", "Device",
                    "VideoQuartile100Rate", "VideoQuartile25Rate", "VideoQuartile50Rate", "VideoQuartile75Rate",
                    "Conversions",
                    "Ctr", "Cost", "CostPerConversion", "ConversionRate"
            ));
        } else {
            selector.getFields().addAll(Lists.newArrayList("VideoViews", "VideoViewRate", "AccountDescriptiveName",
                    "Impressions", "Clicks", "CampaignName", "Device",
                    "VideoQuartile100Rate", "VideoQuartile25Rate", "VideoQuartile50Rate", "VideoQuartile75Rate",
                    "Conversions",
                    "Ctr", "Cost", "CostPerConversion", "ConversionRate"
            ));
        }
        if (filter != null) {
            final Predicate predicate = new Predicate();

            predicate.setField("AdNetworkType1");
            predicate.setOperator(PredicateOperator.IN);
            predicate.getValues().add(filter);
            final Collection<Predicate> predicates = new ArrayList<>();
            predicates.add(predicate);
            selector.getPredicates().add(predicate);
        }

        // Create report definition.
        ReportDefinition reportDefinition = new ReportDefinition();
        reportDefinition.setReportName("Criteria performance report #" + System.currentTimeMillis());
        reportDefinition.setDateRangeType(ReportDefinitionDateRangeType.CUSTOM_DATE);
        DateRange dateRange = new DateRange();
        dateRange.setMin(DateUtils.getAdWordsStartDate(startDate));
        dateRange.setMax(DateUtils.getAdWordsEndDate(endDate));
        selector.setDateRange(dateRange);
        reportDefinition.setReportType(ReportDefinitionReportType.VIDEO_PERFORMANCE_REPORT);
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

            VideoReport report = (VideoReport) FileReader.readXML(filename, VideoReport.class);
            System.out.println(report);
            System.out.printf("Report successfully downloaded to: %s%n", filename);
            return report;
        } catch (ReportDownloadResponseException e) {
            System.out.printf("Report was not downloaded due to: %s%n", e);
        } catch (ReportException ex) {
            Logger.getLogger(AdwordsService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(AdwordsService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public VideoReport getVideoDeviceReport(Date startDate, Date endDate, String accountId, String aggregation, String filter) {
        AdWordsSession session = getSession(accountId);

        com.google.api.ads.adwords.lib.jaxb.v201609.Selector selector = new com.google.api.ads.adwords.lib.jaxb.v201609.Selector();
        if (aggregation.equalsIgnoreCase("weekly")) {
            selector.getFields().addAll(Lists.newArrayList("VideoViews", "VideoViewRate", "AccountDescriptiveName",
                    "Impressions", "Clicks", "Week", "Device",
                    "VideoQuartile100Rate", "VideoQuartile25Rate", "VideoQuartile50Rate", "VideoQuartile75Rate",
                    "Conversions",
                    "Ctr", "Cost", "CostPerConversion", "ConversionRate"
            ));
        } else if (aggregation.equalsIgnoreCase("daily")) {
            selector.getFields().addAll(Lists.newArrayList("VideoViews", "VideoViewRate", "AccountDescriptiveName",
                    "Impressions", "Clicks", "Date", "Device",
                    "VideoQuartile100Rate", "VideoQuartile25Rate", "VideoQuartile50Rate", "VideoQuartile75Rate",
                    "Conversions",
                    "Ctr", "Cost", "CostPerConversion", "ConversionRate"
            ));
        } else if (aggregation.equalsIgnoreCase("dayOfWeek")) {
            selector.getFields().addAll(Lists.newArrayList("VideoViews", "VideoViewRate", "AccountDescriptiveName",
                    "Impressions", "Clicks", "DayOfWeek", "Device",
                    "VideoQuartile100Rate", "VideoQuartile25Rate", "VideoQuartile50Rate", "VideoQuartile75Rate",
                    "Conversions", "Campaign",
                    "Ctr", "Cost", "CostPerConversion", "ConversionRate"
            ));
        } else {
            selector.getFields().addAll(Lists.newArrayList("VideoViews", "VideoViewRate", "AccountDescriptiveName",
                    "Impressions", "Clicks", "Device",
                    "VideoQuartile100Rate", "VideoQuartile25Rate", "VideoQuartile50Rate", "VideoQuartile75Rate",
                    "Conversions",
                    "Ctr", "Cost", "CostPerConversion", "ConversionRate"
            ));
        }
        if (filter != null) {
            final Predicate predicate = new Predicate();

            predicate.setField("AdNetworkType1");
            predicate.setOperator(PredicateOperator.IN);
            predicate.getValues().add(filter);
            final Collection<Predicate> predicates = new ArrayList<>();
            predicates.add(predicate);
            selector.getPredicates().add(predicate);
        }

        // Create report definition.
        ReportDefinition reportDefinition = new ReportDefinition();
        reportDefinition.setReportName("Criteria performance report #" + System.currentTimeMillis());
        reportDefinition.setDateRangeType(ReportDefinitionDateRangeType.CUSTOM_DATE);
        DateRange dateRange = new DateRange();
        dateRange.setMin(DateUtils.getAdWordsStartDate(startDate));
        dateRange.setMax(DateUtils.getAdWordsEndDate(endDate));
        selector.setDateRange(dateRange);
        reportDefinition.setReportType(ReportDefinitionReportType.VIDEO_PERFORMANCE_REPORT);
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

            VideoReport report = (VideoReport) FileReader.readXML(filename, VideoReport.class);
            System.out.println(report);
            System.out.printf("Report successfully downloaded to: %s%n", filename);
            return report;
        } catch (ReportDownloadResponseException e) {
            System.out.printf("Report was not downloaded due to: %s%n", e);
        } catch (ReportException ex) {
            Logger.getLogger(AdwordsService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(AdwordsService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public VideoReport getVideoReport(Date startDate, Date endDate, String accountId, String aggregation, String filter) {
        AdWordsSession session = getSession(accountId);

        com.google.api.ads.adwords.lib.jaxb.v201609.Selector selector = new com.google.api.ads.adwords.lib.jaxb.v201609.Selector();
        if (aggregation.equalsIgnoreCase("weekly")) {
            selector.getFields().addAll(Lists.newArrayList("VideoViews", "VideoViewRate", "AccountDescriptiveName",
                    "Impressions", "Clicks", "Week",
                    "VideoQuartile100Rate", "VideoQuartile25Rate", "VideoQuartile50Rate", "VideoQuartile75Rate",
                    "Conversions",
                    "Ctr", "Cost", "CostPerConversion", "ConversionRate"
            ));
        } else if (aggregation.equalsIgnoreCase("daily")) {
            selector.getFields().addAll(Lists.newArrayList("VideoViews", "VideoViewRate", "AccountDescriptiveName",
                    "Impressions", "Clicks", "Date",
                    "VideoQuartile100Rate", "VideoQuartile25Rate", "VideoQuartile50Rate", "VideoQuartile75Rate",
                    "Conversions",
                    "Ctr", "Cost", "CostPerConversion", "ConversionRate"
            ));
        } else if (aggregation.equalsIgnoreCase("dayOfWeek")) {
            selector.getFields().addAll(Lists.newArrayList("VideoViews", "VideoViewRate", "AccountDescriptiveName",
                    "Impressions", "Clicks", "DayOfWeek",
                    "VideoQuartile100Rate", "VideoQuartile25Rate", "VideoQuartile50Rate", "VideoQuartile75Rate",
                    "Conversions",
                    "Ctr", "Cost", "CostPerConversion", "ConversionRate"
            ));
        } else if (aggregation.equalsIgnoreCase("summary")) {
            selector.getFields().addAll(Lists.newArrayList("VideoViews", "VideoViewRate", "AccountDescriptiveName",
                    "Impressions", "Clicks",
                    "VideoQuartile100Rate", "VideoQuartile25Rate", "VideoQuartile50Rate", "VideoQuartile75Rate",
                    "Conversions",
                    "Ctr", "Cost", "CostPerConversion", "ConversionRate"
            ));
        } else if (aggregation.equalsIgnoreCase("videoTab")) {
            selector.getFields().addAll(Lists.newArrayList("VideoViews", "VideoViewRate", "AccountDescriptiveName",
                    "Impressions", "Clicks",
                    "VideoQuartile100Rate", "VideoQuartile25Rate", "VideoQuartile50Rate", "VideoQuartile75Rate",
                    "Conversions", "VideoTitle",
                    "Ctr", "Cost", "CostPerConversion", "ConversionRate"
            ));
        } else {
            selector.getFields().addAll(Lists.newArrayList("VideoViews", "VideoViewRate", "AccountDescriptiveName",
                    "Impressions", "Clicks",
                    "VideoQuartile100Rate", "VideoQuartile25Rate", "VideoQuartile50Rate", "VideoQuartile75Rate",
                    "Conversions",
                    "Ctr", "Cost", "CostPerConversion", "ConversionRate"
            ));
        }
        if (filter != null) {
            final Predicate predicate = new Predicate();

            predicate.setField("AdNetworkType1");
            predicate.setOperator(PredicateOperator.IN);
            predicate.getValues().add(filter);
            final Collection<Predicate> predicates = new ArrayList<>();
            predicates.add(predicate);
            selector.getPredicates().add(predicate);
        }

        // Create report definition.
        ReportDefinition reportDefinition = new ReportDefinition();
        reportDefinition.setReportName("Criteria performance report #" + System.currentTimeMillis());
        reportDefinition.setDateRangeType(ReportDefinitionDateRangeType.CUSTOM_DATE);
        DateRange dateRange = new DateRange();
        dateRange.setMin(DateUtils.getAdWordsStartDate(startDate));
        dateRange.setMax(DateUtils.getAdWordsEndDate(endDate));
        selector.setDateRange(dateRange);
        reportDefinition.setReportType(ReportDefinitionReportType.VIDEO_PERFORMANCE_REPORT);
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

            VideoReport report = (VideoReport) FileReader.readXML(filename, VideoReport.class);
            System.out.println(report);
            System.out.printf("Report successfully downloaded to: %s%n", filename);
            return report;
        } catch (ReportDownloadResponseException e) {
            System.out.printf("Report was not downloaded due to: %s%n", e);
        } catch (ReportException ex) {
            Logger.getLogger(AdwordsService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(AdwordsService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public AddGroupReport getAdGroupReport(Date startDate, Date endDate, String accountId, String aggregation, String filter) {
        AdWordsSession session = getSession(accountId);
        com.google.api.ads.adwords.lib.jaxb.v201609.Selector selector = new com.google.api.ads.adwords.lib.jaxb.v201609.Selector();
        if (aggregation == null || aggregation.isEmpty()) {
            selector.getFields().addAll(Lists.newArrayList("CampaignId", "AccountDescriptiveName", "CampaignName", "AdGroupId", "AdGroupName",
                    "Impressions", "Clicks",
                    "SearchExactMatchImpressionShare", "SearchRankLostImpressionShare", "NumOfflineInteractions",
                    "Conversions", "SearchImpressionShare", "AveragePosition", "AllConversions",
                    "NumOfflineInteractions",
                    "AverageCpc", "Ctr", "Cost", "CostPerConversion", "ConversionRate"
            ));
        } else {
            selector.getFields().addAll(Lists.newArrayList("CampaignId", "AccountDescriptiveName", "CampaignName", "AdGroupId", "AdGroupName",
                    "Impressions", "Clicks", "Date",
                    "SearchExactMatchImpressionShare", "SearchRankLostImpressionShare", "NumOfflineInteractions",
                    "Conversions", "SearchImpressionShare", "AveragePosition", "AllConversions",
                    "NumOfflineInteractions",
                    "AverageCpc", "Ctr", "Cost", "CostPerConversion", "ConversionRate"
            ));
        }
        if (filter != null) {
            final Predicate predicate = new Predicate();
            predicate.setField("AdNetworkType1");
            predicate.setOperator(PredicateOperator.IN);
            predicate.getValues().add(filter);

            final Predicate predicate1 = new Predicate();
            predicate1.setField("CampaignName");
            predicate1.setOperator(PredicateOperator.CONTAINS_IGNORE_CASE);
            predicate1.getValues().add("model");

//            predicate1.getValues().add("");
//            final Collection<Predicate> predicates = new ArrayList<>();
//            predicates.add(predicate);
//            predicates.add(predicate1);
            selector.getPredicates().add(predicate);
            selector.getPredicates().add(predicate1);
        }

        // Create report definition.
        ReportDefinition reportDefinition = new ReportDefinition();
        reportDefinition.setReportName("Criteria performance report #" + System.currentTimeMillis());
        reportDefinition.setDateRangeType(ReportDefinitionDateRangeType.CUSTOM_DATE);
        DateRange dateRange = new DateRange();
        dateRange.setMin(DateUtils.getAdWordsStartDate(startDate));
        dateRange.setMax(DateUtils.getAdWordsEndDate(endDate));
        selector.setDateRange(dateRange);
        reportDefinition.setReportType(ReportDefinitionReportType.ADGROUP_PERFORMANCE_REPORT);
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

            AddGroupReport report = (AddGroupReport) FileReader.readXML(filename, AddGroupReport.class);
            System.out.println(report);
            System.out.printf("Report successfully downloaded to: %s%n", filename);
            return report;
        } catch (ReportDownloadResponseException e) {
            System.out.printf("Report was not downloaded due to: %s%n", e);
        } catch (ReportException ex) {
            Logger.getLogger(AdwordsService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(AdwordsService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public AccountDeviceReport getAccountDevicePerformanceReport(Date startDate, Date endDate, String accountId, String aggregation, String filter) {
        AdWordsSession session = getSession(accountId);
        com.google.api.ads.adwords.lib.jaxb.v201609.Selector selector = new com.google.api.ads.adwords.lib.jaxb.v201609.Selector();
        if (aggregation == null || aggregation.isEmpty()) {
            selector.getFields().addAll(Lists.newArrayList("Device", "VideoViews", "VideoViewRate", "AccountDescriptiveName",
                    "Impressions", "Clicks",
                    "SearchExactMatchImpressionShare", "SearchBudgetLostImpressionShare", "SearchRankLostImpressionShare",
                    "Conversions", "SearchImpressionShare", "AveragePosition", "AllConversions",
                    "AverageCpc", "Ctr", "Cost", "CostPerConversion", "ConversionRate"
            ));
        } else {
            selector.getFields().addAll(Lists.newArrayList("Device", "VideoViews", "VideoViewRate", "AccountDescriptiveName",
                    "Impressions", "Clicks", "Date",
                    "SearchExactMatchImpressionShare", "SearchBudgetLostImpressionShare", "SearchRankLostImpressionShare",
                    "Conversions", "SearchImpressionShare", "AveragePosition", "AllConversions",
                    "AverageCpc", "Ctr", "Cost", "CostPerConversion", "ConversionRate"
            ));
        }
        if (filter != null) {
            final Predicate predicate = new Predicate();
            predicate.setField("AdNetworkType1");
            predicate.setOperator(PredicateOperator.IN);
            predicate.getValues().add(filter);
            final Collection<Predicate> predicates = new ArrayList<>();
            predicates.add(predicate);
            selector.getPredicates().add(predicate);
        }
        // Create report definition.
        ReportDefinition reportDefinition = new ReportDefinition();
        reportDefinition.setReportName("Criteria performance report #" + System.currentTimeMillis());
        reportDefinition.setDateRangeType(ReportDefinitionDateRangeType.CUSTOM_DATE);
        DateRange dateRange = new DateRange();
        dateRange.setMin(DateUtils.getAdWordsStartDate(startDate));
        dateRange.setMax(DateUtils.getAdWordsEndDate(endDate));
        selector.setDateRange(dateRange);
        reportDefinition.setReportType(ReportDefinitionReportType.ACCOUNT_PERFORMANCE_REPORT);
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

            AccountDeviceReport report = (AccountDeviceReport) FileReader.readXML(filename, AccountDeviceReport.class);
            System.out.println(report);
            System.out.printf("Report successfully downloaded to: %s%n", filename);
            return report;
        } catch (ReportDownloadResponseException e) {
            System.out.printf("Report was not downloaded due to: %s%n", e);
        } catch (ReportException ex) {
            Logger.getLogger(AdwordsService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(AdwordsService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public CampaignPerformanceReport getCampaignPerformanceReport(Date startDate, Date endDate, String accountId, String aggregation, String filter) {
        AdWordsSession session = getSession(accountId);
        com.google.api.ads.adwords.lib.jaxb.v201609.Selector selector = new com.google.api.ads.adwords.lib.jaxb.v201609.Selector();
        if (aggregation == null || aggregation.isEmpty()) {
            selector.getFields().addAll(Lists.newArrayList("CampaignId", "CampaignName", "Impressions", "Clicks",
                    "Conversions", "SearchImpressionShare", "AveragePosition", "AllConversions", "NumOfflineInteractions",
                    "AverageCpc", "Ctr", "Cost", "CostPerConversion", "Amount", "ConversionRate"
            ));
        } else {
            selector.getFields().addAll(Lists.newArrayList("CampaignId", "CampaignName", "Impressions", "Clicks", "Date",
                    "Conversions", "SearchImpressionShare", "AveragePosition", "AllConversions", "NumOfflineInteractions",
                    "AverageCpc", "Ctr", "Cost", "CostPerConversion", "Amount", "ConversionRate"
            ));
        }
        if (filter != null) {
            final Predicate predicate = new Predicate();

            predicate.setField("AdNetworkType1");
            predicate.setOperator(PredicateOperator.IN);
            predicate.getValues().add(filter);
            final Collection<Predicate> predicates = new ArrayList<>();
            predicates.add(predicate);
            selector.getPredicates().add(predicate);
        }
        // Create report definition.
        ReportDefinition reportDefinition = new ReportDefinition();
        reportDefinition.setReportName("Criteria performance report #" + System.currentTimeMillis());
        reportDefinition.setDateRangeType(ReportDefinitionDateRangeType.CUSTOM_DATE);
        DateRange dateRange = new DateRange();
        dateRange.setMin(DateUtils.getAdWordsStartDate(startDate));
        dateRange.setMax(DateUtils.getAdWordsEndDate(endDate));
        selector.setDateRange(dateRange);
        reportDefinition.setReportType(ReportDefinitionReportType.CAMPAIGN_PERFORMANCE_REPORT);
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

            CampaignPerformanceReport report = (CampaignPerformanceReport) FileReader.readXML(filename, CampaignPerformanceReport.class);
            System.out.println(report);
            System.out.printf("Report successfully downloaded to: %s%n", filename);
            return report;
        } catch (ReportDownloadResponseException e) {
            System.out.printf("Report was not downloaded due to: %s%n", e);
        } catch (ReportException ex) {
            Logger.getLogger(AdwordsService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(AdwordsService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public CampaignDeviceReport getCampaignDeviceReport(Date startDate, Date endDate, String accountId, String aggregation, String filter) {
        AdWordsSession session = getSession(accountId);
        com.google.api.ads.adwords.lib.jaxb.v201609.Selector selector = new com.google.api.ads.adwords.lib.jaxb.v201609.Selector();
        if (aggregation == null || aggregation.isEmpty()) {
            selector.getFields().addAll(Lists.newArrayList("Device", "CampaignId", "AccountDescriptiveName", "CampaignName", "VideoViews", "VideoViewRate",
                    "VideoQuartile100Rate", "VideoQuartile25Rate", "VideoQuartile50Rate", "VideoQuartile75Rate", "Impressions", "Clicks",
                    "SearchExactMatchImpressionShare", "SearchBudgetLostImpressionShare", "SearchRankLostImpressionShare",
                    "Conversions", "SearchImpressionShare", "AveragePosition", "AllConversions",
                    "AverageCpc", "Ctr", "Cost", "CostPerConversion", "Amount", "ConversionRate"
            ));
        } else {
            selector.getFields().addAll(Lists.newArrayList("Device", "CampaignId", "AccountDescriptiveName", "CampaignName", "VideoViews", "VideoViewRate",
                    "VideoQuartile100Rate", "VideoQuartile25Rate", "VideoQuartile50Rate", "VideoQuartile75Rate", "Impressions", "Clicks", "Date",
                    "SearchExactMatchImpressionShare", "SearchBudgetLostImpressionShare", "SearchRankLostImpressionShare",
                    "Conversions", "SearchImpressionShare", "AveragePosition", "AllConversions",
                    "AverageCpc", "Ctr", "Cost", "CostPerConversion", "Amount", "ConversionRate"
            ));
        }
        if (filter != null) {
            final Predicate predicate = new Predicate();

            predicate.setField("AdNetworkType1");
            predicate.setOperator(PredicateOperator.IN);
            predicate.getValues().add(filter);
            final Collection<Predicate> predicates = new ArrayList<>();
            predicates.add(predicate);
            selector.getPredicates().add(predicate);
        }
        // Create report definition.
        ReportDefinition reportDefinition = new ReportDefinition();
        reportDefinition.setReportName("Criteria performance report #" + System.currentTimeMillis());
        reportDefinition.setDateRangeType(ReportDefinitionDateRangeType.CUSTOM_DATE);
        DateRange dateRange = new DateRange();
        dateRange.setMin(DateUtils.getAdWordsStartDate(startDate));
        dateRange.setMax(DateUtils.getAdWordsEndDate(endDate));
        selector.setDateRange(dateRange);
        reportDefinition.setReportType(ReportDefinitionReportType.CAMPAIGN_PERFORMANCE_REPORT);
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

            CampaignDeviceReport report = (CampaignDeviceReport) FileReader.readXML(filename, CampaignDeviceReport.class);
            System.out.println(report);
            System.out.printf("Report successfully downloaded to: %s%n", filename);
            return report;
        } catch (ReportDownloadResponseException e) {
            System.out.printf("Report was not downloaded due to: %s%n", e);
        } catch (ReportException ex) {
            Logger.getLogger(AdwordsService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(AdwordsService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public AdReport getAdReport(Date startDate, Date endDate, String accountId, String aggregation, String filter) {
        AdWordsSession session = getSession(accountId);
        com.google.api.ads.adwords.lib.jaxb.v201609.Selector selector = new com.google.api.ads.adwords.lib.jaxb.v201609.Selector();
        if (aggregation == null || aggregation.isEmpty()) {
            selector.getFields().addAll(Lists.newArrayList("CampaignId", "AccountDescriptiveName", "CampaignName", "VideoViews", "VideoViewRate",
                    "VideoQuartile100Rate", "VideoQuartile25Rate", "VideoQuartile50Rate", "VideoQuartile75Rate", "Impressions", "Clicks",
                    "Conversions", "AveragePosition", "AllConversions", "AdGroupName", "AdGroupId", "Headline",
                    "AdType", "Description", "Description1", "Description2", "DisplayUrl", "CreativeFinalUrls", "CreativeDestinationUrl",
                    "AverageCpc", "Ctr", "Cost", "CostPerConversion", "ConversionRate"
            ));
        } else {
            selector.getFields().addAll(Lists.newArrayList("CampaignId", "AccountDescriptiveName", "CampaignName", "VideoViews", "VideoViewRate",
                    "VideoQuartile100Rate", "VideoQuartile25Rate", "VideoQuartile50Rate", "VideoQuartile75Rate", "Impressions", "Clicks", "Date",
                    "Conversions", "AveragePosition", "AllConversions", "AdGroupName", "AdGroupId", "Headline",
                    "AdType", "Description", "Description1", "Description2", "DisplayUrl", "CreativeFinalUrls", "CreativeDestinationUrl",
                    "AverageCpc", "Ctr", "Cost", "CostPerConversion", "ConversionRate"
            ));
        }
        if (filter != null) {
            final Predicate predicate = new Predicate();

            predicate.setField("AdNetworkType1");
            predicate.setOperator(PredicateOperator.IN);
            predicate.getValues().add(filter);
            final Collection<Predicate> predicates = new ArrayList<>();
            predicates.add(predicate);
            selector.getPredicates().add(predicate);
        }
        // Create report definition.
        ReportDefinition reportDefinition = new ReportDefinition();
        reportDefinition.setReportName("Criteria performance report #" + System.currentTimeMillis());
        reportDefinition.setDateRangeType(ReportDefinitionDateRangeType.CUSTOM_DATE);
        DateRange dateRange = new DateRange();
        dateRange.setMin(DateUtils.getAdWordsStartDate(startDate));
        dateRange.setMax(DateUtils.getAdWordsEndDate(endDate));
        selector.setDateRange(dateRange);
        reportDefinition.setReportType(ReportDefinitionReportType.AD_PERFORMANCE_REPORT);
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

            AdReport report = (AdReport) FileReader.readXML(filename, AdReport.class);
            System.out.println(report);
            System.out.printf("Report successfully downloaded to: %s%n", filename);
            return report;
        } catch (ReportDownloadResponseException e) {
            System.out.printf("Report was not downloaded due to: %s%n", e);
        } catch (ReportException ex) {
            Logger.getLogger(AdwordsService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(AdwordsService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public AccountHourOfDayReport getAccountHourOfDayReport(Date startDate, Date endDate, String accountId, String filter) {
        AdWordsSession session = getSession(accountId);
        com.google.api.ads.adwords.lib.jaxb.v201609.Selector selector = new com.google.api.ads.adwords.lib.jaxb.v201609.Selector();
        selector.getFields().addAll(Lists.newArrayList("VideoViews", "VideoViewRate", "AccountDescriptiveName",
                "Impressions", "Clicks",
                "SearchExactMatchImpressionShare", "SearchBudgetLostImpressionShare", "SearchRankLostImpressionShare",
                "Conversions", "SearchImpressionShare", "AveragePosition",
                "AverageCpc", "Ctr", "Cost", "CostPerConversion", "ConversionRate", "HourOfDay"
        ));
        if (filter != null) {
            final Predicate predicate = new Predicate();

            predicate.setField("AdNetworkType1");
            predicate.setOperator(PredicateOperator.IN);
            predicate.getValues().add(filter);
            final Collection<Predicate> predicates = new ArrayList<>();
            predicates.add(predicate);
            selector.getPredicates().add(predicate);
        }
        // Create report definition.
        ReportDefinition reportDefinition = new ReportDefinition();
        reportDefinition.setReportName("Criteria performance report #" + System.currentTimeMillis());
        reportDefinition.setDateRangeType(ReportDefinitionDateRangeType.CUSTOM_DATE);
        DateRange dateRange = new DateRange();
        dateRange.setMin(DateUtils.getAdWordsStartDate(startDate));
        dateRange.setMax(DateUtils.getAdWordsEndDate(endDate));
        selector.setDateRange(dateRange);
        reportDefinition.setReportType(ReportDefinitionReportType.ACCOUNT_PERFORMANCE_REPORT);
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

            AccountHourOfDayReport report = (AccountHourOfDayReport) FileReader.readXML(filename, AccountHourOfDayReport.class);
            System.out.println(report);
            System.out.printf("Report successfully downloaded to: %s%n", filename);
            return report;
        } catch (ReportDownloadResponseException e) {
            System.out.printf("Report was not downloaded due to: %s%n", e);
        } catch (ReportException ex) {
            Logger.getLogger(AdwordsService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(AdwordsService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public AccountDayOfWeekReport getAccountDayOfWeekReport(Date startDate, Date endDate, String accountId, String filter) {
        AdWordsSession session = getSession(accountId);
        com.google.api.ads.adwords.lib.jaxb.v201609.Selector selector = new com.google.api.ads.adwords.lib.jaxb.v201609.Selector();
        selector.getFields().addAll(Lists.newArrayList("VideoViews", "VideoViewRate", "AccountDescriptiveName",
                "Impressions", "Clicks", "DayOfWeek",
                "SearchExactMatchImpressionShare", "SearchBudgetLostImpressionShare", "SearchRankLostImpressionShare",
                "Conversions", "SearchImpressionShare", "AveragePosition",
                "AverageCpc", "Ctr", "Cost", "CostPerConversion", "ConversionRate"
        ));
        if (filter != null) {
            final Predicate predicate = new Predicate();

            predicate.setField("AdNetworkType1");
            predicate.setOperator(PredicateOperator.IN);
            predicate.getValues().add(filter);
            final Collection<Predicate> predicates = new ArrayList<>();
            predicates.add(predicate);
            selector.getPredicates().add(predicate);
        }
        // Create report definition.
        ReportDefinition reportDefinition = new ReportDefinition();
        reportDefinition.setReportName("Criteria performance report #" + System.currentTimeMillis());
        reportDefinition.setDateRangeType(ReportDefinitionDateRangeType.CUSTOM_DATE);
        DateRange dateRange = new DateRange();
        dateRange.setMin(DateUtils.getAdWordsStartDate(startDate));
        dateRange.setMax(DateUtils.getAdWordsEndDate(endDate));
        selector.setDateRange(dateRange);
        reportDefinition.setReportType(ReportDefinitionReportType.ACCOUNT_PERFORMANCE_REPORT);
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

            AccountDayOfWeekReport report = (AccountDayOfWeekReport) FileReader.readXML(filename, AccountDayOfWeekReport.class);
            System.out.println(report);
            System.out.printf("Report successfully downloaded to: %s%n", filename);
            return report;
        } catch (ReportDownloadResponseException e) {
            System.out.printf("Report was not downloaded due to: %s%n", e);
        } catch (ReportException ex) {
            Logger.getLogger(AdwordsService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(AdwordsService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public GeoReport getGeoReport(Date startDate, Date endDate, String accountId, String aggregation, String filter) {
        AdWordsSession session = getSession(accountId);
        com.google.api.ads.adwords.lib.jaxb.v201609.Selector selector = new com.google.api.ads.adwords.lib.jaxb.v201609.Selector();
        if (aggregation == null || aggregation.isEmpty()) {
            selector.getFields().addAll(Lists.newArrayList("VideoViews", "VideoViewRate", "AccountDescriptiveName",
                    "Impressions", "Clicks", "CountryCriteriaId", "CityCriteriaId",
                    "Conversions", "AveragePosition", "AllConversions",
                    "AverageCpc", "Ctr", "Cost", "CostPerConversion", "ConversionRate"
            ));
        } else {
            selector.getFields().addAll(Lists.newArrayList("VideoViews", "VideoViewRate", "AccountDescriptiveName",
                    "Impressions", "Clicks", "Date", "CountryCriteriaId", "CityCriteriaId",
                    "Conversions", "AveragePosition", "AllConversions",
                    "AverageCpc", "Ctr", "Cost", "CostPerConversion", "ConversionRate"
            ));
        }
        if (filter != null) {
            final Predicate predicate = new Predicate();

            predicate.setField("AdNetworkType1");
            predicate.setOperator(PredicateOperator.IN);
            predicate.getValues().add(filter);
            final Collection<Predicate> predicates = new ArrayList<>();
            predicates.add(predicate);
            selector.getPredicates().add(predicate);
        }
        // Create report definition.
        ReportDefinition reportDefinition = new ReportDefinition();
        reportDefinition.setReportName("Criteria performance report #" + System.currentTimeMillis());
        reportDefinition.setDateRangeType(ReportDefinitionDateRangeType.CUSTOM_DATE);
        DateRange dateRange = new DateRange();
        dateRange.setMin(DateUtils.getAdWordsStartDate(startDate));
        dateRange.setMax(DateUtils.getAdWordsEndDate(endDate));
        selector.setDateRange(dateRange);
        reportDefinition.setReportType(ReportDefinitionReportType.GEO_PERFORMANCE_REPORT);
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

            GeoReport report = (GeoReport) FileReader.readXML(filename, GeoReport.class);
            System.out.println(report);
            System.out.printf("Report successfully downloaded to: %s%n", filename);
            return report;
        } catch (ReportDownloadResponseException e) {
            System.out.printf("Report was not downloaded due to: %s%n", e);
        } catch (ReportException ex) {
            Logger.getLogger(AdwordsService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(AdwordsService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public CallConversionReport getAccountCallConversionsReport(Date startDate, Date endDate, String accountId, String aggregation, String filter) {
        System.out.println(accountId);
        AdWordsSession session = getSession(accountId);
        com.google.api.ads.adwords.lib.jaxb.v201609.Selector selector = new com.google.api.ads.adwords.lib.jaxb.v201609.Selector();
        selector.getFields().addAll(Lists.newArrayList("AccountDescriptiveName",
                "Conversions"
        ));

        if (filter != null) {
            final Predicate predicate1 = new Predicate();
            predicate1.setField("ConversionTypeName");
            predicate1.setOperator(PredicateOperator.IN);
            predicate1.getValues().add("Lead");
            final Predicate predicate = new Predicate();
            predicate.setField("AdNetworkType1");
            predicate.setOperator(PredicateOperator.IN);
            predicate.getValues().add(filter);
            //final Collection<Predicate> predicates = new ArrayList<>();
            //predicates.add(predicate);
            //predicates.add(predicate1);
            selector.getPredicates().add(predicate);
            selector.getPredicates().add(predicate1);
        }
        // Create report definition.
        ReportDefinition reportDefinition = new ReportDefinition();
        reportDefinition.setReportName("Call Conversion report #" + System.currentTimeMillis());
        reportDefinition.setDateRangeType(ReportDefinitionDateRangeType.CUSTOM_DATE);
        DateRange dateRange = new DateRange();
        dateRange.setMin(DateUtils.getAdWordsStartDate(startDate));
        dateRange.setMax(DateUtils.getAdWordsEndDate(endDate));
        selector.setDateRange(dateRange);
        reportDefinition.setReportType(ReportDefinitionReportType.ACCOUNT_PERFORMANCE_REPORT);
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

            CallConversionReport report = (CallConversionReport) FileReader.readXML(filename, CallConversionReport.class);
            System.out.println(report);
            System.out.printf("Report successfully downloaded to: %s%n", filename);
            return report;
        } catch (ReportDownloadResponseException e) {
            System.out.printf("Report was not downloaded due to: %s%n", e);
        } catch (ReportException ex) {
            Logger.getLogger(AdwordsService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(AdwordsService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(AdwordsService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

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
        }
        else if (reportType.equalsIgnoreCase("account")) {
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

}
