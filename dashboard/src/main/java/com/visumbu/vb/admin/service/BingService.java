/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.admin.service;
//
//import com.microsoft.bingads.AuthorizationData;
//import com.microsoft.bingads.PasswordAuthentication;
//import com.microsoft.bingads.reporting.AccountPerformanceReportColumn;
//import com.microsoft.bingads.reporting.AccountPerformanceReportRequest;
//import com.microsoft.bingads.reporting.AccountReportScope;
//import com.microsoft.bingads.reporting.AccountThroughAdGroupReportScope;
//import com.microsoft.bingads.reporting.AccountThroughCampaignReportScope;
//import com.microsoft.bingads.reporting.AdGroupPerformanceReportColumn;
//import com.microsoft.bingads.reporting.AdGroupPerformanceReportFilter;
//import com.microsoft.bingads.reporting.AdGroupPerformanceReportRequest;
//import com.microsoft.bingads.reporting.AdPerformanceReportColumn;
//import com.microsoft.bingads.reporting.AdPerformanceReportRequest;
//import com.microsoft.bingads.reporting.ArrayOfAccountPerformanceReportColumn;
//import com.microsoft.bingads.reporting.ArrayOfAdGroupPerformanceReportColumn;
//import com.microsoft.bingads.reporting.ArrayOfAdPerformanceReportColumn;
//import com.microsoft.bingads.reporting.ArrayOfCampaignPerformanceReportColumn;
//import com.microsoft.bingads.reporting.ArrayOfCampaignReportScope;
//import com.microsoft.bingads.reporting.ArrayOfGeoLocationPerformanceReportColumn;
//import com.microsoft.bingads.reporting.ArrayOfKeywordPerformanceReportColumn;
//import com.microsoft.bingads.reporting.ArrayOfKeywordPerformanceReportSort;
//import com.microsoft.bingads.reporting.ArrayOflong;
//import com.microsoft.bingads.reporting.CampaignPerformanceReportColumn;
//import com.microsoft.bingads.reporting.CampaignPerformanceReportRequest;
//import com.microsoft.bingads.reporting.CampaignReportScope;
//import com.microsoft.bingads.reporting.CampaignStatusReportFilter;
//import com.microsoft.bingads.reporting.GeoLocationPerformanceReportColumn;
//import com.microsoft.bingads.reporting.GeoLocationPerformanceReportRequest;
//import com.microsoft.bingads.reporting.KeywordPerformanceReportColumn;
//import com.microsoft.bingads.reporting.KeywordPerformanceReportRequest;
//import com.microsoft.bingads.reporting.KeywordPerformanceReportSort;
//import com.microsoft.bingads.reporting.NonHourlyReportAggregation;
//import com.microsoft.bingads.reporting.ReportAggregation;
//import com.microsoft.bingads.reporting.ReportFormat;
//import com.microsoft.bingads.reporting.ReportRequest;
//import com.microsoft.bingads.reporting.ReportTime;
//import com.microsoft.bingads.reporting.ReportTimePeriod;
//import com.microsoft.bingads.reporting.ReportingDownloadParameters;
//import com.microsoft.bingads.reporting.ReportingServiceManager;
//import com.microsoft.bingads.reporting.SortOrder;
//import com.visumbu.api.bing.report.xml.bean.AccountDayOfWeekPerformanceReport;
//import com.visumbu.api.bing.report.xml.bean.AccountDevicePerformanceReport;
//import com.visumbu.api.bing.report.xml.bean.AccountHourOfDayPerformanceReport;
//import com.visumbu.api.bing.report.xml.bean.AccountPerformanceReport;
//import com.visumbu.api.bing.report.xml.bean.AdGroupPerformanceReport;
//import com.visumbu.api.bing.report.xml.bean.AdPerformanceReport;
//import com.visumbu.api.bing.report.xml.bean.CampaignDevicePerformanceReport;
//import com.visumbu.api.bing.report.xml.bean.CampaignPerformanceReport;
//import com.visumbu.api.bing.report.xml.bean.Data;
//import com.visumbu.api.bing.report.xml.bean.GeoCityLocationPerformanceReport;
//import com.visumbu.api.bing.report.xml.bean.GeoZipLocationPerformanceReport;
//import com.visumbu.api.bing.report.xml.bean.KeywordPerformanceReport;
//import com.visumbu.api.bing.report.xml.bean.KeywordPerformanceRow;
//import com.visumbu.vb.utils.FileReader;
//import java.io.File;
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Map;
//import java.util.concurrent.ExecutionException;
//import java.util.concurrent.TimeUnit;
//import java.util.concurrent.TimeoutException;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//import org.apache.commons.lang3.RandomStringUtils;
//import org.codehaus.jackson.map.ObjectMapper;
//import org.codehaus.jackson.type.TypeReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author jp
 */
