/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.api.admin.controller;

import com.visumbu.api.admin.service.AdwordsService;
import com.visumbu.api.admin.service.BingService;
import com.visumbu.api.admin.service.CenturyCallService;
import com.visumbu.api.admin.service.UserService;
import com.visumbu.api.adwords.report.xml.bean.AccountDeviceReport;
import com.visumbu.api.adwords.report.xml.bean.AccountDeviceReportRow;
import com.visumbu.api.adwords.report.xml.bean.AccountReport;
import com.visumbu.api.adwords.report.xml.bean.AccountReportRow;
import com.visumbu.api.adwords.report.xml.bean.AdGroupReportRow;
import com.visumbu.api.adwords.report.xml.bean.AdReport;
import com.visumbu.api.adwords.report.xml.bean.AdReportRow;
import com.visumbu.api.adwords.report.xml.bean.AddGroupReport;
import com.visumbu.api.adwords.report.xml.bean.CallConversionReport;
import com.visumbu.api.adwords.report.xml.bean.CallConversionReportRow;
import com.visumbu.api.adwords.report.xml.bean.CampaignDeviceReport;
import com.visumbu.api.adwords.report.xml.bean.CampaignDeviceReportRow;
import com.visumbu.api.adwords.report.xml.bean.CampaignPerformanceReportRow;
import com.visumbu.api.adwords.report.xml.bean.CampaignReport;
import com.visumbu.api.adwords.report.xml.bean.CampaignReportRow;
import com.visumbu.api.adwords.report.xml.bean.GeoReport;
import com.visumbu.api.adwords.report.xml.bean.GeoReportRow;
import com.visumbu.api.bean.ColumnDef;
import com.visumbu.api.bean.AccountDetails;
import com.visumbu.api.bing.report.xml.bean.AccountDevicePerformanceReport;
import com.visumbu.api.bing.report.xml.bean.AccountDevicePerformanceRow;
import com.visumbu.api.bing.report.xml.bean.AccountPerformanceReport;
import com.visumbu.api.bing.report.xml.bean.AccountPerformanceRow;
import com.visumbu.api.bing.report.xml.bean.AdGroupPerformanceReport;
import com.visumbu.api.bing.report.xml.bean.AdGroupPerformanceRow;
import com.visumbu.api.bing.report.xml.bean.AdPerformanceReport;
import com.visumbu.api.bing.report.xml.bean.AdPerformanceRow;
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
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 *
 * @author Varghees
 */
@Controller
@RequestMapping("paid")
public class PaidTabController {

    @Autowired
    private UserService userService;

    @Autowired
    private BingService bingService;

    @Autowired
    private CenturyCallService centuryCallService;

    @Autowired
    private AdwordsService adwordsService;

