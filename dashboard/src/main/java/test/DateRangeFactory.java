/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

/**
 *
 * @author deeta1
 */
import com.visumbu.vb.bean.DateRange;
import com.visumbu.vb.bean.Range;
import java.util.Calendar;
import java.util.Date;

public class DateRangeFactory {

    // Range will be days, month, week, year
    // getRange("week", -7) //last 7 weeks
    // public static DateRange getRange(String range, Integer count) {
    // }
    public static DateRange getRange(Range range) {
        DateRange dateRange = new DateRange();
        Calendar calendar = Calendar.getInstance();

        if (range.equals(Range.TODAY)) {
            dateRange.setEndDate(calendar.getTime());
            dateRange.setStartDate(calendar.getTime());
            return dateRange;

        } else if (range.equals(Range.YESTERDAY)) {
            dateRange.setEndDate(calendar.getTime());
            calendar.add(Calendar.DATE, -1);
            dateRange.setStartDate(calendar.getTime());
            return dateRange;

        } else if (range.equals(Range.THIS_WEEK)) {
            dateRange.setEndDate(calendar.getTime());
            calendar.add(Calendar.DAY_OF_MONTH, calendar.getFirstDayOfWeek() - calendar.get(Calendar.DAY_OF_WEEK));
            dateRange.setStartDate(calendar.getTime());
            return dateRange;

        } else if (range.equals(Range.LAST_WEEK)) {
            calendar.add(Calendar.WEEK_OF_YEAR, -1);
            calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
            dateRange.setStartDate(calendar.getTime());
            calendar.add(Calendar.DAY_OF_WEEK, 6);
            dateRange.setEndDate(calendar.getTime());
            return dateRange;

        } else if (range.equals(Range.THIS_MONTH)) {
            dateRange.setEndDate(calendar.getTime());
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = 1;
            calendar.set(year, month, day);
            calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
            dateRange.setStartDate(calendar.getTime());
            return dateRange;

        } else if (range.equals(Range.LAST_MONTH)) {
            calendar.set(Calendar.DATE, 1);
            calendar.add(Calendar.DAY_OF_MONTH, -1);
            calendar.getTime();
            calendar.set(Calendar.DATE, 1);
            dateRange.setStartDate(calendar.getTime());
            calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
            dateRange.setEndDate(calendar.getTime());
            return dateRange;

        } else if (range.equals(Range.THIS_YEAR)) {
            dateRange.setEndDate(calendar.getTime());
            calendar.set(Calendar.MONTH, 0);
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            dateRange.setStartDate(calendar.getTime());
            return dateRange;

        } else if (range.equals(Range.LAST_YEAR)) {
            int year = calendar.get(Calendar.YEAR) - 1;
            // System.out.println(year);
            int month = 0;
            int day = 1;
            calendar.set(year, month, day);
            calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
            dateRange.setStartDate(calendar.getTime());
            calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DAY_OF_YEAR));
            dateRange.setEndDate(calendar.getTime());
            return dateRange;

        } else {
            return null;
        }

    }

    public static DateRange getRange(Range range, int count, Date endDate) {

        DateRange dateRange = new DateRange();
        Calendar calendar = Calendar.getInstance();

        if (range.equals(Range.DAY)) {
            System.out.println("day and count ----> " + count);
            calendar.add(Calendar.DAY_OF_MONTH, -1);
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DATE);
            calendar.set(year, month, day, 23, 59, 59);
            dateRange.setEndDate(calendar.getTime());

            calendar.setTime(new Date());
            calendar.add(Calendar.DAY_OF_MONTH, count * (-1));
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH);
            day = calendar.get(Calendar.DATE);
            calendar.set(year, month, day, 0, 0, 0);
            dateRange.setStartDate(calendar.getTime());
            return dateRange;
        }

        if (range.equals(Range.WEEK)) {
            System.out.println("week and count ----> " + count);

            calendar.add(Calendar.WEEK_OF_YEAR, count * (-1));
            calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DATE);
            calendar.set(year, month, day, 0, 0, 0);
            dateRange.setStartDate(calendar.getTime());

            calendar = Calendar.getInstance();
            calendar.add(Calendar.WEEK_OF_YEAR, -1);
            calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek() + 6);
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH);
            day = calendar.get(Calendar.DATE);
            calendar.set(year, month, day, 23, 59, 59);
            dateRange.setEndDate(calendar.getTime());
            return dateRange;
        } else if (range.equals(Range.MONTH)) {
            System.out.println("month and count ----> " + count);
            calendar.add(Calendar.MONTH, count * (-1));
            calendar.set(Calendar.DATE, 1);
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DATE);
            calendar.set(year, month, day, 0, 0, 0);
            dateRange.setStartDate(calendar.getTime());
            calendar = Calendar.getInstance();
            if (count == 0) {
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DATE);
                calendar.set(year, month, day, 23, 59, 59);
                dateRange.setEndDate(calendar.getTime());
            } else {
                calendar.set(Calendar.DATE, 1);
                calendar.add(Calendar.DAY_OF_MONTH, -1);
                calendar.getTime();
                calendar.set(Calendar.DATE, 1);
                calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DATE);
                calendar.set(year, month, day, 23, 59, 59);
                dateRange.setEndDate(calendar.getTime());
            }

            return dateRange;

        } else if (range.equals(Range.YEAR)) {
            System.out.println("year and count ----> " + count);
            if (count == 0) {
                dateRange.setEndDate(calendar.getTime());
                calendar.set(Calendar.MONTH, 0);
                calendar.set(Calendar.DAY_OF_MONTH, 1);
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DATE);
                calendar.set(year, month, day, 0, 0, 0);
                dateRange.setStartDate(calendar.getTime());
            } else {
                int year = calendar.get(Calendar.YEAR) - count;
                int month = 0;
                int day = 1;
                calendar.set(year, month, day, 0, 0, 0);
                calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
                dateRange.setStartDate(calendar.getTime());
                calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR) - 1;
                month = 0;
                day = 1;
                calendar.set(year, month, day);
                calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
                calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DAY_OF_YEAR));
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DATE);
                calendar.set(year, month, day, 23, 59, 59);
                dateRange.setEndDate(calendar.getTime());
            }
            return dateRange;
        } else {
            return null;
        }
    }

    public static void main(String args[]) {
        int count = 4;
        DateRange dateRangeDay = getRange(Range.DAY, count, null);
        DateRange dateRangeWeek = getRange(Range.WEEK, count, null);
        DateRange dateRangeMonth = getRange(Range.MONTH, count, null);
        DateRange dateRangeYear = getRange(Range.YEAR, count, null);

        System.out.println("Date Range Last " + count + " Day Start Date ----------> " + dateRangeDay.getStartDate());
        System.out.println("Date Range Last " + count + " Day End Date ----------> " + dateRangeDay.getEndDate());
        System.out.println("");
        System.out.println("Date Range Last " + count + " Week Start Date ----------> " + dateRangeWeek.getStartDate());
        System.out.println("Date Range Last " + count + " Week End Date ----------> " + dateRangeWeek.getEndDate());
        System.out.println("");
        System.out.println("Date Range Last " + count + " Month Start Date ----------> " + dateRangeMonth.getStartDate());
        System.out.println("Date Range Last " + count + " Month End Date ----------> " + dateRangeMonth.getEndDate());
        System.out.println("");
        System.out.println("Date Range Last " + count + " Year Start Date ----------> " + dateRangeYear.getStartDate());
        System.out.println("Date Range Last " + count + " Year End Date----------> " + dateRangeYear.getEndDate());
    }

}
