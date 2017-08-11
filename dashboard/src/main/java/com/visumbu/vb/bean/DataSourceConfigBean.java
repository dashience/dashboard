/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.bean;

/**
 *
 * @author duc-dev-03
 */
public class DataSourceConfigBean {
    private Integer id;
    private Integer dataSetConfigId;
    private Integer dataSourceId;
    private String metrics;
    private String alias;
    private String orderBy;

    public String getMetrics() {
        return metrics;
    }

    public void setMetrics(String metrics) {
        this.metrics = metrics;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getDataSetConfigId() {
        return dataSetConfigId;
    }

    public void setDataSetConfigId(Integer dataSetConfigId) {
        this.dataSetConfigId = dataSetConfigId;
    }
    
    public Integer getDataSourceId() {
        return dataSourceId;
    }

    public void setDataSourceId(Integer dataSourceId) {
        this.dataSourceId = dataSourceId;
    }
    
}
