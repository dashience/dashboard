/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.api.adwords.report.xml.bean;

import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author duc-dev-04
 */
@XmlRootElement(name = "report")
public class CampaignPerformanceReport {
    private List<CampaignPerformanceReportRow> campaignPerformanceReportRow;

    public List<CampaignPerformanceReportRow> getCampaignPerformanceReportRow() {
        return campaignPerformanceReportRow;
    }

    @XmlElementWrapper(name = "table")
    @XmlElement(name = "row")
    public void setCampaignPerformanceReportRow(List<CampaignPerformanceReportRow> campaignPerformanceReportRow) {
        this.campaignPerformanceReportRow = campaignPerformanceReportRow;
    }
    

    @Override
    public String toString() {
        return "CampaignPerformanceReport{" + "campaignPerformanceReportRow=" + campaignPerformanceReportRow + '}';
    }
}
