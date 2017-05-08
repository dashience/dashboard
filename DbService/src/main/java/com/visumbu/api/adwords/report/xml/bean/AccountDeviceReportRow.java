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
public class AccountDeviceReportRow {

    private String videoViews;
    private String videoViewRate;
    private String account;
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
    private String avgCPC;
    private String ctr;
    private String cost;
    private String costConv;
    private String SearchBudgetLostImpressionShare;
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

    public String getAccount() {
        return account;
    }

    @XmlAttribute
    public void setAccount(String account) {
        this.account = account;
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
        return searchImprShare.replaceAll("%", "");
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

    public String getSearchBudgetLostImpressionShare() {
        return SearchBudgetLostImpressionShare;
    }

    @XmlAttribute
    public void setSearchBudgetLostImpressionShare(String SearchBudgetLostImpressionShare) {
        this.SearchBudgetLostImpressionShare = SearchBudgetLostImpressionShare;
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
        return "AccountDeviceReportRow{" + "videoViews=" + videoViews + ", videoViewRate=" + videoViewRate + ", account=" + account + ", impressions=" + impressions + ", clicks=" + clicks + ", day=" + day + ", searchExactMatchIS=" + searchExactMatchIS + ", searchLostISBudget=" + searchLostISBudget + ", searchLostISRank=" + searchLostISRank + ", conversions=" + conversions + ", searchImprShare=" + searchImprShare + ", avgPosition=" + avgPosition + ", allConv=" + allConv + ", avgCPC=" + avgCPC + ", ctr=" + ctr + ", cost=" + cost + ", costConv=" + costConv + ", SearchBudgetLostImpressionShare=" + SearchBudgetLostImpressionShare + ", convRate=" + convRate + '}';
    }

}
