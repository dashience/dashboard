/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.api.admin.controller;

import com.google.api.services.analyticsreporting.v4.model.GetReportsResponse;
import com.visumbu.api.admin.service.AdwordsService;
import com.visumbu.api.admin.service.BingService;
import com.visumbu.api.admin.service.GaService;
import com.visumbu.api.admin.service.UserService;
import com.visumbu.api.adwords.report.xml.bean.AccountDeviceReport;
import com.visumbu.api.adwords.report.xml.bean.AccountDeviceReportRow;
import com.visumbu.api.adwords.report.xml.bean.AccountReport;
import com.visumbu.api.adwords.report.xml.bean.AccountReportRow;
import com.visumbu.api.adwords.report.xml.bean.AdReport;
import com.visumbu.api.adwords.report.xml.bean.AdReportRow;
import com.visumbu.api.adwords.report.xml.bean.CallConversionReport;
import com.visumbu.api.adwords.report.xml.bean.CallConversionReportRow;
import com.visumbu.api.adwords.report.xml.bean.CampaignDeviceReport;
import com.visumbu.api.adwords.report.xml.bean.CampaignDeviceReportRow;
import com.visumbu.api.adwords.report.xml.bean.CampaignPerformanceReportRow;
import com.visumbu.api.adwords.report.xml.bean.GeoReport;
import com.visumbu.api.adwords.report.xml.bean.GeoReportRow;
import com.visumbu.api.bean.AccountDetails;
import com.visumbu.api.bean.ColumnDef;
import com.visumbu.api.dashboard.bean.AccountPerformanceReportBean;
import com.visumbu.api.dashboard.bean.AdPerformanceReportBean;
import com.visumbu.api.dashboard.bean.DevicePerformanceReportBean;
import com.visumbu.api.dashboard.bean.CampaignPerformanceReportBean;
import com.visumbu.api.dashboard.bean.GeoPerformanceReportBean;
import com.visumbu.api.utils.ApiUtils;
import com.visumbu.api.utils.DateUtils;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
@RequestMapping("display")
public class DisplayTabController {

    @Autowired
    private UserService userService;

    @Autowired
    private BingService bingService;

    @Autowired
    private AdwordsService adwordsService;

    @Autowired
    private GaService gaService;

