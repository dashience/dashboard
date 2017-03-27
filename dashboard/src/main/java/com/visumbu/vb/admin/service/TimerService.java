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
@Service("cronService")
public class TimerService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private SchedulerService schedulerService;

    @Autowired
    private SchedulerDao schedulerDao;

    // @Scheduled(cron = "*/30 * * * * *")
    public void executeDailyTasks() {
        Integer hour = DateUtils.getCurrentHour();
        Date today = new Date();

        List<Scheduler> scheduledTasks = schedulerDao.getDailyTasks(hour, today); //schedulerDao.getScheduledTasks("Daily");
        System.out.println("TOTAL TASKS " + scheduledTasks.size());
        for (Iterator<Scheduler> iterator = scheduledTasks.iterator(); iterator.hasNext();) {
            System.out.println("Inside for --- ");
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
            String filename = "/tmp/" + scheduler.getSchedulerName() + "_" + currentDateStr + ".pdf";
            filename = filename.replaceAll(" ", "_");
            String toAddress = accountMailId;
            if (toAddress != null && !toAddress.isEmpty()) {
                toAddress += "," + scheduler.getSchedulerEmail();
            }
            String subject = "[ Scheduled Report ] " + scheduler.getSchedulerName() + " " + scheduler.getAccountId().getAccountName() + " " + currentDateStr;
            String message = "scheduler message";
            Boolean schedulerStatus = downloadReportAndSend(startDate, endDate, dealerId, exportType, report.getId(), filename, toAddress, subject, message);
            schedulerHistory.setFileName(filename);
            schedulerHistory.setEmailId(toAddress);
            schedulerHistory.setEmailSubject(subject);
            schedulerHistory.setEmailMessage(message);

            schedulerHistory.setStatus(schedulerStatus ? "Success" : "Failed");
            Date schedulerEndTime = new Date();
            schedulerHistory.setExecutionEndTime(schedulerEndTime);
            schedulerHistory.setSchedulerId(schedulerById);
            schedulerHistory.setSchedulerName(schedulerById.getSchedulerName());
            //schedulerService.createSchedulerHistory(schedulerHistory);

        }
        System.out.println(new Date());
        System.out.println(userDao.getActiveAgency());
    }

//    @Scheduled(cron = "*/30 * * * * *")
    public void executeWeeklyTask() {
        System.out.println("Test Weekly1");
        Integer hour = DateUtils.getCurrentHour();
        System.out.println("Test Weekly2");
        Date today = new Date();
        System.out.println("Test Weekly3");
        String weekDayToday = DateUtils.getDayOfWeek(DateUtils.getCurrentWeekDay());
        List<Scheduler> scheduledTasks = schedulerDao.getWeeklyTasks(hour, weekDayToday, today);
        System.out.println("Size " + scheduledTasks.size());
        for (Iterator<Scheduler> iterator = scheduledTasks.iterator(); iterator.hasNext();) {
            Scheduler scheduler = iterator.next();
            System.out.println("Inside for --- ");
            Date schedulerStartTime = new Date();
            SchedulerHistory schedulerHistory = new SchedulerHistory();
//            Scheduler scheduler = iterator.next();
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
            String filename = "/tmp/" + scheduler.getSchedulerName() + "_" + currentDateStr + ".pdf";
            filename = filename.replaceAll(" ", "_");
            String toAddress = accountMailId;
            if (toAddress != null && !toAddress.isEmpty()) {
                toAddress += "," + scheduler.getSchedulerEmail();
            }
            String subject = "[ Scheduled Report ] " + scheduler.getSchedulerName() + " " + scheduler.getAccountId().getAccountName() + " " + currentDateStr;
            String message = "scheduler message";
            Boolean schedulerStatus = downloadReportAndSend(startDate, endDate, dealerId, exportType, report.getId(), filename, toAddress, subject, message);
            schedulerHistory.setFileName(filename);
            schedulerHistory.setEmailId(toAddress);
            schedulerHistory.setEmailSubject(subject);
            schedulerHistory.setEmailMessage(message);

            schedulerHistory.setStatus(schedulerStatus ? "Success" : "Failed");
            Date schedulerEndTime = new Date();
            schedulerHistory.setExecutionEndTime(schedulerEndTime);
            schedulerHistory.setSchedulerId(schedulerById);
            schedulerHistory.setSchedulerName(schedulerById.getSchedulerName());
            //schedulerService.createSchedulerHistory(schedulerHistory);
        }
    }

