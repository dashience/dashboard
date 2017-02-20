/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.api.adwords.report.xml.bean;

import com.visumbu.api.utils.ApiUtils;
import javax.xml.bind.annotation.XmlAttribute;

/**
 *
 * @author duc-dev-04
 */
public class CallConversionReportRow {


    private String account;
    private String impressions;
    private String durationSeconds;
    private String status;
    private String callType;
    private String day;
    private String conversions;

    public String getConversions() {
        return conversions;
    }

    @XmlAttribute
    public void setConversions(String conversions) {
        this.conversions = conversions;
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


    public String getDay() {
        return day;
    }

    @XmlAttribute
    public void setDay(String day) {
        this.day = day;
    }

    public String getDurationSeconds() {
        return durationSeconds;
    }

    @XmlAttribute
    public void setDurationSeconds(String durationSeconds) {
        this.durationSeconds = durationSeconds;
    }

    public String getStatus() {
        return status;
    }

    @XmlAttribute
    public void setStatus(String status) {
        this.status = status;
    }

    public String getCallType() {
        return callType;
    }

    @XmlAttribute
    public void setCallType(String callType) {
        this.callType = callType;
    }
    
    @Override
    public String toString() {
        return "CallConversionReportRow{" + "account=" + account + ", impressions=" + impressions + ", durationSeconds=" + durationSeconds + ", status=" + status + ", callType=" + callType + ", day=" + day + '}';
    }

}
