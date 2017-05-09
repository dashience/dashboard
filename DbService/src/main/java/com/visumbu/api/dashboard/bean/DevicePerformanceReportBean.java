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
public class DevicePerformanceReportBean {

    private String device;
    private String deviceType;
    private String campaign;
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
    private String directionsPageView; // goal1
    private String inventoryPageViews; // goal2
    private String leadSubmission; //goal 3
    private String specialsPageView; //goal 4
    private String timeOnSiteGt2Mins; // goal5
    private String vdpViews; // goal 6
    private String engagements; 

    public String getCampaign() {
        return campaign;
    }

    public void setCampaign(String campaign) {
        this.campaign = campaign;
    }
    
    public String getEngagements() {
        return engagements;
    }

    public void setEngagements(String engagements) {
        this.engagements = engagements;
    }
    
    public String getDirectionsPageView() {
        return directionsPageView;
    }

    public void setDirectionsPageView(String directionsPageView) {
        this.directionsPageView = directionsPageView;
    }

    public String getInventoryPageViews() {
        return inventoryPageViews;
    }

    public void setInventoryPageViews(String inventoryPageViews) {
        this.inventoryPageViews = inventoryPageViews;
    }

    public String getLeadSubmission() {
        return leadSubmission;
    }

    public void setLeadSubmission(String leadSubmission) {
        this.leadSubmission = leadSubmission;
    }

    public String getSpecialsPageView() {
        return specialsPageView;
    }

    public void setSpecialsPageView(String specialsPageView) {
        this.specialsPageView = specialsPageView;
    }

    public String getTimeOnSiteGt2Mins() {
        return timeOnSiteGt2Mins;
    }

    public void setTimeOnSiteGt2Mins(String timeOnSiteGt2Mins) {
        this.timeOnSiteGt2Mins = timeOnSiteGt2Mins;
    }

    public String getVdpViews() {
        return vdpViews;
    }

    public void setVdpViews(String vdpViews) {
        this.vdpViews = vdpViews;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }
    
    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
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
