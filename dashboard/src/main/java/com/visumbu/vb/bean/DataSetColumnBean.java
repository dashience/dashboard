/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.bean;

import com.visumbu.vb.model.TabWidget;
import com.visumbu.vb.model.VbUser;
import java.util.List;

/**
 *
 * @author deeta1
 */
public class DataSetColumnBean {

    private Integer id;
    private String fieldName;
    private String displayName;
    private String fieldType;
    private String type;
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
    private Integer dataSetId;
    private Integer sortPriority;
    private String agregationFunction;
    private Integer groupPriority;
    private String sortOrder;
    private List<DataSetColumnBean> tableColumns;
    private VbUser userId;
    private TabWidget widgetId;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public Integer getDataSetId() {
        return dataSetId;
    }

    public void setDataSetId(Integer dataSetId) {
        this.dataSetId = dataSetId;
    }

    public List<DataSetColumnBean> getTableColumns() {
        return tableColumns;
    }

    public void setTableColumns(List<DataSetColumnBean> tableColumns) {
        this.tableColumns = tableColumns;
    }

    public Integer getSortPriority() {
        return sortPriority;
    }

    public void setSortPriority(Integer sortPriority) {
        this.sortPriority = sortPriority;
    }

    public String getAgregationFunction() {
        return agregationFunction;
    }

    public void setAgregationFunction(String agregationFunction) {
        this.agregationFunction = agregationFunction;
    }

    public Integer getGroupPriority() {
        return groupPriority;
    }

    public void setGroupPriority(Integer groupPriority) {
        this.groupPriority = groupPriority;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public VbUser getUserId() {
        return userId;
    }

    public void setUserId(VbUser userId) {
        this.userId = userId;
    }   

    public TabWidget getWidgetId() {
        return widgetId;
    }

    public void setWidgetId(TabWidget widgetId) {
        this.widgetId = widgetId;
    }

    @Override
    public String toString() {
        return "DatasetColumnBean{" + "id=" + id + ", fieldName=" + fieldName + ", displayName=" + displayName + ", type=" + type + ", status=" + status + ", expression=" + expression + ", functionName=" + functionName + ", columnName=" + columnName + ", baseField=" + baseField + ", displayFormat=" + displayFormat + ", customEndDate=" + customEndDate + ", customStartDate=" + customStartDate + ", dateRangeName=" + dateRangeName + ", lastNdays=" + lastNdays + ", lastNmonths=" + lastNmonths + ", lastNweeks=" + lastNweeks + ", lastNyears=" + lastNyears + ", dataSetId=" + dataSetId + ", sortPriority=" + sortPriority + ", tableColumns=" + tableColumns + '}';
    }

}
