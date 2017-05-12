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
public class ClicksImpressionsHourOfDayBean {

    private String hourOfDay;
    private Integer impressions;
    private Integer clicks;
    private Double cost;
    private Double cpc;
    private Double conversions;
    private Double cpa;

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public Double getCpc() {
        return cpc;
    }

    public void setCpc(Double cpc) {
        this.cpc = cpc;
    }

    public Double getConversions() {
        return conversions;
    }

    public void setConversions(Double conversions) {
        this.conversions = conversions;
    }

    public Double getCpa() {
        return cpa;
    }

    public void setCpa(Double cpa) {
        this.cpa = cpa;
    }

    public String getHourOfDay() {
        return hourOfDay;
    }

    public void setHourOfDay(String hourOfDay) {
        this.hourOfDay = hourOfDay;
    }
    
    public Integer getImpressions() {
        return impressions;
    }

    public void setImpressions(Integer impressions) {
        this.impressions = impressions;
    }

    public Integer getClicks() {
        return clicks;
    }

    public void setClicks(Integer clicks) {
        this.clicks = clicks;
    }
}
