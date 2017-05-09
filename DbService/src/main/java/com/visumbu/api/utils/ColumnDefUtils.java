/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.api.utils;

import com.visumbu.api.bean.ColumnDef;

/**
 *
 * @author user
 */
public class ColumnDefUtils {

    public static ColumnDef getCtrColumnDef() {
        ColumnDef columnDef = new ColumnDef();
        columnDef.setFieldName("ctr");
        columnDef.setDisplayName("CTR");
        columnDef.setAgregationFunction("sum");
        return columnDef;
    }

    public static ColumnDef getImpressionsColumnDef() {
        ColumnDef columnDef = new ColumnDef();
        columnDef.setFieldName("impressions");
        columnDef.setDisplayName("Impressions");
        columnDef.setAgregationFunction("sum");
        return columnDef;
    }

    public static ColumnDef getClicksColumnDef() {
        ColumnDef columnDef = new ColumnDef();
        columnDef.setFieldName("clicks");
        columnDef.setDisplayName("Clicks");
        columnDef.setAgregationFunction("sum");
        return columnDef;
    }

    public static ColumnDef getDeviceColumnDef() {
        ColumnDef columnDef = new ColumnDef();
        columnDef.setFieldName("device");
        columnDef.setDisplayName("Device Name");
        columnDef.setGroupPriority(1);
        return columnDef;
    }

    public static ColumnDef getCostColumnDef() {
        ColumnDef columnDef = new ColumnDef();
        columnDef.setFieldName("cost");
        columnDef.setDisplayName("Cost");
        columnDef.setAgregationFunction("sum");
        return columnDef;
    }

    public static ColumnDef getAverageCpcColumnDef() {
        ColumnDef columnDef = new ColumnDef();
        columnDef.setFieldName("averageCpc");
        columnDef.setDisplayName("Average Cpc");
        columnDef.setAgregationFunction("avg");
        return columnDef;
    }

    public static ColumnDef getAveragePositionColumnDef() {
        ColumnDef columnDef = new ColumnDef();
        columnDef.setFieldName("averagePosition");
        columnDef.setDisplayName("Average Position");
        columnDef.setAgregationFunction("avg");
        return columnDef;
    }

    public static ColumnDef getConversionsColumnDef() {
        ColumnDef columnDef = new ColumnDef();
        columnDef.setFieldName("conversions");
        columnDef.setDisplayName("Conversions");
        columnDef.setAgregationFunction("avg");
        return columnDef;
    }

    public static ColumnDef getSearchImpressionsShare() {
        ColumnDef columnDef = new ColumnDef();
        columnDef.setFieldName("searchImpressionsShare");
        columnDef.setDisplayName("Search Impressions Share");
        columnDef.setAgregationFunction("avg");
        return columnDef;
    }

    public static ColumnDef getCpaColumnDef() {
        ColumnDef columnDef = new ColumnDef();
        columnDef.setFieldName("cpa");
        columnDef.setDisplayName("CPA");
        columnDef.setAgregationFunction("avg");
        return columnDef;
    }

    public static ColumnDef getCampaignDeviceColumnDef() {
        ColumnDef columnDef = new ColumnDef();
        columnDef.setFieldName("device");
        columnDef.setDisplayName("Device Name");
        columnDef.setGroupPriority(2);
        return columnDef;
    }

    public static ColumnDef getSourceColumnDef() {
        ColumnDef columnDef = new ColumnDef();
        columnDef.setFieldName("source");
        columnDef.setDisplayName("Source");
        return columnDef;
    }

    public static ColumnDef getCampaignNameColumnDef() {
        ColumnDef columnDef = new ColumnDef();
        columnDef.setFieldName("campaignName");
        columnDef.setDisplayName("Campaign Name");
        columnDef.setGroupPriority(1);
        return columnDef;
    }

    public static ColumnDef getGenericColumnDef(String name, String displayName) {
        ColumnDef columnDef = new ColumnDef();
        columnDef.setFieldName(name);
        columnDef.setDisplayName(displayName);
        return columnDef;
    }

    public static ColumnDef getGenericColumnDef(String name, String displayName, String agreegationType) {
        ColumnDef columnDef = new ColumnDef();
        columnDef.setFieldName(name);
        columnDef.setDisplayName(displayName);
        columnDef.setAgregationFunction(agreegationType);
        return columnDef;
    }

    public static ColumnDef getGenericColumnDef(String name, String displayName, Integer groupOrder) {
        ColumnDef columnDef = new ColumnDef();
        columnDef.setFieldName(name);
        columnDef.setDisplayName(displayName);
        columnDef.setGroupPriority(groupOrder);
        return columnDef;
    }

}
