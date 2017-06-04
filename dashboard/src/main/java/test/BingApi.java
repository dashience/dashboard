/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import com.microsoft.bingads.AuthorizationData;
import com.microsoft.bingads.PasswordAuthentication;
import com.microsoft.bingads.ServiceClient;
import com.microsoft.bingads.customermanagement.AdApiFaultDetail_Exception;
import com.microsoft.bingads.customermanagement.ApiFault_Exception;
import com.microsoft.bingads.customermanagement.GetUserRequest;
import com.microsoft.bingads.customermanagement.ICustomerManagementService;
import com.microsoft.bingads.customermanagement.User;
import com.microsoft.bingads.reporting.AccountPerformanceReportColumn;
import com.microsoft.bingads.reporting.AccountPerformanceReportRequest;
import com.microsoft.bingads.reporting.AccountReportScope;
import com.microsoft.bingads.reporting.AccountThroughAdGroupReportScope;
import com.microsoft.bingads.reporting.AdPerformanceReportColumn;
import com.microsoft.bingads.reporting.AdPerformanceReportRequest;
import com.microsoft.bingads.reporting.ArrayOfAccountPerformanceReportColumn;
import com.microsoft.bingads.reporting.ArrayOfAdPerformanceReportColumn;
import com.microsoft.bingads.reporting.ArrayOfGeoLocationPerformanceReportColumn;
import com.microsoft.bingads.reporting.ArrayOfKeywordPerformanceReportColumn;
import com.microsoft.bingads.reporting.ArrayOfKeywordPerformanceReportSort;
import com.microsoft.bingads.reporting.ArrayOflong;
import com.microsoft.bingads.reporting.GeoLocationPerformanceReportColumn;
import com.microsoft.bingads.reporting.GeoLocationPerformanceReportRequest;
import com.microsoft.bingads.reporting.KeywordPerformanceReportColumn;
import com.microsoft.bingads.reporting.KeywordPerformanceReportRequest;
import com.microsoft.bingads.reporting.KeywordPerformanceReportSort;
import com.microsoft.bingads.reporting.NonHourlyReportAggregation;
import com.microsoft.bingads.reporting.ReportAggregation;
import com.microsoft.bingads.reporting.ReportFormat;
import com.microsoft.bingads.reporting.ReportRequest;
import com.microsoft.bingads.reporting.ReportTime;
import com.microsoft.bingads.reporting.ReportTimePeriod;
import com.microsoft.bingads.reporting.ReportingDownloadParameters;
import com.microsoft.bingads.reporting.ReportingServiceManager;
import com.microsoft.bingads.reporting.SortOrder;
import com.microsoft.bingads.v10.adinsight.ArrayOfKeywordHistoricalPerformance;
import com.microsoft.bingads.v10.adinsight.ArrayOfKeywordKPI;
import com.microsoft.bingads.v10.adinsight.GetHistoricalKeywordPerformanceRequest;
import com.microsoft.bingads.v10.adinsight.GetHistoricalKeywordPerformanceResponse;
import com.microsoft.bingads.v10.campaignmanagement.ApiFaultDetail_Exception;
import com.microsoft.bingads.v10.campaignmanagement.ArrayOfCampaign;
import com.microsoft.bingads.v10.campaignmanagement.Campaign;
import com.microsoft.bingads.v10.campaignmanagement.GetCampaignsByAccountIdRequest;
import com.microsoft.bingads.v10.campaignmanagement.GetCampaignsByIdsRequest;
import com.microsoft.bingads.v10.campaignmanagement.ICampaignManagementService;
import com.microsoft.bingads.v10.adinsight.IAdInsightService;
import com.microsoft.bingads.v10.adinsight.KeywordHistoricalPerformance;
import com.microsoft.bingads.v10.adinsight.KeywordKPI;
import com.visumbu.vb.utils.DateUtils;
import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author user
 */
public class BingApi {

    static AuthorizationData authorizationData = new AuthorizationData();
    public static final String DEVELOPER_TOKEN = "00074192B2151803";
    public static final String USER_NAME = "API_L2TMediaNew";
    public static final String PASSWORD = "l2ttangocode2016";
    public static final Long customerId = 14195800L;
    public static final Long accountId = 2610614L;

