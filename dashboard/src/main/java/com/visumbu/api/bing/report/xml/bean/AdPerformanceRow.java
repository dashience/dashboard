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
public class AdPerformanceRow {

    private Data accountName;
    private Data accountId;
    private Data clicks;
    private Data impressions;
    private Data averageCpc;
    private Data ctr;
    private Data spend;
    private Data conversions;
    private Data costPerConversion;
    private Data conversionRate;
    private Data finalUrl;
    private Data campaignName;
    private Data adGroupName;
    private Data timePeriod;
    private Data adDescription;
    private Data adTitle;
    private Data averagePosition;
    private Data campaignId;
    private Data adGroupId;
    private Data displayUrl;
    private Data destinationUrl;
    
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

    public Data getAdGroupId() {
        return adGroupId;
    }

    @XmlElement(name = "AdGroupId")
    public void setAdGroupId(Data adGroupId) {
        this.adGroupId = adGroupId;
    }

    public Data getAdGroupName() {
        return adGroupName;
    }

    @XmlElement(name = "AdGroupName")
    public void setAdGroupName(Data adGroupName) {
        this.adGroupName = adGroupName;
    }

    public Data getFinalUrl() {
        return finalUrl;
    }

    @XmlElement(name = "FinalUrl")
    public void setFinalUrl(Data finalUrl) {
        this.finalUrl = finalUrl;
    }

    public Data getAdDescription() {
        return adDescription;
    }

    @XmlElement(name = "AdDescription")
    public void setAdDescription(Data adDescription) {
        this.adDescription = adDescription;
    }

    public Data getAdTitle() {
        return adTitle;
    }

    @XmlElement(name = "AdTitle")
    public void setAdTitle(Data adTitle) {
        this.adTitle = adTitle;
    }

    public Data getDisplayUrl() {
        return displayUrl;
    }

    @XmlElement(name = "DisplayUrl")
    public void setDisplayUrl(Data displayUrl) {
        this.displayUrl = displayUrl;
    }

    public Data getDestinationUrl() {
        return destinationUrl;
    }

    @XmlElement(name = "DestinationUrl")
    public void setDestinationUrl(Data destinationUrl) {
        this.destinationUrl = destinationUrl;
    }

    @Override
    public String toString() {
        return "AdPerformanceRow{" + "accountName=" + accountName + ", accountId=" + accountId + ", clicks=" + clicks + ", impressions=" + impressions + ", averageCpc=" + averageCpc + ", ctr=" + ctr + ", spend=" + spend + ", conversions=" + conversions + ", costPerConversion=" + costPerConversion + ", conversionRate=" + conversionRate + ", finalUrl=" + finalUrl + ", campaignName=" + campaignName + ", adGroupName=" + adGroupName + ", timePeriod=" + timePeriod + ", adDescription=" + adDescription + ", adTitle=" + adTitle + ", averagePosition=" + averagePosition + ", campaignId=" + campaignId + ", adGroupId=" + adGroupId + ", displayUrl=" + displayUrl + ", destinationUrl=" + destinationUrl + '}';
    }
    
    
}
