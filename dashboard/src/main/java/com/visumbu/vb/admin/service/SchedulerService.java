/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.admin.service;

import com.visumbu.vb.admin.dao.SchedulerDao;
import com.visumbu.vb.bean.DateRange;
import com.visumbu.vb.bean.Range;
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
import test.DateRangeFactory;

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
        System.out.println("save scheduler");
        Scheduler scheduler = new Scheduler();
        String dateRangeName = schedulerBean.getDateRangeName();
        DateRange dateRange = null;
        String customStartDate = null;
        String customEndDate = null;
        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        Integer lastNdays = null;
        Integer lastNmonths = null;
        Integer lastNweeks = null;
        Integer lastNyears = null;
        if (dateRangeName != null) {
            if (schedulerBean.getLastNdays() != null) {
                lastNdays = schedulerBean.getLastNdays();
                System.out.println("Last N days ----> " + lastNdays);
            }
            if (dateRangeName.equalsIgnoreCase("Last 0 Days")) {
                lastNdays = 0;
            }
            if (schedulerBean.getLastNmonths() != null) {
                lastNmonths = schedulerBean.getLastNmonths();
                System.out.println("Last N months ----> " + lastNmonths);
            }
            if (dateRangeName.equalsIgnoreCase("Last 0 Months")) {
                lastNmonths = 0;
            }
            if (schedulerBean.getLastNweeks() != null) {
                lastNweeks = schedulerBean.getLastNweeks();
                System.out.println("Last N weeks ----> " + lastNweeks);

            }
            if (dateRangeName.equalsIgnoreCase("Last 0 Weeks")) {
                lastNweeks = 0;
            }
            if (schedulerBean.getLastNyears() != null) {
                lastNyears = schedulerBean.getLastNyears();
                System.out.println("Last N years ----> " + lastNyears);
            }
            if (dateRangeName.equalsIgnoreCase("Last 0 Years")) {
                lastNyears = 0;
            }

//            Date startDate = DateUtils.getSixMonthsBack(today);
//            System.out.println("Start Date -----> " + startDate);
//            Date endDate = today;
//            System.out.println("End Date -----> " + endDate);
            Range dateRangeSelect = null;
//            if (dateRangeName.equalsIgnoreCase("Today")) {
//                dateRangeSelect = Range.TODAY;
//            } else if (dateRangeName.equalsIgnoreCase("Yesterday")) {
//                dateRangeSelect = Range.YESTERDAY;
//            } else if (dateRangeName.equalsIgnoreCase("This Week")) {
//                dateRangeSelect = Range.THIS_WEEK;
//            } else if (dateRangeName.equalsIgnoreCase("Last Week")) {
//                dateRangeSelect = Range.LAST_WEEK;
//            } else if (dateRangeName.equalsIgnoreCase("This Month")) {
//                dateRangeSelect = Range.THIS_MONTH;
//            } else if (dateRangeName.equalsIgnoreCase("Last Month")) {
//                dateRangeSelect = Range.LAST_MONTH;
//            } else if (dateRangeName.equalsIgnoreCase("This Year")) {
//                dateRangeSelect = Range.THIS_YEAR;
//            } else if (dateRangeName.equalsIgnoreCase("Last Year")) {
//                dateRangeSelect = Range.LAST_YEAR;
//            } 
            if (lastNdays != null) {
                System.out.println("last days");
                dateRangeSelect = Range.DAY;
            } else if (lastNweeks != null) {
                dateRangeSelect = Range.WEEK;
            } else if (lastNmonths != null) {
                dateRangeSelect = Range.MONTH;
            } else if (lastNyears != null) {
                dateRangeSelect = Range.YEAR;
            }

            if (dateRangeSelect == null && dateRangeName.equalsIgnoreCase("Custom")) {
                customStartDate = schedulerBean.getCustomStartDate();
                customEndDate = schedulerBean.getCustomEndDate();
            } else if (dateRangeSelect == null && dateRangeName.equalsIgnoreCase("Select Date Duration")) {
                customStartDate = schedulerBean.getCustomStartDate();
                customEndDate = schedulerBean.getCustomEndDate();
            } else if (dateRangeSelect.equals(Range.DAY)) {
                dateRange = DateRangeFactory.getRange(dateRangeSelect, lastNdays);
            } else if (dateRangeSelect.equals(Range.WEEK)) {
                dateRange = DateRangeFactory.getRange(dateRangeSelect, lastNweeks);
            } else if (dateRangeSelect.equals(Range.MONTH)) {
                dateRange = DateRangeFactory.getRange(dateRangeSelect, lastNmonths);
            } else if (dateRangeSelect.equals(Range.YEAR)) {
                dateRange = DateRangeFactory.getRange(dateRangeSelect, lastNyears);
            } else {
                dateRange = DateRangeFactory.getRange(dateRangeSelect);
            }

            if (dateRange != null) {
                customStartDate = df.format(dateRange.getStartDate());
                customEndDate = df.format(dateRange.getEndDate());
            }
        }

        System.out.println("dateRange custom start Date-----> " + customStartDate);
        System.out.println("dateRange custom End Date-----> " + customEndDate);

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
        scheduler.setCustomStartDate(customStartDate);
        scheduler.setCustomEndDate(customEndDate);
        scheduler.setDateRangeName(schedulerBean.getDateRangeName());
        scheduler.setLastNdays(lastNdays);
        scheduler.setLastNmonths(lastNmonths);
        scheduler.setLastNweeks(lastNweeks);
        scheduler.setLastNyears(lastNyears);
        scheduler.setSchedulerEmail(schedulerBean.getSchedulerEmail());
        scheduler.setIsAccountEmail(schedulerBean.getIsAccountEmail());
        scheduler.setLastExecutionStatus(schedulerBean.getLastExecutionStatus());
        scheduler.setStatus(schedulerBean.getStatus());
        scheduler.setReportId(schedulerBean.getReportId());
        scheduler.setAgencyId(schedulerBean.getAgencyId());
        scheduler.setAccountId(schedulerBean.getAccountId());

        return (Scheduler) schedulerDao.create(scheduler);
    }

    public Scheduler updateScheduler(SchedulerBean schedulerBean) {
        System.out.println("update scheduler");
        Scheduler scheduler = new Scheduler();
        scheduler.setId(schedulerBean.getId());
        String dateRangeName = schedulerBean.getDateRangeName();
        System.out.println("dateRangeName ---> " + dateRangeName);
        System.out.println("last N Days ----> " + schedulerBean.getLastNdays());
        DateRange dateRange = null;
        String customStartDate = null;
        String customEndDate = null;
        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        Integer lastNdays = null;
        Integer lastNmonths = null;
        Integer lastNweeks = null;
        Integer lastNyears = null;
        if (dateRangeName != null) {
            System.out.println("else if");
            if (schedulerBean.getLastNdays() != null) {
                lastNdays = schedulerBean.getLastNdays();
                System.out.println("Last N days ----> " + lastNdays);
            }
            if (dateRangeName.equalsIgnoreCase("Last 0 Days")) {
                lastNdays = 0;
            }
            if (schedulerBean.getLastNmonths() != null) {
                lastNmonths = schedulerBean.getLastNmonths();
                System.out.println("Last N months ----> " + lastNmonths);
            }
            if (dateRangeName.equalsIgnoreCase("Last 0 Months")) {
                lastNmonths = 0;
            }
            if (schedulerBean.getLastNweeks() != null) {
                lastNweeks = schedulerBean.getLastNweeks();
                System.out.println("Last N weeks ----> " + lastNweeks);

            }
            if (dateRangeName.equalsIgnoreCase("Last 0 Weeks")) {
                lastNweeks = 0;
            }
            if (schedulerBean.getLastNyears() != null) {
                lastNyears = schedulerBean.getLastNyears();
                System.out.println("Last N years ----> " + lastNyears);
            }
            if (dateRangeName.equalsIgnoreCase("Last 0 Years")) {
                lastNyears = 0;
            }

//            Date startDate = DateUtils.getSixMonthsBack(today);
//            System.out.println("Start Date -----> " + startDate);
//            Date endDate = today;
//            System.out.println("End Date -----> " + endDate);
            Range dateRangeSelect = null;
//            if (dateRangeName.equalsIgnoreCase("Today")) {
//                dateRangeSelect = Range.TODAY;
//            } else if (dateRangeName.equalsIgnoreCase("Yesterday")) {
//                dateRangeSelect = Range.YESTERDAY;
//            } else if (dateRangeName.equalsIgnoreCase("This Week")) {
//                dateRangeSelect = Range.THIS_WEEK;
//            } else if (dateRangeName.equalsIgnoreCase("Last Week")) {
//                dateRangeSelect = Range.LAST_WEEK;
//            } else if (dateRangeName.equalsIgnoreCase("This Month")) {
//                dateRangeSelect = Range.THIS_MONTH;
//            } else if (dateRangeName.equalsIgnoreCase("Last Month")) {
//                dateRangeSelect = Range.LAST_MONTH;
//            } else if (dateRangeName.equalsIgnoreCase("This Year")) {
//                dateRangeSelect = Range.THIS_YEAR;
//            } else if (dateRangeName.equalsIgnoreCase("Last Year")) {
//                dateRangeSelect = Range.LAST_YEAR;
//            } 
            if (lastNdays != null) {
                System.out.println("last days");
                dateRangeSelect = Range.DAY;
            } else if (lastNweeks != null) {
                dateRangeSelect = Range.WEEK;
            } else if (lastNmonths != null) {
                dateRangeSelect = Range.MONTH;
            } else if (lastNyears != null) {
                dateRangeSelect = Range.YEAR;
            }

            if (dateRangeSelect == null && dateRangeName.equalsIgnoreCase("Custom")) {
                customStartDate = schedulerBean.getCustomStartDate();
                customEndDate = schedulerBean.getCustomEndDate();
            } else if (dateRangeSelect == null && dateRangeName.equalsIgnoreCase("Select Date Duration")) {
                customStartDate = schedulerBean.getCustomStartDate();
                customEndDate = schedulerBean.getCustomEndDate();
            } else if (dateRangeSelect.equals(Range.DAY)) {
                System.out.println("lastNdays ---> " + lastNdays);
                dateRange = DateRangeFactory.getRange(dateRangeSelect, lastNdays);
            } else if (dateRangeSelect.equals(Range.WEEK)) {
                dateRange = DateRangeFactory.getRange(dateRangeSelect, lastNweeks);
            } else if (dateRangeSelect.equals(Range.MONTH)) {
                dateRange = DateRangeFactory.getRange(dateRangeSelect, lastNmonths);
            } else if (dateRangeSelect.equals(Range.YEAR)) {
                dateRange = DateRangeFactory.getRange(dateRangeSelect, lastNyears);
            } else {
                System.out.println("without count");
                dateRange = DateRangeFactory.getRange(dateRangeSelect);
            }

            if (dateRange != null) {
                System.out.println("dateRange start Date---> " + dateRange.getStartDate());
                customStartDate = df.format(dateRange.getStartDate());
                System.out.println("dateRange end Date---> " + dateRange.getEndDate());
                customEndDate = df.format(dateRange.getEndDate());
            }
        }

        System.out.println("dateRange custom start Date-----> " + customStartDate);
        System.out.println("dateRange custom End Date-----> " + customEndDate);
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
        scheduler.setCustomStartDate(customStartDate);
        scheduler.setCustomEndDate(customEndDate);
        scheduler.setDateRangeName(schedulerBean.getDateRangeName());
        scheduler.setLastNdays(lastNdays);
        scheduler.setLastNmonths(lastNmonths);
        scheduler.setLastNweeks(lastNweeks);
        scheduler.setLastNyears(lastNyears);
        scheduler.setSchedulerEmail(schedulerBean.getSchedulerEmail());
        scheduler.setIsAccountEmail(schedulerBean.getIsAccountEmail());
        scheduler.setLastExecutionStatus(schedulerBean.getLastExecutionStatus());
        scheduler.setStatus(schedulerBean.getStatus());
        scheduler.setReportId(schedulerBean.getReportId());
        scheduler.setAgencyId(schedulerBean.getAgencyId());
        scheduler.setAccountId(schedulerBean.getAccountId());

        return (Scheduler) schedulerDao.update(scheduler);
    }

    public List<SchedulerHistory> getSchedulerHistoryById(Integer schedulerId) {
        return schedulerDao.getSchedulerHistoryById(schedulerId);
    }

    public Scheduler updateSchedulerEnableDisable(Scheduler scheduler) {
        return schedulerDao.updateSchedulerEnableDisable(scheduler);
    }

}
