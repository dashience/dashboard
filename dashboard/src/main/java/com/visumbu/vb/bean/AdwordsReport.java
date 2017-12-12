/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.bean;

import com.google.api.ads.adwords.lib.jaxb.v201710.ReportDefinitionReportType;

/**
 *
 * @author user
 */
public class AdwordsReport {

    private String reportName;
    private String[] fields;
    private ReportDefinitionReportType reportType;

    public AdwordsReport(String reportName, String[] fields, ReportDefinitionReportType reportType) {
        this.reportName = reportName;
        this.fields = fields;
        this.reportType = reportType;
    }

    public String getReportName() {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public String[] getFields() {
        return fields;
    }

    public void setFields(String[] fields) {
        this.fields = fields;
    }

    public ReportDefinitionReportType getReportType() {
        return reportType;
    }

    public void setReportType(ReportDefinitionReportType reportType) {
        this.reportType = reportType;
    }

    @Override
    public String toString() {
        return "AdwordsReport{" + "reportName=" + reportName + ", fields=" + fields + ", reportType=" + reportType + '}';
    }

}