//    @Scheduled(cron = "*/30 * * * * *")
    public void executeMonthlyTask() {
        // Integer hour = DateUtils.getCurrentHour();
        System.out.println("Monthly 1");
        Date today = new Date();
        System.out.println("Monthly 2");
        String currentDateHour = DateUtils.dateToString(new Date(), "MM/dd/yyyy HH:mm");
        System.out.println("Monthly 3");
        System.out.println(currentDateHour);
        System.out.println(currentDateHour.getClass().getName());
        System.out.println("Monthly 4");
        List<Scheduler> scheduledTasks = schedulerDao.getMonthlyTasks(currentDateHour, today);
        System.out.println("Monthly 5");
        for (Iterator<Scheduler> iterator = scheduledTasks.iterator(); iterator.hasNext();) {
            System.out.println("Monthly Report Size : " + scheduledTasks.size());
            System.out.println("Monthly 6");
            Scheduler scheduler = iterator.next();
            System.out.println("Inside for ---> Monthly ");
            Date schedulerStartTime = new Date();
            SchedulerHistory schedulerHistory = new SchedulerHistory();
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
            String filename = "/tmp/" + scheduler.getSchedulerName() + "_" + currentDateStr + ".pdf";
            filename = filename.replaceAll(" ", "_");
            String toAddress = accountMailId;
            if (toAddress != null && !toAddress.isEmpty()) {
                toAddress += "," + scheduler.getSchedulerEmail();
            }
            String subject = "[ Scheduled Report ] " + scheduler.getSchedulerName() + " " + scheduler.getAccountId().getAccountName() + " " + currentDateStr;
            String message = "scheduler message";
            Boolean schedulerStatus = downloadReportAndSend(startDate, endDate, dealerId, exportType, report.getId(), filename, toAddress, subject, message);
            schedulerHistory.setFileName(filename);
            schedulerHistory.setEmailId(toAddress);
            schedulerHistory.setEmailSubject(subject);
            schedulerHistory.setEmailMessage(message);

            schedulerHistory.setStatus(schedulerStatus ? "Success" : "Failed");
            Date schedulerEndTime = new Date();
            schedulerHistory.setExecutionEndTime(schedulerEndTime);
            schedulerHistory.setSchedulerId(schedulerById);
            schedulerHistory.setSchedulerName(schedulerById.getSchedulerName());
            //schedulerService.createSchedulerHistory(schedulerHistory);
        }
    }

    @Scheduled(cron = "*/10 * * * * *")
    public void executeYearlyTask() {
//         Integer hour = DateUtils.getCurrentHour();
        Date today = new Date();
        String currentDateHour = DateUtils.dateToString(new Date(), "MM/dd/yyyy HH:mm");
        System.out.println(currentDateHour);
        List<Scheduler> scheduledTasks = schedulerDao.getYearlyTasks(currentDateHour, today);
        for (Iterator<Scheduler> iterator = scheduledTasks.iterator(); iterator.hasNext();) {
            Scheduler scheduler = iterator.next();
            System.out.println("Inside for ---> Yearly ");
            Date schedulerStartTime = new Date();
            SchedulerHistory schedulerHistory = new SchedulerHistory();
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
            String filename = "/tmp/" + scheduler.getSchedulerName() + "_" + currentDateStr + ".pdf";
            filename = filename.replaceAll(" ", "_");
            String toAddress = accountMailId;
            if (toAddress != null && !toAddress.isEmpty()) {
                toAddress += "," + scheduler.getSchedulerEmail();
            }
            String subject = "[ Scheduled Report ] " + scheduler.getSchedulerName() + " " + scheduler.getAccountId().getAccountName() + " " + currentDateStr;
            String message = "scheduler message";
            Boolean schedulerStatus = downloadReportAndSend(startDate, endDate, dealerId, exportType, report.getId(), filename, toAddress, subject, message);
            schedulerHistory.setFileName(filename);
            schedulerHistory.setEmailId(toAddress);
            schedulerHistory.setEmailSubject(subject);
            schedulerHistory.setEmailMessage(message);

            schedulerHistory.setStatus(schedulerStatus ? "Success" : "Failed");
            Date schedulerEndTime = new Date();
            schedulerHistory.setExecutionEndTime(schedulerEndTime);
            schedulerHistory.setSchedulerId(schedulerById);
            schedulerHistory.setSchedulerName(schedulerById.getSchedulerName());
            //schedulerService.createSchedulerHistory(schedulerHistory);
        }
    }

    // @Scheduled(cron = "*/10 * * * * *")
    public void executeYearOfWeek() {
        System.out.println(DateUtils.dateToString(new Date(), "MM/dd/yyyy HH:mm"));
        System.out.println("Year Of Week 1");
        Date today = new Date();
        System.out.println("Year Of Week 2");
        Integer hour = DateUtils.getCurrentHour();
        System.out.println("Year Of Week 3");
        Integer currentYearOfWeekCount = DateUtils.getYearOfWeek();
        System.out.println("Year Of Week 4");
        String weekDayToday = DateUtils.getDayOfWeek(DateUtils.getCurrentWeekDay());
        System.out.println("Year Of Week 5");
        List<Scheduler> scheduledTasks = schedulerDao.getYearOfWeekTasks(hour, weekDayToday, currentYearOfWeekCount, today);
        System.out.println("Year Of Week 6");
        for (Iterator<Scheduler> iterator = scheduledTasks.iterator(); iterator.hasNext();) {
            System.out.println("Year Of Week 7");
            Scheduler scheduler = iterator.next();
            System.out.println("Inside for ---> Year Of Week ");

            Date schedulerStartTime = new Date();
            SchedulerHistory schedulerHistory = new SchedulerHistory();
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
            String filename = "/tmp/" + scheduler.getSchedulerName() + "_" + currentDateStr + ".pdf";
            filename = filename.replaceAll(" ", "_");
            String toAddress = accountMailId;
            if (toAddress != null && !toAddress.isEmpty()) {
                toAddress += "," + scheduler.getSchedulerEmail();
            }else{
                toAddress = scheduler.getSchedulerEmail();
            }
            String subject = "[ Scheduled Report ] " + scheduler.getSchedulerName() + " " + scheduler.getAccountId().getAccountName() + " " + currentDateStr;
            String message = "scheduler message";
            Boolean schedulerStatus = downloadReportAndSend(startDate, endDate, dealerId, exportType, report.getId(), filename, toAddress, subject, message);
            schedulerHistory.setFileName(filename);
            schedulerHistory.setEmailId(toAddress);
            schedulerHistory.setEmailSubject(subject);
            schedulerHistory.setEmailMessage(message);

            schedulerHistory.setStatus(schedulerStatus ? "Success" : "Failed");
            Date schedulerEndTime = new Date();
            schedulerHistory.setExecutionEndTime(schedulerEndTime);
            schedulerHistory.setSchedulerId(schedulerById);
            schedulerHistory.setSchedulerName(schedulerById.getSchedulerName());
            //schedulerService.createSchedulerHistory(schedulerHistory);
        }

    }
    
    //@Scheduled(cron = "*/10 * * * * *")
    public void executeOnce(){
        Integer hour = DateUtils.getCurrentHour();
        Date today = new Date();
         System.out.println("Once");
        List<Scheduler> scheduledTasks = schedulerDao.getOnce(hour, today);
        System.out.println("Once 1");
        for (Iterator<Scheduler> iterator = scheduledTasks.iterator(); iterator.hasNext();) {
            System.out.println("Once 2");
            Scheduler scheduler = iterator.next();
            System.out.println("Inside for ---> Year Of Week ");

            Date schedulerStartTime = new Date();
            SchedulerHistory schedulerHistory = new SchedulerHistory();
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
            String filename = "/tmp/" + scheduler.getSchedulerName() + "_" + currentDateStr + ".pdf";
            filename = filename.replaceAll(" ", "_");
            String toAddress = accountMailId;
            if (toAddress != null && !toAddress.isEmpty()) {
                toAddress += "," + scheduler.getSchedulerEmail();
            }else{
                toAddress = scheduler.getSchedulerEmail();
            }
            System.out.println("ONCE MAIL TO ID " + toAddress);
            String subject = "[ Scheduled Report ] " + scheduler.getSchedulerName() + " " + scheduler.getAccountId().getAccountName() + " " + currentDateStr;
            String message = "scheduler message";
            Boolean schedulerStatus = downloadReportAndSend(startDate, endDate, dealerId, exportType, report.getId(), filename, toAddress, subject, message);
            schedulerHistory.setFileName(filename);
            schedulerHistory.setEmailId(toAddress);
            schedulerHistory.setEmailSubject(subject);
            schedulerHistory.setEmailMessage(message);

            schedulerHistory.setStatus(schedulerStatus ? "Success" : "Failed");
            Date schedulerEndTime = new Date();
            schedulerHistory.setExecutionEndTime(schedulerEndTime);
            schedulerHistory.setSchedulerId(schedulerById);
            schedulerHistory.setSchedulerName(schedulerById.getSchedulerName());
            //schedulerService.createSchedulerHistory(schedulerHistory);
        }
    }

    private Boolean downloadReportAndSend(Date startDate, Date endDate,
            String accountId, String exportType, Integer reportId, String filename,
            String to, String subject, String message) {
        try {
            String startDateStr = URLEncoder.encode(DateUtils.dateToString(startDate, "MM/dd/yyyy"), "UTF-8");
            String endDateStr = URLEncoder.encode(DateUtils.dateToString(endDate, "MM/dd/yyyy"), "UTF-8");

            String urlStr = "http://localhost:8080/VizBoard/admin/proxy/downloadReport/" + reportId + "?dealerId=" + accountId + "&exportType=" + exportType + "&startDate=" + startDateStr + "&endDate=" + endDateStr + "&location=" + accountId + "&accountId=" + accountId;
            System.out.println(urlStr);
            URL website = new URL(urlStr);

            File file = new File(filename);

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