    // public static final Long bingAccountId = 2610614L;
    @RequestMapping(value = "accountPerformance", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    Object getAccountPerformance(HttpServletRequest request, HttpServletResponse response) {
        Map returnMap = new HashMap();
        try {
            Date startDate = DateUtils.getStartDate(request.getParameter("startDate"));
            Date endDate = DateUtils.getEndDate(request.getParameter("endDate"));
            String fieldsOnly = request.getParameter("fieldsOnly");

            System.out.println("-------------------------");
            System.out.println(startDate);
            System.out.println(endDate);
            System.out.println("-------------------------");
            List<ColumnDef> columnDefs = new ArrayList<>();
            columnDefs.add(new ColumnDef("source", "string", "Source"));
            columnDefs.add(new ColumnDef("overall", "string", "Overall", 1));
            columnDefs.add(new ColumnDef("impressions", "number", "Impressions", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
            columnDefs.add(new ColumnDef("clicks", "number", "Clicks", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
            columnDefs.add(new ColumnDef("ctr", "number", "CTR", ColumnDef.Aggregation.CTR, ColumnDef.Format.PERCENTAGE));
            columnDefs.add(new ColumnDef("cost", "number", "Cost", ColumnDef.Aggregation.SUM, ColumnDef.Format.CURRENCY));
            columnDefs.add(new ColumnDef("averageCpc", "number", "Average CPC", ColumnDef.Aggregation.CPC, ColumnDef.Format.CURRENCY));
            columnDefs.add(new ColumnDef("averagePosition", "number", "Average Position", ColumnDef.Aggregation.AVG, ColumnDef.Format.DECIMAL1));
            columnDefs.add(new ColumnDef("conversions", "number", "Conversions", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
            columnDefs.add(new ColumnDef("cpa", "number", "CPL", ColumnDef.Aggregation.CPA, ColumnDef.Format.CURRENCY));
            columnDefs.add(new ColumnDef("searchImpressionsShare", "number", "Search Impression Share", ColumnDef.Aggregation.AVG, ColumnDef.Format.PERCENTAGE));
            returnMap.put("columnDefs", columnDefs);
            if (fieldsOnly != null) {
                return returnMap;
            }
            AccountDetails accountDetails = ApiUtils.toAccountDetails(request, "ppc");
            List<AccountPerformanceReportBean> performanceReportBeans = new ArrayList<>();
            if (accountDetails.getAdwordsAccountId() != null) {
                System.out.println(accountDetails.getAdwordsAccountId());
                //AccountReport adwordsAccountReport = adwordsService.getAccountReport(startDate, endDate, "142-465-1427", "", "SEARCH");
                AccountReport adwordsAccountReport = adwordsService.getAccountReport(startDate, endDate, accountDetails.getAdwordsAccountId(), "", "SEARCH");
                com.visumbu.api.adwords.report.xml.bean.CampaignPerformanceReport adWordsCampaignPerformanceReport = adwordsService.getCampaignPerformanceReport(startDate, endDate, accountDetails.getAdwordsAccountId(), "", "SEARCH");
                Integer conversionSum = 0;
                List<CampaignPerformanceReportRow> campaignPerformanceReportRow = adWordsCampaignPerformanceReport.getCampaignPerformanceReportRow();
                for (Iterator<CampaignPerformanceReportRow> iterator = campaignPerformanceReportRow.iterator(); iterator.hasNext();) {
                    CampaignPerformanceReportRow reportRow = iterator.next();
                    System.out.println("Conversion => " + reportRow.getPhoneCalls());
                    conversionSum += ApiUtils.toInteger(reportRow.getPhoneCalls());
                }

                CallConversionReport accountCallConversionsReport = adwordsService.getAccountCallConversionsReport(startDate, endDate, accountDetails.getAdwordsAccountId(), "", "SEARCH");
                if (accountCallConversionsReport != null) {
                    List<CallConversionReportRow> callConversionReportRow = accountCallConversionsReport.getCallConversionReportRow();
                    for (Iterator<CallConversionReportRow> iterator = callConversionReportRow.iterator(); iterator.hasNext();) {
                        CallConversionReportRow reportRow = iterator.next();
                        System.out.println("Conversion1 => " + ApiUtils.toDouble(reportRow.getConversions()));
                        conversionSum += ApiUtils.toDouble(reportRow.getConversions()).intValue();
                    }
                }
                List<AccountReportRow> adwordsAccountRow = adwordsAccountReport.getAccountReportRow();

                for (Iterator<AccountReportRow> reportRow = adwordsAccountRow.iterator(); reportRow.hasNext();) {
                    AccountReportRow row = reportRow.next();
                    AccountPerformanceReportBean performanceBean = new AccountPerformanceReportBean();
                    performanceBean.setOverall("Overall");
                    performanceBean.setSource("Google");
//            performanceBean.setDevice(row.getDevice());
                    performanceBean.setImpressions(row.getImpressions());
                    performanceBean.setClicks(row.getClicks());
                    performanceBean.setCtr(row.getCtr());
                    String cost = row.getCost(); //Integer.toString(Integer.parseInt(row.getCost()) / 1000000);
                    performanceBean.setCost(cost);
                    String cpc = row.getAvgCPC(); //Integer.toString(Integer.parseInt(row.getAvgCPC()) / 1000000);
                    performanceBean.setAverageCpc(cpc);
                    String cpa = row.getCostConv(); //Integer.toString(Integer.parseInt(row.getCostConv()) / 1000000);
                    performanceBean.setCpa(cpa);

                    performanceBean.setAveragePosition(row.getAvgPosition());
                    performanceBean.setConversions(conversionSum + "");
                    performanceBean.setSearchImpressionsShare(row.getSearchImprShare());
                    performanceBean.setSearchImpressionsShareLostDueToRank(row.getSearchLostISRank());
                    performanceBean.setSearchImpressionsShareLostDueToBudget(row.getSearchLostISBudget());
                    performanceReportBeans.add(performanceBean);
                }
            }

            if (accountDetails.getBingAccountId() != null) {
                System.out.println("Getting BING ACCOUNT ID Long " + accountDetails.getBingAccountId());
                AccountPerformanceReport bingAccountReport = bingService.getAccountPerformanceReport(startDate, endDate, accountDetails.getBingAccountId(), "");
                List<AccountPerformanceRow> bingAccountRows = bingAccountReport.getAccountPerformanceRows();

                for (Iterator<AccountPerformanceRow> reportRow = bingAccountRows.iterator(); reportRow.hasNext();) {
                    AccountPerformanceRow row = reportRow.next();
                    AccountPerformanceReportBean performanceBean = new AccountPerformanceReportBean();
                    performanceBean.setOverall("Overall");
                    performanceBean.setSource("Bing");
                    performanceBean.setImpressions(row.getImpressions().getValue());
                    performanceBean.setClicks(row.getClicks().getValue());
                    performanceBean.setCtr(row.getCtr().getValue());
                    performanceBean.setCost(row.getSpend().getValue());
                    performanceBean.setAverageCpc(row.getAverageCpc().getValue());
                    performanceBean.setAveragePosition(row.getAveragePosition().getValue());
                    System.out.println("Bing Conversions" + row.getPhoneCalls().getValue());
                    performanceBean.setConversions(row.getPhoneCalls().getValue());
                    performanceBean.setCpa(row.getCostPerConversion().getValue());
                    performanceBean.setSearchImpressionsShare(row.getImpressionSharePercent().getValue());
                    performanceBean.setSearchBudgetLostImpressionShare("-");
                    performanceBean.setSearchImpressionsShareLostDueToRank(row.getImpressionLostToRankPercent().getValue());
                    performanceBean.setSearchImpressionsShareLostDueToBudget(row.getImpressionLostToBudgetPercent().getValue());
                    performanceReportBeans.add(performanceBean);

                }
            }
            String dealerId = request.getParameter("dealerMapId");
            if (dealerId != null) {
                Integer totalNoOfCalls = centuryCallService.getTotalNoOfCalls(startDate, endDate, dealerId, fieldsOnly);
                System.out.println("TOTAL CALLS " + totalNoOfCalls);
                AccountPerformanceReportBean performanceBean = new AccountPerformanceReportBean();
                performanceBean.setConversions(totalNoOfCalls + "");
                performanceBean.setOverall("Overall");
                performanceBean.setSource("Calls");
                performanceReportBeans.add(performanceBean);

            }
            returnMap.put("data", sumPerSource(performanceReportBeans, null));

        } catch (InterruptedException ex) {
            Logger.getLogger(PaidTabController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ExecutionException ex) {
            Logger.getLogger(PaidTabController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TimeoutException ex) {
            Logger.getLogger(PaidTabController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return returnMap;
    }

    public static List<AccountPerformanceReportBean> sumPerSource(List<AccountPerformanceReportBean> list, String fieldName) {
        System.out.println("Summary -> " + fieldName);
        Map<String, AccountPerformanceReportBean> map = new HashMap<>();
        Integer count = 0;
        for (AccountPerformanceReportBean p : list) {
            count++;
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

            sum.setSearchImpressionsShare((ApiUtils.toDouble(p.getSearchImpressionsShare()) + ApiUtils.toDouble(sum.getSearchImpressionsShare())) + "");
            sum.setSearchImpressionsShareLostDueToRank((ApiUtils.toDouble(p.getSearchImpressionsShareLostDueToRank()) + ApiUtils.toDouble(sum.getSearchImpressionsShareLostDueToRank())) + "");
            sum.setSearchImpressionsShareLostDueToBudget((ApiUtils.toDouble(p.getSearchImpressionsShareLostDueToBudget()) + ApiUtils.toDouble(sum.getSearchImpressionsShareLostDueToBudget())) + "");

            sum.setAveragePosition((ApiUtils.toDouble(p.getAveragePosition()) + ApiUtils.toDouble(sum.getAveragePosition())) + "");
            sum.setConversions((ApiUtils.toDouble(p.getConversions()) + ApiUtils.toDouble(sum.getConversions())) + "");
            sum.setCtr(ApiUtils.toDouble(sum.getImpressions()) == 0.0 ? "0" : (ApiUtils.toDouble(sum.getClicks()) / ApiUtils.toDouble(sum.getImpressions())) + "");
            sum.setAverageCpc(ApiUtils.toDouble(sum.getClicks()) == 0.0 ? "0" : (ApiUtils.toDouble(sum.getCost()) / ApiUtils.toDouble(sum.getClicks())) + "");
            sum.setCpa(ApiUtils.toDouble(sum.getConversions()) == 0.0 ? "0" : (ApiUtils.toDouble(sum.getCost()) / ApiUtils.toDouble(sum.getConversions())) + "");
        }
        AccountPerformanceReportBean data = map.get("Overall");
        data.setAveragePosition((ApiUtils.toDouble(data.getAveragePosition()) / 2) + "");
        data.setSearchImpressionsShare((ApiUtils.toDouble(data.getSearchImpressionsShare()) / 2) + "");
        data.setSearchImpressionsShareLostDueToRank((ApiUtils.toDouble(data.getSearchImpressionsShareLostDueToRank()) / 2) + "");
        data.setSearchImpressionsShareLostDueToBudget((ApiUtils.toDouble(data.getSearchImpressionsShareLostDueToBudget()) / 2) + "");
        return new ArrayList<AccountPerformanceReportBean>(map.values());
    }

    @RequestMapping(value = "hourOfDayClickImpressions", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    Object getHourOfDayClickImpressions(HttpServletRequest request, HttpServletResponse response) {
        Map returnMap = new HashMap();
        try {
            Date startDate = DateUtils.getStartDate(request.getParameter("startDate"));
            Date endDate = DateUtils.getEndDate(request.getParameter("endDate"));
            String fieldsOnly = request.getParameter("fieldsOnly");
            List<ColumnDef> columnDefs = new ArrayList<>();
            columnDefs.add(new ColumnDef("hourOfDay", "String", "Hour of day"));
            columnDefs.add(new ColumnDef("nthHour", "number", "Nth Hour"));
            columnDefs.add(new ColumnDef("clicks", "number", "Clicks", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
            columnDefs.add(new ColumnDef("impressions", "number", "Impressions", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
            columnDefs.add(new ColumnDef("cost", "number", "Cost", ColumnDef.Aggregation.SUM, ColumnDef.Format.CURRENCY));
            columnDefs.add(new ColumnDef("conversions", "number", "Conversions", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
            columnDefs.add(new ColumnDef("cpc", "number", "CPC", ColumnDef.Aggregation.CPC, ColumnDef.Format.CURRENCY));
            columnDefs.add(new ColumnDef("cpa", "number", "CPL", ColumnDef.Aggregation.CPA, ColumnDef.Format.CURRENCY));
            returnMap.put("columnDefs", columnDefs);
            if (fieldsOnly != null) {
                return returnMap;
            }
            AccountDetails accountDetails = ApiUtils.toAccountDetails(request, "ppc");
            Map<String, ClicksImpressionsHourOfDayBean> dataMap = new HashMap<>();
            if (accountDetails.getAdwordsAccountId() != null) {
                AccountReport adwordsAccountPerformanceReport = adwordsService.getAccountReport(startDate, endDate, accountDetails.getAdwordsAccountId(), "hourOfDay", "SEARCH");
                List<AccountReportRow> adwordsAccountReportRows = adwordsAccountPerformanceReport.getAccountReportRow();
                //returnMap.put("bing", bingAccountPerformanceRows);
                //returnMap.put("adwords", adwordsAccountReportRows);
                for (Iterator<AccountReportRow> iterator = adwordsAccountReportRows.iterator(); iterator.hasNext();) {
                    AccountReportRow accountReportRow = iterator.next();
                    String day = accountReportRow.getHourOfDay();
                    Integer clicks = Integer.parseInt(accountReportRow.getClicks() == null ? "0" : accountReportRow.getClicks());
                    Integer impressions = Integer.parseInt(accountReportRow.getImpressions() == null ? "0" : accountReportRow.getImpressions());
                    Double cost = parseDouble(accountReportRow.getCost() == null ? "0" : accountReportRow.getCost());
                    Double conversions = parseDouble(accountReportRow.getConversions() == null ? "0" : accountReportRow.getConversions());

                    String adwordsStartDayOfWeek = day;
                    ClicksImpressionsHourOfDayBean oldBean = dataMap.get(adwordsStartDayOfWeek);
                    ClicksImpressionsHourOfDayBean bean = new ClicksImpressionsHourOfDayBean();
                    if (oldBean != null) {
                        clicks += oldBean.getClicks();
                        impressions += oldBean.getImpressions();
                        cost += oldBean.getCost();
                        conversions += oldBean.getConversions();
                    }
                    bean.setClicks(clicks);
                    bean.setImpressions(impressions);
                    bean.setCost(cost);
                    bean.setConversions(conversions);
                    bean.setNthHour(ApiUtils.toInteger(adwordsStartDayOfWeek));
                    Integer clockHour = ((ApiUtils.toInteger(adwordsStartDayOfWeek)) % 12); //+ "" +  (((ApiUtils.toInteger(bingStartDayOfWeek)/12) > 0)? " pm" : " am") ;
                    if (clockHour == 0) {
                        clockHour = 12;
                    }
                    bean.setHourOfDay(clockHour + "" + (((ApiUtils.toInteger(adwordsStartDayOfWeek) / 12) > 0) ? " pm" : " am"));
                    dataMap.put(adwordsStartDayOfWeek, bean);
                }
            }
            if (accountDetails.getBingAccountId() != null) {
                AccountPerformanceReport bingAccountPerformanceReport = bingService.getAccountPerformanceReport(startDate, endDate, accountDetails.getBingAccountId(), "hourOfDay");
                List<AccountPerformanceRow> bingAccountPerformanceRows = bingAccountPerformanceReport.getAccountPerformanceRows();
                for (Iterator<AccountPerformanceRow> iterator = bingAccountPerformanceRows.iterator(); iterator.hasNext();) {
                    AccountPerformanceRow accountReportRow = iterator.next();
                    String day = accountReportRow.getHourOfDay().getValue();
                    Integer clicks = Integer.parseInt(accountReportRow.getClicks() == null ? "0" : accountReportRow.getClicks().getValue());
                    Integer impressions = Integer.parseInt(accountReportRow.getImpressions() == null ? "0" : accountReportRow.getImpressions().getValue());
                    Double cost = parseDouble(accountReportRow.getSpend() == null ? "0" : accountReportRow.getSpend().getValue());
                    Double conversions = parseDouble(accountReportRow.getPhoneCalls() == null ? "0" : accountReportRow.getPhoneCalls().getValue());

                    String bingStartDayOfWeek = day;
                    ClicksImpressionsHourOfDayBean oldBean = dataMap.get(bingStartDayOfWeek);
                    ClicksImpressionsHourOfDayBean bean = new ClicksImpressionsHourOfDayBean();
                    if (oldBean != null) {
                        clicks += oldBean.getClicks();
                        impressions += oldBean.getImpressions();
                        cost += oldBean.getCost();
                        conversions += oldBean.getConversions();
                    }
                    bean.setClicks(clicks);
                    bean.setImpressions(impressions);
                    bean.setCost(cost);
                    bean.setConversions(conversions);
                    bean.setNthHour(ApiUtils.toInteger(bingStartDayOfWeek));

                    Integer clockHour = ((ApiUtils.toInteger(bingStartDayOfWeek)) % 12); //+ "" +  (((ApiUtils.toInteger(bingStartDayOfWeek)/12) > 0)? " pm" : " am") ;
                    if (clockHour == 0) {
                        clockHour = 12;
                    }
                    bean.setHourOfDay(clockHour + "" + (((ApiUtils.toInteger(bingStartDayOfWeek) / 12) > 0) ? " pm" : " am"));
                    dataMap.put(bingStartDayOfWeek, bean);
                }
            }
            returnMap.put("data", dataMap.values());

        } catch (InterruptedException ex) {
            Logger.getLogger(PaidTabController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ExecutionException ex) {
            Logger.getLogger(PaidTabController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TimeoutException ex) {
            Logger.getLogger(PaidTabController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return returnMap;
    }

    @RequestMapping(value = "dayOfWeekClickImpressions", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    Object getDayOfWeekClickImpressions(HttpServletRequest request, HttpServletResponse response) {
        Map returnMap = new HashMap();
        try {
            Date startDate = DateUtils.getStartDate(request.getParameter("startDate"));
            Date endDate = DateUtils.getEndDate(request.getParameter("endDate"));
            String fieldsOnly = request.getParameter("fieldsOnly");
            List<ColumnDef> columnDefs = new ArrayList<>();
            columnDefs.add(new ColumnDef("weekId", "number", "Week Id"));
            columnDefs.add(new ColumnDef("weekDay", "String", "Week Day"));
            columnDefs.add(new ColumnDef("clicks", "number", "Clicks", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
            columnDefs.add(new ColumnDef("impressions", "number", "Impressions", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
            columnDefs.add(new ColumnDef("cost", "number", "Cost", ColumnDef.Aggregation.SUM, ColumnDef.Format.CURRENCY));
            columnDefs.add(new ColumnDef("conversions", "number", "Conversions", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
            columnDefs.add(new ColumnDef("cpc", "number", "CPC", ColumnDef.Aggregation.CPC, ColumnDef.Format.CURRENCY));
            columnDefs.add(new ColumnDef("cpa", "number", "CPL", ColumnDef.Aggregation.CPA, ColumnDef.Format.CURRENCY));
            returnMap.put("columnDefs", columnDefs);
            if (fieldsOnly != null) {
                return returnMap;
            }
            AccountDetails accountDetails = ApiUtils.toAccountDetails(request, "ppc");
            Map<String, ClicksImpressionsGraphBean> dataMap = new HashMap<>();
            if (accountDetails.getAdwordsAccountId() != null) {
                List<ClicksImpressionsGraphBean> clicksImpressionsGraphBeans = new ArrayList<>();
                AccountReport adwordsAccountPerformanceReport = adwordsService.getAccountReport(startDate, endDate, accountDetails.getAdwordsAccountId(), "dayOfWeek", "SEARCH");
                List<AccountReportRow> adwordsAccountReportRows = adwordsAccountPerformanceReport.getAccountReportRow();
                //returnMap.put("bing", bingAccountPerformanceRows);
                //returnMap.put("adwords", adwordsAccountReportRows);
                for (Iterator<AccountReportRow> iterator = adwordsAccountReportRows.iterator(); iterator.hasNext();) {
                    AccountReportRow accountReportRow = iterator.next();
                    String day = accountReportRow.getDayOfWeek();
                    Integer clicks = Integer.parseInt(accountReportRow.getClicks() == null ? "0" : accountReportRow.getClicks());
                    Integer impressions = Integer.parseInt(accountReportRow.getImpressions() == null ? "0" : accountReportRow.getImpressions());
                    Double cost = parseDouble(accountReportRow.getCost() == null ? "0" : accountReportRow.getCost());
                    Double conversions = parseDouble(accountReportRow.getAllConv() == null ? "0" : accountReportRow.getAllConv());

                    String adwordsStartDayOfWeek = day;
                    ClicksImpressionsGraphBean oldBean = dataMap.get(adwordsStartDayOfWeek);
                    ClicksImpressionsGraphBean bean = new ClicksImpressionsGraphBean();
                    if (oldBean != null) {
                        clicks += oldBean.getClicks();
                        impressions += oldBean.getImpressions();
                        cost += oldBean.getCost();
                        conversions += oldBean.getConversions();
                    }
                    bean.setClicks(clicks);
                    bean.setImpressions(impressions);
                    bean.setCost(cost);
                    bean.setConversions(conversions);
                    bean.setWeekDay(adwordsStartDayOfWeek);
                    bean.setWeekId(DateUtils.getWeekDayByDay(adwordsStartDayOfWeek) + "");
                    dataMap.put(adwordsStartDayOfWeek, bean);
                }
            }
            if (accountDetails.getBingAccountId() != null) {

                AccountPerformanceReport bingAccountPerformanceReport = bingService.getAccountPerformanceReport(startDate, endDate, accountDetails.getBingAccountId(), "dayOfWeek");
                List<AccountPerformanceRow> bingAccountPerformanceRows = bingAccountPerformanceReport.getAccountPerformanceRows();
                for (Iterator<AccountPerformanceRow> iterator = bingAccountPerformanceRows.iterator(); iterator.hasNext();) {
                    AccountPerformanceRow accountReportRow = iterator.next();
                    String day = accountReportRow.getDayOfWeek().getValue();
                    Integer clicks = Integer.parseInt(accountReportRow.getClicks() == null ? "0" : accountReportRow.getClicks().getValue());
                    Integer impressions = Integer.parseInt(accountReportRow.getImpressions() == null ? "0" : accountReportRow.getImpressions().getValue());
                    Double cost = parseDouble(accountReportRow.getSpend() == null ? "0" : accountReportRow.getSpend().getValue());
                    Double conversions = parseDouble(accountReportRow.getPhoneCalls() == null ? "0" : accountReportRow.getPhoneCalls().getValue());

                    String bingStartDayOfWeek = DateUtils.getDayOfWeek(Integer.parseInt(day));
                    ClicksImpressionsGraphBean oldBean = dataMap.get(bingStartDayOfWeek);
                    ClicksImpressionsGraphBean bean = new ClicksImpressionsGraphBean();
                    if (oldBean != null) {
                        clicks += oldBean.getClicks();
                        impressions += oldBean.getImpressions();
                        cost += oldBean.getCost();
                        conversions += oldBean.getConversions();
                    }
                    bean.setClicks(clicks);
                    bean.setImpressions(impressions);
                    bean.setCost(cost);
                    bean.setConversions(conversions);
                    bean.setWeekDay(bingStartDayOfWeek);
                    bean.setWeekId(day);
                    dataMap.put(bingStartDayOfWeek, bean);
                }
            }
            returnMap.put("data", dataMap.values());

        } catch (InterruptedException ex) {
            Logger.getLogger(PaidTabController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ExecutionException ex) {
            Logger.getLogger(PaidTabController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TimeoutException ex) {
            Logger.getLogger(PaidTabController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return returnMap;
    }

    @RequestMapping(value = "clicksImpressionsGraph", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    Object getClicksImpressionsGraph(HttpServletRequest request, HttpServletResponse response) {
        Map returnMap = new HashMap();
        try {
            Date startDate = DateUtils.get12WeeksBack(request.getParameter("endDate"));
            Date endDate = DateUtils.getEndDate(request.getParameter("endDate"));
            String fieldsOnly = request.getParameter("fieldsOnly");
            List<ColumnDef> columnDefs = new ArrayList<>();
            columnDefs.add(new ColumnDef("weekDay", "date", "Week Day"));
            columnDefs.add(new ColumnDef("clicks", "number", "Clicks", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
            columnDefs.add(new ColumnDef("impressions", "number", "Impressions", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
            columnDefs.add(new ColumnDef("cost", "number", "Cost", ColumnDef.Aggregation.SUM, ColumnDef.Format.CURRENCY));
            columnDefs.add(new ColumnDef("conversions", "number", "Conversions", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
            columnDefs.add(new ColumnDef("cpc", "number", "CPC", ColumnDef.Aggregation.CPC, ColumnDef.Format.CURRENCY));
            columnDefs.add(new ColumnDef("cpa", "number", "CPL", ColumnDef.Aggregation.CPA, ColumnDef.Format.CURRENCY));
            returnMap.put("columnDefs", columnDefs);
            if (fieldsOnly != null) {
                return returnMap;
            }
            AccountDetails accountDetails = ApiUtils.toAccountDetails(request, "ppc");
            Map<String, ClicksImpressionsGraphBean> dataMap = new HashMap<>();
            if (accountDetails.getAdwordsAccountId() != null) {

                List<ClicksImpressionsGraphBean> clicksImpressionsGraphBeans = new ArrayList<>();
                AccountReport adwordsAccountPerformanceReport = adwordsService.getAccountReport(startDate, endDate, accountDetails.getAdwordsAccountId(), "daily", "SEARCH");
                List<AccountReportRow> adwordsAccountReportRows = adwordsAccountPerformanceReport.getAccountReportRow();
                //returnMap.put("bing", bingAccountPerformanceRows);
                //returnMap.put("adwords", adwordsAccountReportRows);
                for (Iterator<AccountReportRow> iterator = adwordsAccountReportRows.iterator(); iterator.hasNext();) {
                    AccountReportRow accountReportRow = iterator.next();
                    String day = accountReportRow.getDay();
                    Integer clicks = Integer.parseInt(accountReportRow.getClicks() == null ? "0" : accountReportRow.getClicks());
                    Integer impressions = Integer.parseInt(accountReportRow.getImpressions() == null ? "0" : accountReportRow.getImpressions());
                    Double cost = parseDouble(accountReportRow.getCost() == null ? "0" : accountReportRow.getCost());
//                Double cpc = parseDouble(accountReportRow.getAvgCPC() == null ? "0" : accountReportRow.getAvgCPC());
//                Double cpa = parseDouble(accountReportRow.getCostConv() == null ? "0" : accountReportRow.getCostConv());
                    Double conversions = parseDouble(accountReportRow.getAllConv() == null ? "0" : accountReportRow.getAllConv());

                    String adwordsStartDayOfWeek = DateUtils.getStartDayOfWeek(DateUtils.toDate(day, "yyyy-MM-dd"), "MM/dd/yyyy");
                    ClicksImpressionsGraphBean oldBean = dataMap.get(adwordsStartDayOfWeek);
                    ClicksImpressionsGraphBean bean = new ClicksImpressionsGraphBean();
                    if (oldBean != null) {
                        clicks += oldBean.getClicks();
                        impressions += oldBean.getImpressions();
                        cost += oldBean.getCost();
                        conversions += oldBean.getConversions();
                    }
                    bean.setClicks(clicks);
                    bean.setImpressions(impressions);
                    bean.setCost(cost);
                    bean.setCpc(cost / clicks);
                    bean.setCpa(cost / conversions);
                    bean.setConversions(conversions);
                    bean.setWeekDay(adwordsStartDayOfWeek);
                    dataMap.put(adwordsStartDayOfWeek, bean);
                }
            }
            if (accountDetails.getBingAccountId() != null) {

                AccountPerformanceReport bingAccountPerformanceReport = bingService.getAccountPerformanceReport(startDate, endDate, accountDetails.getBingAccountId(), "daily");
                List<AccountPerformanceRow> bingAccountPerformanceRows = bingAccountPerformanceReport.getAccountPerformanceRows();

                for (Iterator<AccountPerformanceRow> iterator = bingAccountPerformanceRows.iterator(); iterator.hasNext();) {
                    AccountPerformanceRow accountReportRow = iterator.next();
                    String day = accountReportRow.getGregorianDate().getValue();
                    Integer clicks = Integer.parseInt(accountReportRow.getClicks() == null ? "0" : accountReportRow.getClicks().getValue());
                    Integer impressions = Integer.parseInt(accountReportRow.getImpressions() == null ? "0" : accountReportRow.getImpressions().getValue());
                    Double cost = parseDouble(accountReportRow.getSpend() == null ? "0" : accountReportRow.getSpend().getValue());
                    Double conversions = parseDouble(accountReportRow.getPhoneCalls() == null ? "0" : accountReportRow.getPhoneCalls().getValue());
                    Double cpc = parseDouble(accountReportRow.getAverageCpc() == null ? "0" : accountReportRow.getAverageCpc().getValue());
                    Double cpa = parseDouble(accountReportRow.getCostPerConversion() == null ? "0" : accountReportRow.getCostPerConversion().getValue());

                    String bingStartDayOfWeek = DateUtils.getStartDayOfWeek(DateUtils.toDate(day, "MM/dd/yyyy"), "MM/dd/yyyy");
                    ClicksImpressionsGraphBean oldBean = dataMap.get(bingStartDayOfWeek);
                    ClicksImpressionsGraphBean bean = new ClicksImpressionsGraphBean();
                    if (oldBean != null) {
                        clicks += oldBean.getClicks();
                        impressions += oldBean.getImpressions();
                        cost += oldBean.getCost();
                        conversions += oldBean.getConversions();
                        cpc += oldBean.getCpc();
                        cpa += oldBean.getCpa();
                    }
                    bean.setClicks(clicks);
                    bean.setImpressions(impressions);
                    bean.setCost(cost);
                    bean.setConversions(conversions);
                    bean.setCpc(cost / clicks);
                    bean.setCpa(cost / conversions);
                    bean.setWeekDay(bingStartDayOfWeek);

                    dataMap.put(bingStartDayOfWeek, bean);
                }
            }
            returnMap.put("data", dataMap.values());

        } catch (InterruptedException ex) {
            Logger.getLogger(PaidTabController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ExecutionException ex) {
            Logger.getLogger(PaidTabController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TimeoutException ex) {
            Logger.getLogger(PaidTabController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return returnMap;
    }

    @RequestMapping(value = "campaignPerformance", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    Object getCampaignPerformance(HttpServletRequest request, HttpServletResponse response) {
        Date startDate = DateUtils.getStartDate(request.getParameter("startDate"));
        Date endDate = DateUtils.getEndDate(request.getParameter("endDate"));
        String fieldsOnly = request.getParameter("fieldsOnly");
        Map returnMap = new HashMap();
        List<ColumnDef> columnDefs = new ArrayList<>();
        columnDefs.add(new ColumnDef("campaignName", "string", "Campaign Name"));
        columnDefs.add(new ColumnDef("impressions", "string", "Impressions", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
        columnDefs.add(new ColumnDef("clicks", "string", "Clicks", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
        columnDefs.add(new ColumnDef("ctr", "string", "CTR", ColumnDef.Aggregation.CTR, ColumnDef.Format.PERCENTAGE));
        columnDefs.add(new ColumnDef("averagePosition", "string", "Average Position", ColumnDef.Aggregation.AVG, ColumnDef.Format.DECIMAL1));
        columnDefs.add(new ColumnDef("cost", "string", "Cost", ColumnDef.Aggregation.SUM, ColumnDef.Format.CURRENCY));
        columnDefs.add(new ColumnDef("averageCpc", "string", "Average CPC", ColumnDef.Aggregation.CPC, ColumnDef.Format.CURRENCY));
        columnDefs.add(new ColumnDef("conversions", "number", "Conversions", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
        columnDefs.add(new ColumnDef("cpa", "string", "CPL", ColumnDef.Aggregation.CPA, ColumnDef.Format.CURRENCY));
        columnDefs.add(new ColumnDef("impressionShare", "string", "Impression Share", ColumnDef.Aggregation.AVG, ColumnDef.Format.PERCENTAGE));
        columnDefs.add(new ColumnDef("searchImpressionsShareLostByBudget", "string", "Search Impression Share Lost By Budget", ColumnDef.Aggregation.AVG, ColumnDef.Format.PERCENTAGE));
        columnDefs.add(new ColumnDef("searchImpressionsShareLostByRank", "string", "Search Impression Share Lost By Rank", ColumnDef.Aggregation.AVG, ColumnDef.Format.PERCENTAGE));
        columnDefs.add(new ColumnDef("source", "string", "Source"));
        returnMap.put("columnDefs", columnDefs);
        if (fieldsOnly != null) {
            return returnMap;
        }
        List<CampaignPerformanceReportBean> performanceReportBeans = new ArrayList<>();
        AccountDetails accountDetails = ApiUtils.toAccountDetails(request, "ppc");
        if (accountDetails.getAdwordsAccountId() != null) {
            String aggregation = "";
            com.visumbu.api.adwords.report.xml.bean.CampaignPerformanceReport adWordsCampaignPerformanceReport = adwordsService.getCampaignPerformanceReport(startDate, endDate, accountDetails.getAdwordsAccountId(), aggregation, "SEARCH");
            List<CampaignPerformanceReportRow> adwordsCampaignPerformanceReportRow = adWordsCampaignPerformanceReport.getCampaignPerformanceReportRow();

            for (Iterator<CampaignPerformanceReportRow> reportRow = adwordsCampaignPerformanceReportRow.iterator(); reportRow.hasNext();) {
                CampaignPerformanceReportRow row = reportRow.next();
                CampaignPerformanceReportBean performanceBean = new CampaignPerformanceReportBean();
                performanceBean.setSource("Google");
                performanceBean.setImpressions(row.getImpressions());
                performanceBean.setCampaignName(row.getCampaign());
                performanceBean.setClicks(row.getClicks());
                performanceBean.setCtr(row.getCtr());
                performanceBean.setAveragePosition(row.getAvgPosition());
                performanceBean.setCost(row.getCost());
                performanceBean.setAverageCpc(row.getAvgCPC());
                performanceBean.setConversions(row.getAllConv());
                performanceBean.setCpa(row.getCostConv());
                //performanceBean.setPhoneCalls(row.getPhoneCalls());
                performanceBean.setSearchImpressionsShare(row.getSearchExactMatchIS());
                performanceBean.setSearchImpressionsShareLostByBudget(row.getSearchLostISBudget());
                performanceBean.setSearchImpressionsShareLostByRank(row.getSearchLostISRank());
                performanceReportBeans.add(performanceBean);
            }
        }
        if (accountDetails.getBingAccountId() != null) {

            CampaignPerformanceReport bingCampaignPerformanceReport = bingService.getCampaignPerformanceReport(startDate, endDate, accountDetails.getBingAccountId(), "");
            List<CampaignPerformanceRow> bingCampaignPerformanceRows = bingCampaignPerformanceReport.getCampaignPerformanceRows();

            for (Iterator<CampaignPerformanceRow> reportRow = bingCampaignPerformanceRows.iterator(); reportRow.hasNext();) {
                CampaignPerformanceRow row = reportRow.next();
                CampaignPerformanceReportBean performanceBean = new CampaignPerformanceReportBean();
                performanceBean.setSource("Bing");
                performanceBean.setImpressions(row.getImpressions().getValue());
                performanceBean.setCampaignName(row.getCampaignName().getValue());
                performanceBean.setClicks(row.getClicks().getValue());
                performanceBean.setCtr(row.getCtr().getValue());
                performanceBean.setAveragePosition(row.getAveragePosition().getValue());
                performanceBean.setCost(row.getSpend().getValue());
                performanceBean.setAverageCpc(row.getClicks().getValue());
                performanceBean.setCtr(row.getCtr().getValue());
                performanceBean.setAverageCpc(row.getAverageCpc().getValue());
                performanceBean.setConversions(row.getPhoneCalls().getValue());
                performanceBean.setCpa(row.getCostPerConversion().getValue());
                performanceBean.setSearchImpressionsShare(row.getImpressionSharePercent().getValue());
                performanceBean.setSearchImpressionsShareLostByBudget(row.getImpressionLostToBudgetPercent().getValue());
                performanceBean.setSearchImpressionsShareLostByRank("--");
                performanceReportBeans.add(performanceBean);

            }
        }
        returnMap.put("data", performanceReportBeans);

        return returnMap;
    }

    @RequestMapping(value = "geoPerformance", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    Object getGeoPerformance(HttpServletRequest request, HttpServletResponse response) {
        Date startDate = DateUtils.getStartDate(request.getParameter("startDate"));
        Date endDate = DateUtils.getEndDate(request.getParameter("endDate"));
        String fieldsOnly = request.getParameter("fieldsOnly");
        Map returnMap = new HashMap();
        List<ColumnDef> columnDefs = new ArrayList<>();
        columnDefs.add(new ColumnDef("source", "string", "Source"));
        columnDefs.add(new ColumnDef("country", "string", "Country"));
        columnDefs.add(new ColumnDef("state", "string", "State"));
        columnDefs.add(new ColumnDef("city", "string", "City"));
        columnDefs.add(new ColumnDef("zip", "string", "Zip"));
        columnDefs.add(new ColumnDef("cost", "number", "Cost", ColumnDef.Aggregation.SUM, ColumnDef.Format.CURRENCY));
        columnDefs.add(new ColumnDef("impressions", "string", "Impressions", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
        columnDefs.add(new ColumnDef("clicks", "string", "Clicks", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
        columnDefs.add(new ColumnDef("ctr", "string", "CTR", ColumnDef.Aggregation.CTR, ColumnDef.Format.PERCENTAGE));
        columnDefs.add(new ColumnDef("averageCpc", "string", "CPC", ColumnDef.Aggregation.CPC, ColumnDef.Format.CURRENCY));
        columnDefs.add(new ColumnDef("conversions", "number", "Conversions", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
        columnDefs.add(new ColumnDef("cpa", "string", "CPL", ColumnDef.Aggregation.CPA, ColumnDef.Format.CURRENCY));
        columnDefs.add(new ColumnDef("searchImpressionsShare", "string", "Search Impression Share", ColumnDef.Aggregation.AVG, ColumnDef.Format.PERCENTAGE));
        columnDefs.add(new ColumnDef("averagePosition", "string", "Average Position", ColumnDef.Aggregation.AVG, ColumnDef.Format.DECIMAL1));
        returnMap.put("columnDefs", columnDefs);
        if (fieldsOnly != null) {
            return returnMap;
        }
        AccountDetails accountDetails = ApiUtils.toAccountDetails(request, "ppc");
        List<GeoPerformanceReportBean> performanceReportBeans = new ArrayList<>();
        if (accountDetails.getAdwordsAccountId() != null) {

            GeoReport adWordsGeoReport = adwordsService.getGeoReport(startDate, endDate, accountDetails.getAdwordsAccountId(), "", "SEARCH");
            List<GeoReportRow> adWordsGeoPerformanceRow = adWordsGeoReport.getGeoReportRow();

            for (Iterator<GeoReportRow> reportRow = adWordsGeoPerformanceRow.iterator(); reportRow.hasNext();) {
                GeoReportRow row = reportRow.next();
                GeoPerformanceReportBean performanceBean = new GeoPerformanceReportBean();
                performanceBean.setSource("Google");
                performanceBean.setCountry(row.getCountryCriteriaId());
                performanceBean.setState("--");
                String city = ApiUtils.getCityById(row.getCityCriteriaId());
                performanceBean.setCity(city);
                performanceBean.setZip("--");
                performanceBean.setImpressions(row.getImpressions());
                performanceBean.setClicks(row.getClicks());
                performanceBean.setCost(row.getCost());
                performanceBean.setCtr(row.getCtr());
                performanceBean.setAverageCpc(row.getAvgCPC());
                performanceBean.setConversions(row.getAllConv());
                performanceBean.setCpa(row.getCostConv());
                performanceBean.setSearchImpressionsShare(row.getSearchBudgetLostImpressionShare());
                performanceBean.setAveragePosition(row.getAvgPosition());
                performanceReportBeans.add(performanceBean);
            }
        }
        if (accountDetails.getBingAccountId() != null) {

            GeoCityLocationPerformanceReport bingGeoReport = bingService.getGeoCityLocationPerformanceReport(startDate, endDate, accountDetails.getBingAccountId(), "");
            List<GeoCityLocationPerformanceRow> bingGeoPerformanceRows = bingGeoReport.getGeoCityLocationPerformanceRows();

            for (Iterator<GeoCityLocationPerformanceRow> reportRow = bingGeoPerformanceRows.iterator(); reportRow.hasNext();) {
                GeoCityLocationPerformanceRow row = reportRow.next();
                GeoPerformanceReportBean performanceBean = new GeoPerformanceReportBean();
                performanceBean.setSource("Bing");
                performanceBean.setCountry(row.getCountry() == null ? "-" : row.getCountry().getValue());
                performanceBean.setState(row.getState().getValue());
                String city = row.getCity().getValue();
                if (city == null || city.isEmpty() || city.equalsIgnoreCase("-")) {
                    city = "Unknown";
                }
                performanceBean.setCity(city);
                performanceBean.setZip("--");
                performanceBean.setCost(row.getSpend().getValue());
                performanceBean.setImpressions(row.getImpressions().getValue());
                performanceBean.setClicks(row.getClicks().getValue());
                performanceBean.setCtr(row.getCtr().getValue());
                performanceBean.setAverageCpc(row.getAverageCpc().getValue());
                performanceBean.setConversions(row.getConversions().getValue());
                performanceBean.setCpa(row.getCostPerConversion().getValue());
                performanceBean.setSearchImpressionsShare("--");
                performanceBean.setAveragePosition(row.getAveragePosition().getValue());
                performanceReportBeans.add(performanceBean);
            }
        }
        returnMap.put("data", performanceReportBeans);
        return returnMap;
    }

    @RequestMapping(value = "deviceConversion", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    Object getDeviceConversion(HttpServletRequest request, HttpServletResponse response) {
        Date startDate = DateUtils.getStartDate(request.getParameter("startDate"));
        Date endDate = DateUtils.getEndDate(request.getParameter("endDate"));
        String fieldsOnly = request.getParameter("fieldsOnly");
        Map returnMap = new HashMap();
        List<ColumnDef> columnDefs = new ArrayList<>();
        columnDefs.add(new ColumnDef("source", "string", "Source"));
        columnDefs.add(new ColumnDef("device", "string", "Device"));
        columnDefs.add(new ColumnDef("conversions", "number", "Conversions", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
        returnMap.put("columnDefs", columnDefs);
        if (fieldsOnly != null) {
            return returnMap;
        }
        AccountDetails accountDetails = ApiUtils.toAccountDetails(request, "ppc");
        List<DevicePerformanceReportBean> performanceReportBeans = new ArrayList<>();
        if (accountDetails.getAdwordsAccountId() != null) {

            AccountDeviceReport adwordsAccountDeviceReport = adwordsService.getAccountDevicePerformanceReport(startDate, endDate, accountDetails.getAdwordsAccountId(), "", "SEARCH");
            List<AccountDeviceReportRow> adwordsAccountDeviceReportRow = adwordsAccountDeviceReport.getAccountDeviceReportRow();

            for (Iterator<AccountDeviceReportRow> reportRow = adwordsAccountDeviceReportRow.iterator(); reportRow.hasNext();) {
                AccountDeviceReportRow row = reportRow.next();
                DevicePerformanceReportBean performanceBean = new DevicePerformanceReportBean();
                performanceBean.setSource("Google");
                if (row.getDevice().contains("Tablet")) {
                    performanceBean.setDevice("Tablet");
                }
                if (row.getDevice().contains("Mobile")) {
                    performanceBean.setDevice("Smartphone");
                }
                if (row.getDevice().contains("Computer")) {
                    performanceBean.setDevice("Computer");
                }
                performanceBean.setConversions(row.getAllConv());
                performanceReportBeans.add(performanceBean);
            }
        }
        if (accountDetails.getBingAccountId() != null) {
            AccountDevicePerformanceReport bingAccountDevicePerformanceReport = bingService.getAccountDevicePerformanceReport(startDate, endDate, accountDetails.getBingAccountId(), "");
            List<AccountDevicePerformanceRow> bingAccountDevicePerformanceRows = bingAccountDevicePerformanceReport.getAccountDevicePerformanceRows();
            for (Iterator<AccountDevicePerformanceRow> reportRow = bingAccountDevicePerformanceRows.iterator(); reportRow.hasNext();) {
                AccountDevicePerformanceRow row = reportRow.next();
                DevicePerformanceReportBean performanceBean = new DevicePerformanceReportBean();
                performanceBean.setSource("Bing");
                performanceBean.setDevice(row.getDeviceType().getValue());
                performanceBean.setConversions(row.getPhoneCalls().getValue());
                performanceReportBeans.add(performanceBean);
            }
        }
        returnMap.put("data", performanceReportBeans);
        return returnMap;
    }

    @RequestMapping(value = "campaignDevice", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    Object getCampaignDevice(HttpServletRequest request, HttpServletResponse response) {
        Date startDate = DateUtils.getStartDate(request.getParameter("startDate"));
        Date endDate = DateUtils.getEndDate(request.getParameter("endDate"));
        String fieldsOnly = request.getParameter("fieldsOnly");
        Map returnMap = new HashMap();
        List<ColumnDef> columnDefs = new ArrayList<>();
        columnDefs.add(new ColumnDef("source", "string", "Source", 1));
        columnDefs.add(new ColumnDef("campaignName", "string", "Campaign Name"));
        columnDefs.add(new ColumnDef("device", "string", "Device"));
        columnDefs.add(new ColumnDef("impressions", "number", "Impressions", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
        columnDefs.add(new ColumnDef("clicks", "number", "Clicks", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
        columnDefs.add(new ColumnDef("ctr", "number", "CTR", ColumnDef.Aggregation.CTR, ColumnDef.Format.PERCENTAGE));
        columnDefs.add(new ColumnDef("cost", "number", "Cost", ColumnDef.Aggregation.SUM, ColumnDef.Format.CURRENCY));
        columnDefs.add(new ColumnDef("averageCpc", "number", "Average CPC", ColumnDef.Aggregation.CPC, ColumnDef.Format.CURRENCY));
        columnDefs.add(new ColumnDef("averagePosition", "number", "Average Position", ColumnDef.Aggregation.AVG, ColumnDef.Format.DECIMAL1));
        columnDefs.add(new ColumnDef("conversions", "number", "Conversions", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
        columnDefs.add(new ColumnDef("cpa", "number", "CPL", ColumnDef.Aggregation.CPA, ColumnDef.Format.CURRENCY));
        columnDefs.add(new ColumnDef("searchImpressionsShare", "number", "Search Impression Share", ColumnDef.Aggregation.AVG, ColumnDef.Format.PERCENTAGE));
        returnMap.put("columnDefs", columnDefs);
        if (fieldsOnly != null) {
            return returnMap;
        }
        AccountDetails accountDetails = ApiUtils.toAccountDetails(request, "ppc");

        List<CampaignDevicePerformanceReportBean> performanceReportBeans = new ArrayList<>();
        if (accountDetails.getAdwordsAccountId() != null) {
            CampaignDeviceReport adwordsCampaignDeviceReport = adwordsService.getCampaignDeviceReport(startDate, endDate, accountDetails.getAdwordsAccountId(), "", "SEARCH");
            List<CampaignDeviceReportRow> adwordsCampaignDeviceReportRow = adwordsCampaignDeviceReport.getCampaignDeviceReportRow();

            for (Iterator<CampaignDeviceReportRow> reportRow = adwordsCampaignDeviceReportRow.iterator(); reportRow.hasNext();) {
                CampaignDeviceReportRow row = reportRow.next();
                CampaignDevicePerformanceReportBean performanceBean = new CampaignDevicePerformanceReportBean();
                performanceBean.setSource("Google");
                if (row.getDevice().contains("Tablet")) {
                    performanceBean.setDevice("Tablet");
                } else if (row.getDevice().contains("Mobile")) {
                    performanceBean.setDevice("Smartphone");
                } else if (row.getDevice().contains("Computer")) {
                    performanceBean.setDevice("Computer");
                } else {
                    performanceBean.setDevice(row.getDevice());
                }
                performanceBean.setCampaignName(row.getCampaign());
                performanceBean.setImpressions(row.getImpressions());
                performanceBean.setClicks(row.getClicks());
                performanceBean.setCtr(row.getCtr());

                performanceBean.setCost(row.getCost());
                performanceBean.setAverageCpc(row.getAvgCPC());
                performanceBean.setCpa(row.getCostConv());

                performanceBean.setAveragePosition(row.getAvgPosition());
                performanceBean.setConversions(row.getAllConv());
                performanceBean.setSearchImpressionsShare(row.getSearchImprShare());
                performanceReportBeans.add(performanceBean);
            }
        }
        if (accountDetails.getBingAccountId() != null) {
            CampaignDevicePerformanceReport bingCampaignDevicePerformanceReport = bingService.getCampaignDevicePerformanceReport(startDate, endDate, accountDetails.getBingAccountId(), "");
            List<CampaignDevicePerformanceRow> bingCampaignDevicePerformanceRows = bingCampaignDevicePerformanceReport.getCampaignDevicePerformanceRows();
            for (Iterator<CampaignDevicePerformanceRow> reportRow = bingCampaignDevicePerformanceRows.iterator(); reportRow.hasNext();) {
                CampaignDevicePerformanceRow row = reportRow.next();
                CampaignDevicePerformanceReportBean performanceBean = new CampaignDevicePerformanceReportBean();
                performanceBean.setSource("Bing");

                performanceBean.setCampaignName(row.getCampaignName().getValue());
                if (row.getDeviceType().getValue().contains("Tablet")) {
                    performanceBean.setDevice("Tablet");
                } else if (row.getDeviceType().getValue().contains("Smartphone")) {
                    performanceBean.setDevice("Smartphone");
                } else if (row.getDeviceType().getValue().contains("Computer")) {
                    performanceBean.setDevice("Computer");
                } else {
                    performanceBean.setDevice(row.getDeviceType().getValue());
                }
                performanceBean.setImpressions(row.getImpressions().getValue());
                performanceBean.setClicks(row.getClicks().getValue());
                performanceBean.setCtr(row.getCtr().getValue());
                performanceBean.setCost(row.getSpend().getValue());
                performanceBean.setAverageCpc(row.getAverageCpc().getValue());
                performanceBean.setAveragePosition(row.getAveragePosition().getValue());
                performanceBean.setConversions(row.getPhoneCalls().getValue());
                performanceBean.setCpa(row.getCostPerConversion().getValue());
                performanceBean.setSearchImpressionsShare(row.getImpressionSharePercent().getValue());
                performanceReportBeans.add(performanceBean);

            }
        }
        returnMap.put("data", performanceReportBeans);
        return returnMap;
    }

    @RequestMapping(value = "accountDevice", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    Object getAccountDevice(HttpServletRequest request, HttpServletResponse response) {
        Date startDate = DateUtils.getStartDate(request.getParameter("startDate"));
        Date endDate = DateUtils.getEndDate(request.getParameter("endDate"));

        String fieldsOnly = request.getParameter("fieldsOnly");
        Map returnMap = new HashMap();
        List<ColumnDef> columnDefs = new ArrayList<>();
        columnDefs.add(new ColumnDef("source", "string", "Source", 1));
        columnDefs.add(new ColumnDef("device", "string", "Device Type"));
        columnDefs.add(new ColumnDef("impressions", "number", "Impressions", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
        columnDefs.add(new ColumnDef("clicks", "number", "Clicks", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
        columnDefs.add(new ColumnDef("ctr", "number", "CTR", ColumnDef.Aggregation.CTR, ColumnDef.Format.PERCENTAGE));
        columnDefs.add(new ColumnDef("cost", "number", "Cost", ColumnDef.Aggregation.SUM, ColumnDef.Format.CURRENCY));
        columnDefs.add(new ColumnDef("averageCpc", "number", "Average CPC", ColumnDef.Aggregation.CPC, ColumnDef.Format.CURRENCY));
        columnDefs.add(new ColumnDef("averagePosition", "number", "Average Position", ColumnDef.Aggregation.AVG, ColumnDef.Format.DECIMAL1));
        columnDefs.add(new ColumnDef("conversions", "number", "Conversions", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
        columnDefs.add(new ColumnDef("cpa", "number", "CPL", ColumnDef.Aggregation.CPA, ColumnDef.Format.CURRENCY));
        columnDefs.add(new ColumnDef("searchImpressionsShare", "number", "Search Impression Share", ColumnDef.Aggregation.AVG, ColumnDef.Format.PERCENTAGE));
        returnMap.put("columnDefs", columnDefs);
        if (fieldsOnly != null) {
            return returnMap;
        }
        AccountDetails accountDetails = ApiUtils.toAccountDetails(request, "ppc");
        List<DevicePerformanceReportBean> performanceReportBeans = new ArrayList<>();
        if (accountDetails.getAdwordsAccountId() != null) {
            AccountDeviceReport adwordsAccountDeviceReport = adwordsService.getAccountDevicePerformanceReport(startDate, endDate, accountDetails.getAdwordsAccountId(), "", "SEARCH");
            List<AccountDeviceReportRow> adwordsAccountDeviceReportRow = adwordsAccountDeviceReport.getAccountDeviceReportRow();
            for (Iterator<AccountDeviceReportRow> reportRow = adwordsAccountDeviceReportRow.iterator(); reportRow.hasNext();) {
                AccountDeviceReportRow row = reportRow.next();
                DevicePerformanceReportBean performanceBean = new DevicePerformanceReportBean();
                performanceBean.setSource("Google");
                if (row.getDevice().contains("Tablet")) {
                    performanceBean.setDevice("Tablet");
                } else if (row.getDevice().contains("Mobile")) {
                    performanceBean.setDevice("Smartphone");
                } else if (row.getDevice().contains("Computer")) {
                    performanceBean.setDevice("Computer");
                } else {
                    performanceBean.setDevice(row.getDevice());
                }
//            performanceBean.setDevice(row.getDevice());
                performanceBean.setImpressions(row.getImpressions());
                performanceBean.setClicks(row.getClicks());
                performanceBean.setCtr(row.getCtr());
                String cost = row.getCost(); //Integer.toString(Integer.parseInt(row.getCost()) / 1000000);
                performanceBean.setCost(cost);
                String cpc = row.getAvgCPC(); //Integer.toString(Integer.parseInt(row.getAvgCPC()) / 1000000);

                performanceBean.setAverageCpc(cpc);
                String cpa = row.getCostConv(); //Integer.toString(Integer.parseInt(row.getCostConv()) / 1000000);

                performanceBean.setCpa(cpa);

                performanceBean.setAveragePosition(row.getAvgPosition());
                performanceBean.setConversions(row.getAllConv());
                performanceBean.setSearchImpressionsShare(row.getSearchImprShare());
                performanceReportBeans.add(performanceBean);
            }
        }
        if (accountDetails.getBingAccountId() != null) {
            AccountDevicePerformanceReport bingAccountDevicePerformanceReport = bingService.getAccountDevicePerformanceReport(startDate, endDate, accountDetails.getBingAccountId(), "");
            List<AccountDevicePerformanceRow> bingAccountDevicePerformanceRows = bingAccountDevicePerformanceReport.getAccountDevicePerformanceRows();
            for (Iterator<AccountDevicePerformanceRow> reportRow = bingAccountDevicePerformanceRows.iterator(); reportRow.hasNext();) {
                AccountDevicePerformanceRow row = reportRow.next();
                DevicePerformanceReportBean performanceBean = new DevicePerformanceReportBean();
                performanceBean.setSource("Bing");
                //performanceBean.setDevice(row.getDeviceType().getValue());
                if (row.getDeviceType().getValue().contains("Tablet")) {
                    performanceBean.setDevice("Tablet");
                } else if (row.getDeviceType().getValue().contains("Smartphone")) {
                    performanceBean.setDevice("Smartphone");
                } else if (row.getDeviceType().getValue().contains("Computer")) {
                    performanceBean.setDevice("Computer");
                } else {
                    performanceBean.setDevice(row.getDeviceType().getValue());
                }
                performanceBean.setImpressions(row.getImpressions().getValue());
                performanceBean.setClicks(row.getClicks().getValue());
                performanceBean.setCtr(row.getCtr().getValue());
                performanceBean.setCost(row.getSpend().getValue());
                performanceBean.setAverageCpc(row.getAverageCpc().getValue());
                performanceBean.setAveragePosition(row.getAveragePosition().getValue());
                performanceBean.setConversions(row.getPhoneCalls().getValue());
                performanceBean.setCpa(row.getCostPerConversion().getValue());
                performanceBean.setSearchImpressionsShare(row.getImpressionSharePercent().getValue());
                performanceReportBeans.add(performanceBean);
            }
        }
        returnMap.put("data", sumPerDevice(performanceReportBeans));
        return returnMap;
    }

    public static List<DevicePerformanceReportBean> sumPerDevice(List<DevicePerformanceReportBean> list) {

        Map<String, DevicePerformanceReportBean> map = new HashMap<>();

        for (DevicePerformanceReportBean p : list) {
            String name = p.getDevice();
            DevicePerformanceReportBean sum = map.get(name);
            if (sum == null) {
                sum = new DevicePerformanceReportBean();
                map.put(name, sum);
            }
            sum.setDevice(name);
            sum.setImpressions((ApiUtils.toInteger(p.getImpressions()) + ApiUtils.toInteger(sum.getImpressions())) + "");
            sum.setClicks((ApiUtils.toInteger(p.getClicks()) + ApiUtils.toInteger(sum.getClicks())) + "");
            sum.setCost((ApiUtils.toDouble(p.getCost()) + ApiUtils.toDouble(sum.getCost())) + "");

            sum.setAveragePosition((ApiUtils.toDouble(p.getAveragePosition()) + ApiUtils.toDouble(sum.getAveragePosition())) / 2 + "");
            sum.setConversions((ApiUtils.toDouble(p.getConversions()) + ApiUtils.toDouble(sum.getConversions())) + "");

            sum.setSearchImpressionsShare((ApiUtils.toDouble(p.getSearchImpressionsShare()) + ApiUtils.toDouble(sum.getSearchImpressionsShare())) / 2 + "");

            sum.setCtr(ApiUtils.toDouble(sum.getImpressions()) == 0.0 ? "0" : (ApiUtils.toDouble(sum.getClicks()) / ApiUtils.toDouble(sum.getImpressions())) + "");
            sum.setAverageCpc(ApiUtils.toDouble(sum.getClicks()) == 0.0 ? "0" : (ApiUtils.toDouble(sum.getCost()) / ApiUtils.toDouble(sum.getClicks())) + "");
            sum.setCpa(ApiUtils.toDouble(sum.getConversions()) == 0.0 ? "0" : (ApiUtils.toDouble(sum.getCost()) / ApiUtils.toDouble(sum.getConversions())) + "");

        }
        return new ArrayList<DevicePerformanceReportBean>(map.values());
    }

    @RequestMapping(value = "callConversionsTest", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    Object callConversionsTest(HttpServletRequest request, HttpServletResponse response) {
        Date startDate = DateUtils.getStartDate(request.getParameter("startDate"));
        Date endDate = DateUtils.getEndDate(request.getParameter("endDate"));
        AccountDetails accountDetails = ApiUtils.toAccountDetails(request, "ppc");

        return adwordsService.getAccountCallConversionsReport(startDate, endDate, accountDetails.getAdwordsAccountId(), "", "SEARCH");
    }

    @RequestMapping(value = "adGroups", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    Object getAdGroups(HttpServletRequest request, HttpServletResponse response) {
        Date startDate = DateUtils.getStartDate(request.getParameter("startDate"));
        Date endDate = DateUtils.getEndDate(request.getParameter("endDate"));

        String fieldsOnly = request.getParameter("fieldsOnly");
        Map returnMap = new HashMap();
        List<ColumnDef> columnDefs = new ArrayList<>();
        columnDefs.add(new ColumnDef("source", "string", "Source", 1));
        columnDefs.add(new ColumnDef("campaignName", "string", "Campaign Name"));
        columnDefs.add(new ColumnDef("adGroupName", "string", "Ad Group Name"));
        columnDefs.add(new ColumnDef("impressions", "number", "Impressions", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
        columnDefs.add(new ColumnDef("clicks", "string", "Clicks", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
        columnDefs.add(new ColumnDef("ctr", "number", "CTR", ColumnDef.Aggregation.CTR, ColumnDef.Format.PERCENTAGE));
        columnDefs.add(new ColumnDef("cost", "number", "Cost", ColumnDef.Aggregation.SUM, ColumnDef.Format.CURRENCY));
        columnDefs.add(new ColumnDef("averageCpc", "number", "Average CPC", ColumnDef.Aggregation.CPC, ColumnDef.Format.CURRENCY));
        columnDefs.add(new ColumnDef("averagePosition", "number", "Average Position", ColumnDef.Aggregation.AVG, ColumnDef.Format.DECIMAL1));
        columnDefs.add(new ColumnDef("conversions", "number", "Conversions", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
        columnDefs.add(new ColumnDef("cpa", "number", "CPL", ColumnDef.Aggregation.CPA, ColumnDef.Format.CURRENCY));
        columnDefs.add(new ColumnDef("searchImpressionsShare", "number", "Search Impression Share", ColumnDef.Aggregation.AVG, ColumnDef.Format.PERCENTAGE));
        returnMap.put("columnDefs", columnDefs);
        if (fieldsOnly != null) {
            return returnMap;
        }
        AccountDetails accountDetails = ApiUtils.toAccountDetails(request, "ppc");

        List<AdGroupPerformanceReportBean> performanceReportBeans = new ArrayList<>();
        if (accountDetails.getAdwordsAccountId() != null) {

            AddGroupReport adwordsAdGroupReport = adwordsService.getAdGroupReport(startDate, endDate, accountDetails.getAdwordsAccountId(), "", "SEARCH");

            List<AdGroupReportRow> adwordsAdGroupReportRow = adwordsAdGroupReport.getAdGroupReportRow();

            for (Iterator<AdGroupReportRow> reportRow = adwordsAdGroupReportRow.iterator(); reportRow.hasNext();) {
                AdGroupReportRow row = reportRow.next();
                AdGroupPerformanceReportBean performanceBean = new AdGroupPerformanceReportBean();
                performanceBean.setSource("Google");
                performanceBean.setCampaignName(row.getCampaign());
                performanceBean.setAdGroupName(row.getAdGroupName());
                performanceBean.setImpressions(row.getImpressions());
                performanceBean.setClicks(row.getClicks());
                performanceBean.setCtr(row.getCtr());

                performanceBean.setCost(row.getCost());
                performanceBean.setAverageCpc(row.getAvgCPC());
                performanceBean.setCpa(row.getCostConv());

                performanceBean.setAveragePosition(row.getAvgPosition());
                performanceBean.setConversions(row.getPhoneCalls());
                performanceBean.setSearchImpressionsShare(row.getSearchImprShare());
                performanceReportBeans.add(performanceBean);
            }
        }
        if (accountDetails.getBingAccountId() != null) {
            AdGroupPerformanceReport bingAdGroupPerformanceReport = bingService.getAdGroupPerformanceReport(startDate, endDate, accountDetails.getBingAccountId(), "");
            List<AdGroupPerformanceRow> bingAdGroupPerformanceRows = bingAdGroupPerformanceReport.getAdGroupPerformanceRows();

            for (Iterator<AdGroupPerformanceRow> reportRow = bingAdGroupPerformanceRows.iterator(); reportRow.hasNext();) {
                AdGroupPerformanceRow row = reportRow.next();
                System.out.println("CAmpaign Name " + row.getCampaignName().getValue());
                if (row.getCampaignName().getValue().toLowerCase().indexOf("model") < 0) {
                    System.out.println("Skip");
                    continue;
                }
                AdGroupPerformanceReportBean performanceBean = new AdGroupPerformanceReportBean();
                performanceBean.setSource("Bing");
                performanceBean.setCampaignName(row.getCampaignName().getValue());
                performanceBean.setAdGroupName(row.getAdGroupName().getValue());
                performanceBean.setImpressions(row.getImpressions().getValue());
                performanceBean.setClicks(row.getClicks().getValue());
                performanceBean.setCtr(row.getCtr().getValue());
                performanceBean.setCost(row.getSpend().getValue());
                performanceBean.setAverageCpc(row.getAverageCpc().getValue());
                performanceBean.setAveragePosition(row.getAveragePosition().getValue());
                performanceBean.setConversions(row.getPhoneCalls().getValue());
                performanceBean.setCpa(row.getCostPerConversion().getValue());
                performanceBean.setSearchImpressionsShare(row.getImpressionSharePercent().getValue());
                performanceReportBeans.add(performanceBean);

            }
        }
        returnMap.put("data", performanceReportBeans);
        return returnMap;
    }

    @RequestMapping(value = "adPerformance", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    Object getAdPerformance(HttpServletRequest request, HttpServletResponse response) {
        Date startDate = DateUtils.getStartDate(request.getParameter("startDate"));
        Date endDate = DateUtils.getEndDate(request.getParameter("endDate"));

        String fieldsOnly = request.getParameter("fieldsOnly");
        Map returnMap = new HashMap();
        List<ColumnDef> columnDefs = new ArrayList<>();
        columnDefs.add(new ColumnDef("source", "string", "Source", 1));
        columnDefs.add(new ColumnDef("adDescription", "string", "Ad Description"));
        columnDefs.add(new ColumnDef("campaignName", "string", "Campaign Name"));
        columnDefs.add(new ColumnDef("adGroupName", "string", "Ad Group Name"));
        columnDefs.add(new ColumnDef("impressions", "number", "Impressions", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
        columnDefs.add(new ColumnDef("clicks", "number", "Clicks", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
        columnDefs.add(new ColumnDef("ctr", "number", "CTR", ColumnDef.Aggregation.CTR, ColumnDef.Format.PERCENTAGE));
        columnDefs.add(new ColumnDef("cost", "number", "Cost", ColumnDef.Aggregation.SUM, ColumnDef.Format.CURRENCY));
        columnDefs.add(new ColumnDef("averageCpc", "number", "Average CPC", ColumnDef.Aggregation.CPC, ColumnDef.Format.CURRENCY));
        columnDefs.add(new ColumnDef("averagePosition", "number", "Average Position", ColumnDef.Aggregation.AVG, ColumnDef.Format.DECIMAL1));
        columnDefs.add(new ColumnDef("conversions", "number", "Conversions", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
        columnDefs.add(new ColumnDef("cpa", "number", "CPL", ColumnDef.Aggregation.CPA, ColumnDef.Format.CURRENCY));
        columnDefs.add(new ColumnDef("adType", "string", "Ad Type"));
        columnDefs.add(new ColumnDef("description", "string", "Description"));
        columnDefs.add(new ColumnDef("description1", "string", "Description1"));
        columnDefs.add(new ColumnDef("description2", "string", "Description2"));
        columnDefs.add(new ColumnDef("displayUrl", "string", "Display Url"));
        columnDefs.add(new ColumnDef("creativeFinalUrl", "string", "Final Url"));
        columnDefs.add(new ColumnDef("creativeDestinationUrl", "string", "Destination Url"));

        returnMap.put("columnDefs", columnDefs);
        if (fieldsOnly != null) {
            return returnMap;
        }
        AccountDetails accountDetails = ApiUtils.toAccountDetails(request, "ppc");
        List<AdPerformanceReportBean> performanceReportBeans = new ArrayList<>();
        if (accountDetails.getAdwordsAccountId() != null) {

            AdReport adwordsAdReport = adwordsService.getAdReport(startDate, endDate, accountDetails.getAdwordsAccountId(), "", "SEARCH");

            List<AdReportRow> adwordsAdReportRow = adwordsAdReport.getAdReportRow();

            for (Iterator<AdReportRow> reportRow = adwordsAdReportRow.iterator(); reportRow.hasNext();) {
                AdReportRow row = reportRow.next();
                AdPerformanceReportBean performanceBean = new AdPerformanceReportBean();
                performanceBean.setSource("Google");
                performanceBean.setCampaignName(row.getCampaign());
                performanceBean.setAdGroupName(row.getAdGroupName());
                performanceBean.setImpressions(row.getImpressions());
                performanceBean.setClicks(row.getClicks());
                performanceBean.setCtr(row.getCtr());
                performanceBean.setCost(row.getCost());
                performanceBean.setAverageCpc(row.getAverageCpc());
                performanceBean.setCpa(row.getCostPerConversion());
                performanceBean.setDescription(row.getDescription());
                performanceBean.setDescription1(row.getDescription1());
                performanceBean.setDescription2(row.getDescription2());
                performanceBean.setAdType(row.getAdType());
                performanceBean.setCreativeDestinationUrl(row.getCreativeDestinationUrl());
                performanceBean.setCreativeFinalUrls(row.getCreativeFinalUrls());
                performanceBean.setDisplayUrl(row.getDisplayUrl());
                performanceBean.setAveragePosition(row.getAveragePosition());
                performanceBean.setConversions(row.getAllConversions());
                performanceBean.setAdDescription(ApiUtils.getPaidAdDescription(performanceBean));
                performanceReportBeans.add(performanceBean);
            }
        }
        if (accountDetails.getBingAccountId() != null) {
            AdPerformanceReport bingAdPerformanceReport = bingService.getAdPerformanceReport(startDate, endDate, accountDetails.getBingAccountId());
            List<AdPerformanceRow> bingAdPerformanceRows = bingAdPerformanceReport.getAdPerformanceRows();
            for (Iterator<AdPerformanceRow> reportRow = bingAdPerformanceRows.iterator(); reportRow.hasNext();) {
                AdPerformanceRow row = reportRow.next();
                AdPerformanceReportBean performanceBean = new AdPerformanceReportBean();
                performanceBean.setSource("Bing");
                performanceBean.setCampaignName(row.getCampaignName().getValue());
                performanceBean.setAdGroupName(row.getAdGroupName().getValue());
                performanceBean.setImpressions(row.getImpressions().getValue());
                performanceBean.setClicks(row.getClicks().getValue());
                performanceBean.setCtr(row.getCtr().getValue());
                performanceBean.setCost(row.getSpend().getValue());
                performanceBean.setAverageCpc(row.getAverageCpc().getValue());
                performanceBean.setAveragePosition(row.getAveragePosition().getValue());
                performanceBean.setConversions(row.getConversions().getValue());
                performanceBean.setCpa(row.getCostPerConversion().getValue());
                performanceBean.setDescription(row.getAdTitle().getValue());
                performanceBean.setDescription1(row.getAdDescription().getValue());
                performanceBean.setDescription2("-");
                performanceBean.setAdType("-");
                performanceBean.setCreativeDestinationUrl(row.getDestinationUrl().getValue());
                performanceBean.setCreativeFinalUrls(row.getFinalUrl().getValue());
                performanceBean.setDisplayUrl(row.getDisplayUrl().getValue());
                performanceBean.setAdDescription(ApiUtils.getPaidAdDescription(performanceBean));
                performanceReportBeans.add(performanceBean);

            }
        }
        returnMap.put("data", performanceReportBeans);
        return returnMap;
    }

    @RequestMapping(value = "campaign", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    Object getCampaign(HttpServletRequest request, HttpServletResponse response) {
        Date startDate = DateUtils.getStartDate(request.getParameter("startDate"));
        Date endDate = DateUtils.getEndDate(request.getParameter("endDate"));
        String fieldsOnly = request.getParameter("fieldsOnly");
        Map returnMap = new HashMap();
        List<ColumnDef> columnDefs = new ArrayList<>();
        columnDefs.add(new ColumnDef("source", "string", "Source", 1));
        columnDefs.add(new ColumnDef("campaignName", "string", "Campaign Name"));
        columnDefs.add(new ColumnDef("impressions", "number", "Impressions", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
        columnDefs.add(new ColumnDef("clicks", "number", "Clicks", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
        columnDefs.add(new ColumnDef("ctr", "number", "CTR", ColumnDef.Aggregation.CTR, ColumnDef.Format.PERCENTAGE));
        columnDefs.add(new ColumnDef("cost", "number", "Cost", ColumnDef.Aggregation.SUM, ColumnDef.Format.CURRENCY));
        columnDefs.add(new ColumnDef("averageCpc", "number", "Average CPC", ColumnDef.Aggregation.CPC, ColumnDef.Format.CURRENCY));
        columnDefs.add(new ColumnDef("averagePosition", "number", "Average Position", ColumnDef.Aggregation.AVG, ColumnDef.Format.DECIMAL1));
        columnDefs.add(new ColumnDef("conversions", "number", "Conversions", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
        columnDefs.add(new ColumnDef("cpa", "number", "CPL", ColumnDef.Aggregation.CPA, ColumnDef.Format.CURRENCY));
        columnDefs.add(new ColumnDef("searchImpressionsShare", "number", "Search Impression Share", ColumnDef.Aggregation.AVG, ColumnDef.Format.PERCENTAGE));
        columnDefs.add(new ColumnDef("searchImpressionsShareLostByBudget", "number", "Search Impression Share Lost By Budget", ColumnDef.Aggregation.AVG, ColumnDef.Format.PERCENTAGE));
        columnDefs.add(new ColumnDef("searchImpressionsShareLostByRank", "number", "Search Impression Share Lost By Rank", ColumnDef.Aggregation.AVG, ColumnDef.Format.PERCENTAGE));
        returnMap.put("columnDefs", columnDefs);
        if (fieldsOnly != null) {
            return returnMap;
        }
        AccountDetails accountDetails = ApiUtils.toAccountDetails(request, "ppc");

        List<CampaignPerformanceReportBean> performanceReportBeans = new ArrayList<>();
        if (accountDetails.getAdwordsAccountId() != null) {
            CampaignReport campaignReport = adwordsService.getCampaignReport(startDate, endDate, accountDetails.getAdwordsAccountId(), "SEARCH");

            List<CampaignReportRow> adwordsCampaignReportRow = campaignReport.getCampaignReportRow();
            for (Iterator<CampaignReportRow> reportRow = adwordsCampaignReportRow.iterator(); reportRow.hasNext();) {
                CampaignReportRow row = reportRow.next();
                CampaignPerformanceReportBean campaignBean = new CampaignPerformanceReportBean();
                campaignBean.setSource("Google");
                campaignBean.setCampaignName(row.getCampaign());
                System.out.println(row.getCampaign());
                campaignBean.setImpressions(row.getImpressions());
                campaignBean.setClicks(row.getClicks());
                campaignBean.setCtr(row.getCtr());
                campaignBean.setCost(row.getCost());
                campaignBean.setAverageCpc(row.getAvgCPC());
                campaignBean.setAveragePosition(row.getAvgPosition());
                campaignBean.setConversions(row.getAllConv());
                campaignBean.setCpa(row.getCostConv());
                campaignBean.setSearchImpressionsShare(row.getSearchImprShare());
                campaignBean.setSearchImpressionsShareLostByBudget(row.getSearchLostISBudget());
                campaignBean.setSearchImpressionsShareLostByRank(row.getSearchLostISRank());
                performanceReportBeans.add(campaignBean);
            }
        }
        if (accountDetails.getBingAccountId() != null) {
            CampaignPerformanceReport campaignPerformanceReport = bingService.getCampaignPerformanceReport(startDate, endDate, accountDetails.getBingAccountId(), "");
            List<CampaignPerformanceRow> bingCampaignPerformanceRows = campaignPerformanceReport.getCampaignPerformanceRows();
            for (Iterator<CampaignPerformanceRow> reportRow = bingCampaignPerformanceRows.iterator(); reportRow.hasNext();) {
                CampaignPerformanceRow row = reportRow.next();
                CampaignPerformanceReportBean campaignBean = new CampaignPerformanceReportBean();
                campaignBean.setSource("Bing");
                campaignBean.setCampaignName(row.getCampaignName().getValue());
                campaignBean.setImpressions(row.getImpressions().getValue());
                campaignBean.setClicks(row.getClicks().getValue());
                campaignBean.setCtr(row.getCtr().getValue());
                campaignBean.setCost(row.getSpend().getValue());
                campaignBean.setAverageCpc(row.getAverageCpc().getValue());
                campaignBean.setAveragePosition(row.getAveragePosition().getValue());
                campaignBean.setConversions(row.getPhoneCalls().getValue());
                campaignBean.setCpa(row.getCostPerConversion().getValue());
                campaignBean.setSearchImpressionsShare(row.getImpressionSharePercent().getValue());
                campaignBean.setSearchImpressionsShareLostByBudget(row.getImpressionLostToBudgetPercent().getValue());
                campaignBean.setSearchImpressionsShareLostByRank("-");
                performanceReportBeans.add(campaignBean);

            }
        }
        return performanceReportBeans;
    }

    private Double parseDouble(String value) {
        if (value == null || value.isEmpty()) {
            return 0.0;
        }
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
        }
        return 0.0;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void handle(HttpMessageNotReadableException e) {
        e.printStackTrace();
    }
}
