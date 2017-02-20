/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.api.admin.controller;

import com.google.api.services.analyticsreporting.v4.model.GetReportsResponse;
import com.google.api.services.analyticsreporting.v4.model.Report;
import com.visumbu.api.admin.service.AdwordsService;
import com.visumbu.api.admin.service.BingService;
import com.visumbu.api.admin.service.GaService;
import com.visumbu.api.admin.service.UserService;
import com.visumbu.api.adwords.report.xml.bean.AccountDeviceReport;
import com.visumbu.api.adwords.report.xml.bean.AccountDeviceReportRow;
import com.visumbu.api.adwords.report.xml.bean.AccountReport;
import com.visumbu.api.adwords.report.xml.bean.AccountReportRow;
import com.visumbu.api.adwords.report.xml.bean.AdGroupReportRow;
import com.visumbu.api.adwords.report.xml.bean.AdReport;
import com.visumbu.api.adwords.report.xml.bean.AdReportRow;
import com.visumbu.api.adwords.report.xml.bean.AddGroupReport;
import com.visumbu.api.adwords.report.xml.bean.CampaignDeviceReport;
import com.visumbu.api.adwords.report.xml.bean.CampaignDeviceReportRow;
import com.visumbu.api.adwords.report.xml.bean.CampaignPerformanceReportRow;
import com.visumbu.api.adwords.report.xml.bean.CampaignReport;
import com.visumbu.api.adwords.report.xml.bean.CampaignReportRow;
import com.visumbu.api.adwords.report.xml.bean.GeoReport;
import com.visumbu.api.adwords.report.xml.bean.GeoReportRow;
import com.visumbu.api.bean.AccountDetails;
import com.visumbu.api.bean.ColumnDef;
import com.visumbu.api.bean.LoginUserBean;
import com.visumbu.api.bing.report.xml.bean.AccountDevicePerformanceReport;
import com.visumbu.api.bing.report.xml.bean.AccountDevicePerformanceRow;
import com.visumbu.api.bing.report.xml.bean.AccountPerformanceReport;
import com.visumbu.api.bing.report.xml.bean.AccountPerformanceRow;
import com.visumbu.api.bing.report.xml.bean.AdGroupPerformanceReport;
import com.visumbu.api.bing.report.xml.bean.AdGroupPerformanceRow;
import com.visumbu.api.bing.report.xml.bean.CampaignDevicePerformanceReport;
import com.visumbu.api.bing.report.xml.bean.CampaignDevicePerformanceRow;
import com.visumbu.api.bing.report.xml.bean.CampaignPerformanceReport;
import com.visumbu.api.bing.report.xml.bean.CampaignPerformanceRow;
import com.visumbu.api.bing.report.xml.bean.GeoCityLocationPerformanceReport;
import com.visumbu.api.bing.report.xml.bean.GeoCityLocationPerformanceRow;
import com.visumbu.api.dashboard.bean.AccountPerformanceReportBean;
import com.visumbu.api.dashboard.bean.AdGroupPerformanceReportBean;
import com.visumbu.api.dashboard.bean.AdPerformanceReportBean;
import com.visumbu.api.dashboard.bean.CampaignDevicePerformanceReportBean;
import com.visumbu.api.dashboard.bean.DevicePerformanceReportBean;
import com.visumbu.api.dashboard.bean.CampaignPerformanceReportBean;
import com.visumbu.api.dashboard.bean.ClicksImpressionsGraphBean;
import com.visumbu.api.dashboard.bean.ClicksImpressionsHourOfDayBean;
import com.visumbu.api.dashboard.bean.GeoPerformanceReportBean;
import com.visumbu.api.utils.ApiUtils;
import com.visumbu.api.utils.DateUtils;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 *
 * @author Varghees
 */
@Controller
@RequestMapping("seo")
public class SeoTabController {

    @Autowired
    private UserService userService;

    @Autowired
    private BingService bingService;

    @Autowired
    private AdwordsService adwordsService;

    @Autowired
    private GaService gaService;

