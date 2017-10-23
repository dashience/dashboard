/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.admin.controller;

import com.visumbu.vb.admin.service.ReportService;
import com.visumbu.vb.admin.service.DealerService;
import com.visumbu.vb.admin.service.UserService;
import com.visumbu.vb.bean.ReportPage;
import com.visumbu.vb.bean.TabWidgetBean;
import com.visumbu.vb.controller.BaseController;
import com.visumbu.vb.model.Report;
import com.visumbu.vb.model.ReportWidget;
import com.visumbu.vb.model.TabWidget;
import com.visumbu.vb.model.VbUser;
import com.visumbu.vb.utils.DateUtils;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
 * @author netphenix
 */
@Controller
@RequestMapping("report")
public class ReportController extends BaseController{

    @Autowired
    private ReportService reportService;
    @Autowired
    private UserService userService;

//    @RequestMapping(value = "visitDetails", method = RequestMethod.GET, produces = "application/json")
//    public @ResponseBody
//    List topDealersByVisit(HttpServletRequest request, HttpServletResponse response) {
//        Date startDate = DateUtils.getStartDate(request.getParameter("startDate"));
//        Date endDate = DateUtils.getEndDate(request.getParameter("endDate"));
//        ReportPage page = getPage(request);
//        return reportService.getVisitDetailedList(startDate, endDate, page);
//    }
//
//    private ReportPage getPage(HttpServletRequest request) {
//        ReportPage reportPage = new ReportPage();
//        if (request.getParameter("page") == null && request.getParameter("count") == null) {
//            return null;
//        }
//        Integer count = 50;
//        if (request.getParameter("count") != null) {
//            count = Integer.parseInt(request.getParameter("count"));
//        }
//        if (request.getParameter("page") != null) {
//            Integer start = 0;
//            Integer page = Integer.parseInt(request.getParameter("page"));
//            start = count * (page - 1);
//            reportPage.setStart(start);
//            reportPage.setPageNo(page);
//            reportPage.setCount(count);
//        }
//        return reportPage;
//    }
    
    @RequestMapping(value = "report", method = RequestMethod.POST, produces = "application/json")
    public @ResponseBody
    Report addReport(HttpServletRequest request, HttpServletResponse response, @RequestBody Report report) {
        VbUser user = userService.findByUsername(getUser(request));
        report.setAgencyId(user.getAgencyId());
        report.setCreatedBy(user);
        return reportService.addReport(report);
    }

    @RequestMapping(value = "report", method = RequestMethod.PUT, produces = "application/json")
    public @ResponseBody
    Report updateReport(HttpServletRequest request, HttpServletResponse response, @RequestBody Report report) {
        VbUser user = userService.findByUsername(getUser(request));
        report.setAgencyId(user.getAgencyId());
        report.setCreatedBy(user);
        return reportService.updateReport(report);
    }
    
    
    @RequestMapping(value = "getReport", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    List getReport(HttpServletRequest request, HttpServletResponse response) {
        VbUser user = userService.findByUsername(getUser(request));
        if (user == null) {
            return null;
        }
        return reportService.getAgencyReport(user);
    }
    
    @RequestMapping(value = "{reportId}", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    Report getReportById(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer reportId) {
        return reportService.getReportById(reportId);
    }
    
    @RequestMapping(value = "dbReportUpdateOrder/{reportId}", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    Object updateReportUpdateOrder(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer reportId) {
        String widgetOrder = request.getParameter("widgetOrder");
        reportService.updateReportOrder(reportId, widgetOrder);
        return null;
    }
    
    @RequestMapping(value = "{reportId}", method = RequestMethod.DELETE, produces = "application/json")
    public @ResponseBody
    Report deleteReport(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer reportId) {
        return reportService.deleteReport(reportId);
    }
    
    @RequestMapping(value = "reportWidget", method = RequestMethod.POST, produces = "application/json")
    public @ResponseBody
    ReportWidget createReportWidget(HttpServletRequest request, HttpServletResponse response, @RequestBody ReportWidget reportWidget) {
        return reportService.createReportWidget(reportWidget);
    }
//

    @RequestMapping(value = "reportWidget", method = RequestMethod.PUT, produces = "application/json")
    public @ResponseBody
    ReportWidget updateReportWidget(HttpServletRequest request, HttpServletResponse response, @RequestBody ReportWidget reportWidget) {
        return reportService.updateReportWidget(reportWidget);
    }
//

    @RequestMapping(value = "reportWidget", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    List getReportWidget(HttpServletRequest request, HttpServletResponse response) {
        return reportService.getReportWidget();
    }
//    

    @RequestMapping(value = "reportWidget/{reportId}", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    List getReportWidget(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer reportId) {
        return reportService.getReportWidget(reportId);
    }

    @RequestMapping(value = "reportWidget/{reportId}", method = RequestMethod.DELETE, produces = "application/json")
    public @ResponseBody
    ReportWidget deleteReportWidget(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer reportId) {
        return reportService.deleteReportWidget(reportId);
    }
    
    @RequestMapping(value = "dbWidget/{tabId}", method = RequestMethod.POST, produces = "application/json")
    public @ResponseBody
    TabWidget createTabWidget(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer tabId, @RequestBody TabWidgetBean tabWidget) {
        return reportService.saveTabWidget(tabId, tabWidget);
    }

    @RequestMapping(value = "dbWidget/{tabId}", method = RequestMethod.PUT, produces = "application/json")
    public @ResponseBody
    TabWidget updateTabWidget(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer tabId, @RequestBody TabWidgetBean tabWidget) {
        return reportService.saveTabWidget(tabId, tabWidget);
        //return null; //uiService.createTabWidget(tabId, tabWidget);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void handle(HttpMessageNotReadableException e) {
        e.printStackTrace();
    }
}
