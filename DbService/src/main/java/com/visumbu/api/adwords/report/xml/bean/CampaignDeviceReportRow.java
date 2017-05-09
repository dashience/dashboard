/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.api.adwords.report.xml.bean;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author duc-dev-04
 */
@XmlRootElement(name = "row")
public class CampaignDeviceReportRow {

    private String campaignID;
    private String account;
    private String campaign;
    private String videoViews;
    private String videoViewRate;
    private String videoQuartile100Rate;
    private String videoQuartile25Rate;
    private String videoQuartile50Rate;
    private String videoQuartile75Rate;
    private String impressions;
    private String clicks;
    private String day;
    private String searchExactMatchIS;
    private String searchLostISBudget;
    private String searchLostISRank;
    private String conversions;
    private String searchImprShare;
    private String avgPosition;
    private String allConv;
    private String phoneCalls;
    private String avgCPC;
    private String ctr;
    private String cost;
    private String costConv;
    private String budget;
    private String convRate;
    private String device;

    public String getAvgCPC() {
        try {
            return Double.toString(Double.parseDouble(avgCPC) / 1000000);
        } catch (Exception e) {
            return "0";
        }
    }

    @XmlAttribute
    public void setAvgCPC(String avgCPC) {
        this.avgCPC = avgCPC;
    }

    public String getCtr() {
        return ctr.replaceAll("%", "");
    }

    @XmlAttribute
    public void setCtr(String ctr) {
        this.ctr = ctr;
    }

    public String getCost() {
        try {
            return Double.toString(Double.parseDouble(cost) / 1000000);
        } catch (Exception e) {
            return "0";
        }
    }

    @XmlAttribute
    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getDevice() {
        return device;
    }

    @XmlAttribute
    public void setDevice(String device) {
        this.device = device;
    }

    public String getCampaignID() {
        return campaignID;
    }

    @XmlAttribute
    public void setCampaignID(String campaignID) {
        this.campaignID = campaignID;
    }

    public String getAccount() {
        return account;
    }

    @XmlAttribute
    public void setAccount(String account) {
        this.account = account;
    }

    public String getCampaign() {
        return campaign;
    }

    @XmlAttribute
    public void setCampaign(String campaign) {
        this.campaign = campaign;
    }

    public String getVideoViews() {
        return videoViews;
    }

    @XmlAttribute
    public void setVideoViews(String videoViews) {
        this.videoViews = videoViews;
    }

    public String getVideoViewRate() {
        return videoViewRate;
    }

    @XmlAttribute
    public void setVideoViewRate(String videoViewRate) {
        this.videoViewRate = videoViewRate;
    }

    public String getVideoQuartile100Rate() {
        return videoQuartile100Rate;
    }

    @XmlAttribute
    public void setVideoQuartile100Rate(String videoQuartile100Rate) {
        this.videoQuartile100Rate = videoQuartile100Rate;
    }

    public String getVideoQuartile25Rate() {
        return videoQuartile25Rate;
    }

    @XmlAttribute
    public void setVideoQuartile25Rate(String videoQuartile25Rate) {
        this.videoQuartile25Rate = videoQuartile25Rate;
    }

    public String getVideoQuartile50Rate() {
        return videoQuartile50Rate;
    }

    @XmlAttribute
    public void setVideoQuartile50Rate(String videoQuartile50Rate) {
        this.videoQuartile50Rate = videoQuartile50Rate;
    }

    public String getVideoQuartile75Rate() {
        return videoQuartile75Rate;
    }

    @XmlAttribute
    public void setVideoQuartile75Rate(String videoQuartile75Rate) {
        this.videoQuartile75Rate = videoQuartile75Rate;
    }

    public String getImpressions() {
        return impressions;
    }

    @XmlAttribute
    public void setImpressions(String impressions) {
        this.impressions = impressions;
    }

    public String getClicks() {
        return clicks;
    }

    @XmlAttribute
    public void setClicks(String clicks) {
        this.clicks = clicks;
    }

    public String getDay() {
        return day;
    }

    @XmlAttribute
    public void setDay(String day) {
        this.day = day;
    }

    public String getSearchExactMatchIS() {
        return searchExactMatchIS;
    }

    @XmlAttribute
    public void setSearchExactMatchIS(String searchExactMatchIS) {
        this.searchExactMatchIS = searchExactMatchIS;
    }

    public String getSearchLostISBudget() {
        return searchLostISBudget;
    }

    @XmlAttribute
    public void setSearchLostISBudget(String searchLostISBudget) {
        this.searchLostISBudget = searchLostISBudget;
    }

    public String getSearchLostISRank() {
        return searchLostISRank;
    }

    @XmlAttribute
    public void setSearchLostISRank(String searchLostISRank) {
        this.searchLostISRank = searchLostISRank;
    }

    public String getConversions() {
        return conversions;
    }

    @XmlAttribute
    public void setConversions(String conversions) {
        this.conversions = conversions;
    }

    public String getSearchImprShare() {
        return searchImprShare;
    }

    @XmlAttribute
    public void setSearchImprShare(String searchImprShare) {
        this.searchImprShare = searchImprShare;
    }

    public String getAvgPosition() {
        return avgPosition;
    }

    @XmlAttribute
    public void setAvgPosition(String avgPosition) {
        this.avgPosition = avgPosition;
    }

    public String getAllConv() {
        return allConv;
    }

    @XmlAttribute
    public void setAllConv(String allConv) {
        this.allConv = allConv;
    }

    public String getPhoneCalls() {
        return phoneCalls;
    }

    @XmlAttribute
    public void setPhoneCalls(String phoneCalls) {
        this.phoneCalls = phoneCalls;
    }

    public String getCostConv() {
        try {
            return Double.toString(Double.parseDouble(costConv) / 1000000);
        } catch (Exception e) {
            return "0";
        }
    }

    @XmlAttribute
    public void setCostConv(String costConv) {
        this.costConv = costConv;
    }

    public String getBudget() {
        return budget;
    }

    @XmlAttribute
    public void setBudget(String budget) {
        this.budget = budget;
    }

    public String getConvRate() {
        return convRate;
    }

    @XmlAttribute
    public void setConvRate(String convRate) {
        this.convRate = convRate;
    }

    @Override
    public String toString() {
        return "CampaignDeviceReportRow{" + "campaignID=" + campaignID + ", account=" + account + ", campaign=" + campaign + ", videoViews=" + videoViews + ", videoViewRate=" + videoViewRate + ", videoQuartile100Rate=" + videoQuartile100Rate + ", videoQuartile25Rate=" + videoQuartile25Rate + ", videoQuartile50Rate=" + videoQuartile50Rate + ", videoQuartile75Rate=" + videoQuartile75Rate + ", impressions=" + impressions + ", clicks=" + clicks + ", day=" + day + ", searchExactMatchIS=" + searchExactMatchIS + ", searchLostISBudget=" + searchLostISBudget + ", searchLostISRank=" + searchLostISRank + ", conversions=" + conversions + ", searchImprShare=" + searchImprShare + ", avgPosition=" + avgPosition + ", allConv=" + allConv + ", phoneCalls=" + phoneCalls + ", avgCPC=" + avgCPC + ", ctr=" + ctr + ", cost=" + cost + ", costConv=" + costConv + ", budget=" + budget + ", convRate=" + convRate + '}';
    }
}
