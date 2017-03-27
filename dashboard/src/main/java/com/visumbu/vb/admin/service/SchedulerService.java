/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.admin.service;

import com.visumbu.vb.admin.dao.SchedulerDao;
import com.visumbu.vb.bean.SchedulerBean;
import com.visumbu.vb.model.Scheduler;
import com.visumbu.vb.model.SchedulerHistory;
import com.visumbu.vb.model.VbUser;
import com.visumbu.vb.utils.DateUtils;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author duc-dev-04
 */
@Service("schedulerService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class SchedulerService {
    
    @Autowired
    private SchedulerDao schedulerDao;

//    public Scheduler createScheduler(Scheduler scheduler) {
//        return (Scheduler) schedulerDao.create(scheduler);
//    }

//    public Scheduler updateScheduler(Scheduler scheduler) {
//        return (Scheduler) schedulerDao.update(scheduler);
//    }

    public List getScheduler(VbUser user) {
        if (user.getAgencyId() == null) {
            List<Scheduler> scheduler = schedulerDao.read(Scheduler.class);
            return scheduler;
        }
        return schedulerDao.getAgencyScheduler(user);
    }

    public Scheduler getSchedulerById(Integer schedulerId) {
        return schedulerDao.getSchedulerById(schedulerId);
    }

    public Scheduler deleteScheduler(Integer schedulerId) {
       return schedulerDao.deleteScheduler(schedulerId);
    }
    
    public SchedulerHistory createSchedulerHistory(SchedulerHistory schedulerHistory) {
        return (SchedulerHistory) schedulerDao.create(schedulerHistory);
    }

    public Scheduler createScheduler(SchedulerBean schedulerBean) {
       
        Scheduler scheduler = new Scheduler();
        Date startDate = DateUtils.toDate(schedulerBean.getStartDate(), "MM/dd/yyyy");
        Date endDate = DateUtils.toDate(schedulerBean.getEndDate(), "MM/dd/yyyy");
        System.out.println(startDate);
        scheduler.setStartDate(startDate);
        scheduler.setEndDate(endDate);
        
        scheduler.setSchedulerName(schedulerBean.getSchedulerName());
        scheduler.setSchedulerWeekly(schedulerBean.getSchedulerWeekly());
        scheduler.setSchedulerTime(schedulerBean.getSchedulerTime());
        scheduler.setSchedulerMonthly(schedulerBean.getSchedulerMonthly());
        scheduler.setSchedulerYearOfWeek(schedulerBean.getSchedulerYearOfWeek());
        scheduler.setSchedulerRepeatType(schedulerBean.getSchedulerRepeatType());
        scheduler.setSchedulerNow(schedulerBean.getSchedulerNow());
        scheduler.setSchedulerYearly(schedulerBean.getSchedulerYearly());
        scheduler.setSchedulerStatus(schedulerBean.getSchedulerStatus());
        scheduler.setSchedulerType(schedulerBean.getSchedulerType());
        scheduler.setCustomStartDate(schedulerBean.getCustomStartDate());
        scheduler.setCustomEndDate(schedulerBean.getCustomEndDate());
        scheduler.setDateRangeName(schedulerBean.getDateRangeName());
        scheduler.setLastNdays(schedulerBean.getLastNdays());
        scheduler.setLastNmonths(schedulerBean.getLastNmonths());
        scheduler.setLastNweeks(schedulerBean.getLastNweeks());
        scheduler.setLastNyears(schedulerBean.getLastNyears());
        scheduler.setSchedulerEmail(schedulerBean.getSchedulerEmail());
        scheduler.setIsAccountEmail(schedulerBean.getIsAccountEmail());
        scheduler.setReportId(schedulerBean.getReportId());
        scheduler.setAgencyId(schedulerBean.getAgencyId());
        scheduler.setAccountId(schedulerBean.getAccountId());
        
        return (Scheduler) schedulerDao.create(scheduler);
    }

    public Scheduler updateScheduler(SchedulerBean schedulerBean) {
        Scheduler scheduler = new Scheduler();
        scheduler.setId(schedulerBean.getId());
        
        Date startDate = DateUtils.toDate(schedulerBean.getStartDate(), "MM/dd/yyyy");
        Date endDate = DateUtils.toDate(schedulerBean.getEndDate(), "MM/dd/yyyy");
        scheduler.setStartDate(startDate);
        scheduler.setEndDate(endDate);
        
        scheduler.setSchedulerName(schedulerBean.getSchedulerName());
        scheduler.setSchedulerWeekly(schedulerBean.getSchedulerWeekly());
        scheduler.setSchedulerTime(schedulerBean.getSchedulerTime());
        scheduler.setSchedulerMonthly(schedulerBean.getSchedulerMonthly());
        scheduler.setSchedulerYearOfWeek(schedulerBean.getSchedulerYearOfWeek());
        scheduler.setSchedulerRepeatType(schedulerBean.getSchedulerRepeatType());
        scheduler.setSchedulerNow(schedulerBean.getSchedulerNow());
        scheduler.setSchedulerYearly(schedulerBean.getSchedulerYearly());
        scheduler.setSchedulerStatus(schedulerBean.getSchedulerStatus());
        scheduler.setSchedulerType(schedulerBean.getSchedulerType());
        scheduler.setCustomStartDate(schedulerBean.getCustomStartDate());
        scheduler.setCustomEndDate(schedulerBean.getCustomEndDate());
        scheduler.setDateRangeName(schedulerBean.getDateRangeName());
        scheduler.setLastNdays(schedulerBean.getLastNdays());
        scheduler.setLastNmonths(schedulerBean.getLastNmonths());
        scheduler.setLastNweeks(schedulerBean.getLastNweeks());
        scheduler.setLastNyears(schedulerBean.getLastNyears());
        scheduler.setSchedulerEmail(schedulerBean.getSchedulerEmail());
        scheduler.setIsAccountEmail(schedulerBean.getIsAccountEmail());
        scheduler.setReportId(schedulerBean.getReportId());
        scheduler.setAgencyId(schedulerBean.getAgencyId());
        scheduler.setAccountId(schedulerBean.getAccountId());
        
        return (Scheduler) schedulerDao.update(scheduler);
    }
    
}
