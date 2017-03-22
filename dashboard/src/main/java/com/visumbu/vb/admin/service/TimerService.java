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

    @Scheduled(cron = "*/30 * * * * *")
    public void executeDailyTasks() {
        Integer hour = DateUtils.getCurrentHour();
        Date today = new Date();

        List<Scheduler> scheduledTasks = schedulerDao.getDailyTasks(hour, today); //schedulerDao.getScheduledTasks("Daily");
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
            String filename = "/tmp/" + scheduler.getSchedulerName() + "_" + currentDateStr;
            filename = filename.replaceAll(" ", "_");
            String toAddress = accountMailId;
            if (toAddress != null && !toAddress.isEmpty()) {
                toAddress += "," + scheduler.getSchedulerEmail();
            }
            String subject = "[ Scheduled Report ] " + scheduler.getSchedulerName() + " " + scheduler.getAccountId().getAccountName() + " " + currentDateStr;
            String message  = "scheduler message";
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
            sender.sendMail(to, subject, message, Arrays.asList(attachments));
        } catch (IOException ex) {
            Logger.getLogger(TimerService.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }

}
