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
import com.visumbu.vb.model.Agency;
import com.visumbu.vb.model.AgencySettings;
import com.visumbu.vb.model.Report;
import com.visumbu.vb.model.Scheduler;
import com.visumbu.vb.model.SchedulerHistory;
import com.visumbu.vb.utils.DateUtils;
import com.visumbu.vb.utils.PropertyReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
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
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
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

    PropertyReader propReader = new PropertyReader();

    private final String urlDownloadReport = "url.downloadReport";
    private final String urlGenerator = "url.urlGenerator";
    private final String reportDownloadUrl = "url.reportDownloadUrl";

    public void executeTasks(List<Scheduler> scheduledTasks) {
        System.out.println("Executing Tasks " + scheduledTasks);
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
            System.out.println("startdate ----> " + scheduler.getCustomStartDate());

            if (dateRangeName != null) {
                if (scheduler.getLastNdays() != null) {
                    lastNdays = scheduler.getLastNdays();
                    System.out.println("Last N days ----> " + lastNdays);
                }
                if (scheduler.getLastNmonths() != null) {
                    lastNmonths = scheduler.getLastNmonths();
                    System.out.println("Last N months ----> " + lastNmonths);
                }
                if (scheduler.getLastNweeks() != null) {
                    lastNweeks = scheduler.getLastNweeks();
                    System.out.println("Last N weeks ----> " + lastNweeks);

                }
                if (scheduler.getLastNyears() != null) {
                    lastNyears = scheduler.getLastNyears();
                    System.out.println("Last N years ----> " + lastNyears);
                }

                System.out.println("dateRangename ----> " + dateRangeName);

                Range dateRangeSelect = null;

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
                } else if (dateRangeSelect.equals(Range.DAY)) {
                    dateRange = DateRangeFactory.getRange(dateRangeSelect, lastNdays, new Date());
                } else if (dateRangeSelect.equals(Range.WEEK)) {
                    dateRange = DateRangeFactory.getRange(dateRangeSelect, lastNweeks, new Date());
                } else if (dateRangeSelect.equals(Range.MONTH)) {
                    dateRange = DateRangeFactory.getRange(dateRangeSelect, lastNmonths, new Date());
                } else if (dateRangeSelect.equals(Range.YEAR)) {
                    dateRange = DateRangeFactory.getRange(dateRangeSelect, lastNyears, new Date());
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
//            String status = scheduler.getStatus();
//            if (status.equalsIgnoreCase("Active")) {
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
//            } else {
//                System.out.println("Scheduler is InActive");
//            }
        }
    }

