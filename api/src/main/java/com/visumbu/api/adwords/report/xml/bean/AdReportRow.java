/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.api.adwords.report.xml.bean;

import com.visumbu.api.utils.ApiUtils;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author duc-dev-04
 */
@XmlRootElement(name = "row")
public class AdReportRow {

    private String campaignId;
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
    private String conversions;
    private String averagePosition;
    private String allConversions;
    private String adGroupName;
    private String adGroupId;
    private String headline;
    private String adType;
    private String description;
    private String description1;
    private String description2;
    private String displayUrl;
    private String creativeFinalUrls;
    private String creativeDestinationUrl;
    private String averageCpc;
    private String ctr;
    private String cost;
    private String costPerConversion;
    private String conversionRate;

    public String getCampaignId() {
        return campaignId;
    }

    @XmlAttribute
    public void setCampaignId(String campaignId) {
        this.campaignId = campaignId;
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

    public String getConversions() {
        return conversions;
    }

    @XmlAttribute
    public void setConversions(String conversions) {
        this.conversions = conversions;
    }

    public String getAveragePosition() {
        return averagePosition;
    }

    @XmlAttribute(name = "avgPosition")
    public void setAveragePosition(String averagePosition) {
        this.averagePosition = averagePosition;
    }

    public String getAllConversions() {
        return allConversions;
    }

    @XmlAttribute(name = "allConv")
    public void setAllConversions(String allConversions) {
        this.allConversions = allConversions;
    }

    public String getAdGroupName() {
        return adGroupName;
    }

    @XmlAttribute(name = "adGroup")
    public void setAdGroupName(String adGroupName) {
        this.adGroupName = adGroupName;
    }

    public String getAdGroupId() {
        return adGroupId;
    }

    @XmlAttribute
    public void setAdGroupId(String adGroupId) {
        this.adGroupId = adGroupId;
    }

    public String getHeadline() {
        return headline;
    }

    @XmlAttribute(name = "ad")
    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public String getAdType() {
        return adType;
    }

    @XmlAttribute(name = "adType")
    public void setAdType(String adType) {
        this.adType = adType;
    }

    public String getDescription() {
        return description;
    }

    @XmlAttribute(name = "description")
    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription1() {
        return description1;
    }

    @XmlAttribute(name = "descriptionLine1")
    public void setDescription1(String description1) {
        this.description1 = description1;
    }

    public String getDescription2() {
        return description2;
    }

    @XmlAttribute(name = "descriptionLine2")
    public void setDescription2(String description2) {
        this.description2 = description2;
    }

    public String getDisplayUrl() {
        return displayUrl;
    }

    @XmlAttribute
    public void setDisplayUrl(String displayUrl) {
        this.displayUrl = displayUrl;
    }

    public String getCreativeFinalUrls() {
        return creativeFinalUrls;
    }

    @XmlAttribute(name = "finalURL")
    public void setCreativeFinalUrls(String creativeFinalUrls) {
        this.creativeFinalUrls = creativeFinalUrls;
    }

    public String getCreativeDestinationUrl() {
        return creativeDestinationUrl;
    }

    public void setCreativeDestinationUrl(String creativeDestinationUrl) {
        this.creativeDestinationUrl = creativeDestinationUrl;
    }

    public String getAverageCpc() {
        try {
            return Double.toString(Double.parseDouble(averageCpc) / 1000000L);
        } catch (Exception e) {
            return "0";
        }
    }

    @XmlAttribute(name = "avgCPC")
    public void setAverageCpc(String averageCpc) {
        this.averageCpc = averageCpc;
    }

    public String getCtr() {
        return ApiUtils.removePercent(ctr);
        //return ctr;
    }

    @XmlAttribute
    public void setCtr(String ctr) {
        this.ctr = ctr;
    }

    public String getCost() {
        try {
            return Double.toString(Double.parseDouble(cost) / 1000000L);
        } catch (Exception e) {
            return "0";
        }
    }

    @XmlAttribute
    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getCostPerConversion() {
        try {
            return Double.toString(Double.parseDouble(costPerConversion) / 1000000L);
        } catch (Exception e) {
            return "0";
        }
    }

    @XmlAttribute(name = "costConv")
    public void setCostPerConversion(String costPerConversion) {
        this.costPerConversion = costPerConversion;
    }

    public String getConversionRate() {
        return conversionRate;
    }

    @XmlAttribute
    public void setConversionRate(String conversionRate) {
        this.conversionRate = conversionRate;
    }

    @Override
    public String toString() {
        return "AdReportRow{" + "conversions=" + conversions + ", adGroupId=" + adGroupId + ", description=" + description + ", description1=" + description1 + ", description2=" + description2 + '}';
    }

    
}
