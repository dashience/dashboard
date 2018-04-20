/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author varghees
 */
public class TestFpadQueries {

    static String allReports[] = {"wtd", "edge"};
    static String allLevels[] = {"Overall", "Category", "SubCategory", "Description"};
    static String allSegments[] = {"Country", "State", "City", "Location", "SalesType", "ParkType", "None"};
    static String allFrequency[] = {"Date", "None"};
    static String allMetrics[] = {"Customers", "sales", "established parks", "new_park", "NPS"};

    public static void main(String argv[]) {
        String reportName = "edge";
        String level = "Category";
        String segment = "Country";
        String frequency = "Date";

        String query = getQuery(reportName, level, segment, frequency);
        System.out.println(query);
    }

    private static String getQuery(String reportName, String level, String segment, String frequency) {
        String query = "select ";
        List<String> select = new ArrayList<>();
        List<String> groupBy = new ArrayList<>();
        List<String> orderBy = new ArrayList<>();

        if (level != null && level.equalsIgnoreCase("Overall")) {
            groupBy.add(level);
            select.add(level);
        }
        if (segment != null) {
            groupBy.add(segment);
            select.add(segment);
        }
        if (frequency != null) {
            groupBy.add(frequency);
            select.add(frequency);
        }

        for (int i = 0; i < allMetrics.length; i++) {
            if (level.equalsIgnoreCase("location") && allMetrics[i].equalsIgnoreCase("nps")) {
                continue;
            }
            String metric = "sum(" + allMetrics[i] + ") " + allMetrics[i];
            select.add(metric);
        }

        String selectQry = String.join(",", select);
        String groupQry = String.join(",", groupBy);

        String queryStr = "select " + selectQry + " from " + reportName + " group by " + groupQry;
        return queryStr;
    }
}
