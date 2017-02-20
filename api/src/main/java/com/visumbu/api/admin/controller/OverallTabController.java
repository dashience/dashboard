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
import com.visumbu.api.admin.service.CenturyCallService;
import com.visumbu.api.admin.service.DealerVaultService;
import com.visumbu.api.admin.service.DynamicDisplayService;
import com.visumbu.api.admin.service.FacebookService;
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
import com.visumbu.api.adwords.report.xml.bean.VideoReport;
import com.visumbu.api.adwords.report.xml.bean.VideoReportRow;
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
import com.visumbu.api.dashboard.bean.VideoPerformanceReportBean;
import com.visumbu.api.utils.ApiUtils;
import com.visumbu.api.utils.DateUtils;
import com.visumbu.api.utils.JsonSimpleUtils;
import com.visumbu.api.utils.Rest;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
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
import org.apache.commons.beanutils.BeanUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
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
@RequestMapping("overall")
public class OverallTabController {

    @Autowired
    private UserService userService;

    @Autowired
    private AdwordsService adwordsService;

    @Autowired
    private BingService bingService;

    @Autowired
    private DynamicDisplayService dynamicDisplayService;

    @Autowired
    private FacebookService facebookService;

    @Autowired
    private GaService gaService;

    @Autowired
    private CenturyCallService centuryCallService;

    @Autowired
    private DealerVaultService dealerVaultService;

    public static final Long bingAccountId = 2610614L;

