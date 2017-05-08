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
 * @author user
 */
@XmlRootElement
public class KeywordPerformanceRow {

    private Data gregorianDate;
    private Data accountId;
    private Data accountNumber;
    private Data accountName;
    private Data campaignId;
    private Data campaignName;
    private Data keyword;
    private Data keywordId;
    private Data devicetype;
    private Data biddedMatchType;
    private Data clicks;
    private Data impressions;
    private Data ctr;
    private Data averageCpc;
    private Data conversions;
    private Data costPerConversions;
    private Data conversionRate;
    private Data spend;
    private Data qualityScore;

    public Data getGregorianDate() {
        return gregorianDate;
    }

    @XmlElement(name = "GregorianDate")
    public void setGregorianDate(Data gregorianDate) {
        this.gregorianDate = gregorianDate;
    }

    public Data getAccountId() {
        return accountId;
    }

    @XmlElement(name = "AccountId")
    public void setAccountId(Data accountId) {
        this.accountId = accountId;
    }

    public Data getAccountNumber() {
        return accountNumber;
    }

    @XmlElement(name = "AccountNumber")
    public void setAccountNumber(Data accountNumber) {
        this.accountNumber = accountNumber;
    }

    public Data getAccountName() {
        return accountName;
    }

    @XmlElement(name = "AccountName")
    public void setAccountName(Data accountName) {
        this.accountName = accountName;
    }

    public Data getCampaignId() {
        return campaignId;
    }

    @XmlElement(name = "CampaignId")
    public void setCampaignId(Data campaignId) {
        this.campaignId = campaignId;
    }

    public Data getCampaignName() {
        return campaignName;
    }

    @XmlElement(name = "CampaignName")
    public void setCampaignName(Data campaignName) {
        this.campaignName = campaignName;
    }

    public Data getKeyword() {
        return keyword;
    }

    @XmlElement(name = "Keyword")
    public void setKeyword(Data keyword) {
        this.keyword = keyword;
    }

    public Data getKeywordId() {
        return keywordId;
    }

    @XmlElement(name = "KeywordId")
    public void setKeywordId(Data keywordId) {
        this.keywordId = keywordId;
    }

    public Data getDevicetype() {
        return devicetype;
    }

    @XmlElement(name = "Devicetype")
    public void setDevicetype(Data devicetype) {
        this.devicetype = devicetype;
    }

    public Data getBiddedMatchType() {
        return biddedMatchType;
    }

    @XmlElement(name = "BiddedMatchType")
    public void setBiddedMatchType(Data biddedMatchType) {
        this.biddedMatchType = biddedMatchType;
    }

    public Data getClicks() {
        return clicks;
    }

    @XmlElement(name = "Clicks")
    public void setClicks(Data clicks) {
        this.clicks = clicks;
    }

    public Data getImpressions() {
        return impressions;
    }

    @XmlElement(name = "Impressions")
    public void setImpressions(Data impressions) {
        this.impressions = impressions;
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

    @XmlElement(name = "AverateCpc")
    public void setAverageCpc(Data averageCpc) {
        this.averageCpc = averageCpc;
    }

    public Data getConversions() {
        return conversions;
    }

    @XmlElement(name = "Conversions")
    public void setConversions(Data conversions) {
        this.conversions = conversions;
    }

    public Data getCostPerConversions() {
        return costPerConversions;
    }

    @XmlElement(name = "CostPerConversions")
    public void setCostPerConversions(Data costPerConversions) {
        this.costPerConversions = costPerConversions;
    }

    public Data getConversionRate() {
        return conversionRate;
    }

    @XmlElement(name = "ConversionRate")
    public void setConversionRate(Data conversionRate) {
        this.conversionRate = conversionRate;
    }

    public Data getSpend() {
        return spend;
    }

    @XmlElement(name = "Spend")
    public void setSpend(Data spend) {
        this.spend = spend;
    }

    public Data getQualityScore() {
        return qualityScore;
    }

    @XmlElement(name = "QualityScore")
    public void setQualityScore(Data qualityScore) {
        this.qualityScore = qualityScore;
    }
    
    
    @Override
    public String toString() {
        return "KeywordPerformanceRow{" + "gregorianDate=" + gregorianDate + ", accountId=" + accountId + ", accountNumber=" + accountNumber + ", accountName=" + accountName + ", campaignId=" + campaignId + ", campaignName=" + campaignName + ", keyword=" + keyword + ", keywordId=" + keywordId + ", devicetype=" + devicetype + ", biddedMatchType=" + biddedMatchType + ", clicks=" + clicks + '}';
    }


}
