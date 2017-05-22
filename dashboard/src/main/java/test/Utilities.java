/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

/**
 *
 * @author deldot
 */
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Utilities {

    public static void main(String args[]) {

        Date today = new Date();

        String stringDate = dateToStringFormatter(today);
        String timestamp = dateToTimeStamp(stringDate);

        System.out.println("*********************************");
        System.out.println("String Date-->" + stringDate);
        System.out.println("TimeStamp-->" +timestamp);

    }

    public static String dateToTimeStamp(String strDate) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY-MM-DD");
            Date date = dateFormat.parse(strDate);
            long unixTime = (long) date.getTime() / 1000;
            System.out.println(unixTime);//<- prints 1352504418
        } catch (ParseException e) {
            System.out.println("Date parse exception tooks place");
            System.out.println(e);
        }
        return null;
    }

    public static String dateToStringFormatter(Date date) {

        SimpleDateFormat formatter = new SimpleDateFormat("YYYY-MM-DD");
        String dateString = formatter.format(date);
        return dateString;
    }

}