//    @Scheduled(cron = "0/5 * * * * *")
//    public void testScheduler() {
//        System.out.println("Test Scheduler...");
//        System.out.println("Success....");
//    }
    @Scheduled(cron = "0 0 */1 * * *")
    public void executeDailyTasks() {
        System.out.println("Executing daily Tasks....");
        List<Agency> allAgencies = schedulerDao.getAllAgency();
        System.out.println("all Agencies --> " + allAgencies);
        for (Iterator<Agency> iterator = allAgencies.iterator(); iterator.hasNext();) {
            Agency agency = iterator.next();
            System.out.println("Executing Daily Task for Agency " + agency.toString());
            AgencySettings agencySettings = userDao.getAgencySettingsById(agency.getId());
            String timezone = agencySettings.getTimeZoneId().getShortDescription();
            System.out.println("Timezone ===> " + timezone);
            Date today = DateUtils.convertCurrentTimeToTz(timezone, new Date());
            System.out.println("Converted Time  in Timezone " + timezone + " " + today + " Local Time " + new Date());
            // Date today = new Date();
            Integer hour = DateUtils.getHour(today);
            List<Scheduler> scheduledTasks = schedulerDao.getDailyTasks(hour, today, agency); //schedulerDao.getScheduledTasks("Daily");
            executeTasks(scheduledTasks);
        }
    }

    @Scheduled(cron = "0 0 */1 * * *")
    public void executeWeeklyTask() {
        System.out.println("Executing weekly Tasks....");
        List<Agency> allAgencies = schedulerDao.getAllAgency();
        for (Iterator<Agency> iterator = allAgencies.iterator(); iterator.hasNext();) {
            Agency agency = iterator.next();
            System.out.println("Executing weekly Task for Agency " + agency.toString());
            AgencySettings agencySettings = userDao.getAgencySettingsById(agency.getId());
            String timezone = agencySettings.getTimeZoneId().getShortDescription();
            System.out.println("Timezone ===> " + timezone);
            Date today = DateUtils.convertCurrentTimeToTz(timezone, new Date());
            System.out.println("Converted Time  in Timezone " + timezone + " " + today + " Local Time " + new Date());
            // Date today = new Date();
            String weekDayToday = DateUtils.getDayOfWeek(DateUtils.getCurrentWeekDay(today));
            Integer hour = DateUtils.getHour(today);
            List<Scheduler> scheduledTasks = schedulerDao.getWeeklyTasks(hour, weekDayToday, today, agency);
            executeTasks(scheduledTasks);
        }
    }

    @Scheduled(cron = "0 0 */1 * * *")
    public void executeMonthlyTask() {
        List<Agency> allAgencies = schedulerDao.getAllAgency();
        for (Iterator<Agency> iterator = allAgencies.iterator(); iterator.hasNext();) {
            Agency agency = iterator.next();
            System.out.println("Executing Montyly Task for Agency " + agency.toString());
            AgencySettings agencySettings = userDao.getAgencySettingsById(agency.getId());
            String timezone = agencySettings.getTimeZoneId().getShortDescription();
            System.out.println("Monthly Timezone ===> " + timezone);
            Date today = DateUtils.convertCurrentTimeToTz(timezone, new Date());
            System.out.println("Monthly Converted Time  in Timezone " + timezone + " " + today + " Local Time " + new Date());
            // Date today = new Date();
            String currentDateHour = DateUtils.dateToString(today, "MM/dd/yyyy HH:00");
            List<Scheduler> scheduledTasks = schedulerDao.getMonthlyTasks(currentDateHour, today, agency);
            executeTasks(scheduledTasks);
        }
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
        List<Agency> allAgencies = schedulerDao.getAllAgency();
        for (Iterator<Agency> iterator = allAgencies.iterator(); iterator.hasNext();) {
            Agency agency = iterator.next();
            System.out.println("Executing Montyly Task for Agency " + agency.toString());
            AgencySettings agencySettings = userDao.getAgencySettingsById(agency.getId());
            String timezone = agencySettings.getTimeZoneId().getShortDescription();
            System.out.println("Monthly Timezone ===> " + timezone);
            Date today = DateUtils.convertCurrentTimeToTz(timezone, new Date());
            System.out.println("Monthly Converted Time  in Timezone " + timezone + " " + today + " Local Time " + new Date());
            Integer hour = DateUtils.getHour(today);
            System.out.println("Once");
            List<Scheduler> scheduledTasks = schedulerDao.getOnce(hour, today, agency);
            System.out.println("Once 1");
            executeTasks(scheduledTasks);
        }
    }

    private Boolean downloadReportAndSend(Date startDate, Date endDate,
            String accountId, String exportType, Integer reportId, String filename,
            String to, String subject, String message) {
        try {
            System.out.println("exportType: " + exportType);
            String startDateStr = URLEncoder.encode(DateUtils.dateToString(startDate, "MM/dd/yyyy"), "UTF-8");
            String endDateStr = URLEncoder.encode(DateUtils.dateToString(endDate, "MM/dd/yyyy"), "UTF-8");

            String url = propReader.readUrl(reportDownloadUrl)+ accountId + "/" + reportId + "?startDate=" + startDateStr + "&endDate=" + endDateStr;
            String pdfGenerator = propReader.readUrl(urlGenerator)+URLEncoder.encode(url,"UTF-8");
            downloadUrlAndSave(filename, pdfGenerator);
            //String urlStr = propReader.readUrl(urlDownloadReport) + reportId + "?dealerId=" + accountId + "&exportType=" + exportType + "&startDate=" + startDateStr + "&endDate=" + endDateStr + "&location=" + accountId + "&accountId=" + accountId;

            //System.out.println(urlStr);
           // URL website = new URL(urlStr);

//            File file = new File(filename);
//            System.out.println("filename: " + filename);
//            FileUtils.copyURLToFile(website, file);
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

    public static String downloadUrlAndSave(String filename, String urlPath) {
        String savedFile = filename;
        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[]{
            new X509TrustManager() {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                public void checkClientTrusted(
                        java.security.cert.X509Certificate[] certs, String authType) {
                }

                public void checkServerTrusted(
                        java.security.cert.X509Certificate[] certs, String authType) {
                }
            }
        };

// Install the all-trusting trust manager
        try {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
        }

        try {
            URL url = new java.net.URL(urlPath);
            URLConnection urlConnect = url.openConnection();
            urlConnect.setDoInput(true);
            urlConnect.setDoOutput(true);
            byte[] buffer = new byte[8 * 1024];
            System.out.println("FILE NAME " + filename);
            InputStream input = urlConnect.getInputStream();
            try {
                File file = new File(filename);
                boolean mkdirs = file.getParentFile().mkdirs();
                OutputStream output = new FileOutputStream(filename);
                try {
                    int bytesRead;
                    while ((bytesRead = input.read(buffer)) != -1) {
                        output.write(buffer, 0, bytesRead);
                    }
                } finally {
                    output.close();
                }
            } finally {
                input.close();
            }
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return savedFile;
    }

}
