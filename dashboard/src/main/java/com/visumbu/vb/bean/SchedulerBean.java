/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.bean;

import com.visumbu.vb.model.Account;
import com.visumbu.vb.model.Agency;
import com.visumbu.vb.model.Report;

/**
 *
 * @author duc-dev-04
 */
public class SchedulerBean {

    private Integer id;
    private String schedulerName;
    private String startDate;
    private String endDate;
    private String schedulerWeekly;
    private String schedulerTime;
    private String schedulerMonthly;
    private Integer schedulerYearOfWeek;
    private String schedulerRepeatType;
    private String schedulerNow;
    private String schedulerYearly;
    private String schedulerStatus;
    private String schedulerType;
    private String customEndDate;
    private String customStartDate;
    private String dateRangeName;
    private Integer lastNdays;
    private Integer lastNmonths;
    private Integer lastNweeks;
    private Integer lastNyears;
    private String schedulerEmail;
    private Boolean isAccountEmail;
    private String status;
    private String lastExecutionStatus;
    private Report reportId;
    private Agency agencyId;
    private Account accountId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }    

    public String getSchedulerName() {
        return schedulerName;
    }

    public void setSchedulerName(String schedulerName) {
        this.schedulerName = schedulerName;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getSchedulerWeekly() {
        return schedulerWeekly;
    }

    public void setSchedulerWeekly(String schedulerWeekly) {
        this.schedulerWeekly = schedulerWeekly;
    }

    public String getSchedulerTime() {
        return schedulerTime;
    }

    public void setSchedulerTime(String schedulerTime) {
        this.schedulerTime = schedulerTime;
    }

    public String getSchedulerMonthly() {
        return schedulerMonthly;
    }

    public void setSchedulerMonthly(String schedulerMonthly) {
        this.schedulerMonthly = schedulerMonthly;
    }

    public Integer getSchedulerYearOfWeek() {
        return schedulerYearOfWeek;
    }

    public void setSchedulerYearOfWeek(Integer schedulerYearOfWeek) {
        this.schedulerYearOfWeek = schedulerYearOfWeek;
    }

    public String getSchedulerRepeatType() {
        return schedulerRepeatType;
    }

    public void setSchedulerRepeatType(String schedulerRepeatType) {
        this.schedulerRepeatType = schedulerRepeatType;
    }

    public String getSchedulerNow() {
        return schedulerNow;
    }

    public void setSchedulerNow(String schedulerNow) {
        this.schedulerNow = schedulerNow;
    }

    public String getSchedulerYearly() {
        return schedulerYearly;
    }

    public void setSchedulerYearly(String schedulerYearly) {
        this.schedulerYearly = schedulerYearly;
    }

    public String getSchedulerStatus() {
        return schedulerStatus;
    }

    public void setSchedulerStatus(String schedulerStatus) {
        this.schedulerStatus = schedulerStatus;
    }

    public String getSchedulerType() {
        return schedulerType;
    }

    public void setSchedulerType(String schedulerType) {
        this.schedulerType = schedulerType;
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

    public String getSchedulerEmail() {
        return schedulerEmail;
    }

    public void setSchedulerEmail(String schedulerEmail) {
        this.schedulerEmail = schedulerEmail;
    }

    public Boolean getIsAccountEmail() {
        return isAccountEmail;
    }

    public void setIsAccountEmail(Boolean isAccountEmail) {
        this.isAccountEmail = isAccountEmail;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLastExecutionStatus() {
        return lastExecutionStatus;
    }

    public void setLastExecutionStatus(String lastExecutionStatus) {
        this.lastExecutionStatus = lastExecutionStatus;
    }

    public Report getReportId() {
        return reportId;
    }

    public void setReportId(Report reportId) {
        this.reportId = reportId;
    }

    public Agency getAgencyId() {
        return agencyId;
    }

    public void setAgencyId(Agency agencyId) {
        this.agencyId = agencyId;
    }

    public Account getAccountId() {
        return accountId;
    }

    public void setAccountId(Account accountId) {
        this.accountId = accountId;
    }

    @Override
    public String toString() {
        return "SchedulerBean{" + "id=" + id + ", schedulerName=" + schedulerName + ", startDate=" + startDate + ", endDate=" + endDate + ", schedulerWeekly=" + schedulerWeekly + ", schedulerTime=" + schedulerTime + ", schedulerMonthly=" + schedulerMonthly + ", schedulerYearOfWeek=" + schedulerYearOfWeek + ", schedulerRepeatType=" + schedulerRepeatType + ", schedulerNow=" + schedulerNow + ", schedulerYearly=" + schedulerYearly + ", schedulerStatus=" + schedulerStatus + ", schedulerType=" + schedulerType + ", customEndDate=" + customEndDate + ", customStartDate=" + customStartDate + ", dateRangeName=" + dateRangeName + ", lastNdays=" + lastNdays + ", lastNmonths=" + lastNmonths + ", lastNweeks=" + lastNweeks + ", lastNyears=" + lastNyears + ", schedulerEmail=" + schedulerEmail + ", isAccountEmail=" + isAccountEmail + ", status=" + status + ", lastExecutionStatus=" + lastExecutionStatus + ", reportId=" + reportId + ", agencyId=" + agencyId + ", accountId=" + accountId + '}';
    }
   
}
