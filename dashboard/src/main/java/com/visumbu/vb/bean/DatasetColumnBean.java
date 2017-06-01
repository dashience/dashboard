/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.bean;

import java.util.List;

/**
 *
 * @author deeta1
 */
public class DatasetColumnBean {

    private Integer Id;
    private String fieldName;
    private String displayName;
    private String fieldType;
    private String status;
    private String expression;
    private String functionName;
    private String columnName;
    private String baseField;
    private String displayFormat;
    private String customEndDate;
    private String customStartDate;
    private String dateRangeName;
    private Integer lastNdays;
    private Integer lastNmonths;
    private Integer lastNweeks;
    private Integer lastNyears;
    private Integer datasetId;
    private List<DatasetColumnBean> tableColumns;

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public void setFunctionName(String functionName) {
        this.functionName = functionName;
    }

    public String getFunctionName() {
        return functionName;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getBaseField() {
        return baseField;
    }

    public void setBaseField(String baseField) {
        this.baseField = baseField;
    }

    public String getDisplayFormat() {
        return displayFormat;
    }

    public void setDisplayFormat(String displayFormat) {
        this.displayFormat = displayFormat;
    }

    public String getCustomEndDate() {
        return customEndDate;
    }

    public void setCustomEndDate(String customEndDate) {
        this.customEndDate = customEndDate;
    }

    public String getCustomStartDate() {
        return customStartDate;
    }

    public void setCustomStartDate(String customStartDate) {
        this.customStartDate = customStartDate;
    }

    public String getDateRangeName() {
        return dateRangeName;
    }

    public void setDateRangeName(String dateRangeName) {
        this.dateRangeName = dateRangeName;
    }

    public Integer getLastNdays() {
        return lastNdays;
    }

    public void setLastNdays(Integer lastNdays) {
        this.lastNdays = lastNdays;
    }

    public Integer getLastNmonths() {
        return lastNmonths;
    }

    public void setLastNmonths(Integer lastNmonths) {
        this.lastNmonths = lastNmonths;
    }

    public Integer getLastNweeks() {
        return lastNweeks;
    }

    public void setLastNweeks(Integer lastNweeks) {
        this.lastNweeks = lastNweeks;
    }

    public Integer getLastNyears() {
        return lastNyears;
    }

    public void setLastNyears(Integer lastNyears) {
        this.lastNyears = lastNyears;
    }
    
    public Integer getDatasetId() {
        return datasetId;
    }

    public void setDatasetId(Integer datasetId) {
        this.datasetId = datasetId;
    }

    public List<DatasetColumnBean> getTableColumns() {
        return tableColumns;
    }

    public void setTableColumns(List<DatasetColumnBean> tableColumns) {
        this.tableColumns = tableColumns;
    }

    public Integer getId() {
        return Id;
    }

    public void setId(Integer Id) {
        this.Id = Id;
    }

    @Override
    public String toString() {
        return "DatasetColumnBean{" + "Id=" + Id + ", fieldName=" + fieldName + ", displayName=" + displayName + ", fieldType=" + fieldType + ", status=" + status + ", functionName=" + functionName + ", expression=" + expression + ", displayFormat=" + displayFormat + ", datasetId=" + datasetId + ", tableColumns=" + tableColumns + '}';
    }

}
