/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.api.bing.report.xml.bean;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author duc-dev-04
 */
@XmlRootElement
public class CampaignPerformanceRow {

    private Data impressions;
    private Data accountId;
    private Data clicks;
    private Data ctr;
    private Data averageCpc;
    private Data spend;
    private Data conversions;
    private Data conversionRate;
    private Data costPerConversion;
    private Data accountName;
    private Data impressionSharePercent;
    private Data impressionLostToBudgetPercent;
    private Data averagePosition;
    private Data timePeriod;
    private Data campaignName;
    private Data campaignId;
    private Data phoneCalls;

    public Data getImpressions() {
        return impressions;
    }

    @XmlElement(name = "Impressions")
    public void setImpressions(Data impressions) {
        this.impressions = impressions;
    }

    public Data getAccountId() {
        return accountId;
    }

    @XmlElement(name = "AccountId")
    public void setAccountId(Data accountId) {
        this.accountId = accountId;
    }

    public Data getClicks() {
        return clicks;
    }

    @XmlElement(name = "Clicks")
    public void setClicks(Data clicks) {
        this.clicks = clicks;
    }

    public Data getCtr() {
        return ctr;
    }

    @XmlElement(name = "Ctr")
    public void setCtr(Data ctr) {
        this.ctr = ctr;
    }

    public Data getAverageCpc() {
        return averageCpc;
    }

    @XmlElement(name = "AverageCpc")
    public void setAverageCpc(Data averageCpc) {
        this.averageCpc = averageCpc;
    }

    public Data getSpend() {
        return spend;
    }

    @XmlElement(name = "Spend")
    public void setSpend(Data spend) {
        this.spend = spend;
    }

    public Data getConversions() {
        return conversions;
    }

    @XmlElement(name = "Conversions")
    public void setConversions(Data conversions) {
        this.conversions = conversions;
    }

    public Data getConversionRate() {
        return conversionRate;
    }

    @XmlElement(name = "ConversionRate")
    public void setConversionRate(Data conversionRate) {
        this.conversionRate = conversionRate;
    }

    public Data getCostPerConversion() {
        return costPerConversion;
    }

    @XmlElement(name = "CostPerConversion")
    public void setCostPerConversion(Data costPerConversion) {
        this.costPerConversion = costPerConversion;
    }

    public Data getAccountName() {
        return accountName;
    }

    @XmlElement(name = "AccountName")
    public void setAccountName(Data accountName) {
        this.accountName = accountName;
    }

    public Data getImpressionSharePercent() {
        return impressionSharePercent;
    }

    @XmlElement(name = "ImpressionSharePercent")
    public void setImpressionSharePercent(Data impressionSharePercent) {
        this.impressionSharePercent = impressionSharePercent;
    }

    public Data getImpressionLostToBudgetPercent() {
        return impressionLostToBudgetPercent;
    }

    @XmlElement(name = "ImpressionLostToBudgetPercent")
    public void setImpressionLostToBudgetPercent(Data impressionLostToBudgetPercent) {
        this.impressionLostToBudgetPercent = impressionLostToBudgetPercent;
    }

    public Data getAveragePosition() {
        return averagePosition;
    }

    @XmlElement(name = "AveragePosition")
    public void setAveragePosition(Data averagePosition) {
        this.averagePosition = averagePosition;
    }

    public Data getTimePeriod() {
        return timePeriod;
    }

    @XmlElement(name = "TimePeriod")
    public void setTimePeriod(Data timePeriod) {
        this.timePeriod = timePeriod;
    }

    public Data getCampaignName() {
        return campaignName;
    }

    @XmlElement(name = "CampaignName")
    public void setCampaignName(Data campaignName) {
        this.campaignName = campaignName;
    }

    public Data getCampaignId() {
        return campaignId;
    }

    @XmlElement(name = "CampaignId")
    public void setCampaignId(Data campaignId) {
        this.campaignId = campaignId;
    }

    public Data getPhoneCalls() {
        return phoneCalls;
    }

    @XmlElement(name = "PhoneCalls")
    public void setPhoneCalls(Data phoneCalls) {
        this.phoneCalls = phoneCalls;
    }

    @Override
    public String toString() {
        return "CampaignPerformanceRow{" + "impressions=" + impressions + ", accountId=" + accountId + ", clicks=" + clicks + ", ctr=" + ctr + ", averageCpc=" + averageCpc + ", spend=" + spend + ", conversions=" + conversions + ", conversionRate=" + conversionRate + ", costPerConversion=" + costPerConversion + ", accountName=" + accountName + ", impressionSharePercent=" + impressionSharePercent + ", impressionLostToBudgetPercent=" + impressionLostToBudgetPercent + ", averagePosition=" + averagePosition + ", timePeriod=" + timePeriod + ", campaignName=" + campaignName + ", campaignId=" + campaignId + ", phoneCalls=" + phoneCalls + '}';
    }   
    
}
