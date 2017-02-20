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
@RequestMapping("socialImpact")
public class SocialImpactTabController {

    @Autowired
    private UserService userService;

    @Autowired
    private BingService bingService;

    @Autowired
    private AdwordsService adwordsService;

    @Autowired
    private FacebookService facebookService;

    @RequestMapping(value = "postPerformance", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    Object getPostPerformance(HttpServletRequest request, HttpServletResponse response) {
        Date startDate = DateUtils.getStartDate(request.getParameter("startDate"));
        Date endDate = DateUtils.getEndDate(request.getParameter("endDate"));

        String fieldsOnly = request.getParameter("fieldsOnly");
        Map returnMap = new HashMap();
        List<ColumnDef> columnDefs = new ArrayList<>();
        columnDefs.add(new ColumnDef("message", "string", "Posts"));
        columnDefs.add(new ColumnDef("type", "string", "Type"));
        columnDefs.add(new ColumnDef("shares", "number", "Shares", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
        columnDefs.add(new ColumnDef("comments", "number", "Comments", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
        columnDefs.add(new ColumnDef("likes", "number", "Likes", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
        columnDefs.add(new ColumnDef("reactions", "number", "Reactions", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
        columnDefs.add(new ColumnDef("engagements", "number", "Engagements", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
        columnDefs.add(new ColumnDef("created_time", "date", "Created Time"));

        returnMap.put("columnDefs", columnDefs);
        if (fieldsOnly != null) {
            return returnMap;
        }
        returnMap.put("data", null);
        AccountDetails accountDetails = ApiUtils.toAccountDetails(request, "none");
        System.out.println("Facebook  SM Account Id " + accountDetails.getFacebookSmAccountId());
        if (accountDetails.getFacebookSmAccountId() != null) {
            Object accountPerformance = facebookService.getPostPerformance(accountDetails.getFacebookSmAccountId(), startDate, endDate);
            returnMap.put("data", accountPerformance);
        }
        return returnMap;
    }

    @RequestMapping(value = "postPerformanceByType", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    Object getPostPerformanceByType(HttpServletRequest request, HttpServletResponse response) {
        Date startDate = DateUtils.getStartDate(request.getParameter("startDate"));
        Date endDate = DateUtils.getEndDate(request.getParameter("endDate"));

        String fieldsOnly = request.getParameter("fieldsOnly");
        Map returnMap = new HashMap();
        List<ColumnDef> columnDefs = new ArrayList<>();
        columnDefs.add(new ColumnDef("type", "string", "Type"));
        columnDefs.add(new ColumnDef("shares", "number", "Shares", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
        columnDefs.add(new ColumnDef("comments", "number", "Comments", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
        columnDefs.add(new ColumnDef("likes", "number", "Likes", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
        columnDefs.add(new ColumnDef("reactions", "number", "Reactions", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
        columnDefs.add(new ColumnDef("engagements", "number", "Engagements", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
        returnMap.put("columnDefs", columnDefs);
        if (fieldsOnly != null) {
            return returnMap;
        }
        returnMap.put("data", null);
        AccountDetails accountDetails = ApiUtils.toAccountDetails(request, "none");
        if (accountDetails.getFacebookSmAccountId() != null) {
            List<Map<String, String>> accountPerformance = facebookService.getPostPerformance(accountDetails.getFacebookSmAccountId(), startDate, endDate);
            returnMap.put("data", sumByType(accountPerformance));
        }
        return returnMap;
    }

    public static List<Map<String, String>> sumByType(List<Map<String, String>> list) {

        Map<String, Map<String, String>> map = new HashMap<>();
        for (Map<String, String> p : list) {
            String name = p.get("type");
            Map<String, String> sum = map.get(name);
            if (sum == null) {
                sum = new HashMap<String, String>();
                map.put(name, sum);
            }
            sum.put("type", name);
            sum.put("shares", (ApiUtils.toInteger(p.get("shares")) + ApiUtils.toInteger(sum.get("shares"))) + "");
            sum.put("comments", (ApiUtils.toInteger(p.get("comments")) + ApiUtils.toInteger(sum.get("comments"))) + "");
            sum.put("likes", (ApiUtils.toDouble(p.get("likes")) + ApiUtils.toDouble(sum.get("likes"))) + "");
            sum.put("reactions", (ApiUtils.toDouble(p.get("reactions")) + ApiUtils.toDouble(sum.get("reactions"))) + "");
            sum.put("engagements", (ApiUtils.toDouble(p.get("engagements")) + ApiUtils.toDouble(sum.get("engagements"))) + "");

        }
        return new ArrayList<Map<String, String>>(map.values());
    }

    @RequestMapping(value = "postSummary", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    Object getPostSummary(HttpServletRequest request, HttpServletResponse response) {
        Date startDate = DateUtils.getStartDate(request.getParameter("startDate"));
        Date endDate = DateUtils.getEndDate(request.getParameter("endDate"));

        String fieldsOnly = request.getParameter("fieldsOnly");
        Map returnMap = new HashMap();
        List<ColumnDef> columnDefs = new ArrayList<>();
        columnDefs.add(new ColumnDef("shares", "number", "Shares", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
        columnDefs.add(new ColumnDef("comments", "number", "Comments", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
        columnDefs.add(new ColumnDef("likes", "number", "Likes", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
        columnDefs.add(new ColumnDef("reactions", "number", "Reactions", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
        columnDefs.add(new ColumnDef("engagements", "number", "Engagements", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
        returnMap.put("columnDefs", columnDefs);
        if (fieldsOnly != null) {
            return returnMap;
        }
        AccountDetails accountDetails = ApiUtils.toAccountDetails(request, "none");
        if (accountDetails.getFacebookSmAccountId() != null) {
            List<Map<String, String>> accountPerformance = facebookService.getPostPerformance(accountDetails.getFacebookSmAccountId(), startDate, endDate);
            returnMap.put("data", sumBySummary(accountPerformance));
        } else {
            returnMap.put("data", null);

        }

        return returnMap;
    }

    public static List<Map<String, String>> sumBySummary(List<Map<String, String>> list) {

        Map<String, Map<String, String>> map = new HashMap<>();
        for (Map<String, String> p : list) {
            String name = "summary";
            Map<String, String> sum = map.get(name);
            if (sum == null) {
                sum = new HashMap<String, String>();
                map.put(name, sum);
            }
            // sum.put("type", name);
            sum.put("shares", (ApiUtils.toInteger(p.get("shares")) + ApiUtils.toInteger(sum.get("shares"))) + "");
            sum.put("comments", (ApiUtils.toInteger(p.get("comments")) + ApiUtils.toInteger(sum.get("comments"))) + "");
            sum.put("likes", (ApiUtils.toDouble(p.get("likes")) + ApiUtils.toDouble(sum.get("likes"))) + "");
            sum.put("reactions", (ApiUtils.toDouble(p.get("reactions")) + ApiUtils.toDouble(sum.get("reactions"))) + "");
            sum.put("engagements", (ApiUtils.toDouble(p.get("engagements")) + ApiUtils.toDouble(sum.get("engagements"))) + "");

        }
        return new ArrayList<Map<String, String>>(map.values());
    }

    @RequestMapping(value = "last12WeeksPerformance", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    Object getLast12WeeksPerformance(HttpServletRequest request, HttpServletResponse response) {
        Date startDate = DateUtils.get12WeeksBack(request.getParameter("endDate"));
        Date endDate = DateUtils.getEndDate(request.getParameter("endDate"));

        String fieldsOnly = request.getParameter("fieldsOnly");
        Map returnMap = new HashMap();
        List<ColumnDef> columnDefs = new ArrayList<>();
        columnDefs.add(new ColumnDef("message", "string", "Message"));
        columnDefs.add(new ColumnDef("date", "date", "Week"));
        columnDefs.add(new ColumnDef("type", "string", "type"));
        columnDefs.add(new ColumnDef("shares", "number", "Shares", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
        columnDefs.add(new ColumnDef("comments", "number", "Comments", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
        columnDefs.add(new ColumnDef("likes", "number", "Likes", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
        columnDefs.add(new ColumnDef("reactions", "number", "Reactions", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
        columnDefs.add(new ColumnDef("engagements", "number", "Engagements", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
        columnDefs.add(new ColumnDef("created_time", "date", "Created Time"));

        returnMap.put("columnDefs", columnDefs);
        if (fieldsOnly != null) {
            return returnMap;
        }
        returnMap.put("data", null);
        AccountDetails accountDetails = ApiUtils.toAccountDetails(request, "facebook");
        if (accountDetails.getFacebookSmAccountId() != null) {
            Object accountPerformance = facebookService.getLast12WeeksPerformance(accountDetails.getFacebookSmAccountId(), startDate, endDate);
            returnMap.put("data", accountPerformance);
        }
        return returnMap;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void handle(HttpMessageNotReadableException e) {
        e.printStackTrace();
    }
}
