/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.admin.service;

/**
 *
 * @author duc-dev-04
 */
import com.visumbu.mail.MailProperties;
import com.visumbu.mail.TextMailWithAttachment;
import com.visumbu.vb.admin.dao.SchedulerDao;
import com.visumbu.vb.admin.dao.UserDao;
import com.visumbu.vb.bean.DateRange;
import com.visumbu.vb.bean.Range;
import com.visumbu.vb.model.Report;
import com.visumbu.vb.model.Scheduler;
import com.visumbu.vb.model.SchedulerHistory;
import com.visumbu.vb.utils.DateUtils;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import test.DateRangeFactory;

@EnableScheduling
@Service("timeService")
public class TimerService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private SchedulerService schedulerService;

    @Autowired
    private SchedulerDao schedulerDao;

    public void executeTasks(List<Scheduler> scheduledTasks) {
        Date today = new Date();
//        DateRangeFactory dateRangeFactory = new DateRangeFactory();
        for (Iterator<Scheduler> iterator = scheduledTasks.iterator(); iterator.hasNext();) {
            Date schedulerStartTime = new Date();
            SchedulerHistory schedulerHistory = new SchedulerHistory();
            Scheduler scheduler = iterator.next();
            Report report = scheduler.getReportId();
            Integer schedulerId = scheduler.getId();
            Scheduler schedulerById = schedulerDao.getSchedulerById(schedulerId);
            String dealerId = scheduler.getAccountId().getId() + "";
            String accountMailId = scheduler.getAccountId().getEmailId();
            String exportType = scheduler.getSchedulerType();
            System.out.println("Last Execution Status -----> " + scheduler.getLastExecutionStatus());
            String dateRangeName = scheduler.getDateRangeName();
            System.out.println("Date Range Name ----> " + dateRangeName);
            System.out.println("scheduler lastndays ----> " + scheduler.getLastNdays());
            String currentDateStr = null;
            schedulerHistory.setExecutionStartTime(schedulerStartTime);
            currentDateStr = DateUtils.dateToString(new Date(), "dd/MM/yyyy HH:mm:ss");
            System.out.println("CurrentDateStr -----> " + currentDateStr);
            DateRange dateRange = null;
            Date startDate = null;
            Date endDate = null;
            SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
            Integer lastNdays = null;
            Integer lastNmonths = null;
            Integer lastNweeks = null;
            Integer lastNyears = null;

            if (dateRangeName != null) {

                System.out.println("scheduler.getLastNdays() ----> " + scheduler.getLastNdays());
                if (scheduler.getLastNdays() != null) {
                    lastNdays = scheduler.getLastNdays();
                    System.out.println("Last N days ----> " + lastNdays);
                } else if (dateRangeName.equalsIgnoreCase("Last 0 Days")) {
                    lastNdays = 0;
                }
                if (scheduler.getLastNmonths() != null) {
                    lastNmonths = scheduler.getLastNmonths();
                    System.out.println("Last N months ----> " + lastNmonths);
                } else if (dateRangeName.equalsIgnoreCase("Last 0 Months")) {
                    lastNmonths = 0;
                }
                if (scheduler.getLastNweeks() != null) {
                    lastNweeks = scheduler.getLastNweeks();
                    System.out.println("Last N weeks ----> " + lastNweeks);

                } else if (dateRangeName.equalsIgnoreCase("Last 0 Weeks")) {
                    lastNweeks = 0;
                }
                if (scheduler.getLastNyears() != null) {
                    lastNyears = scheduler.getLastNyears();
                    System.out.println("Last N years ----> " + lastNyears);
                } else if (dateRangeName.equalsIgnoreCase("Last 0 Years")) {
                    lastNyears = 0;
                }

                System.out.println("dateRangename ----> " + dateRangeName);

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
                    dateRangeSelect = Range.DAY;
                } else if (lastNweeks != null) {
                    dateRangeSelect = Range.WEEK;
                } else if (lastNmonths != null) {
                    dateRangeSelect = Range.MONTH;
                } else if (lastNyears != null) {
                    dateRangeSelect = Range.YEAR;
                }

                if (dateRangeSelect == null && dateRangeName.equalsIgnoreCase("Custom")) {
                    try {
                        startDate = df.parse(scheduler.getCustomStartDate());
                        endDate = df.parse(scheduler.getCustomEndDate());
                    } catch (ParseException ex) {
                        Logger.getLogger(TimerService.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else if (dateRangeSelect == null && dateRangeName.equalsIgnoreCase("Select Date Duration")) {
                    startDate = null;
                    endDate = null;
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
                    startDate = dateRange.getStartDate();
                    endDate = dateRange.getEndDate();
                }
            }

            System.out.println("dateRange start Date-----> " + startDate);
            System.out.println("dateRange End Date-----> " + endDate);
            schedulerHistory.setStartTime(startDate);
            schedulerHistory.setEndTime(endDate);

            String filename = "/tmp/" + scheduler.getSchedulerName() + "_" + currentDateStr + "." + exportType;
            filename = filename.replaceAll(" ", "_");
            String toAddress = accountMailId;

            if (toAddress != null && !toAddress.isEmpty()) {
                toAddress += "," + scheduler.getSchedulerEmail();
            } else {
                toAddress = scheduler.getSchedulerEmail();
            }
            System.out.println("TO Address============================================>");
            System.out.println(toAddress);
            String subject = "[ Scheduled Report ] " + scheduler.getSchedulerName() + " " + scheduler.getAccountId().getAccountName() + " " + currentDateStr;
            String message = subject + "\n\n- System";
            Boolean schedulerStatus = downloadReportAndSend(startDate, endDate, dealerId, exportType, report.getId(), filename, toAddress, subject, message);
            schedulerHistory.setFileName(filename);
            schedulerHistory.setEmailId(toAddress);
            schedulerHistory.setEmailSubject(subject);
            schedulerHistory.setEmailMessage(message);
            scheduler.setLastExecutionStatus(new Date() + " " + (schedulerStatus ? "Success" : "Failed"));
            schedulerDao.update(scheduler);
            schedulerHistory.setStatus(schedulerStatus ? "Success" : "Failed");
            Date schedulerEndTime = new Date();
            schedulerHistory.setExecutionEndTime(schedulerEndTime);
            schedulerHistory.setSchedulerId(schedulerById);
            schedulerHistory.setSchedulerName(schedulerById.getSchedulerName());
            schedulerService.createSchedulerHistory(schedulerHistory);
        }
    }

    @Scheduled(cron = "0 0 */1 * * *")
    public void executeDailyTasks() {
        Integer hour = DateUtils.getCurrentHour();
        Date today = new Date();
        List<Scheduler> scheduledTasks = schedulerDao.getDailyTasks(hour, today); //schedulerDao.getScheduledTasks("Daily");
        executeTasks(scheduledTasks);
    }

    @Scheduled(cron = "0 0 */1 * * *")
    public void executeWeeklyTask() {
        Integer hour = DateUtils.getCurrentHour();
        Date today = new Date();
        String weekDayToday = DateUtils.getDayOfWeek(DateUtils.getCurrentWeekDay());
        List<Scheduler> scheduledTasks = schedulerDao.getWeeklyTasks(hour, weekDayToday, today);
        executeTasks(scheduledTasks);
    }

    @Scheduled(cron = "0 0 */1 * * *")
    public void executeMonthlyTask() {
        Date today = new Date();
        String currentDateHour = DateUtils.dateToString(new Date(), "MM/dd/yyyy HH:00");
        List<Scheduler> scheduledTasks = schedulerDao.getMonthlyTasks(currentDateHour, today);
        executeTasks(scheduledTasks);
    }

    @Scheduled(cron = "0 0 */1 * * *")
    public void executeYearlyTask() {
//         Integer hour = DateUtils.getCurrentHour();
        System.out.println("Yearly Tasks");
        Date today = new Date();
        String currentDateHour = DateUtils.dateToString(new Date(), "MM/dd/yyyy HH:00");
        System.out.println(currentDateHour);
        List<Scheduler> scheduledTasks = schedulerDao.getYearlyTasks(currentDateHour, today);
        System.out.println(scheduledTasks);
//        executeTasks(scheduledTasks);
    }

    @Scheduled(cron = "0 0 */1 * * *")
    public void executeYearOfWeek() {
        Date today = new Date();
        Integer hour = DateUtils.getCurrentHour();
        Integer currentYearOfWeekCount = DateUtils.getYearOfWeek();
        String weekDayToday = DateUtils.getDayOfWeek(DateUtils.getCurrentWeekDay());
        List<Scheduler> scheduledTasks = schedulerDao.getYearOfWeekTasks(hour, weekDayToday, currentYearOfWeekCount, today);
        executeTasks(scheduledTasks);

    }

    @Scheduled(cron = "0 0 */1 * * *")
    public void executeOnce() {
        Integer hour = DateUtils.getCurrentHour();
        Date today = new Date();
        System.out.println("Once");
        List<Scheduler> scheduledTasks = schedulerDao.getOnce(hour, today);
        System.out.println("Once 1");
        executeTasks(scheduledTasks);
    }

    private Boolean downloadReportAndSend(Date startDate, Date endDate,
            String accountId, String exportType, Integer reportId, String filename,
            String to, String subject, String message) {
        try {
            System.out.println("exportType: " + exportType);
            String startDateStr = URLEncoder.encode(DateUtils.dateToString(startDate, "MM/dd/yyyy"), "UTF-8");
            String endDateStr = URLEncoder.encode(DateUtils.dateToString(endDate, "MM/dd/yyyy"), "UTF-8");

//            String urlStr = "http://192.168.0.100:8080/dashboard/admin/proxy/downloadReport/" + reportId + "?dealerId=" + accountId + "&exportType=" + exportType + "&startDate=" + startDateStr + "&endDate=" + endDateStr + "&location=" + accountId + "&accountId=" + accountId;
            String urlStr = "http://localhost:8084/dashboard/admin/proxy/downloadReport/" + reportId + "?dealerId=" + accountId + "&exportType=" + exportType + "&startDate=" + startDateStr + "&endDate=" + endDateStr + "&location=" + accountId + "&accountId=" + accountId;
            System.out.println(urlStr);
            URL website = new URL(urlStr);

            File file = new File(filename);
            System.out.println("filename: " + filename);
            FileUtils.copyURLToFile(website, file);
            MailProperties mailProps = new MailProperties();
            TextMailWithAttachment sender = new TextMailWithAttachment(mailProps);
            String[] attachments = {filename};
            System.out.println("Sending mail to " + to);
            sender.sendMail(to, subject, message, Arrays.asList(attachments));
        } catch (IOException ex) {
            Logger.getLogger(TimerService.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }

}
