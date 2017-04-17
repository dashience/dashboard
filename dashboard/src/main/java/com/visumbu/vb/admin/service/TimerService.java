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
import com.visumbu.vb.model.Report;
import com.visumbu.vb.model.Scheduler;
import com.visumbu.vb.model.SchedulerHistory;
import com.visumbu.vb.utils.DateUtils;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
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
            schedulerHistory.setExecutionStartTime(schedulerStartTime);
            String currentDateStr = DateUtils.dateToString(new Date(), "dd/MM/yyyy HH:mm:ss");
            Date startDate = DateUtils.getSixMonthsBack(today);
            Date endDate = today;
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
            System.out.println("exportType: "+exportType);
            String startDateStr = URLEncoder.encode(DateUtils.dateToString(startDate, "MM/dd/yyyy"), "UTF-8");
            String endDateStr = URLEncoder.encode(DateUtils.dateToString(endDate, "MM/dd/yyyy"), "UTF-8");

//            String urlStr = "http://localhost:8082/testing/admin/proxy/downloadReport/" + reportId + "?dealerId=" + accountId + "&exportType=" + exportType + "&startDate=" + startDateStr + "&endDate=" + endDateStr + "&location=" + accountId + "&accountId=" + accountId;
            String urlStr = "http://localhost:8084/dashboard/admin/proxy/downloadReport/" + reportId + "?dealerId=" + accountId + "&exportType=" + exportType + "&startDate=" + startDateStr + "&endDate=" + endDateStr + "&location=" + accountId + "&accountId=" + accountId;
            System.out.println(urlStr);
            URL website = new URL(urlStr);

            File file = new File(filename);
            System.out.println("filename: "+filename);
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
