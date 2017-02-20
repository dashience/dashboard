/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.utils;

import java.text.NumberFormat;
import java.util.Locale;

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

        if (jFormat.indexOf("%") >= 0) {
            sufix = "%";
            jFormat = jFormat.replace("%", "");
        }
        if (jFormat.indexOf('$') >= 0) {
            prefix = "$";
            jFormat = jFormat.replace("$", "");
        }
        if (jFormat != null && !jFormat.isEmpty() && jFormat.indexOf("f") < 0 && jFormat.indexOf("d") < 0) {
            jFormat = jFormat + "f";
        }
        if (jFormat != null && !jFormat.isEmpty()) {
            returnValue = prefix + String.format("%" + jFormat, ApiUtils.toDouble(value)) + sufix;
        }
        return returnValue;
    }
    
    public static void main(String argv[]) {
        String format = ".1%";
        String value = "5346.00";
        System.out.println(Formatter.format(format, value));
    }
}
