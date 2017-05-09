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
 * @author user
 */
@XmlRootElement(name = "report")
public class CampaignReport {
    private List<CampaignReportRow> campaignReportRow;

    public List<CampaignReportRow> getCampaignReportRow() {
        return campaignReportRow;
    }

    @XmlElementWrapper(name = "table")
    @XmlElement(name = "row")
    public void setCampaignReportRow(List<CampaignReportRow> campaignReportRow) {
        this.campaignReportRow = campaignReportRow;
    }

    @Override
    public String toString() {
        return "CampaignReport{" + "campaignReportRow=" + campaignReportRow + '}';
    }
}
