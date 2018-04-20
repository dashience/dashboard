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
public class JoinDataSetBean {

    private Integer id;
    private String dataSetName;
    private Integer dataSetIdFirst;
    private Integer dataSetIdSecond;
    private String operationType;
    private List<JoinDataSetBean> conditionFields;
    private Integer conditionId;
    private String conditionFieldFirst;
    private String conditionFieldSecond;
    private String columnName;

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

    public Integer getConditionId() {
        return conditionId;
    }

    public void setConditionId(Integer conditionId) {
        this.conditionId = conditionId;
    }

    public List<JoinDataSetBean> getConditionFields() {
        return conditionFields;
    }

    public void setConditionFields(List<JoinDataSetBean> conditionFields) {
        this.conditionFields = conditionFields;
    }

    public String getConditionFieldFirst() {
        return conditionFieldFirst;
    }

    public void setConditionFieldFirst(String conditionFieldFirst) {
        this.conditionFieldFirst = conditionFieldFirst;
    }

    public String getConditionFieldSecond() {
        return conditionFieldSecond;
    }

    public void setConditionFieldSecond(String conditionFieldSecond) {
        this.conditionFieldSecond = conditionFieldSecond;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    } 

}
