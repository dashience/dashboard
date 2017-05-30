///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package test;
//
//import com.microsoft.bingads.AuthorizationData;
//import com.microsoft.bingads.PasswordAuthentication;
//import com.microsoft.bingads.reporting.AccountPerformanceReportColumn;
//import com.microsoft.bingads.reporting.AccountPerformanceReportRequest;
//import com.microsoft.bingads.reporting.AccountReportScope;
//import com.microsoft.bingads.reporting.ArrayOfAccountPerformanceReportColumn;
//import com.microsoft.bingads.reporting.ArrayOflong;
//import com.microsoft.bingads.reporting.ReportAggregation;
//import com.microsoft.bingads.reporting.ReportFormat;
//import com.microsoft.bingads.reporting.ReportRequest;
//import com.microsoft.bingads.reporting.ReportTime;
//import com.microsoft.bingads.reporting.ReportingDownloadParameters;
//import com.microsoft.bingads.reporting.ReportingServiceManager;
//import com.visumbu.api.bing.report.xml.bean.AccountPerformanceReport;
//import static com.visumbu.vb.admin.service.BingService.tmpDir;
//import com.visumbu.vb.utils.DateUtils;
//import com.visumbu.vb.utils.FileReader;
//import java.io.File;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.List;
//import java.util.concurrent.ExecutionException;
//import java.util.concurrent.TimeUnit;
//import java.util.concurrent.TimeoutException;
//import org.apache.commons.lang3.RandomStringUtils;
//
///**
// *
// * @author deldot
// */
//public class BingServicesTest {
//    private AuthorizationData authorizationData = new AuthorizationData();
//    public static final String DEVELOPER_TOKEN = "00074192B2151803";
//    public static final String USER_NAME = "API_L2TMediaNew";
//    public static final String PASSWORD = "l2ttangocode2016";
//    public static final Long customerId = 14195800L;
//    //public static final Long accountId = 2610614L;
//    public static final String tmpDir = "/tmp/";
//    
//    public static void main(String args[]) throws InterruptedException, ExecutionException, TimeoutException{
//        Date startDate = DateUtils.get30DaysBack();
//        Date endDate=new Date();
//        Long accountId = 2610614L;
//        String aggregation="daily";
//        List report=(List) getAccountPerformanceReport(startDate,endDate,accountId,aggregation);
//    }
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
//      private static List getAccountPerformanceReport(Date startDate, Date endDate, Long accountId, 
//              String aggregation)
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
//      private static ReportRequest getAccountPerformaceReportRequest(Date startDate, Date endDate, String aggregation) {
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
//    
//    
//    
////    
//}
