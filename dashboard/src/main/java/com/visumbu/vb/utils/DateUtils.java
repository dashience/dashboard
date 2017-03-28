/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.utils;

import com.visumbu.vb.bean.DateRange;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.joda.time.LocalDate;

/**
 *
 * @author varghees
 */
public class DateUtils {

    
    public static String toAdWordsDate(Date date) {
        String format = "YYYYMMdd";
        return dateToString(date, format);
    }

    public static String getAdWordsStartDate(Date startDate) {
        if (startDate == null) {
            return "7DaysAgo";
        }
        return toAdWordsDate(startDate);
    }

    public static String getAdWordsEndDate(Date endDate) {
        if (endDate == null) {
            return "today";
        }
        return toAdWordsDate(endDate);
    }
    
    public static Date get30DaysBack() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd ");
        Calendar cal = Calendar.getInstance();
        Calendar calReturn = Calendar.getInstance();
        calReturn.add(Calendar.DATE, -30);
        return calReturn.getTime();
    }

    public static String getQueryString(String tableName, String searchText) {
        String searchString = searchText;
        searchString = searchString.replaceAll("\\s+and\\s+", " matchand ");
        searchString = searchString.replaceAll("\\s+or\\s+", " matchor ");
        searchString = searchString.replaceAll("createdBy", "createdBy.username");

        String queryString = "from " + tableName + " where  ";

        String[] searchTexts = searchString.split(" match");
        for (int i = 0; i < searchTexts.length; i++) {
            String searchStr = searchTexts[i];
            if (searchStr.startsWith("and ")) {
                queryString += " and ";
                searchStr = searchStr.replaceFirst("and ", "");
            } else if (searchStr.startsWith("or ")) {
                queryString += " or ";
                searchStr = searchStr.replaceFirst("or ", "");
            }
            System.out.println(searchStr);

            searchStr = searchStr.trim();
            if (searchStr.contains("!=")) {
                searchStr = searchStr.replaceAll("\\s+!=\\s+", " != '");
            } else if (searchStr.contains("=")) {
                searchStr = searchStr.replaceAll("\\s*=\\s*", " = '");

            }
            searchStr = searchStr.replaceAll("\\s+like\\s+", " like '");
            if (searchStr.contains(" contains ")) {
                searchStr = "lower(" + searchStr.replaceAll("\\s+contains\\s+", ") like lower('%");
                searchStr += "%') ";
            } else {
                searchStr += "' ";
            }
            queryString += searchStr;
        }
        return queryString;
    }

    public static String toJSDate(Date date) {
        String format = "yyyy-MM-dd HH:mm:ss";
        return dateToString(date, format);
    }

    public static String toTTDate(Date date) {
        String format = "dd/MM/yyyy HH:mm:ss";
        return dateToString(date, format);
    }

    public static String dateToString(Date date, String format) {
        if (date == null) {
            return "-";
        }
        DateFormat df = new SimpleDateFormat(format);
        String reportDate = df.format(date);
        return reportDate;
    }

    public static Date getFirstDateOfCurrentMonth() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, Calendar.getInstance().getActualMinimum(Calendar.DAY_OF_MONTH));
        return cal.getTime();
    }

    public static Date getFirstDayOfLastMonth() {
        Date date = getFirstDateOfCurrentMonth();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, -1);
        return cal.getTime();
    }

    public static Date getFirstDayOfNextMonth() {
        Date date = getFirstDateOfCurrentMonth();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, 1);
        return cal.getTime();
    }

    public static Date getTonight() {
        Calendar calendar = new GregorianCalendar();
        calendar.set(Calendar.HOUR_OF_DAY, 23); //anything 0 - 23
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        Date today = calendar.getTime(); //the midnight, that's the first second of the day.
        return today;
    }

    public static Date getToday() {
        Calendar calendar = new GregorianCalendar();
        calendar.set(Calendar.HOUR_OF_DAY, 0); //anything 0 - 23
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date today = calendar.getTime(); //the midnight, that's the first second of the day.
        return today;
    }

    public static Date get24HoursBack() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        return cal.getTime();
    }

    public static Date getOneMonthsBack(Date date) {
        Calendar cal = Calendar.getInstance();
        if (date != null) {
            cal.setTime(date);
        }
        cal.add(Calendar.MONTH, -1);
        cal.set(Calendar.HOUR_OF_DAY, 0); //anything 0 - 23
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        return cal.getTime();
    }

    public static Date getSixMonthsBack(Date date) {
        Calendar cal = Calendar.getInstance();
        if (date != null) {
            cal.setTime(date);
        }
        cal.add(Calendar.MONTH, -6);
        cal.set(Calendar.HOUR_OF_DAY, 0); //anything 0 - 23
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        return cal.getTime();
    }

    public static Date getYesterday() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        cal.set(Calendar.HOUR_OF_DAY, 0); //anything 0 - 23
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        return cal.getTime();
    }

    public static Date getYesterday(String date) {
        Calendar cal = Calendar.getInstance();
        Date parsedDate = getEndDate(date);
        cal.setTime(parsedDate);
        cal.add(Calendar.DATE, -1);
        cal.set(Calendar.HOUR_OF_DAY, 0); //anything 0 - 23
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        return cal.getTime();
    }

    public static Date getEndDate(String strEnd) {
        System.out.println("Start Date " + strEnd);
        if (strEnd.length() < 12) {
            strEnd += " 23:59:59";
        }
        DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        Date endDate = null;
        try {
            endDate = (Date) formatter.parse(strEnd);
        } catch (Exception ex) {
            System.out.println("Exception End ");
            endDate = new Date();
        }
        return endDate;
    }

    public static Date getStartDate(String strStart) {
        System.out.println("Start Date " + strStart);
        if (strStart.length() < 12) {
            strStart += " 00:00:00";
        }
        DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        Date startDate = null;
        try {
            startDate = (Date) formatter.parse(strStart);
        } catch (Exception ex) {
            System.out.println("Exception Start ");
            startDate = DateUtils.getYesterday();
        }
        return startDate;
    }

    public static Date getStartTodayDate(String strStart) {
        System.out.println("Start Date " + strStart);
        if (strStart.length() < 12) {
            strStart += " 00:00:00";
        }
        DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        Date startDate = null;
        try {
            startDate = (Date) formatter.parse(strStart);
        } catch (Exception ex) {
            System.out.println("Exception Start ");
            startDate = DateUtils.getToday();
        }
        return startDate;
    }

    public static Date toDate(String dateStr, String formatStr) {
        if (dateStr == null || dateStr.isEmpty()) {
            return null;
        }
        try {
            DateFormat format = new SimpleDateFormat(formatStr);
            Date date = format.parse(dateStr);
            return date;
        } catch (ParseException ex) {
            Logger.getLogger(DateUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static Date toDate(String dateStr) {
        if (dateStr.length() < 12) {
            dateStr += " 00:00:00";
        }
        String format = "dd-M-yyyy HH:mm:ss";
        return toDate(dateStr, format);

    }

    public static Date jsToJavaDate(String dateStr) {
        if (dateStr.length() < 12) {
            dateStr += " 00:00:00";
        }
        String format = "dd/M/yyyy HH:mm:ss";
        return toDate(dateStr, format);
    }

    public static Long dateDiff(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            return 0L;
        }
        return Math.abs(date1.getTime() - date2.getTime()) / (60 * 60 * 1000);
    }

    public static Long dateDiffInSec(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            return 0L;
        }
        return (date1.getTime() - date2.getTime()) / 1000;
    }

    public static Long timeDiff(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            return 0L;
        }
        return Math.abs(date1.getTime() - date2.getTime());
    }

    public static Integer getDifferenceInMonths(Date startDate, Date endDate) {
        Calendar c1 = Calendar.getInstance();
        c1.setTime(startDate);
        Calendar c2 = Calendar.getInstance();
        c2.setTime(endDate);
        int diff = 0;
        if (c2.after(c1)) {
            while (c2.after(c1)) {
                c1.add(Calendar.MONTH, 1);
                if (c2.after(c1)) {
                    diff++;
                }
            }
        } else if (c2.before(c1)) {
            while (c2.before(c1)) {
                c1.add(Calendar.MONTH, -1);
                if (c1.before(c2)) {
                    diff--;
                }
            }
        }
        return diff;
    }

    public static List<Date> getDaysBetweenDates(Date startdate, Date enddate) {
        List<Date> dates = new ArrayList<Date>();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(startdate);

        while (calendar.getTime().before(enddate)) {
            Date result = calendar.getTime();
            dates.add(result);
            calendar.add(Calendar.DATE, 1);
        }
        return dates;
    }

    public static Integer getCurrentHour() {
        Date date = new Date();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    public static Integer getCurrentWeekDay() {
        Date date = new Date();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_WEEK);
    }

    public static String getDayOfWeek(Integer day) {
        String[] days = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
        return days[day - 1];
    }
    public static Date getStartDateOfWeek(Date currentStart) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public static Date getNextWeek(Date weekStart) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    public static Integer getYearOfWeek() {
        Date date = new Date();
        Calendar calendar =  new GregorianCalendar();    
        calendar.setTime(date);
        return  calendar.get(Calendar.WEEK_OF_YEAR);
    }
}
