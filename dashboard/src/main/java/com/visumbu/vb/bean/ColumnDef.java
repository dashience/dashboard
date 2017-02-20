/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.bean;

/**
 *
 * @author user
 */
public class ColumnDef {

    private String fieldName;
    private String type;
    private String sortPriority;
    private String sortOrder;
    private String agregationFunction;
    private String displayName;
    private Integer groupPriority;
    private String displayFormat;

    public static class Aggregation {

        public static final String SUM = "sum";
        public static final String AVG = "avg";
        public static final String CTR = "ctr";   // clicks/impressions
        public static final String CPA = "cpa";   // cost/conversions
        public static final String CPC = "cpc";   // cost/clicks
        public static final String MAX = "max";
        public static final String MIN = "min";
        public static final String COUNT = "count";
        public static final String None = "";
        public static final String CPR = "cpr";  // cost/reacctions
        public static final String CPL = "cpl";  // cost/likes
        public static final String CPLC = "cplc"; // Cost/Link Click
        public static final String CPComment = "cpcomment"; // cost/comments
        public static final String CPostE = "cposte";  // cost/post engagements
        public static final String CPageE = "cpagee"; // cost/page engagements
        public static final String CPP = "cpp";  // cost/posts
    }
    public static class Format {
        public static final String CURRENCY = "$,.2f";
        public static final String PERCENTAGE = ".2%";
        public static final String DECIMAL1 = ",.1f";
        public static final String DECIMAL2 = ",.2f";
        public static final String INTEGER = ",.0f";
    }

    public ColumnDef(String fieldName, String type, String displayName) {
        this.fieldName = fieldName;
        this.type = type;
        this.displayName = displayName;
    }
    public ColumnDef(String fieldName, String type, String displayName, Integer groupPriority) {
        this.fieldName = fieldName;
        this.type = type;
        this.displayName = displayName;
        this.groupPriority = groupPriority;
    }

    public ColumnDef(String fieldName, String type, String displayName, String agregationFunction, String displayFormat) {
        this.fieldName = fieldName;
        this.type = type;
        this.agregationFunction = agregationFunction;
        this.displayName = displayName;
        this.displayFormat = displayFormat;
    }

    public ColumnDef(String fieldName,String type, String displayName, String agregationFunction) {
        this.fieldName = fieldName;
        this.agregationFunction = agregationFunction;
        this.displayName = displayName;
    }

    public ColumnDef(String fieldName, String sortPriority, String sortOrder, String agregationFunction, String displayName, Integer groupPriority, String displayFormat) {
        this.fieldName = fieldName;
        this.type = type;
        this.sortPriority = sortPriority;
        this.sortOrder = sortOrder;
        this.agregationFunction = agregationFunction;
        this.displayName = displayName;
        this.groupPriority = groupPriority;
        this.displayFormat = displayFormat;
    }

    public ColumnDef() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDisplayFormat() {
        return displayFormat;
    }

    public void setDisplayFormat(String displayFormat) {
        this.displayFormat = displayFormat;
    }
    
    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getSortPriority() {
        return sortPriority;
    }

    public void setSortPriority(String sortPriority) {
        this.sortPriority = sortPriority;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }

    public String getAgregationFunction() {
        return agregationFunction;
    }

    public void setAgregationFunction(String agregationFunction) {
        this.agregationFunction = agregationFunction;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Integer getGroupPriority() {
        return groupPriority;
    }

    public void setGroupPriority(Integer groupPriority) {
        this.groupPriority = groupPriority;
    }

    @Override
    public String toString() {
        return "ColumnDef{" + "fieldName=" + fieldName + ", type=" + type + ", sortPriority=" + sortPriority + ", sortOrder=" + sortOrder + ", agregationFunction=" + agregationFunction + ", displayName=" + displayName + ", groupPriority=" + groupPriority + ", displayFormat=" + displayFormat + '}';
    }
    
}
