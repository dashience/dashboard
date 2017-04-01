/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.api.bing.report.xml.bean;

import java.util.List;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlNsForm;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchema;

/**
 *
 * @author user
 */

@XmlRootElement(name = "Report")
public class AccountHourOfDayPerformanceReport {

    private List<Column> keywordPerformanceReportColumns;
    private String reportName;
    private String xmlns;
    private String reportTime;
    private String timeZone;
    private String reportAggregation;
    private String lastCompletedAvailableDay;
    private String lastCompletedAvailableHour;
    private String potentialIncompleteData;
    private List<AccountHourOfDayPerformanceRow> accountHourOfDayPerformanceRows;

    public List<AccountHourOfDayPerformanceRow> getAccountHourOfDayPerformanceRows() {
        return accountHourOfDayPerformanceRows;
    }

    @XmlElementWrapper(name = "Table")
    @XmlElement(name = "Row")
    public void setAccountHourOfDayPerformanceRows(List<AccountHourOfDayPerformanceRow> accountHourOfDayPerformanceRows) {
        this.accountHourOfDayPerformanceRows = accountHourOfDayPerformanceRows;
    }
    
    public String getXmlns() {
        return xmlns;
    }

    @XmlAttribute(name = "xmlns")
    public void setXmlns(String xmlns) {
        this.xmlns = xmlns;
    }
    
    public String getReportName() {
        return reportName;
    }

    @XmlAttribute(name = "ReportName")
    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public String getReportTime() {
        return reportTime;
    }

    @XmlAttribute(name = "ReportTime")
    public void setReportTime(String reportTime) {
        this.reportTime = reportTime;
    }

    public String getTimeZone() {
        return timeZone;
    }

    @XmlAttribute(name = "TimeZone")
    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public String getReportAggregation() {
        return reportAggregation;
    }

    @XmlAttribute(name = "ReportAggregation")
    public void setReportAggregation(String reportAggregation) {
        this.reportAggregation = reportAggregation;
    }

    public String getLastCompletedAvailableDay() {
        return lastCompletedAvailableDay;
    }

    @XmlAttribute(name = "LastCompletedAvailableDay")
    public void setLastCompletedAvailableDay(String lastCompletedAvailableDay) {
        this.lastCompletedAvailableDay = lastCompletedAvailableDay;
    }

    public String getLastCompletedAvailableHour() {
        return lastCompletedAvailableHour;
    }

    @XmlAttribute(name = "LastCompletedAvailableHour")
    public void setLastCompletedAvailableHour(String lastCompletedAvailableHour) {
        this.lastCompletedAvailableHour = lastCompletedAvailableHour;
    }

    public String getPotentialIncompleteData() {
        return potentialIncompleteData;
    }

    @XmlAttribute(name = "PotentialIncompleteData")
    public void setPotentialIncompleteData(String potentialIncompleteData) {
        this.potentialIncompleteData = potentialIncompleteData;
    }

    public List<Column> getKeywordPerformanceReportColumns() {
        return keywordPerformanceReportColumns;
    }

    @XmlElement(name = "KeywordPerformanceReportColumns")
    public void setKeywordPerformanceReportColumns(List<Column> keywordPerformanceReportColumns) {
        this.keywordPerformanceReportColumns = keywordPerformanceReportColumns;
    }

    @Override
    public String toString() {
        return "AccountHourOfDayPerformanceReport{" + "keywordPerformanceReportColumns=" + keywordPerformanceReportColumns + ", reportName=" + reportName + ", xmlns=" + xmlns + ", reportTime=" + reportTime + ", timeZone=" + timeZone + ", reportAggregation=" + reportAggregation + ", lastCompletedAvailableDay=" + lastCompletedAvailableDay + ", lastCompletedAvailableHour=" + lastCompletedAvailableHour + ", potentialIncompleteData=" + potentialIncompleteData + ", accountHourOfDayPerformanceRows=" + accountHourOfDayPerformanceRows + '}';
    }
}
