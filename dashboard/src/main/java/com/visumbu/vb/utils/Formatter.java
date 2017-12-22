/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.utils;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 *
 * @author user
 */
public class Formatter {

    public static String format(String format, String value) {
        String returnValue = value;
        String jFormat = format;
        String prefix = "";
        String sufix = "";
        Integer multiplier = 1;
        System.out.println("format ---->" + jFormat + "<----->");
        System.out.println("value ----->" + value);
        if (jFormat.indexOf("%") >= 0) {
            System.out.println("if --- 1");
            sufix = "%";
            multiplier = 1;
            jFormat = jFormat.replace("%", "");
        }
        if (jFormat.indexOf('$') >= 0) {
            System.out.println("if --- 2");
            prefix = "$";
            jFormat = jFormat.replace("$", "");
        }
        if (jFormat != null && jFormat.equals("H:M:S")) {
            System.out.println("format -----> H:M:S");
            double doubleValue = Double.parseDouble(value);
            DecimalFormat df = new DecimalFormat("#.00");
            String val = df.format(doubleValue);
            System.out.println("val ---> "+val);
            Date d = new Date((long) (Float.parseFloat(val) * 1000L));
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss"); // HH for 0-23
            sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
            String time = sdf.format(d);
            returnValue = time;
            System.out.println("Return Value ----> " + value);
            return returnValue;
        }
        if (jFormat != null && !jFormat.isEmpty() && jFormat.indexOf("f") < 0 && jFormat.indexOf("d") < 0) {
            System.out.println("if --- 3");
            jFormat = jFormat + "f";
        }
        if (jFormat != null && !jFormat.isEmpty()) {
            System.out.println("if --- 4");
            returnValue = prefix + String.format("%" + jFormat, multiplier * ApiUtils.toDouble(value)) + sufix;
        }
        return returnValue;
    }

    public static void main(String argv[]) {
        String format = "$,.2f";
        String value = "5346.00";
        System.out.println(Formatter.format(format, value));
    }
}

