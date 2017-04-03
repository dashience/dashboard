/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.admin.controller;

import com.visumbu.vb.admin.service.SchedulerService;
import com.visumbu.vb.admin.service.UserService;
import com.visumbu.vb.bean.SchedulerBean;
import com.visumbu.vb.controller.BaseController;
import com.visumbu.vb.model.Scheduler;
import com.visumbu.vb.model.SchedulerHistory;
import com.visumbu.vb.model.VbUser;
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
 * @author duc-dev-04
 */
@Controller
@RequestMapping("scheduler")
public class SchedulerController extends BaseController{
    @Autowired
    private SchedulerService schedulerService;
    
    @Autowired
    private UserService userService;
    
    @RequestMapping(value = "scheduler", method = RequestMethod.POST, produces = "application/json")
    public @ResponseBody
    Scheduler createScheduler(HttpServletRequest request, HttpServletResponse response, @RequestBody SchedulerBean schedulerBean) {
        VbUser user = userService.findByUsername(getUser(request));
        schedulerBean.setAgencyId(user.getAgencyId());
        return schedulerService.createScheduler(schedulerBean);
    }

    @RequestMapping(value = "scheduler", method = RequestMethod.PUT, produces = "application/json")
    public @ResponseBody
    Scheduler updateScheduler(HttpServletRequest request, HttpServletResponse response, @RequestBody SchedulerBean schedulerBean) {
        VbUser user = userService.findByUsername(getUser(request));
        schedulerBean.setAgencyId(user.getAgencyId());
        return schedulerService.updateScheduler(schedulerBean);
    }
    
    @RequestMapping(value = "scheduler", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    List getScheduler(HttpServletRequest request, HttpServletResponse response) {
         VbUser user = userService.findByUsername(getUser(request));
        if (user == null) {
            return null;
        }
        return schedulerService.getScheduler(user);
    }
    
    @RequestMapping(value = "scheduler/{schedulerId}", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    Scheduler getSchedulerById(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer schedulerId) {
        return schedulerService.getSchedulerById(schedulerId);
    }
    @RequestMapping(value = "schedulerHistory/{schedulerId}", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    List getSchedulerHistoryById(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer schedulerId) {
        return schedulerService.getSchedulerHistoryById(schedulerId);
    }
    
//    @RequestMapping(value = "schedulerByReport/{reportId}", method = RequestMethod.GET, produces = "application/json")
//    public @ResponseBody
//    Scheduler getSchedulerByReportId(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer reportId) {
//        return schedulerService.getSchedulerByReportId(reportId);
//    }
    
    @RequestMapping(value = "scheduler/{schedulerId}", method = RequestMethod.DELETE, produces = "application/json")
    public @ResponseBody
    Scheduler deleteScheduler(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer schedulerId) {
        return schedulerService.deleteScheduler(schedulerId);
    }
    
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void handle(HttpMessageNotReadableException e) {
        e.printStackTrace();
    }
}
