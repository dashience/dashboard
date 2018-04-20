/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.bean;

import java.util.List;
import java.util.Map;

/**
 *
 * @author varghees
 */
public class DataSourceMetric {
    private String reportName;
    private Map<String, String> metricsAndOrder; // Holds metric and corresponding order

    public String getReportName() {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public Map<String, String> getMetricsAndOrder() {
        return metricsAndOrder;
    }

    public void setMetricsAndOrder(Map<String, String> metricsAndOrder) {
        this.metricsAndOrder = metricsAndOrder;
    }
    
}
