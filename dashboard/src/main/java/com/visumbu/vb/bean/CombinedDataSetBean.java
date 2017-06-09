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
public class CombinedDataSetBean {

    private Integer id;
    private String dataSetName;
    private Integer dataSetIdFirst;
    private Integer dataSetIdSecond;
    private String operationType;
    private List<CombinedDataSetBean> conditionFields;
    private String firstDataSetColumn;
    private String secondDataSetColumn;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    public String getDataSetName() {
        return dataSetName;
    }

    public void setDataSetName(String dataSetName) {
        this.dataSetName = dataSetName;
    }

    public Integer getDataSetIdFirst() {
        return dataSetIdFirst;
    }

    public void setDataSetIdFirst(Integer dataSetIdFirst) {
        this.dataSetIdFirst = dataSetIdFirst;
    }

    public Integer getDataSetIdSecond() {
        return dataSetIdSecond;
    }

    public void setDataSetIdSecond(Integer dataSetIdSecond) {
        this.dataSetIdSecond = dataSetIdSecond;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public List<CombinedDataSetBean> getConditionFields() {
        return conditionFields;
    }

    public void setConditionFields(List<CombinedDataSetBean> conditionFields) {
        this.conditionFields = conditionFields;
    }

    public String getFirstDataSetColumn() {
        return firstDataSetColumn;
    }

    public void setFirstDataSetColumn(String firstDataSetColumn) {
        this.firstDataSetColumn = firstDataSetColumn;
    }

    public String getSecondDataSetColumn() {
        return secondDataSetColumn;
    }

    public void setSecondDataSetColumn(String secondDataSetColumn) {
        this.secondDataSetColumn = secondDataSetColumn;
    }

}