    @RequestMapping(value = "geoPerformance", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    Object getGeoPerformance(HttpServletRequest request, HttpServletResponse response) {
        Date startDate = DateUtils.getStartDate(request.getParameter("startDate"));
        Date endDate = DateUtils.getEndDate(request.getParameter("endDate"));

        String fieldsOnly = request.getParameter("fieldsOnly");
        Map returnMap = new HashMap();
        List<ColumnDef> columnDefs = new ArrayList<>();
        columnDefs.add(new ColumnDef("source", "string", "Source", 1));
        columnDefs.add(new ColumnDef("city", "string", "City", 1));
        columnDefs.add(new ColumnDef("day", "string", "Day"));
        columnDefs.add(new ColumnDef("impressions", "number", "Impressions", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
        columnDefs.add(new ColumnDef("clicks", "number", "Clicks", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
        columnDefs.add(new ColumnDef("ctr", "number", "CTR", ColumnDef.Aggregation.CTR, ColumnDef.Format.PERCENTAGE));
        columnDefs.add(new ColumnDef("cost", "number", "Cost", ColumnDef.Aggregation.SUM, ColumnDef.Format.CURRENCY));
        columnDefs.add(new ColumnDef("averageCpc", "number", "Average CPC", ColumnDef.Aggregation.CPC, ColumnDef.Format.CURRENCY));
        columnDefs.add(new ColumnDef("averagePosition", "number", "Average Position", ColumnDef.Aggregation.AVG, ColumnDef.Format.DECIMAL1));
        columnDefs.add(new ColumnDef("conversions", "number", "Conversions", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
        columnDefs.add(new ColumnDef("cpa", "number", "CPL", ColumnDef.Aggregation.CPA, ColumnDef.Format.CURRENCY));

        columnDefs.add(new ColumnDef("directionsPageView", "number", "Directions Page View", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
        columnDefs.add(new ColumnDef("inventoryPageViews", "number", "Inventory Page Views", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
        columnDefs.add(new ColumnDef("leadSubmission", "number", "Lead Submission", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
        columnDefs.add(new ColumnDef("specialsPageView", "number", "Specials Page View", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
        columnDefs.add(new ColumnDef("timeOnSiteGt2Mins", "number", "Time On Site > 2Mins", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
        columnDefs.add(new ColumnDef("vdpViews", "number", "VDP Views", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
        returnMap.put("columnDefs", columnDefs);
        if (fieldsOnly != null) {
            return returnMap;
        }
        AccountDetails accountDetails = ApiUtils.toAccountDetails(request, "display");
        List<GeoPerformanceReportBean> performanceReportBeans = new ArrayList<>();
        if (accountDetails.getAdwordsAccountId() != null) {
            //GeoReport adwordsGeoReport = adwordsService.getGeoReport(startDate, endDate, "391-089-0213", "", "CONTENT");
            GeoReport adwordsGeoReport = adwordsService.getGeoReport(startDate, endDate, accountDetails.getAdwordsAccountId(), "", "CONTENT");
            List<GeoReportRow> adwordsGeoReportRow = adwordsGeoReport.getGeoReportRow();
            List<Map<String, String>> gaData = new ArrayList<>();
            if (accountDetails.getAnalyticsProfileId() != null) {
                GetReportsResponse goals = gaService.getGeoGoals(accountDetails.getAnalyticsAccountId(),accountDetails.getAnalyticsProfileId(), startDate, endDate);
                //GetReportsResponse goals = gaService.getGeoGoals("123125706", startDate, endDate);
                gaData = (List) gaService.getResponseAsMap(goals).get("data");
            }
            for (Iterator<GeoReportRow> reportRow = adwordsGeoReportRow.iterator(); reportRow.hasNext();) {
                GeoReportRow row = reportRow.next();
                GeoPerformanceReportBean performanceBean = new GeoPerformanceReportBean();
                performanceBean.setSource("Google");

                performanceBean.setCity(ApiUtils.getCityById(row.getCityCriteriaId()));
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
                performanceBean.setDirectionsPageView(getGaDataForType(gaData, "ga:city", performanceBean.getCity(), "directionsPageView"));
                performanceBean.setInventoryPageViews(getGaDataForType(gaData, "ga:city", performanceBean.getCity(), "inventoryPageViews"));
                performanceBean.setLeadSubmission(getGaDataForType(gaData, "ga:city", performanceBean.getCity(), "leadSubmission"));
                performanceBean.setSpecialsPageView(getGaDataForType(gaData, "ga:city", performanceBean.getCity(), "specialsPageView"));
                performanceBean.setTimeOnSiteGt2Mins(getGaDataForType(gaData, "ga:city", performanceBean.getCity(), "timeOnSiteGt2Mins"));
                performanceBean.setVdpViews(getGaDataForType(gaData, "ga:city", performanceBean.getCity(), "vdpViews"));
                Integer engagements = 0;
                engagements += (parseInt(performanceBean.getDirectionsPageView() == null ? "0" : performanceBean.getDirectionsPageView())
                        + parseInt(performanceBean.getInventoryPageViews() == null ? "0" : performanceBean.getInventoryPageViews())
                        + parseInt(performanceBean.getLeadSubmission() == null ? "0" : performanceBean.getLeadSubmission())
                        + parseInt(performanceBean.getSpecialsPageView() == null ? "0" : performanceBean.getSpecialsPageView())
                        + parseInt(performanceBean.getTimeOnSiteGt2Mins() == null ? "0" : performanceBean.getTimeOnSiteGt2Mins())
                        + parseInt(performanceBean.getVdpViews() == null ? "0" : performanceBean.getVdpViews()));
                performanceBean.setEngagements(engagements + "");
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
        columnDefs.add(new ColumnDef("campaign", "string", "Campaign", 1));
        columnDefs.add(new ColumnDef("device", "string", "Device"));
        columnDefs.add(new ColumnDef("impressions", "number", "Impressions", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
        columnDefs.add(new ColumnDef("clicks", "number", "Clicks", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
        columnDefs.add(new ColumnDef("ctr", "number", "CTR", ColumnDef.Aggregation.CTR, ColumnDef.Format.PERCENTAGE));
        columnDefs.add(new ColumnDef("cost", "number", "Cost", ColumnDef.Aggregation.SUM, ColumnDef.Format.CURRENCY));
        columnDefs.add(new ColumnDef("averageCpc", "number", "Average CPC", ColumnDef.Aggregation.CPC, ColumnDef.Format.CURRENCY));
        columnDefs.add(new ColumnDef("averagePosition", "number", "Average Position", ColumnDef.Aggregation.AVG, ColumnDef.Format.DECIMAL1));
        columnDefs.add(new ColumnDef("conversions", "number", "Conversions", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
        columnDefs.add(new ColumnDef("cpa", "number", "CPL", ColumnDef.Aggregation.CPA, ColumnDef.Format.CURRENCY));

        columnDefs.add(new ColumnDef("day", "string", "Day", ColumnDef.Aggregation.None, ColumnDef.Format.INTEGER));
        columnDefs.add(new ColumnDef("directionsPageView", "number", "Directions Page View", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
        columnDefs.add(new ColumnDef("inventoryPageViews", "number", "Inventory Page Views", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
        columnDefs.add(new ColumnDef("leadSubmission", "number", "Lead Submission", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
        columnDefs.add(new ColumnDef("specialsPageView", "number", "Specials Page View", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
        columnDefs.add(new ColumnDef("timeOnSiteGt2Mins", "number", "Time On Site > 2Mins", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
        columnDefs.add(new ColumnDef("vdpViews", "number", "VDP Views", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
        returnMap.put("columnDefs", columnDefs);
        if (fieldsOnly != null) {
            return returnMap;
        }
        List<DevicePerformanceReportBean> performanceReportBeans = new ArrayList<>();
        AccountDetails accountDetails = ApiUtils.toAccountDetails(request, "display");
        if (accountDetails.getAdwordsAccountId() != null) {
            CampaignDeviceReport adwordsCampaignDeviceReport = adwordsService.getCampaignDeviceReport(startDate, endDate, accountDetails.getAdwordsAccountId(), "", "CONTENT");
            List<CampaignDeviceReportRow> adwordsCampaignDeviceReportRow = adwordsCampaignDeviceReport.getCampaignDeviceReportRow();
            List<Map<String, String>> gaData = new ArrayList<>();
            if (accountDetails.getAnalyticsProfileId() != null) {
                GetReportsResponse goals = gaService.getCampaignDeviceGoals(accountDetails.getAnalyticsAccountId(),accountDetails.getAnalyticsProfileId(), startDate, endDate);
                gaData = (List) gaService.getResponseAsMap(goals).get("data");
            }
            for (Iterator<CampaignDeviceReportRow> reportRow = adwordsCampaignDeviceReportRow.iterator(); reportRow.hasNext();) {
                CampaignDeviceReportRow row = reportRow.next();
                DevicePerformanceReportBean performanceBean = new DevicePerformanceReportBean();
                performanceBean.setSource("Google");
                if (row.getDevice().contains("Tablet")) {
                    performanceBean.setDevice("tablet");
                } else if (row.getDevice().contains("Mobile")) {
                    performanceBean.setDevice("mobile");
                } else if (row.getDevice().contains("Computer")) {
                    performanceBean.setDevice("desktop");
                } else {
                    performanceBean.setDevice(row.getDevice());
                }
                performanceBean.setCampaign(row.getCampaign());
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
                performanceBean.setDirectionsPageView(getGaDataFor2Types(gaData, "ga:deviceCategory", performanceBean.getDevice(), "ga:campaign", row.getCampaign(), "directionsPageView"));
                performanceBean.setInventoryPageViews(getGaDataFor2Types(gaData, "ga:deviceCategory", performanceBean.getDevice(), "ga:campaign", row.getCampaign(), "inventoryPageViews"));
                performanceBean.setLeadSubmission(getGaDataFor2Types(gaData, "ga:deviceCategory", performanceBean.getDevice(), "ga:campaign", row.getCampaign(), "leadSubmission"));
                performanceBean.setSpecialsPageView(getGaDataFor2Types(gaData, "ga:deviceCategory", performanceBean.getDevice(), "ga:campaign", row.getCampaign(), "specialsPageView"));
                performanceBean.setTimeOnSiteGt2Mins(getGaDataFor2Types(gaData, "ga:deviceCategory", performanceBean.getDevice(), "ga:campaign", row.getCampaign(), "timeOnSiteGt2Mins"));
                performanceBean.setVdpViews(getGaDataFor2Types(gaData, "ga:deviceCategory", performanceBean.getDevice(), "ga:campaign", row.getCampaign(), "vdpViews"));
                Integer engagements = 0;
                engagements += (parseInt(performanceBean.getDirectionsPageView() == null ? "0" : performanceBean.getDirectionsPageView())
                        + parseInt(performanceBean.getInventoryPageViews() == null ? "0" : performanceBean.getInventoryPageViews())
                        + parseInt(performanceBean.getLeadSubmission() == null ? "0" : performanceBean.getLeadSubmission())
                        + parseInt(performanceBean.getSpecialsPageView() == null ? "0" : performanceBean.getSpecialsPageView())
                        + parseInt(performanceBean.getTimeOnSiteGt2Mins() == null ? "0" : performanceBean.getTimeOnSiteGt2Mins())
                        + parseInt(performanceBean.getVdpViews() == null ? "0" : performanceBean.getVdpViews()));
                performanceBean.setEngagements(engagements + "");
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
        columnDefs.add(new ColumnDef("device", "string", "Device"));
        columnDefs.add(new ColumnDef("impressions", "number", "Impressions", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
        columnDefs.add(new ColumnDef("clicks", "number", "Clicks", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
        columnDefs.add(new ColumnDef("ctr", "number", "CTR", ColumnDef.Aggregation.CTR, ColumnDef.Format.PERCENTAGE));
        columnDefs.add(new ColumnDef("cost", "number", "Cost", ColumnDef.Aggregation.SUM, ColumnDef.Format.CURRENCY));
        columnDefs.add(new ColumnDef("averageCpc", "number", "Average CPC", ColumnDef.Aggregation.CPC, ColumnDef.Format.CURRENCY));
        columnDefs.add(new ColumnDef("averagePosition", "number", "Average Position", ColumnDef.Aggregation.AVG, ColumnDef.Format.DECIMAL1));
        columnDefs.add(new ColumnDef("conversions", "number", "Conversions", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
        columnDefs.add(new ColumnDef("cpa", "number", "CPL", ColumnDef.Aggregation.CPA, ColumnDef.Format.CURRENCY));

        columnDefs.add(new ColumnDef("day", "string", "Day"));
        columnDefs.add(new ColumnDef("directionsPageView", "number", "Directions Page View", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
        columnDefs.add(new ColumnDef("inventoryPageViews", "number", "Inventory Page Views", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
        columnDefs.add(new ColumnDef("leadSubmission", "number", "Lead Submission", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
        columnDefs.add(new ColumnDef("specialsPageView", "number", "Specials Page View", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
        columnDefs.add(new ColumnDef("timeOnSiteGt2Mins", "number", "Time On Site > 2Mins", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
        columnDefs.add(new ColumnDef("vdpViews", "number", "VDP Views", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
        columnDefs.add(new ColumnDef("engagements", "number", "Engagements", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
        returnMap.put("columnDefs", columnDefs);
        if (fieldsOnly != null) {
            return returnMap;
        }
        AccountDetails accountDetails = ApiUtils.toAccountDetails(request, "display");
        List<DevicePerformanceReportBean> performanceReportBeans = new ArrayList<>();
        if (accountDetails.getAdwordsAccountId() != null) {
            AccountDeviceReport adwordsAccountDeviceReport = adwordsService.getAccountDevicePerformanceReport(startDate, endDate, accountDetails.getAdwordsAccountId(), "", "CONTENT");
            List<AccountDeviceReportRow> adwordsAccountDeviceReportRow = adwordsAccountDeviceReport.getAccountDeviceReportRow();
            List<Map<String, String>> gaData = new ArrayList<>();
            if (accountDetails.getAnalyticsProfileId() != null) {
                GetReportsResponse goals = gaService.getDeviceGoals(accountDetails.getAnalyticsAccountId(),accountDetails.getAnalyticsProfileId(), startDate, endDate);
                gaData = (List) gaService.getResponseAsMap(goals).get("data");
            }
            for (Iterator<AccountDeviceReportRow> reportRow = adwordsAccountDeviceReportRow.iterator(); reportRow.hasNext();) {
                AccountDeviceReportRow row = reportRow.next();
                DevicePerformanceReportBean performanceBean = new DevicePerformanceReportBean();
                performanceBean.setSource("Google");
                if (row.getDevice().contains("Tablet")) {
                    performanceBean.setDevice("tablet");
                } else if (row.getDevice().contains("Mobile")) {
                    performanceBean.setDevice("mobile");
                } else if (row.getDevice().contains("Computer")) {
                    performanceBean.setDevice("desktop");
                } else {
                    performanceBean.setDevice(row.getDevice());
                }
                //performanceBean.setDevice(row.getDevice());
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
                performanceBean.setDirectionsPageView(getGaDataForType(gaData, "ga:deviceCategory", performanceBean.getDevice(), "directionsPageView"));
                performanceBean.setInventoryPageViews(getGaDataForType(gaData, "ga:deviceCategory", performanceBean.getDevice(), "inventoryPageViews"));
                performanceBean.setLeadSubmission(getGaDataForType(gaData, "ga:deviceCategory", performanceBean.getDevice(), "leadSubmission"));
                performanceBean.setSpecialsPageView(getGaDataForType(gaData, "ga:deviceCategory", performanceBean.getDevice(), "specialsPageView"));
                performanceBean.setTimeOnSiteGt2Mins(getGaDataForType(gaData, "ga:deviceCategory", performanceBean.getDevice(), "timeOnSiteGt2Mins"));
                performanceBean.setVdpViews(getGaDataForType(gaData, "ga:deviceCategory", performanceBean.getDevice(), "vdpViews"));
                Integer engagements = 0;
                engagements += (parseInt(performanceBean.getDirectionsPageView() == null ? "0" : performanceBean.getDirectionsPageView())
                        + parseInt(performanceBean.getInventoryPageViews() == null ? "0" : performanceBean.getInventoryPageViews())
                        + parseInt(performanceBean.getLeadSubmission() == null ? "0" : performanceBean.getLeadSubmission())
                        + parseInt(performanceBean.getSpecialsPageView() == null ? "0" : performanceBean.getSpecialsPageView())
                        + parseInt(performanceBean.getTimeOnSiteGt2Mins() == null ? "0" : performanceBean.getTimeOnSiteGt2Mins())
                        + parseInt(performanceBean.getVdpViews() == null ? "0" : performanceBean.getVdpViews()));
                performanceBean.setEngagements(engagements + "");
                performanceReportBeans.add(performanceBean);
            }
        }
        returnMap.put("data", performanceReportBeans);
        return returnMap;
    }

    private Integer parseInt(String string) {
        if (string == null || string.isEmpty()) {
            return 0;
        }
        return Integer.parseInt(string);
    }

    @RequestMapping(value = "ad", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    Object getAd(HttpServletRequest request, HttpServletResponse response) {
        Date startDate = DateUtils.getStartDate(request.getParameter("startDate"));
        Date endDate = DateUtils.getEndDate(request.getParameter("endDate"));

        String fieldsOnly = request.getParameter("fieldsOnly");
        Map returnMap = new HashMap();
        List<ColumnDef> columnDefs = new ArrayList<>();
        columnDefs.add(new ColumnDef("source", "string", "Source", 1));
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
        columnDefs.add(new ColumnDef("searchImpressionsShare", "number", "Search Impression Share", ColumnDef.Aggregation.AVG, ColumnDef.Format.PERCENTAGE));

        columnDefs.add(new ColumnDef("directionsPageView", "number", "Directions Page View", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
        columnDefs.add(new ColumnDef("inventoryPageViews", "number", "Inventory Page Views", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
        columnDefs.add(new ColumnDef("leadSubmission", "number", "Lead Submission", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
        columnDefs.add(new ColumnDef("specialsPageView", "number", "Specials Page View", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
        columnDefs.add(new ColumnDef("timeOnSiteGt2Mins", "number", "Time On Site > 2Mins", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
        columnDefs.add(new ColumnDef("vdpViews", "number", "VDP Views", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
        columnDefs.add(new ColumnDef("adDescription", "string", "Ad Description"));
        columnDefs.add(new ColumnDef("description", "string", "Description"));
        columnDefs.add(new ColumnDef("description1", "string", "Description1"));
        columnDefs.add(new ColumnDef("description2", "string", "Description2"));
        columnDefs.add(new ColumnDef("headline", "string", "Headline"));
        columnDefs.add(new ColumnDef("creativeFinalUrls", "string", "Creative Final Urls"));
        columnDefs.add(new ColumnDef("adType", "string", "Ad Type"));
        columnDefs.add(new ColumnDef("ad", "string", "Ad"));

        returnMap.put("columnDefs", columnDefs);
        if (fieldsOnly != null) {
            return returnMap;
        }
        AccountDetails accountDetails = ApiUtils.toAccountDetails(request, "display");
        List<AdPerformanceReportBean> performanceReportBeans = new ArrayList<>();
        if (accountDetails.getAdwordsAccountId() != null) {
            AdReport adwordsAdReport = adwordsService.getAdReport(startDate, endDate, accountDetails.getAdwordsAccountId(), "", "CONTENT");
            List<Map<String, String>> gaData = new ArrayList<>();
            if (accountDetails.getAnalyticsProfileId() != null) {
                GetReportsResponse goals = gaService.getAdGoals(accountDetails.getAnalyticsAccountId(),accountDetails.getAnalyticsProfileId(), startDate, endDate, "");
                gaData = (List) gaService.getResponseAsMap(goals).get("data");
            }
            returnMap.put("gaData", gaData);
            List<AdReportRow> adwordsAdReportRow = adwordsAdReport.getAdReportRow();

            for (Iterator<AdReportRow> reportRow = adwordsAdReportRow.iterator(); reportRow.hasNext();) {
                AdReportRow row = reportRow.next();
                AdPerformanceReportBean performanceBean = new AdPerformanceReportBean();
                performanceBean.setSource("Google");
                performanceBean.setAdType(row.getAdType());
                performanceBean.setCampaignName(row.getCampaign());
                performanceBean.setAdGroupName(row.getAdGroupName());
                performanceBean.setAccountDescriptiveName(row.getAccount());
                performanceBean.setImpressions(row.getImpressions());
                performanceBean.setClicks(row.getClicks());
                performanceBean.setCtr(row.getCtr());
                performanceBean.setDescription(row.getDescription());
                performanceBean.setDescription1(row.getDescription1());
                performanceBean.setDescription2(row.getDescription2());
                performanceBean.setCost(row.getCost());
                performanceBean.setAverageCpc(row.getAverageCpc());
                performanceBean.setCpa(row.getCostPerConversion());
                performanceBean.setAveragePosition(row.getAveragePosition());
                performanceBean.setConversions(row.getAllConversions());
                performanceBean.setCreativeFinalUrls(row.getCreativeFinalUrls());
                performanceBean.setHeadline(row.getHeadline());
                performanceBean.setDirectionsPageView(getGaDataForDateAdType(gaData, "ga:adContent", row.getHeadline(), "directionsPageView"));
                performanceBean.setInventoryPageViews(getGaDataForDateAdType(gaData, "ga:adContent", row.getHeadline(), "inventoryPageViews"));
                performanceBean.setLeadSubmission(getGaDataForDateAdType(gaData, "ga:adContent", row.getHeadline(), "leadSubmission"));
                performanceBean.setSpecialsPageView(getGaDataForDateAdType(gaData, "ga:adContent", row.getHeadline(), "specialsPageView"));
                performanceBean.setTimeOnSiteGt2Mins(getGaDataForDateAdType(gaData, "ga:adContent", row.getHeadline(), "timeOnSiteGt2Mins"));
                performanceBean.setVdpViews(getGaDataForDateAdType(gaData, "ga:adContent", row.getHeadline(), "vdpViews"));
                performanceBean.setAdDescription(ApiUtils.getDisplayAdDescription(performanceBean));
                performanceReportBeans.add(performanceBean);
            }
        }
        returnMap.put("data", performanceReportBeans);
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
        columnDefs.add(new ColumnDef("source", "string", "Source"));
        columnDefs.add(new ColumnDef("day", "string", "Day"));
        columnDefs.add(new ColumnDef("impressions", "number", "Impressions", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
        columnDefs.add(new ColumnDef("clicks", "number", "Clicks", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
        columnDefs.add(new ColumnDef("ctr", "number", "CTR", ColumnDef.Aggregation.CTR, ColumnDef.Format.PERCENTAGE));
        columnDefs.add(new ColumnDef("averagePosition", "number", "Average Position", ColumnDef.Aggregation.AVG, ColumnDef.Format.DECIMAL1));
        columnDefs.add(new ColumnDef("cost", "number", "Cost", ColumnDef.Aggregation.SUM, ColumnDef.Format.CURRENCY));
        columnDefs.add(new ColumnDef("averageCpc", "number", "Average CPC", ColumnDef.Aggregation.CPC, ColumnDef.Format.CURRENCY));
        columnDefs.add(new ColumnDef("conversions", "number", "Conversions", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
        columnDefs.add(new ColumnDef("cpa", "number", "CPL", ColumnDef.Aggregation.CPA, ColumnDef.Format.CURRENCY));
        columnDefs.add(new ColumnDef("impressionShare", "number", "Impression Share", ColumnDef.Aggregation.AVG, ColumnDef.Format.PERCENTAGE));
        columnDefs.add(new ColumnDef("searchImpressionsShareLostByBudget", "number", "Search Impression Share Lost By Budget", ColumnDef.Aggregation.AVG, ColumnDef.Format.PERCENTAGE));
        columnDefs.add(new ColumnDef("searchImpressionsShareLostByRank", "number", "Search Impression Share Lost By Rank", ColumnDef.Aggregation.AVG, ColumnDef.Format.PERCENTAGE));

        columnDefs.add(new ColumnDef("directionsPageView", "number", "Directions Page View", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
        columnDefs.add(new ColumnDef("inventoryPageViews", "number", "Inventory Page Views", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
        columnDefs.add(new ColumnDef("leadSubmission", "number", "Lead Submission", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
        columnDefs.add(new ColumnDef("specialsPageView", "number", "Specials Page View", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
        columnDefs.add(new ColumnDef("timeOnSiteGt2Mins", "number", "Time On Site > 2Mins", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
        columnDefs.add(new ColumnDef("vdpViews", "number", "VDP Views", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
        returnMap.put("columnDefs", columnDefs);
        if (fieldsOnly != null) {
            return returnMap;
        }
        AccountDetails accountDetails = ApiUtils.toAccountDetails(request, "display");
        List<CampaignPerformanceReportBean> performanceReportBeans = new ArrayList<>();
        if (accountDetails.getAdwordsAccountId() != null) {
            com.visumbu.api.adwords.report.xml.bean.CampaignPerformanceReport adWordsCampaignPerformanceReport = adwordsService.getCampaignPerformanceReport(startDate, endDate, accountDetails.getAdwordsAccountId(), "", "CONTENT");
            List<CampaignPerformanceReportRow> adwordsCampaignPerformanceReportRow = adWordsCampaignPerformanceReport.getCampaignPerformanceReportRow();
            List<Map<String, String>> gaData = new ArrayList<>();
            if (accountDetails.getAnalyticsProfileId() != null) {
                GetReportsResponse goals = gaService.getCampaignGoals(accountDetails.getAnalyticsAccountId(),accountDetails.getAnalyticsProfileId(), startDate, endDate, "");
                gaData = (List) gaService.getResponseAsMap(goals).get("data");
            }
            returnMap.put("gaData", gaData);
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
                performanceBean.setSearchImpressionsShare(row.getSearchExactMatchIS());
                performanceBean.setSearchImpressionsShareLostByBudget(row.getSearchLostISBudget());
                performanceBean.setSearchImpressionsShareLostByRank(row.getSearchLostISRank());
                performanceBean.setDay(row.getDay());

                performanceBean.setDirectionsPageView(getGaDataForDateType(gaData, "ga:campaign", row.getCampaign(), "directionsPageView"));
                performanceBean.setInventoryPageViews(getGaDataForDateType(gaData, "ga:campaign", row.getCampaign(), "inventoryPageViews"));
                performanceBean.setLeadSubmission(getGaDataForDateType(gaData, "ga:campaign", row.getCampaign(), "leadSubmission"));
                performanceBean.setSpecialsPageView(getGaDataForDateType(gaData, "ga:campaign", row.getCampaign(), "specialsPageView"));
                performanceBean.setTimeOnSiteGt2Mins(getGaDataForDateType(gaData, "ga:campaign", row.getCampaign(), "timeOnSiteGt2Mins"));
                performanceBean.setVdpViews(getGaDataForDateType(gaData, "ga:campaign", row.getCampaign(), "vdpViews"));
                performanceReportBeans.add(performanceBean);
            }
        }
        returnMap.put("data", performanceReportBeans);

        return returnMap;
    }

    @RequestMapping(value = "accountPerformance12Weeks", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    Object getAccountPerformance12Weeks(HttpServletRequest request, HttpServletResponse response) {
        Map returnMap = new HashMap();
        try {
            Date startDate = DateUtils.get12WeeksBack(request.getParameter("endDate"));
            Date endDate = DateUtils.getEndDate(request.getParameter("endDate"));
            String fieldsOnly = request.getParameter("fieldsOnly");
            List<ColumnDef> columnDefs = new ArrayList<>();
            columnDefs.add(new ColumnDef("source", "string", "Source", 1));
            columnDefs.add(new ColumnDef("week", "date", "Week", 1));
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
            AccountDetails accountDetails = ApiUtils.toAccountDetails(request, "display");
            List<AccountPerformanceReportBean> performanceReportBeans = new ArrayList<>();
            if (accountDetails.getAdwordsAccountId() != null) {
                AccountReport adwordsAccountReport = adwordsService.getAccountReport(startDate, endDate, accountDetails.getAdwordsAccountId(), "weekly", "CONTENT");
                List<AccountReportRow> adwordsAccountRow = adwordsAccountReport.getAccountReportRow();

                for (Iterator<AccountReportRow> reportRow = adwordsAccountRow.iterator(); reportRow.hasNext();) {
                    AccountReportRow row = reportRow.next();
                    AccountPerformanceReportBean performanceBean = new AccountPerformanceReportBean();
                    performanceBean.setSource("Google");
//            performanceBean.setDevice(row.getDevice());
                    performanceBean.setImpressions(row.getImpressions());
                    performanceBean.setClicks(row.getClicks());
                    performanceBean.setCtr(row.getCtr());
                    performanceBean.setDay(DateUtils.dateToString(DateUtils.toDate(row.getWeek(), "yyyy-MM-dd"), "MM/dd/yyyy"));
                    performanceBean.setWeek(DateUtils.dateToString(DateUtils.toDate(row.getWeek(), "yyyy-MM-dd"), "MM/dd/yyyy")); //
                    String cost = row.getCost(); //Integer.toString(Integer.parseInt(row.getCost()) / 1000000);
                    performanceBean.setCost(cost);
                    String cpc = row.getAvgCPC(); //Integer.toString(Integer.parseInt(row.getAvgCPC()) / 1000000);
                    performanceBean.setAverageCpc(cpc);
                    String cpa = row.getCostConv(); //Integer.toString(Integer.parseInt(row.getCostConv()) / 1000000);
                    performanceBean.setCpa(cpa);

                    performanceBean.setAveragePosition(row.getAvgPosition());
                    performanceBean.setConversions(row.getAllConv());
                    performanceBean.setSearchImpressionsShare(row.getSearchImprShare());
                    performanceBean.setSearchImpressionsShareLostDueToRank(row.getSearchLostISRank());
                    performanceBean.setSearchImpressionsShareLostDueToBudget(row.getSearchLostISBudget());

                    performanceReportBeans.add(performanceBean);
                }
                returnMap.put("data", performanceReportBeans);
            }
        } catch (Exception ex) {
            Logger.getLogger(PaidTabController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return returnMap;
    }

    @RequestMapping(value = "accountPerformance", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    Object getAccountPerformance(HttpServletRequest request, HttpServletResponse response) {
        Map returnMap = new HashMap();
        try {
            Date startDate = DateUtils.getStartDate(request.getParameter("startDate"));
            Date endDate = DateUtils.getEndDate(request.getParameter("endDate"));
            String fieldsOnly = request.getParameter("fieldsOnly");
            List<ColumnDef> columnDefs = new ArrayList<>();
            columnDefs.add(new ColumnDef("source", "string", "Source", 1));
            columnDefs.add(new ColumnDef("impressions", "number", "Impressions", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
            columnDefs.add(new ColumnDef("clicks", "number", "Clicks", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
            columnDefs.add(new ColumnDef("ctr", "number", "CTR", ColumnDef.Aggregation.CTR, ColumnDef.Format.PERCENTAGE));
            columnDefs.add(new ColumnDef("cost", "number", "Cost", ColumnDef.Aggregation.SUM, ColumnDef.Format.CURRENCY));
            columnDefs.add(new ColumnDef("averageCpc", "number", "Average CPC", ColumnDef.Aggregation.CPC, ColumnDef.Format.CURRENCY));
            columnDefs.add(new ColumnDef("averagePosition", "number", "Average Position", ColumnDef.Aggregation.AVG, ColumnDef.Format.DECIMAL1));
            columnDefs.add(new ColumnDef("conversions", "number", "Conversions", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
            columnDefs.add(new ColumnDef("cpa", "number", "CPL", ColumnDef.Aggregation.CPA, ColumnDef.Format.CURRENCY));
            columnDefs.add(new ColumnDef("searchImpressionsShare", "number", "Search Impression Share", ColumnDef.Aggregation.AVG, ColumnDef.Format.PERCENTAGE));
            columnDefs.add(new ColumnDef("searchImpressionsShareLostDueToBudget", "number", "Search Impression Share Lost Due To Budget", ColumnDef.Aggregation.AVG, ColumnDef.Format.PERCENTAGE));

            columnDefs.add(new ColumnDef("searchImpressionsShareLostDueToRank", "number", "Search Impression Share Lost Due To Rank", ColumnDef.Aggregation.AVG, ColumnDef.Format.PERCENTAGE));
            columnDefs.add(new ColumnDef("day", "string", "Day"));
            columnDefs.add(new ColumnDef("directionsPageView", "number", "Directions Page View", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
            columnDefs.add(new ColumnDef("inventoryPageViews", "number", "Inventory Page Views", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
            columnDefs.add(new ColumnDef("leadSubmission", "number", "Lead Submission", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
            columnDefs.add(new ColumnDef("specialsPageView", "number", "Specials Page View", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
            columnDefs.add(new ColumnDef("timeOnSiteGt2Mins", "number", "Time On Site > 2Mins", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
            columnDefs.add(new ColumnDef("vdpViews", "number", "VDP Views", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
            returnMap.put("columnDefs", columnDefs);
            if (fieldsOnly != null) {
                return returnMap;
            }
            AccountDetails accountDetails = ApiUtils.toAccountDetails(request, "display");
            
//            com.visumbu.api.adwords.report.xml.bean.CampaignPerformanceReport adWordsCampaignPerformanceReport = adwordsService.getCampaignPerformanceReport(startDate, endDate, accountDetails.getAdwordsAccountId(), "", "CONTENT");
//                Integer conversionSum = 0;
//                List<CampaignPerformanceReportRow> campaignPerformanceReportRow = adWordsCampaignPerformanceReport.getCampaignPerformanceReportRow();
//                for (Iterator<CampaignPerformanceReportRow> iterator = campaignPerformanceReportRow.iterator(); iterator.hasNext();) {
//                    CampaignPerformanceReportRow reportRow = iterator.next();
//                    System.out.println("Conversion => " + reportRow.getPhoneCalls());
//                    conversionSum += ApiUtils.toInteger(reportRow.getPhoneCalls());
//                }
//
//                CallConversionReport accountCallConversionsReport = adwordsService.getAccountCallConversionsReport(startDate, endDate, accountDetails.getAdwordsAccountId(), "", "CONTENT");
//                if (accountCallConversionsReport != null) {
//                    List<CallConversionReportRow> callConversionReportRow = accountCallConversionsReport.getCallConversionReportRow();
//                    for (Iterator<CallConversionReportRow> iterator = callConversionReportRow.iterator(); iterator.hasNext();) {
//                        CallConversionReportRow reportRow = iterator.next();
//                        System.out.println("Conversion1 => " + ApiUtils.toDouble(reportRow.getConversions()));
//                        conversionSum += ApiUtils.toDouble(reportRow.getConversions()).intValue();
//                    }
//                }
            List<AccountPerformanceReportBean> performanceReportBeans = new ArrayList<>();
            if (accountDetails.getAdwordsAccountId() != null) {
                GetReportsResponse goals = gaService.getGoals(accountDetails.getAnalyticsAccountId(),accountDetails.getAnalyticsProfileId(), startDate, endDate, "");
                List<Map<String, String>> gaData = (List) gaService.getResponseAsMap(goals).get("data");

                AccountReport adwordsAccountReport = adwordsService.getAccountReport(startDate, endDate, accountDetails.getAdwordsAccountId(), "", "CONTENT");
                List<AccountReportRow> adwordsAccountRow = adwordsAccountReport.getAccountReportRow();
                for (Iterator<AccountReportRow> reportRow = adwordsAccountRow.iterator(); reportRow.hasNext();) {
                    AccountReportRow row = reportRow.next();
                    AccountPerformanceReportBean performanceBean = new AccountPerformanceReportBean();
                    performanceBean.setSource("Google");
                    performanceBean.setDay(row.getDay());
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
                    // performanceBean.setConversions(conversionSum + "");
                    performanceBean.setSearchImpressionsShare(row.getSearchImprShare());
                    performanceBean.setSearchImpressionsShareLostDueToRank(row.getSearchLostISRank());
                    performanceBean.setSearchImpressionsShareLostDueToBudget(row.getSearchLostISBudget());
                    performanceBean.setDirectionsPageView(getGaDataFor(gaData, row.getDay(), "directionsPageView"));
                    performanceBean.setInventoryPageViews(getGaDataFor(gaData, row.getDay(), "inventoryPageViews"));
                    performanceBean.setLeadSubmission(getGaDataFor(gaData, row.getDay(), "leadSubmission"));
                    performanceBean.setSpecialsPageView(getGaDataFor(gaData, row.getDay(), "specialsPageView"));
                    performanceBean.setTimeOnSiteGt2Mins(getGaDataFor(gaData, row.getDay(), "timeOnSiteGt2Mins"));
                    performanceBean.setVdpViews(getGaDataFor(gaData, row.getDay(), "vdpViews"));
                    performanceReportBeans.add(performanceBean);
                }
            }
            returnMap.put("data", performanceReportBeans);

        } catch (Exception ex) {
            Logger.getLogger(PaidTabController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return returnMap;
    }

    private String getGaDataFor(List<Map<String, String>> gaData, String date, String metric) {
        for (Iterator<Map<String, String>> iterator = gaData.iterator(); iterator.hasNext();) {
            Map<String, String> data = iterator.next();
            return data.get(metric);
//            if (data.get("ga:date").equalsIgnoreCase(date.replaceAll("-", ""))) {
//                return data.get(metric);
//            }
        }
        return "";
    }

    private String getGaDataForType(List<Map<String, String>> gaData, String field, String value, String metric) {
        for (Iterator<Map<String, String>> iterator = gaData.iterator(); iterator.hasNext();) {
            Map<String, String> data = iterator.next();
            if (data.get(field).equalsIgnoreCase(value)) {
                return data.get(metric);
            }
        }
        return "0";
    }

    private String getGaDataFor2Types(List<Map<String, String>> gaData, String field1, String value1, String field2, String value2, String metric) {
        for (Iterator<Map<String, String>> iterator = gaData.iterator(); iterator.hasNext();) {
            Map<String, String> data = iterator.next();
            if (data.get(field1).equalsIgnoreCase(value1) && data.get(field2).equalsIgnoreCase(value2)) {
                return data.get(metric);
            }
        }
        return "0";
    }

    private String getGaDataForDateType(List<Map<String, String>> gaData, String field, String value, String metric) {
        for (Iterator<Map<String, String>> iterator = gaData.iterator(); iterator.hasNext();) {
            Map<String, String> data = iterator.next();
            //return data.get(metric);
            if (data.get(field).equalsIgnoreCase(value)) {
                return data.get(metric);
            }
        }
        return "0";
    }

    private String getGaDataForDateAdType(List<Map<String, String>> gaData, String field, String value, String metric) {
        for (Iterator<Map<String, String>> iterator = gaData.iterator(); iterator.hasNext();) {
            Map<String, String> data = iterator.next();
            System.out.println("Matching value ---> " + value);
            System.out.println("Matching field ---> " + "Ad name: " + data.get(field));
            if (value.startsWith("Ad name: " + data.get(field))) {
                //if (data.get(field).indexOf(value) > 0) {
                System.out.println("Matching =====" + data.get(metric));
                return data.get(metric);
            }
        }
        return "";
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void handle(HttpMessageNotReadableException e) {
        e.printStackTrace();
    }
}
