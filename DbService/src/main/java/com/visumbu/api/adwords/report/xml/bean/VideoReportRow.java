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
public class VideoReportRow {

    private String account;
    private String campaign;
    private String device;
    private String impressions;
    private String clicks;
    private String day;
    private String week;
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
    private String dayOfWeek;
    private String hourOfDay;
    private String viewRate;
    private String views;
    private String videoPlayedTo100;
    private String videoPlayedTo25;
    private String videoPlayedTo50;
    private String videoPlayedTo75;
    private String videoTitle;

    public String getVideoTitle() {
        return videoTitle;
    }

    @XmlAttribute
    public void setVideoTitle(String videoTitle) {
        this.videoTitle = videoTitle;
    }

    public String getViewRate() {
        return viewRate.replaceAll("%", "");
    }

    @XmlAttribute
    public void setViewRate(String viewRate) {
        this.viewRate = viewRate;
    }

    public String getViews() {
        return views;
    }

    @XmlAttribute
    public void setViews(String views) {
        this.views = views;
    }

    public String getVideoPlayedTo100() {
        return videoPlayedTo100.replaceAll("%", "");
    }

    @XmlAttribute
    public void setVideoPlayedTo100(String videoPlayedTo100) {
        this.videoPlayedTo100 = videoPlayedTo100;
    }

    public String getVideoPlayedTo25() {
        return videoPlayedTo25.replaceAll("%", "");
    }

    @XmlAttribute
    public void setVideoPlayedTo25(String videoPlayedTo25) {
        this.videoPlayedTo25 = videoPlayedTo25;
    }

    public String getVideoPlayedTo50() {
        return videoPlayedTo50.replaceAll("%", "");
    }

    @XmlAttribute
    public void setVideoPlayedTo50(String videoPlayedTo50) {
        this.videoPlayedTo50 = videoPlayedTo50;
    }

    public String getVideoPlayedTo75() {
        return videoPlayedTo75.replaceAll("%", "");
    }

    @XmlAttribute
    public void setVideoPlayedTo75(String videoPlayedTo75) {
        this.videoPlayedTo75 = videoPlayedTo75;
    }

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

    public String getHourOfDay() {
        return hourOfDay;
    }

    @XmlAttribute
    public void setHourOfDay(String hourOfDay) {
        this.hourOfDay = hourOfDay;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    @XmlAttribute
    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public String getDevice() {
        return device;
    }

    @XmlAttribute
    public void setDevice(String device) {
        this.device = device;
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

    public String getWeek() {
        return week;
    }

    @XmlAttribute
    public void setWeek(String week) {
        this.week = week;
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
        return "AccountReportRow{account=" + account + ", impressions=" + impressions + ", clicks=" + clicks + ", day=" + day + ", searchExactMatchIS=" + searchExactMatchIS + ", searchLostISBudget=" + searchLostISBudget + ", searchLostISRank=" + searchLostISRank + ", conversions=" + conversions + ", searchImprShare=" + searchImprShare + ", avgPosition=" + avgPosition + ", allConv=" + allConv + ", avgCPC=" + avgCPC + ", ctr=" + ctr + ", cost=" + cost + ", costConv=" + costConv + ", SearchBudgetLostImpressionShare=" + SearchBudgetLostImpressionShare + ", convRate=" + convRate + ", dayOfWeek=" + dayOfWeek + '}';
    }

}