    public static void main(String argv[]) {
        try {
            PasswordAuthentication passwordAuthentication
                    = new PasswordAuthentication(USER_NAME, PASSWORD);
            authorizationData.setAuthentication(passwordAuthentication);

            authorizationData.setCustomerId(customerId);
            authorizationData.setAccountId(accountId);
            authorizationData.setDeveloperToken(DEVELOPER_TOKEN);
            /*
             // -----------------
             ServiceClient<IAdInsightService> adInsightService = new ServiceClient<IAdInsightService>(
             authorizationData,
             IAdInsightService.class);

             GetHistoricalKeywordPerformanceRequest getHistoricalKeywordPerformanceRequest = new GetHistoricalKeywordPerformanceRequest();

             try {
             GetHistoricalKeywordPerformanceResponse historicalKeywordPerformance = adInsightService.getService().getHistoricalKeywordPerformance(getHistoricalKeywordPerformanceRequest);
             ArrayOfKeywordHistoricalPerformance keywordHistoricalPerformances = historicalKeywordPerformance.getKeywordHistoricalPerformances();
             List<KeywordHistoricalPerformance> keywordHistoricalPerformances1 = keywordHistoricalPerformances.getKeywordHistoricalPerformances();
             for (Iterator<KeywordHistoricalPerformance> iterator = keywordHistoricalPerformances1.iterator(); iterator.hasNext();) {
             KeywordHistoricalPerformance performance = iterator.next();
             //System.out.println(performance.getKeyword());
             ArrayOfKeywordKPI keywordKPIs = performance.getKeywordKPIs();
             for (Iterator<KeywordKPI> iterator1 = keywordKPIs.getKeywordKPIs().iterator(); iterator1.hasNext();) {
             KeywordKPI kPI = iterator1.next();
             System.out.println("Impressions " + kPI.getImpressions());
}
             }

             } catch (com.microsoft.bingads.v10.adinsight.AdApiFaultDetail_Exception ex) {
             Logger.getLogger(BingApi.class.getName()).log(Level.SEVERE, null, ex);
             } catch (com.microsoft.bingads.v10.adinsight.ApiFaultDetail_Exception ex) {
             Logger.getLogger(BingApi.class.getName()).log(Level.SEVERE, null, ex);
             }
             if (1 == 1) {
             return;
             }
             // -------------
             ServiceClient<ICampaignManagementService> CampaignService = new ServiceClient<ICampaignManagementService>(
             authorizationData,
             ICampaignManagementService.class);

             GetCampaignsByAccountIdRequest getCampaignsByAccountIdRequest = new GetCampaignsByAccountIdRequest();
             getCampaignsByAccountIdRequest.setAccountId(accountId);
             try {
             ArrayOfCampaign campaigns = CampaignService.getService().getCampaignsByAccountId(getCampaignsByAccountIdRequest).getCampaigns();
             List<Campaign> campaigns1 = campaigns.getCampaigns();
             for (Iterator<Campaign> iterator = campaigns1.iterator(); iterator.hasNext();) {
             Campaign campaign = iterator.next();
             System.out.println(campaign);
             }
             } catch (com.microsoft.bingads.v10.campaignmanagement.AdApiFaultDetail_Exception ex) {
             Logger.getLogger(BingApi.class.getName()).log(Level.SEVERE, null, ex);
             } catch (ApiFaultDetail_Exception ex) {
             Logger.getLogger(BingApi.class.getName()).log(Level.SEVERE, null, ex);
             }

             if (1 == 1) {
             return;
             }
             // -----------------
             */

            ReportingServiceManager reportingServiceManager = new ReportingServiceManager(authorizationData);
            reportingServiceManager.setStatusPollIntervalInMilliseconds(5000);

            ReportRequest reportRequest
                    = //getAccountHourPerformaceReportRequest();
                    //getGeoLocationPerformaceReportRequest();
                    //getAdPerformaceReportRequest();
                    //getAccountPerformaceReportRequest(); 
                    getKeywordPerformanceReportRequest(DateUtils.get30DaysBack(), new Date());

            ReportingDownloadParameters reportingDownloadParameters = new ReportingDownloadParameters();
            reportingDownloadParameters.setReportRequest(reportRequest);
            reportingDownloadParameters.setResultFileDirectory(new File("f:\\test"));
            reportingDownloadParameters.setResultFileName("testGeoP");
            reportingDownloadParameters.setOverwriteResultFile(true);

// You may optionally cancel the downloadFileAsync operation after a specified time interval.
            File resultFile = reportingServiceManager.downloadFileAsync(
                    reportingDownloadParameters,
                    null).get(3600000, TimeUnit.MILLISECONDS);

            System.out.println(String.format("Download result file: %s\n", resultFile.getName()));
        } catch (InterruptedException ex) {
            Logger.getLogger(BingApi.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ExecutionException ex) {
            Logger.getLogger(BingApi.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TimeoutException ex) {
            Logger.getLogger(BingApi.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static ReportRequest getAccountHourPerformaceReportRequest() {
        AccountPerformanceReportRequest report = new AccountPerformanceReportRequest();
        ReportFormat ReportFileFormat = ReportFormat.XML;
        report.setFormat(ReportFileFormat);
        report.setReportName("My Keyword Performance Report");
        report.setReturnOnlyCompleteData(false);
        report.setAggregation(ReportAggregation.DAILY);

        ArrayOflong accountIds = new ArrayOflong();
        accountIds.getLongs().add(authorizationData.getAccountId());

        report.setScope(new AccountReportScope());
        report.getScope().setAccountIds(accountIds);
        report.setTime(new ReportTime());
        report.getTime().setPredefinedTime(ReportTimePeriod.TODAY);

        ArrayOfAccountPerformanceReportColumn accountPerformanceReportColumn = new ArrayOfAccountPerformanceReportColumn();
        accountPerformanceReportColumn.getAccountPerformanceReportColumns().add(AccountPerformanceReportColumn.IMPRESSIONS);
        accountPerformanceReportColumn.getAccountPerformanceReportColumns().add(AccountPerformanceReportColumn.ACCOUNT_ID);
        accountPerformanceReportColumn.getAccountPerformanceReportColumns().add(AccountPerformanceReportColumn.CLICKS);
        accountPerformanceReportColumn.getAccountPerformanceReportColumns().add(AccountPerformanceReportColumn.CTR);
        accountPerformanceReportColumn.getAccountPerformanceReportColumns().add(AccountPerformanceReportColumn.AVERAGE_CPC);
        accountPerformanceReportColumn.getAccountPerformanceReportColumns().add(AccountPerformanceReportColumn.SPEND);
        accountPerformanceReportColumn.getAccountPerformanceReportColumns().add(AccountPerformanceReportColumn.CONVERSIONS);
        accountPerformanceReportColumn.getAccountPerformanceReportColumns().add(AccountPerformanceReportColumn.CONVERSION_RATE);
        accountPerformanceReportColumn.getAccountPerformanceReportColumns().add(AccountPerformanceReportColumn.COST_PER_CONVERSION);
        accountPerformanceReportColumn.getAccountPerformanceReportColumns().add(AccountPerformanceReportColumn.ACCOUNT_NAME);
        accountPerformanceReportColumn.getAccountPerformanceReportColumns().add(AccountPerformanceReportColumn.ACCOUNT_STATUS);
        accountPerformanceReportColumn.getAccountPerformanceReportColumns().add(AccountPerformanceReportColumn.IMPRESSION_SHARE_PERCENT);
        accountPerformanceReportColumn.getAccountPerformanceReportColumns().add(AccountPerformanceReportColumn.IMPRESSION_LOST_TO_BUDGET_PERCENT);
        accountPerformanceReportColumn.getAccountPerformanceReportColumns().add(AccountPerformanceReportColumn.IMPRESSION_LOST_TO_RANK_PERCENT);
        report.setColumns(accountPerformanceReportColumn);
        return report;
    }

    private static ReportRequest getGeoLocationPerformaceReportRequest() {
        GeoLocationPerformanceReportRequest report = new GeoLocationPerformanceReportRequest();
        ReportFormat ReportFileFormat = ReportFormat.CSV;
        report.setFormat(ReportFileFormat);
        report.setReportName("My Keyword Performance Report");
        report.setReturnOnlyCompleteData(false);
        report.setAggregation(NonHourlyReportAggregation.DAILY);

        ArrayOflong accountIds = new ArrayOflong();
        accountIds.getLongs().add(authorizationData.getAccountId());

        report.setScope(new AccountThroughAdGroupReportScope());
        report.getScope().setAccountIds(accountIds);
        report.setTime(new ReportTime());
        report.getTime().setPredefinedTime(ReportTimePeriod.LAST_WEEK);

        ArrayOfGeoLocationPerformanceReportColumn geoLocationPerformanceReportColumn = new ArrayOfGeoLocationPerformanceReportColumn();
        geoLocationPerformanceReportColumn.getGeoLocationPerformanceReportColumns().add(GeoLocationPerformanceReportColumn.CLICKS);
        geoLocationPerformanceReportColumn.getGeoLocationPerformanceReportColumns().add(GeoLocationPerformanceReportColumn.IMPRESSIONS);
        geoLocationPerformanceReportColumn.getGeoLocationPerformanceReportColumns().add(GeoLocationPerformanceReportColumn.AVERAGE_CPC);
        geoLocationPerformanceReportColumn.getGeoLocationPerformanceReportColumns().add(GeoLocationPerformanceReportColumn.CTR);
        geoLocationPerformanceReportColumn.getGeoLocationPerformanceReportColumns().add(GeoLocationPerformanceReportColumn.SPEND);
        geoLocationPerformanceReportColumn.getGeoLocationPerformanceReportColumns().add(GeoLocationPerformanceReportColumn.CONVERSIONS);
        geoLocationPerformanceReportColumn.getGeoLocationPerformanceReportColumns().add(GeoLocationPerformanceReportColumn.COST_PER_CONVERSION);
        geoLocationPerformanceReportColumn.getGeoLocationPerformanceReportColumns().add(GeoLocationPerformanceReportColumn.CONVERSION_RATE);
        geoLocationPerformanceReportColumn.getGeoLocationPerformanceReportColumns().add(GeoLocationPerformanceReportColumn.TIME_PERIOD);
        geoLocationPerformanceReportColumn.getGeoLocationPerformanceReportColumns().add(GeoLocationPerformanceReportColumn.STATE);
        geoLocationPerformanceReportColumn.getGeoLocationPerformanceReportColumns().add(GeoLocationPerformanceReportColumn.COUNTRY);

        report.setColumns(geoLocationPerformanceReportColumn);
        return report;
    }

    private static ReportRequest getAdPerformaceReportRequest() {
        AdPerformanceReportRequest report = new AdPerformanceReportRequest();
        ReportFormat ReportFileFormat = ReportFormat.CSV;
        report.setFormat(ReportFileFormat);
        report.setReportName("My Keyword Performance Report");
        report.setReturnOnlyCompleteData(false);
        report.setAggregation(NonHourlyReportAggregation.DAILY);

        ArrayOflong accountIds = new ArrayOflong();
        accountIds.getLongs().add(authorizationData.getAccountId());

        report.setScope(new AccountThroughAdGroupReportScope());
        report.getScope().setAccountIds(accountIds);
        report.setTime(new ReportTime());
        report.getTime().setPredefinedTime(ReportTimePeriod.LAST_WEEK);

        ArrayOfAdPerformanceReportColumn adPerformanceReportColumn = new ArrayOfAdPerformanceReportColumn();
        adPerformanceReportColumn.getAdPerformanceReportColumns().add(AdPerformanceReportColumn.ACCOUNT_NAME);
        adPerformanceReportColumn.getAdPerformanceReportColumns().add(AdPerformanceReportColumn.ACCOUNT_ID);
        adPerformanceReportColumn.getAdPerformanceReportColumns().add(AdPerformanceReportColumn.CLICKS);
        adPerformanceReportColumn.getAdPerformanceReportColumns().add(AdPerformanceReportColumn.IMPRESSIONS);
        adPerformanceReportColumn.getAdPerformanceReportColumns().add(AdPerformanceReportColumn.AVERAGE_CPC);
        adPerformanceReportColumn.getAdPerformanceReportColumns().add(AdPerformanceReportColumn.CTR);
        adPerformanceReportColumn.getAdPerformanceReportColumns().add(AdPerformanceReportColumn.SPEND);
        adPerformanceReportColumn.getAdPerformanceReportColumns().add(AdPerformanceReportColumn.CONVERSIONS);
        adPerformanceReportColumn.getAdPerformanceReportColumns().add(AdPerformanceReportColumn.COST_PER_CONVERSION);
        adPerformanceReportColumn.getAdPerformanceReportColumns().add(AdPerformanceReportColumn.CONVERSION_RATE);
        adPerformanceReportColumn.getAdPerformanceReportColumns().add(AdPerformanceReportColumn.DEVICE_TYPE);
        adPerformanceReportColumn.getAdPerformanceReportColumns().add(AdPerformanceReportColumn.CAMPAIGN_NAME);
        adPerformanceReportColumn.getAdPerformanceReportColumns().add(AdPerformanceReportColumn.AD_GROUP_NAME);
        adPerformanceReportColumn.getAdPerformanceReportColumns().add(AdPerformanceReportColumn.TIME_PERIOD);
        adPerformanceReportColumn.getAdPerformanceReportColumns().add(AdPerformanceReportColumn.AD_DESCRIPTION);
        adPerformanceReportColumn.getAdPerformanceReportColumns().add(AdPerformanceReportColumn.AD_TITLE);
        adPerformanceReportColumn.getAdPerformanceReportColumns().add(AdPerformanceReportColumn.DESTINATION_URL);

        report.setColumns(adPerformanceReportColumn);
        return report;
    }

    private static ReportRequest getAccountPerformaceReportRequest() {
        AccountPerformanceReportRequest report = new AccountPerformanceReportRequest();
        ReportFormat ReportFileFormat = ReportFormat.CSV;
        report.setFormat(ReportFileFormat);
        report.setReportName("My Keyword Performance Report");
        report.setReturnOnlyCompleteData(false);
        report.setAggregation(ReportAggregation.DAILY);

        ArrayOflong accountIds = new ArrayOflong();
        accountIds.getLongs().add(authorizationData.getAccountId());

        report.setScope(new AccountReportScope());
        report.getScope().setAccountIds(accountIds);
        report.setTime(new ReportTime());
        report.getTime().setPredefinedTime(ReportTimePeriod.LAST_WEEK);

        ArrayOfAccountPerformanceReportColumn accountPerformanceReportColumn = new ArrayOfAccountPerformanceReportColumn();
        accountPerformanceReportColumn.getAccountPerformanceReportColumns().add(AccountPerformanceReportColumn.IMPRESSIONS);
        accountPerformanceReportColumn.getAccountPerformanceReportColumns().add(AccountPerformanceReportColumn.ACCOUNT_ID);
        accountPerformanceReportColumn.getAccountPerformanceReportColumns().add(AccountPerformanceReportColumn.CLICKS);
        accountPerformanceReportColumn.getAccountPerformanceReportColumns().add(AccountPerformanceReportColumn.CTR);
        accountPerformanceReportColumn.getAccountPerformanceReportColumns().add(AccountPerformanceReportColumn.AVERAGE_CPC);
        accountPerformanceReportColumn.getAccountPerformanceReportColumns().add(AccountPerformanceReportColumn.SPEND);
        accountPerformanceReportColumn.getAccountPerformanceReportColumns().add(AccountPerformanceReportColumn.CONVERSIONS);
        accountPerformanceReportColumn.getAccountPerformanceReportColumns().add(AccountPerformanceReportColumn.CONVERSION_RATE);
        accountPerformanceReportColumn.getAccountPerformanceReportColumns().add(AccountPerformanceReportColumn.COST_PER_CONVERSION);
        accountPerformanceReportColumn.getAccountPerformanceReportColumns().add(AccountPerformanceReportColumn.ACCOUNT_NAME);
        accountPerformanceReportColumn.getAccountPerformanceReportColumns().add(AccountPerformanceReportColumn.IMPRESSION_SHARE_PERCENT);
        accountPerformanceReportColumn.getAccountPerformanceReportColumns().add(AccountPerformanceReportColumn.IMPRESSION_LOST_TO_BUDGET_PERCENT);
        accountPerformanceReportColumn.getAccountPerformanceReportColumns().add(AccountPerformanceReportColumn.IMPRESSION_LOST_TO_RANK_PERCENT);
        accountPerformanceReportColumn.getAccountPerformanceReportColumns().add(AccountPerformanceReportColumn.DEVICE_TYPE);
        accountPerformanceReportColumn.getAccountPerformanceReportColumns().add(AccountPerformanceReportColumn.TIME_PERIOD);
        report.setColumns(accountPerformanceReportColumn);
        return report;
    }

    private static ReportRequest getKeywordPerformanceReportRequest(Date startDate, Date endDate) {
        KeywordPerformanceReportRequest report = new KeywordPerformanceReportRequest();
        ReportFormat ReportFileFormat = ReportFormat.XML;
        report.setFormat(ReportFileFormat);
        report.setReportName("My Keyword Performance Report");
        report.setReturnOnlyCompleteData(false);
        report.setAggregation(ReportAggregation.DAILY);

        ArrayOflong accountIds = new ArrayOflong();
        accountIds.getLongs().add(authorizationData.getAccountId());

        report.setScope(new AccountThroughAdGroupReportScope());
        report.getScope().setAccountIds(accountIds);
        report.getScope().setCampaigns(null);
        report.getScope().setAdGroups(null);

        report.setTime(new ReportTime());
        //report.getTime().setPredefinedTime(ReportTimePeriod.YESTERDAY);

        Calendar startCal = Calendar.getInstance();
        startCal.setTime(startDate);
        report.getTime().setCustomDateRangeStart(new com.microsoft.bingads.reporting.Date());
        report.getTime().getCustomDateRangeStart().setDay(startCal.get(Calendar.DAY_OF_MONTH));
        report.getTime().getCustomDateRangeStart().setMonth(startCal.get(Calendar.MONTH) + 1);
        report.getTime().getCustomDateRangeStart().setYear(startCal.get(Calendar.YEAR));

        // End Date 
        Calendar endCal = Calendar.getInstance();
        endCal.setTime(endDate);
        report.getTime().setCustomDateRangeEnd(new com.microsoft.bingads.reporting.Date());
        System.out.println(endCal.get(Calendar.DAY_OF_MONTH));
        System.out.println(endCal.get(Calendar.MONTH) + 1);
        System.out.println(endCal.get(Calendar.YEAR));
        report.getTime().getCustomDateRangeEnd().setDay(endCal.get(Calendar.DAY_OF_MONTH));
        report.getTime().getCustomDateRangeEnd().setMonth(endCal.get(Calendar.MONTH) + 1);
        report.getTime().getCustomDateRangeEnd().setYear(endCal.get(Calendar.YEAR));
/*
        // You may either use a custom date range or predefined time.
        report.getTime().setCustomDateRangeStart(new com.microsoft.bingads.reporting.Date());
        report.getTime().getCustomDateRangeStart().setMonth(10);
        report.getTime().getCustomDateRangeStart().setDay(26);
        report.getTime().getCustomDateRangeStart().setYear(2016);
        report.getTime().setCustomDateRangeEnd(new com.microsoft.bingads.reporting.Date());
        report.getTime().getCustomDateRangeEnd().setMonth(11);
        report.getTime().getCustomDateRangeEnd().setDay(26);
        report.getTime().getCustomDateRangeEnd().setYear(2016); */
        // If you specify a filter, results may differ from data you see in the Bing Ads web application
        //report.setFilter(new KeywordPerformanceReportFilter());
        //ArrayList<DeviceTypeReportFilter> deviceTypes = new ArrayList<DeviceTypeReportFilter>();
        //deviceTypes.add(DeviceTypeReportFilter.COMPUTER);
        //deviceTypes.add(DeviceTypeReportFilter.SMART_PHONE);
        //report.getFilter().setDeviceType(deviceTypes);
        // Specify the attribute and data report columns.
        ArrayOfKeywordPerformanceReportColumn keywordPerformanceReportColumns = new ArrayOfKeywordPerformanceReportColumn();
        keywordPerformanceReportColumns.getKeywordPerformanceReportColumns().add(KeywordPerformanceReportColumn.TIME_PERIOD);
        keywordPerformanceReportColumns.getKeywordPerformanceReportColumns().add(KeywordPerformanceReportColumn.ACCOUNT_ID);
        keywordPerformanceReportColumns.getKeywordPerformanceReportColumns().add(KeywordPerformanceReportColumn.ACCOUNT_NUMBER);
        keywordPerformanceReportColumns.getKeywordPerformanceReportColumns().add(KeywordPerformanceReportColumn.ACCOUNT_NAME);
        keywordPerformanceReportColumns.getKeywordPerformanceReportColumns().add(KeywordPerformanceReportColumn.CAMPAIGN_ID);
        keywordPerformanceReportColumns.getKeywordPerformanceReportColumns().add(KeywordPerformanceReportColumn.CAMPAIGN_NAME);
        keywordPerformanceReportColumns.getKeywordPerformanceReportColumns().add(KeywordPerformanceReportColumn.KEYWORD);
        keywordPerformanceReportColumns.getKeywordPerformanceReportColumns().add(KeywordPerformanceReportColumn.KEYWORD_ID);
        keywordPerformanceReportColumns.getKeywordPerformanceReportColumns().add(KeywordPerformanceReportColumn.DEVICE_TYPE);
        keywordPerformanceReportColumns.getKeywordPerformanceReportColumns().add(KeywordPerformanceReportColumn.BID_MATCH_TYPE);
        keywordPerformanceReportColumns.getKeywordPerformanceReportColumns().add(KeywordPerformanceReportColumn.CLICKS);
        keywordPerformanceReportColumns.getKeywordPerformanceReportColumns().add(KeywordPerformanceReportColumn.IMPRESSIONS);
        keywordPerformanceReportColumns.getKeywordPerformanceReportColumns().add(KeywordPerformanceReportColumn.CTR);
        keywordPerformanceReportColumns.getKeywordPerformanceReportColumns().add(KeywordPerformanceReportColumn.AVERAGE_CPC);
        keywordPerformanceReportColumns.getKeywordPerformanceReportColumns().add(KeywordPerformanceReportColumn.CONVERSIONS);
        keywordPerformanceReportColumns.getKeywordPerformanceReportColumns().add(KeywordPerformanceReportColumn.COST_PER_CONVERSION);
        keywordPerformanceReportColumns.getKeywordPerformanceReportColumns().add(KeywordPerformanceReportColumn.CONVERSION_RATE);
        keywordPerformanceReportColumns.getKeywordPerformanceReportColumns().add(KeywordPerformanceReportColumn.SPEND);
        keywordPerformanceReportColumns.getKeywordPerformanceReportColumns().add(KeywordPerformanceReportColumn.QUALITY_SCORE);
        report.setColumns(keywordPerformanceReportColumns);

        // You may optionally sort by any KeywordPerformanceReportColumn, and optionally
        // specify the maximum number of rows to return in the sorted report. 
        KeywordPerformanceReportSort keywordPerformanceReportSort = new KeywordPerformanceReportSort();
        keywordPerformanceReportSort.setSortColumn(KeywordPerformanceReportColumn.CLICKS);
        keywordPerformanceReportSort.setSortOrder(SortOrder.ASCENDING);
        ArrayOfKeywordPerformanceReportSort keywordPerformanceReportSorts = new ArrayOfKeywordPerformanceReportSort();
        keywordPerformanceReportSorts.getKeywordPerformanceReportSorts().add(keywordPerformanceReportSort);
        report.setSort(keywordPerformanceReportSorts);

//        report.setMaxRows(100);
        return report;
    }
}
