/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.api.admin.controller;

import com.visumbu.api.admin.service.AdwordsService;
import com.visumbu.api.utils.DateUtils;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
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
 * @author Varghees Samraj
 */
@Controller
@RequestMapping("adwords")
public class AdwordsController {

    @Autowired
    private AdwordsService adwordsService;

    @RequestMapping(value = "getAdwords", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    Object testAdwords(HttpServletRequest request, HttpServletResponse response) {
        Date startDate = DateUtils.getStartDate(request.getParameter("startDate"));
        Date endDate = DateUtils.getEndDate(request.getParameter("endDate"));
        String accountId = request.getParameter("accountId");
        String fields[] = request.getParameterValues("fields");
        String aggregation = request.getParameter("aggregation");
        String reportType = request.getParameter("reportType");
        Map<String, String> filter = new HashMap<>();
        String filters[] = request.getParameterValues("filters");
        for (int i = 0; i < filters.length; i++) {
            String filterString = filters[i];
            String[] filterArr = filterString.split(",");
            filter.put(filterArr[0], filterArr[1]);
        }
        return adwordsService.adWordsAsMap(startDate, endDate, accountId, fields, filter, aggregation, reportType);
    }

    @RequestMapping(value = "getCampain", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    Object getCampain(HttpServletRequest request, HttpServletResponse response) {
        Date startDate = DateUtils.get30DaysBack();
        Date endDate = new Date();
        String accountId = "581-484-4675";
        return adwordsService.getCampaignReport(startDate, endDate, accountId, "CONTENT");
    }

    @RequestMapping(value = "getAccount", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    Object getAccount(HttpServletRequest request, HttpServletResponse response) {
        Date startDate = DateUtils.get30DaysBack();
        Date endDate = new Date();
        String accountId = "581-484-4675";
        return adwordsService.getAccountReport(startDate, endDate, accountId, "daily", "SEARCH");
    }

    @RequestMapping(value = "getAdGroup", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    Object getAdGroup(HttpServletRequest request, HttpServletResponse response) {
        Date startDate = DateUtils.get30DaysBack();
        Date endDate = new Date();
        String accountId = "581-484-4675";
        return adwordsService.getAdGroupReport(startDate, endDate, accountId, "", "SEARCH");
    }

    @RequestMapping(value = "getAccountDevice", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    Object getAccountDevice(HttpServletRequest request, HttpServletResponse response) {
        Date startDate = DateUtils.get30DaysBack();
        Date endDate = new Date();
        String accountId = "581-484-4675";
        return adwordsService.getAccountDevicePerformanceReport(startDate, endDate, accountId, "", "SEARCH");
    }

    @RequestMapping(value = "getCampaignDevice", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    Object getCampaignDevice(HttpServletRequest request, HttpServletResponse response) {
        Date startDate = DateUtils.get30DaysBack();
        Date endDate = new Date();
        String accountId = "581-484-4675";
        return adwordsService.getCampaignDeviceReport(startDate, endDate, accountId, "", "SEARCH");
    }

    @RequestMapping(value = "getCampaignPerformance", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    Object getCampaignPerformance(HttpServletRequest request, HttpServletResponse response) {
        Date startDate = DateUtils.get30DaysBack();
        Date endDate = new Date();
        String accountId = "581-484-4675";
        return adwordsService.getCampaignPerformanceReport(startDate, endDate, accountId, "SEARCH", "");
    }

    @RequestMapping(value = "getAdReport", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    Object getAdReport(HttpServletRequest request, HttpServletResponse response) {
        Date startDate = DateUtils.get30DaysBack();
        Date endDate = new Date();
        String accountId = "581-484-4675";
        return adwordsService.getAdReport(startDate, endDate, accountId, "", "SEARCH");
    }

    @RequestMapping(value = "getAccountHourOfDayReport", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    Object getAccountHourOfDayReport(HttpServletRequest request, HttpServletResponse response) {
        Date startDate = DateUtils.get30DaysBack();
        Date endDate = new Date();
        String accountId = "581-484-4675";
        return adwordsService.getAccountHourOfDayReport(startDate, endDate, accountId, "SEARCH");
    }

    @RequestMapping(value = "getAccountDayOfWeekReport", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    Object getAccountDayOfWeekReport(HttpServletRequest request, HttpServletResponse response) {
        Date startDate = DateUtils.get30DaysBack();
        Date endDate = new Date();
        String accountId = "581-484-4675";
        return adwordsService.getAccountDayOfWeekReport(startDate, endDate, accountId, "SEARCH");
    }

    @RequestMapping(value = "getGeoReport", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    Object getGeoReport(HttpServletRequest request, HttpServletResponse response) {
        Date startDate = DateUtils.get30DaysBack();
        Date endDate = new Date();
        String accountId = "581-484-4675";
        return adwordsService.getGeoReport(startDate, endDate, accountId, "", "SEARCH");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void handle(HttpMessageNotReadableException e) {
        e.printStackTrace();
    }
}
