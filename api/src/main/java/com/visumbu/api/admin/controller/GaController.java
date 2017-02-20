/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.api.admin.controller;

import com.google.api.services.analyticsreporting.v4.model.GetReportsResponse;
import com.visumbu.api.admin.service.BingService;
import com.visumbu.api.admin.service.GaService;
import com.visumbu.api.admin.service.UserService;
import com.visumbu.api.bean.LoginUserBean;
import com.visumbu.api.utils.DateUtils;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Date;
import java.util.List;
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
 * @author jp
 */
@Controller
@RequestMapping("ga")
public class GaController {

    @Autowired
    private GaService gaService;
    

    @RequestMapping(value = "getGa", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    Object getGa(HttpServletRequest request, HttpServletResponse response) throws IOException, GeneralSecurityException {
        Date startDate = DateUtils.getStartDate(request.getParameter("startDate"));
        Date endDate = DateUtils.getEndDate(request.getParameter("endDate"));
        String metrics = request.getParameter("metrics"); // "ga:sessions,sessions;ga:visits,visits";
        String dimensions = request.getParameter("dimensions"); // "ga:browser";
        String filter = request.getParameter("filter"); //"ga:channelGrouping==Display;ga:medium==cpc";
        String raw = request.getParameter("_raw"); //"ga:channelGrouping==Display;ga:medium==cpc";
        filter=filter.replaceAll(",", "==");
        GetReportsResponse gaData = gaService.getGenericData("82176546", startDate, endDate, null, null, metrics, dimensions, filter);
        if(raw != null) {
            return gaData;
        }
        return gaService.getResponseAsMap(gaData);
    }
    
    @RequestMapping(value = "testGa", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    Object getAllDataSets(HttpServletRequest request, HttpServletResponse response) throws IOException, GeneralSecurityException {
        Date startDate = DateUtils.get30DaysBack();
        Date endDate = new Date();
        String metrics = "ga:sessions,sessions;ga:visits,visits";
        String dimensions = "ga:browser";
        String filter = "ga:channelGrouping==Display;ga:medium==cpc";
        return gaService.getGenericData("82176546", startDate, endDate, null, null, metrics, dimensions, filter);
    }
    
    
    @RequestMapping(value = "seoPerformance", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    Object getSeoPerformance(HttpServletRequest request, HttpServletResponse response) throws IOException, GeneralSecurityException {
        Date startDate = DateUtils.get30DaysBack();
        Date endDate = new Date();
        return gaService.getSeoPerformance("","82176546", startDate, endDate, null, null);
    }
    
    
    
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void handle(HttpMessageNotReadableException e) {
        e.printStackTrace();
    }
}
