/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.admin.scheduler.service;

import com.visumbu.vb.model.DataSet;

/**
 *
 * @author dashience
 */
public class SchedulerTemplate {

    private String cron;
    private String accountId;
    private DataSet dataSet;

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public DataSet getDataSet() {
        return dataSet;
    }

    public void setDataSet(DataSet dataSet) {
        this.dataSet = dataSet;
    }

    public String getCron() {
        return cron;
    }

    public void setCron(String cron) {
        this.cron = cron;
    }

//    public String getDataSetName() {
//        return dataSetName;
//    }
//
//    public void setDataSetName(String dataSetName) {
//        this.dataSetName = dataSetName;
//    }
//
//    public String getDataSourceName() {
//        return dataSourceName;
//    }
//
//    public void setDataSourceName(String dataSourceName) {
//        this.dataSourceName = dataSourceName;
//    }
//    private String cron;
//    private String dataSetName;
//    private String dataSourceName;
//    private String accountId;
//    private String userId;
//    private DataSet dataSet;
//
//    public String getUserId() {
//        return userId;
//    }
//
//    public void setUserId(String userId) {
//        this.userId = userId;
//    }
//
//    public String getAccountId() {
//        return accountId;
//    }
//
//    public void setAccountId(String accountId) {
//        this.accountId = accountId;
//    }
}