    @RequestMapping(value = "accountPerformance", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    Object getAccountPerformance(HttpServletRequest request, HttpServletResponse response) {
        Date startDate = DateUtils.getStartDate(request.getParameter("startDate"));
        Date endDate = DateUtils.getEndDate(request.getParameter("endDate"));

        String fieldsOnly = request.getParameter("fieldsOnly");
        Map returnMap = new HashMap();
        List<ColumnDef> columnDefs = new ArrayList<>();
        columnDefs.add(new ColumnDef("visits", "number", "Visits", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
        columnDefs.add(new ColumnDef("percentNewSessions", "string", "% New Sessions", ColumnDef.Aggregation.AVG, ColumnDef.Format.PERCENTAGE));
        columnDefs.add(new ColumnDef("bounceRate", "number", "Bounce Rate", ColumnDef.Aggregation.AVG, ColumnDef.Format.PERCENTAGE));
        columnDefs.add(new ColumnDef("avgTimeOnPage", "string", "Average Time On Page"));
        columnDefs.add(new ColumnDef("directionsPageView", "number", "Directions Page View", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
        columnDefs.add(new ColumnDef("inventoryPageViews", "number", "Inventory Page Views", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
        columnDefs.add(new ColumnDef("leadSubmission", "number", "Lead Submission", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
        columnDefs.add(new ColumnDef("specialsPageView", "number", "Specials Page View", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
        columnDefs.add(new ColumnDef("timeOnSiteGt2Mins", "number", "Time On Site > 2Mins", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
        columnDefs.add(new ColumnDef("vdpViews", "number", "VDP Views", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
        columnDefs.add(new ColumnDef("engagements", "number", "Engagements", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
        columnDefs.add(new ColumnDef("channelGrouping", "string", "Channel Grouping"));

        returnMap.put("columnDefs", columnDefs);
        if (fieldsOnly != null) {
            return returnMap;
        }
        AccountDetails accountDetails = ApiUtils.toAccountDetails(request, "seo");
        System.out.println("GOALS -> " + gaService.getGaGoals(accountDetails.getAnalyticsAccountId(), accountDetails.getAnalyticsProfileId()));
        String metricsList = "ga:visits,visits;ga:percentNewSessions,percentNewSessions;"
                + "ga:bounceRate,bounceRate;ga:avgTimeOnPage,avgTimeOnPage;"
                + gaService.getGaGoals(accountDetails.getAnalyticsAccountId(), accountDetails.getAnalyticsProfileId());
        String dimensions = "ga:channelGrouping";
        String filter = "ga:channelGrouping==Organic Search";
        System.out.println("SEO GA Profile Id " + accountDetails.getAnalyticsProfileId());
        List<Map<String, String>> gaDataMap = new ArrayList<>();
        System.out.println(accountDetails.getAnalyticsProfileId());
        if (accountDetails.getAnalyticsProfileId() != null) {
            GetReportsResponse gaData = gaService.getGenericData(accountDetails.getAnalyticsProfileId(), startDate, endDate, null, null, metricsList, dimensions, filter);
            gaDataMap = (List) gaService.getResponseAsMap(gaData).get("data");

            for (Iterator<Map<String, String>> iterator = gaDataMap.iterator(); iterator.hasNext();) {
                Map<String, String> map = iterator.next();
                map.put("avgTimeOnPage", ApiUtils.toMins(map.get("avgTimeOnPage")));
                map.put("bounceRate", ApiUtils.removePercent(map.get("bounceRate") + ""));
                map.put("percentNewSessions", ApiUtils.removePercent(map.get("percentNewSessions") + ""));
                map.put("channelGrouping", map.get("ga:channelGrouping"));
                Integer engagements = 0;
                engagements += (ApiUtils.toInteger(map.get("directionsPageView"))
                        + ApiUtils.toInteger(map.get("inventoryPageViews"))
                        + ApiUtils.toInteger(map.get("leadSubmission"))
                        + ApiUtils.toInteger(map.get("specialsPageView"))
                        + ApiUtils.toInteger(map.get("timeOnSiteGt2Mins"))
                        + ApiUtils.toInteger(map.get("vdpViews")));
                map.put("engagements", engagements + "");
            }
            // returnMap.put("gaData", gaData);
        }
        returnMap.put("data", gaDataMap);
        return returnMap;
    }

    @RequestMapping(value = "accountPerformance12Weeks", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    Object getGeoPerformance12Weeks(HttpServletRequest request, HttpServletResponse response) {
        Date startDate = DateUtils.get12WeeksBack(request.getParameter("endDate"));
        Date endDate = DateUtils.getEndDate(request.getParameter("endDate"));

        String fieldsOnly = request.getParameter("fieldsOnly");
        Map returnMap = new HashMap();
        List<ColumnDef> columnDefs = new ArrayList<>();
        columnDefs.add(new ColumnDef("visits", "number", "Visits", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
        columnDefs.add(new ColumnDef("percentNewSessions", "string", "% New Sessions", ColumnDef.Aggregation.AVG, ColumnDef.Format.PERCENTAGE));
        columnDefs.add(new ColumnDef("bounceRate", "string", "Bounce Rate", ColumnDef.Aggregation.AVG, ColumnDef.Format.PERCENTAGE));
        columnDefs.add(new ColumnDef("avgTimeOnPage", "string", "Average Time On Page"));
        columnDefs.add(new ColumnDef("directionsPageView", "number", "Directions Page View", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
        columnDefs.add(new ColumnDef("inventoryPageViews", "number", "Inventory Page Views", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
        columnDefs.add(new ColumnDef("leadSubmission", "number", "Lead Submission", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
        columnDefs.add(new ColumnDef("specialsPageView", "number", "Specials Page View", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
        columnDefs.add(new ColumnDef("timeOnSiteGt2Mins", "number", "Time On Site > 2Mins", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
        columnDefs.add(new ColumnDef("vdpViews", "number", "VDP Views", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
        columnDefs.add(new ColumnDef("engagements", "number", "Engagements", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
        columnDefs.add(new ColumnDef("yearWeek", "date", "Year Of Week"));

        returnMap.put("columnDefs", columnDefs);
        if (fieldsOnly != null) {
            return returnMap;
        }
        AccountDetails accountDetails = ApiUtils.toAccountDetails(request, "seo");
        List<Map<String, String>> gaDataMap = new ArrayList<>();
        if (accountDetails.getAnalyticsProfileId() != null) {
            String metricsList = "ga:visits,visits;ga:percentNewSessions,percentNewSessions;"
                    + "ga:bounceRate,bounceRate;ga:avgTimeOnPage,avgTimeOnPage;"
                    + gaService.getGaGoals(accountDetails.getAnalyticsAccountId(), accountDetails.getAnalyticsProfileId());
            String dimensions = "ga:yearWeek";
            String filter = "ga:channelGrouping==Organic Search";

            GetReportsResponse gaData = gaService.getGenericData(accountDetails.getAnalyticsProfileId(), startDate, endDate, null, null, metricsList, dimensions, filter);
            gaDataMap = (List) gaService.getResponseAsMap(gaData).get("data");
            for (Iterator<Map<String, String>> iterator = gaDataMap.iterator(); iterator.hasNext();) {
                Map<String, String> map = iterator.next();
                map.put("avgTimeOnPage", ApiUtils.toMins(map.get("avgTimeOnPage")));
                map.put("bounceRate", ApiUtils.removePercent(map.get("bounceRate") + ""));
                map.put("percentNewSessions", ApiUtils.removePercent(map.get("percentNewSessions") + ""));
                map.put("yearWeek", DateUtils.yearWeekToDate(map.get("ga:yearWeek"), "MM/dd/yyyy"));
                Integer engagements = 0;
                engagements += (ApiUtils.toInteger(map.get("directionsPageView"))
                        + ApiUtils.toInteger(map.get("inventoryPageViews"))
                        + ApiUtils.toInteger(map.get("leadSubmission"))
                        + ApiUtils.toInteger(map.get("specialsPageView"))
                        + ApiUtils.toInteger(map.get("timeOnSiteGt2Mins"))
                        + ApiUtils.toInteger(map.get("vdpViews")));
                map.put("engagements", engagements + "");

            }
        }
        returnMap.put("data", gaDataMap);
        return returnMap;
    }

    @RequestMapping(value = "accountPerformanceDayOfWeek", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    Object getGeoPerformanceDayOfWeek(HttpServletRequest request, HttpServletResponse response) {
        Date startDate = DateUtils.getStartDate(request.getParameter("startDate"));
        Date endDate = DateUtils.getEndDate(request.getParameter("endDate"));

        String fieldsOnly = request.getParameter("fieldsOnly");
        Map returnMap = new HashMap();
        List<ColumnDef> columnDefs = new ArrayList<>();
        columnDefs.add(new ColumnDef("visits", "number", "Visits", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
        columnDefs.add(new ColumnDef("percentNewSessions", "string", "% New Sessions", ColumnDef.Aggregation.AVG, ColumnDef.Format.PERCENTAGE));
        columnDefs.add(new ColumnDef("bounceRate", "string", "Bounce Rate", ColumnDef.Aggregation.AVG, ColumnDef.Format.PERCENTAGE));
        columnDefs.add(new ColumnDef("avgTimeOnPage", "string", "Average Time On Page"));
        columnDefs.add(new ColumnDef("directionsPageView", "number", "Directions Page View", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
        columnDefs.add(new ColumnDef("inventoryPageViews", "number", "Inventory Page Views", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
        columnDefs.add(new ColumnDef("leadSubmission", "number", "Lead Submission", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
        columnDefs.add(new ColumnDef("specialsPageView", "number", "Specials Page View", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
        columnDefs.add(new ColumnDef("timeOnSiteGt2Mins", "number", "Time On Site > 2Mins", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
        columnDefs.add(new ColumnDef("vdpViews", "number", "VDP Views", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
        columnDefs.add(new ColumnDef("engagements", "number", "Engagements", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
        columnDefs.add(new ColumnDef("dayOfWeekName", "string", "Day Of Week"));

        returnMap.put("columnDefs", columnDefs);
        if (fieldsOnly != null) {
            return returnMap;
        }
        AccountDetails accountDetails = ApiUtils.toAccountDetails(request, "seo");
        List<Map<String, String>> gaDataMap = new ArrayList<>();
        if (accountDetails.getAnalyticsProfileId() != null) {
            String metricsList = "ga:visits,visits;ga:percentNewSessions,percentNewSessions;"
                    + "ga:bounceRate,bounceRate;ga:avgTimeOnPage,avgTimeOnPage;"
                    + gaService.getGaGoals(accountDetails.getAnalyticsAccountId(), accountDetails.getAnalyticsProfileId());
            String dimensions = "ga:dayOfWeekName";
            String filter = "ga:channelGrouping==Organic Search";

            GetReportsResponse gaData = gaService.getGenericData(accountDetails.getAnalyticsProfileId(), startDate, endDate, null, null, metricsList, dimensions, filter);
            gaDataMap = (List) gaService.getResponseAsMap(gaData).get("data");
            for (Iterator<Map<String, String>> iterator = gaDataMap.iterator(); iterator.hasNext();) {
                Map<String, String> map = iterator.next();
                map.put("avgTimeOnPage", ApiUtils.toMins(map.get("avgTimeOnPage")));
                map.put("bounceRate", ApiUtils.removePercent(map.get("bounceRate") + ""));
                map.put("percentNewSessions", ApiUtils.removePercent(map.get("percentNewSessions") + ""));
                map.put("dayOfWeekName", map.get("ga:dayOfWeekName"));
                Integer engagements = 0;
                engagements += (ApiUtils.toInteger(map.get("directionsPageView"))
                        + ApiUtils.toInteger(map.get("inventoryPageViews"))
                        + ApiUtils.toInteger(map.get("leadSubmission"))
                        + ApiUtils.toInteger(map.get("specialsPageView"))
                        + ApiUtils.toInteger(map.get("timeOnSiteGt2Mins"))
                        + ApiUtils.toInteger(map.get("vdpViews")));
                map.put("engagements", engagements + "");

            }
        }
        returnMap.put("data", gaDataMap);
        return returnMap;
    }

    @RequestMapping(value = "accountPerformanceTop10Page", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    Object getGeoPerformanceTop10Page(HttpServletRequest request, HttpServletResponse response) {
        Date startDate = DateUtils.getStartDate(request.getParameter("startDate"));
        Date endDate = DateUtils.getEndDate(request.getParameter("endDate"));

        String fieldsOnly = request.getParameter("fieldsOnly");
        Map returnMap = new HashMap();
        List<ColumnDef> columnDefs = new ArrayList<>();
        columnDefs.add(new ColumnDef("visits", "number", "Visits", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
        columnDefs.add(new ColumnDef("percentNewSessions", "string", "% New Sessions", ColumnDef.Aggregation.AVG, ColumnDef.Format.PERCENTAGE));
        columnDefs.add(new ColumnDef("bounceRate", "string", "Bounce Rage", ColumnDef.Aggregation.AVG, ColumnDef.Format.PERCENTAGE));
        columnDefs.add(new ColumnDef("avgTimeOnPage", "string", "Average Time On Page"));
        columnDefs.add(new ColumnDef("directionsPageView", "number", "Directions Page View", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
        columnDefs.add(new ColumnDef("inventoryPageViews", "number", "Inventory Page Views", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
        columnDefs.add(new ColumnDef("leadSubmission", "number", "Lead Submission", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
        columnDefs.add(new ColumnDef("specialsPageView", "number", "Specials Page View", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
        columnDefs.add(new ColumnDef("timeOnSiteGt2Mins", "number", "Time On Site > 2Mins", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
        columnDefs.add(new ColumnDef("vdpViews", "number", "VDP Views", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
        columnDefs.add(new ColumnDef("engagements", "number", "Engagements", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
        columnDefs.add(new ColumnDef("pagePath", "string", "Page"));

        returnMap.put("columnDefs", columnDefs);
        if (fieldsOnly != null) {
            return returnMap;
        }
        AccountDetails accountDetails = ApiUtils.toAccountDetails(request, "seo");
        List<Map<String, String>> gaDataMap = new ArrayList<>();
        if (accountDetails.getAnalyticsProfileId() != null) {
            String metricsList = "ga:visits,visits;ga:percentNewSessions,percentNewSessions;"
                    + "ga:bounceRate,bounceRate;ga:avgTimeOnPage,avgTimeOnPage;"
                    + gaService.getGaGoals(accountDetails.getAnalyticsAccountId(), accountDetails.getAnalyticsProfileId());
            String dimensions = "ga:pagePath";
            String filter = "ga:channelGrouping==Organic Search";
            String orderBy = "ga:visits";

            GetReportsResponse gaData = gaService.getGenericData(accountDetails.getAnalyticsProfileId(), startDate, endDate, null, null, metricsList, dimensions, filter, orderBy, 10);
            gaDataMap = (List) gaService.getResponseAsMap(gaData).get("data");
            for (Iterator<Map<String, String>> iterator = gaDataMap.iterator(); iterator.hasNext();) {
                Map<String, String> map = iterator.next();
                map.put("avgTimeOnPage", ApiUtils.toMins(map.get("avgTimeOnPage")));
                map.put("bounceRate", ApiUtils.removePercent(map.get("bounceRate") + ""));
                map.put("percentNewSessions", ApiUtils.removePercent(map.get("percentNewSessions") + ""));
                map.put("pagePath", map.get("ga:pagePath"));
                Integer engagements = 0;
                engagements += (ApiUtils.toInteger(map.get("directionsPageView"))
                        + ApiUtils.toInteger(map.get("inventoryPageViews"))
                        + ApiUtils.toInteger(map.get("leadSubmission"))
                        + ApiUtils.toInteger(map.get("specialsPageView"))
                        + ApiUtils.toInteger(map.get("timeOnSiteGt2Mins"))
                        + ApiUtils.toInteger(map.get("vdpViews")));
                map.put("engagements", engagements + "");

            }
        }
        returnMap.put("data", gaDataMap);
        return returnMap;
    }

    @RequestMapping(value = "accountDevicePerformance", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    Object getAccountDevicePerformance(HttpServletRequest request, HttpServletResponse response) {
        Date startDate = DateUtils.getStartDate(request.getParameter("startDate"));
        Date endDate = DateUtils.getEndDate(request.getParameter("endDate"));

        String fieldsOnly = request.getParameter("fieldsOnly");
        Map returnMap = new HashMap();
        List<ColumnDef> columnDefs = new ArrayList<>();
        columnDefs.add(new ColumnDef("visits", "number", "Visits", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
        columnDefs.add(new ColumnDef("percentNewSessions", "string", "% New Sessions", ColumnDef.Aggregation.AVG, ColumnDef.Format.PERCENTAGE));
        columnDefs.add(new ColumnDef("bounceRate", "string", "Bounce Rage", ColumnDef.Aggregation.AVG, ColumnDef.Format.PERCENTAGE));
        columnDefs.add(new ColumnDef("avgTimeOnPage", "string", "Average Time On Page"));
        columnDefs.add(new ColumnDef("directionsPageView", "number", "Directions Page View", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
        columnDefs.add(new ColumnDef("inventoryPageViews", "number", "Inventory Page Views", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
        columnDefs.add(new ColumnDef("leadSubmission", "number", "Lead Submission", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
        columnDefs.add(new ColumnDef("specialsPageView", "number", "Specials Page View", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
        columnDefs.add(new ColumnDef("timeOnSiteGt2Mins", "number", "Time On Site > 2Mins", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
        columnDefs.add(new ColumnDef("vdpViews", "number", "VDP Views", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
        columnDefs.add(new ColumnDef("engagements", "number", "Engagements", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
        columnDefs.add(new ColumnDef("deviceCategory", "string", "Device"));

        returnMap.put("columnDefs", columnDefs);
        if (fieldsOnly != null) {
            return returnMap;
        }
        AccountDetails accountDetails = ApiUtils.toAccountDetails(request, "seo");
        List<Map<String, String>> gaDataMap = new ArrayList<>();
        if (accountDetails.getAnalyticsProfileId() != null) {
            String metricsList = "ga:visits,visits;ga:percentNewSessions,percentNewSessions;"
                    + "ga:bounceRate,bounceRate;ga:avgTimeOnPage,avgTimeOnPage;"
                    + gaService.getGaGoals(accountDetails.getAnalyticsAccountId(), accountDetails.getAnalyticsProfileId());
            String dimensions = "ga:deviceCategory";
            String filter = "ga:channelGrouping==Organic Search";

            GetReportsResponse gaData = gaService.getGenericData(accountDetails.getAnalyticsProfileId(), startDate, endDate, null, null, metricsList, dimensions, filter);
            gaDataMap = (List) gaService.getResponseAsMap(gaData).get("data");
            for (Iterator<Map<String, String>> iterator = gaDataMap.iterator(); iterator.hasNext();) {
                Map<String, String> map = iterator.next();
                map.put("avgTimeOnPage", ApiUtils.toMins(map.get("avgTimeOnPage")));
                map.put("bounceRate", ApiUtils.removePercent(map.get("bounceRate") + ""));
                map.put("percentNewSessions", ApiUtils.removePercent(map.get("percentNewSessions") + ""));
                map.put("deviceCategory", map.get("ga:deviceCategory"));
                Integer engagements = 0;
                engagements += (ApiUtils.toInteger(map.get("directionsPageView"))
                        + ApiUtils.toInteger(map.get("inventoryPageViews"))
                        + ApiUtils.toInteger(map.get("leadSubmission"))
                        + ApiUtils.toInteger(map.get("specialsPageView"))
                        + ApiUtils.toInteger(map.get("timeOnSiteGt2Mins"))
                        + ApiUtils.toInteger(map.get("vdpViews")));
                map.put("engagements", engagements + "");
            }
        }
        returnMap.put("data", gaDataMap);
        return returnMap;
    }

    @RequestMapping(value = "accountGeoPerformance", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    Object getAccountGeoPerformance(HttpServletRequest request, HttpServletResponse response) {
        Date startDate = DateUtils.getStartDate(request.getParameter("startDate"));
        Date endDate = DateUtils.getEndDate(request.getParameter("endDate"));

        String fieldsOnly = request.getParameter("fieldsOnly");
        Map returnMap = new HashMap();
        List<ColumnDef> columnDefs = new ArrayList<>();
        columnDefs.add(new ColumnDef("visits", "number", "Visits", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
        columnDefs.add(new ColumnDef("percentNewSessions", "string", "% New Sessions", ColumnDef.Aggregation.AVG, ColumnDef.Format.PERCENTAGE));
        columnDefs.add(new ColumnDef("bounceRate", "string", "Bounce Rage", ColumnDef.Aggregation.AVG, ColumnDef.Format.PERCENTAGE));
        columnDefs.add(new ColumnDef("avgTimeOnPage", "string", "Average Time On Page"));
        columnDefs.add(new ColumnDef("directionsPageView", "number", "Directions Page View", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
        columnDefs.add(new ColumnDef("inventoryPageViews", "number", "Inventory Page Views", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
        columnDefs.add(new ColumnDef("leadSubmission", "number", "Lead Submission", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
        columnDefs.add(new ColumnDef("specialsPageView", "number", "Specials Page View", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
        columnDefs.add(new ColumnDef("timeOnSiteGt2Mins", "number", "Time On Site > 2Mins", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
        columnDefs.add(new ColumnDef("vdpViews", "number", "VDP Views", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
        columnDefs.add(new ColumnDef("engagements", "number", "Engagements", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
        columnDefs.add(new ColumnDef("city", "String", "City"));

        returnMap.put("columnDefs", columnDefs);
        if (fieldsOnly != null) {
            return returnMap;
        }
        AccountDetails accountDetails = ApiUtils.toAccountDetails(request, "seo");
        List<Map<String, String>> gaDataMap = new ArrayList<>();
        if (accountDetails.getAnalyticsProfileId() != null) {
            String metricsList = "ga:visits,visits;ga:percentNewSessions,percentNewSessions;"
                    + "ga:bounceRate,bounceRate;ga:avgTimeOnPage,avgTimeOnPage;"
                    + gaService.getGaGoals(accountDetails.getAnalyticsAccountId(), accountDetails.getAnalyticsProfileId());
            String dimensions = "ga:city";
            String filter = "ga:channelGrouping==Organic Search";

            GetReportsResponse gaData = gaService.getGenericData(accountDetails.getAnalyticsProfileId(), startDate, endDate, null, null, metricsList, dimensions, filter);
            gaDataMap = (List) gaService.getResponseAsMap(gaData).get("data");
            for (Iterator<Map<String, String>> iterator = gaDataMap.iterator(); iterator.hasNext();) {
                Map<String, String> map = iterator.next();
                map.put("avgTimeOnPage", ApiUtils.toMins(map.get("avgTimeOnPage")));
                map.put("bounceRate", ApiUtils.removePercent(map.get("bounceRate") + ""));
                map.put("percentNewSessions", ApiUtils.removePercent(map.get("percentNewSessions") + ""));
                map.put("city", map.get("ga:city"));
                Integer engagements = 0;
                engagements += (ApiUtils.toInteger(map.get("directionsPageView"))
                        + ApiUtils.toInteger(map.get("inventoryPageViews"))
                        + ApiUtils.toInteger(map.get("leadSubmission"))
                        + ApiUtils.toInteger(map.get("specialsPageView"))
                        + ApiUtils.toInteger(map.get("timeOnSiteGt2Mins"))
                        + ApiUtils.toInteger(map.get("vdpViews")));
                map.put("engagements", engagements + "");

            }
        }
        returnMap.put("data", gaDataMap);
        return returnMap;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void handle(HttpMessageNotReadableException e) {
        e.printStackTrace();
    }
}
