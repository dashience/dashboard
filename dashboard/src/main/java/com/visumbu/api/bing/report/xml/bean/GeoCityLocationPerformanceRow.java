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
public class GeoCityLocationPerformanceRow {
    private Data clicks;
    private Data impressions;
    private Data averageCpc;
    private Data ctr;
    private Data spend;
    private Data conversions;
    private Data costPerConversion;
    private Data conversionRate;
    private Data timePeriod;
    private Data city;
    private Data country;
    private Data accountName;
    private Data state;
    private Data averagePosition;

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

    public Data getAverageCpc() {
        return averageCpc;
    }

    @XmlElement(name = "AverageCpc")
    public void setAverageCpc(Data averageCpc) {
        this.averageCpc = averageCpc;
    }

    public Data getCtr() {
        return ctr;
    }

    @XmlElement(name = "Ctr")
    public void setCtr(Data ctr) {
        this.ctr = ctr;
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

    public Data getTimePeriod() {
        return timePeriod;
    }

    @XmlElement(name = "TimePeriod")
    public void setTimePeriod(Data timePeriod) {
        this.timePeriod = timePeriod;
    }

    public Data getCity() {
        return city;
    }

    @XmlElement(name = "City")
    public void setCity(Data city) {
        this.city = city;
    }

    public Data getCountry() {
        return country;
    }

    @XmlElement(name = "Country")
    public void setCountry(Data country) {
        this.country = country;
    }

    public Data getAccountName() {
        return accountName;
    }

    @XmlElement(name = "AccountName")
    public void setAccountName(Data accountName) {
        this.accountName = accountName;
    }

    public Data getState() {
        return state;
    }

    @XmlElement(name = "State")
    public void setState(Data state) {
        this.state = state;
    }

    public Data getAveragePosition() {
        return averagePosition;
    }

    @XmlElement(name = "AveragePosition")
    public void setAveragePosition(Data averagePosition) {
        this.averagePosition = averagePosition;
    }

    @Override
    public String toString() {
        return "GeoCityLocationPerformanceRow{" + "clicks=" + clicks + ", impressions=" + impressions + ", averageCpc=" + averageCpc + ", ctr=" + ctr + ", spend=" + spend + ", conversions=" + conversions + ", costPerConversion=" + costPerConversion + ", conversionRate=" + conversionRate + ", timePeriod=" + timePeriod + ", city=" + city + ", country=" + country + ", accountName=" + accountName + ", state=" + state + ", averagePosition=" + averagePosition + '}';
    }    
}
