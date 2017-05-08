/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.api.dashboard.bean;

/**
 *
 * @author user
 */
public class CampaignDevicePerformanceReportBean {
    private String campaignName;
    private String device;
    private String impressions;
    private String clicks;
    private String ctr;
    private String averagePosition;
    private String cost;
    private String averageCpc;
    private String conversions;
    private String cpa;
    private String searchImpressionsShare;
    private String source;

    public String getCampaignName() {
        return campaignName;
    }

    public void setCampaignName(String campaignName) {
        this.campaignName = campaignName;
    }
    
    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }
    
    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getImpressions() {
        return impressions;
    }

    public void setImpressions(String impressions) {
        this.impressions = impressions;
    }

    public String getClicks() {
        return clicks;
    }

    public void setClicks(String clicks) {
        this.clicks = clicks;
    }

    public String getCtr() {
        return ctr;
    }

    public void setCtr(String ctr) {
        this.ctr = ctr;
    }

    public String getAveragePosition() {
        return averagePosition;
    }

    public void setAveragePosition(String averagePosition) {
        this.averagePosition = averagePosition;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getAverageCpc() {
        return averageCpc;
    }

    public void setAverageCpc(String averageCpc) {
        this.averageCpc = averageCpc;
    }

    public String getConversions() {
        return conversions;
    }

    public void setConversions(String conversions) {
        this.conversions = conversions;
    }

    public String getCpa() {
        return cpa;
    }

    public void setCpa(String cpa) {
        this.cpa = cpa;
    }

    public String getSearchImpressionsShare() {
        return searchImpressionsShare;
    }

    public void setSearchImpressionsShare(String searchImpressionsShare) {
        this.searchImpressionsShare = searchImpressionsShare;
    }
    
    
}
