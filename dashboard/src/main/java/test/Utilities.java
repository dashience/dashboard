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
import java.util.TimeZone;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Utilities {

    public static void main(String args[]) {

        Date today = new Date();

        String stringDate = dateToStringFormatter(today);
        String timestamp = dateToTimeStamp(stringDate);

//        System.out.println("*********************************");
//        System.out.println("String Date-->" + stringDate);
//        System.out.println("TimeStamp-->" + timestamp);
    }

    public static String getData(HttpServletRequest request, HttpServletResponse response) {
        String startDate = request.getParameter("startDate");
        System.out.println("My StartDate-->" + startDate);
        return null;
    }

    public static String dateToTimeStamp(String strDate) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-DD");
            Date date = sdf.parse(strDate);
//            long unixTime = (long) date.getTime() / 1000;
//            System.out.println(unixTime);//<- prints 1352504418
//            dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
//            String time=dateFormat.format(date);
//            System.out.println("*******************");
//            System.out.println("GMT ="+time);

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

//Here you say to java the initial timezone. This is the secret
            sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
//Will print in UTC
            System.out.println(sdf.format(calendar.getTime()));

//Here you set to your timezone
            sdf.setTimeZone(TimeZone.getDefault());
//Will print on your default Timezone
            System.out.println("--->"+sdf.format(calendar.getTime()));
        } catch (Exception e) {
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