@Service("bingService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class BingService {

//    private AuthorizationData authorizationData = new AuthorizationData();
//    public static final String DEVELOPER_TOKEN = "00074192B2151803";
//    public static final String USER_NAME = "API_L2TMediaNew";
//    public static final String PASSWORD = "l2ttangocode2016";
//    public static final Long customerId = 14195800L;
//    //public static final Long accountId = 2610614L;
//    public static final String tmpDir = "/tmp/";
//
//    private void initAuthentication(Long accountId) {
//        PasswordAuthentication passwordAuthentication
//                = new PasswordAuthentication(USER_NAME, PASSWORD);
//        authorizationData.setAuthentication(passwordAuthentication);
//
//        authorizationData.setCustomerId(customerId);
//        authorizationData.setAccountId(accountId);
//        authorizationData.setDeveloperToken(DEVELOPER_TOKEN);
//
//    }
//
//    private List<Map<String, String>> toMap(List data) {
//        List<Map<String, String>> returnMap = new ArrayList<>();
//        for (Iterator iterator = data.iterator(); iterator.hasNext();) {
//            Object dataObject = iterator.next();
//            ObjectMapper mapper = new ObjectMapper();
//            Map<String, Data> map
//                    = mapper.convertValue(dataObject, new TypeReference<Map<String, Data>>() {
//                    });
//            Map<String, String> mapData = new HashMap<>();
//            for (Map.Entry<String, Data> entrySet : map.entrySet()) {
//                String key = entrySet.getKey();
//                Data value = entrySet.getValue();
//                mapData.put(key, value.getValue());
//            }
//            returnMap.add(mapData);
//        }
//        return returnMap;
//    }
//
//    public List<Map<String, String>> get(String reportName, Long accountId, Date startDate, Date endDate, String aggregation) {
//        try {
//            if (reportName.equalsIgnoreCase("keywordPerformace")) {
//                KeywordPerformanceReport report = getKeywordPerformanceReport(startDate, endDate, accountId);
//                List<KeywordPerformanceRow> keywordPerformanceRows = report.getKeywordPerformanceRows();
//                return toMap(keywordPerformanceRows);
//            } else if (reportName.equalsIgnoreCase("accountPerformace")) {
//                return toMap(getAccountPerformanceReport(startDate, endDate, accountId, aggregation).getAccountPerformanceRows());
//            } else if (reportName.equalsIgnoreCase("accountPerformace")) {
//                return toMap(getAccountPerformanceReport(startDate, endDate, accountId, aggregation).getAccountPerformanceRows());
//            } else if (reportName.equalsIgnoreCase("devicePerformace")) {
//                return toMap(getAccountDevicePerformanceReport(startDate, endDate, accountId, aggregation).getAccountDevicePerformanceRows());
//            } else if (reportName.equalsIgnoreCase("campaignPerformace")) {
//                return toMap(getCampaignPerformanceReport(startDate, endDate, accountId, aggregation).getCampaignPerformanceRows());
//            } else if (reportName.equalsIgnoreCase("adGroupPerformace")) {
//                return toMap(getAdGroupPerformanceReport(startDate, endDate, accountId, aggregation).getAdGroupPerformanceRows());
//            }else if (reportName.equalsIgnoreCase("adPerformace")) {
//                return toMap(getAdPerformanceReport(startDate, endDate, accountId).getAdPerformanceRows());
//            }else if (reportName.equalsIgnoreCase("geoPerformace")) {
//                return toMap(getGeoCityLocationPerformanceReport(startDate, endDate, accountId, aggregation).getGeoCityLocationPerformanceRows());
//            }
//        } catch (ExecutionException ex) {
//            Logger.getLogger(BingService.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (InterruptedException ex) {
//            Logger.getLogger(BingService.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (TimeoutException ex) {
//            Logger.getLogger(BingService.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        
//        return null;
//    }
//
//    public KeywordPerformanceReport getKeywordPerformanceReport(Date startDate, Date endDate, Long accountId)
//            throws InterruptedException, ExecutionException, TimeoutException {
//        initAuthentication(accountId);
//        ReportingServiceManager reportingServiceManager = new ReportingServiceManager(authorizationData);
//        reportingServiceManager.setStatusPollIntervalInMilliseconds(5000);
//        String filename = "bing-" + RandomStringUtils.randomAlphanumeric(32).toUpperCase() + ".xml";
//        ReportRequest reportRequest
//                = //getAccountHourPerformaceReportRequest();
//                //getGeoLocationPerformaceReportRequest();
//                //getAdPerformaceReportRequest();
//                //getAccountPerformaceReportRequest(); 
//                getKeywordPerformanceReportRequest(startDate, endDate);
//
//        ReportingDownloadParameters reportingDownloadParameters = new ReportingDownloadParameters();
//        reportingDownloadParameters.setReportRequest(reportRequest);
//        reportingDownloadParameters.setResultFileDirectory(new File(tmpDir));
//        reportingDownloadParameters.setResultFileName(filename);
//        reportingDownloadParameters.setOverwriteResultFile(true);
//
//// You may optionally cancel the downloadFileAsync operation after a specified time interval.
//        File resultFile = reportingServiceManager.downloadFileAsync(
//                reportingDownloadParameters,
//                null).get(3600000, TimeUnit.MILLISECONDS);
//        KeywordPerformanceReport report = (KeywordPerformanceReport) FileReader.readXML(resultFile, KeywordPerformanceReport.class);
//        System.out.println(report);
//        return report;
//    }
//
//    public AccountPerformanceReport getAccountPerformanceReport(Date startDate, Date endDate, Long accountId, String aggregation)
//            throws InterruptedException, ExecutionException, TimeoutException {
//        initAuthentication(accountId);
//        ReportingServiceManager reportingServiceManager = new ReportingServiceManager(authorizationData);
//        reportingServiceManager.setStatusPollIntervalInMilliseconds(5000);
//        String filename = "bing-" + RandomStringUtils.randomAlphanumeric(32).toUpperCase() + ".xml";
//        ReportRequest reportRequest
//                = getAccountPerformaceReportRequest(startDate, endDate, aggregation);
//
//        ReportingDownloadParameters reportingDownloadParameters = new ReportingDownloadParameters();
//        reportingDownloadParameters.setReportRequest(reportRequest);
//        reportingDownloadParameters.setResultFileDirectory(new File(tmpDir));
//        reportingDownloadParameters.setResultFileName(filename);
//        reportingDownloadParameters.setOverwriteResultFile(true);
//
//// You may optionally cancel the downloadFileAsync operation after a specified time interval.
//        File resultFile = reportingServiceManager.downloadFileAsync(
//                reportingDownloadParameters,
//                null).get(3600000, TimeUnit.MILLISECONDS);
//        AccountPerformanceReport report = (AccountPerformanceReport) FileReader.readXML(resultFile, AccountPerformanceReport.class);
//        System.out.println(report);
//        return report;
//    }
//
//    public AccountHourOfDayPerformanceReport getAccountHourOfDayPerformanceReport(Date startDate, Date endDate, Long accountId)
//            throws InterruptedException, ExecutionException, TimeoutException {
//        initAuthentication(accountId);
//        ReportingServiceManager reportingServiceManager = new ReportingServiceManager(authorizationData);
//        reportingServiceManager.setStatusPollIntervalInMilliseconds(5000);
//        String filename = "bing-" + RandomStringUtils.randomAlphanumeric(32).toUpperCase() + ".xml";
//        ReportRequest reportRequest
//                = getAccountHourOfDayPerformaceReportRequest(startDate, endDate);
//
//        ReportingDownloadParameters reportingDownloadParameters = new ReportingDownloadParameters();
//        reportingDownloadParameters.setReportRequest(reportRequest);
//        reportingDownloadParameters.setResultFileDirectory(new File(tmpDir));
//        reportingDownloadParameters.setResultFileName(filename);
//        reportingDownloadParameters.setOverwriteResultFile(true);
//
//// You may optionally cancel the downloadFileAsync operation after a specified time interval.
//        File resultFile = reportingServiceManager.downloadFileAsync(
//                reportingDownloadParameters,
//                null).get(3600000, TimeUnit.MILLISECONDS);
//        AccountPerformanceReport report1 = (AccountPerformanceReport) FileReader.readXML(resultFile, AccountPerformanceReport.class);
//        System.out.println(report1);
//
//        AccountHourOfDayPerformanceReport report = (AccountHourOfDayPerformanceReport) FileReader.readXML(resultFile, AccountHourOfDayPerformanceReport.class);
//        System.out.println(report);
//        return report;
//    }
//
//    public AccountDayOfWeekPerformanceReport getAccountDayOfWeekPerformanceReport(Date startDate, Date endDate, Long accountId)
//            throws InterruptedException, ExecutionException, TimeoutException {
//        initAuthentication(accountId);
//        ReportingServiceManager reportingServiceManager = new ReportingServiceManager(authorizationData);
//        reportingServiceManager.setStatusPollIntervalInMilliseconds(5000);
//        String filename = "bing-" + RandomStringUtils.randomAlphanumeric(32).toUpperCase() + ".xml";
//        ReportRequest reportRequest
//                = getAccountDayOfWeekPerformaceReportRequest(startDate, endDate);
//
//        ReportingDownloadParameters reportingDownloadParameters = new ReportingDownloadParameters();
//        reportingDownloadParameters.setReportRequest(reportRequest);
//        reportingDownloadParameters.setResultFileDirectory(new File(tmpDir));
//        reportingDownloadParameters.setResultFileName(filename);
//        reportingDownloadParameters.setOverwriteResultFile(true);
//
//        // You may optionally cancel the downloadFileAsync operation after a specified time interval.
//        File resultFile = reportingServiceManager.downloadFileAsync(
//                reportingDownloadParameters,
//                null).get(3600000, TimeUnit.MILLISECONDS);
//        AccountDayOfWeekPerformanceReport report = (AccountDayOfWeekPerformanceReport) FileReader.readXML(resultFile, AccountDayOfWeekPerformanceReport.class);
//        System.out.println(report);
//        return report;
//    }
//
//    public AccountDevicePerformanceReport getAccountDevicePerformanceReport(Date startDate, Date endDate, Long accountId, String aggregation) {
//        try {
//            initAuthentication(accountId);
//            ReportingServiceManager reportingServiceManager = new ReportingServiceManager(authorizationData);
//            reportingServiceManager.setStatusPollIntervalInMilliseconds(5000);
//            String filename = "bing-" + RandomStringUtils.randomAlphanumeric(32).toUpperCase() + ".xml";
//            ReportRequest reportRequest
//                    = getAccountDevicePerformaceReportRequest(startDate, endDate, aggregation);
//
//            ReportingDownloadParameters reportingDownloadParameters = new ReportingDownloadParameters();
//            reportingDownloadParameters.setReportRequest(reportRequest);
//            reportingDownloadParameters.setResultFileDirectory(new File(tmpDir));
//            reportingDownloadParameters.setResultFileName(filename);
//            reportingDownloadParameters.setOverwriteResultFile(true);
//
//            // You may optionally cancel the downloadFileAsync operation after a specified time interval.
//            File resultFile = reportingServiceManager.downloadFileAsync(
//                    reportingDownloadParameters,
//                    null).get(3600000, TimeUnit.MILLISECONDS);
//            AccountDevicePerformanceReport report = (AccountDevicePerformanceReport) FileReader.readXML(resultFile, AccountDevicePerformanceReport.class);
//            System.out.println(report);
//            return report;
//        } catch (InterruptedException ex) {
//            Logger.getLogger(BingService.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (ExecutionException ex) {
//            Logger.getLogger(BingService.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (TimeoutException ex) {
//            Logger.getLogger(BingService.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return null;
//    }
//
//    public CampaignPerformanceReport getCampaignPerformanceReport(Date startDate, Date endDate, Long accountId, String aggregation) {
//        try {
//            initAuthentication(accountId);
//            ReportingServiceManager reportingServiceManager = new ReportingServiceManager(authorizationData);
//            reportingServiceManager.setStatusPollIntervalInMilliseconds(5000);
//            String filename = "bing-" + RandomStringUtils.randomAlphanumeric(32).toUpperCase() + ".xml";
//            ReportRequest reportRequest
//                    = getCampaignPerformaceReportRequest(startDate, endDate, aggregation);
//
//            ReportingDownloadParameters reportingDownloadParameters = new ReportingDownloadParameters();
//            reportingDownloadParameters.setReportRequest(reportRequest);
//            reportingDownloadParameters.setResultFileDirectory(new File(tmpDir));
//            reportingDownloadParameters.setResultFileName(filename);
//            reportingDownloadParameters.setOverwriteResultFile(true);
//
//            // You may optionally cancel the downloadFileAsync operation after a specified time interval.
//            File resultFile = reportingServiceManager.downloadFileAsync(
//                    reportingDownloadParameters,
//                    null).get(3600000, TimeUnit.MILLISECONDS);
//            CampaignPerformanceReport report = (CampaignPerformanceReport) FileReader.readXML(resultFile, CampaignPerformanceReport.class);
//            System.out.println(report);
//            return report;
//        } catch (InterruptedException ex) {
//            Logger.getLogger(BingService.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (ExecutionException ex) {
//            Logger.getLogger(BingService.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (TimeoutException ex) {
//            Logger.getLogger(BingService.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return null;
//    }
//
//    public CampaignDevicePerformanceReport getCampaignDevicePerformanceReport(Date startDate, Date endDate, Long accountId, String aggregation) {
//        try {
//            initAuthentication(accountId);
//            ReportingServiceManager reportingServiceManager = new ReportingServiceManager(authorizationData);
//            reportingServiceManager.setStatusPollIntervalInMilliseconds(5000);
//            String filename = "bing-" + RandomStringUtils.randomAlphanumeric(32).toUpperCase() + ".xml";
//            ReportRequest reportRequest
//                    = getCampaignDevicePerformaceReportRequest(startDate, endDate, aggregation);
//
//            ReportingDownloadParameters reportingDownloadParameters = new ReportingDownloadParameters();
//            reportingDownloadParameters.setReportRequest(reportRequest);
//            reportingDownloadParameters.setResultFileDirectory(new File(tmpDir));
//            reportingDownloadParameters.setResultFileName(filename);
//            reportingDownloadParameters.setOverwriteResultFile(true);
//
//            // You may optionally cancel the downloadFileAsync operation after a specified time interval.
//            File resultFile = reportingServiceManager.downloadFileAsync(
//                    reportingDownloadParameters,
//                    null).get(3600000, TimeUnit.MILLISECONDS);
//            CampaignDevicePerformanceReport report = (CampaignDevicePerformanceReport) FileReader.readXML(resultFile, CampaignDevicePerformanceReport.class);
//            System.out.println(report);
//            return report;
//        } catch (InterruptedException ex) {
//            Logger.getLogger(BingService.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (ExecutionException ex) {
//            Logger.getLogger(BingService.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (TimeoutException ex) {
//            Logger.getLogger(BingService.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return null;
//    }
//
//    public AdGroupPerformanceReport getAdGroupPerformanceReport(Date startDate, Date endDate, Long accountId, String aggregation) {
//        try {
//            initAuthentication(accountId);
//            ReportingServiceManager reportingServiceManager = new ReportingServiceManager(authorizationData);
//            reportingServiceManager.setStatusPollIntervalInMilliseconds(5000);
//            String filename = "bing-" + RandomStringUtils.randomAlphanumeric(32).toUpperCase() + ".xml";
//            ReportRequest reportRequest
//                    = getAdGroupPerformaceReportRequest(startDate, endDate, aggregation);
//
//            ReportingDownloadParameters reportingDownloadParameters = new ReportingDownloadParameters();
//            reportingDownloadParameters.setReportRequest(reportRequest);
//            reportingDownloadParameters.setResultFileDirectory(new File(tmpDir));
//            reportingDownloadParameters.setResultFileName(filename);
//            reportingDownloadParameters.setOverwriteResultFile(true);
//
//            // You may optionally cancel the downloadFileAsync operation after a specified time interval.
//            File resultFile = reportingServiceManager.downloadFileAsync(
//                    reportingDownloadParameters,
//                    null).get(3600000, TimeUnit.MILLISECONDS);
//            AdGroupPerformanceReport report = (AdGroupPerformanceReport) FileReader.readXML(resultFile, AdGroupPerformanceReport.class);
//            System.out.println(report);
//            return report;
//        } catch (InterruptedException ex) {
//            Logger.getLogger(BingService.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (ExecutionException ex) {
//            Logger.getLogger(BingService.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (TimeoutException ex) {
//            Logger.getLogger(BingService.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return null;
//    }
//
//    public AdPerformanceReport getAdPerformanceReport(Date startDate, Date endDate, Long accountId) {
//        try {
//            initAuthentication(accountId);
//            ReportingServiceManager reportingServiceManager = new ReportingServiceManager(authorizationData);
//            reportingServiceManager.setStatusPollIntervalInMilliseconds(5000);
//            String filename = "bing-" + RandomStringUtils.randomAlphanumeric(32).toUpperCase() + ".xml";
//            ReportRequest reportRequest
//                    = getAdPerformaceReportRequest(startDate, endDate);
//
//            ReportingDownloadParameters reportingDownloadParameters = new ReportingDownloadParameters();
//            reportingDownloadParameters.setReportRequest(reportRequest);
//            reportingDownloadParameters.setResultFileDirectory(new File(tmpDir));
//            reportingDownloadParameters.setResultFileName(filename);
//            reportingDownloadParameters.setOverwriteResultFile(true);
//
//            // You may optionally cancel the downloadFileAsync operation after a specified time interval.
//            File resultFile = reportingServiceManager.downloadFileAsync(
//                    reportingDownloadParameters,
//                    null).get(3600000, TimeUnit.MILLISECONDS);
//            AdPerformanceReport report = (AdPerformanceReport) FileReader.readXML(resultFile, AdPerformanceReport.class);
//            System.out.println(report);
//            return report;
//        } catch (InterruptedException ex) {
//            Logger.getLogger(BingService.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (ExecutionException ex) {
//            Logger.getLogger(BingService.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (TimeoutException ex) {
//            Logger.getLogger(BingService.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return null;
//    }
//
//    public GeoCityLocationPerformanceReport getGeoCityLocationPerformanceReport(Date startDate, Date endDate, Long accountId, String aggregation) {
//        try {
//            initAuthentication(accountId);
//            ReportingServiceManager reportingServiceManager = new ReportingServiceManager(authorizationData);
//            reportingServiceManager.setStatusPollIntervalInMilliseconds(5000);
//            String filename = "bing-" + RandomStringUtils.randomAlphanumeric(32).toUpperCase() + ".xml";
//            ReportRequest reportRequest
//                    = getGeoCityLocationPerformanceReportRequest(startDate, endDate, aggregation);
//
//            ReportingDownloadParameters reportingDownloadParameters = new ReportingDownloadParameters();
//            reportingDownloadParameters.setReportRequest(reportRequest);
//            reportingDownloadParameters.setResultFileDirectory(new File(tmpDir));
//            reportingDownloadParameters.setResultFileName(filename);
//            reportingDownloadParameters.setOverwriteResultFile(true);
//
//            // You may optionally cancel the downloadFileAsync operation after a specified time interval.
//            File resultFile = reportingServiceManager.downloadFileAsync(
//                    reportingDownloadParameters,
//                    null).get(3600000, TimeUnit.MILLISECONDS);
//            GeoCityLocationPerformanceReport report = (GeoCityLocationPerformanceReport) FileReader.readXML(resultFile, GeoCityLocationPerformanceReport.class);
//            System.out.println(report);
//            return report;
//        } catch (InterruptedException ex) {
//            Logger.getLogger(BingService.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (ExecutionException ex) {
//            Logger.getLogger(BingService.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (TimeoutException ex) {
//            Logger.getLogger(BingService.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return null;
//    }
//
//    public GeoZipLocationPerformanceReport getGeoZipLocationPerformanceReport(Date startDate, Date endDate, Long accountId)
//            throws InterruptedException, ExecutionException, TimeoutException {
//        initAuthentication(accountId);
//        ReportingServiceManager reportingServiceManager = new ReportingServiceManager(authorizationData);
//        reportingServiceManager.setStatusPollIntervalInMilliseconds(5000);
//        String filename = "bing-" + RandomStringUtils.randomAlphanumeric(32).toUpperCase() + ".xml";
//        ReportRequest reportRequest
//                = getGeoZipLocationPerformanceReportRequest(startDate, endDate);
//
//        ReportingDownloadParameters reportingDownloadParameters = new ReportingDownloadParameters();
//        reportingDownloadParameters.setReportRequest(reportRequest);
//        reportingDownloadParameters.setResultFileDirectory(new File(tmpDir));
//        reportingDownloadParameters.setResultFileName(filename);
//        reportingDownloadParameters.setOverwriteResultFile(true);
//
//        // You may optionally cancel the downloadFileAsync operation after a specified time interval.
//        File resultFile = reportingServiceManager.downloadFileAsync(
//                reportingDownloadParameters,
//                null).get(3600000, TimeUnit.MILLISECONDS);
//        GeoZipLocationPerformanceReport report = (GeoZipLocationPerformanceReport) FileReader.readXML(resultFile, GeoZipLocationPerformanceReport.class);
//        System.out.println(report);
//        return report;
//    }
//
//    private ReportRequest getKeywordPerformanceReportRequest(Date startDate, Date endDate) {
//        KeywordPerformanceReportRequest report = new KeywordPerformanceReportRequest();
//        ReportFormat ReportFileFormat = ReportFormat.XML;
//        report.setFormat(ReportFileFormat);
//        report.setReportName("My Keyword Performance Report");
//        report.setReturnOnlyCompleteData(false);
//        report.setAggregation(ReportAggregation.DAILY);
//
//        ArrayOflong accountIds = new ArrayOflong();
//        accountIds.getLongs().add(authorizationData.getAccountId());
//
//        report.setScope(new AccountThroughAdGroupReportScope());
//        report.getScope().setAccountIds(accountIds);
//        report.getScope().setCampaigns(null);
//        report.getScope().setAdGroups(null);
//
//        report.setTime(new ReportTime());
//        // report.getTime().setPredefinedTime(ReportTimePeriod.YESTERDAY);
//        /* Start Date */
//        Calendar startCal = Calendar.getInstance();
//        startCal.setTime(startDate);
//        report.getTime().setCustomDateRangeStart(new com.microsoft.bingads.reporting.Date());
//        report.getTime().getCustomDateRangeStart().setDay(startCal.get(Calendar.DAY_OF_MONTH));
//        report.getTime().getCustomDateRangeStart().setMonth(startCal.get(Calendar.MONTH) + 1);
//        report.getTime().getCustomDateRangeStart().setYear(startCal.get(Calendar.YEAR));
//
//        // End Date 
//        Calendar endCal = Calendar.getInstance();
//        endCal.setTime(endDate);
//        report.getTime().setCustomDateRangeEnd(new com.microsoft.bingads.reporting.Date());
//        System.out.println(endCal.get(Calendar.DAY_OF_MONTH));
//        System.out.println(endCal.get(Calendar.MONTH) + 1);
//        System.out.println(endCal.get(Calendar.YEAR));
//        report.getTime().getCustomDateRangeEnd().setDay(endCal.get(Calendar.DAY_OF_MONTH));
//        report.getTime().getCustomDateRangeEnd().setMonth(endCal.get(Calendar.MONTH) + 1);
//        report.getTime().getCustomDateRangeEnd().setYear(endCal.get(Calendar.YEAR));
//
//        ArrayOfKeywordPerformanceReportColumn keywordPerformanceReportColumns = new ArrayOfKeywordPerformanceReportColumn();
//        keywordPerformanceReportColumns.getKeywordPerformanceReportColumns().add(KeywordPerformanceReportColumn.TIME_PERIOD);
//        keywordPerformanceReportColumns.getKeywordPerformanceReportColumns().add(KeywordPerformanceReportColumn.ACCOUNT_ID);
//        keywordPerformanceReportColumns.getKeywordPerformanceReportColumns().add(KeywordPerformanceReportColumn.ACCOUNT_NUMBER);
//        keywordPerformanceReportColumns.getKeywordPerformanceReportColumns().add(KeywordPerformanceReportColumn.ACCOUNT_NAME);
//        keywordPerformanceReportColumns.getKeywordPerformanceReportColumns().add(KeywordPerformanceReportColumn.CAMPAIGN_ID);
//        keywordPerformanceReportColumns.getKeywordPerformanceReportColumns().add(KeywordPerformanceReportColumn.CAMPAIGN_NAME);
//        keywordPerformanceReportColumns.getKeywordPerformanceReportColumns().add(KeywordPerformanceReportColumn.KEYWORD);
//        keywordPerformanceReportColumns.getKeywordPerformanceReportColumns().add(KeywordPerformanceReportColumn.KEYWORD_ID);
//        keywordPerformanceReportColumns.getKeywordPerformanceReportColumns().add(KeywordPerformanceReportColumn.DEVICE_TYPE);
//        keywordPerformanceReportColumns.getKeywordPerformanceReportColumns().add(KeywordPerformanceReportColumn.BID_MATCH_TYPE);
//        keywordPerformanceReportColumns.getKeywordPerformanceReportColumns().add(KeywordPerformanceReportColumn.CLICKS);
//        keywordPerformanceReportColumns.getKeywordPerformanceReportColumns().add(KeywordPerformanceReportColumn.IMPRESSIONS);
//        keywordPerformanceReportColumns.getKeywordPerformanceReportColumns().add(KeywordPerformanceReportColumn.CTR);
//        keywordPerformanceReportColumns.getKeywordPerformanceReportColumns().add(KeywordPerformanceReportColumn.AVERAGE_CPC);
//        keywordPerformanceReportColumns.getKeywordPerformanceReportColumns().add(KeywordPerformanceReportColumn.CONVERSIONS);
//        keywordPerformanceReportColumns.getKeywordPerformanceReportColumns().add(KeywordPerformanceReportColumn.COST_PER_CONVERSION);
//        keywordPerformanceReportColumns.getKeywordPerformanceReportColumns().add(KeywordPerformanceReportColumn.CONVERSION_RATE);
//        keywordPerformanceReportColumns.getKeywordPerformanceReportColumns().add(KeywordPerformanceReportColumn.SPEND);
//        keywordPerformanceReportColumns.getKeywordPerformanceReportColumns().add(KeywordPerformanceReportColumn.QUALITY_SCORE);
//        report.setColumns(keywordPerformanceReportColumns);
//
//        // You may optionally sort by any KeywordPerformanceReportColumn, and optionally
//        // specify the maximum number of rows to return in the sorted report. 
//        KeywordPerformanceReportSort keywordPerformanceReportSort = new KeywordPerformanceReportSort();
//        keywordPerformanceReportSort.setSortColumn(KeywordPerformanceReportColumn.CLICKS);
//        keywordPerformanceReportSort.setSortOrder(SortOrder.ASCENDING);
//        ArrayOfKeywordPerformanceReportSort keywordPerformanceReportSorts = new ArrayOfKeywordPerformanceReportSort();
//        keywordPerformanceReportSorts.getKeywordPerformanceReportSorts().add(keywordPerformanceReportSort);
//        report.setSort(keywordPerformanceReportSorts);
//
////        report.setMaxRows(100);
//        return report;
//    }
//
//    private ReportRequest getAccountDayofWeekPerformaceReportRequest() {
//        AccountPerformanceReportRequest report = new AccountPerformanceReportRequest();
//        ReportFormat ReportFileFormat = ReportFormat.XML;
//        report.setFormat(ReportFileFormat);
//        report.setReportName("My Keyword Performance Report");
//        report.setReturnOnlyCompleteData(false);
//        report.setAggregation(ReportAggregation.DAY_OF_WEEK);
//
//        ArrayOflong accountIds = new ArrayOflong();
//        accountIds.getLongs().add(authorizationData.getAccountId());
//
//        report.setScope(new AccountReportScope());
//        report.getScope().setAccountIds(accountIds);
//        report.setTime(new ReportTime());
//        report.getTime().setPredefinedTime(ReportTimePeriod.TODAY);
//
//        ArrayOfAccountPerformanceReportColumn accountPerformanceReportColumn = new ArrayOfAccountPerformanceReportColumn();
//        accountPerformanceReportColumn.getAccountPerformanceReportColumns().add(AccountPerformanceReportColumn.IMPRESSIONS);
//        accountPerformanceReportColumn.getAccountPerformanceReportColumns().add(AccountPerformanceReportColumn.ACCOUNT_ID);
//        accountPerformanceReportColumn.getAccountPerformanceReportColumns().add(AccountPerformanceReportColumn.CLICKS);
//        accountPerformanceReportColumn.getAccountPerformanceReportColumns().add(AccountPerformanceReportColumn.CTR);
//        accountPerformanceReportColumn.getAccountPerformanceReportColumns().add(AccountPerformanceReportColumn.AVERAGE_CPC);
//        accountPerformanceReportColumn.getAccountPerformanceReportColumns().add(AccountPerformanceReportColumn.SPEND);
//        accountPerformanceReportColumn.getAccountPerformanceReportColumns().add(AccountPerformanceReportColumn.CONVERSIONS);
//        accountPerformanceReportColumn.getAccountPerformanceReportColumns().add(AccountPerformanceReportColumn.CONVERSION_RATE);
//        accountPerformanceReportColumn.getAccountPerformanceReportColumns().add(AccountPerformanceReportColumn.COST_PER_CONVERSION);
//        accountPerformanceReportColumn.getAccountPerformanceReportColumns().add(AccountPerformanceReportColumn.ACCOUNT_NAME);
//        accountPerformanceReportColumn.getAccountPerformanceReportColumns().add(AccountPerformanceReportColumn.ACCOUNT_STATUS);
//        accountPerformanceReportColumn.getAccountPerformanceReportColumns().add(AccountPerformanceReportColumn.TIME_PERIOD);
//
//        report.setColumns(accountPerformanceReportColumn);
//        return report;
//    }
//
//    private ReportRequest getGeoCityLocationPerformanceReportRequest(Date startDate, Date endDate, String aggregation) {
//        GeoLocationPerformanceReportRequest report = new GeoLocationPerformanceReportRequest();
//        ReportFormat ReportFileFormat = ReportFormat.XML;
//        report.setFormat(ReportFileFormat);
//        report.setReportName("My Keyword Performance Report");
//        report.setReturnOnlyCompleteData(false);
//        if (aggregation == null || aggregation.isEmpty()) {
//
//            report.setAggregation(NonHourlyReportAggregation.SUMMARY);
//        } else {
//            report.setAggregation(NonHourlyReportAggregation.DAILY);
//        }
//
//        ArrayOflong accountIds = new ArrayOflong();
//        accountIds.getLongs().add(authorizationData.getAccountId());
//
//        report.setScope(new AccountThroughAdGroupReportScope());
//        report.getScope().setAccountIds(accountIds);
//        report.setTime(new ReportTime());
//        // report.getTime().setPredefinedTime(ReportTimePeriod.YESTERDAY);
//        /* Start Date */
//        Calendar startCal = Calendar.getInstance();
//        startCal.setTime(startDate);
//        report.getTime().setCustomDateRangeStart(new com.microsoft.bingads.reporting.Date());
//        report.getTime().getCustomDateRangeStart().setDay(startCal.get(Calendar.DAY_OF_MONTH));
//        report.getTime().getCustomDateRangeStart().setMonth(startCal.get(Calendar.MONTH) + 1);
//        report.getTime().getCustomDateRangeStart().setYear(startCal.get(Calendar.YEAR));
//
//        // End Date 
//        Calendar endCal = Calendar.getInstance();
//        endCal.setTime(endDate);
//        report.getTime().setCustomDateRangeEnd(new com.microsoft.bingads.reporting.Date());
//        System.out.println(endCal.get(Calendar.DAY_OF_MONTH));
//        System.out.println(endCal.get(Calendar.MONTH) + 1);
//        System.out.println(endCal.get(Calendar.YEAR));
//        report.getTime().getCustomDateRangeEnd().setDay(endCal.get(Calendar.DAY_OF_MONTH));
//        report.getTime().getCustomDateRangeEnd().setMonth(endCal.get(Calendar.MONTH) + 1);
//        report.getTime().getCustomDateRangeEnd().setYear(endCal.get(Calendar.YEAR));
//
//        ArrayOfGeoLocationPerformanceReportColumn geoLocationPerformanceReportColumn = new ArrayOfGeoLocationPerformanceReportColumn();
//        geoLocationPerformanceReportColumn.getGeoLocationPerformanceReportColumns().add(GeoLocationPerformanceReportColumn.CLICKS);
//        geoLocationPerformanceReportColumn.getGeoLocationPerformanceReportColumns().add(GeoLocationPerformanceReportColumn.IMPRESSIONS);
//        geoLocationPerformanceReportColumn.getGeoLocationPerformanceReportColumns().add(GeoLocationPerformanceReportColumn.AVERAGE_CPC);
//        geoLocationPerformanceReportColumn.getGeoLocationPerformanceReportColumns().add(GeoLocationPerformanceReportColumn.CTR);
//        geoLocationPerformanceReportColumn.getGeoLocationPerformanceReportColumns().add(GeoLocationPerformanceReportColumn.SPEND);
//        geoLocationPerformanceReportColumn.getGeoLocationPerformanceReportColumns().add(GeoLocationPerformanceReportColumn.CONVERSIONS);
//        geoLocationPerformanceReportColumn.getGeoLocationPerformanceReportColumns().add(GeoLocationPerformanceReportColumn.COST_PER_CONVERSION);
//        geoLocationPerformanceReportColumn.getGeoLocationPerformanceReportColumns().add(GeoLocationPerformanceReportColumn.CONVERSION_RATE);
//        if (aggregation == null || aggregation.isEmpty()) {
//        } else {
//            geoLocationPerformanceReportColumn.getGeoLocationPerformanceReportColumns().add(GeoLocationPerformanceReportColumn.TIME_PERIOD);
//        }
//        geoLocationPerformanceReportColumn.getGeoLocationPerformanceReportColumns().add(GeoLocationPerformanceReportColumn.CITY);
//        geoLocationPerformanceReportColumn.getGeoLocationPerformanceReportColumns().add(GeoLocationPerformanceReportColumn.COUNTRY);
//        geoLocationPerformanceReportColumn.getGeoLocationPerformanceReportColumns().add(GeoLocationPerformanceReportColumn.ACCOUNT_NAME);
//        geoLocationPerformanceReportColumn.getGeoLocationPerformanceReportColumns().add(GeoLocationPerformanceReportColumn.STATE);
//        geoLocationPerformanceReportColumn.getGeoLocationPerformanceReportColumns().add(GeoLocationPerformanceReportColumn.AVERAGE_POSITION);
//
//        report.setColumns(geoLocationPerformanceReportColumn);
//        return report;
//    }
//
//    private ReportRequest getGeoZipLocationPerformanceReportRequest(Date startDate, Date endDate) {
//        GeoLocationPerformanceReportRequest report = new GeoLocationPerformanceReportRequest();
//        ReportFormat ReportFileFormat = ReportFormat.XML;
//        report.setFormat(ReportFileFormat);
//        report.setReportName("My Keyword Performance Report");
//        report.setReturnOnlyCompleteData(false);
//        report.setAggregation(NonHourlyReportAggregation.DAILY);
//
//        ArrayOflong accountIds = new ArrayOflong();
//        accountIds.getLongs().add(authorizationData.getAccountId());
//
//        report.setScope(new AccountThroughAdGroupReportScope());
//        report.getScope().setAccountIds(accountIds);
//        report.setTime(new ReportTime());
//        // report.getTime().setPredefinedTime(ReportTimePeriod.YESTERDAY);
//        /* Start Date */
//        Calendar startCal = Calendar.getInstance();
//        startCal.setTime(startDate);
//        report.getTime().setCustomDateRangeStart(new com.microsoft.bingads.reporting.Date());
//        report.getTime().getCustomDateRangeStart().setDay(startCal.get(Calendar.DAY_OF_MONTH));
//        report.getTime().getCustomDateRangeStart().setMonth(startCal.get(Calendar.MONTH) + 1);
//        report.getTime().getCustomDateRangeStart().setYear(startCal.get(Calendar.YEAR));
//
//        // End Date 
//        Calendar endCal = Calendar.getInstance();
//        endCal.setTime(endDate);
//        report.getTime().setCustomDateRangeEnd(new com.microsoft.bingads.reporting.Date());
//        System.out.println(endCal.get(Calendar.DAY_OF_MONTH));
//        System.out.println(endCal.get(Calendar.MONTH) + 1);
//        System.out.println(endCal.get(Calendar.YEAR));
//        report.getTime().getCustomDateRangeEnd().setDay(endCal.get(Calendar.DAY_OF_MONTH));
//        report.getTime().getCustomDateRangeEnd().setMonth(endCal.get(Calendar.MONTH) + 1);
//        report.getTime().getCustomDateRangeEnd().setYear(endCal.get(Calendar.YEAR));
//
//        ArrayOfGeoLocationPerformanceReportColumn geoLocationPerformanceReportColumn = new ArrayOfGeoLocationPerformanceReportColumn();
//        geoLocationPerformanceReportColumn.getGeoLocationPerformanceReportColumns().add(GeoLocationPerformanceReportColumn.CLICKS);
//        geoLocationPerformanceReportColumn.getGeoLocationPerformanceReportColumns().add(GeoLocationPerformanceReportColumn.IMPRESSIONS);
//        geoLocationPerformanceReportColumn.getGeoLocationPerformanceReportColumns().add(GeoLocationPerformanceReportColumn.AVERAGE_CPC);
//        geoLocationPerformanceReportColumn.getGeoLocationPerformanceReportColumns().add(GeoLocationPerformanceReportColumn.CTR);
//        geoLocationPerformanceReportColumn.getGeoLocationPerformanceReportColumns().add(GeoLocationPerformanceReportColumn.SPEND);
//        geoLocationPerformanceReportColumn.getGeoLocationPerformanceReportColumns().add(GeoLocationPerformanceReportColumn.CONVERSIONS);
//        geoLocationPerformanceReportColumn.getGeoLocationPerformanceReportColumns().add(GeoLocationPerformanceReportColumn.COST_PER_CONVERSION);
//        geoLocationPerformanceReportColumn.getGeoLocationPerformanceReportColumns().add(GeoLocationPerformanceReportColumn.CONVERSION_RATE);
//        geoLocationPerformanceReportColumn.getGeoLocationPerformanceReportColumns().add(GeoLocationPerformanceReportColumn.TIME_PERIOD);
//        geoLocationPerformanceReportColumn.getGeoLocationPerformanceReportColumns().add(GeoLocationPerformanceReportColumn.COUNTRY);
//        geoLocationPerformanceReportColumn.getGeoLocationPerformanceReportColumns().add(GeoLocationPerformanceReportColumn.ACCOUNT_NAME);
//        geoLocationPerformanceReportColumn.getGeoLocationPerformanceReportColumns().add(GeoLocationPerformanceReportColumn.STATE);
//        geoLocationPerformanceReportColumn.getGeoLocationPerformanceReportColumns().add(GeoLocationPerformanceReportColumn.AVERAGE_POSITION);
//        geoLocationPerformanceReportColumn.getGeoLocationPerformanceReportColumns().add(GeoLocationPerformanceReportColumn.MOST_SPECIFIC_LOCATION);
//
//        report.setColumns(geoLocationPerformanceReportColumn);
//        return report;
//    }
//
//    private ReportRequest getAdPerformaceReportRequest(Date startDate, Date endDate) {
//        AdPerformanceReportRequest report = new AdPerformanceReportRequest();
//        ReportFormat ReportFileFormat = ReportFormat.XML;
//        report.setFormat(ReportFileFormat);
//        report.setReportName("My Keyword Performance Report");
//        report.setReturnOnlyCompleteData(false);
//        report.setAggregation(NonHourlyReportAggregation.DAILY);
//
//        ArrayOflong accountIds = new ArrayOflong();
//        accountIds.getLongs().add(authorizationData.getAccountId());
//
//        report.setScope(new AccountThroughAdGroupReportScope());
//        report.getScope().setAccountIds(accountIds);
//        report.setTime(new ReportTime());
//
//        /* Start Date */
//        Calendar startCal = Calendar.getInstance();
//        startCal.setTime(startDate);
//        report.getTime().setCustomDateRangeStart(new com.microsoft.bingads.reporting.Date());
//        report.getTime().getCustomDateRangeStart().setDay(startCal.get(Calendar.DAY_OF_MONTH));
//        report.getTime().getCustomDateRangeStart().setMonth(startCal.get(Calendar.MONTH) + 1);
//        report.getTime().getCustomDateRangeStart().setYear(startCal.get(Calendar.YEAR));
//
//        // End Date 
//        Calendar endCal = Calendar.getInstance();
//        endCal.setTime(endDate);
//        report.getTime().setCustomDateRangeEnd(new com.microsoft.bingads.reporting.Date());
//        System.out.println(endCal.get(Calendar.DAY_OF_MONTH));
//        System.out.println(endCal.get(Calendar.MONTH) + 1);
//        System.out.println(endCal.get(Calendar.YEAR));
//        report.getTime().getCustomDateRangeEnd().setDay(endCal.get(Calendar.DAY_OF_MONTH));
//        report.getTime().getCustomDateRangeEnd().setMonth(endCal.get(Calendar.MONTH) + 1);
//        report.getTime().getCustomDateRangeEnd().setYear(endCal.get(Calendar.YEAR));
//
//        ArrayOfAdPerformanceReportColumn adPerformanceReportColumn = new ArrayOfAdPerformanceReportColumn();
//        adPerformanceReportColumn.getAdPerformanceReportColumns().add(AdPerformanceReportColumn.ACCOUNT_NAME);
//        adPerformanceReportColumn.getAdPerformanceReportColumns().add(AdPerformanceReportColumn.ACCOUNT_ID);
//        adPerformanceReportColumn.getAdPerformanceReportColumns().add(AdPerformanceReportColumn.CLICKS);
//        adPerformanceReportColumn.getAdPerformanceReportColumns().add(AdPerformanceReportColumn.IMPRESSIONS);
//        adPerformanceReportColumn.getAdPerformanceReportColumns().add(AdPerformanceReportColumn.AVERAGE_CPC);
//        adPerformanceReportColumn.getAdPerformanceReportColumns().add(AdPerformanceReportColumn.CTR);
//        adPerformanceReportColumn.getAdPerformanceReportColumns().add(AdPerformanceReportColumn.SPEND);
//        adPerformanceReportColumn.getAdPerformanceReportColumns().add(AdPerformanceReportColumn.CONVERSIONS);
//        adPerformanceReportColumn.getAdPerformanceReportColumns().add(AdPerformanceReportColumn.COST_PER_CONVERSION);
//        adPerformanceReportColumn.getAdPerformanceReportColumns().add(AdPerformanceReportColumn.CONVERSION_RATE);
//        adPerformanceReportColumn.getAdPerformanceReportColumns().add(AdPerformanceReportColumn.FINAL_URL);
//        adPerformanceReportColumn.getAdPerformanceReportColumns().add(AdPerformanceReportColumn.CAMPAIGN_NAME);
//        adPerformanceReportColumn.getAdPerformanceReportColumns().add(AdPerformanceReportColumn.AD_GROUP_NAME);
//        adPerformanceReportColumn.getAdPerformanceReportColumns().add(AdPerformanceReportColumn.TIME_PERIOD);
//        adPerformanceReportColumn.getAdPerformanceReportColumns().add(AdPerformanceReportColumn.AD_DESCRIPTION);
//        adPerformanceReportColumn.getAdPerformanceReportColumns().add(AdPerformanceReportColumn.AD_TITLE);
//        adPerformanceReportColumn.getAdPerformanceReportColumns().add(AdPerformanceReportColumn.CAMPAIGN_ID);
//        adPerformanceReportColumn.getAdPerformanceReportColumns().add(AdPerformanceReportColumn.AD_GROUP_ID);
//        adPerformanceReportColumn.getAdPerformanceReportColumns().add(AdPerformanceReportColumn.DISPLAY_URL);
//        adPerformanceReportColumn.getAdPerformanceReportColumns().add(AdPerformanceReportColumn.AVERAGE_POSITION);
//        adPerformanceReportColumn.getAdPerformanceReportColumns().add(AdPerformanceReportColumn.DESTINATION_URL);
//
//        report.setColumns(adPerformanceReportColumn);
//        return report;
//    }
//
//    private ReportRequest getAccountPerformaceReportRequest(Date startDate, Date endDate, String aggregation) {
//        AccountPerformanceReportRequest report = new AccountPerformanceReportRequest();
//        ReportFormat ReportFileFormat = ReportFormat.XML;
//        report.setFormat(ReportFileFormat);
//        report.setReportName("My Keyword Performance Report");
//        report.setReturnOnlyCompleteData(false);
//        if (aggregation.equalsIgnoreCase("weekly")) {
//            report.setAggregation(ReportAggregation.WEEKLY);
//        } else if (aggregation.equalsIgnoreCase("dayOfWeek")) {
//            report.setAggregation(ReportAggregation.DAY_OF_WEEK);
//        } else if (aggregation.equalsIgnoreCase("hourOfDay")) {
//            report.setAggregation(ReportAggregation.HOUR_OF_DAY);
//        } else if (aggregation.isEmpty()) {
//            report.setAggregation(ReportAggregation.SUMMARY);
//        } else {
//            report.setAggregation(ReportAggregation.DAILY);
//        }
//
//        ArrayOflong accountIds = new ArrayOflong();
//        accountIds.getLongs().add(authorizationData.getAccountId());
//
//        report.setScope(new AccountReportScope());
//        report.getScope().setAccountIds(accountIds);
//        report.setTime(new ReportTime());
//        // report.getTime().setPredefinedTime(ReportTimePeriod.YESTERDAY);
//        /* Start Date */
//        Calendar startCal = Calendar.getInstance();
//        startCal.setTime(startDate);
//        report.getTime().setCustomDateRangeStart(new com.microsoft.bingads.reporting.Date());
//        report.getTime().getCustomDateRangeStart().setDay(startCal.get(Calendar.DAY_OF_MONTH));
//        report.getTime().getCustomDateRangeStart().setMonth(startCal.get(Calendar.MONTH) + 1);
//        report.getTime().getCustomDateRangeStart().setYear(startCal.get(Calendar.YEAR));
//
//        // End Date 
//        Calendar endCal = Calendar.getInstance();
//        endCal.setTime(endDate);
//        report.getTime().setCustomDateRangeEnd(new com.microsoft.bingads.reporting.Date());
//        System.out.println(endCal.get(Calendar.DAY_OF_MONTH));
//        System.out.println(endCal.get(Calendar.MONTH) + 1);
//        System.out.println(endCal.get(Calendar.YEAR));
//        report.getTime().getCustomDateRangeEnd().setDay(endCal.get(Calendar.DAY_OF_MONTH));
//        report.getTime().getCustomDateRangeEnd().setMonth(endCal.get(Calendar.MONTH) + 1);
//        report.getTime().getCustomDateRangeEnd().setYear(endCal.get(Calendar.YEAR));
//
//        ArrayOfAccountPerformanceReportColumn accountPerformanceReportColumn = new ArrayOfAccountPerformanceReportColumn();
//        accountPerformanceReportColumn.getAccountPerformanceReportColumns().add(AccountPerformanceReportColumn.IMPRESSIONS);
//        accountPerformanceReportColumn.getAccountPerformanceReportColumns().add(AccountPerformanceReportColumn.ACCOUNT_ID);
//        accountPerformanceReportColumn.getAccountPerformanceReportColumns().add(AccountPerformanceReportColumn.CLICKS);
//        accountPerformanceReportColumn.getAccountPerformanceReportColumns().add(AccountPerformanceReportColumn.CTR);
//        accountPerformanceReportColumn.getAccountPerformanceReportColumns().add(AccountPerformanceReportColumn.AVERAGE_CPC);
//        accountPerformanceReportColumn.getAccountPerformanceReportColumns().add(AccountPerformanceReportColumn.SPEND);
//        accountPerformanceReportColumn.getAccountPerformanceReportColumns().add(AccountPerformanceReportColumn.CONVERSIONS);
//        accountPerformanceReportColumn.getAccountPerformanceReportColumns().add(AccountPerformanceReportColumn.CONVERSION_RATE);
//        accountPerformanceReportColumn.getAccountPerformanceReportColumns().add(AccountPerformanceReportColumn.COST_PER_CONVERSION);
//        accountPerformanceReportColumn.getAccountPerformanceReportColumns().add(AccountPerformanceReportColumn.ACCOUNT_NAME);
//        if (!(aggregation.equalsIgnoreCase("dayOfWeek") || aggregation.equalsIgnoreCase("hourOfDay"))) {
//            accountPerformanceReportColumn.getAccountPerformanceReportColumns().add(AccountPerformanceReportColumn.IMPRESSION_SHARE_PERCENT);
//            accountPerformanceReportColumn.getAccountPerformanceReportColumns().add(AccountPerformanceReportColumn.IMPRESSION_LOST_TO_BUDGET_PERCENT);
//            accountPerformanceReportColumn.getAccountPerformanceReportColumns().add(AccountPerformanceReportColumn.IMPRESSION_LOST_TO_RANK_PERCENT);
//        }
//        if (!aggregation.isEmpty()) {
//            accountPerformanceReportColumn.getAccountPerformanceReportColumns().add(AccountPerformanceReportColumn.TIME_PERIOD);
//        }
//        accountPerformanceReportColumn.getAccountPerformanceReportColumns().add(AccountPerformanceReportColumn.AVERAGE_POSITION);
//        accountPerformanceReportColumn.getAccountPerformanceReportColumns().add(AccountPerformanceReportColumn.PHONE_CALLS);
//        report.setColumns(accountPerformanceReportColumn);
//        return report;
//    }
//
//    private ReportRequest getAccountDayOfWeekPerformaceReportRequest(Date startDate, Date endDate) {
//        AccountPerformanceReportRequest report = new AccountPerformanceReportRequest();
//        ReportFormat ReportFileFormat = ReportFormat.CSV;
//        report.setFormat(ReportFileFormat);
//        report.setReportName("My Keyword Performance Report");
//        report.setReturnOnlyCompleteData(false);
//        report.setAggregation(ReportAggregation.DAY_OF_WEEK);
//
//        ArrayOflong accountIds = new ArrayOflong();
//        accountIds.getLongs().add(authorizationData.getAccountId());
//
//        report.setScope(new AccountReportScope());
//        report.getScope().setAccountIds(accountIds);
//        report.setTime(new ReportTime());
//        // report.getTime().setPredefinedTime(ReportTimePeriod.YESTERDAY);
//        /* Start Date */
//        Calendar startCal = Calendar.getInstance();
//        startCal.setTime(startDate);
//        report.getTime().setCustomDateRangeStart(new com.microsoft.bingads.reporting.Date());
//        report.getTime().getCustomDateRangeStart().setDay(startCal.get(Calendar.DAY_OF_MONTH));
//        report.getTime().getCustomDateRangeStart().setMonth(startCal.get(Calendar.MONTH) + 1);
//        report.getTime().getCustomDateRangeStart().setYear(startCal.get(Calendar.YEAR));
//
//        // End Date 
//        Calendar endCal = Calendar.getInstance();
//        endCal.setTime(endDate);
//        report.getTime().setCustomDateRangeEnd(new com.microsoft.bingads.reporting.Date());
//        System.out.println(endCal.get(Calendar.DAY_OF_MONTH));
//        System.out.println(endCal.get(Calendar.MONTH) + 1);
//        System.out.println(endCal.get(Calendar.YEAR));
//        report.getTime().getCustomDateRangeEnd().setDay(endCal.get(Calendar.DAY_OF_MONTH));
//        report.getTime().getCustomDateRangeEnd().setMonth(endCal.get(Calendar.MONTH) + 1);
//        report.getTime().getCustomDateRangeEnd().setYear(endCal.get(Calendar.YEAR));
//
//        ArrayOfAccountPerformanceReportColumn accountPerformanceReportColumn = new ArrayOfAccountPerformanceReportColumn();
//        accountPerformanceReportColumn.getAccountPerformanceReportColumns().add(AccountPerformanceReportColumn.IMPRESSIONS);
//        accountPerformanceReportColumn.getAccountPerformanceReportColumns().add(AccountPerformanceReportColumn.ACCOUNT_ID);
//        accountPerformanceReportColumn.getAccountPerformanceReportColumns().add(AccountPerformanceReportColumn.CLICKS);
//        accountPerformanceReportColumn.getAccountPerformanceReportColumns().add(AccountPerformanceReportColumn.CTR);
//        accountPerformanceReportColumn.getAccountPerformanceReportColumns().add(AccountPerformanceReportColumn.AVERAGE_CPC);
//        accountPerformanceReportColumn.getAccountPerformanceReportColumns().add(AccountPerformanceReportColumn.SPEND);
//        accountPerformanceReportColumn.getAccountPerformanceReportColumns().add(AccountPerformanceReportColumn.CONVERSIONS);
//        accountPerformanceReportColumn.getAccountPerformanceReportColumns().add(AccountPerformanceReportColumn.CONVERSION_RATE);
//        accountPerformanceReportColumn.getAccountPerformanceReportColumns().add(AccountPerformanceReportColumn.COST_PER_CONVERSION);
//        accountPerformanceReportColumn.getAccountPerformanceReportColumns().add(AccountPerformanceReportColumn.ACCOUNT_NAME);
//        accountPerformanceReportColumn.getAccountPerformanceReportColumns().add(AccountPerformanceReportColumn.IMPRESSION_SHARE_PERCENT);
//        accountPerformanceReportColumn.getAccountPerformanceReportColumns().add(AccountPerformanceReportColumn.IMPRESSION_LOST_TO_BUDGET_PERCENT);
//        accountPerformanceReportColumn.getAccountPerformanceReportColumns().add(AccountPerformanceReportColumn.IMPRESSION_LOST_TO_RANK_PERCENT);
//        accountPerformanceReportColumn.getAccountPerformanceReportColumns().add(AccountPerformanceReportColumn.AVERAGE_POSITION);
//        accountPerformanceReportColumn.getAccountPerformanceReportColumns().add(AccountPerformanceReportColumn.TIME_PERIOD);
//        accountPerformanceReportColumn.getAccountPerformanceReportColumns().add(AccountPerformanceReportColumn.PHONE_CALLS);
//        report.setColumns(accountPerformanceReportColumn);
//        return report;
//    }
//
//    private ReportRequest getAccountHourOfDayPerformaceReportRequest(Date startDate, Date endDate) {
//        AccountPerformanceReportRequest report = new AccountPerformanceReportRequest();
//        ReportFormat ReportFileFormat = ReportFormat.XML;
//        report.setFormat(ReportFileFormat);
//        report.setReportName("My Keyword Performance Report");
//        report.setReturnOnlyCompleteData(false);
//        report.setAggregation(ReportAggregation.HOUR_OF_DAY);
//
//        ArrayOflong accountIds = new ArrayOflong();
//        accountIds.getLongs().add(authorizationData.getAccountId());
//
//        report.setScope(new AccountReportScope());
//        report.getScope().setAccountIds(accountIds);
//        report.setTime(new ReportTime());
//        // report.getTime().setPredefinedTime(ReportTimePeriod.YESTERDAY);
//        /* Start Date */
//        Calendar startCal = Calendar.getInstance();
//        startCal.setTime(startDate);
//        report.getTime().setCustomDateRangeStart(new com.microsoft.bingads.reporting.Date());
//        report.getTime().getCustomDateRangeStart().setDay(startCal.get(Calendar.DAY_OF_MONTH));
//        report.getTime().getCustomDateRangeStart().setMonth(startCal.get(Calendar.MONTH) + 1);
//        report.getTime().getCustomDateRangeStart().setYear(startCal.get(Calendar.YEAR));
//
//        // End Date 
//        Calendar endCal = Calendar.getInstance();
//        endCal.setTime(endDate);
//        report.getTime().setCustomDateRangeEnd(new com.microsoft.bingads.reporting.Date());
//        System.out.println(endCal.get(Calendar.DAY_OF_MONTH));
//        System.out.println(endCal.get(Calendar.MONTH) + 1);
//        System.out.println(endCal.get(Calendar.YEAR));
//        report.getTime().getCustomDateRangeEnd().setDay(endCal.get(Calendar.DAY_OF_MONTH));
//        report.getTime().getCustomDateRangeEnd().setMonth(endCal.get(Calendar.MONTH) + 1);
//        report.getTime().getCustomDateRangeEnd().setYear(endCal.get(Calendar.YEAR));
//
//        ArrayOfAccountPerformanceReportColumn accountPerformanceReportColumn = new ArrayOfAccountPerformanceReportColumn();
//        accountPerformanceReportColumn.getAccountPerformanceReportColumns().add(AccountPerformanceReportColumn.IMPRESSIONS);
//        accountPerformanceReportColumn.getAccountPerformanceReportColumns().add(AccountPerformanceReportColumn.ACCOUNT_ID);
//        accountPerformanceReportColumn.getAccountPerformanceReportColumns().add(AccountPerformanceReportColumn.CLICKS);
//        accountPerformanceReportColumn.getAccountPerformanceReportColumns().add(AccountPerformanceReportColumn.CTR);
//        accountPerformanceReportColumn.getAccountPerformanceReportColumns().add(AccountPerformanceReportColumn.AVERAGE_CPC);
//        accountPerformanceReportColumn.getAccountPerformanceReportColumns().add(AccountPerformanceReportColumn.SPEND);
//        accountPerformanceReportColumn.getAccountPerformanceReportColumns().add(AccountPerformanceReportColumn.CONVERSIONS);
//        accountPerformanceReportColumn.getAccountPerformanceReportColumns().add(AccountPerformanceReportColumn.CONVERSION_RATE);
//        accountPerformanceReportColumn.getAccountPerformanceReportColumns().add(AccountPerformanceReportColumn.COST_PER_CONVERSION);
//        accountPerformanceReportColumn.getAccountPerformanceReportColumns().add(AccountPerformanceReportColumn.ACCOUNT_NAME);
//        accountPerformanceReportColumn.getAccountPerformanceReportColumns().add(AccountPerformanceReportColumn.AVERAGE_POSITION);
//        accountPerformanceReportColumn.getAccountPerformanceReportColumns().add(AccountPerformanceReportColumn.TIME_PERIOD);
//        accountPerformanceReportColumn.getAccountPerformanceReportColumns().add(AccountPerformanceReportColumn.PHONE_CALLS);
//        report.setColumns(accountPerformanceReportColumn);
//        return report;
//    }
//
//    private ReportRequest getAccountDevicePerformaceReportRequest(Date startDate, Date endDate, String aggregation) {
//        AccountPerformanceReportRequest report = new AccountPerformanceReportRequest();
//        ReportFormat ReportFileFormat = ReportFormat.XML;
//        report.setFormat(ReportFileFormat);
//        report.setReportName("My Keyword Performance Report");
//        report.setReturnOnlyCompleteData(false);
//        if (aggregation == null || aggregation.isEmpty()) {
//            report.setAggregation(ReportAggregation.SUMMARY);
//        } else {
//            report.setAggregation(ReportAggregation.DAILY);
//        }
//        ArrayOflong accountIds = new ArrayOflong();
//        accountIds.getLongs().add(authorizationData.getAccountId());
//
//        report.setScope(new AccountReportScope());
//        report.getScope().setAccountIds(accountIds);
//        report.setTime(new ReportTime());
//        // report.getTime().setPredefinedTime(ReportTimePeriod.YESTERDAY);
//        /* Start Date */
//        Calendar startCal = Calendar.getInstance();
//        startCal.setTime(startDate);
//        report.getTime().setCustomDateRangeStart(new com.microsoft.bingads.reporting.Date());
//        report.getTime().getCustomDateRangeStart().setDay(startCal.get(Calendar.DAY_OF_MONTH));
//        report.getTime().getCustomDateRangeStart().setMonth(startCal.get(Calendar.MONTH) + 1);
//        report.getTime().getCustomDateRangeStart().setYear(startCal.get(Calendar.YEAR));
//
//        // End Date 
//        Calendar endCal = Calendar.getInstance();
//        endCal.setTime(endDate);
//        report.getTime().setCustomDateRangeEnd(new com.microsoft.bingads.reporting.Date());
//        System.out.println(endCal.get(Calendar.DAY_OF_MONTH));
//        System.out.println(endCal.get(Calendar.MONTH) + 1);
//        System.out.println(endCal.get(Calendar.YEAR));
//        report.getTime().getCustomDateRangeEnd().setDay(endCal.get(Calendar.DAY_OF_MONTH));
//        report.getTime().getCustomDateRangeEnd().setMonth(endCal.get(Calendar.MONTH) + 1);
//        report.getTime().getCustomDateRangeEnd().setYear(endCal.get(Calendar.YEAR));
//
//        ArrayOfAccountPerformanceReportColumn accountPerformanceReportColumn = new ArrayOfAccountPerformanceReportColumn();
//        accountPerformanceReportColumn.getAccountPerformanceReportColumns().add(AccountPerformanceReportColumn.IMPRESSIONS);
//        accountPerformanceReportColumn.getAccountPerformanceReportColumns().add(AccountPerformanceReportColumn.ACCOUNT_ID);
//        accountPerformanceReportColumn.getAccountPerformanceReportColumns().add(AccountPerformanceReportColumn.CLICKS);
//        accountPerformanceReportColumn.getAccountPerformanceReportColumns().add(AccountPerformanceReportColumn.CTR);
//        accountPerformanceReportColumn.getAccountPerformanceReportColumns().add(AccountPerformanceReportColumn.AVERAGE_CPC);
//        accountPerformanceReportColumn.getAccountPerformanceReportColumns().add(AccountPerformanceReportColumn.SPEND);
//        accountPerformanceReportColumn.getAccountPerformanceReportColumns().add(AccountPerformanceReportColumn.CONVERSIONS);
//        accountPerformanceReportColumn.getAccountPerformanceReportColumns().add(AccountPerformanceReportColumn.CONVERSION_RATE);
//        accountPerformanceReportColumn.getAccountPerformanceReportColumns().add(AccountPerformanceReportColumn.COST_PER_CONVERSION);
//        accountPerformanceReportColumn.getAccountPerformanceReportColumns().add(AccountPerformanceReportColumn.ACCOUNT_NAME);
//        accountPerformanceReportColumn.getAccountPerformanceReportColumns().add(AccountPerformanceReportColumn.IMPRESSION_SHARE_PERCENT);
//        accountPerformanceReportColumn.getAccountPerformanceReportColumns().add(AccountPerformanceReportColumn.IMPRESSION_LOST_TO_BUDGET_PERCENT);
//        accountPerformanceReportColumn.getAccountPerformanceReportColumns().add(AccountPerformanceReportColumn.IMPRESSION_LOST_TO_RANK_PERCENT);
//        accountPerformanceReportColumn.getAccountPerformanceReportColumns().add(AccountPerformanceReportColumn.AVERAGE_POSITION);
//        accountPerformanceReportColumn.getAccountPerformanceReportColumns().add(AccountPerformanceReportColumn.DEVICE_TYPE);
//        if (aggregation == null || aggregation.isEmpty()) {
//        } else {
//            accountPerformanceReportColumn.getAccountPerformanceReportColumns().add(AccountPerformanceReportColumn.TIME_PERIOD);
//        }
//        accountPerformanceReportColumn.getAccountPerformanceReportColumns().add(AccountPerformanceReportColumn.PHONE_CALLS);
//        report.setColumns(accountPerformanceReportColumn);
//        return report;
//    }
//
//    private ReportRequest getCampaignPerformaceReportRequest(Date startDate, Date endDate, String aggregation) {
//        CampaignPerformanceReportRequest report = new CampaignPerformanceReportRequest();
//        ReportFormat ReportFileFormat = ReportFormat.XML;
//        report.setFormat(ReportFileFormat);
//        report.setReportName("My Keyword Performance Report");
//        report.setReturnOnlyCompleteData(false);
//        if (aggregation == null || aggregation.isEmpty()) {
//            report.setAggregation(ReportAggregation.SUMMARY);
//
//        } else {
//            report.setAggregation(ReportAggregation.DAILY);
//        }
//        ArrayOflong accountIds = new ArrayOflong();
//        accountIds.getLongs().add(authorizationData.getAccountId());
//
//        report.setScope(new AccountThroughCampaignReportScope());
//        report.getScope().setAccountIds(accountIds);
//        report.setTime(new ReportTime());
//        // report.getTime().setPredefinedTime(ReportTimePeriod.YESTERDAY);
//        /* Start Date */
//        Calendar startCal = Calendar.getInstance();
//        startCal.setTime(startDate);
//        report.getTime().setCustomDateRangeStart(new com.microsoft.bingads.reporting.Date());
//        report.getTime().getCustomDateRangeStart().setDay(startCal.get(Calendar.DAY_OF_MONTH));
//        report.getTime().getCustomDateRangeStart().setMonth(startCal.get(Calendar.MONTH) + 1);
//        report.getTime().getCustomDateRangeStart().setYear(startCal.get(Calendar.YEAR));
//
//        // End Date 
//        Calendar endCal = Calendar.getInstance();
//        endCal.setTime(endDate);
//        report.getTime().setCustomDateRangeEnd(new com.microsoft.bingads.reporting.Date());
//        System.out.println(endCal.get(Calendar.DAY_OF_MONTH));
//        System.out.println(endCal.get(Calendar.MONTH) + 1);
//        System.out.println(endCal.get(Calendar.YEAR));
//        report.getTime().getCustomDateRangeEnd().setDay(endCal.get(Calendar.DAY_OF_MONTH));
//        report.getTime().getCustomDateRangeEnd().setMonth(endCal.get(Calendar.MONTH) + 1);
//        report.getTime().getCustomDateRangeEnd().setYear(endCal.get(Calendar.YEAR));
//
//        ArrayOfCampaignPerformanceReportColumn campaignPerformanceReportColumn = new ArrayOfCampaignPerformanceReportColumn();
//        campaignPerformanceReportColumn.getCampaignPerformanceReportColumns().add(CampaignPerformanceReportColumn.IMPRESSIONS);
//        campaignPerformanceReportColumn.getCampaignPerformanceReportColumns().add(CampaignPerformanceReportColumn.ACCOUNT_ID);
//        campaignPerformanceReportColumn.getCampaignPerformanceReportColumns().add(CampaignPerformanceReportColumn.CLICKS);
//        campaignPerformanceReportColumn.getCampaignPerformanceReportColumns().add(CampaignPerformanceReportColumn.CTR);
//        campaignPerformanceReportColumn.getCampaignPerformanceReportColumns().add(CampaignPerformanceReportColumn.AVERAGE_CPC);
//        campaignPerformanceReportColumn.getCampaignPerformanceReportColumns().add(CampaignPerformanceReportColumn.SPEND);
//        campaignPerformanceReportColumn.getCampaignPerformanceReportColumns().add(CampaignPerformanceReportColumn.CONVERSIONS);
//        campaignPerformanceReportColumn.getCampaignPerformanceReportColumns().add(CampaignPerformanceReportColumn.CONVERSION_RATE);
//        campaignPerformanceReportColumn.getCampaignPerformanceReportColumns().add(CampaignPerformanceReportColumn.COST_PER_CONVERSION);
//        campaignPerformanceReportColumn.getCampaignPerformanceReportColumns().add(CampaignPerformanceReportColumn.ACCOUNT_NAME);
//        campaignPerformanceReportColumn.getCampaignPerformanceReportColumns().add(CampaignPerformanceReportColumn.IMPRESSION_SHARE_PERCENT);
//        campaignPerformanceReportColumn.getCampaignPerformanceReportColumns().add(CampaignPerformanceReportColumn.IMPRESSION_LOST_TO_BUDGET_PERCENT);
//        campaignPerformanceReportColumn.getCampaignPerformanceReportColumns().add(CampaignPerformanceReportColumn.IMPRESSION_LOST_TO_RANK_PERCENT);
//        campaignPerformanceReportColumn.getCampaignPerformanceReportColumns().add(CampaignPerformanceReportColumn.AVERAGE_POSITION);
//        if (aggregation != null && !aggregation.isEmpty()) {
//            campaignPerformanceReportColumn.getCampaignPerformanceReportColumns().add(CampaignPerformanceReportColumn.TIME_PERIOD);
//        }
//        campaignPerformanceReportColumn.getCampaignPerformanceReportColumns().add(CampaignPerformanceReportColumn.CAMPAIGN_NAME);
//        campaignPerformanceReportColumn.getCampaignPerformanceReportColumns().add(CampaignPerformanceReportColumn.CAMPAIGN_ID);
//        campaignPerformanceReportColumn.getCampaignPerformanceReportColumns().add(CampaignPerformanceReportColumn.PHONE_CALLS);
//        report.setColumns(campaignPerformanceReportColumn);
//        return report;
//    }
//
//    private ReportRequest getCampaignDevicePerformaceReportRequest(Date startDate, Date endDate, String aggregation) {
//        CampaignPerformanceReportRequest report = new CampaignPerformanceReportRequest();
//        ReportFormat ReportFileFormat = ReportFormat.XML;
//        report.setFormat(ReportFileFormat);
//        report.setReportName("My Keyword Performance Report");
//        report.setReturnOnlyCompleteData(false);
//        if (aggregation == null || aggregation.isEmpty()) {
//            report.setAggregation(ReportAggregation.SUMMARY);
//        } else {
//            report.setAggregation(ReportAggregation.DAILY);
//        }
//
//        ArrayOflong accountIds = new ArrayOflong();
//        accountIds.getLongs().add(authorizationData.getAccountId());
//
//        report.setScope(new AccountThroughCampaignReportScope());
//        report.getScope().setAccountIds(accountIds);
//        report.setTime(new ReportTime());
//        //report.setTime(new ReportTime());
//        // report.getTime().setPredefinedTime(ReportTimePeriod.YESTERDAY);
//        /* Start Date */
//        Calendar startCal = Calendar.getInstance();
//        startCal.setTime(startDate);
//        report.getTime().setCustomDateRangeStart(new com.microsoft.bingads.reporting.Date());
//        report.getTime().getCustomDateRangeStart().setDay(startCal.get(Calendar.DAY_OF_MONTH));
//        report.getTime().getCustomDateRangeStart().setMonth(startCal.get(Calendar.MONTH) + 1);
//        report.getTime().getCustomDateRangeStart().setYear(startCal.get(Calendar.YEAR));
//
//        // End Date 
//        Calendar endCal = Calendar.getInstance();
//        endCal.setTime(endDate);
//        report.getTime().setCustomDateRangeEnd(new com.microsoft.bingads.reporting.Date());
//        System.out.println(endCal.get(Calendar.DAY_OF_MONTH));
//        System.out.println(endCal.get(Calendar.MONTH) + 1);
//        System.out.println(endCal.get(Calendar.YEAR));
//        report.getTime().getCustomDateRangeEnd().setDay(endCal.get(Calendar.DAY_OF_MONTH));
//        report.getTime().getCustomDateRangeEnd().setMonth(endCal.get(Calendar.MONTH) + 1);
//        report.getTime().getCustomDateRangeEnd().setYear(endCal.get(Calendar.YEAR));
//
//        ArrayOfCampaignPerformanceReportColumn campaignPerformanceReportColumn = new ArrayOfCampaignPerformanceReportColumn();
//        campaignPerformanceReportColumn.getCampaignPerformanceReportColumns().add(CampaignPerformanceReportColumn.IMPRESSIONS);
//        campaignPerformanceReportColumn.getCampaignPerformanceReportColumns().add(CampaignPerformanceReportColumn.ACCOUNT_ID);
//        campaignPerformanceReportColumn.getCampaignPerformanceReportColumns().add(CampaignPerformanceReportColumn.CLICKS);
//        campaignPerformanceReportColumn.getCampaignPerformanceReportColumns().add(CampaignPerformanceReportColumn.CTR);
//        campaignPerformanceReportColumn.getCampaignPerformanceReportColumns().add(CampaignPerformanceReportColumn.AVERAGE_CPC);
//        campaignPerformanceReportColumn.getCampaignPerformanceReportColumns().add(CampaignPerformanceReportColumn.SPEND);
//        campaignPerformanceReportColumn.getCampaignPerformanceReportColumns().add(CampaignPerformanceReportColumn.CONVERSIONS);
//        campaignPerformanceReportColumn.getCampaignPerformanceReportColumns().add(CampaignPerformanceReportColumn.CONVERSION_RATE);
//        campaignPerformanceReportColumn.getCampaignPerformanceReportColumns().add(CampaignPerformanceReportColumn.COST_PER_CONVERSION);
//        campaignPerformanceReportColumn.getCampaignPerformanceReportColumns().add(CampaignPerformanceReportColumn.ACCOUNT_NAME);
//        campaignPerformanceReportColumn.getCampaignPerformanceReportColumns().add(CampaignPerformanceReportColumn.IMPRESSION_SHARE_PERCENT);
//        campaignPerformanceReportColumn.getCampaignPerformanceReportColumns().add(CampaignPerformanceReportColumn.IMPRESSION_LOST_TO_BUDGET_PERCENT);
//        campaignPerformanceReportColumn.getCampaignPerformanceReportColumns().add(CampaignPerformanceReportColumn.IMPRESSION_LOST_TO_RANK_PERCENT);
//        campaignPerformanceReportColumn.getCampaignPerformanceReportColumns().add(CampaignPerformanceReportColumn.AVERAGE_POSITION);
//        if (aggregation == null || aggregation.isEmpty()) {
//
//        } else {
//            campaignPerformanceReportColumn.getCampaignPerformanceReportColumns().add(CampaignPerformanceReportColumn.TIME_PERIOD);
//        }
//        campaignPerformanceReportColumn.getCampaignPerformanceReportColumns().add(CampaignPerformanceReportColumn.CAMPAIGN_NAME);
//        campaignPerformanceReportColumn.getCampaignPerformanceReportColumns().add(CampaignPerformanceReportColumn.CAMPAIGN_ID);
//        campaignPerformanceReportColumn.getCampaignPerformanceReportColumns().add(CampaignPerformanceReportColumn.DEVICE_TYPE);
//        campaignPerformanceReportColumn.getCampaignPerformanceReportColumns().add(CampaignPerformanceReportColumn.PHONE_CALLS);
//        report.setColumns(campaignPerformanceReportColumn);
//        return report;
//    }
//
//    private ReportRequest getAdGroupPerformaceReportRequest(Date startDate, Date endDate, String aggregation) {
//        AdGroupPerformanceReportRequest report = new AdGroupPerformanceReportRequest();
//        ReportFormat ReportFileFormat = ReportFormat.XML;
//        report.setFormat(ReportFileFormat);
//        report.setReportName("My Keyword Performance Report");
//        report.setReturnOnlyCompleteData(false);
//        report.setAggregation(ReportAggregation.DAILY);
//
//        ArrayOflong accountIds = new ArrayOflong();
//        accountIds.getLongs().add(authorizationData.getAccountId());
//
//        report.setScope(new AccountThroughAdGroupReportScope());
//        report.getScope().setAccountIds(accountIds);
//        report.setTime(new ReportTime());
//        report.setTime(new ReportTime());
//        // report.getTime().setPredefinedTime(ReportTimePeriod.YESTERDAY);
//        /* Start Date */
//        Calendar startCal = Calendar.getInstance();
//        startCal.setTime(startDate);
//        report.getTime().setCustomDateRangeStart(new com.microsoft.bingads.reporting.Date());
//        report.getTime().getCustomDateRangeStart().setDay(startCal.get(Calendar.DAY_OF_MONTH));
//        report.getTime().getCustomDateRangeStart().setMonth(startCal.get(Calendar.MONTH) + 1);
//        report.getTime().getCustomDateRangeStart().setYear(startCal.get(Calendar.YEAR));
//
//        // End Date 
//        Calendar endCal = Calendar.getInstance();
//        endCal.setTime(endDate);
//        report.getTime().setCustomDateRangeEnd(new com.microsoft.bingads.reporting.Date());
//        System.out.println(endCal.get(Calendar.DAY_OF_MONTH));
//        System.out.println(endCal.get(Calendar.MONTH) + 1);
//        System.out.println(endCal.get(Calendar.YEAR));
//        report.getTime().getCustomDateRangeEnd().setDay(endCal.get(Calendar.DAY_OF_MONTH));
//        report.getTime().getCustomDateRangeEnd().setMonth(endCal.get(Calendar.MONTH) + 1);
//        report.getTime().getCustomDateRangeEnd().setYear(endCal.get(Calendar.YEAR));
//
//        ArrayOfAdGroupPerformanceReportColumn adGroupPerformanceReportColumn = new ArrayOfAdGroupPerformanceReportColumn();
//        adGroupPerformanceReportColumn.getAdGroupPerformanceReportColumns().add(AdGroupPerformanceReportColumn.IMPRESSIONS);
//        adGroupPerformanceReportColumn.getAdGroupPerformanceReportColumns().add(AdGroupPerformanceReportColumn.ACCOUNT_ID);
//        adGroupPerformanceReportColumn.getAdGroupPerformanceReportColumns().add(AdGroupPerformanceReportColumn.CLICKS);
//        adGroupPerformanceReportColumn.getAdGroupPerformanceReportColumns().add(AdGroupPerformanceReportColumn.CTR);
//        adGroupPerformanceReportColumn.getAdGroupPerformanceReportColumns().add(AdGroupPerformanceReportColumn.AVERAGE_CPC);
//        adGroupPerformanceReportColumn.getAdGroupPerformanceReportColumns().add(AdGroupPerformanceReportColumn.SPEND);
//        adGroupPerformanceReportColumn.getAdGroupPerformanceReportColumns().add(AdGroupPerformanceReportColumn.CONVERSIONS);
//        adGroupPerformanceReportColumn.getAdGroupPerformanceReportColumns().add(AdGroupPerformanceReportColumn.CONVERSION_RATE);
//        adGroupPerformanceReportColumn.getAdGroupPerformanceReportColumns().add(AdGroupPerformanceReportColumn.COST_PER_CONVERSION);
//        adGroupPerformanceReportColumn.getAdGroupPerformanceReportColumns().add(AdGroupPerformanceReportColumn.ACCOUNT_NAME);
//        adGroupPerformanceReportColumn.getAdGroupPerformanceReportColumns().add(AdGroupPerformanceReportColumn.IMPRESSION_SHARE_PERCENT);
//        adGroupPerformanceReportColumn.getAdGroupPerformanceReportColumns().add(AdGroupPerformanceReportColumn.IMPRESSION_LOST_TO_BUDGET_PERCENT);
//        adGroupPerformanceReportColumn.getAdGroupPerformanceReportColumns().add(AdGroupPerformanceReportColumn.IMPRESSION_LOST_TO_RANK_PERCENT);
//        adGroupPerformanceReportColumn.getAdGroupPerformanceReportColumns().add(AdGroupPerformanceReportColumn.AVERAGE_POSITION);
//        if (aggregation == null || aggregation.isEmpty()) {
//
//        } else {
//            adGroupPerformanceReportColumn.getAdGroupPerformanceReportColumns().add(AdGroupPerformanceReportColumn.TIME_PERIOD);
//        }
//        adGroupPerformanceReportColumn.getAdGroupPerformanceReportColumns().add(AdGroupPerformanceReportColumn.CAMPAIGN_NAME);
//        adGroupPerformanceReportColumn.getAdGroupPerformanceReportColumns().add(AdGroupPerformanceReportColumn.CAMPAIGN_ID);
//        adGroupPerformanceReportColumn.getAdGroupPerformanceReportColumns().add(AdGroupPerformanceReportColumn.AD_GROUP_ID);
//        adGroupPerformanceReportColumn.getAdGroupPerformanceReportColumns().add(AdGroupPerformanceReportColumn.AD_GROUP_NAME);
//        adGroupPerformanceReportColumn.getAdGroupPerformanceReportColumns().add(AdGroupPerformanceReportColumn.PHONE_CALLS);
//        report.setColumns(adGroupPerformanceReportColumn);
//        return report;
//    }
}
