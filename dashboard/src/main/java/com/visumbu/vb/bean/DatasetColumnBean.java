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
    private String displayFormat;
    private Integer datasetId;
    private String column;
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

    public String getDisplayFormat() {
        return displayFormat;
    }

    public void setDisplayFormat(String displayFormat) {
        this.displayFormat = displayFormat;
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

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    @Override
    public String toString() {
        return "DatasetColumnBean{" + "Id=" + Id + ", fieldName=" + fieldName + ", displayName=" + displayName + ", fieldType=" + fieldType + ", status=" + status + ", functionName=" + functionName + ", expression=" + expression + ", displayFormat=" + displayFormat + ", datasetId=" + datasetId + ", column=" + column + ", tableColumns=" + tableColumns + '}';
    }

}