    @RequestMapping(value = "inventory", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    Object getInventory(HttpServletRequest request, HttpServletResponse response) {
        Date startDate = DateUtils.getStartDate(request.getParameter("startDate"));
        Date endDate = DateUtils.getEndDate(request.getParameter("endDate"));
        String fieldsOnly = request.getParameter("fieldsOnly");
        String dealerId = request.getParameter("dealerMapId");
        return dealerVaultService.getInventory(startDate, endDate, dealerId, fieldsOnly);
    }

    @RequestMapping(value = "totalNoOfCalls", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    Object getTotalNoOfCalls(HttpServletRequest request, HttpServletResponse response) {
        Date startDate = DateUtils.getStartDate(request.getParameter("startDate"));
        Date endDate = DateUtils.getEndDate(request.getParameter("endDate"));
        String dealerId = request.getParameter("dealerMapId");

        String fieldsOnly = request.getParameter("fieldsOnly");
        Map returnMap = new HashMap();
        List<ColumnDef> columnDefs = new ArrayList<>();
        columnDefs.add(new ColumnDef("totalNoOfCalls", "number", "Total Calls", 1));

        returnMap.put("columnDefs", columnDefs);
        if (fieldsOnly != null) {
            return returnMap;
        }
        // AccountDetails accountDetails = ApiUtils.toAccountDetails(request, "facebook");
        if (dealerId != null) {
            Integer totalNoOfCalls = centuryCallService.getTotalNoOfCalls(startDate, endDate, dealerId, fieldsOnly);
            Map map = new HashMap();
            map.put("totalNoOfCalls", totalNoOfCalls);
            List<Map> list = new ArrayList<>();
            list.add(map);
            returnMap.put("data", list);
        } else {
            returnMap.put("data", null);
        }
        return returnMap;
    }

    @RequestMapping(value = "totalNoOfSales", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    Object getTotalNoOfSales(HttpServletRequest request, HttpServletResponse response) {
        Date startDate = DateUtils.getStartDate(request.getParameter("startDate"));
        Date endDate = DateUtils.getEndDate(request.getParameter("endDate"));
        String dealerId = request.getParameter("dealerMapId");

        String fieldsOnly = request.getParameter("fieldsOnly");
        Map returnMap = new HashMap();
        List<ColumnDef> columnDefs = new ArrayList<>();
        columnDefs.add(new ColumnDef("totalNoOfSales", "budget", "Total Sales", 1));

        returnMap.put("columnDefs", columnDefs);
        if (fieldsOnly != null) {
            return returnMap;
        }
        // AccountDetails accountDetails = ApiUtils.toAccountDetails(request, "facebook");
        if (dealerId != null) {
            Integer totalNoOfCalls = centuryCallService.getTotalNoOfSales(startDate, endDate, dealerId, fieldsOnly);
            Map map = new HashMap();
            map.put("totalNoOfSales", totalNoOfCalls);
            List<Map> list = new ArrayList<>();
            list.add(map);
            returnMap.put("data", list);
        } else {
            returnMap.put("data", null);
        }
        return returnMap;
    }

    @RequestMapping(value = "totalBudget", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    Object getTotalBudget(HttpServletRequest request, HttpServletResponse response) {
        Date startDate = DateUtils.getStartDate(request.getParameter("startDate"));
        Date endDate = DateUtils.getEndDate(request.getParameter("endDate"));
        String dealerId = request.getParameter("dealerMapId");

        String fieldsOnly = request.getParameter("fieldsOnly");
        Map returnMap = new HashMap();
        List<ColumnDef> columnDefs = new ArrayList<>();
        columnDefs.add(new ColumnDef("budget", "number", "Budget", 1));

        returnMap.put("columnDefs", columnDefs);
        if (fieldsOnly != null) {
            return returnMap;
        }
        // AccountDetails accountDetails = ApiUtils.toAccountDetails(request, "facebook");
        if (dealerId != null) {
            Double budget = centuryCallService.getTotalBudget(startDate, endDate, dealerId, fieldsOnly);
            Map map = new HashMap();
            map.put("budget", budget);
            List<Map> list = new ArrayList<>();
            list.add(map);
            returnMap.put("data", list);
        } else {
            returnMap.put("data", null);
        }
        return returnMap;
    }

    @RequestMapping(value = "overallPerformance/{frequency}/{range}/{count}", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    Object getAdPerformance(HttpServletRequest request, HttpServletResponse response,
            @PathVariable String frequency,
            @PathVariable String range, @PathVariable Integer count) {
        Map returnMap = new HashMap();
        try {
            Date startDate = DateUtils.getStartDate(request.getParameter("startDate"));
            Date endDate = DateUtils.getEndDate(request.getParameter("endDate"));
            String dealerId = request.getParameter("dealerMapId");
            String aggregation = "";
            if (range.equalsIgnoreCase("week")) {
                aggregation = "weekly";
                if (count == null || count == 0) {
                    count = 12;
                }
                startDate = DateUtils.goBack(request.getParameter("endDate"), range, count);
            }
            String fieldsOnly = request.getParameter("fieldsOnly");
            System.out.println(startDate);
            System.out.println(endDate);

            List<ColumnDef> columnDefs = new ArrayList<>();
            columnDefs.add(new ColumnDef("source", "string", "Source", 1));
            columnDefs.add(new ColumnDef("impressions", "number", "Impressions", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
            columnDefs.add(new ColumnDef("clicks", "number", "Clicks", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
            columnDefs.add(new ColumnDef("ctr", "number", "CTR", ColumnDef.Aggregation.CTR, ColumnDef.Format.PERCENTAGE));
            if (aggregation.equalsIgnoreCase("weekly")) {
                columnDefs.add(new ColumnDef("day", "string", "Date"));
            }
            columnDefs.add(new ColumnDef("cost", "number", "Cost", ColumnDef.Aggregation.SUM, ColumnDef.Format.CURRENCY));
            columnDefs.add(new ColumnDef("averageCpc", "number", "Average CPC", ColumnDef.Aggregation.CPC, ColumnDef.Format.CURRENCY));
            columnDefs.add(new ColumnDef("averagePosition", "number", "Average Position", ColumnDef.Aggregation.AVG, ColumnDef.Format.DECIMAL1));
            columnDefs.add(new ColumnDef("conversions", "number", "Conversions", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
            columnDefs.add(new ColumnDef("cpa", "number", "CPL", ColumnDef.Aggregation.CPA, ColumnDef.Format.CURRENCY));
            returnMap.put("columnDefs", columnDefs);
            if (fieldsOnly != null) {
                return returnMap;
            }
            List<AccountPerformanceReportBean> performanceReportBeans = new ArrayList<>();
            AccountDetails accountDetails = ApiUtils.toAccountDetails(request, "ppc");
            if (accountDetails.getAdwordsAccountId() != null) {
                AccountReport adwordsAccountReport = adwordsService.getAccountReport(startDate, endDate, accountDetails.getAdwordsAccountId(), aggregation, "SEARCH");
                List<AccountReportRow> adwordsAccountRow = adwordsAccountReport.getAccountReportRow();

                for (Iterator<AccountReportRow> reportRow = adwordsAccountRow.iterator(); reportRow.hasNext();) {
                    AccountReportRow row = reportRow.next();
                    AccountPerformanceReportBean performanceBean = new AccountPerformanceReportBean();
                    performanceBean.setSource("Paid Search");
                    performanceBean.setImpressions(row.getImpressions());
                    performanceBean.setClicks(row.getClicks());
                    performanceBean.setCtr(row.getCtr());
                    performanceBean.setCost(row.getCost());
                    performanceBean.setDay(row.getDay());
                    performanceBean.setAverageCpc(row.getAvgCPC());
                    performanceBean.setCpa(row.getCostConv());
                    performanceBean.setAveragePosition(row.getAvgPosition());
                    performanceBean.setConversions(row.getConversions());
                    performanceReportBeans.add(performanceBean);
                }
            }
            if (accountDetails.getBingAccountId() != null) {
                AccountPerformanceReport bingAccountReport = bingService.getAccountPerformanceReport(startDate, endDate, accountDetails.getBingAccountId(), aggregation);
                List<AccountPerformanceRow> bingAccountRows = bingAccountReport.getAccountPerformanceRows();
                for (Iterator<AccountPerformanceRow> reportRow = bingAccountRows.iterator(); reportRow.hasNext();) {
                    AccountPerformanceRow row = reportRow.next();
                    AccountPerformanceReportBean performanceBean = new AccountPerformanceReportBean();
                    performanceBean.setSource("Paid Search");
                    performanceBean.setImpressions(row.getImpressions().getValue());
                    performanceBean.setClicks(row.getClicks().getValue());
                    performanceBean.setCtr(row.getCtr().getValue());
                    if (aggregation.equalsIgnoreCase("week")) {
                        performanceBean.setDay(DateUtils.getStartDayOfWeek(DateUtils.toDate(row.getGregorianDate().getValue(), "MM/dd/yyyy")));
                    }
                    performanceBean.setCost(row.getSpend().getValue());
                    performanceBean.setAverageCpc(row.getAverageCpc().getValue());
                    performanceBean.setCpa(row.getCostPerConversion().getValue());
                    performanceBean.setAveragePosition(row.getAveragePosition().getValue());
                    performanceBean.setConversions(row.getConversions().getValue());
                    performanceReportBeans.add(performanceBean);
                }
            }
            // Display
            accountDetails = ApiUtils.toAccountDetails(request, "display");
            if (accountDetails.getAdwordsAccountId() != null) {
                List<Map<String, String>> gaData = new ArrayList<>();
                if (accountDetails.getAnalyticsProfileId() != null) {
                    GetReportsResponse goals = gaService.getGoals(accountDetails.getAnalyticsAccountId(), accountDetails.getAnalyticsProfileId(), startDate, endDate, "");
                    gaData = (List) gaService.getResponseAsMap(goals).get("data");
                }
                AccountReport displayAdwordsAccountReport = adwordsService.getAccountReport(startDate, endDate, accountDetails.getAdwordsAccountId(), aggregation, "CONTENT");
                List<AccountReportRow> displayAdwordsAccountRow = displayAdwordsAccountReport.getAccountReportRow();
                //List<AccountPerformanceReportBean> performanceReportBeans = new ArrayList<>();
                for (Iterator<AccountReportRow> reportRow = displayAdwordsAccountRow.iterator(); reportRow.hasNext();) {
                    AccountReportRow row = reportRow.next();
                    AccountPerformanceReportBean performanceBean = new AccountPerformanceReportBean();
                    performanceBean.setSource("Display");
                    performanceBean.setImpressions(row.getImpressions());
                    performanceBean.setClicks(row.getClicks());
                    performanceBean.setCtr(row.getCtr());
                    performanceBean.setCost(row.getCost());
                    performanceBean.setAverageCpc(row.getAvgCPC());
                    performanceBean.setCpa(row.getCostConv());
                    performanceBean.setDay(row.getWeek());
                    performanceBean.setAveragePosition(row.getAvgPosition());
                    performanceBean.setConversions(row.getConversions());

                    performanceBean.setDirectionsPageView(getGaDataFor(gaData, row.getDay(), "directionsPageView"));
                    performanceBean.setInventoryPageViews(getGaDataFor(gaData, row.getDay(), "inventoryPageViews"));
                    performanceBean.setLeadSubmission(getGaDataFor(gaData, row.getDay(), "leadSubmission"));
                    performanceBean.setSpecialsPageView(getGaDataFor(gaData, row.getDay(), "specialsPageView"));
                    performanceBean.setTimeOnSiteGt2Mins(getGaDataFor(gaData, row.getDay(), "timeOnSiteGt2Mins"));
                    performanceBean.setVdpViews(getGaDataFor(gaData, row.getDay(), "vdpViews"));
                    Integer engagements = 0;
                    engagements += (ApiUtils.toInteger(performanceBean.getDirectionsPageView() == null ? "0" : performanceBean.getDirectionsPageView())
                            + ApiUtils.toInteger(performanceBean.getInventoryPageViews() == null ? "0" : performanceBean.getInventoryPageViews())
                            + ApiUtils.toInteger(performanceBean.getLeadSubmission() == null ? "0" : performanceBean.getLeadSubmission())
                            + ApiUtils.toInteger(performanceBean.getSpecialsPageView() == null ? "0" : performanceBean.getSpecialsPageView())
                            + ApiUtils.toInteger(performanceBean.getTimeOnSiteGt2Mins() == null ? "0" : performanceBean.getTimeOnSiteGt2Mins())
                            + ApiUtils.toInteger(performanceBean.getVdpViews() == null ? "0" : performanceBean.getVdpViews()));
                    performanceBean.setConversions(engagements + "");
                    performanceReportBeans.add(performanceBean);
                }
            }
            // Video
            accountDetails = ApiUtils.toAccountDetails(request, "youtube");
            if (accountDetails.getAdwordsAccountId() != null) {
                VideoReport adwordsVideoReport = adwordsService.getVideoReport(startDate, endDate, accountDetails.getAdwordsAccountId(), aggregation, "YOUTUBE_WATCH");
                List<VideoReportRow> adwordsVideoRow = adwordsVideoReport.getVideoReportRow();
                // List<VideoPerformanceReportBean> performanceReportBeans = new ArrayList<>();
                for (Iterator<VideoReportRow> reportRow = adwordsVideoRow.iterator(); reportRow.hasNext();) {
                    VideoReportRow row = reportRow.next();
                    AccountPerformanceReportBean performanceBean = new AccountPerformanceReportBean();
                    performanceBean.setSource("Video");
                    performanceBean.setImpressions(row.getImpressions());
                    performanceBean.setClicks(row.getClicks());
                    if (aggregation.equalsIgnoreCase("week")) {
                        performanceBean.setDay(row.getWeek());
                    }
                    performanceBean.setCtr(row.getCtr());
                    performanceBean.setCost(row.getCost());
                    performanceBean.setAverageCpc(row.getAvgCPC());
                    performanceBean.setCpa(row.getCostConv());
                    performanceBean.setAveragePosition(row.getAvgPosition());
                    performanceBean.setConversions(row.getConversions());
                    performanceReportBeans.add(performanceBean);
                }
            }
            // Paid Social
            // List<AccountPerformanceReportBean> paidSocialPerformanceReportBeans = new ArrayList<>();
            accountDetails = ApiUtils.toAccountDetails(request, "facebook");
            if (accountDetails.getFacebookAccountId() != null) {
                List<Map<String, String>> accountPerformance = (List<Map<String, String>>) facebookService.getAccountPerformance(accountDetails.getFacebookAccountId(), startDate, endDate, aggregation);
                for (Iterator<Map<String, String>> iterator = accountPerformance.iterator(); iterator.hasNext();) {
                    Map<String, String> paidSocialPerformance = iterator.next();
                    AccountPerformanceReportBean performanceBean = new AccountPerformanceReportBean();
                    performanceBean.setSource("Paid Social");
                    performanceBean.setImpressions(paidSocialPerformance.get("impressions"));
                    performanceBean.setClicks(paidSocialPerformance.get("clicks"));
                    performanceBean.setCtr(paidSocialPerformance.get("ctr"));
                    performanceBean.setCost(paidSocialPerformance.get("spend"));
                    performanceBean.setDay(paidSocialPerformance.get("date_start"));
                    performanceBean.setAverageCpc(paidSocialPerformance.get("cpc"));
                    // performanceBean.setCpa(paidSocialPerformance.get("cost_page_engagement"));
                    performanceBean.setCpa("0");
                    performanceBean.setAveragePosition("0");
                    //performanceBean.setConversions(paidSocialPerformance.get("actions_page_engagement"));
                    performanceBean.setConversions("0");
                    performanceReportBeans.add(performanceBean);
                }
            }
            // Dynamic Display
            try {
                Map jsonMap = JsonSimpleUtils.jsonToMap((JSONObject) dynamicDisplayService.getAccountPerformance(startDate, endDate, dealerId, fieldsOnly));
                List jsonDataList = (List) jsonMap.get("data");
                System.out.println("DYNAMIC DISPLAY ");
                System.out.println(jsonDataList);
                for (Iterator iterator = jsonDataList.iterator(); iterator.hasNext();) {
                    System.out.println("CURRENT DATA DYNAMIC DISPLAY");
                    Map map = (Map) iterator.next();
                    System.out.println(map);
                    AccountPerformanceReportBean performanceBean = new AccountPerformanceReportBean();
                    performanceBean.setSource("Dynamic Display");
                    performanceBean.setImpressions(map.get("impression") + "");
                    performanceBean.setClicks(map.get("engage_clicks") + "");
                    performanceBean.setCtr(map.get("ctr") + "");
                    performanceBean.setCost("0");
                    performanceBean.setDay("-");
                    performanceBean.setAverageCpc("0");
                    performanceBean.setCpa("0");
                    performanceBean.setAveragePosition("0");
                    performanceBean.setConversions(map.get("direct_conversions") + "");
                    performanceReportBeans.add(performanceBean);
                }
            } catch (Exception ex) {

            }

            System.out.println(" Range " + range);

            if (frequency.equalsIgnoreCase("summary")) {
                returnMap.put("data", sumPerSource(performanceReportBeans, ""));
            } else if (range.equalsIgnoreCase("week")) {
                returnMap.put("data", sumPerSource(performanceReportBeans, "day"));
            } else {
                returnMap.put("data", sumPerSource(performanceReportBeans, "source"));
            }
        } catch (Exception ex) {
            Logger.getLogger(PaidTabController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return returnMap;
    }

    public static List<AccountPerformanceReportBean> sumPerSource(List<AccountPerformanceReportBean> list, String fieldName) {
        System.out.println("Summary -> " + fieldName);
        Map<String, AccountPerformanceReportBean> map = new HashMap<>();
        for (AccountPerformanceReportBean p : list) {
            String name = "Overall";
            if (fieldName != null && !fieldName.isEmpty()) {
                try {
                    name = BeanUtils.getProperty(p, fieldName);
                } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {
                    Logger.getLogger(OverallTabController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            AccountPerformanceReportBean sum = map.get(name);
            if (sum == null) {
                sum = new AccountPerformanceReportBean();
                map.put(name, sum);
            }
            if (fieldName != null && !fieldName.isEmpty()) {
                try {
                    BeanUtils.setProperty(p, fieldName, name);
                } catch (IllegalAccessException | InvocationTargetException ex) {
                    Logger.getLogger(OverallTabController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            sum.setSource(name);
            sum.setImpressions((ApiUtils.toInteger(p.getImpressions()) + ApiUtils.toInteger(sum.getImpressions())) + "");
            sum.setClicks((ApiUtils.toInteger(p.getClicks()) + ApiUtils.toInteger(sum.getClicks())) + "");
            sum.setCost((ApiUtils.toDouble(p.getCost()) + ApiUtils.toDouble(sum.getCost())) + "");
            sum.setAveragePosition((ApiUtils.toDouble(p.getAveragePosition()) + ApiUtils.toDouble(sum.getAveragePosition())) / 2 + "");
            sum.setConversions((ApiUtils.toDouble(p.getConversions()) + ApiUtils.toDouble(sum.getConversions())) + "");
            sum.setCtr(ApiUtils.toDouble(sum.getImpressions()) == 0.0 ? "0" : (ApiUtils.toDouble(sum.getClicks()) / ApiUtils.toDouble(sum.getImpressions())) + "");
            sum.setAverageCpc(ApiUtils.toDouble(sum.getClicks()) == 0.0 ? "0" : (ApiUtils.toDouble(sum.getCost()) / ApiUtils.toDouble(sum.getClicks())) + "");
            sum.setCpa(ApiUtils.toDouble(sum.getConversions()) == 0.0 ? "0" : (ApiUtils.toDouble(sum.getCost()) / ApiUtils.toDouble(sum.getConversions())) + "");
        }
        return new ArrayList<AccountPerformanceReportBean>(map.values());
    }

    private String getGaDataFor(List<Map<String, String>> gaData, String date, String metric) {
        for (Iterator<Map<String, String>> iterator = gaData.iterator(); iterator.hasNext();) {
            Map<String, String> data = iterator.next();
            return data.get(metric);
        }
        return "";
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void handle(HttpMessageNotReadableException e) {
        e.printStackTrace();
    }
}
