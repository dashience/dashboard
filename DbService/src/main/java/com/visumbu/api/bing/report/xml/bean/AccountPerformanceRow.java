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
public class AccountPerformanceRow {

    private Data gregorianDate;
    private Data accountId;
    private Data accountName;
    private Data clicks;
    private Data impressions;
    private Data ctr;
    private Data averageCpc;
    private Data conversions;
    private Data costPerConversion;
    private Data conversionRate;
    private Data spend;
    private Data qualityScore;
    private Data impressionSharePercent;
    private Data impressionLostToBudgetPercent;
    private Data impressionLostToRankPercent;
    private Data averagePosition;
    private Data phoneCalls;
    private Data dayOfWeek;
    private Data hourOfDay;

    public Data getHourOfDay() {
        return hourOfDay;
    }

    @XmlElement(name = "HourOfDay")
    public void setHourOfDay(Data hourOfDay) {
        this.hourOfDay = hourOfDay;
    }

    public Data getDayOfWeek() {
        return dayOfWeek;
    }

    @XmlElement(name = "DayOfWeek")
    public void setDayOfWeek(Data dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }
    
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

    public Data getAccountName() {
        return accountName;
    }

    @XmlElement(name = "AccountName")
    public void setAccountName(Data accountName) {
        this.accountName = accountName;
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

    @XmlElement(name = "AverageCpc")
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

    public Data getCostPerConversion() {
        return costPerConversion;
    }

    @XmlElement(name = "CostPerConversion")
    public void setCostPerConversion(Data costPerConversion) {
        this.costPerConversion = costPerConversion;
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

    public Data getImpressionLostToRankPercent() {
        return impressionLostToRankPercent;
    }

    @XmlElement(name = "ImpressionLostToRankPercent")
    public void setImpressionLostToRankPercent(Data impressionLostToRankPercent) {
        this.impressionLostToRankPercent = impressionLostToRankPercent;
    }

    public Data getAveragePosition() {
        return averagePosition;
    }

    @XmlElement(name = "AveragePosition")
    public void setAveragePosition(Data averagePosition) {
        this.averagePosition = averagePosition;
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
        return "AccountPerformanceRow{" + "gregorianDate=" + gregorianDate + ", accountId=" + accountId + ", accountName=" + accountName + ", clicks=" + clicks + ", impressions=" + impressions + ", ctr=" + ctr + ", averageCpc=" + averageCpc + ", conversions=" + conversions + ", costPerConversion=" + costPerConversion + ", conversionRate=" + conversionRate + ", spend=" + spend + ", qualityScore=" + qualityScore + ", impressionSharePercent=" + impressionSharePercent + ", impressionLostToBudgetPercent=" + impressionLostToBudgetPercent + ", impressionLostToRankPercent=" + impressionLostToRankPercent + ", averagePosition=" + averagePosition + ", phoneCalls=" + phoneCalls + ", dayOfWeek=" + dayOfWeek + '}';
    }

}
